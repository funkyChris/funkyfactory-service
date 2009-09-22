/*
 * Qualipso Funky Factory
 * Copyright (C) 2006-2010 INRIA
 * http://www.inria.fr - molli@loria.fr
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation. See the GNU
 * Lesser General Public License in LGPL.txt for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 * Initial authors :
 *
 * Jérôme Blanchard / INRIA
 * Christophe Bouthier / INRIA
 * Pascal Molli / Nancy Université
 * Gérald Oster / Nancy Université
 */
package org.qualipso.funkyfactory.test.clock.functionnal;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Properties;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qualipso.funkyfactory.service.clock.ClockService;
import org.qualipso.funkyfactory.service.clock.ClockServiceException;

/**
 * Functionnal test for Clock service
 * 
 * @author <a href="mailto:christophe.bouthier@loria.fr">Christophe Bouthier</a>
 * @date 26 August 2009
 */
public class ClockServiceFunctionalTest {

	private static Log logger = LogFactory.getLog(ClockServiceFunctionalTest.class);

	private static Context context;

	/**
	 * Set up service for all tests.
	 */
	@BeforeClass
	public static void before() throws NamingException {
		Properties properties = new Properties();
		properties.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		properties.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		properties.put("java.naming.provider.url", "localhost:1099");
		System.setProperty("java.security.auth.login.config", ClassLoader.getSystemResource("jaas.config").getPath());
		context = new InitialContext(properties);
	}

	/**
	 * Test the getTime ClockService unauthentified
	 */
	@Test
	public void testGetTimeUnauthentified() {
		logger.debug("Testing ClockService  unauthentified");

		try {
			messageTest();
		} catch (NamingException e) {
			logger.error("Problem when doing the service lookup");
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		} catch (ClockServiceException e) {
			logger.error("Problem when calling the service");
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	/**
	 * Test the getTime ClockService authentified
	 */
	@Test
	public void testGetTimeAuthentified() {
		logger.debug("Testing ClockService  authentified");

		try {
			UsernamePasswordHandler uph = new UsernamePasswordHandler("kermit", "thefrog"); 
			LoginContext loginContext = new LoginContext("tests", uph);
			loginContext.login();

			messageTest();

			loginContext.logout();
		} catch (LoginException e) {
			logger.error("Problem when loggin in");
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		} catch (NamingException e) {
			logger.error("Problem when doing the service lookup");
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		} catch (ClockServiceException e) {
			logger.error("Problem when calling the service");
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	/**
	 * Real test.
	 * We can't test directly by comparing to a hard-coded string, as the date is always changing,
	 * so we only test if the format is correct, by matching it to a given regex. 
	 */
	private void messageTest() throws NamingException, ClockServiceException {
		ClockService clockService = (ClockService) context.lookup("ClockService");
		String time = clockService.getTime();
		logger.info("time: " + time);
			// French format
			assertTrue(Pattern.matches("\\d\\d?\\s[^\\s]*\\s\\d{4}\\s\\d{2}:\\d{2}$", time));
			// US format
			// assertTrue(Pattern.matches("[^\\s]*\\s\\d\\d?,\\s\\d{4}\\s\\d{1,2}:\\d{2}\\s[AP]M$", time));
	}
}
