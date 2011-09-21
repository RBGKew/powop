package org.openarchives.pmh;

import org.openarchives.pmh.marshall.xml.MetadataPrefixConverter;

import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * <p>
 * Java class for metadataFormatType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="metadataFormatType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metadataPrefix"
 *             type="{http://www.openarchives.org/OAI/2.0/}metadataPrefixType"/>
 *         &lt;element name="schema"
 *             type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="metadataNamespace"
 *             type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
public class MetadataFormat {

    /**
     *
     */
    @XStreamConverter(MetadataPrefixConverter.class)
    private MetadataPrefix metadataPrefix;

    /**
     *
     */
    private String schema;

    /**
     *
     */
    private String metadataNamespace;

    /**
     * Gets the value of the metadataPrefix property.
     *
     * @return the metadata prefix
     */
    public final MetadataPrefix getMetadataPrefix() {
        return metadataPrefix;
    }

    /**
     * Sets the value of the metadataPrefix property.
     *
     * @param newMetadataPrefix Set the metadata prefix
     */
    public final void setMetadataPrefix(
            final MetadataPrefix newMetadataPrefix) {
        this.metadataPrefix = newMetadataPrefix;
    }

    /**
     * Gets the value of the schema property.
     *
     * @return the schema
     */
    public final String getSchema() {
        return schema;
    }

    /**
     * Sets the value of the schema property.
     *
     * @param value Set the schema
     */
    public final void setSchema(final String value) {
        this.schema = value;
    }

    /**
     * Gets the value of the metadataNamespace property.
     *
     * @return the metadata namespace
     */
    public final String getMetadataNamespace() {
        return metadataNamespace;
    }

    /**
     * Sets the value of the metadataNamespace property.
     *
     * @param value Set the metadata namespace
     */
    public final void setMetadataNamespace(final String value) {
        this.metadataNamespace = value;
    }
}
