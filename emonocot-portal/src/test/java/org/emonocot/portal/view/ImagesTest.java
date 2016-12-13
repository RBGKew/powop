package org.emonocot.portal.view;

import static org.junit.Assert.assertEquals;

import org.emonocot.model.Image;
import org.emonocot.model.Taxon;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class ImagesTest {

	@Test
	public void testHeaderImageSortingByRating() {
		Image i1 = new Image();
		Image i2 = new Image();
		Image i3 = new Image();
		Taxon t = new Taxon();

		i1.setIdentifier("i1");
		i1.setRating(1.0);
		i2.setIdentifier("i2");
		i2.setRating(3.1415);
		i3.setIdentifier("i3");
		i3.setRating(2.1415);
		t.setImages(ImmutableList.<Image>of(i1, i2, i3));

		Images images = new Images(t);

		assertEquals(i2, images.getHeaderImages().get(0));
	}
}
