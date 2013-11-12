package org.emonocot.harvest.media;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.jempbox.xmp.XMPMetadata;
import org.apache.jempbox.xmp.XMPSchema;
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
import org.emonocot.job.dwc.exception.InvalidValuesException;
import org.emonocot.model.Image;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.MediaFormat;
import org.emonocot.model.constants.RecordType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.InputSource;

/**
 *
 * @author ben
 *
 */
public class ImageMetadataExtractorImpl implements ItemProcessor<Image, Image>, ImageMetadataExtractor {

    private Logger logger = LoggerFactory.getLogger(ImageMetadataExtractorImpl.class);

    private HtmlSanitizer sanitizer;
    
    private String imageDirectory;
    
    private ImageAnnotator imageAnnotator;
    
    private Validator validator;
    
    private List<DateTimeFormatter> dateTimeFormatters = new ArrayList<DateTimeFormatter>();
    
    public ImageMetadataExtractorImpl() {
   	
    	dateTimeFormatters.add(ISODateTimeFormat.dateTimeParser());
    	dateTimeFormatters.add(DateTimeFormat.fullDate());
    	dateTimeFormatters.add(DateTimeFormat.fullDateTime());
    	dateTimeFormatters.add(DateTimeFormat.shortDate());
    	dateTimeFormatters.add(DateTimeFormat.shortDateTime());
    	dateTimeFormatters.add(DateTimeFormat.mediumDate());
    	dateTimeFormatters.add(DateTimeFormat.mediumDateTime());

    }

    
    /**
     * An ordered array of metadata schemas to use in adding metadata to the image 
     */
    private Class[] schemas = {XMPSchemaIptc4xmpCore.class, XMPSchemaRightsManagement.class,
            XMPSchemaDublinCore.class, XMPSchemaPhotoshop.class};
    
    /**
     * @param sanitizer the sanitizer to set
     */
    @Autowired
    public void setSanitizer(HtmlSanitizer sanitizer) {
        this.sanitizer = sanitizer;
    }
    
    @Autowired
    public void setValidator(Validator validator) {
    	this.validator = validator;
    }

    /**
     *
     * @param newImageDirectory
     *            Set the image directory
     */
    public void setImageDirectory(String newImageDirectory) {
        this.imageDirectory = newImageDirectory;
    }
    
    /**
     * @param imageAnnotator the imageAnnotator to set
     */
    public void setImageAnnotator(ImageAnnotator imageAnnotator) {
        this.imageAnnotator = imageAnnotator;
    }

    /**
     * @param schemas the schemas to set
     */
    public void setSchemas(Class[] schemas) {
        this.schemas = schemas;
    }

    /* (non-Javadoc)
	 * @see org.emonocot.harvest.media.ImageMetadataExtractor#process(org.emonocot.model.Image)
	 */
    @Override
	public Image process(Image image) throws Exception {
        String imageFileName = imageDirectory + File.separatorChar + image.getId() + '.' + image.getFormat();
        File file = new File(imageFileName);
        logger.debug("Image File " + imageFileName);
        if (!file.exists()) {
            logger.error("File does not exist in image directory, skipping");
            //TODO error annotation
            return null;
        }
        boolean metadataFound = false;
        //Search for additional metadata
        Image embeddedMetadata = new Image();
        String xmpXml = Sanselan.getXmpXml(file);
		if (xmpXml != null && !xmpXml.isEmpty()) {
			logger.debug("Attempting to extract metadata from xmp-xml:\n" + xmpXml);
			try {
				XMPMetadata xmp = XMPMetadata.load(new InputSource(new StringReader(xmpXml)));
				for (Class schemaClass : schemas) {
					XMPSchema schema = xmp.getSchemaByClass(schemaClass);
					if (schema instanceof XMPSchemaIptc4xmpCore) {
						XMPSchemaIptc4xmpCore iptcSchema = (XMPSchemaIptc4xmpCore) schema;
						metadataFound = addIptcProperies(iptcSchema,embeddedMetadata) || metadataFound;
						logger.debug("Known schema that will be added:"	+ schema.toString() + "\n"
								+ schema.getElement().getTextContent());
					} else if (schema instanceof XMPSchemaDublinCore) {
						XMPSchemaDublinCore dcSchema = (XMPSchemaDublinCore) schema;
						metadataFound = addDcProperies(dcSchema, embeddedMetadata) || metadataFound;
						logger.debug("Known schema that will be added:"	+ schema.toString() + "\n"
								+ schema.getElement().getTextContent());
					} else if (schema instanceof XMPSchemaRightsManagement) {
						XMPSchemaRightsManagement rightsSchema = (XMPSchemaRightsManagement) schema;
						metadataFound = addRightsProprties(rightsSchema,embeddedMetadata) || metadataFound;
						logger.debug("Known schema that will be added:"	+ schema.toString() + "\n"
								+ schema.getElement().getTextContent());
					} else if (schema instanceof XMPSchemaPhotoshop) {
						XMPSchemaPhotoshop photoshopSchema = (XMPSchemaPhotoshop) schema;
						metadataFound = addPhotoshopProperties(photoshopSchema,	embeddedMetadata, image) || metadataFound;
						logger.debug("Known schema that will be added:"	+ schema.toString() + "\n"
								+ schema.getElement().getTextContent());
					} else {
						logger.info("Unable to process a schema of: " + schemaClass);
					}
				}
			} catch (IOException ioe) {
				logger.error("Exception parsing XMP XML " + xmpXml, ioe);
			}
		} else {
			logger.debug("Image " + file + " does not contain embedded XMP metadata");
		}
        
        IImageMetadata metadata = Sanselan.getMetadata(new File(imageFileName));
        if(metadata != null) {
             logger.debug("The metadata visible to Sanselan is: " +  metadata.toString("*"));
             metadataFound = addSanselanProperties(metadata, embeddedMetadata) || metadataFound;
        } else {
        	logger.debug("There is no metadata available from Sanselan");
        }
        //Apply any supplementary metadata
        if(metadataFound && update(image, embeddedMetadata)) {
            validate(image);
            return image;
        } else {
        	logger.debug("No metadata was updated, skipping");
        	return null;
        }
    }
    
    /**
     * @param image The persisted image to update
     * @param embeddedMetadata The image containing supplementary values
     * @return Whether any metadata was updated on the persisted image
     */
    private boolean update(Image image, Image embeddedMetadata) {
        boolean updated = false;
        if(image.getTitle() == null && embeddedMetadata.getTitle() != null) {
            image.setTitle(embeddedMetadata.getTitle());
            updated = true;
        }
        if(image.getDescription() == null && embeddedMetadata.getDescription() != null) {
            image.setDescription(embeddedMetadata.getDescription());
            updated = true;
        }
        if(embeddedMetadata.getSubject() != null) {
            if(image.getSubject() == null) {
                image.setSubject(embeddedMetadata.getSubject());
                updated = true;
            } else {
                StringBuffer newSubject = new StringBuffer();
                newSubject.append(image.getSubject());
                for(String subject : embeddedMetadata.getSubject().split(",")) {
                    if(!newSubject.toString().contains(subject.trim())) {
                        newSubject.append(", " + subject.trim());
                    }
                }
                image.setSubject(newSubject.toString());
                updated = true; //Not strictly always true
            }
        }
        if(image.getCreator() == null && embeddedMetadata.getCreator() != null) {
            image.setCreator(embeddedMetadata.getCreator());
            updated = true;
        }
        if(image.getFormat() == null && embeddedMetadata.getFormat() != null) {
            image.setFormat(embeddedMetadata.getFormat());
            updated = true;
        }
        if(image.getSpatial() == null && embeddedMetadata.getSpatial() != null) {
            image.setSpatial(embeddedMetadata.getSpatial());
            updated = true;
        }
        if(image.getCreated() == null && embeddedMetadata.getCreated() != null) {
            image.setCreated(embeddedMetadata.getCreated());
            updated = true;
        }
        if(image.getRights() == null && embeddedMetadata.getRights() != null) {
            image.setRights(embeddedMetadata.getRights());
            updated = true;
        }
        if(image.getRightsHolder() == null && embeddedMetadata.getRightsHolder() != null) {
            image.setRightsHolder(embeddedMetadata.getRightsHolder());
            updated = true;
        }
        if(image.getLicense() == null && embeddedMetadata.getLicense() != null) {
            image.setLicense(embeddedMetadata.getLicense());
            updated = true;
        }
        if(image.getLocation() == null && embeddedMetadata.getLocation() != null) {
            image.setLocation(embeddedMetadata.getLocation());
            updated = true;
        }
        return updated;
    }

    protected void validate(Image image) {
    	Set<ConstraintViolation<Image>> violations = validator.validate(image);
    	if(!violations.isEmpty()) {
    		 RecordType recordType = RecordType.Image;
    		 StringBuffer stringBuffer = new StringBuffer();
    		 stringBuffer.append(violations.size()).append(" constraint violations:");
    		for(ConstraintViolation<Image> violation : violations) {			
    			stringBuffer.append(violation.getPropertyPath() +  " " + violation.getMessage());
    		}
    		throw new InvalidValuesException(stringBuffer.toString(), recordType,  -1);    		
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
        List<String> subjects = dcSchema.getSubjects();
        if(subjects != null && subjects.size() > 0) {
            StringBuffer uncleanSubject = new StringBuffer();
            int startAt = 0;
            if(image.getSubject() != null) {
                uncleanSubject.append(image.getSubject());
            } else {
                uncleanSubject.append(sanitizer.sanitize(subjects.get(startAt++)));
            }
            for (int i = startAt; i < subjects.size(); i++) {
                String subject = sanitizer.sanitize(subjects.get(i)); //We need to check the sanitized string
                if(StringUtils.isNotBlank(subject) && !uncleanSubject.toString().contains(subject)) {
                    uncleanSubject.append(", " + subject);
                }
            }
            if(image.getSubject() == null || uncleanSubject.length() > image.getSubject().length()) {
                image.setSubject(uncleanSubject.toString()); //Sanitized earlier
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
        if(image.getFormat() == null && StringUtils.isNotBlank(dcSchema.getFormat())) {
            String format = dcSchema.getFormat();
            if(format.contains("gif")) {
                image.setFormat(MediaFormat.gif);
                isSomethingDifferent = true;
            } else if(format.contains("jpeg")) {
                image.setFormat(MediaFormat.jpg);
                isSomethingDifferent = true;
            } else if(format.contains("png")) {
                image.setFormat(MediaFormat.png);
                isSomethingDifferent = true;
            }
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
     * @param embeddedMetadata
     * @return Whether any properties has been updated
     */
    private boolean addPhotoshopProperties(XMPSchemaPhotoshop photoshopSchema, Image embeddedMetadata, Image image) {
        boolean isSomethingDifferent = false;
        StringBuffer newSpatial = new StringBuffer();
        if(StringUtils.isNotBlank(embeddedMetadata.getSpatial())) {
            newSpatial.append(embeddedMetadata.getSpatial());
        }
        if(StringUtils.isNotBlank(photoshopSchema.getState())) {
            if(newSpatial.length() > 0 ) {
                newSpatial.append(", ");
            }
            newSpatial.append(sanitizer.sanitize(photoshopSchema.getState()));
        }
        if(StringUtils.isNotBlank(photoshopSchema.getCountry())) {
            if(newSpatial.length() > 0 ) {
                newSpatial.append(", ");
            }
            newSpatial.append(sanitizer.sanitize(photoshopSchema.getCountry()));
        }
        if(!newSpatial.toString().equals(embeddedMetadata.getSpatial())) {
            embeddedMetadata.setSpatial(newSpatial.toString());
            isSomethingDifferent = true;
        }
        if(StringUtils.isNotBlank(photoshopSchema.getInstructions())) {
            //N.B. We could try and use the taxon matcher to associate an additional taxon (or multiple taxa if we are clear about the separator)
            logger.info("Photoshop instruction found: " + photoshopSchema.getInstructions());
            //TODO Match Taxon?
        }
        if(embeddedMetadata.getCreated() == null && photoshopSchema.getDateCreated() != null) {
        	IllegalArgumentException iae = null;
        	DateTime dateCreated = null;
			for (DateTimeFormatter dateTimeFormatter : dateTimeFormatters) {
				try {
					dateCreated = dateTimeFormatter.parseDateTime(photoshopSchema.getDateCreated());
					
				} catch (IllegalArgumentException e) {
					iae = e;
				}
			}
			if(dateCreated == null) {
			    imageAnnotator.annotate(image, AnnotationType.Warn,	AnnotationCode.BadField, photoshopSchema.getDateCreated() + " is not a well-formed date");
			    logger.warn("Unable to set the Date Created for image"	+ embeddedMetadata.getId() + " identifier: " + embeddedMetadata.getIdentifier(), iae);
			} else {
				embeddedMetadata.setCreated(dateCreated);
			}
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
        logger.debug("URL: " + rightsSchema.getWebStatement() + "for Usage terms/License: "
                + rightsSchema.getUsageTerms());
        if(image.getLicense() == null) {
            StringBuffer uncleanLicense = new StringBuffer();
            URI licenseURI = null;
            try {
                licenseURI = new URI(rightsSchema.getUsageTerms());
            } catch (NullPointerException e) {
                logger.debug(rightsSchema.getUsageTerms() + " is not a valid URI");
            } catch (URISyntaxException e) {
                logger.debug(rightsSchema.getUsageTerms() + " is not a valid URI");
            }
            if(licenseURI != null) {
                uncleanLicense.append(rightsSchema.getUsageTerms());
            } else {
                if(StringUtils.isNotBlank(rightsSchema.getWebStatement())) {
                    uncleanLicense.append(rightsSchema.getWebStatement());
                }
                if(StringUtils.isNotBlank(rightsSchema.getUsageTerms())) {
                    if(uncleanLicense.length() > 0) {
                        uncleanLicense.append("#");
                    }
                    uncleanLicense.append(rightsSchema.getUsageTerms());
                }
            }
            String license = sanitizer.sanitize(uncleanLicense.toString());
            if(StringUtils.isNotBlank(license)) {
                image.setLicense(license);
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
            if (jpegMetadata.findEXIFValue(TiffConstants.TIFF_TAG_COPYRIGHT) != null
                    && image.getRights() == null) {
            	
                image.setRights(sanitizer.sanitize(jpegMetadata.findEXIFValue(
                        TiffConstants.TIFF_TAG_COPYRIGHT).getStringValue()));
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
                    image.setLongitude(gpsInfo.getLongitudeAsDegreesEast());
                    image.setLatitude(gpsInfo.getLatitudeAsDegreesNorth());
                    isSomethingDifferent = true;
                }
            }
        }
        return isSomethingDifferent ;
    }

	public void afterPropertiesSet() throws Exception {
        assert imageDirectory != null;
        if (sanitizer == null) {
            sanitizer = new HtmlSanitizer();
            sanitizer.afterPropertiesSet();
        }
    }
}
