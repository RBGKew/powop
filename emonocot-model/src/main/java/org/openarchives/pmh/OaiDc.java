package org.openarchives.pmh;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.emonocot.model.marshall.DateTimeConverter;
import org.joda.time.DateTime;

import com.thoughtworks.xstream.annotations.XStreamConverter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "oai_dcType", namespace = "http://www.openarchives.org/OAI/2.0/oai_dc/", propOrder = {
    "title",
    "creator",
    "subject",
    "publisher",
    "date",
    "format",
    "identifier",
    "source",
    "language",
    "coverage",
    "rights"
})
@XmlRootElement(name = "dc", namespace = "http://www.openarchives.org/OAI/2.0/oai_dc/")
public class OaiDc {
	@XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
    private String title;
	
	@XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
    private String creator;
	
	@XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
    private String subject;
	
	@XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
    private String publisher;
	
    @XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
    @XStreamConverter(DateTimeConverter.class)
    private DateTime date;
    
    @XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
    private String format;
    
    @XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
    private URI identifier;
    
    @XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
    private String source;
    
    @XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
    private String language;
    
    @XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
    private String coverage;
    
    @XmlElement(namespace = "http://purl.org/dc/elements/1.1/")
    private String rights;
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public URI getIdentifier() {
		return identifier;
	}

	public void setIdentifier(URI identifier) {
		this.identifier = identifier;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}
}
