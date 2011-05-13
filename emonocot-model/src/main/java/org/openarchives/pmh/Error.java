package org.openarchives.pmh;

/**
 * <p>
 * Java class for OAI-PMHerrorType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="OAI-PMHerrorType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="code" use="required"
 *       type="{http://www.openarchives.org/OAI/2.0/}OAI-PMHerrorcodeType" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
public class Error {

    /**
     *
     */
    private String value;

    /**
     *
     */
    private ErrorCode code;

    /**
     * Gets the value of the value property.
     *
     * @return the message in the error
     */
    public final String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param newValue Set the message of the error
     */
    public final void setValue(final String newValue) {
        this.value = newValue;
    }

    /**
     * Gets the value of the code property.
     *
     * @return the code of the error
     */
    public final ErrorCode getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param newCode Set the code of the error
     */
    public final void setCode(final ErrorCode newCode) {
        this.code = newCode;
    }

}
