package org.emonocot.persistence.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.emonocot.model.common.Base;
import org.emonocot.model.hibernate.DistributionBridge;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author ben
 *
 */
public class HibernateSearchIndexPurger extends HibernateDaoSupport {

    /**
     *
     */
    private static Logger logger
        = LoggerFactory.getLogger(HibernateSearchIndexPurger.class);
    /**
     *
     */
    private List<Class<? extends Base>> indexedClasses
        = new ArrayList<Class<? extends Base>>();

    /**
     *
     */
    private boolean purgeOnInit = false;

    /**
     * @param indexedClasses the indexedClasses to set
     */
    public final void setIndexedClasses(List<Class<? extends Base>> indexedClasses) {
        this.indexedClasses = indexedClasses;
    }

    /**
     * @param newPurgeOnInit purge the indexes on init?
     */
    public final void setPurgeOnInit(final boolean newPurgeOnInit) {
        this.purgeOnInit = newPurgeOnInit;
    }

    /**
    *
    * @param sessionFactory Set the session factory
    */
   @Autowired
   public final void setHibernateSessionFactory(
           final SessionFactory sessionFactory) {
       this.setSessionFactory(sessionFactory);
   }

   /**
    *
    */
   public final void purgeIndices() {
        if (purgeOnInit) {
        	logger.debug("Loading regions");
        	DistributionBridge distributionBridge = new DistributionBridge();
        	
        	try {
				distributionBridge.setupRegions();
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage());
			}
            logger.debug("Purging Indices");
            FullTextSession fullTextSession = Search
                    .getFullTextSession(getSession());

            for (Class<? extends Base> indexedClass : indexedClasses) {
                logger.debug("Purging " + indexedClass + " index");
                Transaction tx = fullTextSession.beginTransaction();
                fullTextSession.purgeAll(indexedClass);
                fullTextSession.getSearchFactory().optimize(indexedClass);
                tx.commit();
            }
        }
   }
}
