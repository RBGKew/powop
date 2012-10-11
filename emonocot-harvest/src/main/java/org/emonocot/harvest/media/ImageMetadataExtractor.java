package org.emonocot.harvest.media;

import java.io.File;

import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.ImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.emonocot.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 *
 * @author ben
 *
 */
public class ImageMetadataExtractor implements ItemProcessor<Image, Image> {

    /**
    *
    */
    private Logger logger = LoggerFactory
            .getLogger(ImageMetadataExtractor.class);

    /**
     *
     */
    private String imageDirectory;

    /**
     *
     */
    private GeometryFactory geometryFactory = new GeometryFactory();

    /**
     *
     * @param newImageDirectory
     *            Set the image directory
     */
    public final void setImageDirectory(final String newImageDirectory) {
        this.imageDirectory = newImageDirectory;
    }

    /**
     * @param image
     *            Set the image
     * @return an image or null if the image is to be skipped
     * @throws Exception
     *             if there is a problem processing the image
     */
    public final Image process(final Image image) throws Exception {
        String imageFileName = imageDirectory + File.separatorChar
                + image.getId() + '.' + image.getFormat();
        File file = new File(imageFileName);
        logger.debug("Image File " + imageFileName);
        if (!file.exists()) {
            logger.error("File does not exist in image directory, skipping");
            return null;
        }

        IImageMetadata metadata = Sanselan.getMetadata(file);

        if (metadata instanceof JpegImageMetadata) {
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            StringBuffer keywords = null;
            StringBuffer spatial = null;

            for (Object o : jpegMetadata.getItems()) {
                if (o instanceof ImageMetadata.Item) {
                    ImageMetadata.Item item = (ImageMetadata.Item) o;
                    if (item.getKeyword().equals("Object Name")
                            && image.getTitle() == null) {
                        image.setTitle(item.getText());
                    }
                    if (item.getKeyword().equals("Keywords")) {
                        if (keywords == null) {
                            keywords = new StringBuffer();
                            keywords.append(item.getText());
                        } else {
                            keywords.append(", " + item.getText());
                        }
                    } else if (item.getKeyword().equals("Sublocation")
                            || item.getKeyword().equals("Province/State")
                            || item.getKeyword().equals(
                                    "Country/Primary Location Name")) {
                        if (spatial == null) {
                            spatial = new StringBuffer();
                            spatial.append(item.getText());
                        } else {
                            spatial.append(", " + item.getText());
                        }
                    }
                }
            }
            if (spatial != null && image.getSpatial() == null) {
                image.setSpatial(spatial.toString());
            }
            if (keywords != null && image.getSubject() == null) {
                image.setSubject(keywords.toString());
            }
            if (jpegMetadata.findEXIFValue(TiffConstants.TIFF_TAG_ARTIST) != null
                    && image.getCreator() == null) {
                image.setCreator(jpegMetadata.findEXIFValue(
                        TiffConstants.TIFF_TAG_ARTIST).getStringValue());
            }
            if (jpegMetadata
                    .findEXIFValue(TiffConstants.TIFF_TAG_IMAGE_DESCRIPTION) != null
                    && image.getDescription() == null) {
                image.setDescription(jpegMetadata.findEXIFValue(
                        TiffConstants.TIFF_TAG_IMAGE_DESCRIPTION)
                        .getStringValue());
            }
            TiffImageMetadata exifMetadata = jpegMetadata.getExif();
            if (exifMetadata != null) {
                TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
                if (null != gpsInfo && image.getLocation() == null) {
                    Point location = geometryFactory
                            .createPoint(new Coordinate(gpsInfo
                                    .getLongitudeAsDegreesEast(), gpsInfo
                                    .getLatitudeAsDegreesNorth()));
                    image.setLocation(location);
                }
            }
        }

        return image;
    }

}
