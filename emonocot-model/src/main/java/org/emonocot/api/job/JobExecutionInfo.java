package org.emonocot.api.job;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.marshall.json.DateTimeDeserializer;
import org.emonocot.model.marshall.json.DateTimeSerializer;
import org.joda.time.DateTime;
import org.springframework.batch.core.BatchStatus;

/**
 *
 * @author ben
 *
 */
public class JobExecutionInfo implements Serializable {

    /**
     *
     */
    private String resource;
    /**
     *
     */
    private Long id;
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
    private JobInstanceInfo jobInstance;

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
    
	private String resourceIdentifier;
	
	public void setResourceIdentifier(String resourceIdentifier) {
		this.resourceIdentifier = resourceIdentifier;
	}

    /**
     *
     * @param newResource Set the resource
     */
    public final void setResource(final String newResource) {
        this.resource = newResource;
    }

    /**
     *
     * @param newId Set the id;
     */
    public final void setId(final Long newId) {
        this.id = newId;
    }

    /**
     *
     * @param newStatus Set the status
     */
    public final void setStatus(final BatchStatus newStatus) {
        this.status = newStatus;
    }

    /**
     *
     * @param newStartTime Set the start time
     */
    @JsonDeserialize(using = DateTimeDeserializer.class)
    public final void setStartTime(final DateTime newStartTime) {
        this.startTime = newStartTime;
    }

    /**
     *
     * @param newExitCode Set the exit code
     */
    public final void setExitCode(final String newExitCode) {
        this.exitCode = newExitCode;
    }

    /**
     *
     * @param newDuration Set the duration
     */
    @JsonDeserialize(using = DateTimeDeserializer.class)
    public final void setDuration(final DateTime newDuration) {
        this.duration = newDuration;
    }

    /**
     *
     * @param newExitDescription Set the exit description
     */
    public final void setExitDescription(final String newExitDescription) {
        this.exitDescription = newExitDescription;
    }

    /**
     *
     * @param newJobInstance Set the job instance
     */
    public final void setJobInstance(final JobInstanceInfo newJobInstance) {
        this.jobInstance = newJobInstance;
    }

    /**
     * @return the resource
     */
    public final String getResource() {
        return resource;
    }

    /**
     * @return the id
     */
    public final Long getId() {
        return id;
    }

    /**
     * @return the status
     */
    public final BatchStatus getStatus() {
        return status;
    }

    /**
     * @return the startTime
     */
    @JsonSerialize(using = DateTimeSerializer.class)
    public final DateTime getStartTime() {
        return startTime;
    }

    /**
     * @return the exitCode
     */
    public final String getExitCode() {
        return exitCode;
    }

    /**
     * @return the duration
     */
    @JsonSerialize(using = DateTimeSerializer.class)
    public final DateTime getDuration() {
        return duration;
    }

    /**
     * @return the exitDescription
     */
    public final String getExitDescription() {
        return exitDescription;
    }

    /**
     * @return the jobInstance
     */
    public final JobInstanceInfo getJobInstance() {
        return jobInstance;
    }

	/**
	 * @return the read
	 */
	public Integer getRecordsRead() {
		return recordsRead;
	}

	/**
	 * @param read the read to set
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
	 * @param processed the processed to set
	 */
	public void setProcessSkip(Integer processSkip) {
		this.processSkip = processSkip;
	}

	/**
	 * @return the written
	 */
	public Integer getWritten() {
		return written;
	}

	/**
	 * @param written the written to set
	 */
	public void setWritten(Integer written) {
		this.written = written;
	}

	/**
	 * @return the readSkip
	 */
	public Integer getReadSkip() {
		return readSkip;
	}

	/**
	 * @param readSkip the readSkip to set
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
	 * @param writeSkip the writeSkip to set
	 */
	public void setWriteSkip(Integer writeSkip) {
		this.writeSkip = writeSkip;
	}

	public String getResourceIdentifier() {
		return resourceIdentifier;
	}
}
