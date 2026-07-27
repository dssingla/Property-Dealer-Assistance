package jdbc;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CreateAdmin {

    public static void main(String[] args) {

        try {

            Connection con = DBConnection.getConnection();

            String hash = BCrypt.hashpw("admin123", BCrypt.gensalt());

            PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO admin(username,email,phone,password,notification,dark_mode,auto_backup) VALUES(?,?,?,?,?,?,?)");

            pst.setString(1, "admin");
            pst.setString(2, "admin@gmail.com");
            pst.setString(3, "9876543210");
            pst.setString(4, hash);
            pst.setBoolean(5, true);
            pst.setBoolean(6, false);
            pst.setBoolean(7, false);

            pst.executeUpdate();

            System.out.println("Admin Created Successfully");

            pst.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}