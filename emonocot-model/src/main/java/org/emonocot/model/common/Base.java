package org.emonocot.model.common;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.ObjectUtils;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.proxy.HibernateProxyHelper;
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
@AnalyzerDef(name = "facetAnalyzer", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class))
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
            return true;
        }
        if (other == null) {
            return false;
        }
        // Only works when classes are instantiated
        if ((other.getClass().equals(this.getClass()))) {

            Base base = (Base) other;
            if (this.getIdentifier() == null && base.getIdentifier() == null) {

                if (this.getId() != null && base.getId() != null) {
                    return ObjectUtils.equals(this.getId(), base.getId());
                } else {
                    return false;
                }
            } else {
                return ObjectUtils.equals(this.identifier, base.identifier);
            }
        } else if (HibernateProxyHelper.getClassWithoutInitializingProxy(other)
                .equals(this.getClass())) {
            // Case to check when proxies are involved
            Identifiable base = (Identifiable) other;

            if (this.getIdentifier() == null && base.getIdentifier() == null) {
                return false;
            } else {
                return ObjectUtils
                        .equals(this.identifier, base.getIdentifier());
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(this.identifier);
    }

}
