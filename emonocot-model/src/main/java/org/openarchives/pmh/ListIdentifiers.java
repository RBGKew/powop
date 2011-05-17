package org.openarchives.pmh;

import java.util.ArrayList;
import java.util.List;

import org.openarchives.pmh.marshall.ResumptionTokenConverter;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * <p>
 * Java class for ListIdentifiersType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="ListIdentifiersType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header"
 *             type="{http://www.openarchives.org/OAI/2.0/}headerType"
 *             maxOccurs="unbounded"/>
 *         &lt;element name="resumptionToken"
 *             type="{http://www.openarchives.org/OAI/2.0/}resumptionTokenType"
 *             minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
public class ListIdentifiers {

    /**
     *
     */
    @XStreamImplicit(itemFieldName = "header")
    private List<Header> header;

    /**
     *
     */
    @XStreamConverter(ResumptionTokenConverter.class)
    private ResumptionToken resumptionToken;

    /**
     * Gets the value of the header property.
     *
     * @return a list of headers
     */
    public final List<Header> getHeader() {
        if (header == null) {
            header = new ArrayList<Header>();
        }
        return this.header;
    }

    /**
     * Gets the value of the resumptionToken property.
     *
     * @return the resumption token
     */
    public final ResumptionToken getResumptionToken() {
        return resumptionToken;
    }

    /**
     * Sets the value of the resumptionToken property.
     *
     * @param value the new resumption token
     */
    public final void setResumptionToken(final ResumptionToken value) {
        this.resumptionToken = value;
    }
}
