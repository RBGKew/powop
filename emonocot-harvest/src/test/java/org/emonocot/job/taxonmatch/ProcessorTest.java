package org.emonocot.job.taxonmatch;

import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.emonocot.model.Taxon;
import org.gbif.ecat.parser.NameParser;
import org.gbif.ecat.parser.UnparsableException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jk00kg
 *
 */
public class ProcessorTest {

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * @throws Exception
     */
    @Test
    public final void testUnparsable() throws Exception {
        NameParser parser = EasyMock.createMock(NameParser.class);
        EasyMock.expect(parser.parse("☺")).andThrow(
                new UnparsableException(null, "☺"));
        EasyMock.replay(parser);
        Processor processor = new Processor();
        processor.setNameParser(parser);

        Taxon taxon = new Taxon();
        taxon.setScientificName("☺");
        Result result = processor.process(taxon);
        EasyMock.verify(parser);
        assertTrue("Reult status should be " + TaxonMatchStatus.CANNOT_PARSE,
                TaxonMatchStatus.CANNOT_PARSE.equals(result.getStatus()));
    }

}
