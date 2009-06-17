/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.windows;

import com.silverpeas.openoffice.windows.MsOfficeRegistryHelper;
import junit.framework.TestCase;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class MsOfficeFinderTest extends TestCase {

  MsOfficeRegistryHelper helper = new MsOfficeRegistryHelper();

  public MsOfficeFinderTest(String testName) {
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

  public void testFindExcel() throws Exception {
    String path = helper.findSpreadsheet();
    assertNotNull(path);
    assertEquals("\"C:\\PROGRA~1\\MICROS~3\\OFFICE11\\EXCEL.EXE\"", path);
  }

  public void testFindPowerpoint() throws Exception {
    String path = helper.findPresentation();
    assertNotNull(path);
    assertEquals("\"C:\\PROGRA~1\\MICROS~3\\OFFICE11\\POWERPNT.EXE\"", path);
  }

  public void testFindWord() throws Exception {
    String path = helper.findWordEditor();
    assertNotNull(path);
    assertEquals("\"C:\\PROGRA~1\\MICROS~3\\OFFICE11\\WINWORD.EXE\" /m", path);
  }

  public void testExtractPath() {
    assertEquals("C:\\PROGRA~1\\MICROS~3\\OFFICE11\\WINWORD.EXE ", helper.
        extractPath("C:\\PROGRA~1\\MICROS~3\\OFFICE11\\WINWORD.EXE "));
    assertEquals("C:\\PROGRA~1\\MICROS~3\\OFFICE11\\WINWORD.EXE", helper.
        extractPath("C:\\PROGRA~1\\MICROS~3\\OFFICE11\\WINWORD.EXE /automation"));
    assertEquals("C:\\PROGRA~1\\MICROS~3\\OFFICE11\\EXCEL.EXE",
        helper.extractPath(
        "C:\\PROGRA~1\\MICROS~3\\OFFICE11\\EXCEL.EXE /automation *]gAVn-}f(ZXfeAR6.jiEXCELFiles>!De@]Vz(r=f`1lfq`?R& /automation\\0\\0"));

    assertEquals("\"C:\\PROGRA~1\\MICROS~3\\OFFICE11\\EXCEL.EXE",
        helper.extractPath(
        "\"C:\\PROGRA~1\\MICROS~3\\OFFICE11\\EXCEL.EXE /automation\r\nLocalServer32	REG_MULTI_SZ	\r\n*]gAVn-}f(ZXfeAR6.jiEXCELFiles>!De@]Vz(r=f`1lfq`?R& /automation\\0\\0\""));
  }

  public void testIsMicrosoftOffice2007() throws Exception {
    assertFalse(helper.isMicrosoftOffice2007());
  }
}
