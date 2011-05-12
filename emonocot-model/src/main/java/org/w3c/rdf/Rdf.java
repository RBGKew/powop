package org.w3c.rdf;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.tdwg.BaseThing;
import org.tdwg.voc.TaxonConcept;
import org.tdwg.voc.SpeciesProfileModel;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "things"
})
@XmlRootElement(name = "RDF", namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
public class Rdf {

    @XmlElements({
	  @XmlElement(name = "TaxonConcept", namespace = "http://rs.tdwg.org/ontology/voc/TaxonConcept#", type = TaxonConcept.class),
	  @XmlElement(name = "SpeciesProfileModel", namespace = "http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#", type = SpeciesProfileModel.class)
    })
    protected Set<BaseThing> things = new HashSet<BaseThing>();

	public Set<BaseThing> getThings() {
		return things;
	}

	public void addThing(BaseThing thing) {
		this.things.add(thing);
	}
	
	public void removeThing(BaseThing thing) {
		this.things.remove(thing);
	}
}
