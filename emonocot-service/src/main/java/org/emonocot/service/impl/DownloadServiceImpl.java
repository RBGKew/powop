/**
 * 
 */
package org.emonocot.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.api.job.DarwinCorePropertyMap;
import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.model.auth.Permission;
import org.emonocot.model.auth.User;
import org.emonocot.model.registry.Resource;
import org.emonocot.service.DownloadService;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author jk00kg
 *
 */
@Service
public class DownloadServiceImpl implements DownloadService {
    
    private JobLauncher jobLauncher;

    /**
     * @param jobLauncher the jobLauncher to set
     */
    @Autowired
    @Qualifier("readOnlyJobLauncher")
    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    public int getDownloadLimit() {
        return 10000;
    }

    /* (non-Javadoc)
     * @see org.emonocot.service.DownloadService#requestDownload(org.emonocot.service.DownloadType, java.lang.String, java.util.List, java.lang.String, java.lang.String, java.lang.String, org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, java.lang.String, java.util.List, org.emonocot.service.RedirectAttributes, org.emonocot.model.auth.User)
     */
    @Override
    public void requestDownload(String query, String selectedFacets, String sort, String spatial,
            Integer expectedCount, String purpose, String downloadFormat, List<String> archiveOptions,
            Resource resource, User requestingUser) throws JobExecutionException {
      //Launch the job
        JobLaunchRequest jobLaunchRequest = new JobLaunchRequest();
        Map<String, String> jobParametersMap = new HashMap<String, String>();
        jobParametersMap.put("resource.identifier", resource.getIdentifier());
        jobParametersMap.put("timestamp", Long.toString(System.currentTimeMillis()));
        String resourceFileName = resource.getParameters().get("download.file");
        String batchFileName;
        if(resourceFileName.endsWith(".zip")){//It's an archive created from a directory
            batchFileName = resourceFileName.substring(0, resourceFileName.length() - 4);
        } else {
            batchFileName = resourceFileName;
        }
        switch (downloadFormat) {
        case "alphabeticalChecklist":
            jobParametersMap.put("download.searchDefinition", "query:" + query + " selectedFacets:" + selectedFacets + " spatial:" + spatial);
            jobParametersMap.put("download.template.filepath", "org/emonocot/job/download/reports/alphabeticalChecklist.jrxml");
            jobParametersMap.put("job.total.reads", Integer.toString(expectedCount));
            jobLaunchRequest.setJob("FlatFileCreation");
            sort = "searchable.label_sort_asc";
            break;
        case "hierarchicalChecklist":
            jobParametersMap.put("download.searchDefinition", "query:" + query + " selectedFacets:" + selectedFacets + " spatial:" + spatial);
            jobParametersMap.put("download.template.filepath", "org/emonocot/job/download/reports/hierarchicalChecklist.jrxml");
            jobParametersMap.put("job.total.reads", Integer.toString(expectedCount));
            jobLaunchRequest.setJob("FlatFileCreation");
            sort = "taxon.taxon_rank_s_asc,taxon.family_s_asc,taxon.genus_s_asc,taxon.specificEpithet_s_asc,taxon.scientific_name_t_asc";
            break;
        case "taxon":
            jobParametersMap.put("download.taxon", toParameter(DarwinCorePropertyMap.getConceptTerms(DwcTerm.Taxon)));
            jobParametersMap.put("job.total.reads", Integer.toString(expectedCount));
            jobLaunchRequest.setJob("FlatFileCreation");
            break;
        default://archive
            jobParametersMap.put("job.total.reads", Integer.toString(expectedCount * (archiveOptions.size() + 1)));
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
        
        jobParametersMap.put("download.file", batchFileName);
        jobParametersMap.put("download.query", query);
        if(spatial != null) {
            jobParametersMap.put("download.spatial", spatial);
        }
        jobParametersMap.put("download.sort", sort);
        jobParametersMap.put("download.format", downloadFormat);
        jobParametersMap.put("download.selectedFacets", selectedFacets);
        jobParametersMap.put("download.fieldsTerminatedBy", "\t");
        jobParametersMap.put("download.fieldsEnclosedBy", "\"");
        jobParametersMap.put("download.user.displayName", requestingUser.getAccountName());
        if(requestingUser.getPermissions().contains(Permission.PERMISSION_ADMINISTRATE.name())) {
            jobParametersMap.put("download.limit", Integer.toString(Integer.MAX_VALUE)); 
        } else {
            jobParametersMap.put("download.limit", "" + Integer.toString(getDownloadLimit()));
        }   
        jobLaunchRequest.setParameters(jobParametersMap);

        jobLauncher.launch(jobLaunchRequest);
        
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

}
