package org.emonocot.job.checklist;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.thoughtworks.xstream.io.xml.QNameMap;

/**
 *
 * @author ben
 *
 */
public class OaiPmhResponseQNameMapFactory extends
        AbstractFactoryBean<QNameMap> {

    @Override
    protected final QNameMap createInstance() throws Exception {
        QNameMap qNameMap = new QNameMap();
        return qNameMap;
    }

    @Override
    public final Class<?> getObjectType() {
        return QNameMap.class;
    }

}
