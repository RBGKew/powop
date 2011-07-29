package org.emonocot.checklist.view.oai.rdf;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import org.emonocot.checklist.model.Article;
import org.emonocot.checklist.model.Author;
import org.emonocot.checklist.model.AuthorType;
import org.emonocot.checklist.model.ChangeEventImpl;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.Distribution;
import org.emonocot.checklist.model.IdentifiableEntity;
import org.emonocot.checklist.model.Protologue;
import org.emonocot.checklist.model.Publication;
import org.emonocot.checklist.model.PublicationType;
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

        taxon.setIdentifier("urn:kew.org:wcs:taxon:2043");
        taxon.setName("Agrostis capillaris");
        taxon.setNameId("urn:lsid:ipni.org:names:385550-1:1.4");
        taxon.getAuthors().put(AuthorType.PRM, new Author("L."));
        taxon.setProtologueAuthor("Linnaeus");
        taxon.setPublicationDate("1753");
        taxon.setVolumeAndPage("1: 62.");
        taxon.setProtologue(new Protologue("Sp. Pl.", 1));
        Set<Distribution> distribution = new HashSet<Distribution>();
        distribution.add(new Distribution(taxon, Region.NORTHERN_EUROPE,
                Country.DEN));
        distribution.add(new Distribution(taxon, Region.NORTHERN_EUROPE,
                Country.SWE));
        distribution.add(new Distribution(taxon, Region.NORTHERN_EUROPE,
                Country.NOR));
        taxon.setDistribution(distribution);

        Publication publication = new Publication("Sed Vitae",
                "Vestibulum et sem ut quam elementum tincidunt sit amet ",
                "Suspendisse", PublicationType.JOURNAL, 1);

        taxon.getCitations().add(
            new Article("Mauris Tincidunt",
                        "Morbi a facilisis orci", publication, 2));

        Taxon synonym = new Taxon();
        synonym.setIdentifier("urn:kew.org:wcs:taxon:2045");
        synonym.setName("Agrostis capillaris castellana (Boiss. & Reut.)"
                      + " O. de Bolos, R.M. Masalles & J. Vigo");

        Set<Taxon> synonyms = new HashSet<Taxon>();
        synonyms.add(synonym);

        synonym = new Taxon();
        synonym.setIdentifier("urn:kew.org:wcs:taxon:2046");
        synonym.setName("Agrostis capillaris olivetorum (Godron)"
                      + " O. de Bolos, R.M. Masalles & J. Vigo");
        synonyms.add(synonym);
        taxon.setSynonyms(synonyms);

        model.put("object", new ChangeEventImpl<IdentifiableEntity>(taxon,
                ChangeType.CREATE, GetRecordViewTest.DATE_TIME));
    }

    @Override
    public final String getExpected() {
        return "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><?xml-stylesheet type='text/xsl' href='oai2.xsl'?><oai:OAI-PMH xmlns:oai='http://www.openarchives.org/OAI/2.0/' xmlns:oai__dc='http://www.openarchives.org/OAI/2.0/oai_dc/' xmlns:dc='http://purl.org/dc/elements/1.1/' xmlns:tc='http://rs.tdwg.org/ontology/voc/TaxonConcept#' xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' xmlns:tn='http://rs.tdwg.org/ontology/voc/TaxonName#' xmlns:owl='http://www.w3.org/2002/07/owl#' xmlns:dcterms='http://purl.org/dc/terms/' xmlns:tcom='http://rs.tdwg.org/ontology/voc/Common#' xmlns:tpub='http://rs.tdwg.org/ontology/voc/PublicationCitation#' xmlns:tp='http://rs.tdwg.org/ontology/voc/Person#' xmlns:tt='http://rs.tdwg.org/ontology/voc/Team#' xmlns:spm='http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#' xmlns:spmi='http://rs.tdwg.org/ontology/voc/SPMInfoItems#' xmlns:gr='http://rs.tdwg.org/ontology/voc/GeographicRegion#'><oai:responseDate>2011-07-28T10:35:54Z</oai:responseDate><oai:request verb='GetRecord'></oai:request><oai:GetRecord><oai:record><oai:header><oai:identifier>urn:kew.org:wcs:taxon:2043</oai:identifier><oai:datestamp>2005-04-25T11:00:00Z</oai:datestamp></oai:header><oai:metadata><tc:TaxonConcept xmlns:tc='http://rs.tdwg.org/ontology/voc/TaxonConcept#'><dc:identifier xmlns:dc='http://purl.org/dc/elements/1.1/'>urn:kew.org:wcs:taxon:2043</dc:identifier><tcom:publishedInCitation xmlns:tcom='http://rs.tdwg.org/ontology/voc/Common#'><tpub:PublicationCitation xmlns:tpub='http://rs.tdwg.org/ontology/voc/PublicationCitation#'><dc:identifier>urn:kew.org:wcs:publicationEdition:2</dc:identifier><tpub:title>Morbi a facilisis orci</tpub:title><tpub:authorship>Mauris Tincidunt</tpub:authorship><tpub:parentPublication><tpub:PublicationCitation><dc:identifier>urn:kew.org:wcs:publication:1</dc:identifier><tpub:title>Sed Vitae</tpub:title><tpub:authorship>Vestibulum et sem ut quam elementum tincidunt sit amet </tpub:authorship><tpub:publisher>Suspendisse</tpub:publisher><tpub:publicationType rdf:resource='http://rs.tdwg.org/ontology/voc/PublicationCitation#Journal'></tpub:publicationType></tpub:PublicationCitation></tpub:parentPublication><tpub:publicationType rdf:resource='http://rs.tdwg.org/ontology/voc/PublicationCitation#JournalArticle'></tpub:publicationType></tpub:PublicationCitation></tcom:publishedInCitation><tc:hasName><tn:TaxonName xmlns:tn='http://rs.tdwg.org/ontology/voc/TaxonName#'><dc:identifier>urn:lsid:ipni.org:names:385550-1:1.4</dc:identifier><tcom:publishedInCitation><tpub:PublicationCitation xmlns:tpub='http://rs.tdwg.org/ontology/voc/PublicationCitation#'><dc:identifier>urn:kew.org:wcs:placeOfPublication:1</dc:identifier><tpub:title>Sp. Pl.</tpub:title><tpub:authorship>Linnaeus</tpub:authorship><tpub:publicationType rdf:resource='http://rs.tdwg.org/ontology/voc/PublicationCitation#Generic'></tpub:publicationType><tpub:volume>1</tpub:volume><tpub:pages> 62.</tpub:pages><tpub:datePublished>1753</tpub:datePublished></tpub:PublicationCitation></tcom:publishedInCitation><tn:authorship>L.</tn:authorship><tn:combinationAuthorship>L.</tn:combinationAuthorship><tn:nameComplete>Agrostis capillaris</tn:nameComplete><tn:rank rdf:resource='http://rs.tdwg.org/ontology/voc/TaxonRank#Species'></tn:rank><tn:rankString>Species</tn:rankString></tn:TaxonName></tc:hasName><tc:hasRelationship><tc:Relationship><tc:relationshipCategory rdf:resource='http://rs.tdwg.org/ontology/voc/TaxonConcept#HasSynonym'></tc:relationshipCategory><tc:toTaxon rdf:resource='urn:kew.org:wcs:taxon:2045'></tc:toTaxon></tc:Relationship></tc:hasRelationship><tc:hasRelationship><tc:Relationship><tc:relationshipCategory rdf:resource='http://rs.tdwg.org/ontology/voc/TaxonConcept#HasSynonym'></tc:relationshipCategory><tc:toTaxon rdf:resource='urn:kew.org:wcs:taxon:2046'></tc:toTaxon></tc:Relationship></tc:hasRelationship><tc:describedBy><spm:SpeciesProfileModel xmlns:spm='http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#'><spm:aboutTaxon rdf:resource='urn:kew.org:wcs:taxon:2043'></spm:aboutTaxon><spm:hasInformation><spmi:Distribution xmlns:spmi='http://rs.tdwg.org/ontology/voc/SPMInfoItems#'><spm:hasValue rdf:resource='http://rs.tdwg.org/ontology/voc/GeographicRegion.rdf#DEN'></spm:hasValue></spmi:Distribution></spm:hasInformation><spm:hasInformation><spmi:Distribution xmlns:spmi='http://rs.tdwg.org/ontology/voc/SPMInfoItems#'><spm:hasValue rdf:resource='http://rs.tdwg.org/ontology/voc/GeographicRegion.rdf#SWE'></spm:hasValue></spmi:Distribution></spm:hasInformation><spm:hasInformation><spmi:Distribution xmlns:spmi='http://rs.tdwg.org/ontology/voc/SPMInfoItems#'><spm:hasValue rdf:resource='http://rs.tdwg.org/ontology/voc/GeographicRegion.rdf#NOR'></spm:hasValue></spmi:Distribution></spm:hasInformation></spm:SpeciesProfileModel></tc:describedBy></tc:TaxonConcept></oai:metadata></oai:record></oai:GetRecord></oai:OAI-PMH>";
    }

    @Override
    public final AbstractOaiPmhResponseView setView() {
        return new GetRecordView();
    }

}
