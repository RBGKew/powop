/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.powo.model;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.powo.model.constants.AnnotationCode;
import org.powo.model.constants.AnnotationType;
import org.powo.model.constants.RecordType;
import org.powo.model.marshall.json.AnnotatableObjectDeserializer;
import org.powo.model.marshall.json.AnnotatableObjectSerializer;
import org.powo.model.marshall.json.DateTimeDeserializer;
import org.powo.model.marshall.json.DateTimeSerializer;
import org.powo.model.marshall.json.OrganisationDeserialiser;
import org.powo.model.marshall.json.OrganisationSerializer;
import org.powo.model.registry.Organisation;
import org.powo.model.registry.Resource;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
public class Annotation extends Base implements Searchable {

	private static final long serialVersionUID = -3382251087008774134L;

	private Base annotatedObj;

	private Long jobId;

	private AnnotationCode code;

	private String text;

	private Organisation authority;

	private AnnotationType type;

	public Annotation() {
		setIdentifier(UUID.randomUUID().toString());
	}

	private DateTime dateTime = new DateTime();

	private Long id;

	private RecordType recordType;

	private String value;

	private Resource resource;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long newId) {
		this.id = newId;
	}

	@JsonSerialize(using = OrganisationSerializer.class)
	@ManyToOne(fetch = FetchType.LAZY)
	public Organisation getAuthority() {
		return authority;
	}

	@JsonDeserialize(using = OrganisationDeserialiser.class)
	public void setAuthority(Organisation source) {
		this.authority = source;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Enumerated(value = EnumType.STRING)
	public AnnotationType getType() {
		return type;
	}

	public void setType(AnnotationType type) {
		this.type = type;
	}

	@Any(metaColumn = @Column(name = "annotatedObjType"), optional = true, fetch = FetchType.LAZY,metaDef = "AnnotationMetaDef")
	@JoinColumn(name = "annotatedObjId", nullable = true)
	@JsonSerialize(using = AnnotatableObjectSerializer.class)
	public Base getAnnotatedObj() {
		return annotatedObj;
	}

	@JsonDeserialize(using = AnnotatableObjectDeserializer.class)
	public void setAnnotatedObj(Base annotatedObj) {
		this.annotatedObj = annotatedObj;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public void setCode(AnnotationCode code) {
		this.code = code;
	}

	@Lob
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Enumerated(value = EnumType.STRING)
	public AnnotationCode getCode() {
		return code;
	}

	@Enumerated(value = EnumType.STRING)
	public RecordType getRecordType() {
		return recordType;
	}

	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
	}

	@Type(type="dateTimeUserType")
	@JsonSerialize(using = DateTimeSerializer.class)
	public DateTime getDateTime() {
		return dateTime;
	}

	@JsonDeserialize(using = DateTimeDeserializer.class)
	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public SolrInputDocument toSolrInputDocument(ApplicationContext ctx) {
		SolrInputDocument sid = new SolrInputDocument();
		sid.addField("id", getClassName() + "_" + getId());
		sid.addField("base.id_l", getId());
		sid.addField("base.class_searchable_b", false);
		sid.addField("base.class_s", getClass().getName());
		sid.addField("annotation.job_id_l",getJobId());
		sid.addField("annotation.type_s", Objects.toString(getType(), null));
		sid.addField("annotation.record_type_s", Objects.toString(getRecordType(), null));
		sid.addField("annotation.code_s", Objects.toString(getCode(), null));
		StringBuilder summary = new StringBuilder().append(getType()).append(" ")
				.append(getRecordType()).append(" ").append(getCode()).append(" ").append(getText());

		if(getAuthority() != null) {
			sid.addField("base.authority_s", getAuthority().getIdentifier());
			summary.append(" ").append(getAuthority().getIdentifier());
		}

		sid.addField("searchable.solrsummary_t", summary.toString());
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
