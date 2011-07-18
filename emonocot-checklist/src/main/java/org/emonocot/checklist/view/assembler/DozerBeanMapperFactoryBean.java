package org.emonocot.checklist.view.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dozer.BeanFactory;
import org.dozer.CustomConverter;
import org.dozer.CustomFieldMapper;
import org.dozer.DozerBeanMapper;
import org.dozer.DozerEventListener;
import org.dozer.Mapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * extended version of Sören Chittka's DozerBeanMapperFactoryBean, allowing
 * other properties to be set.
 *
 * @author Sören Chittka
 */
public class DozerBeanMapperFactoryBean implements FactoryBean<Mapper>,
        InitializingBean {
    /**
     *
     */
    private DozerBeanMapper beanMapper;

    /**
     *
     */
    private Resource[] mappingFiles;

    /**
     *
     */
    private List<CustomConverter> customConverters;

    /**
     *
     */
    private Map<String, CustomConverter> customConvertersWithId;

    /**
     *
     */
    private List<DozerEventListener> eventListeners;

    /**
     *
     */
    private Map<String, BeanFactory> factories;

    /**
     *
     */
    private CustomFieldMapper customFieldMapper;

    /**
     *
     * @param newMappingFiles Set the mapping files
     */
    public final void setMappingFiles(final Resource[] newMappingFiles) {
        this.mappingFiles = newMappingFiles;
    }

    /**
     *
     * @param newCustomConverters Set the custom converters
     */
    public final void setCustomConverters(
            final List<CustomConverter> newCustomConverters) {
        this.customConverters = newCustomConverters;
    }

    /**
     *
     * @param newEventListeners Set the event listeners
     */
    public final void setEventListeners(
            final List<DozerEventListener> newEventListeners) {
        this.eventListeners = newEventListeners;
    }

    /**
     *
     * @param newFactories Set the bean factories
     */
    public final void setFactories(
            final Map<String, BeanFactory> newFactories) {
        this.factories = newFactories;
    }

    /**
     *
     * @param newCustomFieldMapper Set a custom field mapper
     */
    public final void setCustomFieldMapper(
            final CustomFieldMapper newCustomFieldMapper) {
        this.customFieldMapper = newCustomFieldMapper;
    }

    /**
     *
     * @param newCustomConverters Set identified custom converters
     */
    public final void setCustomConvertersWithId(
            final Map<String, CustomConverter> newCustomConverters) {
        this.customConvertersWithId = newCustomConverters;
    }

    /**
     * @throws Exception if there is a problem
     * @return the initialized beanmapper
     */
    public final Mapper getObject() throws Exception {
        return this.beanMapper;
    }

    /**
     * @return the type of object produced by this factory bean
     */
    public final Class<Mapper> getObjectType() {
        return Mapper.class;
    }

    /**
     * @return true if this bean is a singleton
     */
    public final boolean isSingleton() {
        return true;
    }

    /**
     * @throws Exception if there is a problem initialising this bean
     */
    public final void afterPropertiesSet() throws Exception {
        this.beanMapper = new DozerBeanMapper();

        if (this.mappingFiles != null) {
            final List<String> mappings = new ArrayList<String>(
                    this.mappingFiles.length);
            for (Resource mappingFile : this.mappingFiles) {
                mappings.add(mappingFile.getURL().toString());
            }
            this.beanMapper.setMappingFiles(mappings);
        }
        if (this.customConverters != null) {
            this.beanMapper.setCustomConverters(this.customConverters);
        }
        if (this.eventListeners != null) {
            this.beanMapper.setEventListeners(this.eventListeners);
        }
        if (this.factories != null) {
            this.beanMapper.setFactories(this.factories);
        }

        if (this.customFieldMapper != null) {
            this.beanMapper.setCustomFieldMapper(customFieldMapper);
        }

        if (this.customConvertersWithId != null) {
            this.beanMapper.setCustomConvertersWithId(customConvertersWithId);
        }
    }

}
