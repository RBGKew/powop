package org.tdwg.voc;

import java.io.Serializable;
import java.net.URI;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
*
* @author ben
*
*/
public class TaxonRank {

   /**
    *
    */
   private TaxonRankTerm tnTaxonRankTerm;

   /**
   *
   */
  @XStreamAlias("rdf:resource")
  @XStreamAsAttribute
  private URI resource;

  /**
   *
   * @return the resource
   */
  public final Serializable getResource() {
      return resource;
  }

  /**
   *
   * @param newResource Set the resource
   */
  public final void setResource(final URI newResource) {
      this.resource = newResource;
  }

   /**
    *
    */
   protected TaxonRank() {
   }

   /**
    *
    * @param taxonRankTerm Set the taxon rank term
    * @param useRelation express the term as a link or embed the object
    */
   protected TaxonRank(
           final TaxonRankTerm taxonRankTerm,
           final boolean useRelation) {
       if (useRelation) {
           if (taxonRankTerm != null
                   && taxonRankTerm.getIdentifier() != null) {
                   setResource(taxonRankTerm.getIdentifier());
           } else {
               this.tnTaxonRankTerm = taxonRankTerm;
           }
       } else {
           this.tnTaxonRankTerm = taxonRankTerm;
       }
   }

   /**
    *
    * @return the taxon rank term
    */
   public final TaxonRankTerm getTaxonRankTerm() {
       return tnTaxonRankTerm;
   }

   /**
    *
    * @param taxonRankTerm Set the taxon rank term
    */
   protected final void setTaxonRankTerm(
           final TaxonRankTerm taxonRankTerm) {
       this.tnTaxonRankTerm = taxonRankTerm;
   }

   /**
    *
    * @param taxonRankTerm Set the taxon rank term
    */
   protected TaxonRank(
           final TaxonRankTerm taxonRankTerm) {
       this.tnTaxonRankTerm = taxonRankTerm;
   }
}
