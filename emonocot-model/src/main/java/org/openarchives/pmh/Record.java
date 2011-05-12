package org.openarchives.pmh;

import java.util.ArrayList;
import java.util.List;

/**
 * A record has a header, a metadata part, and an optional about container
 *
 * <p>
 * Java class for recordType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="recordType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header"
 *           type="{http://www.openarchives.org/OAI/2.0/}headerType"/>
 *         &lt;element name="metadata"
 *           type="{http://www.openarchives.org/OAI/2.0/}metadataType"
 *           minOccurs="0"/>
 *         &lt;element name="about"
 *           type="{http://www.openarchives.org/OAI/2.0/}aboutType"
 *           maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */

public class Record {

    /**
     *
     */
    private Header header;

    /**
     *
     */
    private Metadata metadata;

    /**
     *
     */
    private List<About> about;

    /**
     * Gets the value of the header property.
     *
     * @return the value of the header property
     */
    public final Header getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     *
     * @param value Set the header property
     */
    public final void setHeader(final Header value) {
        this.header = value;
    }

    /**
     * Gets the value of the metadata property.
     *
     * @return the value of the metadata property
     */
    public final Metadata getMetadata() {
        return metadata;
    }

    /**
     * Sets the value of the metadata property.
     *
     * @param value Set the value of the metadata property
     */
    public final void setMetadata(final Metadata value) {
        this.metadata = value;
    }

    /**
     * Gets the value of the about property.
     *
     * @return the value of the about property.
     */
    public final List<About> getAbout() {
        if (about == null) {
            about = new ArrayList<About>();
        }
        return this.about;
    }
}
