package org.emonocot.job.dwc.taxon;

import org.emonocot.model.Taxon;


/**
*
* @author ben
*
*/
public class TaxonRelationship {

   private Taxon from;

   private String toIdentifier;

   private TaxonRelationshipType term;

   public TaxonRelationship(Taxon newFrom, TaxonRelationshipType newTerm, String toIdentifier) {
       this.from = newFrom;
       this.toIdentifier = toIdentifier;
       this.term = newTerm;
   }

   public final TaxonRelationshipType getTerm() {
       return term;
   }

    public final Taxon getFrom() {
        return from;
    }

    public final String getToIdentifier() {
        return toIdentifier;
    }
}
