package org.openarchives.pmh.marshall;

import javax.xml.namespace.QName;

import org.openarchives.pmh.OAIPMH;
import org.springframework.beans.factory.config.AbstractFactoryBean;

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
    protected final QNameMap createInstance() {
        QNameMap qNameMap = new QNameMap();
        qNameMap.setDefaultNamespace("http://www.openarchives.org/OAI/2.0/");
        qNameMap.setDefaultPrefix("oai");

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
                "publicationCitation", "tpc"),
                "tpcPublicationCitation");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "authorship", "tn"), "tnAuthorship");
        qNameMap.registerMapping(new QName(
                "http://rs.tdwg.org/ontology/voc/TaxonName#",
                "nameComplete", "tn"), "tnNameComplete");
        // TODO register other mappings
        return qNameMap;
    }

    @Override
    public final Class<?> getObjectType() {
        return QNameMap.class;
    }

}
