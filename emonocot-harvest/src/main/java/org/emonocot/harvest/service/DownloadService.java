/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.harvest.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.emonocot.api.job.DarwinCorePropertyMap;
import org.emonocot.api.job.ExtendedAcTerm;
import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.model.auth.Permission;
import org.emonocot.model.auth.User;
import org.emonocot.model.registry.Resource;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.Term;
import org.springframework.beans.factory.annotation.Autowired;

public class DownloadService  {

	private JobLauncher jobLauncher;

	/**
	 * @param jobLauncher the jobLauncher to set
	 */
	@Autowired
	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	public int getDownloadLimit() {
		return 10000;
	}

	public void requestDownload(String query, Map<String, String> selectedFacets, String sort, String spatial,
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
					jobParametersMap.put("download.image", toParameter(DarwinCorePropertyMap.getConceptTerms(ExtendedAcTerm.Multimedia)));
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

		StringBuffer selectedFacetBuffer = new StringBuffer();
		if (selectedFacets != null && !selectedFacets.isEmpty()) {
			boolean isFirst = true;
			for (Entry<String, String> facet : selectedFacets.entrySet()) {
				if(!isFirst) {
					selectedFacetBuffer.append(",");
				} else {
					isFirst = false;
				}
				selectedFacetBuffer.append(facet.getKey() + "=" + facet.getValue());
			}
		}
		jobParametersMap.put("download.selectedFacets", selectedFacetBuffer.toString());
		jobParametersMap.put("download.fieldsTerminatedBy", "\t");
		jobParametersMap.put("download.fieldsEnclosedBy", "\"");
		jobParametersMap.put("download.user.displayName", requestingUser.getAccountName());
		if(requestingUser.getAuthorities().contains(Permission.PERMISSION_ADMINISTRATE)) {
			jobParametersMap.put("download.limit", Integer.toString(Integer.MAX_VALUE));
		} else {
			jobParametersMap.put("download.limit", "" + Integer.toString(getDownloadLimit()));
		}
		jobLaunchRequest.setParameters(jobParametersMap);

		jobLauncher.launch(jobLaunchRequest);

	}

	private String toParameter(Collection<Term> terms) {
		StringBuffer stringBuffer = new StringBuffer();
		if (terms != null && !terms.isEmpty()) {
			boolean isFirst = true;
			for (Term term : terms) {
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
