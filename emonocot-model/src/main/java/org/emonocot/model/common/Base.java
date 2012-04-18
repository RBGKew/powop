package org.emonocot.model.common;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.ObjectUtils;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
@MappedSuperclass
@AnalyzerDef(name = "facetAnalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class))
public abstract class Base implements Serializable, Identifiable, SecuredObject {
    
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(Base.class);

    /**
     *
     */
    private static final long serialVersionUID = 4778611345983453363L;
    /**
     *
     */
    protected String identifier;

    @Override
    public boolean equals(Object other) {
        // check for self-comparison
        if (this == other) {
            //logger.info("this == other, returning true");
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            //logger.info("other == null || other.getClass() != this.getClass(), returning false");
            return false;
        }
        Base base = (Base) other;
        //logger.info("ObjectUtils.equals(this.identifier, base.identifier) |" + this.identifier + " | " + base.identifier + " returning " + ObjectUtils.equals(this.identifier, base.identifier));
        if (this.identifier == null && base.identifier == null) {
            if (this.getId() != null && base.getId() != null) {
                return ObjectUtils.equals(this.getId(), base.getId());
            } else {
                return false;
            }
        }
        return ObjectUtils.equals(this.identifier, base.identifier);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(this.identifier);
    }

}
