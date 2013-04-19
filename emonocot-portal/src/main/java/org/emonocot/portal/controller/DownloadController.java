package org.emonocot.portal.controller;

import java.security.Principal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.emonocot.api.ResourceService;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.UserService;
import org.emonocot.api.job.DarwinCorePropertyMap;
import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.model.SearchableObject;
import org.emonocot.model.auth.Permission;
import org.emonocot.model.auth.User;
import org.emonocot.model.constants.ResourceType;
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
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
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
    
    private UserService userService;
    
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
    public void setUserService(UserService userService) {
        this.userService = userService;
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
       @RequestParam(value = "x1", required = false) Double x1,
       @RequestParam(value = "y1", required = false) Double y1,
       @RequestParam(value = "x2", required = false) Double x2,
       @RequestParam(value = "y2", required = false) Double y2,
       HttpServletRequest request,
       Model model) {

       Map<String, String> selectedFacets = null;
       if (facets != null && !facets.isEmpty()) {
           selectedFacets = new HashMap<String, String>();
           for (FacetRequest facetRequest : facets) {
               selectedFacets.put(facetRequest.getFacet(),
                       facetRequest.getSelected());
           }
       }
       
       String spatial = null;
       DecimalFormat decimalFormat = new DecimalFormat("###0.0");
       if (x1 != null && y1 != null && x2 != null && y2 != null && (x1 != 0.0 && y1 != 0.0 && x2 != 0.0 && x2 != 0.0 && y2 != 0.0)) {
    	 model.addAttribute("x1",x1);
    	 model.addAttribute("y1",y1);
    	 model.addAttribute("x2",x2);
    	 model.addAttribute("y2",y2);
         spatial = "Intersects(" + decimalFormat.format(x1) + " " + decimalFormat.format(y1) + " " + decimalFormat.format(x2) + " " + decimalFormat.format(y2) + ")";
       }

       //Run the search
       Page<? extends SearchableObject> result = searchableObjectService.search(query, spatial, 10, 0, null, null, selectedFacets, sort, null);

       result.setSort(sort);
       result.putParam("query", query);
       model.addAttribute("taxonTerms", DarwinCorePropertyMap.getConceptTerms(DwcTerm.Taxon));
       model.addAttribute("taxonMap", DarwinCorePropertyMap.getPropertyMap(DwcTerm.Taxon));
       model.addAttribute("result", result);
       if(result.getSize() > downloadLimit && !request.isUserInRole(Permission.PERMISSION_ADMINISTRATE.name())) {
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
	       @RequestParam(value = "x1", required = false) Double x1,
	       @RequestParam(value = "y1", required = false) Double y1,
	       @RequestParam(value = "x2", required = false) Double x2,
	       @RequestParam(value = "y2", required = false) Double y2,
	       @RequestParam(value = "purpose", required = false) String purpose,
	       Model model,
	       HttpServletRequest request,
	       @RequestParam(value="downloadFormat", required = true) String downloadFormat,
	       @RequestParam(value = "archiveOptions", required = false) List<String> archiveOptions,
	       RedirectAttributes redirectAttributes,
	       Principal principal) {
       

        User user = userService.load(principal.getName());

	    Map<String, String> selectedFacets = null;
        if (facets != null && !facets.isEmpty()) {
           selectedFacets = new HashMap<String, String>();
           for (FacetRequest facetRequest : facets) {
               selectedFacets.put(facetRequest.getFacet(),
                       facetRequest.getSelected());
           }
        }
        
        String spatial = null;
        DecimalFormat decimalFormat = new DecimalFormat("###0.0");
        if (x1 != null && y1 != null && x2 != null && y2 != null && (x1 != 0.0 && y1 != 0.0 && x2 != 0.0 && x2 != 0.0 && y2 != 0.0)) {
          spatial = "Intersects(" + decimalFormat.format(x1) + " " + decimalFormat.format(y1) + " " + decimalFormat.format(x2) + " " + decimalFormat.format(y2) + ")";
        }
        
        if(archiveOptions == null) {
        	archiveOptions = new ArrayList<String>();
        }
	    Page<? extends SearchableObject> result = searchableObjectService.search(query, spatial, 10, 0, null, null, selectedFacets, sort, null);
	    
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

        //Launch the job
        JobLaunchRequest jobLaunchRequest = new JobLaunchRequest();
		Map<String, String> jobParametersMap = new HashMap<String, String>();
		jobParametersMap.put("resource.identifier", resource.getIdentifier());
		jobParametersMap.put("timestamp", Long.toString(System.currentTimeMillis()));
		String downloadFileName = UUID.randomUUID().toString(); // Download file - either the file or the directory
		//TODO change to if(!"archive".equals(downloadFormat)){} else{}//use archive case block
        switch (downloadFormat) {
        case "alphabeticalChecklist":
            downloadFileName = downloadFileName + ".pdf";
            jobParametersMap.put("download.checklist.pdf", "true");
            jobParametersMap.put("download.template.filepath", "org/emonocot/job/download/reports/alphabeticalChecklist.jrxml");
            jobParametersMap.put("job.total.reads", Integer.toString(result.getSize()));
            jobLaunchRequest.setJob("FlatFileCreation");
            sort = "searchable.label_sort_asc";
            break;
        case "taxon":
            downloadFileName = downloadFileName + ".txt";
            jobParametersMap.put("download.taxon", toParameter(DarwinCorePropertyMap.getConceptTerms(DwcTerm.Taxon)));
            jobParametersMap.put("job.total.reads", Integer.toString(result.getSize()));
            jobLaunchRequest.setJob("FlatFileCreation");
            break;
        default:
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
            jobLaunchRequest.setJob("DarwinCoreArchiveCreation");
            break;
        }
        
        jobParametersMap.put("download.file", downloadFileName);
        jobParametersMap.put("download.query", query);
        if(spatial != null) {
            jobParametersMap.put("download.spatial", spatial);
        }
        jobParametersMap.put("download.sort", sort);
        jobParametersMap.put("download.selectedFacets", selectedFacetBuffer.toString());
        jobParametersMap.put("download.fieldsTerminatedBy", "\t");
        jobParametersMap.put("download.fieldsEnclosedBy", "\"");
        jobParametersMap.put("download.user.displayName", user.getAccountName());
        if(request.isUserInRole(Permission.PERMISSION_ADMINISTRATE.name())) {
        	jobParametersMap.put("download.limit", new Integer(Integer.MAX_VALUE).toString()); 
        } else {
        	jobParametersMap.put("download.limit", downloadLimit.toString());
        }	
		jobLaunchRequest.setParameters(jobParametersMap);

		//Save the 'resource'
		try {
			resource.setResourceType(ResourceType.DOWNLOAD);
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
			switch (downloadFormat) {
            case "taxon":
            case "alphabeticalChecklist":
                resource.getParameters().put("download.file", downloadFileName);
                break;
	        default:
                resource.getParameters().put("download.file", downloadFileName + ".zip");
	            break;
			}
			resourceService.save(resource);
			jobLauncher.launch(jobLaunchRequest);			
			
			String[] codes = new String[] { "download.submitted" };
			Object[] args = new Object[] {};
			DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
					codes, args);
			redirectAttributes.addFlashAttribute("info", message);
			queryLog.info("Download: \'{}\', format: {}, purpose: {},"
	                + "facet: [{}], {} results", new Object[] {query,
	                downloadFormat, purpose, selectedFacets, result.getSize() });
		} catch (JobExecutionException e) {
			String[] codes = new String[] { "download.failed" };
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
				stringBuffer.append(term.qualifiedName());
           }
       }
       return stringBuffer.toString();
   }
   
    @RequestMapping(value = "/progress", method = RequestMethod.GET, produces = "text/html")
    public String getProgress(@RequestParam("downloadId") Long downloadId, Model model, Locale locale) {
    	Resource resource = resourceService.load(downloadId);
    	model.addAttribute("resource", resource);
    	
    	String downloadFileName = resource.getParameters().get("download.file");
    	if(resource.getBaseUrl() == null) {
    		resource.setExitDescription(messageSource.getMessage("download.being.prepared", new Object[] {}, locale));
    	} else {
    	    resource.setExitDescription(messageSource.getMessage("download.will.be.available", new Object[] {messageSource.getMessage("portal.baseUrl", new Object[] {}, locale), downloadFileName}, locale));
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
	    				jobExecutionInfo.setExitDescription(messageSource.getMessage("download.is.available", new Object[] {messageSource.getMessage("portal.baseUrl", new Object[] {}, locale), downloadFileName}, locale));
	    			} else if(jobExecutionInfo.getExitCode().equals("FAILED")) {
	    				jobExecutionInfo.setExitDescription(messageSource.getMessage("download.failed", new Object[] {}, locale));
	    			} else {
	    				jobExecutionInfo.setExitDescription(messageSource.getMessage("download.will.be.available", new Object[] {messageSource.getMessage("portal.baseUrl", new Object[] {}, locale), downloadFileName}, locale));
	    			}
	    		}
	    	}
			
		}
		return jobExecutionInfo;
	}  

}
