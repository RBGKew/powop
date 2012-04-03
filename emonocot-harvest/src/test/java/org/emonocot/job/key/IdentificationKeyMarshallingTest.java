package org.emonocot.job.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stax.StAXSource;

import org.emonocot.job.common.AbstractXmlEventReaderTest;
import org.emonocot.model.marshall.xml.StaxDriver;
import org.emonocot.model.marshall.xml.UriConverter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.tdwg.ubif.Representation;
import org.tdwg.ubif.TaxonName;
import org.tdwg.ubif.marshall.xml.QNameMapFactory;
import org.tdwg.ubif.marshall.xml.ReflectionProviderFactory;


import com.bea.xml.stream.MXParserFactory;
import com.thoughtworks.xstream.converters.ConverterMatcher;

/**
 *
 * @author ben
 *
 */
public class IdentificationKeyMarshallingTest extends
        AbstractXmlEventReaderTest {
    /**
     *
     */
    private Unmarshaller unmarshaller = null;

    /**
     *
     */
    private String filename
        = "/org/emonocot/job/sdd/TestKey_fragment.xml";

    /**
     * @throws Exception if there is a problem initializing the unmarshaller
     */
    @Before
    public final void setUp() throws Exception {
        QNameMapFactory qNameMapFactory
        = new QNameMapFactory();
        qNameMapFactory.afterPropertiesSet();
        ReflectionProviderFactory reflectionProviderFactory
        = new ReflectionProviderFactory();
        reflectionProviderFactory.afterPropertiesSet();

        StaxDriver streamDriver = new StaxDriver(
                qNameMapFactory.getObject());
        streamDriver.setRepairingNamespace(false);
        streamDriver.setXmlInputFactory(new MXParserFactory());

        unmarshaller = new XStreamMarshaller();

        ((XStreamMarshaller) unmarshaller).setAutodetectAnnotations(true);
        Map<String, Class> aliases = new HashMap<String, Class>();
        aliases.put("Representation", Representation.class);
        aliases.put("TaxonName", TaxonName.class);

        ((XStreamMarshaller) unmarshaller).setAliases(aliases);

        ((XStreamMarshaller) unmarshaller).setStreamDriver(streamDriver);
        ((XStreamMarshaller) unmarshaller).setConverters(
                new ConverterMatcher[] {
                        new UriConverter()
                        });
        ((XStreamMarshaller) unmarshaller).afterPropertiesSet();

    }

    /**
     * @throws Exception
     *             if there is a problem
     *
     */
    @Test
    public final void testParseTaxonItemFragment() throws
            Exception {

        TaxonName taxonName = (TaxonName) unmarshaller
            .unmarshal(new StAXSource(getXMLEventReader(filename)));
        assertNotNull("taxonName should not be null", taxonName);
        assertEquals("taxonName.id should equal t2", "t2", taxonName.getId());
        assertEquals("taxonName.representation.label should be Arum alpinariae", "Arum alpinariae", taxonName.getRepresentation().getLabel());
        assertEquals("taxonName.representation.mediaObject.ref should be m2", "m2", taxonName.getRepresentation().getMediaObject().getRef());
    }
}
