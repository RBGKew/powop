package org.openarchives.pmh.marshall.xml;

import javax.xml.namespace.QName;

import org.openarchives.pmh.Metadata;
import org.openarchives.pmh.OAIPMH;
import org.openarchives.pmh.Record;
import org.openarchives.pmh.ResumptionToken;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.tdwg.voc.TaxonConcept;

import com.thoughtworks.xstream.io.xml.QNameMap;

/**
 *
 * @author ben
 *
 */
public class OpenArchivesQNameMapFactory extends
        AbstractFactoryBean<QNameMap> {

    /**
     * Returns a QNameMap which maps the namespace:name couplets on
     * to object types for the OAI PMH Schema.
     *
     * @return a map containing qnames from the oai-pmh schema
     * namespace.
     */
    @Override
    public final QNameMap createInstance() {
        QNameMap qNameMap = new QNameMap();
        qNameMap.setDefaultNamespace("http://www.openarchives.org/OAI/2.0/");
        qNameMap.setDefaultPrefix("oai");

        qNameMap.registerMapping(new QName(
                "http://www.openarchives.org/OAI/2.0/", "record", "oai"),
                Record.class);
        qNameMap.registerMapping(new QName(
                "http://www.openarchives.org/OAI/2.0/",
                "error", "oai"), "error");
        qNameMap.registerMapping(new QName(
                "http://www.openarchives.org/OAI/2.0/",
                "resumptionToken", "oai"), ResumptionToken.class);
        qNameMap.registerMapping(new QName(
                "http://www.openarchives.org/OAI/2.0/", "OAI-PMH"),
                OAIPMH.class);
        qNameMap.registerMapping(new QName(
                "http://www.openarchives.org/OAI/2.0/oai_dc/", "dc", "oai_dc"),
                "dc");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "title", "dc"),
                "dcTitle");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "creator", "dc"),
                "dcCreator");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "subject", "dc"),
                "dcSubject");
        qNameMap.registerMapping(new QName(
                "http://www.openarchives.org/OAI/2.0/oai_dc/", "publisher",
                "dc"), "dcPublisher");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "date", "dc"),
                "dcDate");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "format", "dc"),
                "dcFormat");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "identifier", "dc"),
                "dcIdentifier");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "source", "dc"),
                "dcSource");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "language", "dc"),
                "dcLanguage");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "coverage", "dc"),
                "dcCoverage");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "rights", "dc"),
                "dcRights");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonConcept#",
                "TaxonConcept", "tc"), "taxonConcept");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonConcept#",
                "primary", "tc"), "tcPrimary");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonConcept#",
                "status", "tc"), "tcStatus");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonConcept#",
                "hasName", "tc"), "tcHasName");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "TaxonName", "tn"), "tnTaxonName");
        qNameMap.registerMapping(new QName(
                "http://www.w3.org/2002/07/owl#",
                "sameAs", "owl"), "owlSameAs");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/terms/",
                "created", "dcterms"), "dctermsCreated");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/terms/",
                "date", "dcterms"), "dctermsDate");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "contributor", "dc"),
                "dcContributor");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "relation", "dc"),
                "dcRelation");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Common#",
                "abcdEquivalence", "tcom"), "tcomAbcdEquivalence");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Common#",
                "berlinModelEquivalence", "tcom"),
                "tcomBerlinModelEquivalence");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Common#",
                "darwinCoreEquivalence", "tcom"),
                "tcomDarwinCoreEquivalence");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Common#",
                "isDeprecated", "tcom"), "tcomIsDeprecated");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Common#",
                "isRestricted", "tcom"), "tcomIsRestricted");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Common#",
                "microReference", "tcom"), "tcomMicroReference");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Common#",
                "notes", "tcom"), "tcomNotes");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Common#",
                "publishedIn", "tcom"), "tcomPublishedIn");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Common#",
                "taxonomicPlacementFormal", "tcom"),
                "tcomTaxonomicPlacementFormal");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Common#",
                "taxonomicPlacementInformal", "tcom"),
                "tcomTaxonomicPlacementInformal");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Common#",
                "tcsEquivalence", "tcom"),
                "tcomTcsEquivalence");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Common#",
                "publishedInCitation", "tcom"),
                "tcomPublishedInCitation");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/PublicationCitation#",
                "PublicationCitation", "tpub"),
                "tpubPublicationCitation");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/PublicationCitation#",
                "title", "tpub"), "tpubTitle");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/PublicationCitation#",
                "authorship", "tpub"), "tpubAuthorship");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/PublicationCitation#",
                "publisher", "tpub"), "tpubPublisher");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/PublicationCitation#",
                "parentPublication", "tpub"), "tpubParentPublication");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/PublicationCitation#",
                "publicationType", "tpub"), "tpubPublicationType");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/PublicationCitation#",
                "volume", "tpub"), "tpubVolume");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/PublicationCitation#",
                "pages", "tpub"), "tpubPages");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/PublicationCitation#",
                "datePublished", "tpub"), "tpubDatePublished");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "authorship", "tn"), "tnAuthorship");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "basionymAuthorship", "tn"), "tnBasionymAuthorship");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "combinationAuthorship", "tn"), "tnCombinationAuthorship");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "nameComplete", "tn"), "tnNameComplete");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "family", "tn"), "tnFamily");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "uninomial", "tn"), "tnUninomial");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "genusPart", "tn"), "tnGenusPart");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "specificEpithet", "tn"), "tnSpecificEpithet");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "infraSpecificEpithet", "tn"), "tnInfraSpecificEpithet");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "rankString", "tn"), "tnRankString");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "rank", "tn"), "tnRank");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Person#",
                "Person", "tp"), "tpPerson");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Team#",
                "Team", "tt"), "ttTeam");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonConcept#",
                "Relationship", "tc"), "tcRelationship");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonConcept#",
                "accordingTo", "tc"), "tcAccordingTo");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonConcept#",
                "hasRelationship", "tc"), "tcHasRelationship");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonConcept#",
                "fromTaxon", "tc"), "tcFromTaxon");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonConcept#",
                "toTaxon", "tc"), "tcToTaxon");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonConcept#",
                "relationshipCategory", "tc"), "tcRelationshipCategory");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonConcept#",
                "TaxonConcept", "tc"), "tcTaxonConcept");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonConcept#",
                "TaxonRelationshipTerm", "tc"), "tcTaxonRelationshipTerm");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonConcept#",
                "describedBy", "tc"), "tcDescribedBy");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#",
                "SpeciesProfileModel", "spm"), "spmSpeciesProfileModel");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#",
                "aboutTaxon", "spm"), "spmAboutTaxon");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#",
                "hasInformation", "spm"), "spmHasInformation");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#",
                "InfoItem", "spm"), "spmInfoItem");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/SPMInfoItems#",
                "Distribution", "spmi"), "spmiDistribution");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#",
                "category", "spm"), "spmCategory");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#",
                "context", "spm"), "spmContext");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#",
                "hasValue", "spm"), "spmHasValue");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#",
                "hasContent", "spm"), "spmHasContent");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/GeographicRegion#",
                "GeographicRegion", "gr"), "grGeographicRegion");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/GeographicRegion#",
                "code", "gr"), "grCode");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/GeographicRegion#",
                "name", "gr"), "grName");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/GeographicRegion#",
                "isPartOf", "gr"), "grIsPartOf");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/Common#",
                "DefinedTerm", "tcom"), "tcomDefinedTerm");
        qNameMap.registerMapping(new QName(
                "http://grassbase.kew.org/ontology/Character#",
                "QuantitativeData", "grass"), "grassQuantitativeData");
        qNameMap.registerMapping(new QName(
                "http://grassbase.kew.org/ontology/Character#",
                "min", "grass"), "grassMin");
        qNameMap.registerMapping(new QName(
                "http://grassbase.kew.org/ontology/Character#",
                "low", "grass"), "grassLow");
        qNameMap.registerMapping(new QName(
                "http://grassbase.kew.org/ontology/Character#",
                "max", "grass"), "grassMax");
        qNameMap.registerMapping(new QName(
                "http://grassbase.kew.org/ontology/Character#",
                "high", "grass"), "grassHigh");
        qNameMap.registerMapping(new QName(
                "http://grassbase.kew.org/ontology/Character#",
                "mean", "grass"), "grassMean");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/SPMInfoItems#",
                "Habitat", "spmi"), "spmiHabitat");
        // TODO register other mappings
        return qNameMap;
    }

    @Override
    public final Class<?> getObjectType() {
        return QNameMap.class;
    }

}
