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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxonConcept", propOrder = {
	    "primary",
	    "accordingTo",
	    "hasName",
	    "hasRelationships",
	    "describedBys"
})
@XmlRootElement(name = "TaxonConcept", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
public class TaxonConcept extends Concept {

	@XmlElement(namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
	private Boolean primary;

	@XmlElement(namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
	private AccordingTo accordingTo;
	
	@XmlElement(name = "hasName", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
	private HasName hasName;
	
	@XmlElement(name = "hasRelationship", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
	private Set<HasRelationship> hasRelationships = null;
	
	@XmlElement(name = "describedBy", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
	private Set<DescribedBy> describedBys = null;
	
	public Set<Relationship> getHasRelationship() {
		if(hasRelationships != null) {
			Set<Relationship> relationships = new HashSet<Relationship>();
			for(HasRelationship hasRelationship : hasRelationships) {
				relationships.add(hasRelationship.getRelationship());
			}
			return relationships;
		} else {
			return null;
		}
	}
	
	public void addHasRelationship(Relationship relationship) {
		if(relationship != null) {
			if(this.hasRelationships == null) {
			  this.hasRelationships = new HashSet<HasRelationship>();
			}
			this.hasRelationships.add(new HasRelationship(relationship));
		}
	}

	public void setHasRelationship(Set<Relationship> relationships) {
		if(relationships != null) {
		  this.hasRelationships = new HashSet<HasRelationship>();
		  for(Relationship relationship : relationships) {
			hasRelationships.add( new HasRelationship(relationship));
		  }
		} else {
			hasRelationships = null;
		}
	}
	
	public Set<SpeciesProfileModel> getDescribedBy() {
		if(describedBys != null) {
			Set<SpeciesProfileModel> speciesProfileModels = new HashSet<SpeciesProfileModel>();
			for(DescribedBy describedBy : describedBys) {
				speciesProfileModels.add(describedBy.getSpeciesProfileModel());
			}
			return speciesProfileModels;
		} else {
			return null;
		}
	}

	public void setDescribedBy(Set<SpeciesProfileModel> speciesProfileModels) {
		if(speciesProfileModels != null) {
		  this.describedBys = new HashSet<DescribedBy>();
		  for(SpeciesProfileModel speciesProfileModel : speciesProfileModels) {
			describedBys.add( new DescribedBy(speciesProfileModel));
		  }
		} else {
			describedBys = null;
		}
	}
	
	public void addDescribedBy(SpeciesProfileModel speciesProfileModel) {
		
		if(speciesProfileModel != null) {
			if(this.describedBys == null) {
				this.describedBys = new HashSet<DescribedBy>();
			}
			describedBys.add( new DescribedBy(speciesProfileModel));
		} 
	}

	public TaxonName getHasName() {
		return hasName != null ? hasName.getTaxonName() : null;
	}

	public void setHasName(TaxonName taxonName) {
		this.hasName = new HasName(taxonName, false);
	}
	
	public TaxonName getHasNameRelation() {
		return hasName != null ? hasName.getTaxonName() : null;
	}

	public void setHasNameRelation(TaxonName taxonName) {
		this.hasName = new HasName(taxonName, true);
	}

	public Boolean isPrimary() {
		return primary;
	}
	
	public Actor getAccordingTo() {
		return accordingTo != null ? accordingTo.getActor() : null;
	}

	public void setAccordingTo(Actor accordingTo) {
		this.accordingTo = new AccordingTo(accordingTo, false);
	}
	
	public Actor getAccordingToRelation() {
		return accordingTo != null ? accordingTo.getActor() : null;
	}

	public void setAccordingToRelation(Actor accordingTo) {
		this.accordingTo = new AccordingTo(accordingTo, true);
	}

	public void setPrimary(Boolean primary) {
		this.primary = primary;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "HasName", propOrder = {
        "taxonName"
    })
	public static class HasName extends LinkType {
		@XmlElement(name = "TaxonName", namespace = "http://rs.tdwg.org/ontology/voc/TaxonName#")
		private TaxonName taxonName;
		
        protected HasName() {}
		
        protected HasName(TaxonName taxonName, boolean useRelation) {
        	if(useRelation) {
			    if(taxonName != null && taxonName.getIdentifier() != null) {
			    	this.setResource(taxonName.getIdentifier());
			    }  else {
			    	this.taxonName = taxonName;
			    }
			} else {
				this.taxonName = taxonName;
			}
		}

		protected TaxonName getTaxonName() {
			return taxonName;
		}

		protected void setTaxonName(TaxonName taxonName) {
			this.taxonName = taxonName;
		}
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "HasRelationship", propOrder = {
        "relationship"
    })
	public static class HasRelationship extends LinkType {

		@XmlElement(name = "Relationship", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
		private Relationship relationship;
		
		protected HasRelationship() {}
		
		protected HasRelationship(Relationship relationship) {
			this.relationship = relationship;
		}
		
		protected Relationship getRelationship() {
			return relationship;
		}

		protected void setRelationship(Relationship relationship) {
			this.relationship = relationship;
		}
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "AccordingTo", propOrder = {
        "actor"
    })
	public static class AccordingTo extends LinkType {

		@XmlElements({
			@XmlElement(name = "Person", namespace = "http://rs.tdwg.org/ontology/voc/Person#", type = Person.class),
			@XmlElement(name = "Team", namespace = "http://rs.tdwg.org/ontology/voc/Team#", type = Team.class)
		})
		private Actor actor;
		
		protected AccordingTo() {}
		
		protected AccordingTo(Actor actor, boolean useRelation) {
			if(useRelation) {
			    if(actor != null && actor.getIdentifier() != null) {
			    	this.setResource(actor.getIdentifier());
			    }  else {
			    	this.actor = actor;
			    }
			} else {
				this.actor = actor;
			}
		}
		
		protected Actor getActor() {
			return actor;
		}

		protected void setActor(Actor actor) {
			this.actor = actor;
		}
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "DescribedBy", propOrder = {
        "speciesProfileModel"
    })
	public static class DescribedBy extends LinkType {

		@XmlElement(name = "SpeciesProfileModel", namespace = "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#")
		private SpeciesProfileModel speciesProfileModel;
		
		protected DescribedBy() {}
		
		protected DescribedBy(SpeciesProfileModel speciesProfileModel) {
			this.speciesProfileModel = speciesProfileModel;
		}
		
		protected SpeciesProfileModel getSpeciesProfileModel() {
			return speciesProfileModel;
		}

		protected void setSpeciesProfileModel(SpeciesProfileModel speciesProfileModel) {
			this.speciesProfileModel = speciesProfileModel;
		}
	}

//	public Object onCycleDetected(Context context) {
//		TaxonConcept taxonConcept = new TaxonConcept();
//		taxonConcept.setIdentifier(super.getIdentifier());
//		taxonConcept.setTitle(super.getTitle());
//		return taxonConcept;
//	}
}
