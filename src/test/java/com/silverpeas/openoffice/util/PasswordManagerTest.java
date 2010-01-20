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
 * FLOSS exception.  You should have recieved a copy of the text describing
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.silverpeas.openoffice.util;

import com.silverpeas.openoffice.AuthenticationInfo;
import junit.framework.TestCase;

/**
 *
 * @author ehugonnet
 */
public class PasswordManagerTest extends TestCase {
    
    public PasswordManagerTest(String testName) {
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

  /**
   * Test of decodePassword method, of class PasswordManager.
   */
  public void testDecodePassword() throws Exception {
    String encodedPassword = "5f6b7130460e9b316ca968ec952d3fa3";
    String expResult = "helloworld";
    String result = PasswordManager.decodePassword(encodedPassword);
    assertEquals(expResult, result);
    encodedPassword = "4e2bb11fc119471ff3c0ba210d9843d1";
    expResult = "héhèhàhh";
    result = PasswordManager.decodePassword(encodedPassword);
    assertEquals(expResult, result);
  }

  /**
   * Test of encodePassword method, of class PasswordManager.
   */
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
  public void testExtractAuthenticationInfo() {
    System.out.println("extractAuthenticationInfo");
    String login = "bart";
    String encodedPassword = "5f6b7130460e9b316ca968ec952d3fa3";
    AuthenticationInfo expResult = new AuthenticationInfo("bart", "helloworld");
    AuthenticationInfo result =
        PasswordManager.extractAuthenticationInfo(login, encodedPassword);
    assertEquals(expResult, result);
  }

}
