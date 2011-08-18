package org.emonocot.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

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
    private boolean present = false;

    /**
     *
     */
    private String code;

    /**
     *
     */
    private String text;

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
     * @return the present
     */
    @Column(columnDefinition = "boolean not null default 0")
    public boolean isPresent() {
        return present;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param present
     *            the present to set
     */
    public void setPresent(boolean present) {
        this.present = present;
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
}
