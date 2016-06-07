package org.familysearch.paas;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * Acceptance Test to validate the service - Factory
 * @author EgglestonBD
 * Date: 5/18/12
 */
public class Acceptance {

  private static final Log LOGGER = LogFactory.getLog(Acceptance.class);

  private String service = "";

  @BeforeSuite(alwaysRun = true)
  public void init(){
    // Setup Test Data acceptance.
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Creating Acceptance Data.");
    }
    // ...

  }

  /**
   * Call AcceptanceValidations with each service location.
   * Note this is a pipe delimited list as you may have multiple service instances you are probing.
   * @return results from the test.
   */
  @Factory
  public Object[] create() {
    List<Object> vResult = new ArrayList<Object>();
    if (LOGGER.isInfoEnabled()) {
     LOGGER.info("Executing AcceptanceValidations.");
    }

    vResult.add(new AcceptanceValidations());
    //vResult.add(new AcceptanceValidations2());
    return vResult.toArray(new Object[vResult.size()]);
  }

}

