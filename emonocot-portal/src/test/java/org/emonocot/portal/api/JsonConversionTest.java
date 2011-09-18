package org.emonocot.portal.api;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.emonocot.model.taxon.Taxon;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;

public class JsonConversionTest {
    
    MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();
    
    @Test
    public void testConvertTaxon() throws Exception {
        //assertTrue(jsonConverter.canRead(Taxon.class, MediaType.APPLICATION_JSON));
//        MockHttpServletRequest httpRequest = new MockHttpServletRequest();
//        httpRequest.setContentType("application/json");
//        String content = "{\"identifier\":\"urn:kew.org:wcs:taxon:2295\",\"name\":\"Acorus\"}";
//        httpRequest.setContent(content.getBytes());
//        HttpInputMessage inputMessage = new ServletServerHttpRequest(httpRequest);
//        jsonConverter.read(Taxon.class, inputMessage);
    }
}
