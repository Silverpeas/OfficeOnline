/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.util;

import junit.framework.TestCase;

/**
 *
 * @author ehugonnet
 */
public class UrlExtractorTest extends TestCase {

  public UrlExtractorTest(String testName) {
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
   * Test of decodeUrl method, of class UrlExtractor.
   */
  public void testDecodeUrl() {
    String encodedUrl = "http%3A%2F%2Fdsr-preprod-3%2Fsilverpeas%2Frepository%2Fjackrabbit%2Fattachments%2Fkmelia34%2FAttachment%2FImages%2F44733%2FAtelier+en+classe.doc";
    String expResult = "http://dsr-preprod-3/silverpeas/repository/jackrabbit/attachments/kmelia34/Attachment/Images/44733/Atelier en classe.doc";
    String result = UrlExtractor.decodeUrl(encodedUrl);
    assertEquals(expResult, result);
  }

  /**
   * Test of escapeUrl method, of class UrlExtractor.
   */
  public void testEscapeUrl() {
    String url = "http://dsr-preprod-3/silverpeas/repository/jackrabbit/attachments/kmelia34/Attachment/Images/44733/Atelier en classe.doc";
    String expResult = "\"http://dsr-preprod-3/silverpeas/repository/jackrabbit/attachments/kmelia34/Attachment/Images/44733/Atelier en classe.doc\"";
    String expResultUnix = "http://dsr-preprod-3/silverpeas/repository/jackrabbit/attachments/kmelia34/Attachment/Images/44733/Atelier%20en%20classe.doc";
    System.setProperty("os.name", "Windows XP");
    String result = UrlExtractor.escapeUrl(url);
    assertEquals(expResult, result);
    System.setProperty("os.name", "Linux");
    result = UrlExtractor.escapeUrl(url);
    assertEquals(expResultUnix, result);
  }

  /**
   * Test of extractUrl method, of class UrlExtractor.
   */
  public void testExtractUrl() {
    System.setProperty("os.name", "Windows XP");
    String encodedUrl = "http%3A%2F%2Fdsr-preprod-3%2Fsilverpeas%2Frepository%2Fjackrabbit%2Fattachments%2Fkmelia34%2FAttachment%2FImages%2F44733%2FAtelier+en+classe.doc";
    String expResult = "\"http://dsr-preprod-3/silverpeas/repository/jackrabbit/attachments/kmelia34/Attachment/Images/44733/Atelier en classe.doc\"";
    String result = UrlExtractor.extractUrl(encodedUrl);
    assertEquals(expResult, result);
  }

  /**
   * Test of decodePath method, of class UrlExtractor.
   */
  public void testDecodePath() {
    String encodedPath = "C%3A%5C%5CProgram+Files%5C%5CMicrosoft+Office%5C%5COFFICE10";
    String expResult = "C:\\\\Program Files\\\\Microsoft Office\\\\OFFICE10";
    String result = UrlExtractor.decodePath(encodedPath);
    assertEquals(expResult, result);
  }
}
