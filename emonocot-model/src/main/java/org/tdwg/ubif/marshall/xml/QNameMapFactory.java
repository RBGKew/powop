package org.tdwg.ubif.marshall.xml;

import javax.xml.namespace.QName;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.tdwg.ubif.TaxonName;

import com.thoughtworks.xstream.io.xml.QNameMap;

/**
 *
 * @author ben
 *
 */
public class QNameMapFactory extends
        AbstractFactoryBean<QNameMap> {

    /**
     * Returns a QNameMap which maps the namespace:name couplets on
     * to object types for the OAI PMH Schema.
     *
     * @return a map containing qnames from the oai-pmh schema
     * namespace.
     */
    @Override
    public final QNameMap createInstance() {
        QNameMap qNameMap = new QNameMap();
        qNameMap.setDefaultNamespace("http://rs.tdwg.org/UBIF/2006/");
        qNameMap.registerMapping(new QName("http://rs.tdwg.org/UBIF/2006/", "TaxonName"),
                TaxonName.class);
        return qNameMap;
    }

    @Override
    public final Class<?> getObjectType() {
        return QNameMap.class;
    }

}
