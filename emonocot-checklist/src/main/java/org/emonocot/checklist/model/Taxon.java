package org.emonocot.checklist.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
@Entity
@Table(name = "vwMonocot_Name")
public class Taxon implements IdentifiableEntity<String> {

    /**
     *
     */
    public static final String IDENTIFIER_PREFIX = "urn:kew.org:wcs:taxon:";

    /**
    *
    */
    public static final String NAME_IDENTIFIER_PREFIX = "urn:kew.org:wcs:name:";

    /**
     *
     */
    @Id
    @Column(name = "Plant_name_id")
    private Long id;

    /**
     *
     */
    @Column(name = "Institute_id")
    private String nameId;

    /**
     *
     */
    @Column(name = "Full_epithet")
    private String name;

    /**
     *
     */
    @Transient
    private Rank rank = null;

    /**
     *
     */
    @Column(name = "Family")
    private String family;

    /**
     *
     */
    @Column(name = "Genus_hybrid_marker")
    private String genusHybridMarker;

    /**
     *
     */
    @Column(name = "Genus")
    private String genus;

    /**
     *
     */
    @Column(name = "Species_hybrid_marker")
    private String speciesHybridMarker;

    /**
     *
     */
    @Column(name = "Species")
    private String species;

    /**
     *
     */
    @Column(name = "Infraspecific_rank")
    private String infraspecificRank;

    /**
     *
     */
    @Column(name = "Infraspecific_epithet")
    private String infraspecificEpithet;

    /**
     *
     */
    @Column(name = "Date_of_entry")
    @Type(type = "dateTimeUserType")
    private DateTime dateEntered;

    /**
     *
     */
    @Column(name = "Modified_date")
    @Type(type = "dateTimeUserType")
    private DateTime dateModified;

    /**
     *
     */
    @Column(name = "Deleted_date", nullable = true)
    @Type(type = "dateTimeUserType")
    private DateTime dateDeleted;

    /**
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Accepted_plant_name_id")
    @Where(clause = "Plant_name_id > 0")
    private Taxon acceptedName;

    /**
     *
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "Accepted_plant_name_id")
    @Where(clause = "Plant_name_id > 0")
    private Set<Taxon> synonyms = new HashSet<Taxon>();

    /**
     *
     */
    @Transient
    private Taxon parentTaxon;

    /**
     *
     */
    @Transient
    private Set<Taxon> childTaxa = new HashSet<Taxon>();

    /**
     *
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "Plant_name_id")
    private Set<Distribution> distribution = new HashSet<Distribution>();

    /**
     * @param newId
     *            Set the id
     */
    public void setId(final Long newId) {
        this.id = newId;
    }

    public final String getIdentifier() {
        return Taxon.IDENTIFIER_PREFIX + this.id;
    }

    public final void setIdentifier(final String identifier) {
        if (identifier.startsWith(Taxon.IDENTIFIER_PREFIX)) {
            try {
                this.id = Long.parseLong(identifier
                        .substring(Taxon.IDENTIFIER_PREFIX.length()));
            } catch (Exception e) {
                throw new IllegalArgumentException(identifier
                        + " is not a valid identifier format");
            }
        } else {
            throw new IllegalArgumentException(identifier
                    + " is not a valid identifier format");
        }
    }

    /**
     *
     * @return the name id of the taxon
     */
    public final String getNameId() {
        if (nameId != null) {
            return nameId;
        } else {
            return Taxon.NAME_IDENTIFIER_PREFIX + this.id;
        }
    }

    /**
     *
     * @param string
     *            Set the identifier of the name in the taxon
     */
    public final void setNameId(final String string) {
        this.nameId = string;
    }

    /**
     *
     * @return the name of the taxon
     */
    public final String getName() {
        return name;
    }

    /**
     *
     * @param string
     *            Set the name of the taxon
     */
    public final void setName(final String string) {
        this.name = string;
    }

    /**
     * @return the rank
     */
    public final Rank getRank() {
        if (rank == null) { // then set it
            if (species == null && genus != null) {
                this.rank = Rank.GENUS;
            } else if (infraspecificEpithet == null) {
                this.rank = Rank.SPECIES;
            } else if (infraspecificRank != null) {
                // if this grows loop through Rank.values()
                if (infraspecificRank.contains(
                        Rank.SUBSPECIES.getAbbreviation())) {
                    this.rank = Rank.SUBSPECIES;
                } else if (infraspecificRank.contains(
                        Rank.VARIETY.getAbbreviation())) {
                    this.rank = Rank.VARIETY;
                } else {
                    this.rank = Rank.INFRASPECIFIC;
                }
            } else {
                setRank(Rank.UNKNOWN);
            }
        }
        return rank;
    }

    /**
     * @param rank
     *            the rank to set
     */
    public final void setRank(final Rank rank) {
        this.rank = rank;
    }

    /**
     * @return the family
     */
    public final String getFamily() {
        return family;
    }

    /**
     * @param family
     *            the family to set
     */
    public final void setFamily(final String family) {
        this.family = family;
    }

    /**
     * @return the genusHybridMarker
     */
    public final String getGenusHybridMarker() {
        return genusHybridMarker;
    }

    /**
     * @param genusHybridMarker
     *            the genusHybridMarker to set
     */
    public final void setGenusHybridMarker(final String genusHybridMarker) {
        this.genusHybridMarker = genusHybridMarker;
    }

    /**
     * @return the genus
     */
    public final String getGenus() {
        return genus;
    }

    /**
     * @param genus
     *            the genus to set
     */
    public final void setGenus(final String genus) {
        this.genus = genus;
    }

    /**
     * @return the speciesHybridMarker
     */
    public final String getSpeciesHybridMarker() {
        return speciesHybridMarker;
    }

    /**
     * @param speciesHybridMarker
     *            the speciesHybridMarker to set
     */
    public final void setSpeciesHybridMarker(final String speciesHybridMarker) {
        this.speciesHybridMarker = speciesHybridMarker;
    }

    /**
     * @return the species
     */
    public final String getSpecies() {
        return species;
    }

    /**
     * @param species
     *            the species to set
     */
    public final void setSpecies(final String species) {
        this.species = species;
    }

    /**
     * @return the infraspecificRank
     *
     */
    public final String getInfraspecificRank() {
        return infraspecificRank;
    }

    /**
     * @param infraspecificRank
     *            the infraspecificRank to set
     */
    public final void setInfraspecificRank(final String infraspecificRank) {
        this.infraspecificRank = infraspecificRank;
    }

    /**
     * @return the infraspecificEpithet
     */
    public final String getInfraspecificEpithet() {
        return infraspecificEpithet;
    }

    /**
     * @param infraspecificEpithet
     *            the infraspecificEpithet to set
     */
    public final void setInfraspecificEpithet(final String infraspecificEpithet) {
        this.infraspecificEpithet = infraspecificEpithet;
    }

    /**
     * @return the dateEntered
     */
    public DateTime getDateEntered() {
        return dateEntered;
    }

    /**
     * @param dateEntered the dateEntered to set
     */
    public void setDateEntered(DateTime dateEntered) {
        this.dateEntered = dateEntered;
    }

    /**
     * @return the Date the taxon was last modified
     */
    public final DateTime getDateModified() {
        return dateModified;
    }

    /**
     * @param dateModified
     *            Sets the date the taxon was last modified
     */
    public final void setDateModified(final DateTime dateModified) {
        this.dateModified = dateModified;
    }

    /**
     * @return the date that the taxon was deleted (null if it has not been
     *         deleted)
     */
    public final DateTime getDateDeleted() {
        return dateDeleted;
    }

    /**
     * @param dateDeleted
     *            Sets the date the taxon was deleted
     */
    public final void setDateDeleted(final DateTime dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    /**
     *
     * @return the accepted name of the synonym
     */
    public final Taxon getAcceptedName() {
        return acceptedName;
    }

    /**
     *
     * @param newAcceptedName
     *            Set the accepted name of the synonym
     */
    public final void setAcceptedName(final Taxon newAcceptedName) {
        this.acceptedName = newAcceptedName;
    }

    /**
     *
     * @return the synonyms of this taxon
     */
    public final Set<Taxon> getSynonyms() {
        return synonyms;
    }

    /**
     *
     * @param newSynonyms
     *            Set the synonyms of this taxon
     */
    public final void setSynonyms(final Set<Taxon> newSynonyms) {
        this.synonyms = newSynonyms;
    }

    /**
     * @return the parentTaxon
     */
    public final Taxon getParentTaxon() {
        return parentTaxon;
    }

    /**
     * @param parentTaxon
     *            the parentTaxon to set
     */
    public final void setParentTaxon(final Taxon parentTaxon) {
        this.parentTaxon = parentTaxon;
    }

    /**
     * @return the childTaxa
     */
    public final Set<Taxon> getChildTaxa() {
        return childTaxa;
    }

    /**
     * @param childTaxa
     *            the childTaxa to set
     */
    public final void setChildTaxa(final Set<Taxon> childTaxa) {
        this.childTaxa = childTaxa;
    }

    /**
     *
     * @return the distribution of this taxon
     */
    public final Set<Distribution> getDistribution() {
        return distribution;
    }

    /**
     *
     * @param newDistribution
     *            Set the distribution of this taxon
     */
    public final void setDistribution(final Set<Distribution> newDistribution) {
        this.distribution = newDistribution;
    }
}
