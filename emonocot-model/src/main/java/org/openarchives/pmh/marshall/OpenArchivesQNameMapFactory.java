package org.openarchives.pmh.marshall;

import javax.xml.namespace.QName;

import org.openarchives.pmh.OAIPMH;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import com.thoughtworks.xstream.io.xml.QNameMap;

/**
 *
 * @author ben
 *
 */
public class OpenArchivesQNameMapFactory extends
        AbstractFactoryBean<QNameMap> {

    /**
     * Returns a QNameMap which maps the namespace:name couplets on
     * to object types for the OAI PMH Schema.
     *
     * @return a map containing qnames from the oai-pmh schema
     * namespace.
     */
    @Override
    protected final QNameMap createInstance() {
        QNameMap qNameMap = new QNameMap();
        qNameMap.setDefaultNamespace("http://www.openarchives.org/OAI/2.0/");

        qNameMap.registerMapping(new QName(
                "http://www.openarchives.org/OAI/2.0/", "OAI-PMH"),
                OAIPMH.class);
        // TODO register other mappings
        return qNameMap;
    }

    @Override
    public final Class<?> getObjectType() {
        return QNameMap.class;
    }

}
