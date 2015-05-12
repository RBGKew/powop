/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
