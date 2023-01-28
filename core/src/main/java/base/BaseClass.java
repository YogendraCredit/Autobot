package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;


public class BaseClass {
    public static Properties properties;
    public static Object value = null;
    public static final String environment = "qa2";
    private static InputStream inputStream = null;
    private static String propertyFile;
    public static String partnerLoanId = "";
    public static String appFormId = "";

    public static String initializeEnvironment(String key) throws Exception {
        try {
            properties = new Properties();
            //propertyFile = System.getProperty("user.dir") + "/" + environment + ".properties";
            propertyFile = "/Users/yogendrakumar/Downloads/Autobot-main 2/core/src/test/resources/qa2.properties";
            inputStream = new FileInputStream(propertyFile);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propertyFile + "' not found in the classpath");
            }
            value = properties.get(key);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return value.toString();
    }




}