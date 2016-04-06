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
package org.emonocot.model.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.ObjectUtils;
import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.Base;
import org.emonocot.model.Searchable;
import org.emonocot.model.constants.ResourceType;
import org.emonocot.model.constants.SchedulingPeriod;
import org.emonocot.model.marshall.json.OrganisationDeserialiser;
import org.emonocot.model.marshall.json.OrganisationSerializer;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.batch.core.BatchStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * @author ben
 *
 */
@Entity
public class Resource extends Base implements Searchable {

	private static final long serialVersionUID = 5676965857186600965L;

	private Long id;

	private ResourceType resourceType;

	private String uri;

	private DateTime lastHarvested;

	private DateTime lastAttempt;

	private String resource;

	private Long jobId;

	private BatchStatus status;

	private DateTime startTime;

	private String exitCode;

	private Duration duration;

	private String exitDescription;

	private String jobInstance;

	private Organisation organisation;

	private String title;

	private Boolean scheduled = Boolean.FALSE;

	private SchedulingPeriod schedulingPeriod;

	private DateTime nextAvailableDate;

	private Integer recordsRead = 0;

	private Integer readSkip = 0;

	private Integer processSkip = 0;

	private Integer writeSkip = 0;

	private Integer written = 0;

	private Map<String,String> parameters = new HashMap<String,String>();

	private String baseUrl;

	private Long lastHarvestedJobId;

	public Resource() {
		this.identifier = UUID.randomUUID().toString();
	}

	@NotEmpty
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the readSkip
	 */
	public Integer getReadSkip() {
		return readSkip;
	}

	/**
	 * @param readSkip
	 *            the readSkip to set
	 */
	public void setReadSkip(Integer readSkip) {
		this.readSkip = readSkip;
	}

	/**
	 * @return the writeSkip
	 */
	public Integer getWriteSkip() {
		return writeSkip;
	}

	/**
	 * @param writeSkip
	 *            the writeSkip to set
	 */
	public void setWriteSkip(Integer writeSkip) {
		this.writeSkip = writeSkip;
	}

	/**
	 *
	 * @return The unique identifier of the object
	 */
	@NaturalId
	@NotEmpty
	public String getIdentifier() {
		return identifier;
	}

	/**
	 *
	 * @param identifier
	 *            Set the unique identifier of the object
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the Id
	 */
	@Id
	@GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
	public Long getId() {
		return id;
	}

	/**
	 * @return the jobType
	 */
	@NotNull
	@Enumerated(value = EnumType.STRING)
	public ResourceType getResourceType() {
		return resourceType;
	}

	/**
	 * @param newJobType
	 *            Set the job type
	 */
	public void setResourceType(ResourceType newJobType) {
		this.resourceType = newJobType;
	}

	/**
	 * @return the uri
	 */
	@NotEmpty(groups = ReadResource.class)
	@URL
	public String getUri() {
		return uri;
	}

	/**
	 * @param newUri
	 *            Set the uri
	 */
	public void setUri(String newUri) {
		this.uri = newUri;
	}

	/**
	 * @return the last date the resource was last harvested
	 */
	@Type(type = "dateTimeUserType")
	public DateTime getLastHarvested() {
		return lastHarvested;
	}

	/**
	 * @param newLastHarvested
	 *            Set the date the resource was last harvested
	 */
	public void setLastHarvested(DateTime newLastHarvested) {
		this.lastHarvested = newLastHarvested;
	}

	/**
	 * @return the resource
	 */
	@URL
	public String getResource() {
		return resource;
	}

	/**
	 * @param newResource
	 *            Set the resource
	 */
	public void setResource(String newResource) {
		this.resource = newResource;
	}

	/**
	 * @return the jobId
	 */
	public Long getJobId() {
		return jobId;
	}

	/**
	 * @param newJobId
	 *            the jobId to set
	 */
	public void setJobId(Long newJobId) {
		this.jobId = newJobId;
	}

	/**
	 * @return the status
	 */
	@Enumerated(value = EnumType.STRING)
	public BatchStatus getStatus() {
		return status;
	}

	/**
	 * @param newStatus
	 *            the status to set
	 */
	public void setStatus(BatchStatus newStatus) {
		this.status = newStatus;
	}

	/**
	 * @return the startTime
	 */
	@Type(type = "dateTimeUserType")
	public DateTime getStartTime() {
		return startTime;
	}

	/**
	 * @param newStartTime
	 *            Set the start time
	 */
	public void setStartTime(DateTime newStartTime) {
		this.startTime = newStartTime;
	}

	/**
	 * @return the exit code
	 */
	public String getExitCode() {
		return exitCode;
	}

	/**
	 * @param newExitCode
	 *            Set the exit status
	 */
	public void setExitCode(String newExitCode) {
		this.exitCode = newExitCode;
	}

	/**
	 * @return the duration
	 */
	@Type(type = "durationUserType")
	public Duration getDuration() {
		return duration;
	}

	/**
	 * @param newDuration
	 *            Set the duration
	 */
	public void setDuration(Duration newDuration) {
		this.duration = newDuration;
	}

	/**
	 * @return the exitDescription
	 */
	@Lob
	@Length(max = 1431655761)
	public String getExitDescription() {
		return exitDescription;
	}

	/**
	 * @param newExitDescription
	 *            Set the exit description
	 */
	public void setExitDescription(String newExitDescription) {
		this.exitDescription = newExitDescription;
	}

	/**
	 * @return the jobInstance
	 */
	public String getJobInstance() {
		return jobInstance;
	}

	/**
	 * @param newJobInstance
	 *            Set the job instance
	 */
	public void setJobInstance(String newJobInstance) {
		this.jobInstance = newJobInstance;
	}

	/**
	 * @return the source
	 */
	@JsonSerialize(using = OrganisationSerializer.class)
	@ManyToOne(fetch = FetchType.LAZY)
	public Organisation getOrganisation() {
		return organisation;
	}

	/**
	 * @param organisation
	 *            the source to set
	 */
	@JsonDeserialize(using = OrganisationDeserialiser.class)
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the read
	 */
	public Integer getRecordsRead() {
		return recordsRead;
	}

	/**
	 * @param read
	 *            the read to set
	 */
	public void setRecordsRead(Integer read) {
		this.recordsRead = read;
	}

	/**
	 * @return the processed
	 */
	public Integer getProcessSkip() {
		return processSkip;
	}

	/**
	 * @param processed
	 *            the processed to set
	 */
	public void setProcessSkip(Integer processed) {
		this.processSkip = processed;
	}

	/**
	 * @return the written
	 */
	public Integer getWritten() {
		return written;
	}

	/**
	 * @param written
	 *            the written to set
	 */
	public void setWritten(Integer written) {
		this.written = written;
	}

	/**
	 * @return the parameters
	 */
	@ElementCollection
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the scheduled
	 */
	public Boolean getScheduled() {
		return scheduled;
	}

	/**
	 * @param scheduled the scheduled to set
	 */
	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled;
	}

	@Type(type = "dateTimeUserType")
	public DateTime getNextAvailableDate() {
		return nextAvailableDate;
	}

	public void setNextAvailableDate(DateTime nextAvailableDate) {
		this.nextAvailableDate = nextAvailableDate;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	@Enumerated(value = EnumType.STRING)
	public SchedulingPeriod getSchedulingPeriod() {
		return schedulingPeriod;
	}

	public void setSchedulingPeriod(SchedulingPeriod schedulingPeriod) {
		this.schedulingPeriod = schedulingPeriod;
	}

	public void updateNextAvailableDate() {
		if(getScheduled()) {
			DateTime nextAvailableDate = new DateTime();
			switch (getSchedulingPeriod()) {
			case YEARLY:
				nextAvailableDate = nextAvailableDate.plusYears(1);
				break;
			case MONTHLY:
				nextAvailableDate = nextAvailableDate.plusMonths(1);
				break;
			case WEEKLY:
				nextAvailableDate = nextAvailableDate.plusWeeks(1);
				break;
			case DAILY:
				nextAvailableDate = nextAvailableDate.plusDays(1);
				break;
			default:
				nextAvailableDate = null;
			}

			setNextAvailableDate(nextAvailableDate);
		}
	}

	@Override
	@Transient
	@JsonIgnore
	public String getDocumentId() {
		return getClassName() + "_" + getId();
	}


	@Transient
	@JsonIgnore
	public String getClassName() {
		return "Resource";
	}

	@Override
	public SolrInputDocument toSolrInputDocument() {
		DateTimeFormatter solrDateTimeFormat = DateTimeFormat.forPattern("yyyy'-'MM'-'dd'T'HH':'mm':'ss'Z'");
		SolrInputDocument sid = new SolrInputDocument();
		sid.addField("id", getClassName() + "_" + getId());
		sid.addField("base.id_l", getId());
		sid.addField("base.class_searchable_b", false);
		sid.addField("base.class_s", getClass().getName());

		if(getDuration() != null) {
			sid.addField("resource.duration_l",getDuration().getStandardSeconds());
		}
		sid.addField("resource.exit_code_s",getExitCode());
		sid.addField("resource.exit_description_t",getExitDescription());
		if(getLastHarvested() != null) {
			sid.addField("resource.last_harvested_dt",solrDateTimeFormat.print(getLastHarvested()));
		}
		if(getNextAvailableDate() != null) {
			sid.addField("resource.next_available_date_dt",solrDateTimeFormat.print(getNextAvailableDate()));
		}
		sid.addField("resource.process_skip_l",getProcessSkip());
		sid.addField("resource.records_read_l",getRecordsRead());
		sid.addField("resource.resource_type_s", ObjectUtils.toString(getResourceType().toString(), null));
		sid.addField("resource.scheduled_b", getScheduled());
		sid.addField("resource.scheduling_period_s", ObjectUtils.toString(getSchedulingPeriod(), null));
		if(getOrganisation() != null) {
			sid.addField("resource.organisation_s",getOrganisation().getIdentifier());
		}
		if(getStartTime() != null) {
			sid.addField("resource.start_time_dt",solrDateTimeFormat.print(getStartTime()));
		}
		sid.addField("resource.status_s", ObjectUtils.toString(getStatus(), null));
		sid.addField("resource.title_t", getTitle());
		sid.addField("resource.write_skip_l",getWriteSkip());
		sid.addField("resource.written_l",getWritten());
		sid.addField("searchable.label_sort", getTitle());
		StringBuilder summary = new StringBuilder().append(getExitDescription()).append(" ").append(getTitle());
		sid.addField("searchable.solrsummary_t", summary.toString());
		return sid;
	}

	public void setLastHarvestedJobId(Long lastHarvestedJobId) {
		this.lastHarvestedJobId = lastHarvestedJobId;
	}

	public Long getLastHarvestedJobId() {
		return lastHarvestedJobId;
	}

	public interface ReadResource {

	}

	@Type(type = "dateTimeUserType")
	public DateTime getLastAttempt() {
		return lastAttempt;
	}

	public void setLastAttempt(DateTime lastAttempt) {
		this.lastAttempt = lastAttempt;
	}

}
