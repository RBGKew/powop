package org.powo.harvest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.powo.model.registry.Organisation;
import org.springframework.http.MediaType;

public class OrganisationControllerIntegrationTest extends AbstractControllerTest {

	@Before
	public void setup() throws Exception {
		createSource("org1", "http://org1.com", "Org 1");
		createSource("org2", "http://org2.com", "Org 2");
		createSource("org3", "http://org3.com", "Org 3");
		doSetUp();
	}
	@After
	public void tearDown() {
		doTearDown();
	}

	@Ignore
	@Test
	public void testValidCreate() throws Exception {
		Organisation org = Organisation.builder()
				.identifier("blarg")
				.description("blargedy blarg blarg")
				.abbreviation("BLG")
				.subject("stuff")
				.title("Blarg")
				.bibliographicCitation("1983 Blarg L.")
				.build();

		mvc.perform(post("/api/1/organisation")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectToJsonString(org)))
		.andExpect(status().isCreated());
	}

	@Ignore
	@Test
	public void testInvalidCreate() throws Exception {
		Organisation org = Organisation.builder()
				.identifier("blarg")
				.description("blargedy blarg blarg")
				.subject("stuff")
				.title("Blarg")
				.bibliographicCitation("1983 Blarg L.")
				.build();

		mvc.perform(post("/api/1/organisation")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectToJsonString(org)))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.validationErrors.abbreviation").value("may not be empty"));
	}
}
