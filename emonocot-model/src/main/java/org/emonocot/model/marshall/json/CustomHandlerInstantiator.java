package org.emonocot.model.marshall.json;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.HandlerInstantiator;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.emonocot.api.GroupService;
import org.emonocot.api.IdentificationKeyService;
import org.emonocot.api.ImageService;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.SourceService;
import org.emonocot.api.TaxonService;
import org.emonocot.api.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author ben
 *
 */
public class CustomHandlerInstantiator extends HandlerInstantiator {
	
	Logger logger = LoggerFactory.getLogger(CustomHandlerInstantiator.class);

    /**
     *
     */
    private ReferenceService referenceService;

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    private ImageService imageService;

    /**
     *
     */
    private UserService userService;

    /**
     *
     */
    private GroupService groupService;

    /**
    *
    */
   private SourceService sourceService;
   
   /**
    *
    */
   private IdentificationKeyService identificationKeyService;

    /**
     * @param userService the userService to set
     */
    public final void setUserService(final UserService userService) {
        this.userService = userService;
    }

    /**
     * @param groupService the groupService to set
     */
    public final void setGroupService(final GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * @param newReferenceService the referenceService to set
     */
    public final void setReferenceService(
            final ReferenceService newReferenceService) {
        this.referenceService = newReferenceService;
    }

    /**
     * @param newTaxonService the taxonService to set
     */
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    /**
     * @param newImageService the imageService to set
     */
    public final void setImageService(final ImageService newImageService) {
        this.imageService = newImageService;
    }

    /**
     * @param newSourceService the sourceService to set
     */
    public final void setSourceService(final SourceService newSourceService) {
        this.sourceService = newSourceService;
    }
    
    /**
     * @param newIdentificationKeyService the identification key service to set
     */
    public final void setIdentificationKeyService(final IdentificationKeyService newIdentificationKeyService) {
        this.identificationKeyService = newIdentificationKeyService;
    }
    
    public CustomHandlerInstantiator() {
    	
    }

    @Override
    public final JsonDeserializer<?> deserializerInstance(
            final DeserializationConfig deserializerConfig,
            final Annotated annotated,
            final Class<? extends JsonDeserializer<?>> jsonDeserializerClass) {
    	logger.debug("deserializerInstance " +  deserializerConfig + " " + jsonDeserializerClass);
        try {
            if (jsonDeserializerClass.equals(TaxonDeserializer.class)) {
                TaxonDeserializer taxonDeserializer = TaxonDeserializer.class
                        .newInstance();
                taxonDeserializer.setService(taxonService);
                return taxonDeserializer;
            } else if (jsonDeserializerClass
                    .equals(ReferenceDeserializer.class)) {
                ReferenceDeserializer referenceDeserializer
                    = ReferenceDeserializer.class.newInstance();
                referenceDeserializer.setService(referenceService);
                return referenceDeserializer;
            } else if (jsonDeserializerClass
                    .equals(ImageDeserializer.class)) {
                ImageDeserializer imageDeserializer
                    = ImageDeserializer.class.newInstance();
                imageDeserializer.setService(imageService);
                return imageDeserializer;
            } else if (jsonDeserializerClass
                    .equals(UserDeserializer.class)) {
                UserDeserializer userDeserializer
                    = UserDeserializer.class.newInstance();
                userDeserializer.setService(userService);
                return userDeserializer;
            } else if (jsonDeserializerClass
                    .equals(GroupDeserializer.class)) {
                GroupDeserializer groupDeserializer
                    = GroupDeserializer.class.newInstance();
                groupDeserializer.setService(groupService);
                return groupDeserializer;
            } else if (jsonDeserializerClass
                    .equals(SourceDeserializer.class)) {
                SourceDeserializer sourceDeserializer
                    = SourceDeserializer.class.newInstance();
                sourceDeserializer.setService(sourceService);
                return sourceDeserializer;
            }   else if (jsonDeserializerClass
                        .equals(IdentificationKeyDeserializer.class)) {
                    IdentificationKeyDeserializer identificationKeyDeserializer
                        = IdentificationKeyDeserializer.class.newInstance();
                    identificationKeyDeserializer.setService(identificationKeyService);
                    return identificationKeyDeserializer;
            } else if (jsonDeserializerClass
                        .equals(PrincipalDeserializer.class)) {
                    PrincipalDeserializer principalDeserializer
                        = PrincipalDeserializer.class.newInstance();
                    principalDeserializer.setUserService(userService);
                    principalDeserializer.setGroupService(groupService);
                    return principalDeserializer;
            } else if (jsonDeserializerClass
                    .equals(AnnotatableObjectDeserializer.class)) {
                AnnotatableObjectDeserializer annotatableObjectDeserializer
                    = AnnotatableObjectDeserializer.class.newInstance();
                annotatableObjectDeserializer.addService(taxonService);
                annotatableObjectDeserializer.addService(imageService);
                annotatableObjectDeserializer.addService(referenceService);
                annotatableObjectDeserializer.addService(sourceService);
                return annotatableObjectDeserializer;
            }
        } catch (IllegalAccessException iae) {
            return null;
        } catch (InstantiationException ie) {
            return null;
        }
        return null;
    }

    @Override
    public final KeyDeserializer keyDeserializerInstance(
            final DeserializationConfig deserializationConfig,
            final Annotated annotated,
            final Class<? extends KeyDeserializer> keyDeserializerClass) {
        return null;
    }

    @Override
    public final JsonSerializer<?> serializerInstance(
            final SerializationConfig serializationConfig,
            final Annotated annotated,
            final Class<? extends JsonSerializer<?>> jsonSerializerClass) {
        logger.debug("serializerInstance " +  serializationConfig + " " + annotated + " " + jsonSerializerClass);
        return null;
    }

    @Override
    public final TypeIdResolver typeIdResolverInstance(
            final MapperConfig<?> mapperConfig, final Annotated annotated,
            final Class<? extends TypeIdResolver> typeIdResolverClass) {
        return null;
    }

    @Override
    public final TypeResolverBuilder<?> typeResolverBuilderInstance(
            final MapperConfig<?> mapperConfig, final Annotated annotated,
            final Class<? extends TypeResolverBuilder<?>>
                typeResolverBuilderClass) {
        return null;
    }
}
