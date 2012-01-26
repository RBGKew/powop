package org.emonocot.job.grassbase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
import au.org.ala.delta.translation.IterativeTranslator;
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
     *
     */
    private static final int ITEM_ID_ENDS = 16;

    /**
     *
     */
    private static final int ITEM_ID_BEGINS = 11;
    /**
     *
     */
    private static final int CHECKLIST_ID_ENDS = 6;
    /**
     *
     */
    private static final int CHECKLIST_ID_BEGINS = 0;
    /**
     *
     */
    private Map<Integer, Integer> identifiers = new HashMap<Integer, Integer>();

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
            Integer checklistId = Integer.parseInt(line.substring(
                    CHECKLIST_ID_BEGINS, CHECKLIST_ID_ENDS));
            Integer itemId = Integer.parseInt(line.substring(ITEM_ID_BEGINS,
                    ITEM_ID_ENDS));
            identifiers.put(itemId, checklistId);
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

        while (scanner.hasNext()) {
            if ((itemCounter % breakOnItems) == 0) {
                out.write(preamble);
            }
            if (itemCounter > 0) {
                out.write("#" + scanner.next());
            } else {
                scanner.next();
            }
            itemCounter++;
            if ((itemCounter % breakOnItems) == 0) {
                out.flush();
                out.close();
                PrintFile printer = new PrintFile(pout, 0);
                ItemListTypeSetter typeSetter = new DarwinCoreTypeSetter(
                        printer);
                DeltaContext deltaContext = new DeltaContext();
                ItemFormatter itemFormatter = new ItemFormatter(false,
                        CommentStrippingMode.RETAIN,
                        AngleBracketHandlingMode.RETAIN, true, false, false);
                CharacterFormatter characterFormatter = new CharacterFormatter(
                        false, CommentStrippingMode.STRIP_ALL,
                        AngleBracketHandlingMode.RETAIN, true, false);
                AttributeFormatter attributeFormatter = new AttributeFormatter(
                        false, true, CommentStrippingMode.RETAIN);
                DataSetFilter filter = new NaturalLanguageDataSetFilter(
                        deltaContext);
                DarwinCoreNaturalLanguageTranslator translator
                    = new DarwinCoreNaturalLanguageTranslator(
                        deltaContext, typeSetter, printer, itemFormatter,
                        characterFormatter, attributeFormatter, identifiers);
                AbstractDataSetTranslator dataSetTranslator = new AbstractDataSetTranslator(
                        deltaContext, filter, translator);

                initialiseContext(path + "/TONAT", deltaContext);
                translator.setItemNumberOffset(itemCounter - breakOnItems - 1);
                dataSetTranslator.translateItems();

                pout.flush();

                fileCounter++;
                File output = new File(input.getCanonicalPath() + fileCounter);
                input.renameTo(output);
                out = new OutputStreamWriter(new FileOutputStream(input),
                        "UTF-8");

            }
        }
        out.flush();
        out.close();
        File output = new File(input.getCanonicalPath() + fileCounter);
        input.renameTo(output);

    }
}
