package org.emonocot.checklist.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.model.pager.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "/applicationContext-test.xml" })
public class FamilyFilteringIntegrationTest extends AbstractPersistenceTestSupport {
	
	@Autowired
	private TaxonDao taxonDao;
	
	@Test
	public void nonFilteringTest() {
		assertNotNull("TaxonDAO should not be null",taxonDao);
		Page<ChangeEvent<Taxon>> result = taxonDao.page(null, null, null, null, null);
		assertEquals("page() should return five objects", Integer.valueOf(5), result.size());		
	}

	@Test
	public void familyFilteringWithValidFilterTermTest() {
		Page<ChangeEvent<Taxon>> result = taxonDao.page("Loreaceae", null, null, null, null);
		assertEquals("page() should return four objects", Integer.valueOf(4), result.size());		
	}
	
	@Test
	public void familyFilteringWithInvalidFilterTermTest() {
		Page<ChangeEvent<Taxon>> result = taxonDao.page("Badgeraceae", null, null, null, null);
		assertEquals("page() should return no objects", Integer.valueOf(0), result.size());		
	}
}
