/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.tdwg.ubif.marshall.xml;

import javax.xml.namespace.QName;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.tdwg.ubif.TaxonName;

import com.thoughtworks.xstream.io.xml.QNameMap;

/**
 *
 * @author ben
 *
 */
public class QNameMapFactory extends
AbstractFactoryBean<QNameMap> {

	/**
	 * Returns a QNameMap which maps the namespace:name couplets on
	 * to object types for the OAI PMH Schema.
	 *
	 * @return a map containing qnames from the oai-pmh schema
	 * namespace.
	 */
	@Override
	public final QNameMap createInstance() {
		QNameMap qNameMap = new QNameMap();
		qNameMap.setDefaultNamespace("http://rs.tdwg.org/UBIF/2006/");
		qNameMap.registerMapping(new QName("http://rs.tdwg.org/UBIF/2006/", "TaxonName"),
				TaxonName.class);
		return qNameMap;
	}

	@Override
	public final Class<?> getObjectType() {
		return QNameMap.class;
	}

}
