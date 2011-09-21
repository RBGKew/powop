package org.openarchives.pmh;

import java.util.ArrayList;
import java.util.List;

import org.openarchives.pmh.marshall.xml.ResumptionTokenConverter;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * <p>
 * Java class for ListRecordsType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="ListRecordsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="record"
 *             type="{http://www.openarchives.org/OAI/2.0/}recordType"
 *             maxOccurs="unbounded"/>
 *         &lt;element name="resumptionToken"
 *             type="{http://www.openarchives.org/OAI/2.0/}resumptionTokenType"
 *             minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
public class ListRecords {

    /**
     *
     */
    @XStreamImplicit(itemFieldName = "record")
    private List<Record> record;

    /**
     *
     */
    @XStreamConverter(ResumptionTokenConverter.class)
    private ResumptionToken resumptionToken;

    /**
     * Gets the value of the record property.
     *
     * @return the records which match the request
     */
    public final List<Record> getRecord() {
        if (record == null) {
            record = new ArrayList<Record>();
        }
        return this.record;
    }

    /**
     * Gets the value of the resumptionToken property.
     *
     * @return the resumption token
     */
    public final ResumptionToken getResumptionToken() {
        return resumptionToken;
    }

    /**
     * Sets the value of the resumptionToken property.
     *
     * @param value Set the resumption token
     */
    public final void setResumptionToken(final ResumptionToken value) {
        this.resumptionToken = value;
    }
}
