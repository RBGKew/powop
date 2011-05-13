package org.openarchives.pmh;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Java class for ListMetadataFormatsType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="ListMetadataFormatsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metadataFormat"
 *             type="{http://www.openarchives.org/OAI/2.0/}metadataFormatType"
 *             maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
public class ListMetadataFormats {

    /**
     *
     */
    private List<MetadataFormat> metadataFormat;

    /**
     * Gets the value of the metadataFormat property.
     *
     * @return the metadata formats supported
     */
    public final List<MetadataFormat> getMetadataFormat() {
        if (metadataFormat == null) {
            metadataFormat = new ArrayList<MetadataFormat>();
        }
        return this.metadataFormat;
    }
}
