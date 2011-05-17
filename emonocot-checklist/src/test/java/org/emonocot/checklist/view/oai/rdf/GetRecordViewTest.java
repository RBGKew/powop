package org.emonocot.checklist.view.oai.rdf;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import org.emonocot.checklist.model.ChangeEventImpl;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.Distribution;
import org.emonocot.checklist.model.IdentifiableEntity;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.Region;
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

    /**
     *
     */
    public GetRecordViewTest() {
        super.setValidateAgainstSchemas(false);
    }

    @Override
    public final void doSetup(final Map model) {
        Request request = new Request();
        request.setVerb(Verb.GetRecord);
        request.setValue("http://www.example.com/oai");
        model.put("request", request);

        Taxon taxon = new Taxon();

        taxon.setIdentifier("urn:lsid:grassbase.kew.org:taxon:2043");
        taxon.setName("Agrostis capillaris L.");
        taxon.setNameId("urn:lsid:ipni.org:names:385550-1:1.4");
        Set<Distribution> distribution = new HashSet<Distribution>();
        distribution.add(new Distribution(taxon, Region.NORTHERN_EUROPE,
                Country.DEN));
        distribution.add(new Distribution(taxon, Region.NORTHERN_EUROPE,
                Country.SWE));
        distribution.add(new Distribution(taxon, Region.NORTHERN_EUROPE,
                Country.NOR));
        taxon.setDistribution(distribution);

        Taxon synonym = new Taxon();
        synonym.setIdentifier("urn:lsid:grassbase.kew.org:taxon:2045");
        synonym.setName("Agrostis capillaris castellana (Boiss. & Reut.)"
                      + " O. de Bolos, R.M. Masalles & J. Vigo");

        Set<Taxon> synonyms = new HashSet<Taxon>();
        synonyms.add(synonym);

        synonym = new Taxon();
        synonym.setIdentifier("urn:lsid:grassbase.kew.org:taxon:2046");
        synonym.setName("Agrostis capillaris olivetorum (Godron)"
                      + " O. de Bolos, R.M. Masalles & J. Vigo");
        synonyms.add(synonym);
        taxon.setSynonyms(synonyms);

        model.put("object", new ChangeEventImpl<IdentifiableEntity>(taxon,
                ChangeType.CREATE, GetRecordViewTest.DATE_TIME));
    }

    @Override
    public final String getExpected() {
        return "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><?xml-stylesheet type='text/xsl' href='oai2.xsl'?><oai:OAI-PMH xmlns:oai='http://www.openarchives.org/OAI/2.0/' xmlns:oai__dc='http://www.openarchives.org/OAI/2.0/oai_dc/' xmlns:dc='http://purl.org/dc/elements/1.1/' xmlns:tc='http://rs.tdwg.org/ontology/voc/TaxonConcept#' xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' xmlns:tn='http://rs.tdwg.org/ontology/voc/TaxonName#' xmlns:owl='http://www.w3.org/2002/07/owl#' xmlns:dcterms='http://purl.org/dc/terms/' xmlns:tcom='http://rs.tdwg.org/ontology/voc/Common#' xmlns:tpc='http://rs.tdwg.org/ontology/voc/PublicationCitation#'><oai:responseDate>2011-05-17T15:34:05Z</oai:responseDate><oai:request verb='GetRecord'></oai:request><oai:GetRecord><oai:record><oai:header><oai:datestamp>2005-04-25T11:00:00Z</oai:datestamp></oai:header><oai:metadata><tc:TaxonConcept xmlns:tc='http://rs.tdwg.org/ontology/voc/TaxonConcept#'><tc:hasName><tn:TaxonName xmlns:tn='http://rs.tdwg.org/ontology/voc/TaxonName#'><dc:identifier xmlns:dc='http://purl.org/dc/elements/1.1/'>urn:lsid:ipni.org:names:385550-1:1.4</dc:identifier><tn:nameComplete>Agrostis capillaris L.</tn:nameComplete></tn:TaxonName></tc:hasName></tc:TaxonConcept></oai:metadata></oai:record></oai:GetRecord></oai:OAI-PMH>";
    }

    @Override
    public final AbstractOaiPmhResponseView setView() {
        return new GetRecordView();
    }

}
