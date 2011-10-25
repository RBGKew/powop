package org.emonocot.persistence.olap;

import java.sql.Connection;

import javax.sql.DataSource;

import org.olap4j.OlapConnection;
import org.olap4j.OlapWrapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class OlapConnectionFactory implements FactoryBean<OlapConnection> {

   /**
    *
    */
   private DataSource dataSource;

   /**
    *
    */
   private OlapConnection olapConnection;


/**
    * @param newDataSource
    *            the dataSource to set
    */
   @Autowired
   public final void setDataSource(final DataSource newDataSource) {
       this.dataSource = newDataSource;
   }


   /**
    * @return a connection
    * @throws Exception if there is a problem creating the connection
    */
    public final OlapConnection getObject() throws Exception {
        if (olapConnection == null) {
            Connection connection = dataSource.getConnection();
            OlapWrapper wrapper = (OlapWrapper) connection;
            olapConnection = wrapper.unwrap(OlapConnection.class);
        }
        return olapConnection;
    }

    /**
     * @return the object type
     */
    public final Class<?> getObjectType() {
        return OlapConnection.class;
    }

    /**
     * @return true, if the connection is a singleton
     */
    public final boolean isSingleton() {
        return false;
    }
    
    
    
}
