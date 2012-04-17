package org.emonocot.job.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.transform.stax.StAXSource;

import org.junit.Test;
import org.tdwg.ubif.CategoricalCharacter;

/**
 *
 * @author ben
 *
 */
public class UnmarshallCharacterStatesTest extends
        AbstractIdentificationKeyMarshallingTest {
    /**
     *
     */
    String filename = "/org/emonocot/job/sdd/TestKey_characterStatesFragment.xml";

    /**
     * @throws Exception
     *             if there is a problem
     *
     */
    @Test
    public final void testParseCharacterStatesFragment() throws Exception {

        CategoricalCharacter categoricalCharacter = (CategoricalCharacter) super
                .getUnmarshaller().unmarshal(
                        new StAXSource(getXMLEventReader(filename)));
        assertNotNull("categoricalCharacter should not be null",
                categoricalCharacter);
        assertEquals("categoricalCharacter.id should equal c7", "c7",
                categoricalCharacter.getId());
        assertEquals(
                "categoricalCharacter.representation.label should be shape",
                "shape", categoricalCharacter.getRepresentation().getLabel());
        assertEquals("there should be two states",3 , categoricalCharacter
                .getStates().size());

    }
}
