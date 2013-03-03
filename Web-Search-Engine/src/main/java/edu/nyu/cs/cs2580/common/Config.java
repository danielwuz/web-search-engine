package edu.nyu.cs.cs2580.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {

	private static Logger _logger = LogManager.getLogger(Config.class);

	private Properties properties;

	private static Config config;

	private Config() {
		properties = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream("conf.properties");
		try {
			properties.load(is);
		} catch (IOException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private static Config instance() {
		if (config == null) {
			config = new Config();
		}
		return config;
	}

	public static String getProperty(String property) {
		return Config.instance().properties.getProperty(property);
	}
}
