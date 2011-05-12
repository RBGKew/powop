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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.tdwg.BaseThing;

import java.net.URI;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Relationship", propOrder = {
	    "fromTaxon",
	    "relationshipCategory",
	    "toTaxon"
})
@XmlRootElement(name = "Relationship",namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
public class Relationship extends BaseThing {
	
	@XmlElement(name = "fromTaxon", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
	private FromTaxon fromTaxon;
	
	@XmlElement(namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
	private RelationshipCategory relationshipCategory;
	
	@XmlElement(name = "toTaxon", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
	private ToTaxon toTaxon;
	
	public TaxonConcept getFromTaxon() {
		return fromTaxon != null ? fromTaxon.getTaxonConcept() : null;
	}

	public void setFromTaxon(TaxonConcept fromTaxon) {
		this.fromTaxon = new FromTaxon(fromTaxon);
	}

	public TaxonRelationshipTerm getRelationshipCategory() {
		return relationshipCategory != null ? relationshipCategory.getTaxonRelationshipTerm() : null;
	}

	public void setRelationshipCategory(TaxonRelationshipTerm relationshipCategory) {
		this.relationshipCategory = new RelationshipCategory(relationshipCategory,false);
	}
	
	public TaxonRelationshipTerm getRelationshipCategoryRelation() {
		return relationshipCategory != null ? relationshipCategory.getTaxonRelationshipTerm() : null;
	}

	public void setRelationshipCategoryRelation(TaxonRelationshipTerm relationshipCategory) {
		this.relationshipCategory = new RelationshipCategory(relationshipCategory,true);
	}

	public TaxonConcept getToTaxon() {
		return toTaxon != null ? toTaxon.getTaxonConcept() : null;
	}

	public void setToTaxon(TaxonConcept toTaxon) {
		this.toTaxon = new ToTaxon(toTaxon);
	}

	@XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "FromTaxon", propOrder = {
        "taxonConcept"
    })
	public static class FromTaxon extends LinkType {
		
		@XmlElement(name = "TaxonConcept", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
		private TaxonConcept taxonConcept;

		protected FromTaxon() {}
		
		protected FromTaxon(TaxonConcept taxonConcept) {
	        if(taxonConcept != null && taxonConcept.getIdentifier() != null) {
			    //this.setResource(taxonConcept.getIdentifier());
	        	this.setTaxonConcept(taxonConcept);
	        }
		}
		
		protected TaxonConcept getTaxonConcept() {
			return taxonConcept;
		}

		protected void setTaxonConcept(TaxonConcept taxonConcept) {
			this.taxonConcept = taxonConcept;
		}		
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "ToTaxon", propOrder = {
        "taxonConcept"
    })
	public static class ToTaxon extends LinkType {
		
		@XmlElement(name = "TaxonConcept", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
		private TaxonConcept taxonConcept;
		
		protected ToTaxon() {}
		
		protected ToTaxon(TaxonConcept taxonConcept) {
			    this.setTaxonConcept(taxonConcept);
		}

		protected TaxonConcept getTaxonConcept() {
			return taxonConcept;
		}

		protected void setTaxonConcept(TaxonConcept taxonConcept) {
			this.taxonConcept = taxonConcept;
		}
		
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "RelationshipCategory", propOrder = {
        "taxonRelationshipTerm"
    })
	public static class RelationshipCategory extends LinkType {
		
		@XmlElement(name = "TaxonRelationshipTerm", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
		private TaxonRelationshipTerm taxonRelationshipTerm;
		
		protected RelationshipCategory() {}
		
		protected RelationshipCategory(TaxonRelationshipTerm taxonRelationshipTerm, boolean useRelation) {
			if(useRelation) {
			    if(taxonRelationshipTerm != null) {
			    	if(taxonRelationshipTerm.getIdentifier() != null)
			    	this.setResource(taxonRelationshipTerm.getIdentifier());
			    }  else {
			    	this.taxonRelationshipTerm = taxonRelationshipTerm;
			    }
			} else {
				this.taxonRelationshipTerm = taxonRelationshipTerm;
			}
		}

		protected TaxonRelationshipTerm getTaxonRelationshipTerm() {
			return taxonRelationshipTerm;
		}

		protected void setTaxonRelationshipTerm(TaxonRelationshipTerm taxonRelationshipTerm) {
			this.taxonRelationshipTerm = taxonRelationshipTerm;
		}

		protected RelationshipCategory(TaxonRelationshipTerm taxonRelationshipTerm) {
			this.taxonRelationshipTerm = taxonRelationshipTerm;
		}
		
	}

//	public Object onCycleDetected(Context context) {
//		Relationship relationship = new Relationship();
//		if(this.toTaxon != null && this.toTaxon.getTaxonConcept() != null) {
//		    relationship.setToTaxon(this.toTaxon.getTaxonConcept());
//		}
//		
//		if(this.fromTaxon != null && this.fromTaxon.getTaxonConcept() != null) {
//    		relationship.setFromTaxon(this.fromTaxon.getTaxonConcept());
//		}
//		
//		relationship.setRelationshipCategory(this.getRelationshipCategory());
//		return relationship;
//	}
}
