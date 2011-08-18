package org.emonocot.ws.checklist;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.FileEntity;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.emonocot.easymock.matcher.HttpGetMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class OaiPmhClientTest {

    private String authorityName;
    private String authorityURI;
    private String dateLastHarvested;
    private String resumptionToken;
    private String set;
    private String servicesClientIdentifier;
    private String temporaryFileName;
    private HttpResponse response;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // Initialise "Job Parameters"
        resumptionToken = null;
        temporaryFileName = "tmpFile";
        set = "setParam";
        dateLastHarvested = "1";// also see 'from' RequestParam in HttpGet() constructors
        authorityURI = "http://test.authority.uri/path";
        authorityName = "Test Authoritative Source";
        servicesClientIdentifier = "OaiPmhClientTest";

        response = new BasicHttpResponse(new BasicStatusLine(
                HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "Normal Response"));
        response.setEntity(new FileEntity(new File("filename.xml"), "text/xml"));
    }

    @Test
    public final void testListRecords() throws ClientProtocolException,
            IOException, URISyntaxException {
        resumptionToken = null;
        // expected params are set manually to ensure the expected URL matches the order
        HttpGet get = new HttpGet(
                authorityURI
                        + "?scratchpad=" + servicesClientIdentifier
                        + "&verb=ListRecords&metadataPrefix=rdf"
                        + "&from=1970-01-01T00:00:00.001Z&set=" + set);
        HttpClient mockHttpClient = createMock(HttpClient.class);
        expect(mockHttpClient.getParams()).andReturn(new BasicHttpParams());
        expect(mockHttpClient.execute(HttpGetMatcher.eqHttpGet(get)))
                .andReturn(response);
        replay(mockHttpClient);

        OaiPmhClient underTest = new OaiPmhClient();
        underTest.setHttpClient(mockHttpClient);
        underTest.setServicesClientIdentifier(servicesClientIdentifier);

        underTest.listRecords(authorityName, authorityURI, dateLastHarvested,
                temporaryFileName, resumptionToken, set);
        verify(mockHttpClient);

    }

    @Test
    public final void testResumptionTokenUsage()
            throws ClientProtocolException, IOException {
        resumptionToken = "token";
        HttpClient mockHttpClient = createMock(HttpClient.class);

        HttpGet get = new HttpGet(authorityURI + "?scratchpad="
                + servicesClientIdentifier + "&resumptionToken="
                + resumptionToken + "&verb=ListRecords");

        expect(mockHttpClient.getParams()).andReturn(new BasicHttpParams());
        expect(mockHttpClient.execute(HttpGetMatcher.eqHttpGet(get)))
                .andReturn(response);
        replay(mockHttpClient);

        OaiPmhClient underTest = new OaiPmhClient();
        underTest.setHttpClient(mockHttpClient);
        underTest.setServicesClientIdentifier(servicesClientIdentifier);

        underTest.listRecords(authorityName, authorityURI, dateLastHarvested,
                temporaryFileName, resumptionToken, set);
        verify(mockHttpClient);

    }

}
