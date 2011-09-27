package org.emonocot.model.marshall.json;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.module.SimpleKeyDeserializers;
import org.emonocot.model.geography.GeographicalRegion;

/**
 *
 * @author ben
 *
 */
public class CustomModule extends Module {

    @Override
    public final String getModuleName() {
        return "eMonocotModule";
    }

    @Override
    public final void setupModule(final SetupContext setupContext) {
        SimpleKeyDeserializers keyDeserializers = new SimpleKeyDeserializers();
        keyDeserializers.addDeserializer(GeographicalRegion.class,
                new GeographicalRegionKeyDeserializer());
        setupContext.addKeyDeserializers(keyDeserializers);
    }

    @Override
    public final Version version() {
        return new Version(0, 1, 0, "alpha");
    }

}
