package com.mbuyukasik.assignment.kalah.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * This class will be used to access configuration parameters on demand
 * 
 * @author: mehmet buyukasik
 * @version 1.0
 */
@Service
public class AppConfig {
	
	// All configuration parameter names are listed below
	public static final String PARAM_GAME_CODE_LENGTH = "GAME_CODE_LENGTH";
	public static final String PARAM_NUMBER_OF_STONES = "NUMBER_OF_STONES";

	@Autowired
	private Environment env;

	/**
	 * method retrieves configuration value for given key
	 * 
	 * @param: paramName is the key value of configuration parameter
	 */
	public String getConfigValue(String paramName) {
		return env.getProperty(paramName);
	}

}
