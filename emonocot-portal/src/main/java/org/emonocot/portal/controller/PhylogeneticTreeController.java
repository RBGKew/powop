package org.emonocot.portal.controller;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.emonocot.api.PhylogeneticTreeService;
import org.emonocot.model.PhylogeneticTree;
import org.forester.io.parsers.PhylogenyParser;
import org.forester.io.parsers.phyloxml.PhyloXmlParser;
import org.forester.io.writers.PhylogenyWriter;
import org.forester.io.writers.PhylogenyWriter.FORMAT;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode.NH_CONVERSION_SUPPORT_VALUE_STYLE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @RequestMapping(value = "/{id}", params = "!download", method = RequestMethod.GET, produces = {"text/html", "*/*"})
    public String getPage(@PathVariable Long id,
            Model model) {
        PhylogeneticTree tree = getService().load(id);
        model.addAttribute(tree); 
        queryLog.info("PhylogeneticTree: \'{}\'", new Object[] {id});
        return "phylo/show";
    }
    
    @RequestMapping(value = "/{id}", params = "download", method = RequestMethod.GET, produces = {"text/html", "*/*"})
    public String getDownloadPage(@PathVariable Long id, Model model) {
        PhylogeneticTree tree = getService().load(id);
        model.addAttribute(tree); 
        model.addAttribute("formats", Arrays.asList(FORMAT.values()));
        return "phylo/download";
    }
    
    @RequestMapping(value = "/{id}", params = {"download", "format!=PHYLO_XML"}, method = RequestMethod.POST, produces = "text/plain")
    public @ResponseBody String getDownload(@PathVariable Long id,
    		                                @RequestParam(value = "format", required = false, defaultValue = "NH") FORMAT format,
    		                                @RequestParam(value = "purpose", required = false) String purpose,
    		                                Model model) throws Exception {
        PhylogeneticTree tree = getService().load(id);        
        String output = null;
        PhylogenyParser parser = new PhyloXmlParser();
		parser.setSource(new ByteArrayInputStream(tree.getPhylogeny().getBytes()));
		Phylogeny phylogeny = parser.parse()[0];
    	PhylogenyWriter phylogenyWriter = PhylogenyWriter.createPhylogenyWriter();
    	StringBuffer stringBuffer = null;
        switch (format) {
        case NEXUS:        	
    		stringBuffer = phylogenyWriter.toNexus(phylogeny, NH_CONVERSION_SUPPORT_VALUE_STYLE.IN_SQUARE_BRACKETS);
    		output = stringBuffer.toString();
        	break;
        case NHX:
        	stringBuffer = phylogenyWriter.toNewHampshireX(phylogeny);
    		output = stringBuffer.toString();
        	break;
        case NH:
        default:
    		stringBuffer = phylogenyWriter.toNewHampshire(phylogeny, false, tree.getHasBranchLengths());
    		output = stringBuffer.toString();        	
        	break;        
        }
        queryLog.info("DownloadPhylogeny: \'{}\', format: {}, purpose: {}", new Object[] {id, format, purpose});
        return output;
    }
    
    @RequestMapping(value = "/{id}", params = {"download", "format=PHYLO_XML"}, method = RequestMethod.POST, produces = "application/xml")
    public @ResponseBody String getDownloadPhyloXml(@PathVariable Long id,
    		                                @RequestParam(value = "format", required = false, defaultValue = "NH") FORMAT format,
    		                                @RequestParam(value = "purpose", required = false) String purpose,
    		                                Model model) throws Exception {
        PhylogeneticTree tree = getService().load(id);
        queryLog.info("DownloadPhylogeny: \'{}\', format: {}, purpose: {}", new Object[] {id, format, purpose});
        return tree.getPhylogeny();
    }

}
