package org.familysearch.paas;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import org.familysearch.backoff.retry.Retryer;
import org.familysearch.backoff.retry.RetryerBackOffImpl;
import org.familysearch.qa.testcommons.http.Context;
import org.familysearch.qa.testcommons.http.JerseyAssertImpl;

import static org.familysearch.qa.LogInfo.logInfo;
import static org.testng.Assert.assertEquals;

/**
 * Acceptance Test to validate the service - Validations
 * User:
 * Date:
 */
public class AcceptanceValidations {

  private static final Logger LOGGER = LoggerFactory.getLogger(AcceptanceValidations.class);

  private static final MediaType MEDIA_TYPE = MediaType.APPLICATION_XML_TYPE;

  private static final String ANIMAL_BASE = "paas-aws-acceptance-demo-monkey-";

  private static final String ANIMAL = ANIMAL_BASE + dateString();

  private static final int STATUS_OK = 200;
  private static final int STATUS_NO_CONTENT = 204;
  private static final int STATUS_NOT_FOUND = 404;

  private String service = "";
  private JerseyAssertImpl jersey;

  /**
   * Config Methods ***
   */
  @BeforeClass(alwaysRun = true)
  @Parameters({"baseUrl"})
  public void init(String baseUrl) {
    if (baseUrl == null || baseUrl.isEmpty()) {
      throw new IllegalArgumentException("Expected a non-null and non-empty serivce URL");
    }
    this.service = baseUrl.trim();
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Setting service: [" + getService() + "]." + logInfo());
    }
  }

  @AfterClass(alwaysRun = true)
  public void cleanup() throws Exception {
    //Add Suite teardown here
  }

  @BeforeMethod
  public void setUp() throws Exception {

    //Add test setup here
  }

  @AfterMethod
  public void tearDown() throws Exception {
    //Add test tear down here
  }


  @Test(groups = {"smoke", "acceptance"}, timeOut = 300000)
  public void httpGet_HealthCheckHeartbeat() {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Executing httpGet_HealthCheckHeartbeat() to determine Health State. Using service: [" + getService() + "]." + logInfo());
    }
    JerseyAssertImpl myRequest = getJersey();
    myRequest.setPrintStatus(true);
    myRequest.setPrintClientRequest(true);
    myRequest.assertGet("healthcheck/heartbeat", MEDIA_TYPE); // ensures 200 only
  }


  /**
   * Tests the Write to rds
   */
  @Test(groups = {"acceptance"}, timeOut = 300000, dependsOnMethods = {"httpGet_HealthCheckHeartbeat"})
  public void testWrite() {
    JerseyAssertImpl myRequest = getJersey();
    myRequest.setPrintStatus(true);
    myRequest.setPrintClientRequest(true);
    Context context = new Context();
    context.setMediaType(MEDIA_TYPE.toString());
    context.setPath("zoo/animals/" + ANIMAL);
    context.setStatusCode(STATUS_OK);
    myRequest.assertPut(context); // ensures 200 only
  }


  /**
   * Tests the Read from to rds
   */
  @Test(groups = {"acceptance"}, timeOut = 300000, dependsOnMethods = {"httpGet_HealthCheckHeartbeat", "testWrite"})
  public void testRead() {
    JerseyAssertImpl myRequest = getJersey();
    myRequest.setPrintStatus(true);
    myRequest.setPrintClientRequest(true);
    Context context = new Context(getRetryer()); // use retryer to overcome eventual consistency.
    context.setMediaType(MEDIA_TYPE.toString());
    context.setPath("zoo/animals/" + ANIMAL);
    context.setStatusCode(STATUS_OK);
    myRequest.assertGet(context); // ensures 200 only
    String actualValue = myRequest.getResultOutput();
    assertEquals(actualValue, "{\"name\":\"" + ANIMAL + "\"}", "Expected service read to return correct value. ");
  }


  /**
   * Tests the delete from rds
   */
  @Test(groups = {"acceptance"}, timeOut = 300000, dependsOnMethods = {"httpGet_HealthCheckHeartbeat", "testWrite", "testRead"})
  public void testDelete() {
    JerseyAssertImpl myRequest = getJersey();
    myRequest.setPrintStatus(false);
    myRequest.setReturnObject(false);
    myRequest.setPrintClientRequest(true);
    Context context = new Context();
    context.setMediaType(MEDIA_TYPE.toString());
    context.setPath("zoo/animals/" + ANIMAL);
    context.setStatusCode(STATUS_NO_CONTENT);

    myRequest.assertDelete(context);
    myRequest.getResultOutput();

    context.setStatusCode(STATUS_NOT_FOUND);
    myRequest.assertGet(context);

  }

  String getService() {
    return this.service;
  }

  private static String dateString() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
    return dateFormat.format(calendar.getTime());
  }


  JerseyAssertImpl getJersey() {
    if (this.jersey == null) {
      jersey = new JerseyAssertImpl(getService());
    }
    return jersey;
  }

  protected void setJersey(JerseyAssertImpl jersey) {
    this.jersey = jersey;
  }

  private Retryer getRetryer() {
    int maxRetries = 5;
    int defaultDelay = 2000;
    int maxDelay = 32000;
    return new RetryerBackOffImpl(maxRetries, defaultDelay, maxDelay);
  }

}

