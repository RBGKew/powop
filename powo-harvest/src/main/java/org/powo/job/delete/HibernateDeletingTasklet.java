package org.powo.job.delete;

import org.hibernate.SessionFactory;
import org.powo.job.delete.GenericHibernateDeleter;
import org.powo.model.*;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.solr.client.solrj.SolrClient;

public class HibernateDeletingTasklet implements Tasklet{

	private SessionFactory sessionFactory;

	private SolrClient solrClient;

	private GenericHibernateDeleter<Taxon> taxonDeleter = new GenericHibernateDeleter<Taxon>();

	private GenericHibernateDeleter<Description> descriptionDeleter = new GenericHibernateDeleter<Description>();

	private GenericHibernateDeleter<Concept> conceptDeleter = new GenericHibernateDeleter<Concept>();

	private GenericHibernateDeleter<Distribution> distributionDeleter = new GenericHibernateDeleter<Distribution>();

	private GenericHibernateDeleter<Identifier> identifierDeleter = new GenericHibernateDeleter<Identifier>();

	private GenericHibernateDeleter<Image> imageDeleter = new GenericHibernateDeleter<Image>();

	private GenericHibernateDeleter<MeasurementOrFact> measurementorFactDeleter = new GenericHibernateDeleter<MeasurementOrFact>();

	private GenericHibernateDeleter<Reference> referenceDeleter = new GenericHibernateDeleter<Reference>();

	private GenericHibernateDeleter<TypeAndSpecimen> typeandspecimenDeleter = new GenericHibernateDeleter<TypeAndSpecimen>();

	private GenericHibernateDeleter<VernacularName> vernacularnameDeleter = new GenericHibernateDeleter<VernacularName>();

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}

	@Autowired
	public void setSolrClient(SolrClient solrClient) {
		this.solrClient = solrClient;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext chunkcontext) throws Exception {

		String resource_id = (String) chunkcontext.getStepContext().getStepExecution()
				.getJobExecution().getJobParameters().getString("resource_id");

		taxonDeleter.Delete(sessionFactory, Taxon.class, resource_id, "Taxon", solrClient);
		descriptionDeleter.Delete(sessionFactory, Description.class, resource_id, "Description", solrClient);
		conceptDeleter.Delete(sessionFactory, Concept.class, resource_id, "Concept", solrClient);
		distributionDeleter.Delete(sessionFactory, Distribution.class, resource_id, "Distribution", solrClient);
		identifierDeleter.Delete(sessionFactory, Identifier.class, resource_id, "Identifier", solrClient);
		imageDeleter.Delete(sessionFactory, Image.class, resource_id, "Image", solrClient);
		measurementorFactDeleter.Delete(sessionFactory, MeasurementOrFact.class, resource_id, "MeasurementOrFact", solrClient);
		referenceDeleter.Delete(sessionFactory, Reference.class, resource_id, "Reference", solrClient);
		typeandspecimenDeleter.Delete(sessionFactory, TypeAndSpecimen.class, resource_id, "TypeAndSpecimen", solrClient);
		vernacularnameDeleter.Delete(sessionFactory, VernacularName.class, resource_id, "VernacularName", solrClient);

		return null;
	}
}
