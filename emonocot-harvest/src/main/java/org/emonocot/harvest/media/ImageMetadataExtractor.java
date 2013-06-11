package org.emonocot.harvest.media;

import java.io.File;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.jempbox.xmp.XMPMetadata;
import org.apache.jempbox.xmp.XMPSchema;
import org.apache.jempbox.xmp.XMPSchemaBasicJobTicket;
import org.apache.jempbox.xmp.XMPSchemaDublinCore;
import org.apache.jempbox.xmp.XMPSchemaIptc4xmpCore;
import org.apache.jempbox.xmp.XMPSchemaPhotoshop;
import org.apache.jempbox.xmp.XMPSchemaRightsManagement;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.ImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.emonocot.harvest.common.HtmlSanitizer;
import org.emonocot.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.xml.sax.InputSource;

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
    private HtmlSanitizer sanitizer;
    
    /**
     *
     */
    private String imageDirectory;

    
    /**
     * An ordered array of metadata schemas to use in adding metadata to the image 
     */
    private Class[] schemas = {XMPSchemaIptc4xmpCore.class, XMPSchemaRightsManagement.class,
            XMPSchemaDublinCore.class, XMPSchemaPhotoshop.class};
    
    /**
     * @param sanitizer the sanitizer to set
     */
    public void setSanitizer(HtmlSanitizer sanitizer) {
        this.sanitizer = sanitizer;
    }

    /**
     *
     * @param newImageDirectory
     *            Set the image directory
     */
    public final void setImageDirectory(final String newImageDirectory) {
        this.imageDirectory = newImageDirectory;
    }

    /**
     * @param schemas the schemas to set
     */
    public void setSchemas(Class[] schemas) {
        this.schemas = schemas;
    }

    /**
     * @param image
     *            Set the image
     * @return an image or null if the image is to be skipped
     * @throws Exception
     *             if there is a problem processing the image
     */
    public final Image process(final Image image) throws Exception {
        if (sanitizer == null) {
            sanitizer = new HtmlSanitizer();
            sanitizer.afterPropertiesSet();
        }
        String imageFileName = imageDirectory + File.separatorChar + image.getId() + '.' + image.getFormat();
        File file = new File(imageFileName);
        logger.debug("Image File " + imageFileName);
        if (!file.exists()) {
            logger.error("File does not exist in image directory, skipping");
            //TODO error annotation
            return null;
        }
        boolean metadataUpdated = false;

        
        String xmpXml = Sanselan.getXmpXml(file);
        logger.debug("Attempting to extract metadata from xmp-xml:\n" + xmpXml);
        XMPMetadata xmp = XMPMetadata.load(new InputSource(new StringReader(xmpXml)));
        for(Class schemaClass : schemas) {
            XMPSchema schema = xmp.getSchemaByClass(schemaClass);
            if(schema instanceof XMPSchemaIptc4xmpCore){
                XMPSchemaIptc4xmpCore iptcSchema = (XMPSchemaIptc4xmpCore) schema;
                metadataUpdated = addIptcProperies(iptcSchema, image) || metadataUpdated;
                logger.debug("Known schema that will be added:" + schema.toString() +
                        "\n" + schema.getElement().getTextContent());
            } else if (schema instanceof XMPSchemaDublinCore) {
                XMPSchemaDublinCore dcSchema = (XMPSchemaDublinCore) schema;
                metadataUpdated = addDcProperies(dcSchema, image) || metadataUpdated;
                logger.debug("Known schema that will be added:" + schema.toString() +
                        "\n" + schema.getElement().getTextContent());
            } else if (schema instanceof XMPSchemaRightsManagement) {
                XMPSchemaRightsManagement rightsSchema = (XMPSchemaRightsManagement) schema;
                metadataUpdated = addRightsProprties(rightsSchema, image) || metadataUpdated;
                logger.debug("Known schema that will be added:" + schema.toString() +
                        "\n" + schema.getElement().getTextContent());
            } else if(schema instanceof XMPSchemaPhotoshop) {
                XMPSchemaPhotoshop photoshopSchema = (XMPSchemaPhotoshop) schema;
                metadataUpdated = addPhotoshopProperties(photoshopSchema, image) || metadataUpdated;
                logger.debug("Known schema that will be added:" + schema.toString() +
                        "\n" + schema.getElement().getTextContent());
            } else {
                logger.warn("Unknown schema that will be skipped:" + schema.toString() +
                        "\n" + schema.getElement().getTextContent());
            }
        }
//        XMPSchemaBasicJobTicket bjtSchema = xmp.getBasicJobTicketSchema();
//        addBjtSchema(bjtSchema, image);
        

        ////////////////////////////////////////
        IImageMetadata metadata = Sanselan.getMetadata(new File(imageFileName));
        metadataUpdated = addSanselanProperties(metadata, image) || metadataUpdated;
        ////////////////////////////////////////
        
        
        
        if(metadataUpdated) {
            return image;
        } else {
        	logger.debug("No metadata was updated, skipping");
        	return null;
        }
    }

    /**
     * @param dcSchema
     * @param image
     * @return Whether any properties has been updated
     */
    private boolean addDcProperies(XMPSchemaDublinCore dcSchema, Image image) {
        boolean isSomethingDifferent = false;
        if(image.getTitle() == null && StringUtils.isNotBlank(dcSchema.getTitle())) {
            image.setTitle(sanitizer.sanitize(dcSchema.getTitle()));
            isSomethingDifferent = true;
        }
        if(image.getDescription() == null && StringUtils.isNotBlank(dcSchema.getDescription())) {
            image.setDescription(sanitizer.sanitize(dcSchema.getDescription()));
            isSomethingDifferent = true;
        }
        //N.B. Additional subjects are currently added rather than being ignored or overwriting
        if(dcSchema.getSubjects() != null && dcSchema.getSubjects().size() > 0) {
            StringBuffer uncleanSubject = new StringBuffer();
            if(image.getSubject() != null) {
                uncleanSubject.append(image.getSubject());
            }
            for (String subject : dcSchema.getSubjects()) {
                if(StringUtils.isNotBlank(subject) && !uncleanSubject.toString().contains(subject)) {
                    if(uncleanSubject.length() > 0) {
                        uncleanSubject.append(", ");
                    }
                    uncleanSubject.append(subject);
                }
            }
            if(image.getSubject() == null || uncleanSubject.length() > image.getSubject().length()) {
                image.setSubject(sanitizer.sanitize(uncleanSubject.toString()));
                isSomethingDifferent = true;
            }
        }
        List<String> creators = dcSchema.getCreators();
        if(image.getCreator() == null && creators != null && creators.size() > 0) {
            StringBuffer uncleanCreator = new StringBuffer();
            uncleanCreator.append(creators.get(0));
            for (int i = 1; i < creators.size(); i++) {
                uncleanCreator.append(", " + creators.get(i));
            }
            image.setCreator(sanitizer.sanitize(uncleanCreator.toString()));
            isSomethingDifferent = true;
        }
        return isSomethingDifferent;
    }

    /**
     * @param iptcSchema
     * @param image
     * @return Whether any properties has been updated
     */
    private boolean addIptcProperies(XMPSchemaIptc4xmpCore iptcSchema, Image image) {
        boolean isSomethingDifferent = false;
        if(image.getSpatial() == null && StringUtils.isNotBlank(iptcSchema.getLocation())) {
            image.setSpatial(sanitizer.sanitize(iptcSchema.getLocation()));
            isSomethingDifferent = true;
        }
        return isSomethingDifferent;
    }

    /**
     * @param photoshopSchema
     * @param image
     * @return Whether any properties has been updated
     */
    private boolean addPhotoshopProperties(XMPSchemaPhotoshop photoshopSchema,
            Image image) {
        boolean isSomethingDifferent = false;
        if(image.getSpatial() == null) {
            StringBuffer uncleanSpatial = new StringBuffer();
            if(StringUtils.isNotBlank(photoshopSchema.getState())) {
                uncleanSpatial.append(photoshopSchema.getState());
            }
            if(StringUtils.isNotBlank(photoshopSchema.getCountry())) {
                if(uncleanSpatial.length() > 0 ) {
                    uncleanSpatial.append(", ");
                }
                uncleanSpatial.append(photoshopSchema.getCountry());
            }
        }
        if(StringUtils.isNotBlank(photoshopSchema.getInstructions())) {
            //N.B. We could try and use the taxon matcher to associate an additional taxon (or multiple taxa if we are clear about the separator)
            logger.info("I found photoshop/scratchpad instruction: " + photoshopSchema.getInstructions());
        }
        return isSomethingDifferent;
    }

    /**
     * @param rightsSchema
     * @param image
     * @return Whether any properties has been updated
     */
    private boolean addRightsProprties(XMPSchemaRightsManagement rightsSchema, Image image) {
        boolean isSomethingDifferent = false;
        String copyright = sanitizer.sanitize(rightsSchema.getCopyright());
        if(image.getRights() == null && StringUtils.isNotBlank(copyright)) {
            image.setRights(copyright);
            isSomethingDifferent = true;
        }
        List<String> owners = rightsSchema.getOwners();
        if(image.getRightsHolder() == null && owners != null && owners.size() > 0) {
            StringBuffer ownerList = new StringBuffer();
            ownerList.append(owners.get(0));
            for (int i = 1; i < owners.size(); i++) {
                ownerList.append(", " + owners.get(i));
            }
            image.setRightsHolder(sanitizer.sanitize(ownerList.toString()));
            isSomethingDifferent = true;
        }
        
        //The webstatement/license needs to come from several fields
        logger.debug("URL: " + rightsSchema.getWebStatement() + "for Usage terms/License: " + rightsSchema.getUsageTerms());
        if(image.getLicense() == null) {
            StringBuffer license = new StringBuffer();
            if(StringUtils.isNotBlank(rightsSchema.getWebStatement())) {
                //Create an warning annotation if the image has an invalid web statement? http://wiki.creativecommons.org/WebStatement seems strict
                license.append(rightsSchema.getWebStatement());
            }
            if(StringUtils.isNotBlank(rightsSchema.getUsageTerms())) {
                if(license.length() > 0) {
                    license.append("#");
                }
                license.append(rightsSchema.getUsageTerms());
            }
            String cleanedLicense = sanitizer.sanitize(license.toString());
            if(StringUtils.isNotBlank(cleanedLicense)) {
                image.setLicense(cleanedLicense);
                isSomethingDifferent = true;
            }
        }
        return isSomethingDifferent;
    }

    /**
     * @param metadata
     * @param image
     * @return Whether any properties has been updated
     */
    private boolean addSanselanProperties(IImageMetadata metadata, Image image) throws Exception {
        boolean isSomethingDifferent = false;
        if (metadata instanceof JpegImageMetadata) {
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            StringBuffer keywords = null;
            StringBuffer spatial = null;

            for (Object o : jpegMetadata.getItems()) {
                if (o instanceof ImageMetadata.Item) {
                    ImageMetadata.Item item = (ImageMetadata.Item) o;
                    if (item.getKeyword().equals("Object Name") && image.getTitle() == null) {
                        image.setTitle(sanitizer.sanitize(item.getText()));
                        isSomethingDifferent = true;
                    } else if (item.getKeyword().equals("Keywords")) {
                        if (keywords == null) {
                            keywords = new StringBuffer();
                            keywords.append(item.getText());
                        } else {
                            keywords.append(", " + item.getText());
                        }
                    } else if (item.getKeyword().equals("Sublocation")
                            || item.getKeyword().equals("Province/State")
                            || item.getKeyword().equals("Country/Primary Location Name")) {
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
                image.setSpatial(sanitizer.sanitize(spatial.toString()));
                isSomethingDifferent = true;
            }
            if (keywords != null && image.getSubject() == null) {
                image.setSubject(sanitizer.sanitize(keywords.toString()));
                isSomethingDifferent = true;
            }
            if (jpegMetadata.findEXIFValue(TiffConstants.TIFF_TAG_ARTIST) != null
                    && image.getCreator() == null) {
                image.setCreator(sanitizer.sanitize(jpegMetadata.findEXIFValue(
                        TiffConstants.TIFF_TAG_ARTIST).getStringValue()));
                isSomethingDifferent = true;
            }
            if (jpegMetadata.findEXIFValue(TiffConstants.TIFF_TAG_IMAGE_DESCRIPTION) != null
                    && image.getDescription() == null) {
                image.setDescription(sanitizer.sanitize(jpegMetadata.findEXIFValue(
                        TiffConstants.TIFF_TAG_IMAGE_DESCRIPTION)
                        .getStringValue()));
                isSomethingDifferent = true;
            }
            TiffImageMetadata exifMetadata = jpegMetadata.getExif();
            if (exifMetadata != null) {
                TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
                if (gpsInfo != null && image.getLocation() == null) {
//TODO Ask for rational of setting like this.  N.B.Only the Longitude was being set
                    image.setLongitude(gpsInfo.getLongitudeAsDegreesEast());
                    image.setLatitude(gpsInfo.getLatitudeAsDegreesNorth());
                    isSomethingDifferent = true;
                }
            }
        }//TODO Other image types I'd think
        return isSomethingDifferent ;
    }

    
}
