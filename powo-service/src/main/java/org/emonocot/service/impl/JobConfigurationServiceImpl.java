package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.api.JobConfigurationService;
import org.emonocot.model.JobConfiguration;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.JobConfigurationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobConfigurationServiceImpl implements JobConfigurationService {

	@Autowired
	private JobConfigurationDao dao;

	@Transactional
	public void save(JobConfiguration conf) {
		dao.save(conf);
	}

	@Transactional(readOnly = true)
	public JobConfiguration get(Long id) {
		return dao.get(id);
	}

	@Transactional(readOnly = true)
	public void refresh(JobConfiguration conf) {
		dao.refresh(conf);
	}

	@Transactional
	public void saveOrUpdate(JobConfiguration job) {
		dao.saveOrUpdate(job);
	}

	@Transactional
	public Page<JobConfiguration> list(int page, int size) {
		return new DefaultPageImpl<>(dao.list(page, size), page, size);
	}

	@Override
	public List<JobConfiguration> listByName(String jobName) {
		return dao.listByName(jobName);
	}
}
