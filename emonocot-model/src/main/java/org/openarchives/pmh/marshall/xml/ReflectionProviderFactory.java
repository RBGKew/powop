package org.openarchives.pmh.marshall.xml;

import org.kew.grassbase.ontology.QuantitativeData;
import org.openarchives.pmh.Header;
import org.openarchives.pmh.Identify;
import org.openarchives.pmh.ListIdentifiers;
import org.openarchives.pmh.ListRecords;
import org.openarchives.pmh.ListSets;
import org.openarchives.pmh.MetadataFormat;
import org.openarchives.pmh.OAIPMH;
import org.openarchives.pmh.OaiDc;
import org.openarchives.pmh.Record;
import org.openarchives.pmh.Set;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.tdwg.PublicationCitation;
import org.tdwg.voc.Distribution;
import org.tdwg.voc.InfoItem;
import org.tdwg.voc.Relationship;
import org.tdwg.voc.SpeciesProfileModel;
import org.tdwg.voc.TaxonConcept;
import org.tdwg.voc.TaxonName;

import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.SortableFieldKeySorter;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

/**
 *
 * @author ben
 *
 */
public class ReflectionProviderFactory extends
        AbstractFactoryBean<ReflectionProvider> {
    /**
     *
     */
    private static final String[] OAI_PMH_FIELDS = new String[] {
        "xmlnsOaiDcNamespace",
        "xmlnsDcNamespace",
        "xmlnsTcNamespace",
        "xmlnsRdfNamespace",
        "xmlnsTnNamespace",
        "xmlnsOwlNamespace",
        "xmlnsDctermsNamespace",
        "xmlnsTcomNamespace",
        "xmlnsTpubNamespace",
        "xmlnsTpNamespace",
        "xmlnsTtNamespace",
        "xmlnsSpmNamespace",
        "xmlnsSpmiNamespace",
        "xmlnsGrNamespace",
        "responseDate",
        "request",
        "error",
        "identify",
        "listMetadataFormats",
        "listSets",
        "getRecord",
        "listIdentifiers",
        "listRecords"};

   /**
    *
    */
   private static final String[] RECORD_FIELDS = new String[] {
       "header",
       "metadata",
       "about" };

   /**
    *
    */
   private static final String[] HEADER_FIELDS = new String[] {
           "status",
           "identifier",
           "datestamp",
           "setSpec" };

   /**
    *
    */
   private static final String[] IDENTIFY_FIELDS = new String[] {
       "repositoryName",
       "baseURL",
       "protocolVersion",
       "adminEmail",
       "earliestDatestamp",
       "deletedRecord",
       "granularity",
       "compression",
       "description" };
   /**
    *
    */
   private static final String[] LIST_IDENTIFIERS_FIELDS = new String[] {
       "header",
       "resumptionToken"
   };

   /**
    *
    */
   private static final String[] LIST_RECORDS_FIELDS = new String[] {
      "record",
      "resumptionToken"
   };

   /**
    *
    */
   private static final String[] LIST_SETS_FIELDS = new String[] {
     "set",
     "resumptionToken"
   };

   /**
    *
    */
   private static final String[] METADATA_FORMAT_FIELDS = new String[] {
       "metadataPrefix",
       "schema",
       "metadataNamespace"
   };

   /**
    *
    */
  private static final String[] OAI_DC_FIELDS = new String[] {
      "dcTitle",
      "dcCreator",
      "dcSubject",
      "dcPublisher",
      "dcDate",
      "dcFormat",
      "dcIdentifier",
      "dcSource",
      "dcLanguage",
      "dcCoverage",
      "dcRights"
  };

  /**
   *
   */
   private static final String[] SET_FIELDS = new String[] {
       "setSpec",
       "setName",
       "setDescription"
  };

   /**
    *
    */
    private static final String[] TAXON_CONCEPT_FIELDS = new String[] {
        "dcTitle",
        "owlSameAs",
        "dcIdentifier",
        "dctermsCreated",
        "dcCreator",
        "dctermsDate",
        "dcContributor",
        "dcRelation",
        "tcomAbcdEquivalence",
        "tcomBerlinModelEquivalence",
        "tcomDarwinCoreEquivalence",
        "tcomIsDeprecated",
        "tcomIsRestricted",
        "tcomMicroReference",
        "tcomNotes",
        "tcomPublishedIn",
        "tcomTaxonomicPlacementFormal",
        "tcomTaxonomicPlacementInformal",
        "tcomTcsEquivalence",
        "tcomPublishedInCitations",
        "tcPrimary",
        "tcStatus",
        "tcAccordingTo",
        "tcHasName",
        "tcHasRelationships",
        "tcDescribedBys"
   };

    /**
     *
     */
    private static final String[] TAXON_NAME_FIELDS = new String[] {
        "dcTitle",
        "owlSameAs",
        "dcIdentifier",
        "dctermsCreated",
        "dcCreator",
        "dctermsDate",
        "dcContributor",
        "dcRelation",
        "tcomAbcdEquivalence",
        "tcomBerlinModelEquivalence",
        "tcomDarwinCoreEquivalence",
        "tcomIsDeprecated",
        "tcomIsRestricted",
        "tcomMicroReference",
        "tcomNotes",
        "tcomPublishedIn",
        "tcomTaxonomicPlacementFormal",
        "tcomTaxonomicPlacementInformal",
        "tcomTcsEquivalence",
        "tcomPublishedInCitations",
        "tnAuthorship",
        "tnBasionymAuthorship",
        "tnCombinationAuthorship",
        "tnNameComplete",
        "tnFamily",
        "tnUninomial",
        "tnGenusPart",
        "tnSpecificEpithet",
        "tnInfraSpecificEpithet",
        "tnRank",
        "tnRankString"
    };

   /**
    *
    */
    private static final String[] RELATIONSHIP_FIELDS = new String[] {
        "dcTitle",
        "owlSameAs",
        "dcIdentifier",
        "dctermsCreated",
        "dcCreator",
        "dctermsDate",
        "dcContributor",
        "dcRelation",
        "tcomAbcdEquivalence",
        "tcomBerlinModelEquivalence",
        "tcomDarwinCoreEquivalence",
        "tcomIsDeprecated",
        "tcomIsRestricted",
        "tcomMicroReference",
        "tcomNotes",
        "tcomPublishedIn",
        "tcomTaxonomicPlacementFormal",
        "tcomTaxonomicPlacementInformal",
        "tcomTcsEquivalence",
        "tcFromTaxon",
        "tcRelationshipCategory",
        "tcToTaxon"
   };

   /**
    *
    */
    private static final String[] SPM_FIELDS = new String[] {
        "dcTitle",
        "owlSameAs",
        "dcIdentifier",
        "dctermsCreated",
        "dcCreator",
        "dctermsDate",
        "dcContributor",
        "dcRelation",
        "tcomAbcdEquivalence",
        "tcomBerlinModelEquivalence",
        "tcomDarwinCoreEquivalence",
        "tcomIsDeprecated",
        "tcomIsRestricted",
        "tcomMicroReference",
        "tcomNotes",
        "tcomPublishedIn",
        "tcomTaxonomicPlacementFormal",
        "tcomTaxonomicPlacementInformal",
        "tcomTcsEquivalence",
        "spmAboutTaxon",
        "spmHasInformations"
   };

    /**
    *
    */
    private static final String[] INFO_ITEM_FIELDS = new String[] {
        "dcTitle",
        "owlSameAs",
        "dcIdentifier",
        "dctermsCreated",
        "dcCreator",
        "dctermsDate",
        "dcContributor",
        "dcRelation",
        "tcomAbcdEquivalence",
        "tcomBerlinModelEquivalence",
        "tcomDarwinCoreEquivalence",
        "tcomIsDeprecated",
        "tcomIsRestricted",
        "tcomMicroReference",
        "tcomNotes",
        "tcomPublishedIn",
        "tcomTaxonomicPlacementFormal",
        "tcomTaxonomicPlacementInformal",
        "tcomTcsEquivalence",
        "spmCategory",
        "spmContextValues",
        "spmHasValues"
   };

   /**
    *
    */
    private static final String[] QUANTITATIVE_DATA_FIELDS = new String[] {
        "dcTitle",
        "owlSameAs",
        "dcIdentifier",
        "dctermsCreated",
        "dcCreator",
        "dctermsDate",
        "dcContributor",
        "dcRelation",
        "tcomAbcdEquivalence",
        "tcomBerlinModelEquivalence",
        "tcomDarwinCoreEquivalence",
        "tcomIsDeprecated",
        "tcomIsRestricted",
        "tcomMicroReference",
        "tcomNotes",
        "tcomPublishedIn",
        "tcomTaxonomicPlacementFormal",
        "tcomTaxonomicPlacementInformal",
        "tcomTcsEquivalence",
        "spmCategory",
        "spmContextValues",
        "spmHasValues",
        "grassMin",
        "grassLow",
        "grassMax",
        "grassHigh",
        "grassMean"
   };

    /**
    *
    */
    private static final String[] PUBLICATION_CITATION_FIELDS = new String[] {
        "dcTitle",
        "owlSameAs",
        "dcIdentifier",
        "dctermsCreated",
        "dcCreator",
        "dctermsDate",
        "dcContributor",
        "dcRelation",
        "tcomAbcdEquivalence",
        "tcomBerlinModelEquivalence",
        "tcomDarwinCoreEquivalence",
        "tcomIsDeprecated",
        "tcomIsRestricted",
        "tcomMicroReference",
        "tcomNotes",
        "tcomPublishedIn",
        "tcomTaxonomicPlacementFormal",
        "tcomTaxonomicPlacementInformal",
        "tcomTcsEquivalence",
        "tpubTitle",
        "tpubAuthorship",
        "tpubPublisher",
        "tpubParentPublication",
        "tpubPublicationType",
        "tpubVolume",
        "tpubPages",
        "tpubDatePublished"
   };

    /**
     * Returns a ReflectionProvider that provides core reflection services.
     *
     * @return configured reflection provider.
     */
    @Override
    protected final ReflectionProvider createInstance() {
        SortableFieldKeySorter sorter = new SortableFieldKeySorter();

        sorter.registerFieldOrder(OAIPMH.class,
                ReflectionProviderFactory.OAI_PMH_FIELDS);
        sorter.registerFieldOrder(Record.class,
                ReflectionProviderFactory.RECORD_FIELDS);
        sorter.registerFieldOrder(Header.class,
                ReflectionProviderFactory.HEADER_FIELDS);
        sorter.registerFieldOrder(Identify.class,
                ReflectionProviderFactory.IDENTIFY_FIELDS);
        sorter.registerFieldOrder(ListIdentifiers.class,
                ReflectionProviderFactory.LIST_IDENTIFIERS_FIELDS);
        sorter.registerFieldOrder(ListRecords.class,
                ReflectionProviderFactory.LIST_RECORDS_FIELDS);
        sorter.registerFieldOrder(ListSets.class,
                ReflectionProviderFactory.LIST_SETS_FIELDS);
        sorter.registerFieldOrder(MetadataFormat.class,
                ReflectionProviderFactory.METADATA_FORMAT_FIELDS);
        sorter.registerFieldOrder(OaiDc.class,
                ReflectionProviderFactory.OAI_DC_FIELDS);
        sorter.registerFieldOrder(Set.class,
                ReflectionProviderFactory.SET_FIELDS);
        sorter.registerFieldOrder(TaxonConcept.class,
                ReflectionProviderFactory.TAXON_CONCEPT_FIELDS);
        sorter.registerFieldOrder(TaxonName.class,
                ReflectionProviderFactory.TAXON_NAME_FIELDS);
        sorter.registerFieldOrder(Relationship.class,
                ReflectionProviderFactory.RELATIONSHIP_FIELDS);
        sorter.registerFieldOrder(SpeciesProfileModel.class,
                ReflectionProviderFactory.SPM_FIELDS);
        sorter.registerFieldOrder(InfoItem.class,
                ReflectionProviderFactory.INFO_ITEM_FIELDS);
        sorter.registerFieldOrder(Distribution.class,
                ReflectionProviderFactory.INFO_ITEM_FIELDS);
        sorter.registerFieldOrder(QuantitativeData.class,
                ReflectionProviderFactory.QUANTITATIVE_DATA_FIELDS);
        sorter.registerFieldOrder(PublicationCitation.class,
                ReflectionProviderFactory.PUBLICATION_CITATION_FIELDS);

        FieldDictionary fieldDictionary = new FieldDictionary(sorter);

        ReflectionProvider reflectionProvider
            = new Sun14ReflectionProvider(fieldDictionary);
        return reflectionProvider;
    }

    @Override
    public final Class<?> getObjectType() {
        return ReflectionProvider.class;
    }

}
