package org.emonocot.api.job;

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
public class JobExecutionInfo {

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
}
