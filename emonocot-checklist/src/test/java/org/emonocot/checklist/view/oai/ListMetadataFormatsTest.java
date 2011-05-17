package org.emonocot.checklist.view.oai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openarchives.pmh.MetadataFormat;
import org.openarchives.pmh.MetadataPrefix;
import org.openarchives.pmh.Request;
import org.openarchives.pmh.Verb;

/**
 *
 * @author ben
 *
 */
public class ListMetadataFormatsTest extends AbstractOaiPmhViewTestCase {

    @Override
    public final void doSetup(final Map model) {

        List<MetadataFormat> metadataFormats = new ArrayList<MetadataFormat>();
        MetadataFormat oaiDc = new MetadataFormat();
        oaiDc.setMetadataPrefix(MetadataPrefix.OAI_DC);
        oaiDc.setSchema("http://www.openarchives.org/OAI/2.0/oai_dc.xsd");
        oaiDc.setMetadataNamespace(
                "http://www.openarchives.org/OAI/2.0/oai_dc/");
        metadataFormats.add(oaiDc);

        model.put("object", metadataFormats);

        Request request = new Request();
        request.setVerb(Verb.ListMetadataFormats);
        request.setValue("http://www.example.com/oai");
        model.put("request", request);
    }

    @Override
    public final String getExpected() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><oai:OAI-PMH xmlns:oai=\"http://www.openarchives.org/OAI/2.0/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:owl=\"http://www.w3.org/2002/07/owl#\" xmlns:dcterms=\"http://purl.org/dc/terms/\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:tcom=\"http://rs.tdwg.org/ontology/voc/Common#\" xmlns:tpc=\"http://rs.tdwg.org/ontology/voc/PublicationCitation#\" xmlns:tc=\"http://rs.tdwg.org/ontology/voc/TaxonConcept#\" xmlns:tp=\"http://rs.tdwg.org/ontology/voc/Person#\" xmlns:tt=\"http://rs.tdwg.org/ontology/voc/Team#\" xmlns:tn=\"http://rs.tdwg.org/ontology/voc/TaxonName#\" xmlns:spm=\"http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#\" xmlns:gr=\"http://rs.tdwg.org/ontology/voc/GeographicRegion#\" xmlns:spmi=\"http://rs.tdwg.org/ontology/voc/SPMInfoItems#\"><oai:responseDate>2010-06-18T11:41:59.947+01:00</oai:responseDate><oai:request verb=\"ListMetadataFormats\"></oai:request><oai:ListMetadataFormats><oai:metadataFormat><oai:metadataPrefix>OAI_DC</oai:metadataPrefix><oai:schema>http://www.openarchives.org/OAI/2.0/oai_dc.xsd</oai:schema><oai:metadataNamespace>http://www.openarchives.org/OAI/2.0/oai_dc/</oai:metadataNamespace></oai:metadataFormat></oai:ListMetadataFormats></oai:OAI-PMH>";
    }

    @Override
    public final AbstractOaiPmhResponseView setView() {
        return new ListMetadataFormatsView();
    }

}
