package org.emonocot.portal.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.emonocot.api.ResourceService;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.job.DarwinCorePropertyMap;
import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.model.SearchableObject;
import org.emonocot.model.registry.Resource;
import org.emonocot.pager.Page;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping(value = "/download")
public class DownloadController {	

    private static Logger queryLog = LoggerFactory.getLogger("query");

    private static Logger logger = LoggerFactory.getLogger(DownloadController.class);
    
    private Integer downloadLimit = 10000;

    private SearchableObjectService searchableObjectService;
    
    private ResourceService resourceService;
    
    private JobLauncher jobLauncher;
	
	private JobExplorer jobExplorer;
	
	private MessageSource messageSource;
	
	public void setDownloadLimit(Integer downloadLimit) {
		this.downloadLimit = downloadLimit;
	}

    @Autowired
    public void setSearchableObjectService(SearchableObjectService searchableObjectService) {
        this.searchableObjectService = searchableObjectService;
    }
    
    @Autowired
    public void setResourceService(ResourceService resourceService) {
    	this.resourceService = resourceService;
    }
    
    @Autowired
	@Qualifier("readOnlyJobLauncher")
	public void setJobLauncher(JobLauncher jobLauncher) {
	   this.jobLauncher = jobLauncher;
	}
    
    @Autowired
    public void setJobExplorer(JobExplorer jobExplorer) {
    	this.jobExplorer = jobExplorer;
    }
    
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
    	this.messageSource = messageSource;
    }
    						
    /**
    *
    * @param query
    *            Set the query
    * @param limit
    *            Limit the number of returned results
    * @param start
    *            Set the offset
    * @param facets
    *            The facets to set
    * @param view Set the view
    * @param model Set the model
    *
    * @return a model and view
    */
   @RequestMapping(method = RequestMethod.GET)
   public String search(
       @RequestParam(value = "query", required = false) String query,       
       @RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
       @RequestParam(value = "sort", required = false) String sort,
       Model model) {

       Map<String, String> selectedFacets = null;
       if (facets != null && !facets.isEmpty()) {
           selectedFacets = new HashMap<String, String>();
           for (FacetRequest facetRequest : facets) {
               selectedFacets.put(facetRequest.getFacet(),
                       facetRequest.getSelected());
           }
       }
       

       //Run the search
       Page<? extends SearchableObject> result = searchableObjectService.search(query, null, 10, 0, null, null, selectedFacets, sort, null);

       result.setSort(sort);
       model.addAttribute("taxonTerms", DarwinCorePropertyMap.getConceptTerms(DwcTerm.Taxon));
       model.addAttribute("result", result);
       if(result.getSize() > downloadLimit) {
    	   String[] codes = new String[] { "download.truncated" };
		   Object[] args = new Object[] { result.getSize(), downloadLimit };
		   DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
					codes, args);
		   model.addAttribute("warn",message);
       }

       return "download/download";
   }
   
   @RequestMapping(method = RequestMethod.POST, produces = "text/html", params = "download")
   public String download(
		   @RequestParam(value = "query", required = false) String query,       
	       @RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
	       @RequestParam(value = "sort", required = false) String sort,
	       @RequestParam(value = "purpose", required = false) String purpose,
	       Model model,
	       @RequestParam(value="downloadFormat", required = true) String downloadFormat,
	       @RequestParam(value = "archiveOptions", required = false) List<String> archiveOptions,
	       RedirectAttributes redirectAttributes) {

	    Map<String, String> selectedFacets = null;
        if (facets != null && !facets.isEmpty()) {
           selectedFacets = new HashMap<String, String>();
           for (FacetRequest facetRequest : facets) {
               selectedFacets.put(facetRequest.getFacet(),
                       facetRequest.getSelected());
           }
        }
        
        if(archiveOptions == null) {
        	archiveOptions = new ArrayList<String>();
        }
	    Page<? extends SearchableObject> result = searchableObjectService.search(query, null, 10, 0, null, null, selectedFacets, sort, null);
	    
		Resource resource = new Resource();
		resource.setTitle("download" + Long.toString(System.currentTimeMillis()));
		
		StringBuffer selectedFacetBuffer = new StringBuffer();
        if (facets != null && !facets.isEmpty()) {           
			boolean isFirst = true;
            for (FacetRequest facetRequest : facets) {
				if(!isFirst) {
                    selectedFacetBuffer.append(",");
				} else {
					isFirst = false;
				}
				selectedFacetBuffer.append(facetRequest.getFacet() + "=" + facetRequest.getSelected());
            }
        }
				
		Map<String, String> jobParametersMap = new HashMap<String, String>();
		jobParametersMap.put("resource.identifier", resource.getIdentifier());
		jobParametersMap.put("timestamp", Long.toString(System.currentTimeMillis()));
		// Download file - either the file or the directory
        String downloadFileName = UUID.randomUUID().toString();
        String downloadType = null;
        if(downloadFormat.equals("taxon")) {
			downloadFileName = downloadFileName + ".txt";
			jobParametersMap.put("download.taxon", toParameter(DarwinCorePropertyMap.taxonTerms.keySet()));			
            jobParametersMap.put("job.total.reads", Integer.toString(result.getSize()));
		} else {
			jobParametersMap.put("job.total.reads", Integer.toString(result.getSize() * (archiveOptions.size() + 1)));
			jobParametersMap.put("download.taxon",toParameter(DarwinCorePropertyMap.getConceptTerms(DwcTerm.Taxon)));
			for(String archiveOption : archiveOptions) {
				switch(archiveOption) {
				case "description":
					jobParametersMap.put("download.description", toParameter(DarwinCorePropertyMap.getConceptTerms(GbifTerm.Description)));
					break;
				case "distribution":
					jobParametersMap.put("download.distribution", toParameter(DarwinCorePropertyMap.getConceptTerms(GbifTerm.Distribution)));
					break;
				case "vernacularName":
					jobParametersMap.put("download.vernacularName", toParameter(DarwinCorePropertyMap.getConceptTerms(GbifTerm.VernacularName)));
					break;
				case "identifier":
					jobParametersMap.put("download.identifier", toParameter(DarwinCorePropertyMap.getConceptTerms(GbifTerm.Identifier)));
					break;
				case "measurementOrFact":
					jobParametersMap.put("download.measurementOrFact", toParameter(DarwinCorePropertyMap.getConceptTerms(DwcTerm.MeasurementOrFact)));
					break;
				case "image":
					jobParametersMap.put("download.image", toParameter(DarwinCorePropertyMap.getConceptTerms(GbifTerm.Image)));
					break;
				case "reference":
					jobParametersMap.put("download.reference", toParameter(DarwinCorePropertyMap.getConceptTerms(GbifTerm.Reference)));
					break;
				case "typeAndSpecimen":
					jobParametersMap.put("download.typeAndSpecimen", toParameter(DarwinCorePropertyMap.getConceptTerms(GbifTerm.TypesAndSpecimen)));
					break;
				default:
					break;
				}				
			}
		}
        
        jobParametersMap.put("download.file", downloadFileName);
        jobParametersMap.put("download.query", query);
        jobParametersMap.put("download.sort", sort);
        jobParametersMap.put("download.selectedFacets", selectedFacetBuffer.toString());
        jobParametersMap.put("download.fieldsTerminatedBy", "\t");
        jobParametersMap.put("download.fieldsEnclosedBy", "\"");
        jobParametersMap.put("download.limit", downloadLimit.toString());
        
        JobLaunchRequest jobLaunchRequest = new JobLaunchRequest();
        
        if(downloadFormat.equals("taxon")) {
        	jobLaunchRequest.setJob("FlatFileCreation"); 
		} else {
			jobLaunchRequest.setJob("DarwinCoreArchiveCreation");
		}
			
		jobLaunchRequest.setParameters(jobParametersMap);

		try {
			resource.setStartTime(null);
			resource.setDuration(null);
			resource.setExitCode(null);
			resource.setExitDescription(null);
			resource.setJobId(null);
			resource.setStatus(BatchStatus.UNKNOWN);
			resource.setRecordsRead(0);
			resource.setReadSkip(0);
			resource.setProcessSkip(0);
			resource.setWriteSkip(0);
			resource.setWritten(0);
			if(downloadFormat.equals("taxon")) {
			    resource.getParameters().put("download.file",downloadFileName);
			} else {
				resource.getParameters().put("download.file",downloadFileName + ".zip");
			}
			resourceService.save(resource);
			jobLauncher.launch(jobLaunchRequest);
			
			
			String[] codes = new String[] { "job.started" };
			Object[] args = new Object[] {};
			DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
					codes, args);
			redirectAttributes.addFlashAttribute("info", message);
			queryLog.info("Download: \'{}\', format: {}, purpose: {},"
	                + "facet: [{}], {} results", new Object[] {query,
	                downloadFormat, purpose, selectedFacets, result.getSize() });
		} catch (JobExecutionException e) {
			String[] codes = new String[] { "job.failed" };
			Object[] args = new Object[] { e.getMessage() };
			DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
					codes, args);
			redirectAttributes.addFlashAttribute("error", message);
		}
		return "redirect:/download/progress?downloadId=" + resource.getId();
	}
   
   private String toParameter(Collection<ConceptTerm> terms) {
	   StringBuffer stringBuffer = new StringBuffer();
       if (terms != null && !terms.isEmpty()) {           
			boolean isFirst = true;
           for (ConceptTerm term : terms) {
				if(!isFirst) {
                   stringBuffer.append(",");
				} else {
					isFirst = false;
				}
				stringBuffer.append(term.simpleName());
           }
       }
       return stringBuffer.toString();
   }
   
    @RequestMapping(value = "/progress", method = RequestMethod.GET, produces = "text/html")
    public String getProgress(@RequestParam("downloadId") Long downloadId, Model model, Locale locale) {
    	Resource resource = resourceService.load(downloadId);
    	model.addAttribute("resource", resource);
    	
    	if(resource.getBaseUrl() == null) {
    		resource.setExitDescription(messageSource.getMessage("download.being.prepared", new Object[] {}, locale));
    	} else {
    		String downloadFileName = resource.getParameters().get("download.file");
    		resource.setExitDescription(messageSource.getMessage("download.will.be.available", new Object[] {resource.getBaseUrl(), downloadFileName}, locale));
    	}
    	
    	return "download/progress";
    }
	
	@RequestMapping(value = "/progress", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
	public JobExecutionInfo getProgress(@RequestParam("downloadId") Long downloadId, Locale locale) throws Exception {
		JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
		Resource resource = resourceService.load(downloadId);
		
		JobExecution jobExecution = jobExplorer.getJobExecution(resource.getJobId());
		if(jobExecution != null) {	
			 Float recordsRead = 0f;
			jobExecutionInfo.setStatus(jobExecution.getStatus());
			if(jobExecution.getExitStatus() != null) {
				ExitStatus exitStatus = jobExecution.getExitStatus();
			    jobExecutionInfo.setExitCode(exitStatus.getExitCode());
			    jobExecutionInfo.setExitDescription(exitStatus.getExitDescription());
			    
			   
				Integer readSkip = 0;
				Integer processSkip = 0;
				Integer writeSkip = 0;
				Integer written = 0;
				for(StepExecution stepExecution : jobExecution.getStepExecutions()) {
					recordsRead += stepExecution.getReadCount();
					readSkip += stepExecution.getReadSkipCount();
					processSkip += stepExecution.getProcessSkipCount();
					writeSkip += stepExecution.getWriteSkipCount();
					written += stepExecution.getWriteCount();
				}
				jobExecutionInfo.setRecordsRead(recordsRead.intValue());
				jobExecutionInfo.setReadSkip(readSkip);
				jobExecutionInfo.setProcessSkip(processSkip);
				jobExecutionInfo.setWriteSkip(writeSkip);
				jobExecutionInfo.setWritten(written);
			}			
			Float total = Float.parseFloat(jobExecution.getJobInstance().getJobParameters().getString("job.total.reads"));
			
			jobExecutionInfo.setProgress(Math.round((recordsRead/ total) * 100f));
			if(resource.getBaseUrl() == null) {
				jobExecutionInfo.setExitDescription(messageSource.getMessage("download.being.prepared", new Object[] {}, locale));
	    	} else {
	    		String downloadFileName = resource.getParameters().get("download.file");
	    		if(jobExecutionInfo.getExitCode() == null) {	    		
	    		    jobExecutionInfo.setExitDescription(messageSource.getMessage("download.will.be.available", new Object[] {resource.getBaseUrl(), downloadFileName}, locale));
	    		} else {
	    			if(jobExecutionInfo.getExitCode().equals("COMPLETED")) {
	    				jobExecutionInfo.setExitDescription(messageSource.getMessage("download.is.available", new Object[] {resource.getBaseUrl(), downloadFileName}, locale));
	    			} else if(jobExecutionInfo.getExitCode().equals("FAILED")) {
	    				jobExecutionInfo.setExitDescription(messageSource.getMessage("download.failed", new Object[] {}, locale));
	    			} else {
	    				jobExecutionInfo.setExitDescription(messageSource.getMessage("download.will.be.available", new Object[] {resource.getBaseUrl(), downloadFileName}, locale));
	    			}
	    		}
	    	}
			
		}
		return jobExecutionInfo;
	}  

}
