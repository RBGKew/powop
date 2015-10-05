package org.emonocot.job.delete;

import org.hibernate.SessionFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.solr.client.solrj.SolrServer;
import org.emonocot.model.*;
import org.emonocot.job.delete.GenericHibernateDeleter;

public class HibernateDeletingTasklet implements Tasklet{

	private SessionFactory sessionFactory;
	
	private SolrServer solrServer;
	
	private GenericHibernateDeleter<Taxon> taxonDeleter = new GenericHibernateDeleter<Taxon>();
	
	private GenericHibernateDeleter<Description> descriptionDeleter = new GenericHibernateDeleter<Description>();
	
	private GenericHibernateDeleter<Concept> conceptDeleter = new GenericHibernateDeleter<Concept>();
	
	private GenericHibernateDeleter<Distribution> distributionDeleter = new GenericHibernateDeleter<Distribution>();
	
	private GenericHibernateDeleter<IdentificationKey> identificationKeyDeleter = new GenericHibernateDeleter<IdentificationKey>();
	
	private GenericHibernateDeleter<Identifier> identifierDeleter = new GenericHibernateDeleter<Identifier>();
	
	private GenericHibernateDeleter<Image> imageDeleter = new GenericHibernateDeleter<Image>();
	
	private GenericHibernateDeleter<MeasurementOrFact> measurementorFactDeleter = new GenericHibernateDeleter<MeasurementOrFact>();
	
	private GenericHibernateDeleter<PhylogeneticTree> phylogenetictreeDeleter = new GenericHibernateDeleter<PhylogeneticTree>();
	
	private GenericHibernateDeleter<Place> placeDeleter = new GenericHibernateDeleter<Place>();
	
	private GenericHibernateDeleter<Reference> referenceDeleter = new GenericHibernateDeleter<Reference>();
	
	private GenericHibernateDeleter<TypeAndSpecimen> typeandspecimenDeleter = new GenericHibernateDeleter<TypeAndSpecimen>();
	
	private GenericHibernateDeleter<VernacularName> vernacularnameDeleter = new GenericHibernateDeleter<VernacularName>();
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	
	@Autowired
	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}



	
	
	
	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext chunkcontext) throws Exception {
		
		String resource_id = (String) chunkcontext.getStepContext().getStepExecution()
                .getJobExecution().getJobInstance().getJobParameters().getString("resource_id");
		
		taxonDeleter.Delete(sessionFactory, Taxon.class, resource_id, "Taxon", solrServer);
		descriptionDeleter.Delete(sessionFactory, Description.class, resource_id, "Description", solrServer);
		conceptDeleter.Delete(sessionFactory, Concept.class, resource_id, "Concept", solrServer);
		distributionDeleter.Delete(sessionFactory, Distribution.class, resource_id, "Distribution", solrServer);
		identificationKeyDeleter.Delete(sessionFactory, IdentificationKey.class, resource_id, "IdentificationKey", solrServer);
		identifierDeleter.Delete(sessionFactory, Identifier.class, resource_id, "Identifier", solrServer);
		imageDeleter.Delete(sessionFactory, Image.class, resource_id, "Image", solrServer);
		measurementorFactDeleter.Delete(sessionFactory, MeasurementOrFact.class, resource_id, "MeasurementOrFact", solrServer);
		phylogenetictreeDeleter.Delete(sessionFactory, PhylogeneticTree.class, resource_id, "PhylogeneticTree", solrServer);
		placeDeleter.Delete(sessionFactory, Place.class, resource_id, "Place", solrServer);
		referenceDeleter.Delete(sessionFactory, Reference.class, resource_id, "Reference", solrServer);
		typeandspecimenDeleter.Delete(sessionFactory, TypeAndSpecimen.class, resource_id, "TypeAndSpecimen", solrServer);
		vernacularnameDeleter.Delete(sessionFactory, VernacularName.class, resource_id, "VernacularName", solrServer);
		
		return null;
	}
	

}
