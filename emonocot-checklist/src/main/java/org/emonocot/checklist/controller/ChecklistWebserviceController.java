package org.emonocot.checklist.controller;

import java.util.List;

import org.emonocot.checklist.model.Taxon;
import org.emonocot.checklist.persistence.TaxonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/endpoint") // TODO defined endpoint uri
public class ChecklistWebserviceController {
	
	private TaxonDao taxonDao;
	
	@Autowired
	public void setTaxonDao(TaxonDao taxonDao) {
		this.taxonDao = taxonDao;
	}

	/**
	 * Simple method that allows the client to determine whether the service is up and running
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, params = {"!function"})
	public ModelAndView ping() {
		return new ModelAndView("rdfResponse");
	}
	
	/**
	 * Method which searches for taxon objects who's names match the search term exactly
	 * @param search
	 * @return a list of taxon objects (stored under the key 'result')
	 */
	@RequestMapping(method = RequestMethod.GET, params = {"function=search","search"})
    public ModelAndView search(@RequestParam("search") String search) {
		ModelAndView modelAndView = new ModelAndView("rdfResponse");
		List<Taxon> taxa = taxonDao.search(search);
		modelAndView.addObject("result",taxa);
		return modelAndView;
	}

	/**
	 * Method which returns a single taxon object with the specified identifier.
	 * @param id
	 * @return a taxon objects (stored under the key 'result')
	 * @throws a DataRetrievalFailureException if no taxon has the specified identifier
	 */
	@RequestMapping(method = RequestMethod.GET, params = {"function=details_tcs","id"})
    public ModelAndView get(@RequestParam("id") String id) {
	    ModelAndView modelAndView = new ModelAndView("tcsXmlResponse");
	    Taxon taxon = taxonDao.get(id);
		modelAndView.addObject("result",taxon);
		return modelAndView;
    }
	
	/**
	 * TaxonDao will throw a (wrapped) UnresolvableObjectException if the client provides an invalid
	 * identifier. This method returns a HTTP 400 BAD REQUEST status code and a short message
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(DataRetrievalFailureException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ModelAndView handleInvalidTaxonIdentifier(DataRetrievalFailureException exception) {
		ModelAndView modelAndView = new ModelAndView("exception");
		modelAndView.addObject("exception",exception);
		return modelAndView;
	}
}
