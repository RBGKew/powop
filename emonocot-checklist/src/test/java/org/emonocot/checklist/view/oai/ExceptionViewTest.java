package org.emonocot.checklist.view.oai;

import java.util.Map;

import org.openarchives.pmh.Error;
import org.openarchives.pmh.ErrorCode;
import org.openarchives.pmh.Request;
import org.openarchives.pmh.Verb;

/**
 *
 * @author ben
 *
 */
public class ExceptionViewTest extends AbstractOaiPmhViewTestCase {

    @Override
    public final void doSetup(final Map model) {
        Request request = new Request();
        request.setVerb(Verb.ListIdentifiers);
        request.setValue("http://www.example.com/oai");
        request.setResumptionToken("resumptionToken");
        model.put("request", request);

        Error error = new Error();
        error.setCode(ErrorCode.badResumptionToken);
        error.setValue("Bad Resumption Token");
        model.put("object", error);
    }

    @Override
    public final String getExpected() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><oai:OAI-PMH xmlns:oai=\"http://www.openarchives.org/OAI/2.0/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:owl=\"http://www.w3.org/2002/07/owl#\" xmlns:dcterms=\"http://purl.org/dc/terms/\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:tcom=\"http://rs.tdwg.org/ontology/voc/Common#\" xmlns:tpc=\"http://rs.tdwg.org/ontology/voc/PublicationCitation#\" xmlns:tc=\"http://rs.tdwg.org/ontology/voc/TaxonConcept#\" xmlns:tp=\"http://rs.tdwg.org/ontology/voc/Person#\" xmlns:tt=\"http://rs.tdwg.org/ontology/voc/Team#\" xmlns:tn=\"http://rs.tdwg.org/ontology/voc/TaxonName#\" xmlns:spm=\"http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#\" xmlns:gr=\"http://rs.tdwg.org/ontology/voc/GeographicRegion#\" xmlns:spmi=\"http://rs.tdwg.org/ontology/voc/SPMInfoItems#\"><oai:responseDate>2010-06-18T11:36:00.568+01:00</oai:responseDate><oai:request verb=\"ListIdentifiers\" resumptionToken=\"resumptionToken\"></oai:request><oai:error code=\"badResumptionToken\">Bad Resumption Token</oai:error></oai:OAI-PMH>";
    }

    @Override
    public final AbstractOaiPmhResponseView setView() {
        return new ExceptionView();
    }

}
