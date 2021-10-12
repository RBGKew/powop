package org.powo.job;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.persistence.dao.ImageDao;
import org.powo.persistence.dao.TaxonDao;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ 
  "/META-INF/spring/applicationContext.xml",
  "/META-INF/spring/applicationContext-batch.xml",
  "/META-INF/spring/applicationContext-test.xml",
  "/META-INF/spring/batch/jobs/reindex.xml" 
})
public class ReindexJobFunctionalTest {
  @Autowired
  private JobLauncherTestUtils jobLauncher;

  @Autowired
  private TaxonDao taxa;

  @Autowired
  private ImageDao images;

  @Test
  public void loads() throws Exception {
    var image = new Image();
    image.setIdentifier("test");
    image = images.save(image);
    
    var taxon = new Taxon();
    taxon.setIdentifier("taxon");
    taxon.setImages(List.of(image));
    taxon = taxa.save(taxon);

    var params = new JobParametersBuilder()
      .addJobParameters(jobLauncher.getUniqueJobParameters())
      .addString("query.string", "select t.id from Taxon t")
      .addString("query.type", "org.powo.model.Taxon")
      .toJobParameters();

    jobLauncher.launchJob(params);
  }
}
