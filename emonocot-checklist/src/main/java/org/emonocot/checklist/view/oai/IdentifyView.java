package org.emonocot.checklist.view.oai;

import java.util.Map;

import org.emonocot.checklist.controller.oai.AbstractOaiPmhController;
import org.openarchives.pmh.Identify;
import org.openarchives.pmh.OAIPMH;

/**
 *
 * @author ben
 *
 */
public class IdentifyView extends AbstractOaiPmhResponseView {

    @Override
    protected final void constructResponse(final OAIPMH oaiPmh,
            final Map<String, Object> model) {
        oaiPmh.setIdentify((Identify) model
                .get(AbstractOaiPmhController.OBJECT_KEY));
    }
}
