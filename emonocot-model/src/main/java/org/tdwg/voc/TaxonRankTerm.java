package org.tdwg.voc;

import java.net.URI;
import java.net.URISyntaxException;

import org.tdwg.DefinedTerm;

/**
 *
 * @author ben
 *
 */
public class TaxonRankTerm extends DefinedTerm {

    /**
     *
     */
    private static TaxonRankTerm[] terms;

    static {
        terms = new TaxonRankTerm[] {
                TaxonRankTerm.BIO_VARIETY,
                TaxonRankTerm.CANDIDATE,
                TaxonRankTerm.CLASS,
                TaxonRankTerm.CONVAR,
                TaxonRankTerm.CULTIVAR,
                TaxonRankTerm.CULTIVAR_GROUP,
                TaxonRankTerm.DENOMINATION_CLASS,
                TaxonRankTerm.DIVISION,
                TaxonRankTerm.DOMAIN,
                TaxonRankTerm.EMPIRE,
                TaxonRankTerm.FAMILY,
                TaxonRankTerm.FORM,
                TaxonRankTerm.GENUS,
                TaxonRankTerm.GRAFT_CHIMAERA,
                TaxonRankTerm.GREX,
                TaxonRankTerm.INFRACLASS,
                TaxonRankTerm.INFRADIVISION,
                TaxonRankTerm.INFRAFAMILY,
                TaxonRankTerm.INFRAGENERIC_TAXON,
                TaxonRankTerm.INFRAGENUS,
                TaxonRankTerm.INFRAKINGDOM,
                TaxonRankTerm.INFRAORDER,
                TaxonRankTerm.INFRAPHYLUM,
                TaxonRankTerm.INFRASPECIES,
                TaxonRankTerm.INFRASPECIFIC_TAXON,
                TaxonRankTerm.INFRATRIBE,
                TaxonRankTerm.KINGDOM,
                TaxonRankTerm.ORDER,
                TaxonRankTerm.PATHO_VARIETY,
                TaxonRankTerm.PHYLUM,
                TaxonRankTerm.SECTION,
                TaxonRankTerm.SERIES,
                TaxonRankTerm.SPECIAL_FORM,
                TaxonRankTerm.SPECIES,
                TaxonRankTerm.SPECIES_AGGREGATE,
                TaxonRankTerm.SUB_SUB_FORM,
                TaxonRankTerm.SUB_SUB_VARIETY,
                TaxonRankTerm.SUB_VARIETY,
                TaxonRankTerm.SUB_VARIETY,
                TaxonRankTerm.SUBCLASS,
                TaxonRankTerm.SUBDIVISION,
                TaxonRankTerm.SUBFAMILY,
                TaxonRankTerm.SUBFORM,
                TaxonRankTerm.SUBGENUS,
                TaxonRankTerm.SUBKINGDOM,
                TaxonRankTerm.SUBORDER,
                TaxonRankTerm.SUBPHYLUM,
                TaxonRankTerm.SUBSECTION,
                TaxonRankTerm.SUBSERIES,
                TaxonRankTerm.SUBSPECIES,
                TaxonRankTerm.SUBSPECIFIC_AGGREGATE,
                TaxonRankTerm.SUBTRIBE,
                TaxonRankTerm.SUPERCLASS,
                TaxonRankTerm.SUPERDIVISION,
                TaxonRankTerm.SUPERFAMILY,
                TaxonRankTerm.SUPERKINGDOM,
                TaxonRankTerm.SUPERORDER,
                TaxonRankTerm.SUPERPHYLUM,
                TaxonRankTerm.SUPERTRIBE,
                TaxonRankTerm.TRIBE,
                TaxonRankTerm.VARIETY
        };
    }

   /**
    *
    */
    public static final TaxonRankTerm BIO_VARIETY = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Bio-Variety",
            "Bio-Variety");

    /**
     *
     */
    public static final TaxonRankTerm CANDIDATE = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Candidate",
            "Candidate");

    /**
     *
     */
    public static final TaxonRankTerm CLASS = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Class",
            "Class");

    /**
     *
     */
    public static final TaxonRankTerm CONVAR = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Convar",
            "Convar");

    /**
     *
     */
    public static final TaxonRankTerm CULTIVAR = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Cultivar",
            "Cultivar");

    /**
     *
     */
    public static final TaxonRankTerm CULTIVAR_GROUP = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Bio-Variety",
            "Cultivar-Group");

    /**
     *
     */
    public static final TaxonRankTerm DENOMINATION_CLASS = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank.rdf#DenominationClass",
            "DenominationClass");

    /**
     *
     */
    public static final TaxonRankTerm DIVISION = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Bio-Variety",
            "Bio-Variety");

    /**
     *
     */
    public static final TaxonRankTerm DOMAIN = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Division",
            "Division");

    /**
     *
     */
    public static final TaxonRankTerm EMPIRE = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Empire",
            "Empire");

    /**
     *
     */
    public static final TaxonRankTerm FAMILY = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Family",
            "Family");

    /**
     *
     */
    public static final TaxonRankTerm FORM = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Form",
            "Form");

    /**
     *
     */
    public static final TaxonRankTerm GENUS = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Genus",
            "Genus");

    /**
     *
     */
    public static final TaxonRankTerm GRAFT_CHIMAERA = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Graft-Chimaera",
            "Graft-Chimaera");

    /**
     *
     */
    public static final TaxonRankTerm GREX = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Grex",
            "Grex");

    /**
     *
     */
    public static final TaxonRankTerm INFRACLASS = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Infraclass",
            "Infraclass");

    /**
     *
     */
    public static final TaxonRankTerm INFRADIVISION = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Infradivision",
            "Infradivision");

    /**
     *
     */
    public static final TaxonRankTerm INFRAFAMILY = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Infrafamily",
            "Infrafamily");

    /**
     *
     */
    public static final TaxonRankTerm INFRAGENERIC_TAXON = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#InfragenericTaxon",
            "InfragenericTaxon");

    /**
     *
     */
    public static final TaxonRankTerm INFRAGENUS = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Infragenus",
            "Infragenus");

    /**
     *
     */
    public static final TaxonRankTerm INFRAKINGDOM = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Infrakingdom",
            "Infrakingdom");

    /**
     *
     */
    public static final TaxonRankTerm INFRAORDER = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Infraorder",
            "Infraorder");

    /**
     *
     */
    public static final TaxonRankTerm INFRAPHYLUM = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Infraphylum",
            "Infraphylum");

    /**
     *
     */
    public static final TaxonRankTerm INFRASPECIES = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Infraspecies",
            "Infraspecies");

    /**
     *
     */
    public static final TaxonRankTerm INFRASPECIFIC_TAXON = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#InfraspecificTaxon",
            "InfraspecificTaxon");

    /**
     *
     */
    public static final TaxonRankTerm INFRATRIBE = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Infratribe",
            "Infratribe");

    /**
     *
     */
    public static final TaxonRankTerm KINGDOM = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Kingdom",
            "Kingdom");

    /**
     *
     */
    public static final TaxonRankTerm ORDER = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Order",
            "Order");

    /**
     *
     */
    public static final TaxonRankTerm PATHO_VARIETY = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Patho-Variety",
            "Patho-Variety");

    /**
     *
     */
    public static final TaxonRankTerm PHYLUM = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Phylum",
            "Phylum");

    /**
     *
     */
    public static final TaxonRankTerm SECTION = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Section",
            "Section");

    /**
     *
     */
    public static final TaxonRankTerm SERIES = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Series",
            "Series");

    /**
     *
     */
    public static final TaxonRankTerm SPECIAL_FORM = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#SpecialForm",
            "SpecialForm");

    /**
     *
     */
    public static final TaxonRankTerm SPECIES = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Species",
            "Species");

    /**
     *
     */
    public static final TaxonRankTerm SPECIES_AGGREGATE = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#SpeciesAggregate",
            "SpeciesAggregate");

    /**
     *
     */
    public static final TaxonRankTerm SUBCLASS = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Subclass",
            "Subclass");

    /**
     *
     */
    public static final TaxonRankTerm SUBDIVISION = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Subdivision",
            "Subdivision");

    /**
     *
     */
    public static final TaxonRankTerm SUBFAMILY = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Subfamily",
            "Subfamily");

    /**
     *
     */
    public static final TaxonRankTerm SUBFORM = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Subform",
            "Subform");

    /**
     *
     */
    public static final TaxonRankTerm SUBGENUS = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Subgenus",
            "Subgenus");

    /**
     *
     */
    public static final TaxonRankTerm SUBKINGDOM = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Subkingdom",
            "Subkingdom");

    /**
     *
     */
    public static final TaxonRankTerm SUBORDER = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Suborder",
            "Suborder");

    /**
     *
     */
    public static final TaxonRankTerm SUBPHYLUM = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Subphylum",
            "Subphylum");

    /**
     *
     */
    public static final TaxonRankTerm SUBSECTION = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Subsection",
            "Subsection");

    /**
     *
     */
    public static final TaxonRankTerm SUBSERIES = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Subseries",
            "Subseries");

    /**
     *
     */
    public static final TaxonRankTerm SUBSPECIES = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Subspecies",
            "Subspecies");

    /**
     *
     */
    public static final TaxonRankTerm SUBSPECIFIC_AGGREGATE = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#SubspecificAggregate",
            "SubspecificAggregate");

    /**
     *
     */
    public static final TaxonRankTerm SUB_SUB_FORM = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Subsubform",
            "Subsubform");

    /**
     *
     */
    public static final TaxonRankTerm SUB_SUB_VARIETY = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Sub-Sub-Variety",
            "Sub-Sub-Variety");

    /**
     *
     */
    public static final TaxonRankTerm SUBTRIBE = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Subtribe",
            "Subtribe");

    /**
     *
     */
    public static final TaxonRankTerm SUB_VARIETY = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Sub-Variety",
            "Sub-Variety");

    /**
     *
     */
    public static final TaxonRankTerm SUPERCLASS = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Superclass",
            "Superclass");

    /**
     *
     */
    public static final TaxonRankTerm SUPERDIVISION = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Superdivision",
            "Superdivision");

    /**
     *
     */
    public static final TaxonRankTerm SUPERFAMILY = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Superfamily",
            "Superfamily");

    /**
     *
     */
    public static final TaxonRankTerm SUPERKINGDOM = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Superkingdom",
            "Superkingdom");

    /**
     *
     */
    public static final TaxonRankTerm SUPERORDER = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Superorder",
            "Superorder");

    /**
     *
     */
    public static final TaxonRankTerm SUPERPHYLUM = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Superphylum",
            "Superphylum");

    /**
     *
     */
    public static final TaxonRankTerm SUPERTRIBE = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Supertribe",
            "Supertribe");

    /**
     *
     */
    public static final TaxonRankTerm SUPRAGENERIC_TAXON = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#SupragenericTaxon",
            "SupragenericTaxon");

    /**
     *
     */
    public static final TaxonRankTerm TRIBE = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Tribe",
            "Tribe");

    /**
     *
     */
    public static final TaxonRankTerm VARIETY = new TaxonRankTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonRank#Variety",
            "Variety");

   /**
    *
    */
   public TaxonRankTerm() {
   }

   /**
    *
    * @param identifier Set the identifier of the taxon rank term
    * @param title Set the title of the taxon rank term
    */
   public TaxonRankTerm(final String identifier, final String title) {
       this.setTitle(title);
       try {
           this.setIdentifier(new URI(identifier));
       } catch (URISyntaxException e) {
           throw new IllegalArgumentException(identifier
                   + " is not a valid uri", e);
       }
   }

   /**
    * Resolve a taxon rank term from a uri.
    *
    * @param uri the uri to resolve
    * @return a taxon relationship term
    */
   public static final TaxonRankTerm fromValue(final String uri) {
       for (TaxonRankTerm term : terms) {
           if (term.getIdentifier().toString().equals(uri)) {
               return term;
           }
       }
       return null;
   }

   @Override
   public final boolean equals(final Object obj) {
       if (obj == null
               || !obj.getClass().equals(TaxonRankTerm.class)) {
           return false;
       }

       TaxonRankTerm other = (TaxonRankTerm) obj;
       if (other.getIdentifier() == null || this.getIdentifier() == null) {
           return false;
       }
       return this.getIdentifier().equals(other.getIdentifier());
   }

   @Override
   public final int hashCode() {
       return this.getIdentifier().hashCode();
   }
}
