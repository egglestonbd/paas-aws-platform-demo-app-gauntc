package org.familysearch.paas;

import java.lang.Exception;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * User:
 * Date: 5/21/12
 * Time: 7:46 AM
 * To validate the Acceptance Module.
 * NOTE: This is to test the helper methods only.  No need to test the tests that test the code.
 */
public class AcceptanceTest {
  private Acceptance fixture = new Acceptance();

  @BeforeMethod
  public void setUp() throws Exception {

  }

  @AfterMethod
  public void tearDown() throws Exception {

  }

  @Test
  public void testNew() {

    fixture.init();

  }
}
