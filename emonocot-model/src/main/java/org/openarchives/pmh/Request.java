package org.openarchives.pmh;

import org.joda.time.DateTime;

/**
 * Define requestType, indicating the protocol request that led to the response.
 * Element content is BASE-URL, attributes are arguments of protocol request,
 * attribute-values are values of arguments of protocol request
 *
 * <p>
 * Java class for requestType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="requestType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>anyURI">
 *       &lt;attribute name="verb"
 *         type="{http://www.openarchives.org/OAI/2.0/}verbType" />
 *       &lt;attribute name="identifier"
 *         type="{http://www.openarchives.org/OAI/2.0/}identifierType" />
 *       &lt;attribute name="metadataPrefix"
 *         type="{http://www.openarchives.org/OAI/2.0/}metadataPrefixType" />
 *       &lt;attribute
 *         name="from"
 *         type="{http://www.openarchives.org/OAI/2.0/}UTCdatetimeType" />
 *       &lt;attribute
 *         name="until"
 *         type="{http://www.openarchives.org/OAI/2.0/}UTCdatetimeType" />
 *       &lt;attribute
 *         name="set"
 *         type="{http://www.openarchives.org/OAI/2.0/}setSpecType" />
 *       &lt;attribute
 *         name="resumptionToken"
 *         type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
public class Request {
    /**
     *
     */
    private String value;

    /**
     *
     */
    private Verb verb;

    /**
     *
     */
    private String identifier;

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
    private String set;

    /**
     *
     */
    private String resumptionToken;

    /**
     * Gets the value of the value property.
     *
     * @return The request uri as a string
     */
    public final String getValue() {
        return value;
    }
    /**
     * Sets the value of the value property.
     *
     * @param newValue Set the value property
     */
    public final void setValue(final String newValue) {
        this.value = newValue;
    }
    /**
     * Gets the value of the verb property.
     *
     * @return the verb of the request
     */
    public final Verb getVerb() {
        return verb;
    }

    /**
     * Sets the value of the verb property.
     *
     * @param newVerb Set the verb property
     */
    public final void setVerb(final Verb newVerb) {
        this.verb = newVerb;
    }
    /**
     * Gets the value of the identifier property.
     *
     * @return the identifier of the request
     */
    public final String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     *
     * @param newIdentifier Set the identifier property
     */
    public final void setIdentifier(final String newIdentifier) {
        this.identifier = newIdentifier;
    }

    /**
     * Gets the value of the metadataPrefix property.
     *
     * @return the metadata prefix of the request
     */
    public final MetadataPrefix getMetadataPrefix() {
        return metadataPrefix;
    }

    /**
     * Sets the value of the metadataPrefix property.
     *
     * @param object Set the metadata prefix of the request.
     */
    public final void setMetadataPrefix(final MetadataPrefix object) {
        this.metadataPrefix = object;
    }

    /**
     * Gets the value of the from property.
     *
     * @return the from property
     */
    public final DateTime getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     *
     * @param dateTime Set the from property
     *
     */
    public final void setFrom(final DateTime dateTime) {
        this.from = dateTime;
    }

    /**
     * Gets the value of the until property.
     *
     * @return the until property
     */
    public final DateTime getUntil() {
        return until;
    }

    /**
     * Sets the value of the until property.
     *
     * @param newUntil Set the until property
     */
    public final void setUntil(final DateTime newUntil) {
        this.until = newUntil;
    }

    /**
     * Gets the value of the set property.
     *
     * @return the value of the set spec property
     */
    public final String getSet() {
        return set;
    }

    /**
     * Sets the value of the set property.
     *
     * @param setSpec Set the set spec property
     */
    public final void setSet(final String setSpec) {
        this.set = setSpec;
    }

    /**
     * Gets the value of the resumptionToken property.
     *
     * @return the resumption token
     */
    public final String getResumptionToken() {
        return resumptionToken;
    }

    /**
     * Sets the value of the resumptionToken property.
     *
     * @param newResumptionToken Set the resumption token
     */
    public final void setResumptionToken(final String newResumptionToken) {
        this.resumptionToken = newResumptionToken;
    }
}
