package org.openarchives.pmh;

import java.util.ArrayList;
import java.util.List;

import org.emonocot.model.marshall.DateTimeConverter;
import org.joda.time.DateTime;
import org.openarchives.pmh.marshall.ErrorConverter;
import org.openarchives.pmh.marshall.RequestConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * <p>Java class for OAI-PMHtype complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="OAI-PMHtype">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="responseDate"
 *             type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="request"
 *           type="{http://www.openarchives.org/OAI/2.0/}requestType"/>
 *         &lt;choice>
 *           &lt;element name="error"
 *             type="{http://www.openarchives.org/OAI/2.0/}OAI-PMHerrorType"
 *             maxOccurs="unbounded"/>
 *           &lt;element name="Identify"
 *           type="{http://www.openarchives.org/OAI/2.0/}IdentifyType"/>
 *           &lt;element name="ListMetadataFormats"
 *             type="{http://www.openarchives.org/OAI/2.0/}
 *               ListMetadataFormatsType"/>
 *           &lt;element name="ListSets"
 *             type="{http://www.openarchives.org/OAI/2.0/}ListSetsType"/>
 *           &lt;element
 *             name="GetRecord"
 *             type="{http://www.openarchives.org/OAI/2.0/}GetRecordType"/>
 *           &lt;element
 *           name="ListIdentifiers"
 *           type="{http://www.openarchives.org/OAI/2.0/}ListIdentifiersType"/>
 *           &lt;element
 *           name="ListRecords"
 *           type="{http://www.openarchives.org/OAI/2.0/}ListRecordsType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XStreamAlias("OAI-PMH")
public class OAIPMH {

    /**
     *
     */
    @XStreamConverter(DateTimeConverter.class)
    private DateTime  responseDate = new DateTime().withMillisOfSecond(0);

    /**
     *
     */
    @XStreamConverter(RequestConverter.class)
    private Request request = new Request();

    /**
     *
     */
    @XStreamConverter(ErrorConverter.class)
    private List<Error> error;

    /**
     *
     */
    @XStreamAlias("Identify")
    private Identify identify;

    /**
     *
     */
    @XStreamAlias("ListMetadataFormats")
    private ListMetadataFormats listMetadataFormats;

    /**
     *
     */
    @XStreamAlias("ListSets")
    private ListSets listSets;

    /**
     *
     */
    @XStreamAlias("GetRecord")
    private GetRecord getRecord;

    /**
     *
     */
    @XStreamAlias("ListIdentifiers")
    private ListIdentifiers listIdentifiers;

    /**
     *
     */
    @XStreamAlias("ListRecords")
    private ListRecords listRecords;

    /**
     * Bit of a hack to insert the correct namespace.
     */
    @XStreamAlias("xmlns:oai_dc")
    @XStreamAsAttribute
    private String xmlnsOaiDcNamespace
        = "http://www.openarchives.org/OAI/2.0/oai_dc/";

    /**
     * Bit of a hack to insert the correct namespace.
     */
    @XStreamAlias("xmlns:dc")
    @XStreamAsAttribute
    private String xmlnsDcNamespace
        = "http://purl.org/dc/elements/1.1/";

    /**
     * Gets the value of the responseDate property.
     *
     * @return The date the response was submitted
     */
    public final DateTime getResponseDate() {
        return responseDate;
    }

    /**
     * Sets the value of the responseDate property.
     *
     * @param value Set the response date
     */
    public final void setResponseDate(final DateTime value) {
        this.responseDate = value;
    }

    /**
     * Gets the value of the request property.
     *
     * @return the request
     */
    public final Request getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     *
     * @param value Set the request
     */
    public final void setRequest(final Request value) {
        this.request = value;
    }

    /**
     * Gets the value of the error property.
     *
     * @return A list of error objects representing the errors
     */
    public final List<Error> getError() {
        if (error == null) {
            error = new ArrayList<Error>();
        }
        return this.error;
    }

    /**
     * Gets the value of the identify property.
     *
     * @return The response to an identify request
     */
    public final Identify getIdentify() {
        return identify;
    }

    /**
     * Sets the value of the identify property.
     *
     * @param value Set the identify response
     */
    public final void setIdentify(final Identify value) {
        this.identify = value;
    }

    /**
     * Gets the value of the listMetadataFormats property.
     *
     * @return The response to a list metadata formats request
     */
    public final ListMetadataFormats getListMetadataFormats() {
        return listMetadataFormats;
    }

    /**
     * Sets the value of the listMetadataFormats property.
     *
     * @param value Set the list metadata formats response
     */
    public final void setListMetadataFormats(final ListMetadataFormats value) {
        this.listMetadataFormats = value;
    }

    /**
     * Gets the value of the listSets property.
     *
     * @return the response to a list sets request
     */
    public final ListSets getListSets() {
        return listSets;
    }

    /**
     * Sets the value of the listSets property.
     *
     * @param value Set the value of the list sets response
     */
    public final void setListSets(final ListSets value) {
        this.listSets = value;
    }

    /**
     * Gets the value of the getRecord property.
     *
     * @return the response to a get record request
     */
    public final GetRecord getGetRecord() {
        return getRecord;
    }

    /**
     * Sets the value of the getRecord property.
     *
     * @param value Set the value of the get record response
     */
    public final void setGetRecord(final GetRecord value) {
        this.getRecord = value;
    }

    /**
     * Gets the value of the listIdentifiers property.
     *
     * @return the response to a list identifiers request
     */
    public final ListIdentifiers getListIdentifiers() {
        return listIdentifiers;
    }

    /**
     * Sets the value of the listIdentifiers property.
     *
     * @param value Set the value of the list identifiers response
     */
    public final void setListIdentifiers(final ListIdentifiers value) {
        this.listIdentifiers = value;
    }

    /**
     * Gets the value of the listRecords property.
     *
     * @return the response to a list records request
     */
    public final ListRecords getListRecords() {
        return listRecords;
    }

    /**
     * Sets the value of the listRecords property.
     *
     * @param value Set the value of the list records response
     */
    public final void setListRecords(final ListRecords value) {
        this.listRecords = value;
    }
}
