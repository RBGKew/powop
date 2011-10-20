package org.emonocot.persistence.olap;

import javax.sql.DataSource;

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Util.PropertyList;
import mondrian.rolap.RolapConnection;
import mondrian.rolap.RolapConnectionProperties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

public class RolapConnectionFactory implements FactoryBean<Connection> {
    
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
   private String catalogName;
   /**
    *
    */
   private Boolean poolNeeded;
   
   
   
   public void setCatalogName(String catalogName) {
    this.catalogName = catalogName;
}


public void setPoolNeeded(Boolean poolNeeded) {
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

    
    public Connection getObject() throws Exception {
        if (connection == null) {
            properties = new PropertyList();
            properties.put(RolapConnectionProperties.Catalog.name(),
                    catalogName);
            properties.put(RolapConnectionProperties.PoolNeeded.name(), poolNeeded.toString());
            connection = DriverManager.getConnection(properties, null, dataSource);
        }
        
        return connection;
    }

    
    public Class<?> getObjectType() {
        return Connection.class;
    }

    public boolean isSingleton() {
        return false;
    }

}
