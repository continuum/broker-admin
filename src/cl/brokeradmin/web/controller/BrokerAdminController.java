/*
 * Creado 19/02/2008
 *
 * $Id$
 *
 * Copyright jrodriguez (2007). All rights reserved.
 * Retain all ownership and intellectual property rights in
 * the programs and any source code.
 */
package cl.brokeradmin.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.ibm.broker.config.proxy.AdministeredObject;
import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.CompletionCodeType;
import com.ibm.broker.config.proxy.ConfigManagerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyException;
import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.DeployResult;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.LogEntry;
import com.ibm.broker.config.proxy.LogProxy;
import com.ibm.broker.config.proxy.MessageFlowProxy;
import com.ibm.broker.config.proxy.Subscription;
import com.ibm.broker.config.proxy.SubscriptionsProxy;
import com.ibm.broker.config.proxy.TopicProxy;
import com.ibm.broker.config.proxy.TopicRootProxy;
import com.ibm.broker.config.proxy.TopologyProxy;

import cl.brokeradmin.dao.ConfigurationDao;
import cl.brokeradmin.main.ConfigManagerMain;
import cl.brokeradmin.model.LocalConfigManagerConnectionParameters;

/**
 * MultiAction Controller C del MVC para la aplicacion Diat
 * 
 * @author jars - <a href="mailto:jorge.rodriguez.suarez at gmail.com">Jorge Al. Rodriguez Suarez</a>
 *
 */
public class BrokerAdminController extends MultiActionController {
	
	// class log
	protected static final Log LOG = LogFactory.getLog(BrokerAdminController.class);
	
	// dao interface
	protected ConfigurationDao configurationDao;
	
	// date format for conversion
	protected DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	
	// main services interface proxy to configuration managers
	protected ConfigManagerMain configManagerMain;
	
	/**
	 * @throws ApplicationContextException
	 */
	public BrokerAdminController() throws ApplicationContextException {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @throws ApplicationContextException
	 */
	public BrokerAdminController(Object delegate)
			throws ApplicationContextException {
		super(delegate);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param configurationDao the configurationDao to set
	 */
	public void setConfigurationDao(ConfigurationDao configurationDao) {
		this.configurationDao = configurationDao;
	}

	/**
	 * @param configManagerMain the configManagerMain to set
	 */
	public void setConfigManagerMain(ConfigManagerMain configManagerMain) {
		this.configManagerMain = configManagerMain;
	}

	/**
	 * @param req
	 * @param resp
	 * @param ex
	 * @return
	 * @throws Exception
	 */
	protected ModelAndView jsonException(HttpServletRequest req, HttpServletResponse resp, final Exception ex) throws Exception {
		LOG.error(ex);
		return new ModelAndView(new View() {
			/* (non-Javadoc)
			 * @see org.springframework.web.servlet.View#getContentType()
			 */
			public String getContentType() {
				return "text/json";
			}
			/* (non-Javadoc)
			 * @see org.springframework.web.servlet.View#render(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
			 */
			public void render(Map model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
				resp.setCharacterEncoding("utf-8");
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				resp.getWriter().write("{ 'errorMsg' : '" + ex.getMessage() + "' }");
			}
		});
	}
	
	/**
	 * @param obj
	 * @param json
	 * @return
	 * @throws JSONException
	 * @throws ConfigManagerProxyException
	 */
	private JSONObject setCommonProperties(AdministeredObject obj, JSONObject json) throws Exception {
		return json.put("Usuario Ultimo Update", obj.getLastUpdateUser()).
		put("Descripcion", obj.getLongDescription()).
		put("Nombre", obj.getName()).
		put("Repositorio Timestamp", obj.getRepositoryTimestamp()).
		put("Ultimo Completion Code", obj.getLastCompletionCode()).
		put("Fecha Ultima actualizacion", dateFormat.format(obj.getTimeOfLastUpdate().getTime())).
		put("tipo", obj.getConfigurationObjectType()).
		put("Deployado", obj.isDeployed()).put("Compartido", obj.isShared());
	}
	
	/**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public ModelAndView managers(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		LOG.info("loading the configuration managers proxies from local repository...");
		
		List<LocalConfigManagerConnectionParameters> params = 
			this.configurationDao.getConfigManagerConfigurations(ServletRequestUtils.getStringParameter(req, "enviroment", "development"));
		
		// for each local config manager, show all
		
		JSONArray jsonArr = new JSONArray();
		for (LocalConfigManagerConnectionParameters lcp : params) {
			// connect to each one
			ConfigManagerProxy cmp = null;
			try {
				
				cmp = this.configManagerMain.connectToConfigManager(lcp);
				
			} catch (ConfigManagerProxyException e) {
				LOG.error(e);
				// en este punto no importa
			}
			
			jsonArr.put(configManagerToJson(lcp, cmp));
		};
		
		return new ModelAndView("render_object_to_json").addObject("json", jsonArr);
	}

	/**
	 * @param lcp
	 * @param cmp
	 * @return
	 * @throws JSONException
	 * @throws ConfigManagerProxyException
	 * @throws ConfigManagerProxyPropertyNotInitializedException
	 */
	private JSONObject configManagerToJson(LocalConfigManagerConnectionParameters lcp, ConfigManagerProxy cmp) throws Exception {
		JSONObject properties = null;
		if (null != cmp) {
			properties = this.setCommonProperties(cmp, new JSONObject()).
				put("Version", cmp.getConfigManagerFullVersion()).
				put("Arquitectura", cmp.getConfigManagerOSArch()).
				put("OS", cmp.getConfigManagerOSName()).
				put("OS version", cmp.getConfigManagerOSVersion()).
				put("Config Manager Version", cmp.getConfigManagerVersion()).
				put("Host", lcp.getHost()).
				put("Puerto", lcp.getPort());
		}
		return new JSONObject().
				put("text", lcp.getName() + "[" + lcp.getHost() + " : " + lcp.getPort() + "]").
				put("x_class", "configuration_manager").
				put("x_connected", null != cmp).
				put("x_type", lcp.getClass().getName()).
				put("x_name", lcp.getName()).
				put("x_mvc_method", "managedObjects").
				put("properties", properties);
	}
	
	/**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public ModelAndView managedObjects(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		LOG.info("loading the topology managers proxies from local repository...");
		// name of the configuration manager (keep in cache)
		String manager = ServletRequestUtils.getRequiredStringParameter(req, "x_name");
		boolean connected = ServletRequestUtils.getRequiredBooleanParameter(req, "x_connected");
		// if not connected, connect first
		if (!connected) {
			ConfigManagerProxy cmp = this.configManagerMain.connectToConfigManager(
					this.configurationDao.getLocalConfigManagerParameters(
							ServletRequestUtils.getStringParameter(req, "enviroment", "development"), manager));
		}
		
		// managed objects result
		TopologyProxy topology = this.configManagerMain.getConfigManagerTopology(manager);
		SubscriptionsProxy subscriptions = this.configManagerMain.getConfigManagerSubscription(manager);
		TopicRootProxy topicRoot = this.configManagerMain.getConfigManagerTopicRoot(manager);
		LogProxy log = this.configManagerMain.getConfigManagerLog(manager);
		
		// for each managed object
		JSONArray jsonArr = new JSONArray().
			put(
					new JSONObject().
						put("text", StringUtils.isEmpty(topology.getName()) ? "Broker Topology" : topology.getName()).
						put("x_class", "topology").
						put("x_type", topology.getClass().getName()).
						put("x_name", manager).
						put("x_mvc_method", "brokers").
						put("properties", this.setCommonProperties(topology, new JSONObject()).
								put("# Conexiones", topology.getNumberOfConnections()))
				).
			put(
					new JSONObject().
						put("text", StringUtils.isEmpty(subscriptions.getName()) ? "Subscriptions" : subscriptions.getName()).
						put("x_class", "subscriptions").
						put("x_type", subscriptions.getClass().getName()).
						put("x_name", manager).
						put("x_mvc_method", "subscriptions").
						put("properties", this.setCommonProperties(subscriptions, new JSONObject()))
				).
			put(
					new JSONObject().
						put("text", StringUtils.isEmpty(topicRoot.getName()) ? "Topics" : topicRoot.getName()).
						put("x_class", "topics").
						put("x_type", topicRoot.getClass().getName()).
						put("x_name", manager).
						put("x_mvc_method", "topics").
						put("properties", this.setCommonProperties(topicRoot, new JSONObject()))
				).
			put(
					new JSONObject().
						put("text", StringUtils.isEmpty(log.getName()) ? "Log Events" : log.getName()).
						put("x_class", "log").
						put("x_type", log.getClass().getName()).
						put("x_name", manager).
						put("leaf", true).
						put("properties", this.setCommonProperties(log, new JSONObject()))
				);

		return new ModelAndView("render_object_to_json").addObject("json", jsonArr);
	}
	
	/**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public ModelAndView subscriptions(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		LOG.info("loading all elements from the subscription proxy");
		String manager = ServletRequestUtils.getRequiredStringParameter(req, "x_name");
		Enumeration<Subscription> subscriptions = this.configManagerMain.getConfigManagerSubscription(manager).elements();
		// recue all subscriptions
		JSONArray jsonArr = new JSONArray();
		for (;subscriptions.hasMoreElements();) {
			Subscription s = subscriptions.nextElement();
			jsonArr.put(new JSONObject().
					put("text", s.getBroker() + " - " + s.getTopicName() + " - " + s.getUser()).
					put("x_class", "subscription").
					put("x_type", s.getClass().getName()).
					put("x_manager", manager).
					put("leaf", true).
					put("properties", new JSONObject().
							put("Broker", s.getBroker()).
							put("Cliente", s.getClient()).
							put("Fecha registro", s.getRegistrationDate()).
							put("Punto de Subscripcion", s.getSubscriptionPoint()).
							put("Topic", s.getTopicName()).
							put("Usuario", s.getUser())));
		}
		return new ModelAndView("render_object_to_json").addObject("json", jsonArr);
	}
	
	/**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public ModelAndView topics(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		LOG.info("loading all elements from the topic proxy");
		String manager = ServletRequestUtils.getRequiredStringParameter(req, "x_name");
		Enumeration<TopicProxy> topics = this.configManagerMain.getConfigManagerTopicRoot(manager).getChildTopics(null);
		// recue all subscriptions
		JSONArray jsonArr = new JSONArray();
		for (;topics.hasMoreElements();) {
			TopicProxy t = topics.nextElement();
			jsonArr.put(new JSONObject().
					put("text", t.getTopicName()).
					put("x_class", "topic").
					put("x_type", t.getClass().getName()).
					put("x_manager", manager).
					put("leaf", true).
					put("properties", this.setCommonProperties(t, new JSONObject()).
							put("Nombre", t.getTopicName()).
							put("Multicast", t.getMulticastEnabled()).
							put("Multicast encripted", t.getMulticastEncrypted()).
							put("Grupo Multicast IPv4", t.getMulticastIPv4GroupAddress()).
							put("Grupo Multicast IPv6", t.getMulticastIPv6GroupAddress()).
							put("Multicast QoS", t.getMulticastQualityOfService()).
							put("Numero de politicas", t.getNumberOfPolicies())));
		}
		return new ModelAndView("render_object_to_json").addObject("json", jsonArr);
	}

	/**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public ModelAndView brokers(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		LOG.info("loading the brokers from configuration managers...");
		// format can be xml or json
		String manager = ServletRequestUtils.getRequiredStringParameter(req, "x_name");
		
		List<BrokerProxy> brokers = this.configManagerMain.getBrokers(manager);
		
		// for each broker, show all in json notation
		
		JSONArray jsonArr = new JSONArray();
		for (BrokerProxy broker : brokers) {
			jsonArr.put(new JSONObject().
					put("text", broker.getName()).
					put("x_class", "broker").
					put("x_type", broker.getClass().getName()).
					put("x_cmp", manager).
					put("x_name", broker.getName()).
					put("x_mvc_method", "execGroups").
					put("properties", this.setCommonProperties(broker, new JSONObject()).
							put("Protocolo Autenticacion", broker.getAuthenticationProtocols()).
							put("Broker Host", broker.getInterbrokerHost()).
							put("Broker Port", broker.getInterbrokerPort()).
							put("Nombre Queue Manager", broker.getQueueManagerName()).
							put("SSL Connector", broker.getSSLConnectorEnabled()).
							put("SSL Key", broker.getSSLKeyRingFileName()).
							put("SSL Password File", broker.getSSLPasswordFileName()).
							put("Running", broker.isRunning())
				)
			);
		};
		
		return new ModelAndView("render_object_to_json").addObject("json", jsonArr);
	}
	
	/**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public ModelAndView execGroups(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		LOG.info("loading the executions groups from broker...");
		// format can be xml or json
		String manager = ServletRequestUtils.getRequiredStringParameter(req, "x_cmp");
		String broker = ServletRequestUtils.getRequiredStringParameter(req, "x_name");
		
		List<ExecutionGroupProxy> egroups = this.configManagerMain.getExecutionGroups(manager, broker);
		
		// for each broker, show all in json notation
		
		JSONArray jsonArr = new JSONArray();
		for (ExecutionGroupProxy ex : egroups) {
			jsonArr.put(new JSONObject().
					put("text", ex.getName()).
					put("x_class", "executionGroup").
					put("x_type", ex.getClass().getName()).
					put("x_cmp", manager).
					put("x_broker", broker).
					put("x_name", ex.getName()).
					put("x_mvc_method", "messageFlows").
					put("properties", this.setCommonProperties(ex, new JSONObject()).put("Running", ex.isRunning())
				)
			);
		};

		return new ModelAndView("render_object_to_json").addObject("json", jsonArr);
	}
	
	/**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public ModelAndView messageFlows(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// format can be xml or json
		String manager = ServletRequestUtils.getRequiredStringParameter(req, "x_cmp");
		String broker = ServletRequestUtils.getRequiredStringParameter(req, "x_broker");
		String execGroup = ServletRequestUtils.getRequiredStringParameter(req, "x_name");
		
		List<MessageFlowProxy> msgFlows = this.configManagerMain.getMessagesFlow(manager, broker, execGroup);
		
		// for each broker, show all in json notation
		
		JSONArray jsonArr = new JSONArray();
		for (MessageFlowProxy mf : msgFlows) {
			jsonArr.put(new JSONObject().
					put("text", mf.getName()).
					put("leaf", true).
					put("x_class", "messageFlow").
					put("x_type", mf.getClass().getName()).
					put("x_cmp", manager).
					put("x_broker", broker).
					put("x_exgroup", execGroup).
					put("x_name", mf.getName()).
					put("properties", this.setCommonProperties(mf, new JSONObject()).
							put("Instancias adicionales", mf.getAdditionalInstances()).
							put("BAR", mf.getBARFileName()).
							put("Commit Count", mf.getCommitCount()).
							put("Commit Interval", mf.getCommitInterval()).
							put("Coordinated Transaction", mf.getCoordinatedTransaction()).
							put("Hora ultimo deployment", mf.getDeployTime()).
							put("Extension archivo", mf.getFileExtension()).
							put("Nombre completo", mf.getFullName()).
							put("Hora ultima modificacion", mf.getModifyTime()).
							put("Version", mf.getVersion()).put("Running", mf.isRunning())
				)
			);
		};
		
		return new ModelAndView("render_object_to_json").addObject("json", jsonArr);
	}
	
	/**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public ModelAndView log(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		LOG.info("loading event logs entry for current user");
		String manager = ServletRequestUtils.getRequiredStringParameter(req, "manager");
		// get entradas del log
		Enumeration<LogEntry> logs = this.configManagerMain.getConfigManagerLog(manager).elements();
		JSONObject entries = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		for (;logs.hasMoreElements();) {
			LogEntry entry = logs.nextElement();
			jsonArr.put(new JSONObject().
					put("message", entry.getMessage()).
					put("bip", entry.getMessageNumber()).
					put("isError", entry.isErrorMessage()).
					put("source", entry.getSource()).
					put("timestamp", this.dateFormat.format(entry.getTimestamp())).
					put("detail", entry.getDetail()));
		}
		return new ModelAndView("render_object_to_json").addObject("json", entries.put("entries", jsonArr));
	}
	
	/**
	 * @param req
	 * @param resp
	 * @param connectParams
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addCM(HttpServletRequest req, HttpServletResponse resp, LocalConfigManagerConnectionParameters connParameters) throws Exception {
		// try to connect
		ConfigManagerProxy cmp = this.configManagerMain.connectToConfigManager(connParameters);
		return new ModelAndView("render_object_to_json").addObject("json", this.configManagerToJson(connParameters, cmp).put("success", true));
	}
	
	/**
	 * @param req
	 * @param resp
	 * @param connParameters
	 * @return
	 * @throws Exception
	 */
	public ModelAndView stopMsgFlows(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// format can be xml or json
		String manager = ServletRequestUtils.getRequiredStringParameter(req, "x_cmp");
		String broker = ServletRequestUtils.getRequiredStringParameter(req, "x_broker");
		String execGroup = ServletRequestUtils.getRequiredStringParameter(req, "x_name");
		
		// stop all flows
		this.configManagerMain.getExecutionGroupByName(manager, broker, execGroup).stopMessageFlows(true);
		// return empty object
		return new ModelAndView("render_object_to_json").addObject("json", new JSONObject());
	}
	
	/**
	 * @param req
	 * @param resp
	 * @param connParameters
	 * @return
	 * @throws Exception
	 */
	public ModelAndView startMsgFlows(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// format can be xml or json
		String manager = ServletRequestUtils.getRequiredStringParameter(req, "x_cmp");
		String broker = ServletRequestUtils.getRequiredStringParameter(req, "x_broker");
		String execGroup = ServletRequestUtils.getRequiredStringParameter(req, "x_name");
		
		// stop all flows
		this.configManagerMain.getExecutionGroupByName(manager, broker, execGroup).startMessageFlows();
		// return empty object
		return new ModelAndView("render_object_to_json").addObject("json", new JSONObject());
	}
	
	// actions over Configuration Manager
	
	/**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public ModelAndView cancelCMDeployment(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// format can be xml or json
		DeployResult dr = this.configManagerMain.getConfigManagerFromCache(
				ServletRequestUtils.getRequiredStringParameter(req, "x_name")).cancelDeployment(ServletRequestUtils.getRequiredIntParameter(req, "timeout"));
		// return empty object
		return new ModelAndView("render_object_to_json").addObject("json", new JSONObject().put("deployResult", deployResultToJson(dr)));
	}

	/**
	 * @param dr
	 * @return
	 * @throws JSONException
	 */
	private JSONObject deployResultToJson(DeployResult dr) throws JSONException {
		JSONObject result = new JSONObject().put("completionCode", dr.getCompletionCode());
		Enumeration<LogEntry> logs = dr.getLogEntries();
		JSONArray jsonArr = new JSONArray();
		for (;logs.hasMoreElements();) {
			LogEntry entry = logs.nextElement();
			jsonArr.put(new JSONObject().
					put("message", entry.getMessage()).
					put("bip", entry.getMessageNumber()).
					put("isError", entry.isErrorMessage()).
					put("source", entry.getSource()).
					put("timestamp", this.dateFormat.format(entry.getTimestamp())).
					put("detail", entry.getDetail()));
		}
		return result.put("log", jsonArr);
	}
	
	/**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addBrokerToTopology(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// get parameters
		String manager = ServletRequestUtils.getRequiredStringParameter(req, "manager");
		String name = ServletRequestUtils.getRequiredStringParameter(req, "name");
		String qmgr = ServletRequestUtils.getRequiredStringParameter(req, "queueManager");
		String executionGroupName = ServletRequestUtils.getRequiredStringParameter(req, "execGroupName");
		
		if (LOG.isInfoEnabled()) {
			LOG.info("asociating the broker : " + name + ", in the queue manager : " + qmgr);
		}
		
		// asociate and create execution group
		BrokerProxy bp = this.configManagerMain.getConfigManagerTopology(manager).createBroker(name, qmgr);
		bp.createExecutionGroup(executionGroupName);
		// deploy changes
		DeployResult dr = bp.deploy(ServletRequestUtils.getRequiredIntParameter(req, "timeout"));
		
		// return empty object
		return new ModelAndView("render_object_to_json").addObject("json", new JSONObject().put("success", true).put("deployResult", this.deployResultToJson(dr)));
	}
}
