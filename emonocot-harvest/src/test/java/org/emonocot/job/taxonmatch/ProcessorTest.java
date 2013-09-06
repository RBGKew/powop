package org.emonocot.job.taxonmatch;

import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.model.Taxon;
import org.gbif.ecat.parser.UnparsableException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jk00kg
 *
 */
public class ProcessorTest {
	
	private TaxonMatcher matcher;
	
	private Processor processor;
	
	private Taxon taxon;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    	matcher = EasyMock.createMock(TaxonMatcher.class);
    	processor = new Processor();
    	processor.setTaxonMatcher(matcher);
    	taxon = new Taxon();        
    }
    
    /**
     * @throws Exception
     */
    @Test
    public final void testUnparsable() throws Exception {
    	taxon.setScientificName("☺");
        EasyMock.expect(matcher.match("☺")).andThrow(new UnparsableException(null, "☺"));
        EasyMock.replay(matcher);
        
        Result result = processor.process(taxon);
       
        EasyMock.verify(matcher);
        assertTrue("Reult status should be " + TaxonMatchStatus.CANNOT_PARSE,
                TaxonMatchStatus.CANNOT_PARSE.equals(result.getStatus()));
    }

}
