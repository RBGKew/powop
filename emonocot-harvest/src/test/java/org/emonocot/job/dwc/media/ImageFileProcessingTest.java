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
package org.emonocot.job.dwc.media;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Properties;

import org.emonocot.harvest.common.GetResourceClient;
import org.emonocot.harvest.media.ImageFileProcessorImpl;
import org.emonocot.harvest.media.ImageMetadataExtractorImpl;
import org.emonocot.harvest.media.ImageThumbnailGeneratorImpl;
import org.emonocot.model.Image;
import org.emonocot.model.constants.MediaFormat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 *
 * @author ben
 *
 */
public class ImageFileProcessingTest {

	/**
	 *
	 */
	private ImageFileProcessorImpl imageFileProcessor = new ImageFileProcessorImpl();

	/**
	 *
	 */
	private ImageThumbnailGeneratorImpl imageThumbnailGenerator = new ImageThumbnailGeneratorImpl();

	/**
	 *
	 */
	private ImageMetadataExtractorImpl imageMetadataExtractor = new ImageMetadataExtractorImpl();

	/**
	 *
	 */
	private Image image = new Image();

	/**
	 * @throws Exception
	 *             if there is a problem
	 */
	@Before
	public final void setUp() throws Exception {
		LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
		validatorFactory.afterPropertiesSet();

		image.setFormat(MediaFormat.jpg);
		GetResourceClient getResourceClient = new GetResourceClient();
		imageFileProcessor.setGetResourceClient(getResourceClient);
		String imagesDirectoryName = System.getProperty("java.io.tmpdir")
				+ File.separatorChar + "images";
		File imagesDirectory = new File(imagesDirectoryName);
		imagesDirectory.mkdir();
		imagesDirectory.deleteOnExit();
		imageMetadataExtractor.setImageDirectory(imagesDirectoryName);
		imageMetadataExtractor.setValidator(validatorFactory.getValidator());
		imageMetadataExtractor.afterPropertiesSet();
		imageFileProcessor.setImageDirectory(imagesDirectoryName);
		String thumbnailDirectoryName = System.getProperty("java.io.tmpdir")
				+ File.separatorChar + "thumbnails";
		File thumbnailDirectory = new File(thumbnailDirectoryName);
		thumbnailDirectory.mkdir();
		thumbnailDirectory.deleteOnExit();

		Resource propertiesFile = new ClassPathResource(
				"/META-INF/spring/application.properties");
		Properties properties = new Properties();
		properties.load(propertiesFile.getInputStream());

		String repository = properties.getProperty("test.resource.baseUrl");
		image.setIdentifier(repository + "dwc.jpg");
		imageThumbnailGenerator.setImageDirectory(imagesDirectoryName);
		imageThumbnailGenerator.setThumbnailDirectory(thumbnailDirectoryName);
		imageThumbnailGenerator.setImageMagickSearchPath(properties.getProperty(
				"harvester.imagemagick.path", "/usr/bin"));
		getResourceClient.setProxyHost(properties.getProperty("http.proxyHost",
				null));
		getResourceClient.setProxyPort(properties.getProperty("http.proxyPort",
				null));
		String imageFileName = imagesDirectoryName + File.separatorChar  + image.getId() + '.' + image.getFormat();
		(new File(imageFileName)).delete();
	}

	/**
	 * @throws Exception
	 *             if there is a problem accessing the file
	 */
	@Test
	public final void testProcess() throws Exception {
		Image i = imageFileProcessor.process(image);
		assertTrue("The image file processor failed to return an image", i != null);
		imageMetadataExtractor.process(i);
		assertEquals("Arecaceae; Howea forsteriana", i.getTitle());
		assertEquals("Male inflorescences", i.getDescription());
		assertEquals("William J. Baker", i.getCreator());
		assertEquals(
				"ARECOIDEAE, Arecaceae, Areceae, Howea, Linospadicinae, Palmae, Palms, flowers, inflorescences",
				i.getSubject());
		assertEquals("Path to Little island, Lord Howe Island, Australia", i.getSpatial());
		assertEquals("www.creativecommons.org#Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Unported Licence", i.getLicense());
		assertEquals(MediaFormat.jpg, i.getFormat());
	}

}
