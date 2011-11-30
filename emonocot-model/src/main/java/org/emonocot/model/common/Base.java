package org.emonocot.model.common;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.ObjectUtils;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.annotations.NaturalId;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.TokenizerDef;

/**
 *
 * @author ben
 *
 */
@MappedSuperclass
@AnalyzerDef(name = "facetAnalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class))
public abstract class Base implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4778611345983453363L;
    /**
     *
     */
    private String identifier;

    /**
     *
     * @return The unique identifier of the object
     */
    @Field(analyzer = @Analyzer(
            definition =  "facetAnalyzer"), index = Index.UN_TOKENIZED)
    @NaturalId
    public String getIdentifier() {
        return identifier;
    }

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

    /**
     *
     * @param identifier
     *            Set the unique identifier of the object
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
