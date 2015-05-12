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
package org.emonocot.service;

import java.util.List;
import java.util.Map;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.model.auth.User;
import org.emonocot.model.registry.Resource;

public interface DownloadService {
    
    public void requestDownload(String query, Map<String, String> selectedFacets, String sort, String spatial,
            Integer expectedCount, String purpose, String downloadFormat, List<String> archiveOptions,
            Resource resource, User requestingUser) throws JobExecutionException;
    
    public int getDownloadLimit();
}
