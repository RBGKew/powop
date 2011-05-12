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

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.tdwg.DefinedTerm;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxonRelationshipTerm", propOrder = {})
@XmlRootElement(name = "TaxonRelationshipTerm", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#")
public class TaxonRelationshipTerm extends DefinedTerm {
	
	public TaxonRelationshipTerm() { }
	
	public TaxonRelationshipTerm(String identifier, String title)  {
		this.setTitle(title);
		try {
			this.setIdentifier(new URI(identifier));
		} catch (URISyntaxException e) { }
	}
	public static TaxonRelationshipTerm DOES_NOT_INCLUDE = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#DoesNotInclude", "Does Not Include");
	public static TaxonRelationshipTerm DOES_NOT_OVERLAP = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#DoesNotOverlap", "Does Not Overlap");
	public static TaxonRelationshipTerm EXCLUDES = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#Excludes", "Excludes");
	public static TaxonRelationshipTerm HAS_SYNONYM = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#HasSynonym", "Has Synonym");
	public static TaxonRelationshipTerm HAS_VERNACULAR = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#HasVernacular", "Has Vernacular");
	public static TaxonRelationshipTerm INCLUDES = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#Includes", "Includes");
	public static TaxonRelationshipTerm IS_AMBIREGNAL_OF = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsAmbiregnalOf", "Is Ambiregnal Of");
	public static TaxonRelationshipTerm IS_ANAMORPH_OF = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsAnamorphOf", "Is Anamorph Of");
	public static TaxonRelationshipTerm IS_CHILD_TAXON_OF = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsChildTaxonOf", "Is Child Taxon Of");
	public static TaxonRelationshipTerm IS_CONGRUENT_TO = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsCongruentTo", "Is Congruent To");
	public static TaxonRelationshipTerm IS_FEMALE_PARENT_OF = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsFemaleParentOf", "Is Female Parent Of");
	public static TaxonRelationshipTerm IS_FIRST_PARENT_OF = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsFirstParentOf", "Is First Parent Of");
	public static TaxonRelationshipTerm IS_HYBRID_CHILD_OF = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsHybridChildOf", "Is Hybrid Child Of");
	public static TaxonRelationshipTerm IS_HYBRID_PARENT_OF = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsHybridParentOf", "Is Hybrid Parent Of");
	public static TaxonRelationshipTerm IS_INCLUDED_IN = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsIncludedIn", "Is Included In");
	public static TaxonRelationshipTerm IS_MALE_PARENT_OF = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsMaleParentOf", "Is Male Parent Of");
	public static TaxonRelationshipTerm IS_NOT_CONGRUENT_TO = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsNotCongruentTo", "Is Not Congruent To");
	public static TaxonRelationshipTerm IS_NOT_INCLUDED_IN = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsNotIncludedIn", "Is Not Included In");
	public static TaxonRelationshipTerm IS_PARENT_TAXON_OF = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsParentTaxonOf", "Is Parent Taxon Of");
	public static TaxonRelationshipTerm IS_SECOND_PARENT_OF = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsSecondParentOf", "Is Second Parent Of");
	public static TaxonRelationshipTerm IS_SYONYM_FOR = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsSynonymFor", "Is Synonym For");
	public static TaxonRelationshipTerm IS_TELEMORPH_OF = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsTelemorphOf", "Is Telemorph Of");
	public static TaxonRelationshipTerm IS_VERNACULAR_FOR = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#IsVernacularFor", "Is Vernacular For");
	public static TaxonRelationshipTerm OVERLAPS = new TaxonRelationshipTerm("http://rs.tdwg.org/ontology/voc/TaxonConcept#Overlaps", "Overlaps");
}
