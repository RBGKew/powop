package org.emonocot.checklist.view;

import java.util.Map;

import org.emonocot.checklist.model.Author;
import org.emonocot.checklist.model.AuthorType;
import org.hibernate.Hibernate;

/**
 *
 * @author ben
 *
 */
public final class Functions {
    /**
     *
     */
    private Functions() {
    }

    /**
     *
     * @param string Set the string to escape
     * @return an escaped string
     */
    public static String escape(final String string) {
        return string.replaceAll("&", "&amp;");
    }
    
    /**
    *
    * @param string Set the string to escape
    * @return an escaped string
    */
   public static String authorship(final Map<AuthorType, Author> authors) {
       if (!Hibernate.isInitialized(authors)) {
           return null;
       }
       
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

       if (authors.containsKey(AuthorType.PRM)) {
           stringBuffer.append(authors.get(AuthorType.PRM).getName());
       }
       return stringBuffer.toString();
   }
}
