package org.emonocot.api.job;

import org.joda.time.DateTime;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;

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
    private Integer id;
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
    private ExitStatus exitStatus;
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
     * @param newResource Set the resource
     */
    public final void setResource(final String newResource) {
        this.resource = newResource;
    }

    /**
     *
     * @param newId Set the id;
     */
    public final void setId(final Integer newId) {
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
    public final void setStartTime(final DateTime newStartTime) {
        this.startTime = newStartTime;
    }

    /**
     *
     * @param newExitStatus Set the exit status
     */
    public final void setExitStatus(final ExitStatus newExitStatus) {
        this.exitStatus = newExitStatus;
    }

    /**
     *
     * @param newDuration Set the duration
     */
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
    public final void setJobInstance(final String newJobInstance) {
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
    public final Integer getId() {
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
    public final DateTime getStartTime() {
        return startTime;
    }

    /**
     * @return the exitStatus
     */
    public final ExitStatus getExitStatus() {
        return exitStatus;
    }

    /**
     * @return the duration
     */
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
    public final String getJobInstance() {
        return jobInstance;
    }
}
