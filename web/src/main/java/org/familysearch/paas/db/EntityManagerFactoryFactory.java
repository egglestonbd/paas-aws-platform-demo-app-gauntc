package org.familysearch.paas.db;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.familysearch.sas.client.ObjectRequester;
import org.familysearch.sas.schema.Attribute;
import org.familysearch.sas.schema.Sas;

@Component
public class EntityManagerFactoryFactory {

  private static final Logger LOG = LoggerFactory.getLogger(EntityManagerFactoryFactory.class);
  private static final String SAS_OBJECT_NAME = "paas-aws-platform-demo-app-gauntc-dev-database-db";
  private static final ObjectRequester OBJECT_REQUESTER = new ObjectRequester();

  public EntityManagerFactory createEntityManagerFactory() throws IOException {
    Map<String, Object> configOverrides = new HashMap<String, Object>();
    String user, password, dbConnectionUrl;

    try {
      LOG.info("Retrieving db credentials from SAS object: " + SAS_OBJECT_NAME);
      Sas sas = OBJECT_REQUESTER.getSecureObject(SAS_OBJECT_NAME);
      Map<String, Attribute> attributes = sas.getAttributes();
      user = attributes.get("fchUser").getSingleStringValue();
      password = attributes.get("fchPassword").getSingleStringValue();
      dbConnectionUrl = attributes.get("fchDbConnectUrl").getSingleStringValue();
      LOG.info("Successfully retrieved database connection info from SAS");
      LOG.info(String.format("database credentials: username=%s, password=%s", user, "********"));
    }
    catch (IOException e) {
      throw new RuntimeException("Could not get db coordinates from SAS Object '" + SAS_OBJECT_NAME + "'");
    }

    configOverrides.put("javax.persistence.jdbc.user", user);
    configOverrides.put("javax.persistence.jdbc.password", password);
    configOverrides.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
    configOverrides.put("javax.persistence.jdbc.url", dbConnectionUrl);
    configOverrides.put("hibernate.hbm2ddl.auto", "validate");

    LOG.info(String.format("Establishing a remote database connection with dbConnectionUrl:%s", configOverrides.get("javax.persistence.jdbc.url")));
    return Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa", configOverrides);
  }

  private EntityManagerFactory getInMemoryEntityManagerFactory() {
    LOG.warn("Failed to load remote database connection, an in-memory database will be substituted");
    return Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa");
  }

  private boolean isNull(String property, String propertyName) {
    if (property == null) {
      LOG.error(String.format("Unable to retrieve database info because system property %s is not defined", propertyName));
      return true;
    }
    return false;
  }


}
