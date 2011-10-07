package org.emonocot.job.scratchpads.convert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.emonocot.job.scratchpads.model.EoLAgent;
import org.emonocot.job.scratchpads.model.EoLDataObject;
import org.emonocot.model.common.License;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.api.DescriptionService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author ben
 *
 */
public class EoLTextDataConverterTest {

    /**
     *
     */
    private final DateTime dateTime = new DateTime(2011, 3, 24, 12, 49, 0,
            0, DateTimeZone.forID("GMT"));

   /**
    *
    */
    private final DateTime moreRecentDateTime =
        new DateTime(2011, 4, 13, 16,  39, 0, 0, DateTimeZone.forID("GMT"));
    /**
     *
     */
    private EoLTextDataObjectConverter converter
            = new EoLTextDataObjectConverter();
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
     */
    private EoLDataObject dataObject = new EoLDataObject();
    /**
     *
     */
    private Taxon taxon = new Taxon();
    /**
     *
     */
    private EoLAgent eolAgent = new EoLAgent();
    /**
     *
     */
    private TextContent persistedTextContent = new TextContent();

    /**
     *
     */
    @Before
    public final void setUp() {
        dateTimeConverter = EasyMock.createMock(Converter.class);
        licenseConverter = EasyMock.createMock(Converter.class);
        featureConverter = EasyMock.createMock(Converter.class);
        descriptionService = EasyMock.createMock(DescriptionService.class);
        converter.setDateTimeConverter(dateTimeConverter);
        converter.setLicenseConverter(licenseConverter);
        converter.setFeatureConverter(featureConverter);
        converter.setDescriptionService(descriptionService);
        dataObject.setCreated("1300970978");
        dataObject.setModified("1300970994");
        dataObject.setLicense("http://creativecommons.org/licenses/by-nc/3.0/");
        dataObject.setSubject("http://rs.tdwg.org/ontology/voc/"
                + "SPMInfoItems#GeneralDescription");
        dataObject.setSource("http://scratchpad.cate-araceae.org/"
                + "content/anthurium-schott");
        dataObject.setDescription("&lt;p&gt;HABIT :   evergreen   herbs, stem"
                + " erect, creeping, or short- to long-climbing, rarely"
                + " rhizomatous,   internodes very short (plant &#13;");
        eolAgent.setRole("author");
        eolAgent.setURI("http://scratchpad.cate-araceae.org/users/ben");
        dataObject.setTaxon(taxon);
        dataObject.setAgent(eolAgent);
        persistedTextContent.setCreated(dateTime);
        persistedTextContent.setModified(dateTime);
        persistedTextContent.setLicense(License.ATTRIBUTION_NONCOMMERCIAL);
        persistedTextContent
                .setSource("http://scratchpad.cate-araceae.org/"
                        + "content/anthurium-schott");
        persistedTextContent.setTaxon(taxon);
        persistedTextContent.setFeature(Feature.general);
        persistedTextContent
                .setContent("&lt;p&gt;HABIT :   evergreen   herbs, stem"
                        + " erect, creeping, or short- to long-climbing,"
                        + " rarely rhizomatous,   internodes very short"
                        + " (plant &#13;");
        persistedTextContent
                .setCreator("http://scratchpad.cate-araceae.org/users/ben");
    }

    /**
     * Test that demonstrates the use of this converter where the taxon is new
     * (taxon.getId() == null)
     *
     * A new TextData object is created and populated.
     */
    @Test
    public final void testConvertWithNewTaxon() {
        EasyMock.expect(dateTimeConverter.convert(EasyMock.eq("1300970978")))
                .andReturn(dateTime);
        EasyMock.expect(dateTimeConverter.convert(EasyMock.eq("1300970994")))
                .andReturn(dateTime);
        EasyMock.expect(
                licenseConverter.convert(EasyMock
                        .eq("http://creativecommons.org/licenses/by-nc/3.0/")))
                .andReturn(License.ATTRIBUTION_NONCOMMERCIAL);
        EasyMock.expect(featureConverter.convert(
                EasyMock.eq("http://rs.tdwg.org/ontology/voc/"
                                + "SPMInfoItems#GeneralDescription")))
                .andReturn(Feature.general);
        EasyMock.replay(dateTimeConverter, licenseConverter, featureConverter,
                descriptionService);

        TextContent textContent = converter.convert(dataObject);

        EasyMock.verify(dateTimeConverter, licenseConverter, featureConverter,
                descriptionService);
        assertNotNull("Object returned must not be null", textContent);
        assertTrue("Object returned must be an instance of TextContent",
                textContent instanceof TextContent);
        assertEquals("Created Date should be set properly",
                textContent.getCreated(), dateTime);
        assertEquals("Modified Date should be set properly",
                textContent.getModified(), dateTime);
        assertEquals("License should be set properly",
                textContent.getLicense(), License.ATTRIBUTION_NONCOMMERCIAL);
        assertEquals("Feature should be set properly",
                textContent.getFeature(), Feature.general);
        assertEquals("Taxon should be set properly", textContent.getTaxon(),
                taxon);
        assertEquals("Source should be set properly", textContent.getSource(),
                "http://scratchpad.cate-araceae.org/content/anthurium-schott");
        assertEquals(
                "Content should be set properly",
                textContent.getContent(),
                "&lt;p&gt;HABIT :   evergreen   herbs, stem erect, creeping, or"
                + " short- to long-climbing, rarely rhizomatous,   internodes "
                + "very short (plant &#13;");
        assertEquals("Creator should be set properly",
                textContent.getCreator(),
                "http://scratchpad.cate-araceae.org/users/ben");
    }

    /**
     * Test that demonstrates the use of this converter where the taxon is not
     * new (taxon.getId() != null) but the text data is new
     * (descriptionService.getTextContent(Feature feature, Taxon taxon) == null)
     *
     * A new TextData object is created and populated.
     */
    @Test
    public final void testConvertWithPersistentTaxonNewContent() {
        taxon.setId(1L);
        EasyMock.expect(dateTimeConverter.convert(EasyMock.eq("1300970978")))
                .andReturn(dateTime);
        EasyMock.expect(dateTimeConverter.convert(EasyMock.eq("1300970994")))
                .andReturn(dateTime);
        EasyMock.expect(
                licenseConverter.convert(EasyMock
                        .eq("http://creativecommons.org/licenses/by-nc/3.0/")))
                .andReturn(License.ATTRIBUTION_NONCOMMERCIAL);
        EasyMock.expect(
                featureConverter.convert(EasyMock
                        .eq("http://rs.tdwg.org/ontology/voc/"
                                + "SPMInfoItems#GeneralDescription")))
                .andReturn(Feature.general);
        EasyMock.expect(
                descriptionService.getTextContent(
                        EasyMock.eq(Feature.general),
                        EasyMock.eq(taxon))).andReturn(null);
        EasyMock.replay(dateTimeConverter, licenseConverter, featureConverter,
                descriptionService);

        TextContent textContent = converter.convert(dataObject);

        EasyMock.verify(dateTimeConverter, licenseConverter, featureConverter,
                descriptionService);
        assertNotNull("Object returned must not be null", textContent);
        assertTrue("Object returned must be an instance of TextContent",
                textContent instanceof TextContent);
        assertEquals("Created Date should be set properly",
                textContent.getCreated(), dateTime);
        assertEquals("Modified Date should be set properly",
                textContent.getModified(), dateTime);
        assertEquals("License should be set properly",
                textContent.getLicense(), License.ATTRIBUTION_NONCOMMERCIAL);
        assertEquals("Feature should be set properly",
                textContent.getFeature(), Feature.general);
        assertEquals("Taxon should be set properly", textContent.getTaxon(),
                taxon);
        assertEquals("Source should be set properly", textContent.getSource(),
                "http://scratchpad.cate-araceae.org/content/anthurium-schott");
        assertEquals(
                "Content should be set properly",
                textContent.getContent(),
                "&lt;p&gt;HABIT :   evergreen   herbs, stem erect, creeping,"
                + " or short- to long-climbing, rarely rhizomatous,   "
                + "internodes very short (plant &#13;");
        assertEquals("Creator should be set properly",
                textContent.getCreator(),
                "http://scratchpad.cate-araceae.org/users/ben");
    }

    /**
     * Test that demonstrates the use of this converter where the taxon is not
     * new (taxon.getId() != null) and the text data is old
     * (descriptionService.getTextContent(Feature feature, Taxon taxon) returns
     * an object which equals the new object)
     *
     * The persistent object is returned and the new one is not persisted.
     */
    @Test
    public final void testConvertWithPersistentTaxonOldContent() {
        taxon.setId(1L);

        EasyMock.expect(dateTimeConverter.convert(EasyMock.eq("1300970978")))
                .andReturn(dateTime);
        EasyMock.expect(dateTimeConverter.convert(EasyMock.eq("1300970994")))
                .andReturn(dateTime);
        EasyMock.expect(
                licenseConverter.convert(EasyMock
                        .eq("http://creativecommons.org/licenses/by-nc/3.0/")))
                .andReturn(License.ATTRIBUTION_NONCOMMERCIAL);
        EasyMock.expect(
                featureConverter.convert(EasyMock
                        .eq("http://rs.tdwg.org/ontology/voc/"
                                + "SPMInfoItems#GeneralDescription")))
                .andReturn(Feature.general);
        EasyMock.expect(
                descriptionService.getTextContent(
                        EasyMock.eq(Feature.general),
                        EasyMock.eq(taxon))).andReturn(persistedTextContent);
        EasyMock.replay(dateTimeConverter, licenseConverter, featureConverter,
                descriptionService);

        TextContent textContent = converter.convert(dataObject);

        EasyMock.verify(dateTimeConverter, licenseConverter, featureConverter,
                descriptionService);
        assertEquals("Object returned must be the persisted text content",
                textContent, persistedTextContent);
    }

    /**
     * Test that demonstrates the use of this converter where the taxon is not
     * new (taxon.getId() != null) and the text data is newer
     * (descriptionService.getTextContent(Feature feature, Taxon taxon) returns
     * an object which does not the new object)
     *
     * The new object is returned and is the old object is overwritten once the
     * data is persisted.
     */
    @Test
    public final void testConvertWithPersistentTaxonNewerContent() {
        taxon.setId(1L);
        dataObject.setModified("1302702173");

        EasyMock.expect(dateTimeConverter.convert(EasyMock.eq("1300970978")))
                .andReturn(dateTime);
        EasyMock.expect(dateTimeConverter.convert(EasyMock.eq("1302702173")))
                .andReturn(moreRecentDateTime);
        EasyMock.expect(
                licenseConverter.convert(EasyMock
                        .eq("http://creativecommons.org/licenses/by-nc/3.0/")))
                .andReturn(License.ATTRIBUTION_NONCOMMERCIAL);
        EasyMock.expect(
                featureConverter.convert(EasyMock
                        .eq("http://rs.tdwg.org/ontology/voc/"
                                + "SPMInfoItems#GeneralDescription")))
                .andReturn(Feature.general);
        EasyMock.expect(
                descriptionService.getTextContent(
                        EasyMock.eq(Feature.general),
                        EasyMock.eq(taxon))).andReturn(persistedTextContent);
        EasyMock.replay(dateTimeConverter, licenseConverter, featureConverter,
                descriptionService);

        TextContent textContent = converter.convert(dataObject);

        EasyMock.verify(dateTimeConverter, licenseConverter, featureConverter,
                descriptionService);
        assertTrue("Object returned must be an instance of TextContent",
                textContent instanceof TextContent);
        assertEquals("Created Date should be set properly",
                textContent.getCreated(), dateTime);
        assertEquals("Modified Date should be set properly",
                textContent.getModified(), moreRecentDateTime);
        assertEquals("License should be set properly",
                textContent.getLicense(), License.ATTRIBUTION_NONCOMMERCIAL);
        assertEquals("Feature should be set properly",
                textContent.getFeature(), Feature.general);
        assertEquals("Taxon should be set properly", textContent.getTaxon(),
                taxon);
        assertEquals("Source should be set properly", textContent.getSource(),
                "http://scratchpad.cate-araceae.org/content/anthurium-schott");
        assertEquals(
                "Content should be set properly",
                textContent.getContent(),
                "&lt;p&gt;HABIT :   evergreen   herbs, stem erect, creeping,"
                + " or short- to long-climbing, rarely rhizomatous,   "
                + "internodes very short (plant &#13;");
        assertEquals("Creator should be set properly",
                textContent.getCreator(),
                "http://scratchpad.cate-araceae.org/users/ben");
        assertFalse("Object returned must not be the persisted text content",
                textContent == persistedTextContent);
    }
}
