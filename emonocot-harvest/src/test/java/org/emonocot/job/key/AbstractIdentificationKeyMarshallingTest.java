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
package org.emonocot.job.key;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.job.common.AbstractXmlEventReaderTest;
import org.junit.Before;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.tdwg.ubif.Agent;
import org.tdwg.ubif.CategoricalCharacter;
import org.tdwg.ubif.Dataset;
import org.tdwg.ubif.Representation;
import org.tdwg.ubif.StateDefinition;
import org.tdwg.ubif.TaxonName;
import org.tdwg.ubif.marshall.xml.IgnoreConverter;
import org.tdwg.ubif.marshall.xml.QNameMapFactory;
import org.tdwg.ubif.marshall.xml.ReflectionProviderFactory;

import com.thoughtworks.xstream.converters.ConverterMatcher;
import com.thoughtworks.xstream.converters.basic.URIConverter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public abstract class AbstractIdentificationKeyMarshallingTest extends
AbstractXmlEventReaderTest {

	/**
	 *
	 */
	private Unmarshaller unmarshaller = null;

	/**
	 * @throws Exception
	 *             if there is a problem initializing the unmarshaller
	 */
	@Before
	public final void setUp() throws Exception {
		QNameMapFactory qNameMapFactory = new QNameMapFactory();
		qNameMapFactory.afterPropertiesSet();
		ReflectionProviderFactory reflectionProviderFactory = new ReflectionProviderFactory();
		reflectionProviderFactory.afterPropertiesSet();

		StaxDriver streamDriver = new StaxDriver(qNameMapFactory.getObject());
		streamDriver.setRepairingNamespace(false);

		unmarshaller = new XStreamMarshaller();

		((XStreamMarshaller) unmarshaller).setAutodetectAnnotations(true);
		Map<String, Class<?>> aliases = new HashMap<String, Class<?>>();
		aliases.put("Representation", Representation.class);
		aliases.put("Agent", Agent.class);
		aliases.put("TaxonName", TaxonName.class);
		aliases.put("CategoricalCharacter", CategoricalCharacter.class);
		aliases.put("StateDefinition", StateDefinition.class);
		aliases.put("Dataset", Dataset.class);

		((XStreamMarshaller) unmarshaller).setAliases(aliases);

		((XStreamMarshaller) unmarshaller).setStreamDriver(streamDriver);
		((XStreamMarshaller) unmarshaller)
		.setConverters(new ConverterMatcher[] { new URIConverter(), new IgnoreConverter() });
		((XStreamMarshaller) unmarshaller).afterPropertiesSet();

	}

	/**
	 *
	 * @return the unmarshaller;
	 */
	public Unmarshaller getUnmarshaller() {
		return unmarshaller;
	}
}
