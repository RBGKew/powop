package org.emonocot.job.grassbase;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.format.AttributeFormatter;
import au.org.ala.delta.model.format.CharacterFormatter;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.translation.ItemListTypeSetter;
import au.org.ala.delta.translation.PrintFile;
import au.org.ala.delta.translation.naturallanguage.NaturalLanguageTranslator;

/**
 *
 * @author ben
 *
 */
public class DarwinCoreNaturalLanguageTranslator extends
        NaturalLanguageTranslator {
   /**
    *
    */
    private Logger logger = LoggerFactory.getLogger(DarwinCoreNaturalLanguageTranslator.class);

    /**
     *
     */
    private Map<Integer, TaxonInfo> identifiers = new HashMap<Integer, TaxonInfo>();

    /**
     *
     */
    private Integer itemNumberOffset = null;

    /**
     *
     * @param context Set the context
     * @param typeSetter Set the type setter
     * @param printer Set the printer
     * @param itemFormatter Set the item formatter
     * @param characterFormatter Set the character formatter
     * @param attributeFormatter Set the attribute formatter
     * @param identifiers2 Set the identifiers
     */
    public DarwinCoreNaturalLanguageTranslator(final DeltaContext context,
            final ItemListTypeSetter typeSetter, final PrintFile printer,
            final ItemFormatter itemFormatter,
            final CharacterFormatter characterFormatter,
            final AttributeFormatter attributeFormatter,
            final Map<Integer, TaxonInfo> identifiers2) {
        super(context, typeSetter, printer, itemFormatter,
                characterFormatter, attributeFormatter);
        this.identifiers = identifiers2;
    }

    @Override
    protected final void printItemHeading(final Item item) {
        TaxonInfo taxonInfo = identifiers.get(item.getItemNumber() + itemNumberOffset - 1);
        logger.debug("ITEM_NUMBER " + (item.getItemNumber() - 1) + " TAXON_NAME "  + taxonInfo.getName());

        String checklistIdentifier = null;

        if (taxonInfo.getChecklistId() == null) {
            checklistIdentifier = "";
        } else {
            checklistIdentifier = "urn:kew.org:wcs:taxon:"
                    + taxonInfo.getChecklistId();
        }

        String itemLink = null;
        if (taxonInfo.getLink() != null) {
            itemLink = "http://www.kew.org/data/grasses-db/www/"
                    + taxonInfo.getLink() + ".htm";
        } else {
            itemLink = "";
        }

        String heading = checklistIdentifier + "\t"+ itemLink + "\t"
                + itemLink + "\t";
        _typeSetter.beforeItemHeading();

        _printer.writeJustifiedText(heading,0);

        _typeSetter.afterItemHeading();
    }

    @Override
    public final void beforeItem(final Item item) {

        boolean newFile = _context.getOutputFileSelector()
                .createNewFileIfRequired(item);
        if (newFile) {
            _typeSetter.beforeFirstItem();
        }
        _typeSetter.beforeItem(item);

        printItemHeading(item);
    }

    /**
     *
     * @param offset Set the item number offset
     */
    public final void setItemNumberOffset(final Integer offset) {
        this.itemNumberOffset = offset;
    }

    /**
     *
     * @return the item number offset
     */
    public final Integer getItemNumberOffset() {
        return itemNumberOffset;
    }
}
