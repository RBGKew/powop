package org.openarchives.pmh;

import org.tdwg.voc.TaxonConcept;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Metadata must be expressed in XML that complies with another XML Schema
 * (namespace=#other). Metadata must be explicitly qualified in the response.
 *
 * <p>
 * Java class for metadataType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="metadataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;any/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
public class Metadata {

    /**
     *
     */
    @XStreamAlias("dc")
    private OaiDc oaiDc;

    /**
     *
     */
    private TaxonConcept taxonConcept;

    /**
     * Gets the value of the oaiDc property.
     *
     * @return the object
     */
    public final OaiDc getOaiDc() {
        return oaiDc;
    }

    /**
     * Sets the value of the oaiDc property.
     *
     * @param value Set the object
     */
    public final void setOaiDc(final OaiDc value) {
        this.oaiDc = value;
    }

    /**
     *
     * @return the taxon concept
     */
    public final TaxonConcept getTaxonConcept() {
        return taxonConcept;
    }

    /**
     *
     * @param taxonConcept Set the taxon concept
     */
    public final void setTaxonConcept(final TaxonConcept taxonConcept) {
        this.taxonConcept = taxonConcept;
    }
}
