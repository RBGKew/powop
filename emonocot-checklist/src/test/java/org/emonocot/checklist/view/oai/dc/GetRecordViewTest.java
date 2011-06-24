package org.emonocot.checklist.view.oai.dc;

import java.util.Map;

import org.joda.time.DateTime;
import org.emonocot.checklist.model.ChangeEventImpl;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.IdentifiableEntity;
import org.emonocot.checklist.test.DummyIdentifiableEntity;
import org.emonocot.checklist.view.oai.AbstractOaiPmhResponseView;
import org.emonocot.checklist.view.oai.AbstractOaiPmhViewTestCase;
import org.openarchives.pmh.Request;
import org.openarchives.pmh.Verb;

/**
 *
 * @author ben
 *
 */
public class GetRecordViewTest extends AbstractOaiPmhViewTestCase {

    /**
     *
     */
    private static final DateTime DATE_TIME
        = new DateTime(2005, 04, 25, 12, 0, 0, 0);

    @Override
    public final void doSetup(final Map model) {
        Request request = new Request();
        request.setVerb(Verb.GetRecord);
        request.setValue("http://www.example.com/oai");
        model.put("request", request);

        IdentifiableEntity entity = new DummyIdentifiableEntity();
        entity.setIdentifier("urn:scheme:identifier");

        model.put("object", new ChangeEventImpl<IdentifiableEntity>(entity,
                ChangeType.CREATE, GetRecordViewTest.DATE_TIME));
    }

    @Override
    public final String getExpected() {
        return "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><?xml-stylesheet type='text/xsl' href='oai2.xsl'?><oai:OAI-PMH xmlns:oai='http://www.openarchives.org/OAI/2.0/' xmlns:oai__dc='http://www.openarchives.org/OAI/2.0/oai_dc/' xmlns:dc='http://purl.org/dc/elements/1.1/' xmlns:tc='http://rs.tdwg.org/ontology/voc/TaxonConcept#' xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' xmlns:tn='http://rs.tdwg.org/ontology/voc/TaxonName#' xmlns:owl='http://www.w3.org/2002/07/owl#' xmlns:dcterms='http://purl.org/dc/terms/' xmlns:tcom='http://rs.tdwg.org/ontology/voc/Common#' xmlns:tpc='http://rs.tdwg.org/ontology/voc/PublicationCitation#' xmlns:tp='http://rs.tdwg.org/ontology/voc/Person#' xmlns:tt='http://rs.tdwg.org/ontology/voc/Team#' xmlns:spm='http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#' xmlns:spmi='http://rs.tdwg.org/ontology/voc/SPMInfoItems#' xmlns:gr='http://rs.tdwg.org/ontology/voc/GeographicRegion#'><oai:responseDate>2011-05-20T13:49:55Z</oai:responseDate><oai:request verb='GetRecord'></oai:request><oai:GetRecord><oai:record><oai:header><oai:identifier>urn:scheme:identifier</oai:identifier><oai:datestamp>2005-04-25T11:00:00Z</oai:datestamp></oai:header><oai:metadata><oai_dc:dc xmlns:oai_dc='http://www.openarchives.org/OAI/2.0/oai_dc/'><dc:identifier xmlns:dc='http://purl.org/dc/elements/1.1/'>urn:scheme:identifier</dc:identifier></oai_dc:dc></oai:metadata></oai:record></oai:GetRecord></oai:OAI-PMH>";
    }

    @Override
    public final AbstractOaiPmhResponseView setView() {
        return new GetRecordView();
    }

}
