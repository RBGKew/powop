package org.tdwg.voc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.tdwg.DefinedTerm;
import org.tdwg.Description;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InfoItem", propOrder = {
	    "associatedTaxon",
	    "category",
	    "context",
	    //"contextOccurrence",
	    "contextValues",
	    "hasContent",
	    "hasValues"
})
@XmlRootElement(name = "InfoItem", namespace = "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#")
public class InfoItem extends Description {
	
	@XmlElement(name = "associatedTaxon", namespace = "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#")
	private AssociatedTaxon associatedTaxon;
	
	@XmlElement(name = "category", namespace = "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#")
	private String category;
	
	@XmlElement(name = "context", namespace = "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#")
	private Set<StringType> context;
	
	@XmlElement(name = "hasContent", namespace = "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#")
	private Set<StringType> hasContent;
	
	@XmlElement(name = "contextValue", namespace = "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#")
	private Set<DefinedTermLinkType> contextValues = null;
	
	@XmlElement(name = "hasValue", namespace = "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#")
	private Set<DefinedTermLinkType> hasValues = null;
	
	public TaxonConcept getAssociatedTaxon() {
		return associatedTaxon != null ? associatedTaxon.getTaxonConcept() : null;
	}

	public void setAssociatedTaxon(TaxonConcept taxonConcept) {
		this.associatedTaxon = new AssociatedTaxon(taxonConcept, false);
	}
	
	public TaxonConcept getAssocatedTaxonRelation() {
		return associatedTaxon != null ? associatedTaxon.getTaxonConcept() : null;
	}

	public void setAssociatedTaxonRelation(TaxonConcept taxonConcept) {
		this.associatedTaxon = new AssociatedTaxon(taxonConcept, true);
	}
	
	public Map<Object,StringType> getContext() {
		if(context != null) {
			Map<Object,StringType> contextMap = new HashMap<Object,StringType>();
			for(StringType stringType : context) {
				contextMap.put(stringType.getValue(),stringType);
			}
			return contextMap;
		} else {
			return null;
		}
	}

	public void setContext(Map<Object,StringType> contextMap) {
		if(contextMap != null) {
		    this.context = new HashSet<StringType>();
		    this.context.addAll(contextMap.values());
		} else {
			this.context = null;
		}
	}

	public Map<Object,StringType> getHasContent() {
		if(hasContent != null) {
			Map<Object,StringType> contentMap = new HashMap<Object,StringType>();
			for(StringType stringType : hasContent) {
				contentMap.put(stringType.getValue(),stringType);
			}
			return contentMap;
		} else {
			return null;
		}
	}

	public void setHasContent(Map<Object,StringType> hasContentMap) {
		if(hasContentMap != null) {
		    this.hasContent = new HashSet<StringType>();
		    this.hasContent.addAll(hasContentMap.values());
		} else {
			this.hasContent = null;
		}
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String definedTerm) {
		this.category = definedTerm;
	}
	
	
	public Set<DefinedTerm> getContextValue() {
		if(contextValues != null) {
			Set<DefinedTerm> definedTerms = new HashSet<DefinedTerm>();
			for(DefinedTermLinkType contextValue : contextValues) {
				definedTerms.add(contextValue.getDefinedTerm());
			}
			return definedTerms;
		} else {
			return null;
		}
	}

	public void setContextValue(Set<DefinedTerm> definedTerms) {
		if(definedTerms != null) {
		  this.contextValues = new HashSet<DefinedTermLinkType>();
		  for(DefinedTerm definedTerm : definedTerms) {
			contextValues.add( new DefinedTermLinkType(definedTerm,false));
		  }
		} else {
			contextValues = null;
		}
	}

	public void addContextValue(DefinedTerm definedTerm) {
		if(definedTerm != null) {
		  this.contextValues = new HashSet<DefinedTermLinkType>();
		  contextValues.add( new DefinedTermLinkType(definedTerm,false));
		} else {
			contextValues = null;
		}
	}
	
	public Set<DefinedTerm> getHasValue() {
		if(hasValues != null) {
			Set<DefinedTerm> definedTerms = new HashSet<DefinedTerm>();
			for(DefinedTermLinkType contextValue : hasValues) {
				definedTerms.add(contextValue.getDefinedTerm());
			}
			return definedTerms;
		} else {
			return null;
		}
	}

	public void setHasValue(Set<DefinedTerm> definedTerms) {
		if(definedTerms != null) {
		  this.hasValues = new HashSet<DefinedTermLinkType>();
		  for(DefinedTerm definedTerm : definedTerms) {
			hasValues.add( new DefinedTermLinkType(definedTerm,false));
		  }
		} else {
			hasValues = null;
		}
	}

	public void addValue(DefinedTerm definedTerm) {
		if(definedTerm != null) {
		  if(hasValues == null) {
			  this.hasValues = new HashSet<DefinedTermLinkType>();	
		  }
		  hasValues.add( new DefinedTermLinkType(definedTerm,false));
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "AssociatedTaxon", propOrder = {
        "taxonConcept"
    })
	public static class AssociatedTaxon extends LinkType {
		@XmlElement(name = "TaxonConcept", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
		private TaxonConcept taxonConcept;
		
        protected AssociatedTaxon() {}
		
        protected AssociatedTaxon(TaxonConcept taxonConcept, boolean useRelation) {
        	if(useRelation) {
			    if(taxonConcept != null && taxonConcept.getIdentifier() != null) {
			    	this.setResource(taxonConcept.getIdentifier());
			    }  else {
			    	this.taxonConcept = taxonConcept;
			    }
			} else {
				this.taxonConcept = taxonConcept;
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
    @XmlType(name = "DefinedTermLinkType", propOrder = {
        "definedTerm"
    })
	public static class DefinedTermLinkType extends LinkType {
		@XmlElements(
	 	{@XmlElement(name = "DefinedTerm", namespace = "http://rs.tdwg.org/ontology/voc/Common#", type = DefinedTerm.class),
	 	 @XmlElement(name = "GeographicRegion", namespace = "http://rs.tdwg.org/ontology/voc/GeographicRegion#", type = GeographicRegion.class)
	 	})
		private DefinedTerm definedTerm;
		
        protected DefinedTermLinkType() {}
		
        protected DefinedTermLinkType(DefinedTerm definedTerm, boolean useRelation) {
        	if(useRelation) {
			    if(definedTerm != null && definedTerm.getIdentifier() != null) {
			    	this.setResource(definedTerm.getIdentifier());
			    }  else {
			    	this.definedTerm = definedTerm;
			    }
			} else {
				this.definedTerm = definedTerm;
			}
		}

		protected DefinedTerm getDefinedTerm() {
			return definedTerm;
		}

		protected void setDefinedTerm(DefinedTerm definedTerm) {
			this.definedTerm = definedTerm;
		}
	}
}
