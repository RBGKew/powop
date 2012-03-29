package org.emonocot.ws.checklist;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.FileEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.BasicHttpParams;
import org.emonocot.easymock.matcher.HttpGetMatcher;
import org.emonocot.model.marshall.xml.StaxDriver;
import org.emonocot.model.marshall.xml.UriConverter;
import org.emonocot.model.marshall.xml.XStreamMarshaller;
import org.junit.Before;
import org.junit.Test;
import org.openarchives.pmh.marshall.xml.OpenArchivesQNameMapFactory;
import org.openarchives.pmh.marshall.xml.ReflectionProviderFactory;
import org.openarchives.pmh.marshall.xml.ResumptionTokenConverter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;

import com.bea.xml.stream.MXParserFactory;
import com.thoughtworks.xstream.converters.ConverterMatcher;

/**
 *
 * @author ben
 *
 */
public class OaiPmhClientTest {

    /**
     *
     */
    private String authorityName;
    /**
     *
     */
    private String authorityUri;
    /**
     *
     */
    private String dateLastHarvested;
    /**
     *
     */
    private String resumptionToken;
    /**
     *
     */
    private String set;
    /**
     *
     */
    private String servicesClientIdentifier;
    /**
     *
     */
    private File tempFile = null;
    /**
     *
     */
    private HttpResponse response;

    /**
    *
    */
   private Unmarshaller unmarshaller = null;

    /**
     *
     */
    ClassPathResource resource = new ClassPathResource("/org/emonocot/job/oai/Acoraceae.xml");

    /**
     * @throws Exception if there is a problem setting up the test
     */
    @Before
    public final void setUp() throws Exception {
        OpenArchivesQNameMapFactory oaiPmhQNameMapFactory
        = new OpenArchivesQNameMapFactory();
        oaiPmhQNameMapFactory.afterPropertiesSet();
        ReflectionProviderFactory reflectionProviderFactory
        = new ReflectionProviderFactory();
        reflectionProviderFactory.afterPropertiesSet();

        StaxDriver streamDriver = new StaxDriver(
                oaiPmhQNameMapFactory.getObject());
        streamDriver.setRepairingNamespace(false);
        streamDriver.setXmlInputFactory(new MXParserFactory());

        unmarshaller = new XStreamMarshaller();

        ((XStreamMarshaller) unmarshaller).setAutodetectAnnotations(true);
        ((XStreamMarshaller) unmarshaller)
            .setReflectionProvider(reflectionProviderFactory.getObject());
        ((XStreamMarshaller) unmarshaller)
            .setQNameMap(oaiPmhQNameMapFactory.getObject());

        ((XStreamMarshaller) unmarshaller).setStreamDriver(streamDriver);
        ((XStreamMarshaller) unmarshaller).setConverters(
                new ConverterMatcher[] {
                        new ResumptionTokenConverter(),
                        new UriConverter()
                        });
        ((XStreamMarshaller) unmarshaller).afterPropertiesSet();
        // Initialise "Job Parameters"
        resumptionToken = null;
        tempFile = File.createTempFile("tmpFile", "xml");
        set = "setParam";
        dateLastHarvested = "1";
        // also see 'from' RequestParam in HttpGet() constructors
        authorityUri = "http://test.source.uri/path";
        authorityName = "Test Authoritative Source";
        servicesClientIdentifier = "OaiPmhClientTest";

        response = new BasicHttpResponse(new BasicStatusLine(
                HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "Normal Response"));
        response.setEntity(
                new FileEntity(resource.getFile(), "text/xml"));
    }

    /**
     *
     * @throws IOException if there is a problem with the io streams
     * @throws URISyntaxException if there is a problem with the uri
     */
    @Test
    public final void testListRecords() throws IOException, URISyntaxException {
        // expected params are set manually to ensure
        // the expected URL matches the order
        HttpGet get = new HttpGet(
                authorityUri
                        + "?scratchpad=" + servicesClientIdentifier
                        + "&verb=ListRecords&metadataPrefix=rdf"
                        + "&from=1970-01-01T00:00:00Z&set=" + set);
        HttpClient mockHttpClient = createMock(HttpClient.class);
        expect(mockHttpClient.getParams()).andReturn(new BasicHttpParams());
        expect(mockHttpClient.execute(HttpGetMatcher.eqHttpGet(get)))
                .andReturn(response);
        replay(mockHttpClient);

        StepExecution stepExecution
            = new StepExecution("test", new JobExecution(1L));

        OaiPmhClient underTest = new OaiPmhClient();
        underTest.setHttpClient(mockHttpClient);
        underTest.beforeStep(stepExecution);
        underTest.setServicesClientIdentifier(servicesClientIdentifier);

        underTest.listRecords(authorityName, authorityUri, dateLastHarvested,
                tempFile.getAbsolutePath(), set);
        verify(mockHttpClient);

    }

    /**
     *
     * @throws IOException if there is aproblem with the io streams
     */
    @Test
    public final void testResumptionTokenUsage()
            throws IOException {
        resumptionToken = "token";
        HttpClient mockHttpClient = createMock(HttpClient.class);

        HttpGet get = new HttpGet(authorityUri + "?scratchpad="
                + servicesClientIdentifier + "&resumptionToken="
                + resumptionToken + "&verb=ListRecords");
        JobExecution jobExecution = new JobExecution(1L);
        jobExecution.getExecutionContext()
            .put("resumption.token", resumptionToken);
        StepExecution stepExecution
            = new StepExecution("test", jobExecution);

        expect(mockHttpClient.getParams()).andReturn(new BasicHttpParams());
        expect(mockHttpClient.execute(HttpGetMatcher.eqHttpGet(get)))
                .andReturn(response);
        replay(mockHttpClient);

        OaiPmhClient underTest = new OaiPmhClient();
        underTest.setHttpClient(mockHttpClient);
        underTest.beforeStep(stepExecution);
        underTest.setServicesClientIdentifier(servicesClientIdentifier);

        underTest.listRecords(authorityName, authorityUri, dateLastHarvested,
                tempFile.getAbsolutePath(), set);
        verify(mockHttpClient);
    }

    /**
     *
     * @throws Exception if there is a problem
     */
    @Test
    public final void testIdDoesNotExist() throws Exception {
        // expected params are set manually to ensure
        // the expected URL matches the order
        String identifier = "urn:kew.org:wcs:taxon:1234";
        HttpGet get = new HttpGet(
                authorityUri
                        + "?scratchpad=" + servicesClientIdentifier
                        + "&verb=GetRecord&metadataPrefix=rdf"
                        + "&identifier=" + identifier);
        response = new BasicHttpResponse(new BasicStatusLine(
                HttpVersion.HTTP_1_1, HttpStatus.SC_BAD_REQUEST, "IdDoesNotExist"));
        ClassPathResource idDoesNotExistResource = new ClassPathResource("/org/emonocot/job/oai/IdDoesNotExist.xml");
        response.setEntity(
                new FileEntity(idDoesNotExistResource.getFile(), "text/xml"));
        HttpClient mockHttpClient = createMock(HttpClient.class);
        expect(mockHttpClient.getParams()).andReturn(new BasicHttpParams());
        expect(mockHttpClient.execute(HttpGetMatcher.eqHttpGet(get)))
                .andReturn(response);
        replay(mockHttpClient);
        StepExecution stepExecution
            = new StepExecution("test", new JobExecution(1L));

        OaiPmhClient underTest = new OaiPmhClient();
        underTest.setUnmarshaller(unmarshaller);

        underTest.setHttpClient(mockHttpClient);
        underTest.beforeStep(stepExecution);
        underTest.setServicesClientIdentifier(servicesClientIdentifier);
        OaiPmhException oaiPmhException = null;

        try {
            underTest.getRecord(identifier, authorityName, authorityUri,
                    tempFile.getAbsolutePath());
        } catch (OaiPmhException ope) {
            oaiPmhException = ope;
        }
        if (oaiPmhException == null) {
            fail("OaiPmhException expected");
        }
        assertEquals("idDoesNotExist", oaiPmhException.getCode().toString());
        assertEquals(
                "Could not find object with identifier urn:kew.org:wcs:taxon:219703",
                oaiPmhException.getMessage());
        verify(mockHttpClient);
    }
}
