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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.tdwg.Actor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Person", propOrder = {})
@XmlRootElement(name = "Person", namespace = "http://rs.tdwg.org/ontology/voc/Person#")
public class Person extends Actor {

}
