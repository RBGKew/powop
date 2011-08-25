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
public class ToTaxon {

   /**
    *
    */
   private TaxonConcept tcTaxonConcept;

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
   public ToTaxon() {
   }

   /**
    *
    * @param taxonConcept Set the taxon concept associated with this link
    */
   protected ToTaxon(final TaxonConcept taxonConcept) {
       if (taxonConcept != null && taxonConcept.getIdentifier() != null) {
           this.setResource(taxonConcept.getIdentifier());
       }
   }

   /**
    *
    * @return the taxon concept associated with this link
    */
   public final TaxonConcept getTaxonConcept() {
       return tcTaxonConcept;
   }

   /**
    *
    * @param taxonConcept Set the taxon concept associated with this link
    */
   protected final void setTaxonConcept(final TaxonConcept taxonConcept) {
       this.tcTaxonConcept = taxonConcept;
   }

}
