package org.openarchives.pmh;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.emonocot.model.marshall.xml.DateTimeConverter;
import org.emonocot.model.marshall.xml.UriElementConverter;
import org.joda.time.DateTime;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * A header has a unique identifier, a datestamp, and setSpec(s) in case the
 * item from which the record is disseminated belongs to set(s). the header can
 * carry a deleted status indicating that the record is deleted.
 *
 * <p>
 * Java class for headerType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="headerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifier"
 *           type="{http://www.openarchives.org/OAI/2.0/}identifierType"/>
 *         &lt;element name="datestamp"
 *           type="{http://www.openarchives.org/OAI/2.0/}UTCdatetimeType"/>
 *         &lt;element name="setSpec"
 *           type="{http://www.openarchives.org/OAI/2.0/}setSpecType"
 *             maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="status"
 *         type="{http://www.openarchives.org/OAI/2.0/}statusType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
public class Header {

    /**
     *
     */
    @XStreamConverter(UriElementConverter.class)
    private URI identifier;

    /**
     *
     */
    @XStreamConverter(DateTimeConverter.class)
    private DateTime datestamp;

    /**
     *
     */
    private List<String> setSpec;

    /**
     *
     */
    @XStreamAsAttribute
    private Status status;

    /**
     * Gets the value of the identifier property.
     *
     * @return the value of the identifier property.
     */
    public final URI getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     *
     * @param value Set the value of the identifier property.
     */
    public final void setIdentifier(final URI value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the datestamp property.
     *
     * @return the value of the datestamp property
     */
    public final DateTime getDatestamp() {
        return datestamp;
    }

    /**
     * Sets the value of the datestamp property.
     *
     * @param value Set the value of the datestamp property
     */
    public final void setDatestamp(final DateTime value) {
        this.datestamp = value;
    }

    /**
     * Gets the value of the setSpec property.
     *
     * @return a list of set specifications
     */
    public final List<String> getSetSpec() {
        if (setSpec == null) {
            setSpec = new ArrayList<String>();
        }
        return this.setSpec;
    }

    /**
     * Gets the value of the status property.
     *
     * @return the value of the status property
     */
    public final Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value Set the header status
     */
    public final void setStatus(final Status value) {
        this.status = value;
    }
}
