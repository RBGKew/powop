package org.emonocot.checklist.view.oai;

import java.util.Map;
import java.util.Set;

import org.emonocot.checklist.controller.oai.AbstractOaiPmhController;
import org.openarchives.pmh.ListSets;
import org.openarchives.pmh.OAIPMH;

/**
 *
 * @author ben
 *
 */
public class ListSetsView extends AbstractOaiPmhResponseView {

    /**
     * @param oaiPmh
     *            The oai pmh response to use
     * @param model
     *            The model map to merge in
     */
    protected final void constructResponse(final OAIPMH oaiPmh,
            final Map<String, Object> model) {
        ListSets listSets = new ListSets();

        for (org.openarchives.pmh.Set set :
                (Set<org.openarchives.pmh.Set>) model
                .get(AbstractOaiPmhController.OBJECT_KEY)) {
            listSets.getSet().add(set);
        }
        oaiPmh.setListSets(listSets);
    }
}