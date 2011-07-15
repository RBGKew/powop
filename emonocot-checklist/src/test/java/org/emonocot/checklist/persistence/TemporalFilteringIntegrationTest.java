package org.emonocot.checklist.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.model.pager.Page;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "/applicationContext-test.xml" })
public class TemporalFilteringIntegrationTest extends AbstractPersistenceTestSupport {
	
	@Autowired
	private TaxonDao taxonDao;
	
	@Test
	public void nonFilteringTest() {
		assertNotNull("TaxonDAO should not be null",taxonDao);
		Page<ChangeEvent<Taxon>> result = taxonDao.page(null, null, null, null, null);
		assertEquals("page() should return five objects", Integer.valueOf(5), result.size());		
	}
	
	@Test
	public void filteringFromTest() {
		DateTime dateTime = new DateTime(2011,1, 1, 12, 0, 0, 0);
		
		Page<ChangeEvent<Taxon>> result = taxonDao.page(null, dateTime, null, null, null);
		assertEquals("page() should return three objects", Integer.valueOf(3), result.size());		
	}
	
	@Test
	public void filteringToTest() {
		DateTime dateTime = new DateTime(2011,10, 1, 12, 0, 0, 0);
		
		Page<ChangeEvent<Taxon>> result = taxonDao.page(null, null, dateTime, null, null);
		assertEquals("page() should return four objects", Integer.valueOf(4), result.size());		
	}
	
	@Test
	public void filteringFromAndToTest() {
		DateTime from = new DateTime(2011,1, 1, 12, 0, 0, 0);
		DateTime to = new DateTime(2011,10, 1, 12, 0, 0, 0);
		
		Page<ChangeEvent<Taxon>> result = taxonDao.page(null, from, to, null, null);
		assertEquals("page() should return two objects", Integer.valueOf(2), result.size());		
	}
}
