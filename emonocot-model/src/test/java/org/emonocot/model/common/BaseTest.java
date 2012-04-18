package org.emonocot.model.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.pojo.javassist.JavassistProxyFactory;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class BaseTest {

    /**
     *
     */
    private Annotation b1;

    /**
     *
     */
    private Annotation b2;

    /**
     *
     */
    private Object b3;

    /**
     *
     */
    @Before
    public final void setUp() {
        b1 = new Annotation();
        b2 = new Annotation();

    }

    /**
     * Root Cause of http://build.e-monocot.org/bugzilla/show_bug.cgi?id=262
     * Unexpected Taxon Exception in DwC Harvesting even though the taxon is
     * expected. If both identifiers are null (and if both primary keys are
     * null) equals is false.
     */
    @Test
    public final void testEqualsWithNullIdentifiers() {
        b2.setIdentifier(null);
        b1.setIdentifier(null);
        assertFalse("Equals should return false", b1.equals(b2));
    }

    /**
     * Another problem with
     * http://build.e-monocot.org/bugzilla/show_bug.cgi?id=262 Unexpected Taxon
     * Exception in DwC Harvesting even though the taxon is expected. Comparing
     * HibernateProxies with non-proxies means you can't use o1.getClass() ==
     * o2.getClass().
     * @throws Exception if there is a problem
     */
    @Test
    public final void testEqualsWithHibernateProxies() throws Exception {
        b2.setIdentifier("test");
        b2.setId(1L);
        b1.setIdentifier("test");
        b1.setId(1L);
        SessionImplementor sessionImplementor = EasyMock
                .createMock(SessionImplementor.class);
        SessionFactoryImplementor sessionFactoryImplementor = EasyMock
                .createMock(SessionFactoryImplementor.class);
        EntityPersister entityPersister = EasyMock
                .createMock(EntityPersister.class);
        PersistenceContext persistenceContext = EasyMock
                .createMock(PersistenceContext.class);
        EasyMock.expect(sessionImplementor.getFactory())
                .andReturn(sessionFactoryImplementor).anyTimes();
        EasyMock.expect(
                sessionFactoryImplementor.getEntityPersister((String) EasyMock
                        .eq("Annotation"))).andReturn(entityPersister)
                .anyTimes();
        EasyMock.expect(sessionImplementor.getPersistenceContext()).andReturn(
                persistenceContext);
        EasyMock.expect(persistenceContext.isDefaultReadOnly())
                .andReturn(Boolean.TRUE).anyTimes();
        EasyMock.expect(entityPersister.isMutable()).andReturn(Boolean.TRUE)
                .anyTimes();
        EasyMock.expect(sessionImplementor.isClosed()).andReturn(Boolean.FALSE)
                .anyTimes();
        EasyMock.expect(sessionImplementor.isOpen()).andReturn(Boolean.TRUE)
                .anyTimes();
        EasyMock.expect(sessionImplementor.isConnected())
                .andReturn(Boolean.TRUE).anyTimes();
        EasyMock.expect(
                sessionImplementor.immediateLoad(EasyMock.eq("Annotation"),
                        EasyMock.eq(1L))).andReturn(b2).anyTimes();

        EasyMock.replay(sessionImplementor, sessionFactoryImplementor,
                entityPersister, persistenceContext);

        JavassistProxyFactory javassistProxyFactory = new JavassistProxyFactory();
        Set interfaces = new HashSet();
        interfaces.add(HibernateProxy.class);
        interfaces.add(Serializable.class);
        interfaces.add(Identifiable.class);
        interfaces.add(SecuredObject.class);

        javassistProxyFactory.postInstantiate("Annotation", Annotation.class,
                interfaces, Annotation.class.getDeclaredMethod("getId"),
                Annotation.class.getDeclaredMethod("setId", Long.class), null);

        b3 = javassistProxyFactory.getProxy(1L, sessionImplementor);

        EasyMock.verify(sessionImplementor, sessionFactoryImplementor,
                entityPersister, persistenceContext);
        assertTrue("Equals should return true", b1.equals(b3));
    }
}
