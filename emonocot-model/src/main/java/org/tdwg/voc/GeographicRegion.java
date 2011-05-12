package org.tdwg.voc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.tdwg.DefinedTerm;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeographicalRegion", propOrder = {
	"code",
	"name",
	"isPartOf"
})
@XmlRootElement(name = "GeographicalRegion", namespace = "http://rs.tdwg.org/ontology/voc/GeographicRegion#")
public class GeographicRegion extends DefinedTerm {
	
	@XmlElement(name = "code", namespace = "http://rs.tdwg.org/ontology/voc/GeographicRegion#")
	private String code;
	
	@XmlElement(name = "name", namespace = "http://rs.tdwg.org/ontology/voc/GeographicRegion#")
	private String name;
	
	@XmlElement(name = "isPartOf", namespace = "http://rs.tdwg.org/ontology/voc/GeographicRegion#")
	private GeographicRegion isPartOf;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GeographicRegion getIsPartOf() {
		return isPartOf;
	}

	public void setIsPartOf(GeographicRegion isPartOf) {
		this.isPartOf = isPartOf;
	}
}
