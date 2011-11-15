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
public class TaxonomicStatus {

   /**
    *
    */
   private TaxonStatusTerm tcTaxonStatusTerm;

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
   protected TaxonomicStatus() {
   }

   /**
    *
    * @param taxonStatusTerm Set the taxon status term
    * @param useRelation express the term as a link or embed the object
    */
   protected TaxonomicStatus(
           final TaxonStatusTerm taxonStatusTerm,
           final boolean useRelation) {
       if (useRelation) {
           if (taxonStatusTerm != null
                   && taxonStatusTerm.getIdentifier() != null) {
                   setResource(taxonStatusTerm.getIdentifier());
           } else {
               this.tcTaxonStatusTerm = taxonStatusTerm;
           }
       } else {
           this.tcTaxonStatusTerm = taxonStatusTerm;
       }
   }

   /**
    *
    * @return the taxon status term
    */
   public final TaxonStatusTerm getTaxonStatusTerm() {
       return tcTaxonStatusTerm;
   }

   /**
    *
    * @param taxonStatusTerm Set the taxon status term
    */
   protected final void setTaxonStatusTerm(
           final TaxonStatusTerm taxonStatusTerm) {
       this.tcTaxonStatusTerm = taxonStatusTerm;
   }

   /**
    *
    * @param taxonStatusTerm Set the taxon rank term
    */
   protected TaxonomicStatus(
           final TaxonStatusTerm taxonStatusTerm) {
       this.tcTaxonStatusTerm = taxonStatusTerm;
   }
}
