package org.emonocot.checklist.view.assembler;

import org.hibernate.LazyInitializationException;

/**
 *
 * @author ben
 *
 */
public class HibernateProxyResolver extends
        org.dozer.util.HibernateProxyResolver {
    /**
     * @param object the hibernate proxy
     * @param <T> the type of object
     * @return an object or null if we can't unenhance it
     */
    @Override
    public final <T> T unenhanceObject(final T object) {
        try {
            return super.unenhanceObject(object);
        } catch (LazyInitializationException lie) {
            Class<T> clazz = super.unenhanceClass(object);
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }
    }

}
