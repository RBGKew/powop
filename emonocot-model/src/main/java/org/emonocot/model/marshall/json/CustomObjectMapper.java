package org.emonocot.model.marshall.json;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.emonocot.model.geography.GeographicalRegion;

/**
 *
 * @author ben
 *
 */
public class CustomObjectMapper extends ObjectMapper {

    /**
     *
     */
    public CustomObjectMapper() {
        SimpleModule module = new SimpleModule("GeographicalRegionModule",
                new Version(0, 1, 0, "alpha"));
        module.addKeyDeserializer(GeographicalRegion.class,
                new GeographicalRegionKeyDeserializer());
        registerModule(module);
    }
}
