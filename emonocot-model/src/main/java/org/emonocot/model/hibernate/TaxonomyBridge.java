package org.emonocot.model.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
import org.emonocot.model.Taxon;
//import org.hibernate.search.bridge.FieldBridge;
//import org.hibernate.search.bridge.LuceneOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Method of adding the "family" facet to images. Could be extended to include
 * other objects such as specimens which should be considered to be related to a
 * taxonomic family
 * 
 * @author ben
 */
public class TaxonomyBridge
//implements FieldBridge 
{

    private static Logger logger = LoggerFactory
            .getLogger(TaxonomyBridge.class);

//    /**
//     * @param name
//     *            Set the name of the field
//     * @param value
//     *            Set the value to be indexed
//     * @param document
//     *            Set the lucene document
//     * @param luceneOptions
//     *            Set the options for indexing
//     */
//    public final void set(final String name, final Object value,
//            final Document document, final LuceneOptions luceneOptions) {
//
//        Taxon taxon = reflectiveGetTaxon(value);
//        if (taxon != null && taxon.getFamily() != null
//                && taxon.getFamily().trim().length() > 0) {
//            Field familyField = new Field("family", taxon.getFamily(),
//                    luceneOptions.getStore(), luceneOptions.getIndex(),
//                    luceneOptions.getTermVector());
//            familyField.setBoost(luceneOptions.getBoost());
//            document.add(familyField);
//        }
//    }

    /**
     * @param o
     *            A javabean with an accessible getTaxon method
     * @return The return object from the invocation or null if known exceptions
     *         are thrown
     */
    public Taxon reflectiveGetTaxon(Object o) {
        Taxon taxon = null;
        Method[] allMethods = o.getClass().getDeclaredMethods();
        for (Method m : allMethods) {
            if (m.getName().equals("getTaxon")
                    && (m.getReturnType().equals(Taxon.class))) {
                logger.debug("invoking %s()%n", m.getName());
                try {
                    Object t = m.invoke(o);
                    taxon = (Taxon) t;
                    return taxon;
                } catch (IllegalArgumentException e) {
                    logger.error("Error attempting to get taxon from " + o, e);
                } catch (IllegalAccessException e) {
                    logger.error("Error attempting to get taxon from " + o, e);
                } catch (InvocationTargetException e) {
                    logger.error("Error attempting to get taxon from " + o, e);
                } catch (ClassCastException e) {
                    logger.error("Unable to cast a taxon from " + o, e);
                }
            }
        }
        return taxon;
    }

}
