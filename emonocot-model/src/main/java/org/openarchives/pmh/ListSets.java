package org.openarchives.pmh;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ListSetsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListSetsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="set" type="{http://www.openarchives.org/OAI/2.0/}setType" maxOccurs="unbounded"/>
 *         &lt;element name="resumptionToken" type="{http://www.openarchives.org/OAI/2.0/}resumptionTokenType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListSetsType", propOrder = {
    "set",
    "resumptionToken"
})
public class ListSets {

    @XmlElement(required = true)
    protected List<Set> set;
    protected ResumptionToken resumptionToken;

    /**
     * Gets the value of the set property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the set property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Set }
     * 
     * 
     */
    public List<Set> getSet() {
        if (set == null) {
            set = new ArrayList<Set>();
        }
        return this.set;
    }

    /**
     * Gets the value of the resumptionToken property.
     * 
     * @return
     *     possible object is
     *     {@link ResumptionToken }
     *     
     */
    public ResumptionToken getResumptionToken() {
        return resumptionToken;
    }

    /**
     * Sets the value of the resumptionToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResumptionToken }
     *     
     */
    public void setResumptionToken(ResumptionToken value) {
        this.resumptionToken = value;
    }

}
