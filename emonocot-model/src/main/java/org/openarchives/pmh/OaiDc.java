package org.openarchives.pmh;

import java.net.URI;

import org.emonocot.model.marshall.DateTimeConverter;
import org.emonocot.model.marshall.UriElementConverter;
import org.joda.time.DateTime;

import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 *
 * @author ben
 *
 */
public class OaiDc {

    /**
     * xmlns:dc="http://purl.org/dc/elements/1.1/".
     */
    private String dcTitle;


    /**
     * xmlns:dc="http://purl.org/dc/elements/1.1/".
     */
    private String dcCreator;

    /**
     * xmlns:dc="http://purl.org/dc/elements/1.1/".
     */
    private String dcSubject;

    /**
     * xmlns:dc="http://purl.org/dc/elements/1.1/".
     */
    private String dcPublisher;


    /**
     * xmlns:dc="http://purl.org/dc/elements/1.1/".
     */
    @XStreamConverter(DateTimeConverter.class)
    private DateTime dcDate;

    /**
     * xmlns:dc="http://purl.org/dc/elements/1.1/".
     */
    private String dcFormat;


    /**
     * xmlns:dc="http://purl.org/dc/elements/1.1/".
     */
    @XStreamConverter(UriElementConverter.class)
    private URI dcIdentifier;


    /**
     * xmlns:dc="http://purl.org/dc/elements/1.1/".
     */
    private String dcSource;

    /**
     * xmlns:dc="http://purl.org/dc/elements/1.1/".
     */
    private String dcLanguage;

    /**
     * xmlns:dc="http://purl.org/dc/elements/1.1/".
     */
    private String dcCoverage;

    /**
     * xmlns:dc="http://purl.org/dc/elements/1.1/".
     */
    private String dcRights;

    /**
     *
     * @return the dc:title of the object
     */
    public final String getDcTitle() {
        return dcTitle;
    }

    /**
     *
     * @param title Set the dc:title
     */
    public final void setDcTitle(final String title) {
        this.dcTitle = title;
    }

    /**
     *
     * @return the dc:creator
     */
    public final String getDcCreator() {
        return dcCreator;
    }

    /**
     *
     * @param creator Set the dc:creator
     */
    public final void setDcCreator(final String creator) {
        this.dcCreator = creator;
    }

    /**
     *
     * @return the dc:subject
     */
    public final String getDcSubject() {
        return dcSubject;
    }

    /**
     *
     * @param subject Set the dc:subject
     */
    public final void setDcSubject(final String subject) {
        this.dcSubject = subject;
    }

    /**
     *
     * @return the dc:publisher
     */
    public final String getDcPublisher() {
        return dcPublisher;
    }

    /**
     *
     * @param publisher Set the dc:publisher
     */
    public final void setDcPublisher(final String publisher) {
        this.dcPublisher = publisher;
    }

    /**
     *
     * @return the dc:date
     */
    public final DateTime getDcDate() {
        return dcDate;
    }

    /**
     *
     * @param date Set the dc:date
     */
    public final void setDcDate(final DateTime date) {
        this.dcDate = date;
    }

    /**
     *
     * @return the dc:format
     */
    public final String getDcFormat() {
        return dcFormat;
    }

    /**
     *
     * @param format Set the dc:format
     */
    public final void setDcFormat(final String format) {
        this.dcFormat = format;
    }

    /**
     *
     * @return the dc:identifier
     */
    public final URI getDcIdentifier() {
        return dcIdentifier;
    }

    /**
     *
     * @param identifier Set the dc:identifier
     */
    public final void setDcIdentifier(final URI identifier) {
        this.dcIdentifier = identifier;
    }

    /**
     *
     * @return the dc:source
     */
    public final String getDcSource() {
        return dcSource;
    }

    /**
     *
     * @param source set the dc:source
     */
    public final void setDcSource(final String source) {
        this.dcSource = source;
    }

    /**
     *
     * @return the dc:language
     */
    public final String getDcLanguage() {
        return dcLanguage;
    }

    /**
     *
     * @param language Set the dc:language
     */
    public final void setDcLanguage(final String language) {
        this.dcLanguage = language;
    }

    /**
     *
     * @return the dc:coverage
     */
    public final String getDcCoverage() {
        return dcCoverage;
    }

    /**
     *
     * @param coverage Set the dc:coverage
     */
    public final void setDcCoverage(final String coverage) {
        this.dcCoverage = coverage;
    }

    /**
     *
     * @return the dc:rights
     */
    public final String getDcRights() {
        return dcRights;
    }

    /**
     *
     * @param rights Set the dc:rights
     */
    public final void setDcRights(final String rights) {
        this.dcRights = rights;
    }
}
