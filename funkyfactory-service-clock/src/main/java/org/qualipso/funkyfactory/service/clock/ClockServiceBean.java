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
package org.qualipso.funkyfactory.service.clock;

import java.text.DateFormat;
import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ws.annotation.EndpointConfig;
import org.jboss.wsf.spi.annotation.WebContext;
import org.qualipso.factory.FactoryException;
import org.qualipso.factory.FactoryResource;
import org.qualipso.factory.core.CoreServiceException;

/**
 * Implementation of the ClockService. Provides a time service for the factory.
 * 
 * @author <a href="mailto:christophe.bouthier@loria.fr">Christophe Bouthier</a>
 * @date 27 July 2009 
 */
@Stateless(name = "Clock", mappedName = "ClockService")
@WebService(endpointInterface = "org.qualipso.funkyfactory.service.clock.ClockService", targetNamespace = "http://org.qualipso.funkyfactory.ws/factory", serviceName = "ClockService", portName = "ClockService")
@WebContext(contextRoot = "/funkyfactory-service", urlPattern = "/clock")
@SOAPBinding(style = Style.RPC)
@SecurityDomain(value = "JBossWSDigest")
@EndpointConfig(configName = "Standard WSSecurity Endpoint")
public class ClockServiceBean implements ClockService {

	/**
	 * Return the current time.
	 * 
	 * @return 	 the current server time
	 * @see org.qualipso.funkyfactory_service_clock.ClockService#getTime()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public String getTime() throws ClockServiceException {
		return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(new Date());
	}

	/**
	 * Return all ressources types supported by this service.
	 * Here, return an empty array, no ressources types are supported.
	 * 
	 * @return	empty array
	 * @see org.qualipso.factory.FactoryService#getResourceTypeList()
	 */
	public String[] getResourceTypeList() {
		return new String[0];
	}

	/**
	 * Return the name of the service.
	 * 
	 * @return    service name
	 * @see org.qualipso.factory.FactoryService#getServiceName()
	 */
	public String getServiceName() {
		return "ClockService";
	}

    /**
     * Returns an exception as this service doesn't manage resources.
     * 
     * @see org.qualipso.factory.FactoryService#findResource(java.lang.String)
     */
    public FactoryResource findResource(String arg0) throws FactoryException {
        throw new CoreServiceException("No Resource are managed by HelloWorld Service");
    }

}
