package org.emonocot.persistence.olap;

import javax.sql.DataSource;

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Util.PropertyList;
import mondrian.rolap.RolapConnectionProperties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 *
 */
public class OlapConnectionFactory implements FactoryBean<Connection> {

    /**
    *
    */
   private PropertyList properties;
   /**
    *
    */
   private DataSource dataSource;

   /**
    *
    */
   private Connection connection;
   /**
    *
    */
   private Resource catalog;
   /**
    *
    */
   private Boolean poolNeeded;

   /**
    *
    * @param catalog Set the catalogue name
    */
   public final void setCatalog(final Resource catalog) {
    this.catalog = catalog;
   }


   /**
    *
    * @param poolNeeded Set whether the pool is needed
    */
    public final void setPoolNeeded(final Boolean poolNeeded) {
        this.poolNeeded = poolNeeded;
    }


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
    public final Connection getObject() throws Exception {
        if (connection == null) {
            properties = new PropertyList();
            properties.put(RolapConnectionProperties.Catalog.name(),
                    catalog.getFile().getAbsolutePath());
            properties.put(RolapConnectionProperties.PoolNeeded.name(),
                    poolNeeded.toString());
            connection = DriverManager.getConnection(properties, null,
                    dataSource);
        }
        return connection;
    }

    /**
     * @return the object type
     */
    public final Class<?> getObjectType() {
        return Connection.class;
    }

    /**
     * @return true, if the connection is a singleton
     */
    public final boolean isSingleton() {
        return false;
    }
}
