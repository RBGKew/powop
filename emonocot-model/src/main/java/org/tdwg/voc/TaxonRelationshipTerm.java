package org.tdwg.voc;

import java.net.URI;
import java.net.URISyntaxException;

import org.tdwg.DefinedTerm;

/**
 *
 * @author ben
 *
 */
public class TaxonRelationshipTerm extends DefinedTerm {

    /**
     *
     */
    private static TaxonRelationshipTerm[] terms;
    
    static {
        terms = new TaxonRelationshipTerm[]{
                TaxonRelationshipTerm.DOES_NOT_INCLUDE,
                TaxonRelationshipTerm.INCLUDES,
                TaxonRelationshipTerm.DOES_NOT_OVERLAP,
                TaxonRelationshipTerm.OVERLAPS,
                TaxonRelationshipTerm.EXCLUDES,
                TaxonRelationshipTerm.HAS_SYNONYM,
                TaxonRelationshipTerm.IS_SYONYM_FOR,
                TaxonRelationshipTerm.HAS_VERNACULAR,
                TaxonRelationshipTerm.IS_VERNACULAR_FOR,
                TaxonRelationshipTerm.IS_AMBIREGNAL_OF,
                TaxonRelationshipTerm.IS_ANAMORPH_OF,
                TaxonRelationshipTerm.IS_CHILD_TAXON_OF,
                TaxonRelationshipTerm.IS_PARENT_TAXON_OF,
                TaxonRelationshipTerm.IS_CONGRUENT_TO,
                TaxonRelationshipTerm.IS_FEMALE_PARENT_OF,
                TaxonRelationshipTerm.IS_MALE_PARENT_OF,
                TaxonRelationshipTerm.IS_FIRST_PARENT_OF,
                TaxonRelationshipTerm.IS_SECOND_PARENT_OF,
                TaxonRelationshipTerm.IS_HYBRID_PARENT_OF,
                TaxonRelationshipTerm.IS_HYBRID_CHILD_OF,
                TaxonRelationshipTerm.IS_NOT_CONGRUENT_TO,
                TaxonRelationshipTerm.IS_NOT_INCLUDED_IN
        };
    }
    /**
     *
     */
    public static final TaxonRelationshipTerm DOES_NOT_INCLUDE
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#DoesNotInclude",
            "Does Not Include");

    /**
     *
     */
    public static final TaxonRelationshipTerm DOES_NOT_OVERLAP
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#DoesNotOverlap",
            "Does Not Overlap");

    /**
     *
     */
    public static final TaxonRelationshipTerm EXCLUDES
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#Excludes",
            "Excludes");

    /**
     *
     */
    public static final TaxonRelationshipTerm HAS_SYNONYM
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#HasSynonym",
            "Has Synonym");

    /**
     *
     */
    public static final TaxonRelationshipTerm HAS_VERNACULAR
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#HasVernacular",
            "Has Vernacular");

    /**
     *
     */
    public static final TaxonRelationshipTerm INCLUDES
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#Includes",
            "Includes");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_AMBIREGNAL_OF
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsAmbiregnalOf",
            "Is Ambiregnal Of");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_ANAMORPH_OF
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsAnamorphOf",
            "Is Anamorph Of");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_CHILD_TAXON_OF
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsChildTaxonOf",
            "Is Child Taxon Of");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_CONGRUENT_TO
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsCongruentTo",
            "Is Congruent To");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_FEMALE_PARENT_OF
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsFemaleParentOf",
            "Is Female Parent Of");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_FIRST_PARENT_OF
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsFirstParentOf",
            "Is First Parent Of");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_HYBRID_CHILD_OF
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsHybridChildOf",
            "Is Hybrid Child Of");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_HYBRID_PARENT_OF
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsHybridParentOf",
            "Is Hybrid Parent Of");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_INCLUDED_IN
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsIncludedIn",
            "Is Included In");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_MALE_PARENT_OF
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsMaleParentOf",
            "Is Male Parent Of");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_NOT_CONGRUENT_TO
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsNotCongruentTo",
            "Is Not Congruent To");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_NOT_INCLUDED_IN
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsNotIncludedIn",
            "Is Not Included In");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_PARENT_TAXON_OF
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsParentTaxonOf",
            "Is Parent Taxon Of");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_SECOND_PARENT_OF
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsSecondParentOf",
            "Is Second Parent Of");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_SYONYM_FOR
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsSynonymFor",
            "Is Synonym For");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_TELEMORPH_OF
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsTelemorphOf",
            "Is Telemorph Of");

    /**
     *
     */
    public static final TaxonRelationshipTerm IS_VERNACULAR_FOR
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#IsVernacularFor",
            "Is Vernacular For");

    /**
     *
     */
    public static final TaxonRelationshipTerm OVERLAPS
        = new TaxonRelationshipTerm(
            "http://rs.tdwg.org/ontology/voc/TaxonConcept#Overlaps",
            "Overlaps");

    /**
     *
     */
    public TaxonRelationshipTerm() {
    }

    /**
     *
     * @param identifier Set the identifier of the taxon relationship term
     * @param title Set the title of the taxon relationship term
     */
    public TaxonRelationshipTerm(final String identifier, final String title) {
        this.setTitle(title);
        try {
            this.setIdentifier(new URI(identifier));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(identifier
                    + " is not a valid uri", e);
        }
    }

    /**
     * Resolve a taxon relationship term from a uri.
     *
     * @param uri the uri to resolve
     * @return a taxon relationship term
     */
    public static final TaxonRelationshipTerm fromValue(String uri) {
        for (TaxonRelationshipTerm term : terms) {
            if (term.getIdentifier().toString().equals(uri)) {
                return term;
            }
        }
        return null;
    }

}
