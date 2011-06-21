package org.emonocot.harvest.common;

import java.util.concurrent.Callable;

import org.emonocot.model.taxon.Taxon;
import org.tdwg.voc.TaxonRelationshipTerm;

/**
*
* @author ben
*
*/
public class TaxonRelationship {
   /**
    *
    */
   private Taxon from;
   /**
    *
    */
   private TaxonRelationshipTerm term;
   /**
    *
    */
   private Callable<Taxon> to;

   /**
    *
    * @param newFrom Set the from taxon
    * @param newTerm Set relationship type
    */
   public TaxonRelationship(final Taxon newFrom,
           final TaxonRelationshipTerm newTerm) {
       this.from = newFrom;
       this.term = newTerm;
   }

   /**
    *
    * @param newTo Set the callable which will resolve to the 'to' taxon
    */
   public final void setTo(final Callable<Taxon> newTo) {
       this.to = newTo;
   }
   /**
    *
    * @return The taxon relationship term
    */
   public final TaxonRelationshipTerm getTerm() {
       return term;
   }

   /**
    *
    * @return the 'from' taxon
    */
    public final Taxon getFrom() {
        return from;
    }

    /**
     *
     * @return the 'to' taxon
     */
    public final Callable<Taxon> getTo() {
        return to;
    }
}
