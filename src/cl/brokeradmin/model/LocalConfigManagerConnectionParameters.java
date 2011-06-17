/*
 * Creado 09-09-2008
 *
 * $Id$
 *
 * Copyright Continuum Ltda. (2008).
 *
 */
package cl.brokeradmin.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * JavaBean that represent the parameters needed for connecting to a ConfigurationManager
 * 
 * @author jars <a href="mailto:jorge.rodriguez at continuum.cl">Jorge Al. Rodriguez Suarez</a>
 *
 */
public class LocalConfigManagerConnectionParameters {
	
	// name of configuration manager
	private String name;
	
	// name or ip of the host
	private String host;
	
	// port where is listening
	private int port;
	
	// name of the queue manager
	private String queueManagerName;
	
	// enviroment whre is saved this configuration
	private String enviroment;
	
	// if are current connected to the config manager (ip:port)
	private boolean connected;

	/**
	 * 
	 */
	public LocalConfigManagerConnectionParameters() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param name
	 * @param host
	 * @param port
	 * @param queueManagerName
	 */
	public LocalConfigManagerConnectionParameters(String name, String host, int port, String queueManagerName, String enviroment) {
		super();
		this.name = name;
		this.host = host;
		this.port = port;
		this.queueManagerName = queueManagerName;
		this.enviroment = enviroment;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the queueManagerName
	 */
	public String getQueueManagerName() {
		return queueManagerName;
	}

	/**
	 * @param queueManagerName the queueManagerName to set
	 */
	public void setQueueManagerName(String queueManagerName) {
		this.queueManagerName = queueManagerName;
	}
	
	/**
	 * @return the enviroment
	 */
	public String getEnviroment() {
		return enviroment;
	}

	/**
	 * @param enviroment the enviroment to set
	 */
	public void setEnviroment(String enviroment) {
		this.enviroment = enviroment;
	}

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * @param connected the connected to set
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
