package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
public class DBConnection {

    public static Connection getConnection() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(
                Config.get("db.url"),
                Config.get("db.user"),
                Config.get("db.password")
        );
    }
    public static void main(String[] args) {
        try {
            Connection con = getConnection();
            System.out.println("✅ Connected Successfully");

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}