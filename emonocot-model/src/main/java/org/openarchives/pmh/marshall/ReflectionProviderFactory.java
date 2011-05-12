package org.openarchives.pmh.marshall;

import org.openarchives.pmh.Header;
import org.openarchives.pmh.Identify;
import org.openarchives.pmh.OAIPMH;
import org.openarchives.pmh.Record;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.SortableFieldKeySorter;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

/**
 *
 * @author ben
 *
 */
public class ReflectionProviderFactory extends
        AbstractFactoryBean<ReflectionProvider> {
    /**
     *
     */
    private static final String[] OAI_PMH_FIELDS = new String[] {
        "responseDate",
        "request",
        "error",
        "identify",
        "listMetadataFormats",
        "listSets",
        "getRecord",
        "listIdentifiers",
        "listRecords"};

   /**
    *
    */
   private static final String[] RECORD_FIELDS = new String[] {
       "header",
       "metadata",
       "about" };

   /**
    *
    */
   private static final String[] HEADER_FIELDS = new String[] {
           "status",
           "identifier",
           "datestamp",
           "setSpec" };

   /**
    *
    */
   private static final String[] IDENTIFY_FIELDS = new String[] {
       "repositoryName",
       "baseURL",
       "protocolVersion",
       "adminEmail",
       "earliestDatestamp",
       "deletedRecord",
       "granularity",
       "compression",
       "description" };

    /**
     * Returns a ReflectionProvider that provides core reflection services.
     *
     * @return configured reflection provider.
     */
    @Override
    protected final ReflectionProvider createInstance() {
        SortableFieldKeySorter sorter = new SortableFieldKeySorter();

        sorter.registerFieldOrder(OAIPMH.class,
                ReflectionProviderFactory.OAI_PMH_FIELDS);
        sorter.registerFieldOrder(Record.class,
                ReflectionProviderFactory.RECORD_FIELDS);
        sorter.registerFieldOrder(Header.class,
                ReflectionProviderFactory.HEADER_FIELDS);
        sorter.registerFieldOrder(Identify.class,
                ReflectionProviderFactory.IDENTIFY_FIELDS);

        FieldDictionary fieldDictionary = new FieldDictionary(sorter);

        ReflectionProvider reflectionProvider
            = new Sun14ReflectionProvider(fieldDictionary);
        return reflectionProvider;
    }

    @Override
    public final Class<?> getObjectType() {
        return ReflectionProvider.class;
    }

}
