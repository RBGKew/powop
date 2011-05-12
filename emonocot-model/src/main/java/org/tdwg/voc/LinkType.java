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

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
public abstract class LinkType {
	
	@XmlAttribute(namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
    protected URI resource;

	public Serializable getResource() {
		return resource;
	}

	public void setResource(URI resource) {
		this.resource = resource;
	}
}
