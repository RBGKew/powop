package org.emonocot.harvest.common.index;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.backend.impl.jms.AbstractJMSHibernateSearchController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
public class MessageDrivenSearchController extends
        AbstractJMSHibernateSearchController implements MessageListener {

    /**
    *
    */
    private Logger logger
        = LoggerFactory.getLogger(MessageDrivenSearchController.class);

    /**
     *
     */
    private SessionFactory sessionFactory;

    /**
     * @param message Set the message
     */
    @Override
    public final void onMessage(final Message message) {
        logger.debug("Received message: " + message);
        super.onMessage(message);
        logger.debug("Done processing message: " + message);
    }

    /**
     *
     * @param newSessionFactory Set the session factory
     */
    public final void setSessionFactory(final SessionFactory newSessionFactory) {
        this.sessionFactory = newSessionFactory;
    }

    /**
     * @return the current session
     */
    @Override
    protected final Session getSession() {
        logger.debug("Retrieving session for processing message");
        return sessionFactory.getCurrentSession();
    }

    /**
     * @param session Set the session
     */
    @Override
    protected void cleanSessionIfNeeded(final Session session) {
        // TODO Auto-generated method stub

    }

}
