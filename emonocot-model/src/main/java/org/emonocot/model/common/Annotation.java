package org.emonocot.model.common;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.emonocot.model.description.Distribution;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.hibernate.AnnotatedObjBridge;
import org.emonocot.model.hibernate.DateTimeBridge;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Parameter;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed
public class Annotation extends Base {

   /**
    *
    */
    private static final long serialVersionUID = -3382251087008774134L;

   /**
    *
    */
    private Base annotatedObj;

   /**
    *
    */
    private Long jobId;

    /**
     *
     */
    private AnnotationCode code;

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
    *
    */
   private Long id;

   /**
    *
    */
   private RecordType recordType;

   /**
    *
    */
   private String value;

    /**
     *
     * @return the id
     */
    @Id
    @DocumentId
    @GeneratedValue(generator = "annotation-sequence")
    public Long getId() {
        return id;
    }

    /**
    *
    * @param newId
    *            Set the identifier of this object.
    */
    public void setId(Long newId) {
       this.id = newId;
    }

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
    @Field(analyzer = @Analyzer(
            definition =  "facetAnalyzer"))
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
    @Any(metaColumn = @Column(
                name = "annotatedObjType"), optional = true,
                fetch = FetchType.EAGER)
    @AnyMetaDef(idType = "long", metaType = "string", metaValues = {
            @MetaValue(targetEntity = Taxon.class, value = "Taxon"),
            @MetaValue(targetEntity = Distribution.class, value = "Distribution"),
            @MetaValue(targetEntity = TextContent.class, value = "TextContent"),
            @MetaValue(targetEntity = Image.class, value = "Image"),
            @MetaValue(targetEntity = Reference.class, value = "Reference")
            })
    @JoinColumn(name = "annotatedObjId")
    @FieldBridge(impl = AnnotatedObjBridge.class)
    public Base getAnnotatedObj() {
        return annotatedObj;
    }

    /**
     * @param annotatedObj
     *            the annotatedObj to set
     */
    public void setAnnotatedObj(Base annotatedObj) {
        this.annotatedObj = annotatedObj;
    }

    /**
     * @return the jobId
     */
    @Field
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
    public void setCode(AnnotationCode code) {
        this.code = code;
    }

    /**
     * @return the text
     */
    @Field
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
    @Field(analyzer = @Analyzer(
            definition =  "facetAnalyzer"))
    @Enumerated(value = EnumType.STRING)
    public AnnotationCode getCode() {
        return code;
    }

    /**
     * @return the record type
     */
    @Field(analyzer = @Analyzer(
            definition =  "facetAnalyzer"))
    @Enumerated(value = EnumType.STRING)
    public RecordType getRecordType() {
        return recordType;
    }

    /**
     * @param recordType Set the record type
     */
    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    /**
     * @return the dateTime
     */
    @FieldBridge(impl = DateTimeBridge.class, params = {
        @Parameter(name = "resolution", value = "MILLISECOND")
    })
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

    /**
     * @return the value
     */
    @Field
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
