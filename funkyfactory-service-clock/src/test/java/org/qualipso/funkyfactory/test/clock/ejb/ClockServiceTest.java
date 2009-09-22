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
package org.qualipso.funkyfactory.test.clock.ejb;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qualipso.funkyfactory.service.clock.ClockService;
import org.qualipso.funkyfactory.service.clock.ClockServiceBean;
import org.qualipso.funkyfactory.service.clock.ClockServiceException;

import com.bm.testsuite.BaseSessionBeanFixture;

/**
 * Direct unit tests for the Clock service.
 * 
 * @author <a href="mailto:christophe.bouthier@loria.fr">Christophe Bouthier</a>
 * @date 29 July 2009
 *
 */
public class ClockServiceTest extends BaseSessionBeanFixture<ClockServiceBean> {
	
	private static Log logger = LogFactory.getLog(ClockServiceTest.class);
	
	@SuppressWarnings("unchecked")
	private static final Class[] usedBeans = {};
	
	
	/**
	 * Constructor 
	 */
	public ClockServiceTest() {
		super(ClockServiceBean.class, usedBeans);
	}
	
	/**
	 * Test the Clock service.
	 * We can't test directly by comparing to a hard-coded string, as the date is always changing,
	 * so we only test if the format is correct, by matching it to a given regex.
	 */
	public void testClockService() {
		logger.debug("Testing ClockService");
		
		ClockService service = getBeanToTest();
		
		try {
			String time = service.getTime();
			logger.info("time: " + time);
			// French format
			assertTrue(Pattern.matches("\\d\\d?\\s[^\\s]*\\s\\d{4}\\s\\d{2}:\\d{2}$", time));
			// US format
			// assertTrue(Pattern.matches("[^\\s]*\\s\\d\\d?,\\s\\d{4}\\s\\d{1,2}:\\d{2}\\s[AP]M$", time));
		} catch (ClockServiceException e) {
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

}
