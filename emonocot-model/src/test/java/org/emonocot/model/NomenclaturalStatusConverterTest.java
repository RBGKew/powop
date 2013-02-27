package org.emonocot.model;

import static org.junit.Assert.assertEquals;

import org.emonocot.model.convert.NomenclaturalStatusConverter;
import org.gbif.ecat.voc.NomenclaturalStatus;
import org.junit.Test;

public class NomenclaturalStatusConverterTest {
	
	private NomenclaturalStatusConverter converter = new NomenclaturalStatusConverter();
	
	@Test
	public void testConverter() {		
		assertEquals(NomenclaturalStatus.Available,converter.convert("Available"));
		assertEquals(null,converter.convert(""));		
	}

}
