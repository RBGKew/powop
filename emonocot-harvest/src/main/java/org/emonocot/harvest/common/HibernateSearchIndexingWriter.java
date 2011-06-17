package org.emonocot.harvest.common;

import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author ben
 *
 */
public class HibernateSearchIndexingWriter
    extends HibernateDaoSupport implements ItemWriter<Object>,
    StepExecutionListener {

    /**
     *
     */
    private PlatformTransactionManager transactionManager;

    /**
     *
     */
    private DefaultTransactionDefinition transactionDefinition
        = new DefaultTransactionDefinition();

    /**
     *
     */
    private TransactionStatus transactionStatus;

    /**
     *
     * @param newTransactionManager Set the transaction manager
     */
    public final void setTransactionManager(
            final PlatformTransactionManager newTransactionManager) {
        this.transactionManager = newTransactionManager;
    }

    @Override
    public final void write(
            final List<? extends Object> objects) throws Exception {
        FullTextSession fullTextSession
            = Search.createFullTextSession(getSession());
        fullTextSession.setFlushMode(FlushMode.MANUAL);
        fullTextSession.setCacheMode(CacheMode.IGNORE);

        for (Object o : objects) {
            fullTextSession.index(o); //index each element
        }
        fullTextSession.flushToIndexes(); //apply changes to indexes
        fullTextSession.clear(); //free memory since the queue is processed
    }

    @Override
    public final void beforeStep(final StepExecution stepExecution) {
        transactionStatus
            = transactionManager.getTransaction(transactionDefinition);
    }

    @Override
    public final ExitStatus afterStep(final StepExecution stepExecution) {
        transactionManager.commit(transactionStatus);
        return null;
    }

}
