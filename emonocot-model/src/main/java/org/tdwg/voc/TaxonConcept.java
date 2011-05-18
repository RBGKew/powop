package org.tdwg.voc;

import java.util.HashSet;
import java.util.Set;

import org.tdwg.Actor;
import org.tdwg.Concept;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

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
    private AccordingTo tcAccordingTo;

    /**
     *
     */
    private HasName tcHasName;

    /**
     *
     */
    @XStreamImplicit(itemFieldName = "tcHasRelationship")
    private Set<HasRelationship> tcHasRelationships = null;

    /**
     *
     */
//    private Set<DescribedBy> tcDescribedBys = null;

    /**
     *
     * @return the taxonomic relationships
     */
    public final Set<Relationship> getHasRelationship() {
        if (tcHasRelationships != null) {
            Set<Relationship> relationships = new HashSet<Relationship>();
            for (HasRelationship hasRelationship : tcHasRelationships) {
                relationships.add(hasRelationship.getRelationship());
            }
            return relationships;
        } else {
            return null;
        }
    }

    /**
     *
     * @param relationship Add a new relationship to this taxon concept
     */
    public final void addHasRelationship(final Relationship relationship) {
        if (relationship != null) {
            if (this.tcHasRelationships == null) {
                this.tcHasRelationships = new HashSet<HasRelationship>();
            }
            this.tcHasRelationships.add(new HasRelationship(relationship));
        }
    }

    /**
     *
     * @param relationships
     *            Set the relationships associated with this taxon concept
     */
    public final void setHasRelationship(
                final Set<Relationship> relationships) {
        if (relationships != null) {
            this.tcHasRelationships = new HashSet<HasRelationship>();
            for (Relationship relationship : relationships) {
                tcHasRelationships.add(new HasRelationship(relationship));
            }
        } else {
            tcHasRelationships = null;
        }
    }

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
    /**
     * @return the taxonomic name of this concept
     */
    public final TaxonName getHasName() {
        if (tcHasName != null) {
            return tcHasName.getTaxonName();
        } else {
            return null;
        }
    }

    /**
     *
     * @param taxonName Set the taxonomic name of this concept
     */
    public final void setHasName(final TaxonName taxonName) {
        this.tcHasName = new HasName(taxonName, false);
    }

    /**
     *
     * @return the taxon name of this concept
     */
    public final TaxonName getHasNameRelation() {
        if (tcHasName != null) {
            return tcHasName.getTaxonName();
        } else {
            return null;
        }
    }

    /**
     *
     * @param taxonName Set the taxon name of this concept
     */
    public final void setHasNameRelation(final TaxonName taxonName) {
        this.tcHasName = new HasName(taxonName, true);
    }

    /**
     *
     * @return the tcPrimary property
     */
    public final Boolean isPrimary() {
        return tcPrimary;
    }

    /**
     *
     * @return the actor who expressed this concept
     */
    public final Actor getAccordingTo() {
        if (tcAccordingTo != null) {
            return  tcAccordingTo.getActor();
        } else {
            return null;
        }
    }

    /**
     *
     * @param accordingTo set the actor who expressed this concept
     */
    public final void setAccordingTo(final Actor accordingTo) {
        this.tcAccordingTo = new AccordingTo(accordingTo, false);
    }

    /**
     *
     * @return the actor who expressed this concept
     */
    public final Actor getAccordingToRelation() {
        if (tcAccordingTo != null) {
            return  tcAccordingTo.getActor();
        } else {
            return null;
        }
    }

    /**
     *
     * @param accordingTo Set the actor who expressed this concept
     */
    public final void setAccordingToRelation(final Actor accordingTo) {
        this.tcAccordingTo = new AccordingTo(accordingTo, true);
    }

    /**
     * @param primary Set the tcPrimary property
     */
    public final void setPrimary(final Boolean primary) {
        this.tcPrimary = primary;
    }


    /**
     *
     */
    public static class HasName extends LinkType {

        /**
         *
         */
        private TaxonName tnTaxonName;

        /**
         *
         */
        protected HasName() {
        }

        /**
         *
         * @param taxonName Set the taxon name of this relation
         * @param useRelation true if this element should be
         *     expressed as a relation rather than as an embedded object
         *
         */
        protected HasName(final TaxonName taxonName,
                final boolean useRelation) {
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

        /**
         *
         * @return the taxon name associated with this relation
         */
        protected final TaxonName getTaxonName() {
            return tnTaxonName;
        }

        /**
         *
         * @param taxonName Set the taxon name of this relation
         */
        protected final void setTaxonName(final TaxonName taxonName) {
            this.tnTaxonName = taxonName;
        }
    }

    /**
     *
     */
    public static class AccordingTo extends LinkType {

        /**
         *
         */
        private Person tpPerson;

        /**
         *
         */
        private Team ttTeam;

        /**
         *
         */
        protected AccordingTo() {
        }

        /**
         *
         * @param actor Set the actor of this relation
         * @param useRelation true if this element should be
         *     expressed as a relation rather than as an embedded object
         */
        protected AccordingTo(final Actor actor, final boolean useRelation) {
            if (useRelation) {
                if (actor != null && actor.getIdentifier() != null) {
                    this.setResource(actor.getIdentifier());
                } else {
                    if (actor != null && actor instanceof Team) {
                        this.ttTeam = (Team) actor;
                    } else if (actor != null && actor instanceof Person) {
                        this.tpPerson = (Person) actor;
                    }
                }
            } else {
                if (actor != null && actor instanceof Team) {
                    this.ttTeam = (Team) actor;
                } else if (actor != null && actor instanceof Person) {
                    this.tpPerson = (Person) actor;
                }
            }
        }

        /**
         *
         * @return the actor associated with this relation
         */
        protected final Actor getActor() {
            if (ttTeam != null) {
              return ttTeam;
            } else {
                return tpPerson;
            }
        }

        /**
         *
         * @param actor Set the actor associated with this relation
         */
        protected final void setActor(final Actor actor) {
            if (actor != null && actor instanceof Team) {
                this.ttTeam = (Team) actor;
                this.tpPerson = null;
            } else if (actor != null && actor instanceof Person) {
                this.tpPerson = (Person) actor;
                this.ttTeam = null;
            }
        }
    }
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
