package org.emonocot.factories;

import org.emonocot.api.job.JobConfigurationException;
import org.emonocot.model.JobConfiguration;
import org.emonocot.model.constants.ResourceType;
import org.emonocot.model.marshall.json.ResourceWithJob;
import org.emonocot.model.registry.Resource;

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
				.parameter("resource.id", resource.getId().toString())
				.parameter("resource.identifier", resource.getIdentifier());
	}

	public static JobConfiguration resourceJob(ResourceWithJob resourceWithJob) {
		switch(resourceWithJob.getJobType()) {
		case Harvest:
			return JobConfigurationFactory.harvest(resourceWithJob.getResource());
		case HarvestNames:
			return JobConfigurationFactory.harvestNames(resourceWithJob.getResource());
		case HarvestTaxonomy:
			return JobConfigurationFactory.harvestTaxonomy(resourceWithJob.getResource());
		case HarvestImages:
			return JobConfigurationFactory.harvestImages(resourceWithJob.getResource(), resourceWithJob.getParams().get("image.server"));
		default:
			throw new JobConfigurationException("Not a job type associated with a resource");
		}
	}

	public static JobConfiguration harvest(Resource resource) {
		return baseHarvestConfiguration(resource).build();
	}

	public static JobConfiguration harvestNames(Resource resource) {
		return baseHarvestConfiguration(resource)
				.parameter("taxon.processing.mode", "IMPORT_NAMES")
				.build();
	}

	public static JobConfiguration harvestTaxonomy(Resource resource) {
		return baseHarvestConfiguration(resource)
				.parameter("taxon.processing.mode", "IMPORT_TAXONOMY")
				.build();
	}

	public static JobConfiguration harvestImages(Resource resource, String prefix) {
		return baseHarvestConfiguration(resource)
				.parameter("image.server", prefix)
				.build();
	}

	public static JobConfiguration reIndexTaxa() {
		return JobConfiguration.builder()
				.jobName("ReIndex")
				.description("ReIndex all taxa")
				.parameter("query.string", "select t.id from Taxon t")
				.parameter("query.type", "org.emonocot.model.Taxon")
				.parameter("solr.selected.facets", "base.class_s=org.emonocot.model.Taxon")
				.build();
	}
}