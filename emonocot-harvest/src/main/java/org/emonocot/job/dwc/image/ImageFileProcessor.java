package org.emonocot.job.dwc.image;

import java.io.File;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.ImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.emonocot.model.media.Image;
import org.emonocot.ws.GetResourceClient;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.io.FileSystemResource;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class ImageFileProcessor implements ItemProcessor<Image, Image> {
    
    /**
     *
     */
    private final Integer THUMBNAIL_DIMENSION = 100;
    
    /**
     *
     */
    private final Integer IMAGE_DIMENSION = 1000;

    /**
     *
     */
    private FileSystemResource imageDirectory;

    /**
     *
     */
    private FileSystemResource thumbnailDirectory;

    /**
     *
     */
    private GeometryFactory geometryFactory = new GeometryFactory();

    /**
     *
     */
    private String authorityName;

    /**
     *
     */
    private GetResourceClient getResourceClient;

    public void setThumbnailDirectory(FileSystemResource thumbnailDirectory) {
        this.thumbnailDirectory = thumbnailDirectory;
    }
    
    public void setImageDirectory(FileSystemResource imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public void setGetResourceClient(GetResourceClient getResourceClient) {
        this.getResourceClient = getResourceClient;
    }

    public Image process(Image image) throws Exception {       
        String imageFileName = imageDirectory.getFile().getAbsolutePath() + File.separatorChar + image.getIdentifier() + '.' + image.getFormat();
        String thumbnailFileName = thumbnailDirectory.getFile().getAbsolutePath() + File.separatorChar + image.getIdentifier() + '.' + image.getFormat();
        File file = new File(imageFileName);
        System.out.println("Checking for " + imageFileName);
        if(file.exists()) {
            System.out.println("File Exists");
            return null;
        } else {
            try {
                getResourceClient.getBinaryResource(authorityName,
                        image.getUrl(),
                        "1",
                        imageFileName);
                file = new File(imageFileName);
                ImageInfo imageInfo = Sanselan.getImageInfo(file);
                Integer width = imageInfo.getWidth();
                Integer height = imageInfo.getHeight();
                System.out.println(width + " * " + height);
                
                
                if(width > height) {
                    Integer newWidth = (width / height) * 100; 
                    Integer offset = (newWidth - 100) / 2;
                    // shrink to 100px high then crop
                    ConvertCmd cmd = new ConvertCmd();                    
                    IMOperation op = new IMOperation();
                    op.addImage(imageFileName);
                    op.resize(100, newWidth);                    
                    op.addImage(thumbnailFileName);
                    cmd.run(op);
                    
                    IMOperation op2 = new IMOperation();
                    op2 = new IMOperation();
                    op2.addImage(thumbnailFileName);
                    op2.crop(100, 100, offset);
                    cmd.run(op2);
                    
                } else {
                    
                }
                IImageMetadata metadata = Sanselan.getMetadata(file);
                
                if (metadata instanceof JpegImageMetadata) {
                    JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
                    StringBuffer keywords = null;
                    StringBuffer spatial = null;
                    
                    for(Object o : jpegMetadata.getItems()) {
                        if(o instanceof ImageMetadata.Item) {
                            ImageMetadata.Item item = (ImageMetadata.Item) o;
                            if(item.getKeyword().equals("Object Name") && image.getCaption() == null) {
                                image.setCaption(item.getText());
                            }
                            if(item.getKeyword().equals("Keywords")) {
                                if(keywords == null) {
                                    keywords = new StringBuffer();
                                    keywords.append(item.getText());
                                } else {
                                    keywords.append(", " + item.getText());
                                }
                            } else if(item.getKeyword().equals("Sublocation") || item.getKeyword().equals("Province/State") || item.getKeyword().equals("Country/Primary Location Name")) {
                                if(spatial == null) {
                                    spatial = new StringBuffer();
                                    spatial.append(item.getText());
                                } else {
                                    spatial.append(", " + item.getText());
                                }
                            }
                        }
                    }
                    if(spatial != null && image.getSpatial() == null) {
                        image.setSpatial(spatial.toString());
                    }
                    if(keywords != null && image.getKeywords() == null) {
                        image.setKeywords(keywords.toString());
                    }
                    if(jpegMetadata.findEXIFValue(TiffConstants.TIFF_TAG_ARTIST) != null && image.getCreator() == null) {
                        image.setCreator(jpegMetadata.findEXIFValue(TiffConstants.TIFF_TAG_ARTIST).getStringValue());
                    }
                    if(jpegMetadata.findEXIFValue(TiffConstants.TIFF_TAG_IMAGE_DESCRIPTION) != null && image.getDescription() == null) {
                        image.setDescription(jpegMetadata.findEXIFValue(TiffConstants.TIFF_TAG_IMAGE_DESCRIPTION).getStringValue());
                    }
                    TiffImageMetadata exifMetadata = jpegMetadata.getExif();
                    if (exifMetadata != null) {
                        TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
                        if (null != gpsInfo && image.getLocation() == null) {
                            Point location = geometryFactory
                                    .createPoint(new Coordinate(
                                            gpsInfo.getLongitudeAsDegreesEast(),
                                            gpsInfo.getLatitudeAsDegreesNorth()));
                            image.setLocation(location);
                        }
                    }
                }
            } catch(Exception e) {
                System.out.println(e.toString());
                for(StackTraceElement ste : e.getStackTrace()) {
                    System.out.println(ste);
                }
            }
        }
        return image;
    }

}
