package org.openarchives.pmh;

/**
 * <p>
 * Java class for OAI-PMHerrorcodeType.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="OAI-PMHerrorcodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="cannotDisseminateFormat"/>
 *     &lt;enumeration value="idDoesNotExist"/>
 *     &lt;enumeration value="badArgument"/>
 *     &lt;enumeration value="badVerb"/>
 *     &lt;enumeration value="noMetadataFormats"/>
 *     &lt;enumeration value="noRecordsMatch"/>
 *     &lt;enumeration value="badResumptionToken"/>
 *     &lt;enumeration value="noSetHierarchy"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
public enum ErrorCode {

    /**
     *
     */
    cannotDisseminateFormat,
    /**
     *
     */
    idDoesNotExist,
    /**
     *
     */
    badArgument,
    /**
     *
     */
    badVerb,
    /**
     *
     */
    noMetadataFormats,
    /**
     *
     */
    noRecordsMatch,
    /**
     *
     */
    badResumptionToken,
    /**
     *
     */
    noSetHierarchy;
}
