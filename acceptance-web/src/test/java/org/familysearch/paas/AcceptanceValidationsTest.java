package org.familysearch.paas;

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
public class AcceptanceValidationsTest {
  private AcceptanceValidations fixture = new AcceptanceValidations();

  @BeforeMethod
  public void setUp() throws Exception {

  }

  @AfterMethod
  public void tearDown() throws Exception {

  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void initEmpty() {
    fixture.init("");
  }


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void initNull(){
    fixture.init(null);
  }

  @Test
  public void initValid(){
    fixture.init("http://myUrl");
  }


  @Test
  public void setup()throws Exception{
    fixture.setUp();
  }

  @Test
  public void tearDownTest()throws Exception{
    fixture.tearDown();
  }
}
