/*
 * Creado 09-09-2008
 *
 * $Id$
 *
 * Copyright Continuum Ltda. (2008).
 *
 */
package cl.brokeradmin.aop.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import cl.brokeradmin.dao.ConfigurationDao;
import cl.brokeradmin.model.LocalConfigManagerConnectionParameters;

/**
 * @author jars <a href="mailto:jorge.rodriguez at continuum.cl">Jorge Al. Rodriguez Suarez</a>
 *
 */

@Aspect
public class BrokerAdminMainAspect {
	
	// dao interface
	private ConfigurationDao configurationDao;
	
	/**
	 * 
	 */
	public BrokerAdminMainAspect() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param configurationDao the configurationDao to set
	 */
	public void setConfigurationDao(ConfigurationDao configurationDao) {
		this.configurationDao = configurationDao;
	}
	
	/**
	 * @param connectionParameters
	 */
	@AfterReturning("within(cl.brokeradmin.main.ConfigManagerMainImpl) && " +
			"execution(* connectToConfigManager(cl.brokeradmin.model.LocalConfigManagerConnectionParameters)) &&" +
			"args(parameters)")
	public void saveConfigManagerConnectionParameters(LocalConfigManagerConnectionParameters parameters) {
		// save the config if this dosent exist
		this.configurationDao.saveConfigManagerConnectionParametersIfDoesntExist(parameters);
	}

}
