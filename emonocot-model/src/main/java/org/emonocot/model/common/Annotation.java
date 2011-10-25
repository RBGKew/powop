package org.emonocot.model.common;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.emonocot.model.source.Source;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
@Entity
public class Annotation extends Base {

   /**
    *
    */
    private static final long serialVersionUID = -3382251087008774134L;

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
    private Source source;

    /**
     *
     */
    private AnnotationType type;

    /**
     *
     */
    public Annotation() {
        setIdentifier(UUID.randomUUID().toString());
    }

   /**
    *
    */
   private DateTime dateTime = new DateTime();

    /**
     * @return the authority
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public Source getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(Source source) {
        this.source = source;
    }

    /**
     * @return the type
     */
    @Enumerated(value = EnumType.STRING)
    public AnnotationType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(AnnotationType type) {
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
    public DateTime getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
