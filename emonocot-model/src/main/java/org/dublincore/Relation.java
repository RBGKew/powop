// $Id$
/**
* Copyright (C) 2007 EDIT
* European Distributed Institute of Taxonomy 
* http://www.e-taxonomy.eu
* 
* The contents of this file are subject to the Mozilla Public License Version 1.1
* See LICENSE.TXT at the top of this package for the full license terms.
*/
package org.dublincore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.tdwg.voc.LinkType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Relation", propOrder = {})
@XmlRootElement(name = "Relation", namespace = "http://purl.org/dc/elements/1.1/")
public class Relation extends LinkType {

}
