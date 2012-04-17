package org.emonocot.job.grassbase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.directives.ConforDirectiveFileParser;
import au.org.ala.delta.directives.ConforDirectiveParserObserver;
import au.org.ala.delta.model.format.AttributeFormatter;
import au.org.ala.delta.model.format.CharacterFormatter;
import au.org.ala.delta.model.format.Formatter.AngleBracketHandlingMode;
import au.org.ala.delta.model.format.Formatter.CommentStrippingMode;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.translation.AbstractDataSetTranslator;
import au.org.ala.delta.translation.DataSetFilter;
import au.org.ala.delta.translation.ItemListTypeSetter;
import au.org.ala.delta.translation.PrintFile;
import au.org.ala.delta.translation.naturallanguage.NaturalLanguageDataSetFilter;

/**
 *
 * @author ben
 *
 */
public class GrassbaseDwcProcessor {
    /**
     *
     */
    private static final Logger logger= LoggerFactory.getLogger(GrassbaseDwcProcessor.class);
    /**
     *
     */
    private static final int BREAK_ON_ITEMS = 10;

    /**
     *  "387328,'Aciachne acicularis','imp00000'".
     */
    private Pattern identifierPattern = Pattern.compile("(\\d+),'(imp\\d+)','([\\w-]+ [\\w-]+) .*?',+");

    /**
     *
     */
    private Map<String, TaxonInfo> identifiers = new HashMap<String, TaxonInfo>();

    /**
     *
     */
    private Map<Integer,TaxonInfo> taxonIdentifiers = new HashMap<Integer,TaxonInfo>();

    /**
     *
     * @param path the path to the identifiers file
     * @throws Exception if there is a problem
     */
    public final void processIdentifiers(final String path) throws Exception {
        File identifierFile = new File(path);
        Scanner scanner = new Scanner(identifierFile);
        scanner.useDelimiter("\n");

        while (scanner.hasNext()) {
            String line = scanner.next();
            logger.trace("LINE " + line); 
            line = line.substring(0, line.length() - 1);
            Matcher identifierMatcher = identifierPattern.matcher(line);
            if (identifierMatcher.matches()) {
                Integer checklistId = Integer.parseInt(identifierMatcher
                        .group(1));
                String name = identifierMatcher.group(3);

                String link = identifierMatcher.group(2);
                TaxonInfo taxonInfo = new TaxonInfo(checklistId, name, link);
                identifiers.put(name, taxonInfo);
            } else {
                logger.warn("LINE DOESNT MATCH " + line);
            }
        }
        logger.debug(identifiers.keySet().size() + " identifiers parsed");
    }

    /**
     * Reads in specs/chars/items from the simple test data set but no other
     * configuration. Test cases can manually configure the DeltaContext before
     * doing the translation.
     *
     * @param path
     *            Set the path
     * @param deltaContext
     *            Set the context
     * @throws Exception
     *             if there was an error reading the input files.
     */
    protected final void initialiseContext(final String path,
            final DeltaContext deltaContext) throws Exception {

        File specs = new File(path);

        ConforDirectiveFileParser parser = ConforDirectiveFileParser
                .createInstance();
        ConforDirectiveParserObserver conforObserver
            = new ConforDirectiveParserObserver(deltaContext);
        parser.registerObserver(conforObserver);
        parser.parse(specs, deltaContext);

        conforObserver.finishedProcessing();
    }

    /**
     * Process the files.
     * @param path Set the path
     * @param outputFileName Set the output file name
     * @throws Exception if there is a problem
     */
    public final void process(final String path, final String outputFileName)
            throws Exception {
        PrintStream pout = new PrintStream(
                new FileOutputStream(outputFileName), false, "UTF-8");
        int breakOnItems = BREAK_ON_ITEMS;
        String preamble = "*SHOW: World Grasses - items.\n*ITEM DESCRIPTIONS\n\n";
        int fileCounter = 0;
        int itemCounter = 0;
        File input = new File(path + "/ITEMS");
        File orig = new File(input.getCanonicalPath() + ".orig");
        input.renameTo(orig);
        String fileName = input.getCanonicalPath() + fileCounter;
        Writer out = new OutputStreamWriter(new FileOutputStream(input),
                "UTF-8");
        Scanner scanner = new Scanner(orig);
        scanner.useDelimiter("#");

        /**
         * Skip the preamble at the top of the file
         */
        scanner.next();
        /**
         * Loop over the items
         */
        while (scanner.hasNext()) {
            logger.info("Item " + itemCounter);
            if ((itemCounter % breakOnItems) == 0) {
                logger.debug("Writing Preamble");
                out.write(preamble);
            }

            String next = scanner.next();
            String name = next.substring(0, next.indexOf("/")).trim();
            logger.debug(name);
            setItemId(itemCounter, name);
            out.write("#" + next);
            itemCounter++;

            if ((itemCounter % breakOnItems) == 0) {

                doProcess(out, path, pout, itemCounter - breakOnItems);
                fileCounter++;
                File output = new File(input.getCanonicalPath() + fileCounter);
                input.renameTo(output);
                out = new OutputStreamWriter(new FileOutputStream(input),
                        "UTF-8");

            }
        }
        /**
         * There are any items remaining at the end of the processing.
         * i.e. if # items does not divide evenly into breakOnItems
         *
         */
        if ((itemCounter % breakOnItems) != 0) {
            doProcess(out, path, pout, itemCounter
                    - (itemCounter % breakOnItems));
        }
        File output = new File(input.getCanonicalPath() + fileCounter);
        input.renameTo(output);

    }

    /**
     *
     * @param out Set the output writer
     * @param path Set the path
     * @param pout Set the print stream
     * @param itemOffset Set the item offset
     * @throws Exception if there is a problem
     */
    private void doProcess(final Writer out, final String path,
            final PrintStream pout, final Integer itemOffset) throws Exception {
        out.flush();
        out.close();
        PrintFile printer = new PrintFile(pout, 0);
        printer.setTrimInput(false);
        ItemListTypeSetter typeSetter = new DarwinCoreTypeSetter(printer);
        DeltaContext deltaContext = new DeltaContext();
        ItemFormatter itemFormatter = new ItemFormatter(false,
                CommentStrippingMode.RETAIN, AngleBracketHandlingMode.RETAIN,
                true, false, false);
        CharacterFormatter characterFormatter = new CharacterFormatter(false,
                CommentStrippingMode.STRIP_ALL,
                AngleBracketHandlingMode.RETAIN, true, false);
        AttributeFormatter attributeFormatter = new AttributeFormatter(false,
                true, CommentStrippingMode.RETAIN);
        DataSetFilter filter = new NaturalLanguageDataSetFilter(deltaContext);
        DarwinCoreNaturalLanguageTranslator translator = new DarwinCoreNaturalLanguageTranslator(
                deltaContext, typeSetter, printer, itemFormatter,
                characterFormatter, attributeFormatter, taxonIdentifiers);
        AbstractDataSetTranslator dataSetTranslator = new AbstractDataSetTranslator(
                deltaContext, filter, translator);

        initialiseContext(path + "/TONAT", deltaContext);
        translator.setItemNumberOffset(itemOffset);
        dataSetTranslator.translateItems();

        pout.flush();
    }

    /**
     *
     * @param itemNumber Set the item number
     * @param name Set the name
     */
    private void setItemId(final Integer itemNumber, final String name) {
        TaxonInfo taxonInfo = this.identifiers.get(name);
        if (taxonInfo == null) {
            logger.warn("Could not find TaxonInfo for " + name);
            taxonInfo = new TaxonInfo(null, name, null);
            taxonIdentifiers.put(itemNumber, taxonInfo);
        } else {
            taxonInfo.setItemId(itemNumber);
            taxonIdentifiers.put(itemNumber, taxonInfo);
        }
    }


}
