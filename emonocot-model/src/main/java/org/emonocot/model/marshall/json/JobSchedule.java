package org.emonocot.model.marshall.json;

import org.emonocot.model.constants.SchedulingPeriod;
import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
public class JobSchedule {

	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private DateTime nextRun;

	private SchedulingPeriod schedulingPeriod;
}
