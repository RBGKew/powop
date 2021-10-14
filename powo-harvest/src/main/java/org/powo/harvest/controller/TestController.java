package org.powo.harvest.controller;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  private static final Logger log = LoggerFactory.getLogger(TestController.class);
  
  @Autowired
  private JobLocator jobLocator;

  @Autowired 
  private JobLauncher jobLauncher;

  @GetMapping("/testjob")
  public Map<String, String> testJob() throws Exception {
    log.info("testing job");
    var job = jobLocator.getJob("hibernateTest");
    log.info("found job {}", job);
    var params = new JobParametersBuilder().addDate("runid", new Date()).toJobParameters();
    log.info("made job params {}", params);
    jobLauncher.run(job, params);
    log.info("launched job");
    return Map.of("message", "success");
  }

  @GetMapping("/test-reindex")
  public Map<String, String> testReindex() throws Exception {
    log.info("testing job");
    var job = jobLocator.getJob("ReIndex");
    log.info("found job {}", job);
    var params = new JobParametersBuilder()
      .addDate("runid", new Date())
      .addString("query.string", "select t.id from Taxon t")
      .addString("query.type", "org.powo.model.Taxon")
      .addString("resource.identifier", "reindex")
      .addString("solr.selectedFacets", "base.class_s=org.powo.model.Taxon")
      .toJobParameters();
    log.info("made job params {}", params);
    jobLauncher.run(job, params);
    log.info("launched job");
    return Map.of("message", "success");
  }
}
