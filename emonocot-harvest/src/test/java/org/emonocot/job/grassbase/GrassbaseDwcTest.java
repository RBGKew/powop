package org.emonocot.job.grassbase;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class GrassbaseDwcTest {
    /**
     *
     */
    private String datasetPath = "/org/grassbase/TONAT";

    /**
     *
     */
    private GrassbaseDwcProcessor processor = new GrassbaseDwcProcessor();

    /**
     *
     */
    @Before
    public final void setUp() {

    }


    /**
     * @throws Exception if there is a problem
     */
    @Test
    public final void test() throws Exception {

        processor
                .processIdentifiers("target/test-classes/org/grassbase/GrassChecklist.txt");
        processor.process("target/test-classes/org/grassbase/", "target/out.txt");
    }
}
