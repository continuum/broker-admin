/*
 * Creado 08-09-2008
 *
 * $Id$
 *
 * Copyright Continuum Ltda. (2008).
 *
 */
package cl.brokeradmin.main;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cl.brokeradmin.annotation.Log;
import cl.brokeradmin.model.LocalConfigManagerConnectionParameters;

import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.ConfigManagerConnectionParameters;
import com.ibm.broker.config.proxy.ConfigManagerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyException;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.LogProxy;
import com.ibm.broker.config.proxy.MQConfigManagerConnectionParameters;
import com.ibm.broker.config.proxy.MessageFlowProxy;
import com.ibm.broker.config.proxy.Subscription;
import com.ibm.broker.config.proxy.SubscriptionsProxy;
import com.ibm.broker.config.proxy.TopicRootProxy;
import com.ibm.broker.config.proxy.TopologyProxy;

/**
 * @author jars <a href="mailto:jorge.rodriguez at continuum.cl">Jorge Al. Rodriguez Suarez</a>
 *
 */
public class ConfigManagerMainImpl implements ConfigManagerMain {
	
	// map of key - managers connecteds
	private static final Map<String, ConfigManagerProxy> _MANAGERS;
	
	static {
		_MANAGERS = new ConcurrentHashMap<String, ConfigManagerProxy>();
	}
	
	/**
	 * 
	 */
	public ConfigManagerMainImpl() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * destroy methods
	 */
	@Log(
			level = "info",
			args = true
		)
	public void disconnectManagers() {
		for (String cmpk : _MANAGERS.keySet()) {
			_MANAGERS.get(cmpk).disconnect();
			_MANAGERS.remove(cmpk);
		}
	}

	/* (non-Javadoc)
	 * @see cl.brokeradmin.main.ConfigManagerMain#connectToConfigManager(java.util.Map)
	 */
	@Log(
			level = "info",
			args = true
		)
	public ConfigManagerProxy connectToConfigManager(LocalConfigManagerConnectionParameters connParameters) 
		throws ConfigManagerProxyException {
		// connect to managers
		ConfigManagerConnectionParameters cmcp = new MQConfigManagerConnectionParameters(connParameters.getHost(),
				connParameters.getPort(), connParameters.getQueueManagerName());
		// get instance to config manager proxy
		ConfigManagerProxy cmp = ConfigManagerProxy.getInstance(cmcp);
		// ie everething ok - guardar en cache (rustico, map) la conexion al ConfigManagerProxy
		_MANAGERS.put(connParameters.getName(), cmp);
		// return the connection
		return cmp;
	}

	/* (non-Javadoc)
	 * @see cl.brokeradmin.main.ConfigManagerMain#getConfigManagerFromCache(java.lang.String)
	 */
	@Log(
			level = "info",
			args = true
		)
	public ConfigManagerProxy getConfigManagerFromCache(String configManagerName) throws ConfigManagerProxyException {
		// return ConfigManagerProxy from cache
		return _MANAGERS.get(configManagerName);
	}

	/* (non-Javadoc)
	 * @see cl.brokeradmin.main.ConfigManagerMain#getConfigManagerSubscription(java.lang.String)
	 */
	@Log(
			level = "info",
			args = true
		)
	public SubscriptionsProxy getConfigManagerSubscription(String configManagerName) throws ConfigManagerProxyException {
		// return the Subscriptions proxy object of the configuration manager
		return _MANAGERS.get(configManagerName).getSubscriptions(null, null, null, null, null, null);
	}

	/* (non-Javadoc)
	 * @see cl.brokeradmin.main.ConfigManagerMain#getConfigManagerTopicRoot(java.lang.String)
	 */
	@Log(
			level = "info",
			args = true
		)
	public TopicRootProxy getConfigManagerTopicRoot(String configManagerName) throws ConfigManagerProxyException {
		// return the topic root
		return _MANAGERS.get(configManagerName).getTopicRoot();
	}

	/* (non-Javadoc)
	 * @see cl.brokeradmin.main.ConfigManagerMain#getConfigManagerTopoogy(java.lang.String)
	 */
	@Log(
			level = "info",
			args = true
		)
	public TopologyProxy getConfigManagerTopology(String configManagerName) throws ConfigManagerProxyException {
		// return the 1 - 1 topoology proxy object of the configuration manager
		return _MANAGERS.get(configManagerName).getTopology();
	}

	/* (non-Javadoc)
	 * @see cl.brokeradmin.main.ConfigManagerMain#getConfigManagerLog(java.lang.String)
	 */
	@Log(
			level = "info",
			args = true
		)
	public LogProxy getConfigManagerLog(String configManagerName) throws ConfigManagerProxyException {
		// return the Log
		return _MANAGERS.get(configManagerName).getLog();
	}

	/* (non-Javadoc)
	 * @see cl.brokeradmin.main.ConfigManagerMain#getBrokers()
	 */
	@Log(
			level = "info",
			args = true
		)
	public List<BrokerProxy> getBrokers(String configManagerName) throws ConfigManagerProxyException {
		// get all brokers
		return Collections.list(_MANAGERS.get(configManagerName).getTopology().getBrokers(null));
	}

	/* (non-Javadoc)
	 * @see cl.brokeradmin.main.ConfigManagerMain#getExecutionGroups(java.lang.String)
	 */
	@Log(
			level = "info",
			args = true
		)
	public List<ExecutionGroupProxy> getExecutionGroups(String configManagerName, String brokerName) throws ConfigManagerProxyException {
		// get all executions group
		return Collections.list(_MANAGERS.get(configManagerName).getTopology().getBrokerByName(brokerName).getExecutionGroups(null));
	}

	/* (non-Javadoc)
	 * @see cl.brokeradmin.main.ConfigManagerMain#getExecutionGroupByName(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Log(
			level = "info",
			args = true
		)
	public ExecutionGroupProxy getExecutionGroupByName(String configManagerName, String brokerName, String execGroup) throws ConfigManagerProxyException {
		// get Execution Group By Name
		return _MANAGERS.get(configManagerName).getTopology().getBrokerByName(brokerName).getExecutionGroupByName(execGroup);
	}

	/* (non-Javadoc)
	 * @see cl.brokeradmin.main.ConfigManagerMain#getMessagesFlow(java.lang.String, java.lang.String)
	 */
	@Log(
			level = "info",
			args = true
		)
	public List<MessageFlowProxy> getMessagesFlow(String configManagerName, String brokerName, String executionGroup) throws ConfigManagerProxyException {
		// return all messages flows
		return Collections.list(_MANAGERS.get(configManagerName).getTopology().getBrokerByName(brokerName).getExecutionGroupByName(executionGroup).getMessageFlows(null));
	}

}
