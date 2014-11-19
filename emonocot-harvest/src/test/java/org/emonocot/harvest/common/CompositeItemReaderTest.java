/**
 * 
 */
package org.emonocot.harvest.common;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ItemReader;

/**
 * @author jk00kg
 *
 */
public class CompositeItemReaderTest {

    List<ItemReader<Object>> delegates;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        delegates = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            delegates.add((ItemReader<Object>) EasyMock.createMock(ItemReader.class));
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.emonocot.harvest.common.CompositeItemReader#read()}.
     */
    @Test
    public void testRead() throws Exception {
        ItemReader<Object> mock1 = delegates.get(0);
        ItemReader<Object> mock2 = delegates.get(1);
        EasyMock.expect(mock1.read()).andReturn("from 1st delegate");
        EasyMock.expect(mock1.read()).andReturn("from 1st delegate");
        EasyMock.expect(mock1.read()).andReturn(null);
        EasyMock.expect(mock2.read()).andReturn("from 2nd delegate");
        EasyMock.expect(mock2.read()).andReturn(null);
        EasyMock.replay(mock1,mock2);
        CompositeItemReader<Object> composite = new CompositeItemReader<>();
        composite.setDelegates(delegates);

        for (int i = 1; i <= 2; i++) {
            assertEquals("Read " + (i) + " should have returned an object", "from 1st delegate", composite.read());
        }
        assertEquals("Read 3 should have returned an object", "from 2nd delegate", composite.read());
        assertNull("There should be no more items to read", composite.read());
    }
    
    //Test case of a preceding delegate with no records but a later one does 

}
