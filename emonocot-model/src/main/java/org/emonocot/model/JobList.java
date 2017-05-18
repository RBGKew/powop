package org.emonocot.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import org.emonocot.model.constants.JobListStatus;
import org.emonocot.model.constants.SchedulingPeriod;
import org.emonocot.model.marshall.json.DateTimeDeserializer;
import org.emonocot.model.marshall.json.DateTimeSerializer;
import org.emonocot.model.marshall.json.JobConfigurationsDeserializer;
import org.emonocot.model.marshall.json.JobConfigurationsSerializer;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(Include.NON_NULL)
@Slf4j
public class JobList {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	private int currentJob;

	@Type(type = "dateTimeUserType")
	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private DateTime lastCompletion;

	@Type(type = "dateTimeUserType")
	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private DateTime lastAttempt;

	@Enumerated(value = EnumType.STRING)
	private SchedulingPeriod schedulingPeriod;

	@Type(type = "dateTimeUserType")
	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private DateTime nextRun;

	@Enumerated(value = EnumType.STRING)
	private JobListStatus status;

	@Singular
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@OrderColumn
	@JsonDeserialize(using = JobConfigurationsDeserializer.class)
	@JsonSerialize(using = JobConfigurationsSerializer.class)
	private List<JobConfiguration> jobConfigurations;

	public boolean hasNextJob() {
		log.debug("currentJob: {}, num jobConfigurations: {}", currentJob, getJobConfigurations().size());
		log.debug("jobConfigurations: {}", getJobConfigurations());
		return currentJob < (getJobConfigurations().size() - 1);
	}

	@JsonIgnore
	public boolean isRunningFirstJob() {
		return currentJob == 0;
	}

	@JsonIgnore
	public boolean isScheduled() {
		return status != null && status.equals(JobListStatus.Scheduled);
	}

	@JsonIgnore
	public boolean isSchedulable() {
		return status == null || (status.equals(JobListStatus.Completed) && !hasNextJob());
	}

	public void scheduleNextJob() {
		log.debug("Setting currentJob from {} to {}", currentJob, currentJob + 1);
		currentJob += 1;
		setStatus(JobListStatus.Completed);
	}

	public void updateNextAvailableDate() {
		DateTime nextAvailableDate = getNextRun();
		if(getSchedulingPeriod() == null) {
			nextAvailableDate = null;
		} else {
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
			}
		}

		setNextRun(nextAvailableDate);
		setStatus(JobListStatus.Completed);
	}
}
