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
