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
  }

  /**
   * Test of encodePassword method, of class PasswordManager.
   */
  public void testEncodePassword() throws Exception {
    String password = "helloworld";
    String expResult = "5f6b7130460e9b316ca968ec952d3fa3";
    String result = PasswordManager.encodePassword(password);
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
