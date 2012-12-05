package org.emonocot.job.oaipmh;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.emonocot.api.OrganisationService;
import org.emonocot.job.oaipmh.Validator;
import org.emonocot.model.Taxon;
import org.emonocot.model.registry.Organisation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Test to verify that the OaiPmhProcessor recognised constraints on the objects
 * and tries to rectify where possible.
 *
 * http://build.e-monocot.org/bugzilla/show_bug.cgi?id=101
 *
 * @author ben
 *
 */
public class ValidationConstraintsTest {

    /**
    *
    */
   private Validator processor;

   /**
    *
    */
   private Taxon taxon;

   /**
    *
    */
   private OrganisationService sourceService;

   /**
    * @throws Exception if something goes wrong
    */
   @Before
   public final void setUp() throws Exception {
       taxon = new Taxon();
       taxon.setIdentifier("identifier");
       LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
       validatorFactoryBean.afterPropertiesSet();
       processor = new Validator();

       sourceService = EasyMock.createMock(OrganisationService.class);
       processor.setOrganisationService(sourceService);
       processor.setValidator(validatorFactoryBean);
       processor.setSourceName("test");
       processor.beforeStep(new StepExecution("test", new JobExecution(1L)));
   }

   /**
    * @throws Exception if something goes wrong
    */
   @Test
   public final void testValidObject() throws Exception {
       String validAuthorship = "                                        ";
       taxon.setScientificNameAuthorship(validAuthorship);
       EasyMock.replay(sourceService);

       Taxon t = processor.process(taxon);
       assertEquals("Authorship not be changed", t.getScientificNameAuthorship(), validAuthorship);
       EasyMock.verify(sourceService);
   }

   /**
    * @throws Exception if something goes wrong
    */
   @Test
   public final void testInvalidRootObject() throws Exception {
       taxon.setScientificNameAuthorship("                                                                                                                                                                                                                                                                                                                                                                                                                                                        ");
       EasyMock.expect(
               sourceService.load(EasyMock.eq("test"))).andReturn(new Organisation());
       EasyMock.replay(sourceService);

       Taxon t = processor.process(taxon);
       assertEquals("Authorship be truncated", t.getScientificNameAuthorship().length(), 128);
       EasyMock.verify(sourceService);
   }
}
