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
package org.silverpeas.openoffice.util;

import org.silverpeas.openoffice.util.UrlExtractor;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author ehugonnet
 */
public class UrlExtractorTest {

  private static final String REAL_OS = System.getProperty("os.name");

  @AfterClass
  public static void tearDown() {
    System.setProperty("os.name", REAL_OS);
  }

  /**
   * Test of decodeUrl method, of class UrlExtractor.
   */
  @Test
  public void testDecodeUrl() {
    String encodedUrl =
        "http%3A%2F%2Fdsr-preprod-3%2Fsilverpeas%2Frepository%2Fjackrabbit%2Fattachments%2Fkmelia34%2FAttachment%2FImages%2F44733%2FAtelier+en+classe.doc";
    String expResult =
        "http://dsr-preprod-3/silverpeas/repository/jackrabbit/attachments/kmelia34/Attachment/Images/44733/Atelier en classe.doc";
    String result = UrlExtractor.decodeUrl(encodedUrl);
    assertEquals(expResult, result);
  }

  /**
   * Test of escapeUrl method, of class UrlExtractor.
   */
  @Test
  public void testEscapeUrl() {
    String url =
        "http://dsr-preprod-3/silverpeas/repository/jackrabbit/attachments/kmelia34/Attachment/Images/44733/Atelier en classe.doc";
    String expResult =
        "\"http://dsr-preprod-3/silverpeas/repository/jackrabbit/attachments/kmelia34/Attachment/Images/44733/Atelier%20en%20classe.doc\"";
    String expResultUnix =
        "http://dsr-preprod-3/silverpeas/repository/jackrabbit/attachments/kmelia34/Attachment/Images/44733/Atelier%20en%20classe.doc";
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
  @Test
  public void testExtractUrl() {
    System.setProperty("os.name", "Windows XP");
    String encodedUrl =
        "http%3A%2F%2Fdsr-preprod-3%2Fsilverpeas%2Frepository%2Fjackrabbit%2Fattachments%2Fkmelia34%2FAttachment%2FImages%2F44733%2FAtelier+en+classe.doc";
    String expResult =
        "\"http://dsr-preprod-3/silverpeas/repository/jackrabbit/attachments/kmelia34/Attachment/Images/44733/Atelier%20en%20classe.doc\"";
    String result = UrlExtractor.extractUrl(encodedUrl);
    assertEquals(expResult, result);
  }

  /**
   * Test of decodePath method, of class UrlExtractor.
   */
  @Test
  public void testDecodePath() {
    String encodedPath = "C%3A%5C%5CProgram+Files%5C%5CMicrosoft+Office%5C%5COFFICE10";
    String expResult = "C:\\\\Program Files\\\\Microsoft Office\\\\OFFICE10";
    String result = UrlExtractor.decodePath(encodedPath);
    assertEquals(expResult, result);
  }
}
