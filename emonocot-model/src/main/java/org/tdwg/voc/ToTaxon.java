package org.tdwg.voc;

import java.io.Serializable;
import java.net.URI;

import org.emonocot.model.marshall.UriConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

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
  @XStreamConverter(UriConverter.class)
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
   protected ToTaxon() {
   }

   /**
    *
    * @param taxonConcept Set the taxon concept associated with this link
    */
   protected ToTaxon(final TaxonConcept taxonConcept) {
       if (taxonConcept != null && taxonConcept.getIdentifier() != null) {
           this.setResource(taxonConcept.getIdentifier());
           //this.setTaxonConcept(taxonConcept);
       } else {
           this.setTaxonConcept(taxonConcept);
       }
   }

   /**
    *
    * @return the taxon concept associated with this link
    */
   protected final TaxonConcept getTaxonConcept() {
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
