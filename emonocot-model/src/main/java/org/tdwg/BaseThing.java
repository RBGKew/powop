// $Id$
/**
* Copyright (C) 2007 EDIT
* European Distributed Institute of Taxonomy 
* http://www.e-taxonomy.eu
* 
* The contents of this file are subject to the Mozilla Public License Version 1.1
* See LICENSE.TXT at the top of this package for the full license terms.
*/
package org.tdwg;

import java.net.URI;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.dublincore.Relation;
import org.emonocot.model.marshall.DateTimeConverter;
import org.joda.time.DateTime;
import org.tdwg.voc.LinkType;
import org.tdwg.voc.PublicationCitation;

import com.thoughtworks.xstream.annotations.XStreamConverter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseThing", propOrder = {
	    "title",
	    "sameAs",
	    "identifier",
	    "created",
	    "creator",
	    "date",
	    "contributor",
	    "relation",
	    "abcdEquivalence",
	    "berlinModelEquivalence",
	    "darwinCoreEquivalence",
	    "deprecated",
	    "restricted",
	    "microReference",
	    "notes",
	    "publishedIn",
	    "taxonomicPlacementFormal",
	    "taxonomicPlacementInformal",
	    "tcsEquivalence",
	    "publishedInCitation"
})
public abstract class BaseThing {
	
	@XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
	private String title;
	
	@XmlElement(namespace = "http://www.w3.org/2002/07/owl#")
	private String sameAs;
	
	@XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
	private URI identifier;
	
	@XmlElement(namespace = "http://purl.org/dc/terms/")
	@XStreamConverter(DateTimeConverter.class)
	private DateTime created;
	
	@XmlElement(namespace = "http://purl.org/dc/terms/")
	@XStreamConverter(DateTimeConverter.class)
	private DateTime date;

	@XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
	private String creator;
	
	@XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
	private String contributor;
	
	@XmlElement(name = "relation", namespace = "http://purl.org/dc/elements/1.1/")
	private Relation relation;
	
	@XmlElement
	private String abcdEquivalence;
	
	@XmlElement
	private String berlinModelEquivalence;
	
	@XmlElement
	private String darwinCoreEquivalence;
	
	@XmlElement(name = "isDeprecated")
	private Boolean deprecated;
	
	@XmlElement(name = "isRestricted")
	private Boolean restricted;
	
	@XmlElement
	private String microReference;
	
	@XmlElement
	private Set<String> notes;
	
	@XmlElement
	private String publishedIn;

	@XmlElement
	private String taxonomicPlacementFormal;

	@XmlElement
	private String taxonomicPlacementInformal;

	@XmlElement
	private String tcsEquivalence;

	@XmlElement(name = "publishedInCitation")
	private PublishedInCitation publishedInCitation;
	
	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getContributor() {
		return contributor;
	}

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}
	
	public String getAbcdEquivalence() {
		return abcdEquivalence;
	}
	
	public String getBerlinModelEquivalence() {
		return berlinModelEquivalence;
	}
	
	//dcterms:created
	public DateTime getCreated() {
		return created;
	}

	public String getDarwinCoreEquivalence() {
		return darwinCoreEquivalence;
	}
	
	//dc:identifier
	public URI getIdentifier() {
		return identifier;
	}
	
	public String getMicroReference() {
		return microReference;
	}

	public Set<String> getNotes() {
		return notes;
	}

	public String getPublishedIn() {
		return publishedIn;
	}

	//owl:sameAs
	public String getSameAs() {
		return sameAs;
	}

	public String getTaxonomicPlacementFormal() {
		return taxonomicPlacementFormal;
	}

	public String getTaxonomicPlacementInformal() {
		return taxonomicPlacementInformal;
	}

	public String getTcsEquivalence() {
		return tcsEquivalence;
	}

	//dc:title
	public String getTitle() {
		return title;
	}

	public Boolean isDeprecated() {
		return deprecated;
	}

	public Boolean isRestricted() {
		return restricted;
	}
	
	public void setDeprecated(Boolean deprecated) {
		this.deprecated = deprecated;
	}
	
	public void setIdentifier(URI identifier) {
		this.identifier = identifier;
	}
	
	public void setMicroReference(String microReference) {
		this.microReference = microReference;
	}
	
	public void setNotes(Set<String> notes) {
		this.notes = notes;
	}
	
	public void setPublishedIn(String publishedIn) {
		this.publishedIn = publishedIn;
	}
	
	public void setRestricted(Boolean restricted) {
		this.restricted = restricted;
	} 
	
	public void setSameAs(String sameAs) {
		this.sameAs = sameAs;
	}
	
	public void setTaxonomicPlacementFormal(String taxonomicPlacementFormal) {
		this.taxonomicPlacementFormal = taxonomicPlacementFormal;
	}
	
	public void setTaxonomicPlacementInformal(String taxonomicPlacementInformal) {
		this.taxonomicPlacementInformal = taxonomicPlacementInformal;
	}

	public void setTcsEquivalence(String tcsEquivalence) {
		this.tcsEquivalence = tcsEquivalence;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public void setPublishedInCitation(PublicationCitation publicationCitation) {
		this.publishedInCitation = new PublishedInCitation(publicationCitation, false);
	}
	
	public PublicationCitation getPublishedInCitation() {
		return publishedInCitation != null ? publishedInCitation.getPublicationCitation() : null;
	}
	
	public void setPublishedInCitationRelation(PublicationCitation publicationCitation) {
		this.publishedInCitation = new PublishedInCitation(publicationCitation, true);
	}
	
	public PublicationCitation getPublishedInCitationRelation() {
		return publishedInCitation != null ? publishedInCitation.getPublicationCitation() : null;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public Relation getRelation() {
		return relation;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "publicationCitation"
    })
	public static class PublishedInCitation extends LinkType {
		
		@XmlElement(name = "PublicationCitation", namespace = "http://rs.tdwg.org/ontology/voc/PublicationCitation#")
		private PublicationCitation publicationCitation;
		
		protected PublishedInCitation() {}
		
		protected PublishedInCitation(PublicationCitation publicationCitation, boolean useRelation) {
			if(useRelation) {
			    if(publicationCitation != null && publicationCitation.getIdentifier() != null) {
			    	this.setResource(publicationCitation.getIdentifier());
			    }  else {
			    	this.publicationCitation = publicationCitation;
			    }
			} else {
				this.publicationCitation = publicationCitation;
			}
		}

		protected void setPublicationCitation(PublicationCitation publicationCitation) {
			this.publicationCitation = publicationCitation;
		}

		protected PublicationCitation getPublicationCitation() {
			return publicationCitation;
		}

	}
}
