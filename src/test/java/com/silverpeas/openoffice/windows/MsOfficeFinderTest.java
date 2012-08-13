/**
 * Copyright (C) 2000 - 2009 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://repository.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.openoffice.windows;

import junit.framework.TestCase;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class MsOfficeFinderTest extends TestCase {

  private static final String INSTALLED_OFFICE_PATH = "C:\\PROGRA~1\\MICROS~1\\Office12\\";

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
    if (!System.getProperty("os.name").startsWith("Windows")) {
      return;
    }
    String path = helper.findSpreadsheet();
    assertNotNull(path);
    assertEquals("\"" + INSTALLED_OFFICE_PATH + "EXCEL.EXE\"", path);
  }

  public void testFindPowerpoint() throws Exception {
    if (!System.getProperty("os.name").startsWith("Windows")) {
      return;
    }
    String path = helper.findPresentation();
    assertNotNull(path);
    assertEquals("\"" + INSTALLED_OFFICE_PATH + "POWERPNT.EXE\"", path);
  }

  public void testFindWord() throws Exception {
    if (!System.getProperty("os.name").startsWith("Windows")) {
      return;
    }
    String path = helper.findWordEditor();
    assertNotNull(path);
    assertEquals("\"" + INSTALLED_OFFICE_PATH + "WINWORD.EXE\" /m", path);
  }

  public void testExtractPath() {
    assertEquals(INSTALLED_OFFICE_PATH + "WINWORD.EXE ", helper.
        extractPath(INSTALLED_OFFICE_PATH + "WINWORD.EXE "));
    assertEquals(INSTALLED_OFFICE_PATH + "WINWORD.EXE", helper.
        extractPath(INSTALLED_OFFICE_PATH + "WINWORD.EXE /automation"));
    assertEquals(INSTALLED_OFFICE_PATH + "EXCEL.EXE", helper.extractPath(INSTALLED_OFFICE_PATH
        + "EXCEL.EXE /automation *]gAVn-}f(ZXfeAR6.jiEXCELFiles>!De@]Vz(r=f`1lfq`?R& /automation\\0\\0"));
    assertEquals("\"" + INSTALLED_OFFICE_PATH + "EXCEL.EXE", helper.extractPath("\"" + INSTALLED_OFFICE_PATH
        + "EXCEL.EXE /automation\r\nLocalServer32	REG_MULTI_SZ	\r\n*]gAVn-}f(ZXfeAR6.jiEXCELFiles>!De@]Vz(r=f`1lfq`?R& /automation\\0\\0\""));
  }

  public void testIsMicrosoftOffice2007() throws Exception {
    if (!System.getProperty("os.name").startsWith("Windows")) {
      return;
    }
    assertTrue(helper.isMicrosoftOffice());
  }
}
