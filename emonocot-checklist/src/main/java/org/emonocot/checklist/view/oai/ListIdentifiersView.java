package org.emonocot.checklist.view.oai;

import java.util.Map;

import org.emonocot.checklist.controller.oai.AbstractOaiPmhController;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.model.pager.Page;
import org.openarchives.pmh.Header;
import org.openarchives.pmh.ListIdentifiers;
import org.openarchives.pmh.OAIPMH;
import org.openarchives.pmh.ResumptionToken;

/**
 *
 * @author ben
 *
 */
public class ListIdentifiersView extends AbstractOaiPmhResponseView {

    /**
     * @param oaiPmh The oai pmh response to use
     * @param model The model map to merge in
     */
    protected final void constructResponse(final OAIPMH oaiPmh,
            final Map<String, Object> model) {
        ListIdentifiers listIdentifiers = new ListIdentifiers();

        for (ChangeEvent changeEvent : ((Page<ChangeEvent>) model
                .get(AbstractOaiPmhController.OBJECT_KEY)).getRecords()) {
            listIdentifiers.getHeader().add(
                    getMapper().map(changeEvent, Header.class));
        }

        if (model.containsKey(AbstractOaiPmhController.RESUMPTION_TOKEN_KEY)) {
            listIdentifiers.setResumptionToken((ResumptionToken) model
                    .get(AbstractOaiPmhController.RESUMPTION_TOKEN_KEY));
        }

        oaiPmh.setListIdentifiers(listIdentifiers);
    }
}