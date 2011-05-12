package org.openarchives.pmh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.tdwg.voc.TaxonConcept;
import org.tdwg.voc.SpeciesProfileModel;

/**
 * Metadata must be expressed in XML that complies
 *        with another XML Schema (namespace=#other). Metadata must be 
 *        explicitly qualified in the response.
 * 
 * <p>Java class for metadataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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

	@XmlElements({
	    @XmlElement(name = "dc", namespace = "http://www.openarchives.org/OAI/2.0/oai_dc/", type = OaiDc.class),
	    @XmlElement(name = "TaxonConcept", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#", type = TaxonConcept.class),
	    @XmlElement(name = "SpeciesProfileModel", namespace = "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#", type = SpeciesProfileModel.class)
	})
    protected Object any;

    /**
     * Gets the value of the any property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getAny() {
        return any;
    }

    /**
     * Sets the value of the any property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setAny(Object value) {
        this.any = value;
    }

}
