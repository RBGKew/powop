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
