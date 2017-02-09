package Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * Åª¨ú src/setting.properties ÀÉ®×
 * */
public class ConfigFile {

	Properties prop = new Properties();
	InputStream input = null;

	public ConfigFile() {
		try {

			String filename = "setting.properties";
			input = ConfigFile.class.getClassLoader().getResourceAsStream(
					filename);
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				return;
			}
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getPropValue(String name) {
		return prop.getProperty(name);
	}

}
