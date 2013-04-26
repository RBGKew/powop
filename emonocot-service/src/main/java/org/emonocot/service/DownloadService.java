package org.emonocot.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.emonocot.api.job.JobExecutionException;
import org.emonocot.model.auth.User;
import org.emonocot.model.registry.Resource;
import org.springframework.ui.Model;

public interface DownloadService {
    
    public void requestDownload(String query, String selectedFacets, String sort, String spatial,
            Integer expectedCount, String purpose, String downloadFormat, List<String> archiveOptions,
            Resource resource, User requestingUser) throws JobExecutionException;
    
    public int getDownloadLimit();
}
