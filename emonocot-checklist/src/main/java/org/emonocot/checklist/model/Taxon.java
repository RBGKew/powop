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

import org.hibernate.annotations.Where;

/**
 *
 * @author ben
 *
 */
@Entity
@Table(name = "Plant_Name")
public class Taxon implements IdentifiableEntity<String> {

    /**
     * TODO Define an identifier prefix for real.
     */
    public static final String IDENTIFIER_PREFIX = "urn:lsid:kew.org:taxon:";

    /**
     *
     */
    @Id
    @Column(name = "Plant_name_id")
    private Long id;

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
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "Plant_name_id")
    private Set<Distribution> distribution = new HashSet<Distribution>();

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

    @Override
    public final String getIdentifier() {
        return Taxon.IDENTIFIER_PREFIX + this.id;
    }

    @Override
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
     * @param string Set the name of the taxon
     */
    public final void setName(final String string) {
        this.name = string;
    }

    /**
     *
     * @param string Set the name identifier of the taxon
     */
    public final void setNameId(final String string) {
        this.nameId = string;
    }

    /**
     *
     * @param newDistribution Set the distribution of this taxon
     */
    public final void setDistribution(final Set<Distribution> newDistribution) {
        this.distribution = newDistribution;
    }

    /**
     *
     * @param newSynonyms Set the synonyms of this taxon
     */
    public final void setSynonyms(final Set<Taxon> newSynonyms) {
        this.synonyms = newSynonyms;
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
     * @return the name id of the taxon
     */
    public final String getNameId() {
        return nameId;
    }

    /**
     *
     * @param newAcceptedName Set the accepted name of the synonym
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
     * @return the accepted name of the synonym
     */
    public final Taxon getAcceptedName() {
        return acceptedName;
    }

    /**
     *
     * @return the distribution of this taxon
     */
    public final Set<Distribution> getDistribution() {
        return distribution;
    }
}
