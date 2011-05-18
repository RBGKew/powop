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
public class AboutTaxon {

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
   private TaxonConcept tcTaxonConcept;

   /**
    *
    */
    protected AboutTaxon() {
    }

    /**
     *
     * @param taxonConcept the taxon concept that this description is about
     * @param useRelation use a relation rather than embedding the entity
     */
    protected AboutTaxon(final TaxonConcept taxonConcept,
            final boolean useRelation) {
        if (useRelation) {
            if (taxonConcept != null && taxonConcept.getIdentifier() != null) {
                this.setResource(taxonConcept.getIdentifier());
            } else {
                this.tcTaxonConcept = taxonConcept;
            }
        } else {
            this.tcTaxonConcept = taxonConcept;
        }
    }

    /**
     *
     * @return the taxon concept associated with this description
     */
    protected final TaxonConcept getTaxonConcept() {
        return tcTaxonConcept;
    }

    /**
     *
     * @param taxonConcept
     *            Set the taxon concept associated with this description
     */
    protected final void setTaxonConcept(final TaxonConcept taxonConcept) {
        this.tcTaxonConcept = taxonConcept;
    }
}
