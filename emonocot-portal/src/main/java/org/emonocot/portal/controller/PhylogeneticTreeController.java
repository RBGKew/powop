package org.emonocot.portal.controller;

import org.emonocot.api.PhylogeneticTreeService;
import org.emonocot.model.PhylogeneticTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author jk00kg
 */
@Controller
@RequestMapping("/phylo")
public class PhylogeneticTreeController extends GenericController<PhylogeneticTree, PhylogeneticTreeService> {

    private static Logger queryLog = LoggerFactory.getLogger("query");

    public PhylogeneticTreeController() {
        super("phylo");
    }

    /**
     * @param newIdentificationKeyService
     *            Set the identification key service
     */
    @Autowired
    public void setPhylogeneticTreeService(PhylogeneticTreeService newPhylogeneticTreeService) {
        super.setService(newPhylogeneticTreeService);
    }

    /**
     * @param identifier
     *            The identifier of the identification key
     * @param model
     *            The model
     * @return The name of the view
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {"text/html", "*/*"})
    public String getPage(@PathVariable Long id,
            Model model) {
        PhylogeneticTree tree = getService().load(id);
        model.addAttribute(tree); 
        queryLog.info("PhylogeneticTree: \'{}\'", new Object[] {id});
        return "phylo/show";
    }
}
