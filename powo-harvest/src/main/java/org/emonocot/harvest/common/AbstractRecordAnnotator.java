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
package org.emonocot.harvest.common;

import java.io.Serializable;

import org.emonocot.model.Annotation;
import org.emonocot.model.registry.Organisation;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

public abstract class AbstractRecordAnnotator extends HibernateDaoSupport implements StepExecutionListener {

	private TransactionTemplate transactionTemplate = null;

	private Organisation source = null;

	private String sourceName;

	protected StepExecution stepExecution;

	public final void setSourceName(final String newSourceName) {
		this.sourceName = newSourceName;
	}

	public final void setTransactionManager(final PlatformTransactionManager transactionManager) {
		Assert.notNull(transactionManager, "The 'transactionManager' argument must not be null.");
		this.transactionTemplate = new TransactionTemplate(transactionManager);
		this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	}

	/**
	 *
	 * @return the source
	 */
	protected Organisation getSource() {
		if (source == null) {
			Criteria criteria = currentSession().createCriteria(Organisation.class).add(Restrictions.eq("identifier", sourceName));

			source = (Organisation) criteria.uniqueResult();
		}
		return source;
	}

	/**
	 *
	 * @param annotation Set the annotation
	 */
	public final void annotate(final Annotation annotation) {
		try {
			transactionTemplate.execute(new TransactionCallback() {
				public Serializable doInTransaction(
						final TransactionStatus status) {
					return currentSession().save(annotation);
				}
			});
		} catch (Throwable t) {
			logger.error(t.getMessage());
			throw new RuntimeException(t);
		}
	}

	public final void beforeStep(final StepExecution newStepExecution) {
		this.stepExecution = newStepExecution;
	}

	public final ExitStatus afterStep(final StepExecution newStepExecution) {
		return null;
	}
}
