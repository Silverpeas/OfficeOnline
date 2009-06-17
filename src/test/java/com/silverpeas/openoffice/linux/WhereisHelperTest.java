/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.linux;

import com.silverpeas.openoffice.util.OsEnum;
import junit.framework.TestCase;

/**
 *
 * @author ehugonnet
 */
public class WhereisHelperTest extends TestCase {

  public WhereisHelperTest(String testName) {
    super(testName);
  }

  @Override
  protected void setUp()
      throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown()
      throws Exception {
    super.tearDown();
  }

  // TODO add test methods here. The name must begin with 'test'. For example:
  // public void testHello() {}
  public void testWhereis() {
    WhereisHelper helper = getHelper();
    String result = helper.whereis();
    assertEquals("soffice: /usr/bin/soffice", result);
  }

  public void testFindOpenOffice()
      throws Exception {
    WhereisHelper helper = getHelper();
    String result = helper.findOpenOffice();
    assertEquals("/usr/bin/soffice", result);
  }

  protected WhereisHelper getHelper() {
    OsEnum os = OsEnum.getOS(System.getProperty("os.name"));
    switch (os) {
      case WINDOWS_XP:
      case WINDOWS_VISTA:
        return new WhereisStubHelper();
      case LINUX:
      case MAC_OSX:
        return new WhereisHelper();
      default:
        return new WhereisHelper();
    }
  }
}
