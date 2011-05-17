// $Id$
/**
 * Copyright (C) 2007 EDIT
 * European Distributed Institute of Taxonomy
 * http://www.e-taxonomy.eu
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * See LICENSE.TXT at the top of this package for the full license terms.
 */
package org.tdwg.voc;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.tdwg.Actor;
import org.tdwg.Concept;

/**
 *
 * @author ben
 *
 */
public class TaxonConcept extends Concept {

    /**
     *
     */
    private Boolean tcPrimary;

    /**
     *
     */
//    private AccordingTo tcAccordingTo;

    /**
     *
     */
    private HasName tcHasName;

    /**
     *
     */
//    private Set<HasRelationship> tcHasRelationships = null;

    /**
     *
     */
//    private Set<DescribedBy> tcDescribedBys = null;

//    /**
//     *
//     * @return the taxonomic relationships
//     */
//    public final Set<Relationship> getHasRelationship() {
//        if (tcHasRelationships != null) {
//            Set<Relationship> relationships = new HashSet<Relationship>();
//            for (HasRelationship hasRelationship : tcHasRelationships) {
//                relationships.add(hasRelationship.getRelationship());
//            }
//            return relationships;
//        } else {
//            return null;
//        }
//    }
//
//    public void addHasRelationship(Relationship relationship) {
//        if (relationship != null) {
//            if (this.tcHasRelationships == null) {
//                this.tcHasRelationships = new HashSet<HasRelationship>();
//            }
//            this.tcHasRelationships.add(new HasRelationship(relationship));
//        }
//    }
//
//    public void setHasRelationship(Set<Relationship> relationships) {
//        if (relationships != null) {
//            this.tcHasRelationships = new HashSet<HasRelationship>();
//            for (Relationship relationship : relationships) {
//                tcHasRelationships.add(new HasRelationship(relationship));
//            }
//        } else {
//            tcHasRelationships = null;
//        }
//    }

//    public Set<SpeciesProfileModel> getDescribedBy() {
//        if (describedBys != null) {
//            Set<SpeciesProfileModel> speciesProfileModels = new HashSet<SpeciesProfileModel>();
//            for (DescribedBy describedBy : describedBys) {
//                speciesProfileModels.add(describedBy.getSpeciesProfileModel());
//            }
//            return speciesProfileModels;
//        } else {
//            return null;
//        }
//    }
//
//    public void setDescribedBy(Set<SpeciesProfileModel> speciesProfileModels) {
//        if (speciesProfileModels != null) {
//            this.describedBys = new HashSet<DescribedBy>();
//            for (SpeciesProfileModel speciesProfileModel : speciesProfileModels) {
//                describedBys.add(new DescribedBy(speciesProfileModel));
//            }
//        } else {
//            describedBys = null;
//        }
//    }
//
//    public void addDescribedBy(SpeciesProfileModel speciesProfileModel) {
//
//        if (speciesProfileModel != null) {
//            if (this.describedBys == null) {
//                this.describedBys = new HashSet<DescribedBy>();
//            }
//            describedBys.add(new DescribedBy(speciesProfileModel));
//        }
//    }
//
    public TaxonName getHasName() {
        return tcHasName != null ? tcHasName.getTaxonName() : null;
    }

    public void setHasName(TaxonName taxonName) {
        this.tcHasName = new HasName(taxonName, false);
    }

    public TaxonName getHasNameRelation() {
        return tcHasName != null ? tcHasName.getTaxonName() : null;
    }

    public void setHasNameRelation(TaxonName taxonName) {
        this.tcHasName = new HasName(taxonName, true);
    }
//
//    public Boolean isPrimary() {
//        return primary;
//    }
//
//    public Actor getAccordingTo() {
//        return accordingTo != null ? accordingTo.getActor() : null;
//    }
//
//    public void setAccordingTo(Actor accordingTo) {
//        this.accordingTo = new AccordingTo(accordingTo, false);
//    }
//
//    public Actor getAccordingToRelation() {
//        return accordingTo != null ? accordingTo.getActor() : null;
//    }
//
//    public void setAccordingToRelation(Actor accordingTo) {
//        this.accordingTo = new AccordingTo(accordingTo, true);
//    }
//
//    public void setPrimary(Boolean primary) {
//        this.primary = primary;
//    }
//
    
    public static class HasName extends LinkType {
        
        private TaxonName tnTaxonName;

        protected HasName() {
        }

        protected HasName(TaxonName taxonName, boolean useRelation) {
            if (useRelation) {
                if (taxonName != null && taxonName.getIdentifier() != null) {
                    this.setResource(taxonName.getIdentifier());
                } else {
                    this.tnTaxonName = taxonName;
                }
            } else {
                this.tnTaxonName = taxonName;
            }
        }

        protected TaxonName getTaxonName() {
            return tnTaxonName;
        }

        protected void setTaxonName(TaxonName taxonName) {
            this.tnTaxonName = taxonName;
        }
    }
//
//    @XmlAccessorType(XmlAccessType.FIELD)
//    @XmlType(name = "HasRelationship", propOrder = { "relationship" })
//    public static class HasRelationship extends LinkType {
//
//        @XmlElement(name = "Relationship", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
//        private Relationship relationship;
//
//        protected HasRelationship() {
//        }
//
//        protected HasRelationship(Relationship relationship) {
//            this.relationship = relationship;
//        }
//
//        protected Relationship getRelationship() {
//            return relationship;
//        }
//
//        protected void setRelationship(Relationship relationship) {
//            this.relationship = relationship;
//        }
//    }
//
//    @XmlAccessorType(XmlAccessType.FIELD)
//    @XmlType(name = "AccordingTo", propOrder = { "actor" })
//    public static class AccordingTo extends LinkType {
//
//        @XmlElements({
//                @XmlElement(name = "Person", namespace = "http://rs.tdwg.org/ontology/voc/Person#", type = Person.class),
//                @XmlElement(name = "Team", namespace = "http://rs.tdwg.org/ontology/voc/Team#", type = Team.class) })
//        private Actor actor;
//
//        protected AccordingTo() {
//        }
//
//        protected AccordingTo(Actor actor, boolean useRelation) {
//            if (useRelation) {
//                if (actor != null && actor.getIdentifier() != null) {
//                    this.setResource(actor.getIdentifier());
//                } else {
//                    this.actor = actor;
//                }
//            } else {
//                this.actor = actor;
//            }
//        }
//
//        protected Actor getActor() {
//            return actor;
//        }
//
//        protected void setActor(Actor actor) {
//            this.actor = actor;
//        }
//    }
//
//    @XmlAccessorType(XmlAccessType.FIELD)
//    @XmlType(name = "DescribedBy", propOrder = { "speciesProfileModel" })
//    public static class DescribedBy extends LinkType {
//
//        @XmlElement(name = "SpeciesProfileModel", namespace = "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#")
//        private SpeciesProfileModel speciesProfileModel;
//
//        protected DescribedBy() {
//        }
//
//        protected DescribedBy(SpeciesProfileModel speciesProfileModel) {
//            this.speciesProfileModel = speciesProfileModel;
//        }
//
//        protected SpeciesProfileModel getSpeciesProfileModel() {
//            return speciesProfileModel;
//        }
//
//        protected void setSpeciesProfileModel(
//                SpeciesProfileModel speciesProfileModel) {
//            this.speciesProfileModel = speciesProfileModel;
//        }
//    }
//
//    // public Object onCycleDetected(Context context) {
//    // TaxonConcept taxonConcept = new TaxonConcept();
//    // taxonConcept.setIdentifier(super.getIdentifier());
//    // taxonConcept.setTitle(super.getTitle());
//    // return taxonConcept;
//    // }
}
