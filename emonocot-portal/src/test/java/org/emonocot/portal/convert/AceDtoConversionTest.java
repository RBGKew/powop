package org.emonocot.portal.convert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.codehaus.jackson.map.ObjectMapper;
import org.emonocot.model.auth.User;
import org.emonocot.model.marshall.json.CustomObjectMapperFactory;
import org.emonocot.portal.controller.form.AceDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.acls.domain.BasePermission;

public class AceDtoConversionTest {
	
	private ObjectMapper objectMapper;
	
	@Before
    public void setUp() {        
        CustomObjectMapperFactory objectMapperFactory = new CustomObjectMapperFactory();
        objectMapper = objectMapperFactory.getObject();
    }
	
	 /**
    *
    * @throws Exception
    *             if there is a problem serializing the object
    */
    @Test
    public void testWriteAce() throws Exception {
        AceDto ace = new AceDto();
        User user = new User();
        ace.setPermission(BasePermission.CREATE);

        ace.setObject("testIdentifier");
        ace.setPrincipal("userIdentifier");
        try {
            objectMapper.writeValueAsString(ace);
         } catch (Exception e) {
              fail("No exception expected here");
         }
    }

    /**
    *
    * @throws Exception
    *             if there is a problem serializing the object
    */
    @Test
    public void testAce() throws Exception {

        AceDto aceDto = objectMapper.readValue("{\"principal\":\"userIdentifier\",\"object\":\"testIdentifier\",\"permission\":\"CREATE\"}", AceDto.class);

        assertEquals("testIdentifier", aceDto.getObject());
        assertEquals("userIdentifier", aceDto.getPrincipal());
        assertEquals(BasePermission.CREATE, aceDto.getPermission());
    }

}
