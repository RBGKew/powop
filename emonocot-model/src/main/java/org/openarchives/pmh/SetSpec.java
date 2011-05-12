package org.openarchives.pmh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * <simpleType name="setSpecType">
 *   <restriction base="string">
 *     <pattern value="([A-Za-z0-9\-_\.!~\*'\(\)])+(:[A-Za-z0-9\-_\.!~\*'\(\)]+)*"/>
 *   </restriction>
 * </simpleType>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetSpecType", propOrder = {"value"})
public class SetSpec {
	
	@XmlValue
	private String value;

	public SetSpec(String value) {
        this.value = value;
	}
	
	public SetSpec() { }

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

}
