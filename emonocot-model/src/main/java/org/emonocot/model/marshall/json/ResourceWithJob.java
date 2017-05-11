package org.emonocot.model.marshall.json;

import java.util.Map;

import org.emonocot.model.JobConfiguration;
import org.emonocot.model.constants.JobType;
import org.emonocot.model.registry.Resource;

import lombok.Data;

@Data
public class ResourceWithJob {

	private Resource resource;

	private JobType jobType;

	private Map<String, String> params;

	private JobConfiguration jobConfiguration;

}
