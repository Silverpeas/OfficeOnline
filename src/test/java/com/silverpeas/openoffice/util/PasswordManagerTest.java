/**
 * Copyright (C) 2000 - 2012 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of the GPL, you may
 * redistribute this Program in connection with Free/Libre Open Source Software ("FLOSS")
 * applications as described in Silverpeas's FLOSS exception. You should have received a copy of the
 * text describing the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.silverpeas.openoffice.util;

import org.junit.Test;

import com.silverpeas.openoffice.AuthenticationInfo;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author ehugonnet
 */
public class PasswordManagerTest {

  /**
   * Test of decodePassword method, of class PasswordManager.
   *
   * @throws Exception
   */
  @Test
  public void testDecodePassword() throws Exception {
    String encodedPassword = "5f6b7130460e9b316ca968ec952d3fa3";
    String expResult = "helloworld";
    String result = new String(PasswordManager.decodePassword(encodedPassword));
    assertEquals(expResult, result);
    encodedPassword = "4e2bb11fc119471ff3c0ba210d9843d1";
    expResult = "héhèhàhh";
    result = new String(PasswordManager.decodePassword(encodedPassword));
    assertEquals(expResult, result);
    encodedPassword = "c77d90f5ce1e68a2";
    expResult = "";
    result = new String(PasswordManager.decodePassword(encodedPassword));
    assertEquals(expResult, result);
  }

  /**
   * Test of encodePassword method, of class PasswordManager.
   *
   * @throws Exception
   */
  @Test
  public void testEncodePassword() throws Exception {
    String password = "helloworld";
    String expResult = "5f6b7130460e9b316ca968ec952d3fa3";
    String result = PasswordManager.encodePassword(password);
    assertEquals(expResult, result);
    password = "héhèhàhh";
    expResult = "4e2bb11fc119471ff3c0ba210d9843d1";
    result = PasswordManager.encodePassword(password);
    assertEquals(expResult, result);
  }

  /**
   * Test of extractAuthenticationInfo method, of class PasswordManager.
   */
  @Test
  public void testExtractAuthenticationInfo() {
    String login = "bart";
    String encodedPassword = "5f6b7130460e9b316ca968ec952d3fa3";
    AuthenticationInfo expResult = new AuthenticationInfo("bart", "helloworld".toCharArray());
    AuthenticationInfo result = PasswordManager.extractAuthenticationInfo(login, encodedPassword);
    assertEquals(expResult, result);

    login = "homer%40simpson.com";
    encodedPassword = "046e010f9c0cfaa5";
    expResult = new AuthenticationInfo("homer@simpson.com", "homer".toCharArray());
    result = PasswordManager.extractAuthenticationInfo(login, encodedPassword);
    assertEquals(expResult, result);
  }
}
