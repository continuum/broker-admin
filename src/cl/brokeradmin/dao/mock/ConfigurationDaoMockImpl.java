/*
 * Creado 08-09-2008
 *
 * $Id$
 *
 * Copyright Continuum Ltda. (2008).
 *
 */
package cl.brokeradmin.dao.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.brokeradmin.dao.ConfigurationDao;
import cl.brokeradmin.model.LocalConfigManagerConnectionParameters;

/**
 * @author jars <a href="mailto:jorge.rodriguez at continuum.cl">Jorge Al. Rodriguez Suarez</a>
 *
 */
public class ConfigurationDaoMockImpl implements ConfigurationDao {
	
	private static final Map<String, List<LocalConfigManagerConnectionParameters>> REPO;
	
	static {
		
		REPO = new HashMap<String, List<LocalConfigManagerConnectionParameters>>();

		List<LocalConfigManagerConnectionParameters> l = new ArrayList<LocalConfigManagerConnectionParameters>();
		//l.add(new LocalConfigManagerConnectionParameters("DEV-Default", "172.16.101.128", 2414, "WBRK6_DEFAULT_QUEUE_MANAGER", ConfigurationDao.ENV_DEVELOPMENT));
		//l.add(new LocalConfigManagerConnectionParameters("VMWare_JorgeConfigManager", "172.16.12.128", 3414, "", ConfigurationDao.ENV_DEVELOPMENT));
		
		REPO.put(ConfigurationDao.ENV_DEVELOPMENT, l);
		
	};

	/**
	 * 
	 */
	public ConfigurationDaoMockImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see cl.brokeradmin.dao.ConfigurationDao#getConfigManagerConfigurations(java.lang.String)
	 */
	public List<LocalConfigManagerConnectionParameters> getConfigManagerConfigurations(String enviroment) {
		return new ArrayList<LocalConfigManagerConnectionParameters> (REPO.get(enviroment));
	}

	/* (non-Javadoc)
	 * @see cl.brokeradmin.dao.ConfigurationDao#saveConfigManagerConnectionParametersIfDoesntExist(cl.brokeradmin.model.LocalConfigManagerConnectionParameters)
	 */
	public void saveConfigManagerConnectionParametersIfDoesntExist(LocalConfigManagerConnectionParameters connectionParameters) {
		// TODO Auto-generated method stub
		if (null == this.getLocalConfigManagerParameters(connectionParameters.getEnviroment(), connectionParameters.getName())) {
			REPO.get(connectionParameters.getEnviroment()).add(connectionParameters);
		}
	}

	/* (non-Javadoc)
	 * @see cl.brokeradmin.dao.ConfigurationDao#getLocalConfigManagerParameters(java.lang.String, java.lang.String)
	 */
	public LocalConfigManagerConnectionParameters getLocalConfigManagerParameters(String enviroment, String name) {
		// TODO Auto-generated method stub
		List<LocalConfigManagerConnectionParameters> l = REPO.get(enviroment);
		if (null != l && l.size() > 0) {
			for (LocalConfigManagerConnectionParameters lcp : l) {
				if (name.equals(lcp.getName())) {
					return lcp;
				}
			}
		}
		return null;
	}

}
