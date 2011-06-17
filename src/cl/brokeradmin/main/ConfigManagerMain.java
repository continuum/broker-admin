/*
 * Creado 08-09-2008
 *
 * $Id$
 *
 * Copyright Continuum Ltda. (2008).
 *
 */
package cl.brokeradmin.main;

import java.util.List;

import cl.brokeradmin.model.LocalConfigManagerConnectionParameters;

import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyException;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.LogProxy;
import com.ibm.broker.config.proxy.MessageFlowProxy;
import com.ibm.broker.config.proxy.SubscriptionsProxy;
import com.ibm.broker.config.proxy.TopicRootProxy;
import com.ibm.broker.config.proxy.TopologyProxy;

/**
 * @author jars <a href="mailto:jorge.rodriguez at continuum.cl">Jorge Al. Rodriguez Suarez</a>
 *
 */
public interface ConfigManagerMain {
	
	/**
	 * return an Configuration Manager object.
	 * Look for the connection in the local repository. 
	 * If it dosent exist, save the Connection paramters map.
	 * @param connParameters
	 * @return
	 */
	ConfigManagerProxy connectToConfigManager(LocalConfigManagerConnectionParameters connParameters) throws ConfigManagerProxyException;
	
	/**
	 * @param configManagerName
	 * @return
	 * @throws ConfigManagerProxyException
	 */
	ConfigManagerProxy getConfigManagerFromCache(String configManagerName) throws ConfigManagerProxyException;
	
	/**
	 * @param configManagerName
	 * @return
	 * @throws ConfigManagerProxyException
	 */
	TopologyProxy getConfigManagerTopology(String configManagerName) throws ConfigManagerProxyException;
	
	/**
	 * @param configManagerName
	 * @return
	 * @throws ConfigManagerProxyException
	 */
	SubscriptionsProxy getConfigManagerSubscription(String configManagerName) throws ConfigManagerProxyException;
	
	/**
	 * @param configManagerName
	 * @return
	 * @throws ConfigManagerProxyException
	 */
	TopicRootProxy getConfigManagerTopicRoot(String configManagerName) throws ConfigManagerProxyException;
	
	/**
	 * @param configManaName
	 * @return
	 * @throws ConfigManagerProxyException
	 */
	LogProxy getConfigManagerLog(String configManagerName) throws ConfigManagerProxyException;
	
	/**
	 * list the brokers of the topology (TopologyProxy) from a Configuration Manager
	 * @param configManagerName
	 * @return
	 */
	List<BrokerProxy> getBrokers(String configManagerName) throws ConfigManagerProxyException ;
	
	/**
	 * given the broker name (obtained from topology), get de executions groups
	 * @param brokerName
	 * @return
	 */
	List<ExecutionGroupProxy> getExecutionGroups(String configManagerName, String brokerName) throws ConfigManagerProxyException;
	
	/**
	 * @param configManagerName
	 * @param brokerName
	 * @param execGroup
	 * @return
	 * @throws ConfigManagerProxyException
	 */
	ExecutionGroupProxy getExecutionGroupByName(String configManagerName, String brokerName, String execGroup) throws ConfigManagerProxyException;
	
	/**
	 * given the brokerName and execution Group, obtain the messages flows
	 * @param brokerName
	 * @param executionGroup
	 * @return
	 */
	List<MessageFlowProxy> getMessagesFlow(String configManagerName, String brokerName, String executionGroup) throws ConfigManagerProxyException;

}
