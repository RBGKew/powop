package org.emonocot.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.emonocot.model.constants.JobListStatus;
import org.emonocot.model.constants.SchedulingPeriod;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

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
@Slf4j
public class JobList {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	@Singular
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@OrderBy("id asc")
	private List<JobConfiguration> jobConfigurations;

	private int currentJob;

	@Type(type = "dateTimeUserType")
	private DateTime lastCompletion;

	@Type(type = "dateTimeUserType")
	private DateTime lastAttempt;

	@Enumerated(value = EnumType.STRING)
	private SchedulingPeriod schedulingPeriod;

	@Type(type = "dateTimeUserType")
	private DateTime nextRun;

	@Enumerated(value = EnumType.STRING)
	private JobListStatus status;

	/* A quirk of hibernate FetchType.EAGER is that it can return duplicate items due to the outer join */
	public List<JobConfiguration> getJobConfigurations() {
		return jobConfigurations.stream().distinct().collect(Collectors.toList());
	}

	public boolean hasNextJob() {
		log.debug("currentJob: {}, num jobConfigurations: {}", currentJob, getJobConfigurations().size());
		log.debug("jobConfigurations: {}", getJobConfigurations());
		return currentJob < (getJobConfigurations().size() - 1);
	}

	public boolean isRunningFirstJob() {
		return currentJob == 0;
	}

	public boolean isScheduled() {
		return status != null && status.equals(JobListStatus.Scheduled);
	}

	public void scheduleNextJob() {
		log.debug("Setting currentJob from {} to {}", currentJob, currentJob + 1);
		currentJob += 1;
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
