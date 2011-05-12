package org.openarchives.pmh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for metadataFormatType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="metadataFormatType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metadataPrefix" type="{http://www.openarchives.org/OAI/2.0/}metadataPrefixType"/>
 *         &lt;element name="schema" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="metadataNamespace" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metadataFormatType", propOrder = {
    "metadataPrefix",
    "schema",
    "metadataNamespace"
})
public class MetadataFormat {

    @XmlElement(required = true)
    protected MetadataPrefix metadataPrefix;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String schema;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String metadataNamespace;

    /**
     * Gets the value of the metadataPrefix property.
     * 
     * @return
     *     possible object is
     *     {@link MetadataPrefix }
     *     
     */
    public MetadataPrefix getMetadataPrefix() {
        return metadataPrefix;
    }

    /**
     * Sets the value of the metadataPrefix property.
     * 
     * @param metadataPrefix2
     *     allowed object is
     *     {@link MetadataPrefix }
     *     
     */
    public void setMetadataPrefix(MetadataPrefix metadataPrefix2) {
        this.metadataPrefix = metadataPrefix2;
    }

    /**
     * Gets the value of the schema property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Sets the value of the schema property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchema(String value) {
        this.schema = value;
    }

    /**
     * Gets the value of the metadataNamespace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetadataNamespace() {
        return metadataNamespace;
    }

    /**
     * Sets the value of the metadataNamespace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetadataNamespace(String value) {
        this.metadataNamespace = value;
    }

}
