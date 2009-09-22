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
package org.qualipso.funkyfactory.test.clock.ws;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.ws.BindingProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.ws.core.StubExt;
import org.junit.Before;
import org.junit.Test;
import org.qualipso.funkyfactory.test.clock.ws.client.ClockService;
import org.qualipso.funkyfactory.test.clock.ws.client.ClockServiceException_Exception;
import org.qualipso.funkyfactory.test.clock.ws.client.ClockService_Service;

/**
 * Test Clock service through its WebService interface
 * 
 * @author <a href="mailto:christophe.bouthier@loria.fr">Christophe Bouthier</a>
 * @date 29 July 2009
 * 
 */
public class ClockServiceWSTest {

	private static Log logger = LogFactory.getLog(ClockServiceWSTest.class);

	private ClockService service;

	/**
	 * Set up service for all tests.
	 */
	@Before
	public void setup() {
		ClockService_Service serviceProvider = new ClockService_Service();
		service = serviceProvider.getClockService();
		((StubExt) service).setConfigName("Standard WSSecurity Client");
	}

	/**
	 * Test the getTime WebService factory service unauthentified
	 */
	@Test
	public void testGetTimeUnauthentified() {
		logger.debug("Testing ClockService WebService unauthentified");

		messageTest();
	}

	/**
	 * Test the getTime WebService factory service authentified
	 */
	@Test
	public void testGetTimeAuthentified() {
		logger.debug("Testing ClockService WebService authentified");

		Map<String, Object> reqContext = ((BindingProvider) service).getRequestContext();
		reqContext.put(StubExt.PROPERTY_AUTH_TYPE, StubExt.PROPERTY_AUTH_TYPE_WSSE);
		reqContext.put(BindingProvider.USERNAME_PROPERTY, "kermit");
		reqContext.put(BindingProvider.PASSWORD_PROPERTY, "thefrog");

		messageTest();
	}
	
	
	/**
	 * Real test.
	 * We can't test directly by comparing to a hard-coded string, as the date is always changing,
	 * so we only test if the format is correct, by matching it to a given regex. 
	 */
	private void messageTest() {
		try {
			String time = service.getTime();
			logger.info("time: " + time);
			// French format
			assertTrue(Pattern.matches("\\d\\d?\\s[^\\s]*\\s\\d{4}\\s\\d{2}:\\d{2}$", time));
			// US format
			// assertTrue(Pattern.matches("[^\\s]*\\s\\d\\d?,\\s\\d{4}\\s\\d{1,2}:\\d{2}\\s[AP]M$", time));
		} catch (ClockServiceException_Exception e) {
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

}
