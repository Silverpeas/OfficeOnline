/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.windows;

import com.silverpeas.openoffice.util.RegistryKeyReader;
import junit.framework.TestCase;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class RegistryHelperTest extends TestCase {

  public static final String INSTALLED_VERSION = "3.1";
  public static final String NOT_INSTALLED_VERSION = "2.0";

  public RegistryHelperTest(String testName) {
    super(testName);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public void testReadKey() throws Exception {
    if (!System.getProperty("os.name").startsWith("Windows")) {
      return;
    }
    String clockFormat = RegistryKeyReader.readKey(
        "\"HKEY_CURRENT_USER\\Software\\Microsoft\\Clock\" /v iFormat");
    assertNotNull(clockFormat);
    assertEquals("1", clockFormat);
  }

  public void testGetOpenOffice() throws Exception {
    if (!System.getProperty("os.name").startsWith("Windows")) {
      return;
    }
    WindowsOpenOfficeFinder helper = new WindowsOpenOfficeFinder();
    String openOffice2_4 = helper.getOpenOfficePath(INSTALLED_VERSION);
    assertNotNull(openOffice2_4);
    assertEquals("C:\\Program Files\\OpenOffice.org 3\\program\\soffice.exe",
        openOffice2_4);
    String openOffice2_0 = helper.getOpenOfficePath(NOT_INSTALLED_VERSION);
    assertNull(openOffice2_0);
  }

  public void testFindOpenOffice() throws Exception {
    if (!System.getProperty("os.name").startsWith("Windows")) {
      return;
    }
    WindowsOpenOfficeFinder helper = new WindowsOpenOfficeFinder();
    String openOffice = helper.findOpenOffice();
    assertNotNull(openOffice);
    assertEquals("\"C:\\Program Files\\OpenOffice.org 3\\program\\soffice.exe\"",
        openOffice);

  }
}
