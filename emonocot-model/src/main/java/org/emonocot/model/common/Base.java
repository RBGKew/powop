package org.emonocot.model.common;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.ObjectUtils;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.TokenizerDef;

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
    private static final long serialVersionUID = 4778611345983453363L;
    /**
     *
     */
    protected String identifier;

    @Override
    public boolean equals(Object other) {
        // check for self-comparison
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        Base base = (Base) other;
        return ObjectUtils.equals(this.identifier, base.identifier);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(this.identifier);
    }

}
