package org.emonocot.checklist.view.oai;

import java.util.Map;

import org.emonocot.checklist.controller.oai.AbstractOaiPmhController;
import org.openarchives.pmh.Error;
import org.openarchives.pmh.OAIPMH;

/**
 *
 * @author ben
 *
 */
public class ExceptionView extends AbstractOaiPmhResponseView {

    @Override
    protected final void constructResponse(final OAIPMH oaiPmh,
            final Map<String, Object> model) {
        oaiPmh.setError((Error) model.get(AbstractOaiPmhController.OBJECT_KEY));
    }
}
