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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setIdentifier(final String identifier) {
        // TODO Auto-generated method stub
    }
}
