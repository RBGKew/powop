package org.emonocot.ws.scratchpads;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;	

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.FileEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.BasicHttpParams;
import org.custommonkey.xmlunit.XMLUnit;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class GetXMLDocClientTest {
	
	private GetXMLDocClient getXmlDocClient = new GetXMLDocClient();
	private HttpClient httpClient = EasyMock.createMock(HttpClient.class);
	private BasicHttpResponse httpResponse = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_0,HttpStatus.SC_OK,"OK"));
	private Resource content = new ClassPathResource("/org/emonocot/job/scratchpads/test.xml");
	
	@Before
    public void setUp() throws Exception {
    	getXmlDocClient.setHttpClient(httpClient);    	
    	httpResponse.setEntity(new FileEntity(content.getFile(),"text/xml"));
    	XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setControlParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        XMLUnit.setTestParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        XMLUnit.setSAXParserFactory("org.apache.xerces.jaxp.SAXParserFactoryImpl");
        XMLUnit.setTransformerFactory("org.apache.xalan.processor.TransformerFactoryImpl");
    }
	
	@Test
	public void testGetDocumentSuccessfully() throws Exception {
		File tempFile = File.createTempFile("test","xml");
		tempFile.deleteOnExit();
		
		EasyMock.expect(httpClient.getParams()).andReturn(new BasicHttpParams());
		EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class))).andReturn(httpResponse);
		EasyMock.replay(httpClient);		
		
		ExitStatus exitStatus = getXmlDocClient.getDocument("http://scratchpad.cate-araceae.org", "http://scratchpad.cate-araceae.org/spm/export.xml", Long.toString(new Date().getTime()), tempFile.getAbsolutePath());
		
		EasyMock.verify(httpClient);
	
		assertNotNull("ExitStatus should not be null",exitStatus);
		assertEquals("ExitStatus should be COMPLETED",exitStatus,ExitStatus.COMPLETED);
		assertXMLEqual("getXmlDocClient should have copied the content from the test document into the temporary file",new InputStreamReader(content.getInputStream()),new FileReader(tempFile));		
	}
	
	@Test
	public void testGetDocumentNotModified() throws Exception {
		File tempFile = File.createTempFile("test","xml");
		tempFile.deleteOnExit();
		httpResponse.setStatusLine(new BasicStatusLine(HttpVersion.HTTP_1_0,HttpStatus.SC_NOT_MODIFIED,"Not Modified"));
		
		EasyMock.expect(httpClient.getParams()).andReturn(new BasicHttpParams());
		EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class))).andReturn(httpResponse);
		EasyMock.replay(httpClient);		
		
		ExitStatus exitStatus = getXmlDocClient.getDocument("http://scratchpad.cate-araceae.org", "http://scratchpad.cate-araceae.org/spm/export.xml", Long.toString(new Date().getTime()), tempFile.getAbsolutePath());
		
		EasyMock.verify(httpClient);
	
		assertNotNull("ExitStatus should not be null",exitStatus);
		assertEquals("ExitStatus should be NOT MODIFIED",exitStatus.getExitCode(),"NOT MODIFIED");		
	}
	
	@Test
	public void testGetDocumentAnyOtherStatus() throws Exception {
		File tempFile = File.createTempFile("test","xml");
		tempFile.deleteOnExit();
		httpResponse.setStatusLine(new BasicStatusLine(HttpVersion.HTTP_1_0,HttpStatus.SC_BAD_REQUEST,"Bad Request"));
		
		EasyMock.expect(httpClient.getParams()).andReturn(new BasicHttpParams());
		EasyMock.expect(httpClient.execute(EasyMock.isA(HttpGet.class))).andReturn(httpResponse);
		EasyMock.replay(httpClient);		
		
		ExitStatus exitStatus = getXmlDocClient.getDocument("http://scratchpad.cate-araceae.org", "http://scratchpad.cate-araceae.org/spm/export.xml", Long.toString(new Date().getTime()), tempFile.getAbsolutePath());
		
		EasyMock.verify(httpClient);
	
		assertNotNull("ExitStatus should not be null",exitStatus);
		assertEquals("ExitStatus should be FAILED",exitStatus,ExitStatus.FAILED);		
	}

}
