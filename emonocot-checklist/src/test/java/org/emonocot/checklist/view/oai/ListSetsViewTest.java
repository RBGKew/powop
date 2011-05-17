package org.emonocot.checklist.view.oai;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openarchives.pmh.Request;
import org.openarchives.pmh.Verb;

/**
 *
 * @author ben
 *
 */
public class ListSetsViewTest extends AbstractOaiPmhViewTestCase {

    @Override
    public final void doSetup(final Map model) {
        Request request = new Request();
        request.setVerb(Verb.ListSets);
        request.setValue("http://www.example.com/oai");
        model.put("request", request);

        Set<org.openarchives.pmh.Set> sets
            = new HashSet<org.openarchives.pmh.Set>();
        org.openarchives.pmh.Set set = new org.openarchives.pmh.Set();
        set.setSetName("Electronic Music Collection");
        set.setSetSpec("music:(elec)");
        sets.add(set);

        model.put("object", sets);
    }

    @Override
    public final String getExpected() {
        return "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><?xml-stylesheet type='text/xsl' href='oai2.xsl'?><oai:OAI-PMH xmlns:oai='http://www.openarchives.org/OAI/2.0/' xmlns:oai__dc='http://www.openarchives.org/OAI/2.0/oai_dc/' xmlns:dc='http://purl.org/dc/elements/1.1/'><oai:responseDate>2011-05-16T15:05:12Z</oai:responseDate><oai:request verb='ListSets'></oai:request><oai:ListSets><oai:set><oai:setSpec>music:(elec)</oai:setSpec><oai:setName>Electronic Music Collection</oai:setName></oai:set></oai:ListSets></oai:OAI-PMH>";
    }

    @Override
    public final AbstractOaiPmhResponseView setView() {
        return new ListSetsView();
    }

}
