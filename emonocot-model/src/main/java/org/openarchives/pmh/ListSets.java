package org.openarchives.pmh;

import java.util.ArrayList;
import java.util.List;
/**
 * <p>
 * Java class for ListSetsType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="ListSetsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="set"
 *             type="{http://www.openarchives.org/OAI/2.0/}setType"
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

public class ListSets {

    /**
     *
     */
    private List<Set> set;

    /**
     *
     */
    private ResumptionToken resumptionToken;

    /**
     * Gets the value of the set property.
     *
     * @return the sets known to the repository
     */
    public final List<Set> getSet() {
        if (set == null) {
            set = new ArrayList<Set>();
        }
        return this.set;
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
     * @param value the resumption token
     */
    public final void setResumptionToken(final ResumptionToken value) {
        this.resumptionToken = value;
    }
}
