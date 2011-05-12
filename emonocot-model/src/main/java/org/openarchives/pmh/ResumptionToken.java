package org.openarchives.pmh;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.emonocot.model.marshall.DateTimeConverter;
import org.joda.time.DateTime;

import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * A resumptionToken may have 3 optional attributes and can be used in ListSets,
 * ListIdentifiers, ListRecords responses.
 *
 * <p>
 * Java class for resumptionTokenType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="resumptionTokenType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="expirationDate"
 *       type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="completeListSize"
 *       type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="cursor"
 *       type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
public class ResumptionToken implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private String value;

    /**
     *
     */
    private DateTime expirationDate;

    /**
     *
     */
    private BigInteger completeListSize;

    /**
     *
     */
    private BigInteger cursor;


    /**
     *
     */
    private MetadataPrefix metadataPrefix;

    /**
     *
     */
    private DateTime from;

    /**
     *
     */
    private DateTime until;

    /**
     *
     */
    private SetSpec set;

    /**
     *
     * @param newSize The size of the complete list
     * @param newCurrentIndex The page number of the current list
     * @param newFrom The earliest date of a record in the result set
     * @param newUntil The latest date of a record in the result set
     * @param newMetadataPrefix The metadata prefix for rendering the list
     * @param newSet A set to restrict the results by
     */
    public ResumptionToken(final Integer newSize,
            final Integer newCurrentIndex, final DateTime newFrom,
            final DateTime newUntil, final MetadataPrefix newMetadataPrefix,
            final SetSpec newSet) {
        this.from = newFrom;
        this.until = newUntil;
        this.metadataPrefix = newMetadataPrefix;
        this.set = newSet;
        this.value = UUID.randomUUID().toString();
        this.completeListSize = BigInteger.valueOf(newSize);
        this.cursor = BigInteger.valueOf(newSize * newCurrentIndex);
    }

    /**
     *
     */
    public ResumptionToken() { }

    /**
     * Gets the value of the value property.
     *
     * @return The resumption token
     */
    public final String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param newValue Set the resumption token
     */
    public final void setValue(final String newValue) {
        this.value = newValue;
    }

    /**
     * Gets the value of the expirationDate property.
     *
     * @return the expiration date of this resumption token
     */
    public final DateTime getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the value of the expirationDate property.
     *
     * @param newExpirationDate Set the expiration date of the resumption token
     */
    public final void setExpirationDate(final DateTime newExpirationDate) {
        this.expirationDate = newExpirationDate;
    }

    /**
     * Gets the value of the completeListSize property.
     *
     * @return the complete size of the list
     */
    public final BigInteger getCompleteListSize() {
        return completeListSize;
    }

    /**
     * Sets the value of the completeListSize property.
     *
     * @param newCompleteListSize Set the complete list size
     */
    public final void setCompleteListSize(
            final BigInteger newCompleteListSize) {
        this.completeListSize = newCompleteListSize;
    }

    /**
     * Gets the value of the cursor property.
     *
     * @return the cursor
     */
    public final BigInteger getCursor() {
        return cursor;
    }

    /**
     * Sets the value of the cursor property.
     *
     * @param newCursor Set the value of the cursor.
     */
    public final void setCursor(final BigInteger newCursor) {
        this.cursor = newCursor;
    }

    /**
     *
     * @return the metadata prefix associated with this request
     */
    public final MetadataPrefix getMetadataPrefix() {
        return metadataPrefix;
    }

    /**
     *
     * @return the date time which records must be later than
     */
    public final DateTime getFrom() {
        return from;
    }

    /**
     *
     * @return the date time which records must be earlier than
     */
    public final DateTime getUntil() {
        return until;
    }

    /**
     *
     * @return the set which restricts the results
     */
    public final SetSpec getSet() {
        return set;
    }

    /**
     *
     * @param size Set the total number of results matching these parameters
     * @param currentIndex Set the current page
     */
    public final void updateResults(final Integer size,
            final Integer currentIndex) {
        this.completeListSize = BigInteger.valueOf(size);
        this.cursor = BigInteger.valueOf(size * currentIndex);
    }

    /**
     *
     * @return a new empty resumption token
     */
    public static ResumptionToken emptyResumptionToken() {
        return new ResumptionToken();
    }
}

