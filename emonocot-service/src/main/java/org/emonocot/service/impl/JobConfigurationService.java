package org.emonocot.service.impl;

import org.emonocot.model.JobConfiguration;
import org.emonocot.persistence.dao.JobConfigurationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobConfigurationService {

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
}
