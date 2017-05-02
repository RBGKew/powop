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
package org.emonocot.harvest.scheduling;

import java.text.ParseException;
import java.util.List;

import org.emonocot.harvest.service.JobListService;
import org.joda.time.DateTime;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

@Component
public class JobRunner {

	private Logger logger = LoggerFactory.getLogger(JobRunner.class);

	private static final List<String> cronExpressions = ImmutableList.<String>of(
			"* */1 0-9,17-23 ? * MON-FRI", // Working week schedule
			"* */10 0-23 ? * SAT-SUN"); // Weekend schedule

	@Autowired
	private JobListService jobListService;

	@Scheduled(cron="0 * * * * *")
	protected void run() {
		try {
			for (String cronExpression : cronExpressions) {
				CronExpression expression = new CronExpression(cronExpression);
				DateTime now = new DateTime();
				if (expression.isSatisfiedBy(now.toDate())) {
					jobListService.runAvailable();
				} else {
					logger.debug(now + " is not within " + cronExpression);
				}
			}
		} catch (ParseException e) {
			logger.error("Couldn't parse cron expression: {}", e.getMessage());
		}
	}
}