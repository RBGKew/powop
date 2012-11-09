package org.emonocot.model.hibernate;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.emonocot.model.Base;
//import org.hibernate.search.bridge.FieldBridge;
//import org.hibernate.search.bridge.LuceneOptions;

/**
 *
 * @author ben
 *
 */
public class AnnotatedObjBridge 
//implements FieldBridge 
{

//    /**
//     * @param name Set the name of the field
//     * @param value Set the value to be indexed
//     * @param document Set the lucene document
//     * @param luceneOptions Set the options for indexing
//     */
//    public final void set(final String name, final Object value,
//            final Document document, final LuceneOptions luceneOptions) {
//        if (value != null) {
//            Base base = (Base) value;
//            Field field = new Field(name + "Type",
//                    base.getClass().getSimpleName(),
//                    luceneOptions.getStore(),
//                    luceneOptions.getIndex(),
//                    luceneOptions.getTermVector());
//            field.setBoost(luceneOptions.getBoost());
//            document.add(field);
//        }
//    }

}
