package org.emonocot.job.scratchpads.model;

import org.emonocot.job.scratchpads.model.convert.EoLAgentConverter;
import org.emonocot.model.taxon.Taxon;

import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 *
 * @author ben
 *
 */
public class EoLDataObject {
    /**
     * Maps to the dataType element.
     */
    private String dataType;

    /**
     * Maps to the agent element.
     */
    @XStreamConverter(EoLAgentConverter.class)
    private EoLAgent agent;

    /**
     * Maps to the dcterms:created element.
     */
    private String created;

    /**
     * Maps to the dcterms:modified element.
     */
    private String modified;

    /**
     * Maps to the dc:title element.
     */
    private String title;

    /**
     * Maps to the license element.
     */
    private String license;

    /**
     * Maps to the dc:source element.
     */
    private String source;

    /**
     * Maps to the subject element.
     */
    private String subject;

    /**
     * Maps to the dc:description element.
     */
    private String description;

    /**
     *
     */
    private Taxon taxon;

    /**
     *
     * @return The agent associated with this resource.
     */
    public final EoLAgent getAgent() {
        return agent;
    }

    /**
     *
     * @return The time this resource was created.
     */
    public final String getCreated() {
        return created;
    }

    /**
     *
     * @return The time this resource was last modified.
     */
    public final String getModified() {
        return modified;
    }

    /**
     *
     * @return The title of this resource.
     */
    public final String getTitle() {
        return title;
    }

    /**
     *
     * @return The license of this resource.
     */
    public final String getLicense() {
        return license;
    }

    /**
     *
     * @return The source of this resource.
     */
    public final String getSource() {
        return source;
    }

    /**
     *
     * @return The subject of this resource.
     */
    public final String getSubject() {
        return subject;
    }

    /**
     *
     * @return The description of this resource.
     */
    public final String getDescription() {
        return description;
    }

    /**
     *
     * @return The data type of this resource.
     */
    public final String getDataType() {
        return dataType;
    }

    /**
     *
     * @param newTaxon Set the taxon this data object is about.
     */
    public final void setTaxon(final Taxon newTaxon) {
        this.taxon = newTaxon;
    }

    /**
     *
     * @return The taxon this data object is about.
     */
    public final Taxon getTaxon() {
        return taxon;
    }

    /**
     *
     * @param newCreated Set the time this object was created.
     */
    public final void setCreated(final String newCreated) {
        this.created = newCreated;
    }

    /**
     *
     * @param newModified Set the time this object was last modified.
     */
    public final void setModified(final String newModified) {
        this.modified = newModified;
    }

    /**
     *
     * @param newLicense Set the license of this data object.
     */
    public final void setLicense(final String newLicense) {
        this.license = newLicense;
    }

    /**
     *
     * @param newSubject Set the subject of this data object.
     */
    public final void setSubject(final String newSubject) {
        this.subject = newSubject;
    }

    /**
     *
     * @param newSource Set the source of this data object.
     */
    public final void setSource(final String newSource) {
        this.source = newSource;
    }

    /**
     *
     * @param newDescription Set the description of this data object.
     */
    public final void setDescription(final String newDescription) {
        this.description = newDescription;
    }

    /**
     *
     * @param newAgent Set the agent associated with this data object.
     */
    public final void setAgent(final EoLAgent newAgent) {
        this.agent = newAgent;
    }
}
