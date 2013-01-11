/**
 * Copyright (C) 2000 - 2009 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * As a special exception to the terms and conditions of version 3.0 of the GPL, you may redistribute this Program in
 * connection with Free/Libre Open Source Software ("FLOSS") applications as described in Silverpeas's FLOSS exception. You
 * should have received a copy of the text describing the FLOSS exception, and it is also available here:
 * "http://repository.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.silverpeas.openoffice.windows;

import org.silverpeas.openoffice.windows.WindowsOpenOfficeFinder;
import org.silverpeas.openoffice.util.RegistryKeyReader;
import junit.framework.TestCase;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class RegistryHelperTest extends TestCase {

  public static final String INSTALLED_VERSION = "3.2";
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
            "\"HKEY_CURRENT_USER\\Control Panel\\International\" /v iTime");
    assertNotNull(clockFormat);
    assertEquals("1", clockFormat);
  }

  public void testGetOpenOffice() throws Exception {
    if (!System.getProperty("os.name").startsWith("Windows")) {
      return;
    }
    if (!System.getenv("ProgramFiles").equals(System.getenv("ProgramW6432"))) {
      return;
    }
    WindowsOpenOfficeFinder helper = new WindowsOpenOfficeFinder();
    String openOffice2_4 = helper.getOpenOfficePath(INSTALLED_VERSION);
    assertNotNull(openOffice2_4);
    assertEquals("C:\\Program Files (x86)\\OpenOffice.org 3\\program\\soffice.exe",
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
    if(is64Bits()) {
    assertEquals("\"C:\\Program Files (x86)\\OpenOffice.org 3\\program\\soffice.exe\"", openOffice);
    }else {
      assertEquals("\"C:\\Program Files\\OpenOffice.org 3\\program\\soffice.exe\"", openOffice);
    }
  }

  private boolean is64Bits() {
    if (System.getProperty("os.name").contains("Windows")) {
      return (System.getenv("ProgramFiles(x86)") != null);
    }
    return (System.getProperty("os.arch").indexOf("64") != -1);
  }
}
