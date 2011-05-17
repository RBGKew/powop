package org.emonocot.checklist.view.oai;

import java.util.List;
import java.util.Map;

import org.emonocot.checklist.controller.oai.AbstractOaiPmhController;
import org.openarchives.pmh.ListMetadataFormats;
import org.openarchives.pmh.MetadataFormat;
import org.openarchives.pmh.OAIPMH;

/**
 *
 * @author ben
 *
 */
public class ListMetadataFormatsView extends AbstractOaiPmhResponseView {

    /**
     * @param oaiPmh
     *            The oai pmh response to use
     * @param model
     *            The model map to merge in
     */
    protected final void constructResponse(final OAIPMH oaiPmh,
            final Map<String, Object> model) {

        ListMetadataFormats listMetadataFormats = new ListMetadataFormats();
        List<MetadataFormat> metadataFormats = (List<MetadataFormat>) model
                .get(AbstractOaiPmhController.OBJECT_KEY);

        for (MetadataFormat metadataFormat : metadataFormats) {
            listMetadataFormats.getMetadataFormat().add(metadataFormat);
        }

        oaiPmh.setListMetadataFormats(listMetadataFormats);
    }
}