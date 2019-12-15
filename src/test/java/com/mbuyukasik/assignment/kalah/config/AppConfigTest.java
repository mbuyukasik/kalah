package com.mbuyukasik.assignment.kalah.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.mbuyukasik.assignment.kalah.TestAbstractBootApplication;

/**
 * AppConfigTest - Test class of AppConfig
 * 
 * @author TTMBUYUKASIK
 *
 */
public class AppConfigTest extends TestAbstractBootApplication {

	private static final String CONFIG_ERROR_MESSAGE = "Return value of getConfigValue for %s is incorrect. It should be '%s'";

	@Autowired
	private Environment env;

	@Autowired
	private AppConfig appConfig;

	@Test
	public void testGetConfigValue() {

		String configValue = env.getProperty(AppConfig.PARAM_GAME_CODE_LENGTH);

		assertEquals(configValue, appConfig.getConfigValue(AppConfig.PARAM_GAME_CODE_LENGTH),
				String.format(CONFIG_ERROR_MESSAGE, AppConfig.PARAM_GAME_CODE_LENGTH, configValue));

	}

}
