package org.emonocot.model.marshall.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.emonocot.api.GroupService;
import org.emonocot.api.ImageService;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.SourceService;
import org.emonocot.api.TaxonService;
import org.emonocot.api.UserService;

/**
*
*/
public class CustomObjectMapper extends ObjectMapper {
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
     *
     * @param newReferenceService
     *            Set the reference service
     */
    public final void setReferenceService(
            final ReferenceService newReferenceService) {
        this.referenceService = newReferenceService;
    }

    /**
     *
     * @param newTaxonService Set the taxon service
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
    *
    */
    protected CustomObjectMapper() {
    }

    /**
    *
    */
    protected final void init() {
        CustomHandlerInstantiator handlerInstantiator
            = new CustomHandlerInstantiator();
        handlerInstantiator.setReferenceService(referenceService);
        handlerInstantiator.setTaxonService(taxonService);
        handlerInstantiator.setImageService(imageService);
        handlerInstantiator.setGroupService(groupService);
        handlerInstantiator.setUserService(userService);
        handlerInstantiator.setSourceService(sourceService);
        setHandlerInstantiator(handlerInstantiator);
        CustomModule module = new CustomModule();
        registerModule(module);
    }
}
