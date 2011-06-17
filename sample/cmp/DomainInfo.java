/*
 * Sample program for use with Product         
 *  ProgIds: 5724-J06 5724-J05 5724-J04 5697-J09 5655-M74 5655-M75 5648-C63
 *  (C) Copyright IBM Corporation 2004.                     
 * All Rights Reserved * Licensed Materials - Property of IBM
 *
 * This sample program is provided AS IS and may be used, executed,
 * copied and modified without royalty payment by customer
 *
 * (a) for its own instruction and study,
 * (b) in order to develop applications designed to run with an IBM
 *     WebSphere product, either for customer's own internal use or for
 *     redistribution by customer, as part of such an application, in
 *     customer's own products.
 */
package cmp;

import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Properties;

import cmp.common.ResourcesHandler;

import com.ibm.broker.config.proxy.AdministeredObject;
import com.ibm.broker.config.proxy.AdministeredObjectListener;
import com.ibm.broker.config.proxy.AttributeConstants;
import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.CompletionCodeType;
import com.ibm.broker.config.proxy.ConfigManagerConnectionParameters;
import com.ibm.broker.config.proxy.ConfigManagerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyException;
import com.ibm.broker.config.proxy.ConfigManagerProxyLoggedException;
import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.MQConfigManagerConnectionParameters;
import com.ibm.broker.config.proxy.MQPropertyFileConfigManagerConnectionParameters;
import com.ibm.broker.config.proxy.MessageFlowProxy;
import com.ibm.broker.config.proxy.TopologyProxy;

/*****************************************************************************
 * <P>An application to display information on objects in a specified
 * Configuration Manager's domain.
 *
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>DomainInfo</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Provides example code that shows how to use the
 *     Configuration Manager Proxy API to display domain information.
 *     </UL>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Internal Collaborators</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *       <LI><TT>cmp.ResourcesHandler</TT>
 *     </UL>
 *   </TD>
 * </TR>
 * </TABLE>
 * <pre>
 *
 * Change Activity:
 * -------- ----------- -------------   ------------------------------------
 * Reason:  Date:       Originator:     Comments:
 * -------- ----------- -------------   ------------------------------------
 * 25103.6  2004-04-08  HDMPL           v6 Release
 *
 * </pre>
 *
 * @version Samples/ConfigManagerProxy/cmp/DomainInfo.java, Config.Proxy, S000, S000-L50818.2 1.9
 *****************************************************************************/

public class DomainInfo implements AdministeredObjectListener {
    
    /**
     * Object that defines the connection to the Configuration
     * Manager.
     */
    ConfigManagerProxy connectedInstance;
    
    /**
     * Indentation to use when displaying broker information
     */
    private final static String BROKER_INDENT = "";
    
    /**
     * Indentation to use when displaying execution group information
     */
    private final static String EG_INDENT = "  ";
    
    /**
     * Indentation to use when displaying message flow information
     */
    private final static String MF_INDENT = "    ";
    
    /**
     * True if and only if the application will run continuously;
     * that is, wait for incoming state changes.
     */
    private boolean interactiveMode;
    
    /**
     * The default host name of the Configuration Manager if
     * a .configmgr filename is not supplied
     */
    private final static String DEFAULT_CONFIG_HOSTNAME = "localhost";
    
    /**
     * The default port of the Configuration Manager if
     * a .configmgr filename is not supplied
     */
    private final static int DEFAULT_CONFIG_PORT = 1414;
    
    /**
     * The default queue manager name of the Configuration Manager if
     * a .configmgr filename is not supplied
     */
    private final static String DEFAULT_CONFIG_QMGR = "";
    
    /**
     * Main method. Starts an instance of the domain information
     * program.
     * @param args use -i to enter interactive mode.
     */
    public static void main(String[] args) {
        
        boolean interactiveMode = false;
        boolean finished = false;
        String filename = null;
        
        // Parse the command line arguments
        for (int i=0; i<args.length; i++) {
            if ((args[i].equals("-i")) || (args[i].equals("/i"))) {
                interactiveMode = true;
            } else if ((args[i].equals("-h")) ||
                       (args[i].equals("-help")) ||
                       (args[i].equals("-?")) ||
                       (args[i].equals("/h")) ||
                       (args[i].equals("/help")) ||
                       (args[i].equals("/?"))) {
                displayUsageInfo();
                finished = true;
            } else {
                // An unflagged parameter was supplied.
                // Set it to the .configmgr filename
                // if it has not already been set.
                if (filename == null) {
                    filename = args[i];
                } else {
                    // Unrecognized parameter
                    finished = true;
                    displayUsageInfo();
                }
            }
        }
        
        if (!finished) {
            DomainInfo di = new DomainInfo(filename, interactiveMode);
            di.go();       
        }
    }
    
    /**
     * Connects to a Configuration Manager using the supplied
     * parameters and sends to the log complete information on
     * its domain.
     * @param filename The name of the filename used to provide
     * the connection details to the Configuration Manager.
     * May be null, in which case the default parameters (from
     * mqsicfgutil.ini) will be used.
     * @param interactiveMode if and only if the value is true,
     * the application will listen for changes to the runstate
     * indefinitely once the initial information has been displayed.
     */
    public DomainInfo(String filename, boolean interactiveMode) {
        this.interactiveMode = interactiveMode;
        connectedInstance = connect(filename);
        
        if (connectedInstance == null) {
            displayUsageInfo();
        }
    }
    
    /**
     * Displays the syntax for the DomainInfo command.
     */
    private static void displayUsageInfo() {
        System.err.println("\n"+ResourcesHandler.getNLSResource(ResourcesHandler.DOMAININFO_HELP));
    }

    /**
     * Displays information on objects in the domain. If the Config
     * Manager Proxy could not connect to the Configuration Manager
     * using the parameters described on the constructor, this
     * method does nothing.
     */
    private void go() {
        if (connectedInstance!=null) {
            TopologyProxy topology;
            try {
                topology = connectedInstance.getTopology();
                displayDomainInfo(topology);
                
                if (interactiveMode) {
                    log(ResourcesHandler.getNLSResource(ResourcesHandler.LISTENING));
                    
                    // In interactive mode, the Configuration Manager Proxy
                    // will call the methods beginning 'process...' whenever
                    // the state of the registered domain objects changes.
                    // This happens on a separate notification thread.
                    // 
                    // At this point, the application can do no
                    // more but listen for these state changes. However,
                    // if the main thread were to finish, the JVM would exit
                    // straight away (as the notification thread used by the
                    // Configuration Manager Proxy is daemonic).
                    //
                    // Therefore, the following loop prevents the application
                    // from finishing. One could improve the application by
                    // waiting for user input here (e.g. a 'quit' command).
                    while (true) {
	                    try {
	                        Thread.sleep(10000);
	                    } catch (InterruptedException ex) {
	                        // ignore
	                    }
                    }
                }
                connectedInstance.disconnect();
                log(ResourcesHandler.getNLSResource(ResourcesHandler.DISCONNECTED));
            } catch (ConfigManagerProxyException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Connects to the Queue Manager, on which a Configuration Manager is
     * listening, using the supplied parameters
     * @param filename Location of the file used to provide
     * the connection characteristics to the Configuration Manager.
     * If null, the default connection parameters will be used.
     * @return ConfigManagerProxy connected instance. If the connection
     * could not be established, null is returned.
     */
    private ConfigManagerProxy connect(String filename) {
        ConfigManagerProxy cmp = null;
        ConfigManagerConnectionParameters cmcp;
        
        if (filename != null) {
            // A .configmgr filename was supplied 
            cmcp = new MQPropertyFileConfigManagerConnectionParameters(filename);
        } else {
            // No .configmgr filename was supplied, use defaults
            cmcp = new MQConfigManagerConnectionParameters(
                    DEFAULT_CONFIG_HOSTNAME,
                    DEFAULT_CONFIG_PORT,
                    DEFAULT_CONFIG_QMGR);
        }
        
        try {
            log(ResourcesHandler.getNLSResource(ResourcesHandler.CONNECTING));
            cmp = ConfigManagerProxy.getInstance(cmcp);
            log(ResourcesHandler.getNLSResource(ResourcesHandler.CONNECTED_TO_QMGR));
            
            // Ensure the CM is actually talking to us.
            // (This step isn't necessary - although it does allow us to
            // catch comms failures early.)
            boolean configMgrIsResponding = cmp.hasBeenUpdatedByConfigManager(true);
            
            if (configMgrIsResponding) {
                log(ResourcesHandler.getNLSResource(ResourcesHandler.CONNECTED_TO_CM));
            } else {
                log(ResourcesHandler.getNLSResource(ResourcesHandler.NO_RESPONSE_FROM_CM));
                cmp.disconnect();
                cmp = null;
            }
            
        } catch (ConfigManagerProxyLoggedException e) {
            log(ResourcesHandler.getNLSResource(ResourcesHandler.CONNECT_FAILED));
        }
        return cmp;
    }

    /**
     * Sends to the log name and runstate information for
     * each broker, execution group and message flow in the
     * runtime domain, optionally registering listeners
     * for each object.
     * @param topology Set of brokers for which information
     * is to be displayed.
     * @throws ConfigManagerProxyException
     * if communication problems with the Configuration Manager
     * meant that the required information could not be determined.
     */
    private void displayDomainInfo(TopologyProxy topology)
    throws ConfigManagerProxyException {
        
        // First, register the topology for changes if necessary.
        if (interactiveMode) {
            topology.registerListener(this);
        }
        
        // Get an unfiltered enumeration of all brokers in the domain
        Enumeration allBrokers = topology.getBrokers(null);
        
        while (allBrokers.hasMoreElements()) {
            BrokerProxy thisBroker = (BrokerProxy) (allBrokers.nextElement());
            if (interactiveMode) {
                thisBroker.registerListener(this);
            }
            displayBrokerRunstate(thisBroker);
            
            // Get an unfiltered enumation of all the execution groups in this broker
            Enumeration allEGsInThisBroker = thisBroker.getExecutionGroups(null);
            while (allEGsInThisBroker.hasMoreElements()) {
                ExecutionGroupProxy thisEG = (ExecutionGroupProxy) (allEGsInThisBroker.nextElement());
                if (interactiveMode) {
                    thisEG.registerListener(this);
                }
                displayExecutionGroupRunstate(thisEG);
                
                // Get an unfiltered enumeration of all message flows in this execution group
                Enumeration allFlowsInThisEG = thisEG.getMessageFlows(null);
                while (allFlowsInThisEG.hasMoreElements()) {
                    MessageFlowProxy thisFlow = (MessageFlowProxy) (allFlowsInThisEG.nextElement());
                    if (interactiveMode) {
                        thisFlow.registerListener(this);
                    }
                    displayMessageFlowRunstate(thisFlow);
                }
                
            }
        }
        
    }

    /**
     * Sends to the log a line of text describing the broker
     * and whether it is running
     * @param thisBroker BrokerProxy object representing a broker
     * @throws ConfigManagerProxyPropertyNotInitializedException
     * if communication problems with the Configuration Manager
     * meant that the required information could not be determined.
     */
    private void displayBrokerRunstate(BrokerProxy thisBroker)
    throws ConfigManagerProxyPropertyNotInitializedException {
        
        boolean isRunning = thisBroker.isRunning();
        String brokerName = thisBroker.getName();
        if (isRunning) {
            log(BROKER_INDENT+ResourcesHandler.getNLSResource(
                    ResourcesHandler.BROKER_RUNNING,
                    new String[] {brokerName}));
        } else {
            log(BROKER_INDENT+ResourcesHandler.getNLSResource(
                    ResourcesHandler.BROKER_STOPPED,
                    new String[] {brokerName}));
        }
    }
    
    /**
     * Sends to the log a line of text describing the execution group
     * and whether it is running
     * @param thisExecutionGroup ExecutionGroupProxy object representing
     * an execution group
     * @throws ConfigManagerProxyPropertyNotInitializedException
     * if communication problems with the Configuration Manager
     * meant that the required information could not be determined.
     * @throws ConfigManagerProxyLoggedException if the parent
     * could not be accessed.
     */
    private void displayExecutionGroupRunstate(ExecutionGroupProxy thisExecutionGroup)
    throws ConfigManagerProxyPropertyNotInitializedException, ConfigManagerProxyLoggedException {
        boolean isRunning = thisExecutionGroup.isRunning();
        String executionGroupName = thisExecutionGroup.getName();
        String brokerName = thisExecutionGroup.getParent().getName();
        
        if (isRunning) {
            log(EG_INDENT+ResourcesHandler.getNLSResource(
                    ResourcesHandler.EG_RUNNING,
                    new String[] {executionGroupName, brokerName}));
        } else {
            log(EG_INDENT+ResourcesHandler.getNLSResource(
                    ResourcesHandler.EG_STOPPED,
                    new String[] {executionGroupName, brokerName}));
        }
    }
    
    /**
     * Sends to the log a line of text describing a deployed
     * message flow and whether it is running
     * @param thisFlow MessageFlowProxy object representing a
     * message flow that has been deployed to an execution group
     * @throws ConfigManagerProxyPropertyNotInitializedException
     * if communication problems with the Configuration Manager
     * meant that the required information could not be determined.
     * @throws ConfigManagerProxyLoggedException if the parent
     * object could not be discovered.
     */
    private void displayMessageFlowRunstate(MessageFlowProxy thisFlow)
    throws ConfigManagerProxyPropertyNotInitializedException, ConfigManagerProxyLoggedException {
        boolean isRunning = thisFlow.isRunning();
        String messageFlowName = thisFlow.getName();
        String executionGroupName = thisFlow.getParent().getName();
        String brokerName = thisFlow.getParent().getParent().getName();
        
        if (isRunning) {
            log(MF_INDENT+ResourcesHandler.getNLSResource(
                    ResourcesHandler.MF_RUNNING,
                    new String[] {messageFlowName, executionGroupName, brokerName}));
        } else {
            log(MF_INDENT+ResourcesHandler.getNLSResource(
                    ResourcesHandler.MF_STOPPED,
                    new String[] {messageFlowName, executionGroupName, brokerName}));
        }
    }

   

    /**
     * Prefixes the supplied string with the current timestamp
     * and sends it to System.err.
     * @param string String to be displayed
     */
    private static void log(String string) {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.getDefault());
        String formattedDate = df.format(new Date());
        System.err.println("("+formattedDate+") "+string);
    }

    /**
     * In interactive mode, this method is called by the
     * Configuration Manager Proxy whenever one of the domain objects
     * changes. If the change is to the run-state of an object,
     * or if a new object has been added to the domain, the
     * change is reported.
     * @param affectedObject The object which has changed. The
     * attributes of the object will already have been updated
     * to contain the new information.
     * @param changedAttributes list containing the attribute
     * key names that have changed.
     * @param newSubcomponents list containing the object's
     * subcomponents which were added by the latest change.
     * Each entry is of the form "componenttype+UUID"
     * such as "Broker+123-123-123". A list of valid component
     * types can be found in the typedef-enumeration
     * com.ibm.broker.config.common.ConfigurationObjectType.
     * @param removedSubcomponents List containing the object's
     * subcomponents which were removed by the latest change.
     * Same format as for newChildren.
     */
    public void processModify(AdministeredObject affectedObject, List changedAttributes, List newSubcomponents, List removedSubcomponents) {
        
        try {
	        // We need to check for two types of modification:
	        // 1. new brokers, execution groups and message flows
	        // 2. start and stop messages
	        // We don't need to check for removed objects here, because we
	        // will catch those in processDelete().
	       
	        // 1. Check for new objects first.
	        ListIterator e = newSubcomponents.listIterator();
	        while (e.hasNext()) {
	            String representation = (String) e.next();
	            AdministeredObject newObject = affectedObject.getManagedSubcomponentFromStringRepresentation(representation);
	            // Report a new broker
		        if (newObject instanceof BrokerProxy) {
		            String brokerName = newObject.getName();
		            log(BROKER_INDENT+ResourcesHandler.getNLSResource(
		                    ResourcesHandler.BROKER_ADDED,
		                    new String[] {brokerName}));
		            displayBrokerRunstate((BrokerProxy)newObject);
		        }
		        
		        // Report a new execution group
		        else if (newObject instanceof ExecutionGroupProxy) {
		            String executionGroupName = newObject.getName();
		            String brokerName = newObject.getParent().getName();
		            log(EG_INDENT+ResourcesHandler.getNLSResource(
		                    ResourcesHandler.EG_ADDED,
		                    new String[] {executionGroupName, brokerName}));
		            displayExecutionGroupRunstate((ExecutionGroupProxy)newObject);
		        }
		        
		        // Report a new message flow
		        else if (newObject instanceof MessageFlowProxy) {
		            String messageFlowName = newObject.getName();
		            String executionGroupName = newObject.getParent().getName();
		            String brokerName = newObject.getParent().getParent().getName();
		            log(MF_INDENT+ResourcesHandler.getNLSResource(
		                    ResourcesHandler.MF_ADDED,
		                    new String[] {messageFlowName, executionGroupName, brokerName}));
		            displayMessageFlowRunstate((MessageFlowProxy)newObject);
		        }
		        
		        // Register the new object if necessary - request an
		        // immediate notification to ensure that the initial
                // runstate of the new object is displayed in a
                // processModify() invocation.
                if (interactiveMode) {
		            newObject.registerListener(this);
		        }
	            
	        }
	        
	        
	        // 2. Display the run-state if the list of changed
	        // attributes supplied to us includes the run-state key. 
	        ListIterator e2 = changedAttributes.listIterator();
	        while (e2.hasNext()) {
	            String keyName = (String) e2.next();
	            if (keyName.equals(AttributeConstants.OBJECT_RUNSTATE_PROPERTY)) {
	                if (affectedObject instanceof BrokerProxy) {
	                    displayBrokerRunstate((BrokerProxy)affectedObject);
	                } else if (affectedObject instanceof ExecutionGroupProxy) {
	                    displayExecutionGroupRunstate((ExecutionGroupProxy)affectedObject);
	                } else if (affectedObject instanceof MessageFlowProxy) {
	                    displayMessageFlowRunstate((MessageFlowProxy)affectedObject);
	                }
	            }
	        }
	        
        } catch (ConfigManagerProxyException ex) {
            // Could not getParent() or getName()
            ex.printStackTrace();
        }
        
    }

    /**
     * In interactive mode, this method is called by the Config
     * Manager Proxy if a domain object is deleted.
     * @param deletedObject AdministeredObject which has been
     * deleted.
     */
    public void processDelete(AdministeredObject deletedObject) {
        
        try {
	        // Report a deleted broker
	        if (deletedObject instanceof BrokerProxy) {
	            String brokerName = deletedObject.getName();
	            log(BROKER_INDENT+ResourcesHandler.getNLSResource(
	                    ResourcesHandler.BROKER_DELETED,
	                    new String[] {brokerName}));
	        }
	        
	        // Report a deleted execution group
	        else if (deletedObject instanceof ExecutionGroupProxy) {
	            String executionGroupName = deletedObject.getName();
	            String brokerName = deletedObject.getParent().getName();
	            log(EG_INDENT+ResourcesHandler.getNLSResource(
	                    ResourcesHandler.EG_DELETED,
	                    new String[] {executionGroupName, brokerName}));
	        }
	        
	        // Report a deleted message flow
	        else if (deletedObject instanceof MessageFlowProxy) {
	            String messageFlowName = deletedObject.getName();
	            String executionGroupName = deletedObject.getParent().getName();
	            String brokerName = deletedObject.getParent().getParent().getName();
	            log(MF_INDENT+ResourcesHandler.getNLSResource(
	                    ResourcesHandler.MF_DELETED,
	                    new String[] {messageFlowName, executionGroupName, brokerName}));
	        }
	        
	        // Listeners are deregistered automatically by the
	        // Configuration Manager Proxy after a delete.
	        
        } catch (ConfigManagerProxyException e) {
            // Could not getParent() or getName()
            e.printStackTrace();
        }
        
    }

    /**
     * This method is not used by the DomainInfo application
     * and is included here in order to fully implement the
     * AdministeredObjectListener interface.
     * @param affectedObject The object on which a command was
     * attempted.
     * @param ccType The overall completion code of the action
     * @param bipMessages an unmodifiable list of
     * com.ibm.broker.config.proxy.LogEntry classes that contains
     * any localized BIP Messages associated with the action.
     * @param referenceProperties Properties of the Request that
     * caused this Action Response.
     */
    public void processActionResponse(AdministeredObject affectedObject, CompletionCodeType ccType, List bipMessages, Properties referenceProperties) {
        // This utility doesn't attempt to change state and so
        // monitoring responses to our submitted actions is pointless.
    }

    
}
