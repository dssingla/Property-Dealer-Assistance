package jdbc;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties prop = new Properties();

    static {

        try {

            InputStream in = new FileInputStream("config.properties");
            prop.load(in);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String get(String key) {
        return prop.getProperty(key);
    }

}