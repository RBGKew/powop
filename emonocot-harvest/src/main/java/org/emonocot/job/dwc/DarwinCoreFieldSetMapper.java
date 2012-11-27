package org.emonocot.job.dwc;

import java.util.Arrays;
import java.util.Map;

import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.model.Base;
import org.gbif.dwc.terms.TermFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 * @param <T> the type of object which this class maps
 */
public abstract class DarwinCoreFieldSetMapper<T extends Base> extends AuthorityAware implements
        FieldSetMapper<T> {
    /**
     *
     */
    private Logger logger = LoggerFactory
            .getLogger(DarwinCoreFieldSetMapper.class);

    /**
     *
     */
    private String[] fieldNames;

    /**
     *
     */
    private Class<T> type;

   /**
    *
    * @param object the object to map onto
    * @param fieldName the name of the field
    * @param value the value to map
    * @throws BindException if there is a problem mapping
    *         the value to the object
    */
    public abstract void mapField(final T object, final String fieldName,
            final String value) throws BindException;

    /**
     *
     */
    private Map<String, String> defaultValues;

    /**
     *
     */
    private TermFactory termFactory = new TermFactory();

    /**
     *
     * @param newFieldNames Set the names of the fields
     */
    public final void setFieldNames(final String[] newFieldNames) {
        this.fieldNames = newFieldNames;
    }

   /**
    *
    * @param newDefaultValues Set the defaultValues of the fields
    */
    public final void setDefaultValues(
            final Map<String, String> newDefaultValues) {
        this.defaultValues = newDefaultValues;
    }

    /**
     *
     * @param newType Set the type
     */
    public DarwinCoreFieldSetMapper(final Class<T> newType) {
        this.type = newType;
    }

    /**
     * @param fieldSet Set the field set
     * @return an object
     * @throws BindException if there is a problem binding
     *         the values to the object
     */
    public final T mapFieldSet(final FieldSet fieldSet) throws BindException {
        T t;
        try {
            t = type.newInstance();
        } catch (InstantiationException e) {
            BindException be = new BindException(null, "target");
            be.reject("could not instantiate", e.getMessage());
            throw be;
        } catch (IllegalAccessException e) {
            BindException be = new BindException(null, "target");
            be.reject("could not instantiate", e.getMessage());
            throw be;
        }
        logger.debug("Mapping object " + t + " with fieldNames " + Arrays.toString(fieldNames) + " and fieldSet " + fieldSet);
        try {
          for (int i = 0; i < fieldNames.length; i++) {
              mapField(t, fieldNames[i], fieldSet.readString(i));
          }
          for (String defaultTerm : defaultValues.keySet()) {
            mapField(t, defaultTerm, defaultValues.get(defaultTerm));
          }
        } catch (BindException e) {
            logger.error(e.getMessage());
            throw e;
        }
        logger.debug("Returning object " + t);
        return t;
    }

    /**
     *
     * @return a term factory instance
     */
    public final TermFactory getTermFactory() {
        return termFactory;
    }
}
