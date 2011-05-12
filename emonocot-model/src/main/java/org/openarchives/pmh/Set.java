package org.openarchives.pmh;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * <p>Java class for setType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="setSpec" type="{http://www.openarchives.org/OAI/2.0/}setSpecType"/>
 *         &lt;element name="setName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="setDescription" type="{http://www.openarchives.org/OAI/2.0/}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setType", propOrder = {
    "setSpec",
    "setName",
    "setDescription"
})
public class Set {

    @XmlElement(required = true)
    protected String setSpec;
    @XmlElement(required = true)
    protected String setName;
    protected List<Description> setDescription;

    /**
     * Gets the value of the setSpec property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetSpec() {
        return setSpec;
    }

    /**
     * Sets the value of the setSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetSpec(String value) {
        this.setSpec = value;
    }

    /**
     * Gets the value of the setName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetName() {
        return setName;
    }

    /**
     * Sets the value of the setName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetName(String value) {
        this.setName = value;
    }

    /**
     * Gets the value of the setDescription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the setDescription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSetDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Description }
     * 
     * 
     */
    public List<Description> getSetDescription() {
        if (setDescription == null) {
            setDescription = new ArrayList<Description>();
        }
        return this.setDescription;
    }
    
    public void addDescription(String string) throws ParserConfigurationException, SAXException, IOException {
    	if(setDescription != null){
    		Description description = new Description();
    		InputSource inputSource = new InputSource(new StringReader(string));
    		DocumentBuilderFactory documentBuilderFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
    		documentBuilderFactory.setNamespaceAware(false);
    		
    		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    		Document document = documentBuilder.parse(inputSource);
	    	Element domElem = (Element)document.getDocumentElement();

	    	description.setAny(domElem);
    		
    	    this.setDescription.add(description);
    	}
    }

}
