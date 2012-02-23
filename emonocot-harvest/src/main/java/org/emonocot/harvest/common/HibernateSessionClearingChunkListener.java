package org.emonocot.harvest.common;

import org.hibernate.SessionFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class which clears hibernate session at the beginning of a chunk rather than
 * as part of the write method to avoid problems with related objects and
 * hibernate-search.
 *
 * @author ben
 *
 */
public class HibernateSessionClearingChunkListener implements ChunkListener {

   /**
    *
    */
   private SessionFactory sessionFactory;

   /**
    * @param newSessionFactory Set the session factory
    */
   @Autowired
   public final void setSessionFactory(
           final SessionFactory newSessionFactory) {
       this.sessionFactory = newSessionFactory;
   }

    /**
     *
     */
    public final void afterChunk() {
    }

    /**
     *
     */
    public final void beforeChunk() {
        sessionFactory.getCurrentSession().clear();
    }

}
