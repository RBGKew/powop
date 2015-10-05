package org.emonocot.job.delete;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.emonocot.model.Taxon;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericHibernateDeleter<T>{
	
	private static Logger logger = LoggerFactory.getLogger(GenericHibernateDeleter.class);
	
	public void unlinkTaxon(Long id, Session session){
		
		
		
		Query acceptednameQuery = session.createQuery("update Taxon set acceptedNameUsage = null where acceptedNameUsage_id =:id");
		acceptednameQuery.setParameter("id", id);
		acceptednameQuery.executeUpdate();
		
		Query originalnameQuery = session.createQuery("update Taxon set originalNameUsage = null where originalNameUsage_id =:id");
		originalnameQuery.setParameter("id", id);
		originalnameQuery.executeUpdate();
		
	}
	
	public void Delete(SessionFactory sessionFactory, Class<T> clazz, String resource_id, String type, SolrServer solrServer){
		int listsize = 5000;
		while(listsize > 0){
			Session session = sessionFactory.openSession();
			org.hibernate.Transaction tx = session.beginTransaction();
			Query query = session.createSQLQuery("select " + "id from " + type + " where resource_id = " + resource_id);
			@SuppressWarnings("unchecked")
			List<BigInteger> list = query.list();
			listsize = list.size();
			logger.debug("there are " + listsize + " items found for delete in " + type);
			for(BigInteger item : list){
				
				if(clazz == Taxon.class){
					logger.trace("unlinking taxa");
					 unlinkTaxon(item.longValue(), session);
				}
				
				try {
					solrServer.deleteById(type + "_" + item.toString() );
					solrServer.commit(true,true);
				} catch (SolrServerException | IOException e) {
					logger.debug(e.getMessage());		
				}
				
				session.delete(session.load(clazz, item.longValue()));
				
			}
			session.flush();
			session.clear();
			tx.commit();
			session.close();
		}
		if(sessionFactory.getCache() != null){
        sessionFactory.getCache().evictEntityRegions();
		}
		if(sessionFactory.getCache() != null){
			sessionFactory.getCache().evictCollectionRegions();
			}
		if(sessionFactory.getCache() != null){
			sessionFactory.getCache().evictDefaultQueryRegion();
			}
		
        
        

	}
}
