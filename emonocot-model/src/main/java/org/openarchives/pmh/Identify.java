package org.openarchives.pmh;

import java.util.ArrayList;
import java.util.List;

import org.emonocot.model.marshall.DateTimeConverter;
import org.joda.time.DateTime;
import org.openarchives.pmh.marshall.DeletedRecordConverter;
import org.openarchives.pmh.marshall.GranularityConverter;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * <p>
 * Java class for IdentifyType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="IdentifyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="repositoryName"
             type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="baseURL"
 *           type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="protocolVersion"
 *           type="{http://www.openarchives.org/OAI/2.0/}protocolVersionType"/>
 *         &lt;element name="adminEmail"
 *           type="{http://www.openarchives.org/OAI/2.0/}emailType"
 *           maxOccurs="unbounded"/>
 *         &lt;element name="earliestDatestamp"
 *           type="{http://www.openarchives.org/OAI/2.0/}UTCdatetimeType"/>
 *         &lt;element name="deletedRecord"
 *           type="{http://www.openarchives.org/OAI/2.0/}deletedRecordType"/>
 *         &lt;element name="granularity"
 *           type="{http://www.openarchives.org/OAI/2.0/}granularityType"/>
 *         &lt;element name="compression"
 *           type="{http://www.w3.org/2001/XMLSchema}string"
 *           maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="description"
 *           type="{http://www.openarchives.org/OAI/2.0/}descriptionType"
 *           maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
public class Identify {

    /**
     *
     */
    private String repositoryName;

    /**
     *
     */
    private String baseURL;

    /**
     *
     */
    private String protocolVersion;

    /**
     *
     */
    @XStreamImplicit(itemFieldName = "adminEmail")
    private List<String> adminEmail = new ArrayList<String>();

    /**
     *
     */
    @XStreamConverter(DateTimeConverter.class)
    private DateTime earliestDatestamp;

    /**
     *
     */
    @XStreamConverter(DeletedRecordConverter.class)
    private DeletedRecord deletedRecord;

    /**
     *
     */
    @XStreamConverter(GranularityConverter.class)
    private Granularity granularity;

    /**
     *
     */
    @XStreamImplicit(itemFieldName = "compression")
    private List<String> compression = new ArrayList<String>();

    /**
     *
     */
    @XStreamImplicit(itemFieldName = "description")
    private List<String> description = new ArrayList<String>();

    /**
     * Gets the value of the repositoryName property.
     *
     * @return the name of the repostory
     */
    public final String getRepositoryName() {
        return repositoryName;
    }

    /**
     * Sets the value of the repositoryName property.
     *
     * @param value Set the repository name
     */
    public final void setRepositoryName(final String value) {
        this.repositoryName = value;
    }

    /**
     * Gets the value of the baseURL property.
     *
     * @return the base url of the repository
     */
    public final String getBaseURL() {
        return baseURL;
    }

    /**
     * Sets the value of the baseURL property.
     *
     * @param value Set the base url of the repository
     */
    public final void setBaseURL(final String value) {
        this.baseURL = value;
    }

    /**
     * Gets the value of the protocolVersion property.
     *
     * @return the protocol version of the repository
     */
    public final String getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Sets the value of the protocolVersion property.
     *
     * @param value Set the protocol version of the repository
     */
    public final void setProtocolVersion(final String value) {
        this.protocolVersion = value;
    }

    /**
     * Gets the value of the adminEmail property.
     *
     * @return the emails of the administrators of the repository
     */
    public final List<String> getAdminEmail() {
        if (adminEmail == null) {
            adminEmail = new ArrayList<String>();
        }
        return this.adminEmail;
    }

    /**
     * Gets the value of the earliestDatestamp property.
     *
     * @return the date of the earliest record in the repository
     */
    public final DateTime getEarliestDatestamp() {
        return earliestDatestamp;
    }

    /**
     * Sets the value of the earliestDatestamp property.
     *
     * @param dateTime Set the date of the earliest record in the repository
     */
    public final void setEarliestDatestamp(final DateTime dateTime) {
        this.earliestDatestamp = dateTime;
    }

    /**
     * Gets the value of the deletedRecord property.
     *
     * @return the deleted record property
     */
    public final DeletedRecord getDeletedRecord() {
        return deletedRecord;
    }

    /**
     * Sets the value of the deletedRecord property.
     *
     * @param value Set how the repository handles deleted records
     */
    public final void setDeletedRecord(final DeletedRecord value) {
        this.deletedRecord = value;
    }

    /**
     * Gets the value of the granularity property.
     *
     * @return the granularity of the repository
     */
    public final Granularity getGranularity() {
        return granularity;
    }

    /**
     * Sets the value of the granularity property.
     *
     * @param value Set the granularity of the repository
     */
    public final void setGranularity(final Granularity value) {
        this.granularity = value;
    }

    /**
     * Gets the value of the compression property.
     *
     * @return the compression types supported by the repository
     */
    public final List<String> getCompression() {
        if (compression == null) {
            compression = new ArrayList<String>();
        }
        return this.compression;
    }

    /**
     * Gets the value of the description property.
     *
     * @return descriptions of the repository
     */
    public final List<String> getDescription() {
        if (description == null) {
            description = new ArrayList<String>();
        }
        return this.description;
    }

    /**
     *
     * @param newDescription Set the new description
     */
    public final void setDescription(final List<String> newDescription) {
        this.description = newDescription;
    }

}

