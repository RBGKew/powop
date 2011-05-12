package org.tdwg.voc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
@XmlAccessorType(XmlAccessType.FIELD)

@XmlType(name = "StringType", propOrder = {})
public class StringType {
	@XmlValue
    public String value;

    @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
    public String lang;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
}
