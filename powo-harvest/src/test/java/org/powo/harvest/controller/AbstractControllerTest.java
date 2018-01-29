package org.powo.harvest.controller;

import java.io.IOException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.powo.persistence.AbstractPersistenceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath*:webmvc-config.xml")
public abstract class AbstractControllerTest extends AbstractPersistenceTest {

	@Autowired
	protected WebApplicationContext wac;

	protected MockMvc mvc;

	@Before
	public void setUpMVC() throws Exception {
		this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	protected String objectToJsonString(Object obj) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper.writeValueAsString(obj);
	}
}
