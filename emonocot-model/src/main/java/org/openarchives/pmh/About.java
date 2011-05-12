package org.openarchives.pmh;


/**
 * Data "about" the record must be expressed in XML that is compliant with an
 * XML Schema defined by a community.
 *
 * <p>
 * Java class for aboutType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="aboutType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;any/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
public class About {

    /**
     *
     */
    private String any;

    /**
     * Gets the value of the any property.
     *
     * @return the value of this element
     */
    public final String getAny() {
        return any;
    }

    /**
     * Sets the value of the any property.
     *
     * @param value Set the value of this element
     */
    public final void setAny(final String value) {
        this.any = value;
    }
}

