package org.openarchives.pmh;

/**
 * <p>
 * Java class for GetRecordType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="GetRecordType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="record"
 *         type="{http://www.openarchives.org/OAI/2.0/}recordType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
public class GetRecord {

    /**
     *
     */
    private Record record;

    /**
     * Gets the value of the record property.
     *
     * @return the record
     *
     */
    public final Record getRecord() {
        return record;
    }

    /**
     * Sets the value of the record property.
     *
     * @param value Set the record property
     */
    public final void setRecord(final Record value) {
        this.record = value;
    }
}
