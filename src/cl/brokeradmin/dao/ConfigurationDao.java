/*
 * Creado 08-09-2008
 *
 * $Id$
 *
 * Copyright Continuum Ltda. (2008).
 *
 */
package cl.brokeradmin.dao;

import java.util.List;

import cl.brokeradmin.model.LocalConfigManagerConnectionParameters;


/**
 * @author jars <a href="mailto:jorge.rodriguez at continuum.cl">Jorge Al. Rodriguez Suarez</a>
 *
 */
public interface ConfigurationDao {
	
	// development enviroment
	static final String ENV_DEVELOPMENT = "development";
	
	// development enviroment
	static final String ENV_TEST = "test";
	
	// development enviroment
	static final String ENV_PRODUCTION = "production";
	
	/**
	 * Save an instance of a connection to an Configuration Manager Domain 
	 * in the local repository.
	 * Each instance also represents a connection to a Configuration Manager.
	 * @param connectionParameters
	 */
	 void saveConfigManagerConnectionParametersIfDoesntExist(LocalConfigManagerConnectionParameters connectionParameters);
	 
	 /**
	 * @param enviroment
	 * @param name
	 * @return
	 */
	LocalConfigManagerConnectionParameters getLocalConfigManagerParameters(String enviroment, String name);
	
	/**
	 * return the list of connection parameters from local repo
	 * @param enviroment Enviroment (ENV_DEVELOPMENT, ENV_TEST, ENV_PRODUCTION)
	 * @return
	 */
	public List<LocalConfigManagerConnectionParameters> getConfigManagerConfigurations(String enviroment);

}
