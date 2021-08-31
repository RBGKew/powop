package org.powo.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;
import org.powo.model.constants.JobListStatus;
import org.powo.model.constants.SchedulingPeriod;
import org.powo.model.marshall.json.DateTimeDeserializer;
import org.powo.model.marshall.json.DateTimeSerializer;
import org.powo.model.marshall.json.JobConfigurationsDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic=true)
@Slf4j
public class JobList extends Base {

	private static final long serialVersionUID = 7081744961069520535L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
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
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@OrderColumn
	@JsonDeserialize(using = JobConfigurationsDeserializer.class)
	private List<JobConfiguration> jobConfigurations;

	@JsonProperty(access = Access.READ_ONLY)
	public boolean hasNextJob() {
		log.debug("currentJob: {}, num jobConfigurations: {}", currentJob, getJobConfigurations().size());
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
		setStatus(JobListStatus.Scheduled);
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