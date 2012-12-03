package org.emonocot.model.registry;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.Base;
import org.emonocot.model.constants.JobType;
import org.emonocot.model.marshall.json.OrganisationDeserialiser;
import org.emonocot.model.marshall.json.OrganisationSerializer;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.joda.time.DateTime;
import org.springframework.batch.core.BatchStatus;

/**
 * 
 * @author ben
 * 
 */
@Entity
public class Resource extends Base {
	
   /**
    *
    */
	private static long serialVersionUID = 5676965857186600965L;

	/**
     *
     */
	private Long id;

	/**
     *
     */
	private JobType jobType;

	/**
     *
     */
	private String uri;

	/**
     *
     */
	private DateTime lastHarvested;

	/**
    *
    */
	private String resource;

	/**
    *
    */
	private Long jobId;
	/**
    *
    */
	private BatchStatus status;

	/**
    *
    */
	private DateTime startTime;

   /**
    *
    */
	private String exitCode;

	/**
    *
    */
	private DateTime duration;

	/**
    *
    */
	private String exitDescription;

	/**
    *
    */
	private String jobInstance;

	/**
     *
     */
	private Organisation organisation;

	/**
    *
    */
	private Integer recordsRead = 0;

	/**
    *
    */
	private Integer readSkip = 0;

	/**
    *
    */
	private Integer processSkip = 0;

	/**
    *
    */
	private Integer writeSkip = 0;

   /**
    *
    */
	private Integer written = 0;
	
	/**
	 *
	 */
	private Map<String,String> parameters = new HashMap<String,String>();
	
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
	@GeneratedValue(generator = "system-increment")
	public Long getId() {
		return id;
	}

	/**
	 * @return the jobType
	 */
	@Enumerated(value = EnumType.STRING)
	public JobType getJobType() {
		return jobType;
	}

	/**
	 * @param newJobType
	 *            Set the job type
	 */
	@Enumerated(value = EnumType.STRING)
	public void setJobType(JobType newJobType) {
		this.jobType = newJobType;
	}

	/**
	 * @return the uri
	 */
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
	@Type(type = "dateTimeUserType")
	public DateTime getDuration() {
		return duration;
	}

	/**
	 * @param newDuration
	 *            Set the duration
	 */
	public void setDuration(DateTime newDuration) {
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
}
