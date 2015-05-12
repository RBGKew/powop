/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.model.marshall.json.hibernate;

import java.util.*;

import org.emonocot.model.marshall.json.hibernate.HibernateModule.Feature;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentMap;
import org.hibernate.proxy.HibernateProxy;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.ser.*;

public class HibernateSerializers extends Serializers.Base
{
    protected final int _moduleFeatures;
    
    public HibernateSerializers(int features)
    {
        _moduleFeatures = features;
    }

    @Override
    public JsonSerializer<?> findSerializer(
            SerializationConfig config, JavaType type,
            BeanDescription beanDesc)
    {
        Class<?> raw = type.getRawClass();

        /* Note: PersistentCollection does not implement Collection, so we
         * may get some types here...
         */
        if (PersistentCollection.class.isAssignableFrom(raw)) {
            // TODO: handle iterator types? Or PersistentArrayHolder?
        }
        
        if (HibernateProxy.class.isAssignableFrom(raw)) {
            return new HibernateProxySerializer(isEnabled(Feature.FORCE_LAZY_LOADING));
        }
        return null;
    }

//    @Override
//    public JsonSerializer<?> findCollectionSerializer(SerializationConfig config,
//            CollectionType type, BeanDescription beanDesc,
//            TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer)
//    {
//        Class<?> raw = type.getRawClass();
//        // only handle PersistentCollection style collections...
//        if (PersistentCollection.class.isAssignableFrom(raw)) {
//            /* And for those, figure out "fallback type"; we MUST have some idea of
//             * type to deserialize, aside from nominal PersistentXxx type.
//             */
//            return new PersistentCollectionSerializer(property, _figureFallbackType(config, type),
//                    isEnabled(Feature.FORCE_LAZY_LOADING));
//        }
//        return null;
//    }
//
//    @Override
//    public JsonSerializer<?> findMapSerializer(SerializationConfig config,
//            MapType type, BeanDescription beanDesc,
//            JsonSerializer<Object> keySerializer,
//            TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer)
//    {
//        Class<?> raw = type.getRawClass();
//        if (PersistentMap.class.isAssignableFrom(raw)) {
//            return new PersistentCollectionSerializer(property, _figureFallbackType(config, type),
//                    isEnabled(Feature.FORCE_LAZY_LOADING));
//        }
//        return null;
//    }
    
    public final boolean isEnabled(HibernateModule.Feature f) {
        return (_moduleFeatures & f.getMask()) != 0;
    }

    protected JavaType _figureFallbackType(SerializationConfig config,
            JavaType persistentType)
    {
        // Alas, PersistentTypes are NOT generics-aware... meaning can't specify parameterization
        Class<?> raw = persistentType.getRawClass();
        TypeFactory tf = config.getTypeFactory();
        if (Map.class.isAssignableFrom(raw)) {
            return tf.constructMapType(Map.class, Object.class, Object.class);
        }
        if (List.class.isAssignableFrom(raw)) {
            return tf.constructCollectionType(List.class, Object.class);
        }
        if (Set.class.isAssignableFrom(raw)) {
            return tf.constructCollectionType(Set.class, Object.class);
        }
        // ok, just Collection of some kind
        return tf.constructCollectionType(Collection.class, Object.class);
    }
}

