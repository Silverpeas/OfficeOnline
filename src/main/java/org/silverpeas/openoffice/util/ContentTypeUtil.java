package org.silverpeas.openoffice.util;

import javax.activation.MimetypesFileTypeMap;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author mmoquillon
 */
public class ContentTypeUtil {

  private static final MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();

  public static MsOfficeType getContentType(String url) throws MalformedURLException {
    String fileName = new URL(url).getFile();
    String contentType = mimeTypes.getContentType(fileName.toLowerCase());
    return MsOfficeType.valueOfMimeType(contentType);
  }
}
