package org.openarchives.pmh;

/**
 * <p>
 * Java class for verbType.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="verbType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Identify"/>
 *     &lt;enumeration value="ListMetadataFormats"/>
 *     &lt;enumeration value="ListSets"/>
 *     &lt;enumeration value="GetRecord"/>
 *     &lt;enumeration value="ListIdentifiers"/>
 *     &lt;enumeration value="ListRecords"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
public enum Verb {

    /**
     *
     */
    Identify,
    /**
     *
     */
    ListMetadataFormats,
    /**
     *
     */
    ListSets,
    /**
     *
     */
    GetRecord,
    /**
     *
     */
    ListIdentifiers,
    /**
     *
     */
    ListRecords;
}
