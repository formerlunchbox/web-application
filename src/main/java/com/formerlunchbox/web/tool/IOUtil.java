package com.formerlunchbox.web.tool;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class IOUtil {
  private IOUtil() {
    // Private constructor to prevent instantiation
  }

  // 读jar包根目录下的文件
  public static String loadJarFile(
      ClassLoader loader, String resourceName) {

    InputStream in = loader.getResourceAsStream(resourceName);
    if (null == in) {
      return null;
    }
    String out = null;
    try {
      int len = in.available();
      byte[] bytes = new byte[len];

      int readLength = in.read(bytes);
      if ((long) readLength < len) {
        throw new IOException(String.format("File length is [%d] but read [%d]!", len, readLength));
      }
      out = new String(bytes, StandardCharsets.UTF_8);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeQuietly(in);
    }
    return out;
  }

  public static void closeQuietly(java.io.Closeable o) {
    if (null == o)
      return;
    try {
      o.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
