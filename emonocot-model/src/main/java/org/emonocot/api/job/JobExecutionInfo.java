package org.emonocot.api.job;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.marshall.json.DateTimeDeserializer;
import org.emonocot.model.marshall.json.DateTimeSerializer;
import org.joda.time.DateTime;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

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
    private String jobInstance;

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
	
	private Integer progress;
	
	private String baseUrl;
	
	public JobExecutionInfo() {
		
	}
	
	public JobExecutionInfo(JobExecution jobExecution, String baseUrl) {
		resourceIdentifier = jobExecution.getJobInstance().getJobParameters().getString("resource.identifier");
		DateTime sTime = new DateTime(jobExecution.getStartTime());
		DateTime eTime = new DateTime(jobExecution.getEndTime());
		duration = eTime.minus(sTime.getMillis());
		startTime = sTime;
		exitDescription = jobExecution.getExitStatus().getExitDescription();
		exitCode = jobExecution.getExitStatus().getExitCode();
		id = jobExecution.getId();
		jobInstance = baseUrl + "/jobs/"	+ jobExecution.getJobInstance().getJobName() + "/"	+ jobExecution.getJobInstance().getId();		
		resource = baseUrl + "/jobs/executions/"	+ jobExecution.getId();
		this.baseUrl = baseUrl;
		status = jobExecution.getStatus();
		
		Integer writeSkip = 0;
		for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
			recordsRead += stepExecution.getReadCount();
			readSkip += stepExecution.getReadSkipCount();
			processSkip += stepExecution.getProcessSkipCount();
			written += stepExecution.getWriteCount();
			writeSkip += stepExecution.getWriteSkipCount();
		}
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
	
	public void setResourceIdentifier(String resourceIdentifier) {
		this.resourceIdentifier = resourceIdentifier;
	}

    /**
     *
     * @param newResource Set the resource
     */
    public void setResource(String newResource) {
        this.resource = newResource;
    }

    /**
     *
     * @param newId Set the id;
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     *
     * @param newStatus Set the status
     */
    public void setStatus(BatchStatus newStatus) {
        this.status = newStatus;
    }

    /**
     *
     * @param newStartTime Set the start time
     */
    @JsonDeserialize(using = DateTimeDeserializer.class)
    public void setStartTime(DateTime newStartTime) {
        this.startTime = newStartTime;
    }

    /**
     *
     * @param newExitCode Set the exit code
     */
    public void setExitCode(String newExitCode) {
        this.exitCode = newExitCode;
    }

    /**
     *
     * @param newDuration Set the duration
     */
    @JsonDeserialize(using = DateTimeDeserializer.class)
    public void setDuration(DateTime newDuration) {
        this.duration = newDuration;
    }

    /**
     *
     * @param newExitDescription Set the exit description
     */
    public void setExitDescription(String newExitDescription) {
        this.exitDescription = newExitDescription;
    }

    /**
     *
     * @param newJobInstance Set the job instance
     */
    public void setJobInstance(String newJobInstance) {
        this.jobInstance = newJobInstance;
    }

    /**
     * @return the resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the status
     */
    public BatchStatus getStatus() {
        return status;
    }

    /**
     * @return the startTime
     */
    @JsonSerialize(using = DateTimeSerializer.class)
    public DateTime getStartTime() {
        return startTime;
    }

    /**
     * @return the exitCode
     */
    public String getExitCode() {
        return exitCode;
    }

    /**
     * @return the duration
     */
    @JsonSerialize(using = DateTimeSerializer.class)
    public DateTime getDuration() {
        return duration;
    }

    /**
     * @return the exitDescription
     */
    public String getExitDescription() {
        return exitDescription;
    }

    /**
     * @return the jobInstance
     */
    public String getJobInstance() {
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

	public void setProgress(Integer progress) {
		this.progress = progress;
	}
	
	public Integer getProgress() {
		return progress;
	}
}
