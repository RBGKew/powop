package org.powo.factories;

import java.util.HashMap;
import java.util.Map;

import org.powo.api.job.JobConfigurationException;
import org.powo.model.JobConfiguration;
import org.powo.model.constants.ResourceType;
import org.powo.model.marshall.json.JobWithParams;
import org.powo.model.marshall.json.ResourceWithJob;
import org.powo.model.registry.Resource;

public class JobConfigurationFactory {

	private static JobConfiguration.JobConfigurationBuilder baseHarvestConfiguration(Resource resource) {

		if(resource.getId() == null) {
			throw new JobConfigurationException("Resource must be saved before creating job configuration");
		}

		return JobConfiguration.builder()
				.jobName(ResourceType.DwC_Archive.getJobName())
				.description("Harvest " + resource.getTitle())
				.parameter("authority.name", resource.getOrganisation().getIdentifier())
				.parameter("authority.uri", resource.getUri())
				.parameter("resource.identifier", resource.getIdentifier());
	}

	public static JobConfiguration resourceJob(ResourceWithJob resourceWithJob) {
		JobConfiguration job;
		switch(resourceWithJob.getJobType()) {
		case Harvest:
			job = JobConfigurationFactory.harvest(resourceWithJob.getResource());
			break;
		case HarvestNames:
			job = JobConfigurationFactory.harvestNames(resourceWithJob.getResource());
			break;
		case HarvestTaxonomy:
			job = JobConfigurationFactory.harvestTaxonomy(resourceWithJob.getResource());
			break;
		case HarvestImages:
			job = JobConfigurationFactory.harvestImages(resourceWithJob.getResource(), resourceWithJob.getParams().remove("prefix"));
			break;
		default:
			throw new JobConfigurationException("Not a job type associated with a resource");
		}

		if(resourceWithJob.getParams() != null && !resourceWithJob.getParams().isEmpty()) {
			Map<String, String> params = new HashMap<>(job.getParameters());
			params.putAll(resourceWithJob.getParams());
			job.setParameters(params);
		}

		return job;
	}

	public static JobConfiguration buildJob(JobWithParams jobWithParams) {
		JobConfiguration job;
		switch(jobWithParams.getJobType()) {
		case ReIndex:
			job = JobConfigurationFactory.reIndexTaxa();
			break;
		default:
			throw new JobConfigurationException("Not a job type associated with a resource");
		}

		if(jobWithParams.getParams() != null && !jobWithParams.getParams().isEmpty()) {
			Map<String, String> params = new HashMap<>(job.getParameters());
			params.putAll(jobWithParams.getParams());
			job.setParameters(params);
		}

		if(jobWithParams.getDescription() != null) {
			job.setDescription(jobWithParams.getDescription());
		}

		return job;
	}

	public static JobConfiguration harvest(Resource resource) {
		return baseHarvestConfiguration(resource).build();
	}

	public static JobConfiguration harvestNames(Resource resource) {
		return baseHarvestConfiguration(resource)
				.parameter("taxon.processing.mode", "IMPORT_NAMES")
				.description("Harvest names from " + resource.getTitle())
				.build();
	}

	public static JobConfiguration harvestTaxonomy(Resource resource) {
		return baseHarvestConfiguration(resource)
				.parameter("taxon.processing.mode", "IMPORT_TAXONOMY")
				.description("Harvest taxonomy from " + resource.getTitle())
				.build();
	}

	public static JobConfiguration harvestImages(Resource resource, String prefix) {
		return baseHarvestConfiguration(resource)
				.parameter("prefix", prefix)
				.build();
	}

	public static JobConfiguration reIndexTaxa() {
		return JobConfiguration.builder()
				.jobName("ReIndex")
				.description("ReIndex all taxa")
				.parameter("query.string", "select t.id from Taxon t")
				.parameter("query.type", "org.powo.model.Taxon")
				.parameter("solr.selected.facets", "base.class_s=org.powo.model.Taxon")
				.build();
	}
}