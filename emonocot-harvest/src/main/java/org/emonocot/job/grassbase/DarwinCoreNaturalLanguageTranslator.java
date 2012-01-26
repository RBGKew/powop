package org.emonocot.job.grassbase;

import java.util.HashMap;
import java.util.Map;

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
    private Map<Integer, Integer> identifiers = new HashMap<Integer, Integer>();

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
     * @param newIdentifiers Set the identifiers
     */
    public DarwinCoreNaturalLanguageTranslator(final DeltaContext context,
            final ItemListTypeSetter typeSetter, final PrintFile printer,
            final ItemFormatter itemFormatter,
            final CharacterFormatter characterFormatter,
            final AttributeFormatter attributeFormatter,
            final Map<Integer, Integer> newIdentifiers) {
        super(context, typeSetter, printer, itemFormatter,
                characterFormatter, attributeFormatter);
        this.identifiers = newIdentifiers;
    }

    @Override
    protected final void printItemHeading(final Item item) {
        Integer checklistId = identifiers.get(item.getItemNumber()
                + itemNumberOffset);

        String checklistIdentifier = null;

        if (checklistId == null) {
            checklistIdentifier = "";
        } else {
            checklistIdentifier = "urn:kew.org:wcs:taxon:" + checklistId;
        }

        String itemLink = "http://www.kew.org/data/grasses-db/www/imp"
                + String.format("%05d", itemNumberOffset + item.getItemNumber())
                + ".htm";
        String heading = checklistIdentifier + "\t"
                + itemLink + "\t";
        _typeSetter.beforeItemHeading();

        _printer.writeJustifiedText(heading, -1);

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
