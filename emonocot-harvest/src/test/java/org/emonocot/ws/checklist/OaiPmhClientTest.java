package org.emonocot.ws.checklist;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.FileEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.BasicHttpParams;
import org.emonocot.easymock.matcher.HttpGetMatcher;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

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
    private String authorityURI;
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
    private String temporaryFileName;
    /**
     *
     */
    private HttpResponse response;

    /**
     * @throws Exception if there is a problem setting up the test
     */
    @Before
    public final void setUp() throws Exception {
        // Initialise "Job Parameters"
        resumptionToken = null;
        temporaryFileName = "tmpFile";
        set = "setParam";
        dateLastHarvested = "1";
        // also see 'from' RequestParam in HttpGet() constructors
        authorityURI = "http://test.authority.uri/path";
        authorityName = "Test Authoritative Source";
        servicesClientIdentifier = "OaiPmhClientTest";

        response = new BasicHttpResponse(new BasicStatusLine(
                HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "Normal Response"));
        response.setEntity(
                new FileEntity(new File("filename.xml"), "text/xml"));
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
                authorityURI
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

        underTest.listRecords(authorityName, authorityURI, dateLastHarvested,
                temporaryFileName, set);
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

        HttpGet get = new HttpGet(authorityURI + "?scratchpad="
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

        underTest.listRecords(authorityName, authorityURI, dateLastHarvested,
                temporaryFileName, set);
        verify(mockHttpClient);

    }

}
