package org.emonocot.api;

import org.emonocot.model.marshall.json.ResourceWithJob;

public interface ResourceWithJobService {

	public void save(ResourceWithJob resourceWithJob);

	public void saveOrUpdate(ResourceWithJob resourceWithJob);

}
