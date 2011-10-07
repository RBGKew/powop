package org.emonocot.checklist.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import org.dbunit.dataset.DataSetException;
import org.emonocot.checklist.logging.LoggingConstants;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.model.pager.Page;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import ch.qos.logback.classic.ClassicConstants;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/applicationContext-test.xml" })
public class UsageLoggingIntegrationTest extends
        AbstractPersistenceTestSupport {

    /**
     *
     */
    private static Logger queryLog = LoggerFactory.getLogger("query");

   /**
    *
    */
   @Autowired
   private PlatformTransactionManager transactionManager;

    /**
     * @throws Exception if there is a problem
     */
    @Test
    @Ignore
    public final void loggingTest() throws Exception {
        assertEquals("There should be no rows in the web_log", 0,
                jdbcTemplate
                        .queryForInt("SELECT COUNT(web_log_id) FROM web_log"));
        doInTransaction(new Callable() {
            public Object call() {
                try {
                    MDC.put(LoggingConstants.SEARCH_TYPE_KEY, "st");
                    MDC.put(LoggingConstants.QUERY_KEY,
                            "Familyaceae Genus specificEpithet");
                    MDC.put(LoggingConstants.RESULT_COUNT_KEY, "123");
                    MDC.put(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY,
                            "127.0.0.1");
                    MDC.put(LoggingConstants.MDC_CLIENT_NAME_KEY,
                            "e-monocot.org");
                    queryLog.info("querylogs");
                } finally {
                    MDC.remove(LoggingConstants.SEARCH_TYPE_KEY);
                    MDC.remove(LoggingConstants.QUERY_KEY);
                    MDC.remove(LoggingConstants.RESULT_COUNT_KEY);
                    MDC.remove(LoggingConstants.FAMILY_KEY);
                    MDC.remove(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY);
                    MDC.remove(LoggingConstants.MDC_CLIENT_NAME_KEY);
                }
                return null;
            }
        });
        assertEquals("There should be one row in the web_log", 1,
                jdbcTemplate
                        .queryForInt("SELECT COUNT(web_log_id) FROM web_log"));

        super.assertDatasetsEqual("org/emonocot/checklist/persistence/UsageLoggingIntegrationTest.loggingTest-expected.xml");
    }

    /**
    *
    * @param task Set the method to run in a transaction
    * @return the object returned by the callable method
    * @throws Exception if there is a problem running the method
    */
   private Object doInTransaction(final Callable task) throws Exception {
        DefaultTransactionDefinition transactionDefinition
         = new DefaultTransactionDefinition();
        transactionDefinition.setName("test");
        transactionDefinition
                .setPropagationBehavior(
                        TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager
                .getTransaction(transactionDefinition);
        Object value = null;
        try {
            value = task.call();
        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }
        transactionManager.commit(status);
        return value;
    }
}
