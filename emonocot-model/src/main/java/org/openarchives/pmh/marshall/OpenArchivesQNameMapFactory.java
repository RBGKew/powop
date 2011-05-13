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
        qNameMap.setDefaultPrefix("oai");

        qNameMap.registerMapping(new QName(
                "http://www.openarchives.org/OAI/2.0/", "OAI-PMH"),
                OAIPMH.class);
        qNameMap.registerMapping(new QName(
                "http://www.openarchives.org/OAI/2.0/oai_dc/", "dc", "oai_dc"),
                "dc");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "title", "dc"),
                "dcTitle");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "creator", "dc"),
                "dcCreator");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "subject", "dc"),
                "dcSubject");
        qNameMap.registerMapping(new QName(
                "http://www.openarchives.org/OAI/2.0/oai_dc/", "publisher",
                "dc"), "dcPublisher");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "date", "dc"),
                "dcDate");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "format", "dc"),
                "dcFormat");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "identifier", "dc"),
                "dcIdentifier");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "source", "dc"),
                "dcSource");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "language", "dc"),
                "dcLanguage");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "coverage", "dc"),
                "dcCoverage");
        qNameMap.registerMapping(new QName(
                "http://purl.org/dc/elements/1.1/", "rights", "dc"),
                "dcRights");
        // TODO register other mappings
        return qNameMap;
    }

    @Override
    public final Class<?> getObjectType() {
        return QNameMap.class;
    }

}
