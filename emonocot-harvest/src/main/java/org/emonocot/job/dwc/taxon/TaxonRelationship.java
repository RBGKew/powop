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
   
   private String toScientificName;

   private TaxonRelationshipType term;

   public TaxonRelationship(Taxon from, TaxonRelationshipType term, String toIdentifier, String toScientificName) {
       this.from = from;
       this.toIdentifier = toIdentifier;
       this.term = term;
       this.toScientificName = toScientificName;
   }

   public TaxonRelationshipType getTerm() {
       return term;
   }

    public Taxon getFrom() {
        return from;
    }

    public String getToIdentifier() {
        return toIdentifier;
    }
    
    public String getToScientificName() {
    	return toScientificName;
    }
}
