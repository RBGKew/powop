package org.emonocot.portal.controller;

import org.emonocot.api.JobService;
import org.emonocot.model.job.Job;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
     */
    public JobController() {
        super("job");
    }

}
