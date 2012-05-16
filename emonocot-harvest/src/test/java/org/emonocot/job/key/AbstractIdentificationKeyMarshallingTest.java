package org.emonocot.job.key;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.job.common.AbstractXmlEventReaderTest;
import org.emonocot.model.marshall.xml.StaxDriver;
import org.emonocot.model.marshall.xml.UriConverter;
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

import com.bea.xml.stream.MXParserFactory;
import com.thoughtworks.xstream.converters.ConverterMatcher;

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
        streamDriver.setXmlInputFactory(new MXParserFactory());

        unmarshaller = new XStreamMarshaller();

        ((XStreamMarshaller) unmarshaller).setAutodetectAnnotations(true);
        Map<String, Class> aliases = new HashMap<String, Class>();
        aliases.put("Representation", Representation.class);
        aliases.put("Agent", Agent.class);
        aliases.put("TaxonName", TaxonName.class);
        aliases.put("CategoricalCharacter", CategoricalCharacter.class);
        aliases.put("StateDefinition", StateDefinition.class);
        aliases.put("Dataset", Dataset.class);

        ((XStreamMarshaller) unmarshaller).setAliases(aliases);

        ((XStreamMarshaller) unmarshaller).setStreamDriver(streamDriver);
        ((XStreamMarshaller) unmarshaller)
                .setConverters(new ConverterMatcher[] { new UriConverter(), new IgnoreConverter() });
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