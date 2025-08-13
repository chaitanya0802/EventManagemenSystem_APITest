package baseclass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configmanager {
	
	//create instance
	private static Properties prop =new Properties();
	
	static {
        try {
        	//create fis
            FileInputStream fileInputStream = new FileInputStream("src/test/resources/config.properties/config.property");
            //load
            prop.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	//return property by key
	public static String getProperty(String key) {
		return prop.getProperty(key);
	}
	

}
