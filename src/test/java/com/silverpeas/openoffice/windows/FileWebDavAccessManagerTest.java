/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.silverpeas.openoffice.windows;

import junit.framework.TestCase;

/**
 *
 * @author ehugonnet
 */
public class FileWebDavAccessManagerTest extends TestCase {
    
    public FileWebDavAccessManagerTest(String testName) {
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
   * Test of encodeUrl method, of class FileWebDavAccessManager.
   */
  public void testEncodeUrl() throws Exception {
    System.out.println("encodeUrl");
    String url = "http://localhost:8000/silverpeas/toto/Ceci est un &x@plé.doc";
    String expResult = "http://localhost:8000/silverpeas/toto/Ceci%20est%20un%20%26x%40pl%E9.doc";
    String result = FileWebDavAccessManager.encodeUrl(url);
    assertEquals(expResult, result);
  }

}
