/*
 * Creado 15-09-2008
 *
 * $Id$
 *
 * Copyright Continuum Ltda. (2008).
 *
 */
package cl.brokeradmin.main;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ibm.broker.config.proxy.BrokerProxy;

import cl.brokeradmin.dao.ConfigurationDao;
import cl.brokeradmin.model.LocalConfigManagerConnectionParameters;

/**
 * @author jars <a href="mailto:jorge.rodriguez at continuum.cl">Jorge Al. Rodriguez Suarez</a>
 *
 */
public class ConfigManagerMainImplTest {
	
	protected ConfigManagerMainImpl configMain;
	
	protected static final String _CM_NAME = "VMWare_CM";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		ApplicationContext appCtx = new ClassPathXmlApplicationContext("brokeradmin-config.xml");
		
		configMain = (ConfigManagerMainImpl) appCtx.getBean("configManagerMain");
		
		LocalConfigManagerConnectionParameters params = new LocalConfigManagerConnectionParameters(_CM_NAME, "172.16.12.128", 2414, "WBRK6_DEFAULT_QUEUE_MANAGER", ConfigurationDao.ENV_DEVELOPMENT);
		configMain.connectToConfigManager(params);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}

	/**
	 * Test method for {@link cl.brokeradmin.main.ConfigManagerMainImpl#disconnectManagers()}.
	 */
	@Test
	public void testDisconnectManagers() {
		configMain.disconnectManagers();
	}

	/**
	 * Test method for {@link cl.brokeradmin.main.ConfigManagerMainImpl#connectToConfigManager(cl.brokeradmin.model.LocalConfigManagerConnectionParameters)}.
	 */
	@Test
	public void testConnectToConfigManager() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cl.brokeradmin.main.ConfigManagerMainImpl#getBrokers(java.lang.String)}.
	 */
	@Test
	public void testGetBrokers() throws Exception {
		List<BrokerProxy> brokers = this.configMain.getBrokers(_CM_NAME);
		for (BrokerProxy bp : brokers) {
			System.out.println(bp.getName());
		}
	}

	/**
	 * Test method for {@link cl.brokeradmin.main.ConfigManagerMainImpl#getExecutionGroups(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetExecutionGroups() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link cl.brokeradmin.main.ConfigManagerMainImpl#getMessagesFlow(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetMessagesFlow() {
		fail("Not yet implemented");
	}

}
