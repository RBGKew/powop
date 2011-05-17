package org.tdwg;

import java.net.URI;
import java.util.Set;

import org.dublincore.Relation;
import org.emonocot.model.marshall.DateTimeConverter;
import org.emonocot.model.marshall.UriConverter;
import org.joda.time.DateTime;
import org.tdwg.voc.LinkType;
import org.tdwg.voc.PublicationCitation;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 *
 * @author ben
 *
 */
public abstract class BaseThing {

    /**
     *
     */
    private String dcTitle;

    /**
     *
     */
    private String owlSameAs;

    /**
     *
     */
    @XStreamConverter(UriConverter.class)
    private URI dcIdentifier;

    /**
     *
     */
    @XStreamConverter(DateTimeConverter.class)
    private DateTime dctermsCreated;

    /**
     *
     */
    @XStreamConverter(DateTimeConverter.class)
    private DateTime dctermsDate;

    /**
     *
     */
    private String dcCreator;

    /**
     *
     */
    private String dcContributor;

    /**
     *
     */
    private Relation dcRelation;

    /**
     *
     */
    private String tcomAbcdEquivalence;

    /**
     *
     */
    private String tcomBerlinModelEquivalence;

    /**
     *
     */
    private String tcomDarwinCoreEquivalence;

    /**
     *
     */
    private Boolean tcomIsDeprecated;

    /**
     *
     */
    private Boolean tcomIsRestricted;

    /**
     *
     */
    private String tcomMicroReference;

    /**
     *
     */
    @XStreamImplicit
    private Set<String> tcomNotes;

    /**
     *
     */
    private String tcomPublishedIn;

    /**
     *
     */
    private String tcomTaxonomicPlacementFormal;

    /**
     *
     */
    private String tcomTaxonomicPlacementInformal;

    /**
     *
     */
    private String tcomTcsEquivalence;

    /**
     *
     */
    private PublishedInCitation tcomPublishedInCitation;

    /**
     *
     * @return the date associated with this resource
     */
    public final DateTime getDate() {
        return dctermsDate;
    }

    /**
     *
     * @param date Set the date associated with this resource
     */
    public final void setDate(final DateTime date) {
        this.dctermsDate = date;
    }

    /**
     *
     * @return the resource's creator
     */
    public final String getCreator() {
        return dcCreator;
    }

    /**
     *
     * @param creator Set the resource's creator
     */
    public final void setCreator(final String creator) {
        this.dcCreator = creator;
    }

    /**
     *
     * @return the contributor to this resource
     */
    public final String getContributor() {
        return dcContributor;
    }

    /**
     *
     * @param contributor Set the contributor to this resource
     */
    public final void setContributor(final String contributor) {
        this.dcContributor = contributor;
    }

    /**
     *
     * @return the equivalent class in ABCD
     */
    public final String getAbcdEquivalence() {
        return tcomAbcdEquivalence;
    }

    /**
     *
     * @return the equivalent class in the Berlin Model
     */
    public final String getBerlinModelEquivalence() {
        return tcomBerlinModelEquivalence;
    }

    /**
     *
     * @return the date time this resource was created
     */
    public final DateTime getCreated() {
        return dctermsCreated;
    }

    /**
     *
     * @return the equivalent class in Darwin Core
     */
    public final String getDarwinCoreEquivalence() {
        return tcomDarwinCoreEquivalence;
    }

    /**
     *
     * @return the identifier of this resource
     */
    public final URI getIdentifier() {
        return dcIdentifier;
    }

    /**
     *
     * @return the micro reference associated with this resource
     */
    public final String getMicroReference() {
        return tcomMicroReference;
    }

    /**
     *
     * @return the notes associated with this resource
     */
    public final Set<String> getNotes() {
        return tcomNotes;
    }

    /**
     *
     * @return the publication this resource was published in
     */
    public final String getPublishedIn() {
        return tcomPublishedIn;
    }

    /**
     *
     * @return a resource which is equivalent to this resource
     */
    public final String getSameAs() {
        return owlSameAs;
    }

    /**
     *
     * @return the formal taxonomic placement of this resource
     */
    public final String getTaxonomicPlacementFormal() {
        return tcomTaxonomicPlacementFormal;
    }

    /**
     *
     * @return the informal taxonomic placement of this resource
     */
    public final String getTaxonomicPlacementInformal() {
        return tcomTaxonomicPlacementInformal;
    }

    /**
     *
     * @return the equivalent class in TCS
     */
    public final String getTcsEquivalence() {
        return tcomTcsEquivalence;
    }

    /**
     *
     * @return the title of this resource
     */
    public final String getTitle() {
        return dcTitle;
    }

    /**
     *
     * @return true, if this resource is deprecated, false otherwise
     */
    public final Boolean isDeprecated() {
        return tcomIsDeprecated;
    }

    /**
     *
     * @return true, if this resource is restricted, false otherwise
     */
    public final Boolean isRestricted() {
        return tcomIsRestricted;
    }

    /**
     *
     * @param deprecated Set the deprecated status of this resource
     */
    public final void setDeprecated(final Boolean deprecated) {
        this.tcomIsDeprecated = deprecated;
    }

    /**
     *
     * @param identifier Set the identifier of this resource
     */
    public final void setIdentifier(final URI identifier) {
        this.dcIdentifier = identifier;
    }

    /**
     *
     * @param microReference
     *            Set the micro reference associated with this resource.
     */
    public final void setMicroReference(final String microReference) {
        this.tcomMicroReference = microReference;
    }

    /**
     *
     * @param notes Set the notes associated with this resource
     */
    public final void setNotes(final Set<String> notes) {
        this.tcomNotes = notes;
    }

    /**
     *
     * @param publishedIn Set the publication this resource was published in
     */
    public final void setPublishedIn(final String publishedIn) {
        this.tcomPublishedIn = publishedIn;
    }

    /**
     *
     * @param restricted Set the restricted status of this resource
     */
    public final void setRestricted(final Boolean restricted) {
        this.tcomIsRestricted = restricted;
    }

    /**
     *
     * @param sameAs Set an equivalent resource to this resource
     */
    public final void setSameAs(final String sameAs) {
        this.owlSameAs = sameAs;
    }

    /**
     *
     * @param taxonomicPlacementFormal
     *            Set the formal taxonomic placement of this resource
     */
    public final void setTaxonomicPlacementFormal(
            final String taxonomicPlacementFormal) {
        this.tcomTaxonomicPlacementFormal = taxonomicPlacementFormal;
    }

    /**
     *
     * @param taxonomicPlacementInformal
     *            Set the informal taxonomic placement of this resource
     */
    public final void setTaxonomicPlacementInformal(
            final String taxonomicPlacementInformal) {
        this.tcomTaxonomicPlacementInformal = taxonomicPlacementInformal;
    }

    /**
     *
     * @param tcsEquivalence Set the equivalent class in TCS
     */
    public final void setTcsEquivalence(final String tcsEquivalence) {
        this.tcomTcsEquivalence = tcsEquivalence;
    }

    /**
     *
     * @param title Set the title of this resource
     */
    public final void setTitle(final String title) {
        this.dcTitle = title;
    }

    /**
     *
     * @param created Set the date time this resource was created
     */
    public final void setCreated(final DateTime created) {
        this.dctermsCreated = created;
    }

    /**
     *
     * @param publicationCitation
     *            Set the publication this resource was published in
     */
    public final void setPublishedInCitation(
            final PublicationCitation publicationCitation) {
        this.tcomPublishedInCitation = new PublishedInCitation(
                publicationCitation, false);
    }

    /**
     *
     * @return the publication that this resource was published in
     */
    public final PublicationCitation getPublishedInCitation() {
        if (tcomPublishedInCitation != null) {
         return tcomPublishedInCitation.getPublicationCitation();
        } else {
            return null;
        }
    }

    /**
     *
     * @param publicationCitation Set the published in citation relation
     */
    public final void setPublishedInCitationRelation(
            final PublicationCitation publicationCitation) {
        this.tcomPublishedInCitation = new PublishedInCitation(
                publicationCitation, true);
    }

    /**
     *
     * @return the published in citation relation
     */
    public final PublicationCitation getPublishedInCitationRelation() {
        if (tcomPublishedInCitation != null) {
            return tcomPublishedInCitation.getPublicationCitation();
        } else {
            return null;
        }
    }

    /**
     *
     * @param relation Set a relation
     */
    public final void setRelation(final Relation relation) {
        this.dcRelation = relation;
    }

    /**
     *
     * @return a relation
     */
    public final Relation getRelation() {
        return dcRelation;
    }

    /**
     *
     * @author ben
     *
     */
    public static class PublishedInCitation extends LinkType {

        /**
         *
         */
        private PublicationCitation tpcPublicationCitation;

        /**
         *
         */
        protected PublishedInCitation() {
        }

        /**
         *
         * @param newPublicationCitation Set the publication citation
         * @param useRelation Set as a relation, not as an object
         */
        protected PublishedInCitation(
                final PublicationCitation newPublicationCitation,
                final boolean useRelation) {
            if (useRelation) {
                if (newPublicationCitation != null
                        && newPublicationCitation.getIdentifier() != null) {
                    this.setResource(newPublicationCitation.getIdentifier());
                } else {
                    this.tpcPublicationCitation = newPublicationCitation;
                }
            } else {
                this.tpcPublicationCitation = newPublicationCitation;
            }
        }

        /**
         *
         * @param newPublicationCitation Set the publication citation
         */
        protected final void setPublicationCitation(
                final PublicationCitation newPublicationCitation) {
            this.tpcPublicationCitation = newPublicationCitation;
        }

        /**
         *
         * @return the publication citation
         */
        protected final PublicationCitation getPublicationCitation() {
            return tpcPublicationCitation;
        }
    }
}
