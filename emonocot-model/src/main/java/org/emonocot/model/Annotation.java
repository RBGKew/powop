package org.emonocot.model;

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
import javax.persistence.Transient;

import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.emonocot.model.marshall.json.AnnotatableObjectDeserializer;
import org.emonocot.model.marshall.json.AnnotatableObjectSerializer;
import org.emonocot.model.marshall.json.OrganisationDeserialiser;
import org.emonocot.model.marshall.json.OrganisationSerializer;
import org.emonocot.model.registry.Organisation;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
@Entity
public class Annotation extends Base implements Searchable {

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
    private Organisation authority;

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
    @JsonSerialize(using = OrganisationSerializer.class)
    @ManyToOne(fetch = FetchType.LAZY)
    public Organisation getAuthority() {
        return authority;
    }

    /**
     * @param source the source to set
     */
    @JsonDeserialize(using = OrganisationDeserialiser.class)
    public void setAuthority(Organisation source) {
        this.authority = source;
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
    @Any(metaColumn = @Column(
                name = "annotatedObjType"), optional = true,
                fetch = FetchType.LAZY)
    @AnyMetaDef(idType = "long", metaType = "string", metaValues = {
            @MetaValue(targetEntity = Taxon.class, value = "Taxon"),
            @MetaValue(targetEntity = Distribution.class, value = "Distribution"),
            @MetaValue(targetEntity = Description.class, value = "Description"),
            @MetaValue(targetEntity = Image.class, value = "Image"),
            @MetaValue(targetEntity = Reference.class, value = "Reference"),
            @MetaValue(targetEntity = Organisation.class, value = "Organisation")
            })
    @JoinColumn(name = "annotatedObjId", nullable = true)
    @JsonSerialize(using = AnnotatableObjectSerializer.class)
    public Base getAnnotatedObj() {
        return annotatedObj;
    }

    /**
     * @param annotatedObj
     *            the annotatedObj to set
     */
    @JsonDeserialize(using = AnnotatableObjectDeserializer.class)
    public void setAnnotatedObj(Base annotatedObj) {
        this.annotatedObj = annotatedObj;
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
    public void setCode(AnnotationCode code) {
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
    @Enumerated(value = EnumType.STRING)
    public AnnotationCode getCode() {
        return code;
    }

    /**
     * @return the record type
     */
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
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String newIdentifier) {
        this.identifier = newIdentifier;
    }

	@Override
	public SolrInputDocument toSolrInputDocument() {
		SolrInputDocument sid = new SolrInputDocument();
		sid.addField("id", getClassName() + "_" + getId());
    	sid.addField("base.id_l", getId());
    	sid.addField("base.class_searchable_b", false);
    	sid.addField("base.class_s", getClass().getName());
    	if(getAuthority() != null) {
			sid.addField("base.authority_s", getAuthority().getIdentifier());
		}
    	sid.addField("annotation.job_id_l",getJobId());
    	sid.addField("annotation.type_s",getType());
    	sid.addField("annotation.record_type_s",getRecordType());
    	sid.addField("annotation.code_s",getCode());
    	sid.addField("annotation.text_t",getText());
		return sid;
	}

	@Transient
	@JsonIgnore
	public String getClassName() {
		return "Annotation";
	}
	
	@Override
	@Transient
    @JsonIgnore
	public String getDocumentId() {
		return getClassName() + "_" + getId();
	}

}
