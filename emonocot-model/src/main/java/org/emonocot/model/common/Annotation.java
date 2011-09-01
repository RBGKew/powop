package org.emonocot.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.emonocot.model.authority.Authority;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
@Entity
public class Annotation {

   /**
    *
    */
    private Long id;

   /**
    *
    */
    private String annotatedObjType;

    /**
    *
    */
    private Long annotatedObjId;

   /**
    *
    */
    private Long jobId;

    /**
     *
     */
    private String code;

    /**
     *
     */
    private String text;

    /**
     *
     */
    private Authority authority;

    /**
     *
     */
    private AnnotationType type;

   /**
    *
    */
   private DateTime dateTime = new DateTime();

    /**
     * @return the authority
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public final Authority getAuthority() {
        return authority;
    }

    /**
     * @param authority the authority to set
     */
    public final void setAuthority(Authority authority) {
        this.authority = authority;
    }

    /**
     * @return the type
     */
    @Enumerated(value = EnumType.STRING)
    public final AnnotationType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public final void setType(AnnotationType type) {
        this.type = type;
    }

    /**
     * @return the annotatedObj
     */
    public String getAnnotatedObjType() {
        return annotatedObjType;
    }

    /**
     * @param annotatedObj
     *            the annotatedObj to set
     */
    public void setAnnotatedObjType(String annotatedObjType) {
        this.annotatedObjType = annotatedObjType;
    }

    /**
     * @return the annotatedObj
     */
    public Long getAnnotatedObjId() {
        return annotatedObjId;
    }

    /**
     * @param annotatedObj
     *            the annotatedObj to set
     */
    public void setAnnotatedObjId(Long annotatedObjId) {
        this.annotatedObjId = annotatedObjId;
    }

    /**
     * @return the jobId
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * @param jobId
     *            the jobId to set
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    @Id
    @GeneratedValue(generator = "annotation-sequence")
    public Long getId() {
        return id;
    }

    /**
     *
     * @param code Set the code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the text
     */
    @Lob
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the dateTime
     */
    @Type(type = "olapDateTime")
    public final DateTime getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime the dateTime to set
     */
    public final void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
