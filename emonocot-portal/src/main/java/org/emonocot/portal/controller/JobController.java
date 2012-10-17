package org.emonocot.portal.controller;

import org.emonocot.api.JobService;
import org.emonocot.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/job")
public class JobController extends GenericController<Job, JobService> {

	/**
	 *
	 * @param jobService Set the jobService;
	 */
	@Autowired
	public final void setJobService(final JobService jobService) {
		super.setService(jobService);
	}
	
    /**
     *
     */
    public JobController() {
        super("job");
    }

}
