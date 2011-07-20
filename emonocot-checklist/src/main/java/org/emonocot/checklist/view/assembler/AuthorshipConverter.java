package org.emonocot.checklist.view.assembler;

import java.util.Map;

import org.dozer.ConfigurableCustomConverter;
import org.emonocot.checklist.model.Author;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.checklist.model.AuthorType;
import org.hibernate.Hibernate;

/**
 *
 * @author ben
 *
 */
public class AuthorshipConverter implements ConfigurableCustomConverter {

    /**
     *
     */
    private String parameter;

    @Override
    public final Object convert(final Object destinationFieldValue,
            final Object sourceFieldValue, final Class destinationClass,
            final Class sourceClass) {
        if (sourceFieldValue == null) {
            return null;
        }
        Map<AuthorType, Author> authors
        = ((Taxon) sourceFieldValue).getAuthors();

        if (!Hibernate.isInitialized(authors)) {
            return null;
        }

        if (parameter.equals("basionymAuthorship")) {
            if (authors.containsKey(AuthorType.PAR)) {
                return authors.get(AuthorType.PAR).getName();
            } else if (authors.containsKey(AuthorType.RPL)) {
                return authors.get(AuthorType.RPL).getName();
            } else {
                return null;
            }
        } else if (parameter.equals("combinationAuthorship")) {
            if (authors.containsKey(AuthorType.PRI)) {
                return authors.get(AuthorType.PRI).getName();
            } else {
                return null;
            }
        } else if (parameter.equals("authorship")) {
            StringBuffer stringBuffer = new StringBuffer();

            if (authors.containsKey(AuthorType.PAR)) {
                stringBuffer.append("(");
                stringBuffer.append(authors.get(AuthorType.PAR).getName());
                stringBuffer.append(") ");
            } else if (authors.containsKey(AuthorType.RPL)) {
                stringBuffer.append("(");
                stringBuffer.append(authors.get(AuthorType.RPL).getName());
                stringBuffer.append(") ");
            }

            if (authors.containsKey(AuthorType.PRI)) {
                stringBuffer.append(authors.get(AuthorType.PRI).getName());
            }
            return stringBuffer.toString();
        } else {
            return null;
        }
    }

    @Override
    public final void setParameter(final String newParameter) {
        this.parameter = newParameter;
    }

}
