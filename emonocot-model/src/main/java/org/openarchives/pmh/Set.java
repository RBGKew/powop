package org.openarchives.pmh;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * <p>
 * Java class for setType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="setType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="setSpec"
 *         type="{http://www.openarchives.org/OAI/2.0/}setSpecType"/>
 *         &lt;element name="setName"
 *         type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="setDescription"
 *         type="{http://www.openarchives.org/OAI/2.0/}descriptionType"
 *         maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
public class Set {

    /**
     *
     */
    private String setSpec;

    /**
     *
     */
    private String setName;

    /**
     *
     */
    @XStreamImplicit(itemFieldName = "setDescription")
    private List<String> setDescription;

    /**
     * Gets the value of the setSpec property.
     *
     * @return the set spec
     */
    public final String getSetSpec() {
        return setSpec;
    }

    /**
     * Sets the value of the setSpec property.
     *
     * @param value Set the set spec
     */
    public final void setSetSpec(final String value) {
        this.setSpec = value;
    }

    /**
     * Gets the value of the setName property.
     *
     * @return the set name
     */
    public final String getSetName() {
        return setName;
    }

    /**
     * Sets the value of the setName property.
     *
     * @param value Set the set name
     */
    public final void setSetName(final String value) {
        this.setName = value;
    }

    /**
     * Gets the value of the setDescription property.
     *
     * @return a description of the set
     */
    public final List<String> getSetDescription() {
        if (setDescription == null) {
            setDescription = new ArrayList<String>();
        }
        return this.setDescription;
    }

    /**
     *
     * @param newSetDescription Set the set description
     */
    public final void setSetDescription(final List<String> newSetDescription) {
        this.setDescription = newSetDescription;
    }

}
