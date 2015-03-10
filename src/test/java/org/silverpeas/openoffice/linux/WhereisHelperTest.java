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

package org.silverpeas.openoffice.linux;

import org.silverpeas.openoffice.linux.WhereisHelper;
import org.silverpeas.openoffice.util.OsEnum;
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
      case WINDOWS_SEVEN:
        return new WhereisStubHelper();
      case LINUX:
      case MAC_OSX:
        return new WhereisHelper();
      default:
        return new WhereisHelper();
    }
  }
}
