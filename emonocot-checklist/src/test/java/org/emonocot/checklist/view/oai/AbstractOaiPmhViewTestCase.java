package org.emonocot.checklist.view.oai;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.examples.RecursiveElementNameAndTextQualifier;
import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.emonocot.model.marshall.StaxDriver;
import org.emonocot.model.marshall.UriConverter;
import org.emonocot.model.marshall.XStreamMarshaller;
import org.emonocot.test.xml.IgnoreXPathDifferenceListener;
import org.openarchives.pmh.marshall.OpenArchivesQNameMapFactory;
import org.openarchives.pmh.marshall.ReflectionProviderFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.xml.sax.InputSource;

import com.bea.xml.stream.XMLOutputFactoryBase;
import com.thoughtworks.xstream.converters.ConverterMatcher;
/**
 *
 * @author ben
 *
 */
public abstract class AbstractOaiPmhViewTestCase extends XMLTestCase {
    /**
     *
     */
    private AbstractOaiPmhResponseView view;

    /**
     *
     */
    private Map model;

    /**
     *
     */
    private HttpServletRequest request;

    /**
     *
     */
    private HttpServletResponse response;

    /**
     *
     */
    private boolean validateAgainstSchemas = true;

    /**
     *
     * @param validate
     *            should we validate against the provided schemas
     */
    public final void setValidateAgainstSchemas(final boolean validate) {
        this.validateAgainstSchemas = validate;
    }

    /**
     *
     * @throws Exception if there is a problem parsing the xml
     */
    public final void setUp() throws Exception {
        view = setView();
        XMLUnit.setControlParser(
                "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        XMLUnit.setTestParser(
                "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        XMLUnit.setSAXParserFactory(
                "org.apache.xerces.jaxp.SAXParserFactoryImpl");
        XMLUnit.setIgnoreWhitespace(true);
        ReflectionProviderFactory reflectionProviderFactory
            = new ReflectionProviderFactory();
        reflectionProviderFactory.afterPropertiesSet();
        OpenArchivesQNameMapFactory qNameMapFactory
            = new OpenArchivesQNameMapFactory();
        qNameMapFactory.afterPropertiesSet();
        XStreamMarshaller marshaller = new XStreamMarshaller();

        marshaller.setAutodetectAnnotations(true);
        marshaller.setConverters(new ConverterMatcher[]{new UriConverter()});
        marshaller.setReflectionProvider(reflectionProviderFactory.getObject());
        StaxDriver staxDriver = new StaxDriver(qNameMapFactory.getObject());
        staxDriver.setXmlOutputFactory(new XMLOutputFactoryBase());
        staxDriver.setRepairingNamespace(false);
        staxDriver.setStartDocument(false);

        marshaller.setStreamDriver(staxDriver);

        marshaller.afterPropertiesSet();
        view.setMarshaller(marshaller);
        DozerBeanMapperFactoryBean mapperFactory
            = new DozerBeanMapperFactoryBean();
         mapperFactory.setMappingFiles(new Resource[]{
         new ClassPathResource(
                 "/org/emonocot/checklist/view/assembler/mapping.xml")
//         ,
//         new ClassPathResource(
//                 "/org/kew/grassbase/view/assembler/mapping.xml")
         });
        mapperFactory.afterPropertiesSet();
        view.setMapper((Mapper) mapperFactory.getObject());
        model = new HashMap();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        doSetup(model);
    }

    /**
     *
     * @return an instance of the oai pmh response to render
     */
    public abstract AbstractOaiPmhResponseView setView();

    /**
     *
     * @param newModel Populate the model with test data
     */
    public abstract void doSetup(final Map newModel);

    /**
     *
     * @return the expected response as a string
     */
    public abstract String getExpected();

    /**
     *
     * @throws Exception if there is a problem validating the generated xml
     */
    public final void testView() throws Exception {
        view.render(model, request, response);
        System.out.println(((MockHttpServletResponse) response)
                .getContentAsString());
        Diff difference = new Diff(getExpected(),
                ((MockHttpServletResponse) response).getContentAsString());
        difference
                .overrideDifferenceListener(new IgnoreXPathDifferenceListener(
                  "/OAI-PMH[1]/responseDate[1]/text()[1]",
                  "/OAI-PMH[1]/ListIdentifiers[1]/resumptionToken[1]/text()[1]",
                  "/OAI-PMH[1]/ListRecords[1]/resumptionToken[1]/text()[1]",
                  "/OAI-PMH[1]/ListRecords[1]/record[1]/metadata[1]/dc[1]/"
                  + "date[1]/text()[1]",
                  "/OAI-PMH[1]/ListRecords[1]/record[2]/metadata[1]/dc[1]/"
                  + "date[1]/text()[1]",
                  "/OAI-PMH[1]/ListRecords[1]/record[3]/metadata[1]/dc[1]/"
                  + "date[1]/text()[1]",
                  "/OAI-PMH[1]/ListRecords[1]/record[4]/metadata[1]/dc[1]/"
                  + "date[1]/text()[1]",
                  "/OAI-PMH[1]/ListRecords[1]/record[5]/metadata[1]/dc[1]/"
                  + "date[1]/text()[1]",
                  "/OAI-PMH[1]/ListRecords[1]/record[6]/metadata[1]/dc[1]/"
                  + "date[1]/text()[1]",
                  "/OAI-PMH[1]/ListRecords[1]/record[7]/metadata[1]/dc[1]/"
                  + "date[1]/text()[1]",
                  "/OAI-PMH[1]/ListRecords[1]/record[8]/metadata[1]/dc[1]/"
                  + "date[1]/text()[1]",
                  "/OAI-PMH[1]/ListRecords[1]/record[9]/metadata[1]/dc[1]/"
                  + "date[1]/text()[1]",
                  "/OAI-PMH[1]/ListRecords[1]/record[10]/metadata[1]/dc[1]/"
                  + "date[1]/text()[1]"));
        difference
                .overrideElementQualifier(
                        new RecursiveElementNameAndTextQualifier());
        if (!difference.similar()) {
            System.out.println(difference.toString());
        }
        assertTrue("test XML matches control skeleton XML",
                difference.similar());
        if (validateAgainstSchemas) {
            InputSource inputSource = new InputSource(new StringReader(
                    ((MockHttpServletResponse) response).getContentAsString()));
            Validator validator = new Validator(inputSource);
            validator.useXMLSchema(true);
            validator.setJAXP12SchemaSource(new String[] {
                    "target/test-classes/org/openarchives/pmh/oai-pmh.xsd",
                    "target/test-classes/org/openarchives/pmh/oai_dc.xsd" });
            validator.assertIsValid();
        }
    }
}