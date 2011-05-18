package org.tdwg.voc;

import java.util.HashSet;
import java.util.Set;

import org.tdwg.DefinedTerm;
import org.tdwg.Description;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 *
 * @author ben
 *
 */
public class InfoItem extends Description {

    /**
     *
     */
    private String spmCategory;

    /**
     *
     */
    @XStreamImplicit(itemFieldName = "spmContextValue")
    private Set<DefinedTermLinkType> spmContextValues = null;

    /**
     *
     */
    @XStreamImplicit(itemFieldName = "spmHasValue")
    private Set<DefinedTermLinkType> spmHasValues = null;

    /**
     *
     * @return the category of this information
     */
    public final String getCategory() {
        return spmCategory;
    }

    /**
     *
     * @param definedTerm Set the category of this information
     */
    public final void setCategory(final String definedTerm) {
        this.spmCategory = definedTerm;
    }

    /**
     *
     * @return the context of this information
     */
    public final Set<DefinedTerm> getContext() {
        if (spmContextValues != null) {
            Set<DefinedTerm> definedTerms = new HashSet<DefinedTerm>();
            for (DefinedTermLinkType contextValue : spmContextValues) {
                definedTerms.add(contextValue.getDefinedTerm());
            }
            return definedTerms;
        } else {
            return null;
        }
    }

    /**
     *
     * @param definedTerms set the context of this info item
     */
    public final void setContext(final Set<DefinedTerm> definedTerms) {
        if (definedTerms != null) {
            this.spmContextValues = new HashSet<DefinedTermLinkType>();
            for (DefinedTerm definedTerm : definedTerms) {
                spmContextValues.add(
                        new DefinedTermLinkType(definedTerm, false));
            }
        } else {
            spmContextValues = null;
        }
    }

    /**
     *
     * @param definedTerm Add a term defining the context of this
     *                    information to this info item
     */
    public final void addContextValue(final DefinedTerm definedTerm) {
        if (definedTerm != null) {
            this.spmContextValues = new HashSet<DefinedTermLinkType>();
            spmContextValues.add(new DefinedTermLinkType(definedTerm, false));
        } else {
            spmContextValues = null;
        }
    }

    /**
     *
     * @return the values in this info item
     */
    public final Set<DefinedTerm> getHasValue() {
        if (spmHasValues != null) {
            Set<DefinedTerm> definedTerms = new HashSet<DefinedTerm>();
            for (DefinedTermLinkType contextValue : spmHasValues) {
                definedTerms.add(contextValue.getDefinedTerm());
            }
            return definedTerms;
        } else {
            return null;
        }
    }

    /**
     *
     * @param definedTerms Set the values in this info item
     */
    public final void setHasValue(final Set<DefinedTerm> definedTerms) {
        if (definedTerms != null) {
            this.spmHasValues = new HashSet<DefinedTermLinkType>();
            for (DefinedTerm definedTerm : definedTerms) {
                spmHasValues.add(new DefinedTermLinkType(definedTerm, false));
            }
        } else {
            spmHasValues = null;
        }
    }

    /**
     *
     * @param definedTerm Add a value to this info item
     */
    public final void addValue(final DefinedTerm definedTerm) {
        if (definedTerm != null) {
            if (spmHasValues == null) {
                this.spmHasValues = new HashSet<DefinedTermLinkType>();
            }
            spmHasValues.add(new DefinedTermLinkType(definedTerm, true));
        }
    }
}
