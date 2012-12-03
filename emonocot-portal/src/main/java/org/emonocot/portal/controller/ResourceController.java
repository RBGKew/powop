package org.emonocot.portal.controller;

import org.emonocot.api.ResourceService;
import org.emonocot.model.registry.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/resource")
public class ResourceController extends GenericController<Resource, ResourceService> {

	/**
	 *
	 * @param resourceService Set the jobService;
	 */
	@Autowired
	public final void setResourceService(final ResourceService resourceService) {
		super.setService(resourceService);
	}
	
    /**
     *
     */
    public ResourceController() {
        super("resource");
    }

}
