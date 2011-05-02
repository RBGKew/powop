package org.emonocot.job.scratchpads.convert;

import org.emonocot.job.scratchpads.model.EoLDataObject;
import org.joda.time.DateTime;
import org.emonocot.model.common.License;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.service.DescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class EoLTextDataObjectConverter implements
        Converter<EoLDataObject, TextContent> {

    /**
     *
     */
    private DescriptionService descriptionService;

    /**
     *
     */
    private Converter<String, DateTime> dateTimeConverter;

    /**
     *
     */
    private Converter<String, License> licenseConverter;

    /**
     *
     */
    private Converter<String, Feature> featureConverter;

    /**
     *
     * @param newDescriptionService Set the description service to use.
     */
    @Autowired
    public final void setDescriptionService(
            final DescriptionService newDescriptionService) {
        this.descriptionService = newDescriptionService;
    }

    /**
     *
     * @param dateTimeConverter Set the date time converter to use.
     */
    @Autowired
    public final void setDateTimeConverter(
            final Converter<String, DateTime> dateTimeConverter) {
        this.dateTimeConverter = dateTimeConverter;
    }

    /**
     *
     * @param licenseConverter Set the license converter to use.
     */
    @Autowired
    public final void setLicenseConverter(
            final Converter<String, License> licenseConverter) {
        this.licenseConverter = licenseConverter;
    }

    /**
     *
     * @param featureConverter Set the feature converter to use.
     */
    @Autowired
    public final void setFeatureConverter(
            final Converter<String, Feature> featureConverter) {
        this.featureConverter = featureConverter;
    }

    /**
     * @param dataObject The eol data object to convert into text content.
     * @return A text content object.
     */
    public final TextContent convert(final EoLDataObject dataObject) {
        TextContent textContent = new TextContent();
        textContent.setCreated(dateTimeConverter.convert(dataObject
                .getCreated()));
        textContent.setModified(dateTimeConverter.convert(dataObject
                .getModified()));
        textContent
                .setLicense(licenseConverter.convert(dataObject.getLicense()));
        textContent.setSource(dataObject.getSource());
        textContent.setTaxon(dataObject.getTaxon());
        textContent.setContent(dataObject.getDescription());
        textContent
                .setFeature(featureConverter.convert(dataObject.getSubject()));
        if (dataObject.getAgent() != null
                && dataObject.getAgent().getRole().equals("author")) {
          textContent.setCreator(dataObject.getAgent().getURI());
        }

        if (dataObject.getTaxon().getId() != null) {
            TextContent persistedTextContent = descriptionService
                    .getTextContent(textContent.getFeature(),
                            textContent.getTaxon());

            if (persistedTextContent != null
                    && persistedTextContent.equals(textContent)) {
                return persistedTextContent;
            }
        }
        return textContent;
    }
}
