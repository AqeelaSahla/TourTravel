
package util;

import java.sql.*;

public class KoneksiDB {
  public static Connection getConnection() {
    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      String url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;database=Travel;encrypt=true;trustServerCertificate=true;loginTimeout=30;";
      System.out.println("Koneksi Berhasil");
      return DriverManager.getConnection(url, "sa", "123");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Koneksi Gagal");
      return null;
    }
  }
}
