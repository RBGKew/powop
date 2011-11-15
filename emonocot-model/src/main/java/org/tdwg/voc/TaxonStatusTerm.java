package org.tdwg.voc;

import java.net.URI;
import java.net.URISyntaxException;

import org.tdwg.DefinedTerm;

/**
 *
 * @author ben
 *
 */
public class TaxonStatusTerm extends DefinedTerm {

    /**
    *
    */
    public static final TaxonStatusTerm ACCEPTED = new TaxonStatusTerm(
            "http://e-monocot.org/TaxonomicStatus#accepted",
            "Accepted");

    /**
     *
     */
    public static final TaxonStatusTerm ILLEGITIMATE = new TaxonStatusTerm(
            "http://e-monocot.org/TaxonomicStatus#illegitimate",
            "Illegitimate");

    /**
     *
     */
    public static final TaxonStatusTerm MISSAPPLIED = new TaxonStatusTerm(
            "http://e-monocot.org/TaxonomicStatus#missapplied",
            "Missapplied");

    /**
     *
     */
    public static final TaxonStatusTerm ORTHOGRAPHIC_VARIANT = new TaxonStatusTerm(
            "http://e-monocot.org/TaxonomicStatus#orthographic",
            "Orthographic Variant");

    /**
     *
     */
    public static final TaxonStatusTerm SYNONYM = new TaxonStatusTerm(
            "http://e-monocot.org/TaxonomicStatus#synonym",
            "Synonym");

    /**
     *
     */
    public static final TaxonStatusTerm UNPLACED = new TaxonStatusTerm(
            "http://e-monocot.org/TaxonomicStatus#unplaced",
            "Unplaced");

    /**
     *
     */
    public static final TaxonStatusTerm INVALID = new TaxonStatusTerm(
            "http://e-monocot.org/TaxonomicStatus#invalid",
            "Invalid");

    /**
     *
     */
    private static TaxonStatusTerm[] terms;

    static {
        terms = new TaxonStatusTerm[] {
                TaxonStatusTerm.ACCEPTED,
                TaxonStatusTerm.ILLEGITIMATE,
                TaxonStatusTerm.ORTHOGRAPHIC_VARIANT,
                TaxonStatusTerm.MISSAPPLIED,
                TaxonStatusTerm.INVALID,
                TaxonStatusTerm.UNPLACED,
                TaxonStatusTerm.SYNONYM
        };
    }

   /**
    *
    */
   public TaxonStatusTerm() {
   }

   /**
    *
    * @param identifier Set the identifier of the taxon rank term
    * @param title Set the title of the taxon rank term
    */
   public TaxonStatusTerm(final String identifier, final String title) {
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
   public static final TaxonStatusTerm fromValue(final String uri) {
       for (TaxonStatusTerm term : terms) {
           if (term != null && term.getIdentifier().toString().equals(uri)) {
               return term;
           }
       }
       return null;
   }

   @Override
   public final boolean equals(final Object obj) {
       if (obj == null
               || !obj.getClass().equals(TaxonStatusTerm.class)) {
           return false;
       }

       TaxonStatusTerm other = (TaxonStatusTerm) obj;
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
