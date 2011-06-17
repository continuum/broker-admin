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
package cmp.exerciser;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.JTree;

import cmp.common.ResourcesHandler;

import com.ibm.broker.config.proxy.ConfigManagerConnectionParameters;
import com.ibm.broker.config.proxy.ConfigManagerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyException;
import com.ibm.broker.config.proxy.ConfigManagerProxyLoggedException;
import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.DeployResult;
import com.ibm.broker.config.proxy.MQConfigManagerConnectionParameters;
import com.ibm.broker.config.proxy.MQPropertyFileConfigManagerConnectionParameters;
import com.ibm.broker.config.proxy.Subscription;
import com.ibm.broker.config.proxy.SubscriptionsProxy;

/*****************************************************************************
 * <p>The ConfigManagerProxy object represents a Configuration
 * Manager instance. It is also the root of the administered object
 * hierarchy, which means that all object proxy handles can be
 * (directly or indirectly) obtained from this one.
 * <p>
 * <b>NOTE:</b>
 * <p>
 * Several methods in this class tester take a ConfigManagerProxy
 * parameter. If you wish to gain a handle to such an object
 * in your own code, use something like:
 * <pre>
 * ConfigManagerConnectionParameters cmcp =
 *   MQConfigManagerConnectionParameters("localhost", 1414, "QMGR");
 * ConfigManagerProxy cmp = ConfigManagerProxy.getInstance(cmcp);
 * </pre>
 * 
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ClassTesterForConfigManagerProxy</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Services to test ConfigManagerProxy APIs
 *     </UL>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Internal Collaborators</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *       <LI><TT>cmp.ResourcesHandler</TT>
 *       <LI><TT>cmp.exerciser.CMPAPIExerciser</TT>
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
 * 25103.7  2004-03-18  HDMPL           v6 Release
 *
 * </pre>
 *
 * @version Samples/ConfigManagerProxy/cmp/exerciser/ClassTesterForConfigManagerProxy.java, Config.Proxy, S000, S000-L50907.1 1.22
 *****************************************************************************/
public class ClassTesterForConfigManagerProxy {
    
    /**
     * GUI object to which the tester is linked
     */
    CMPAPIExerciser exerciser;
    
    /**
     * The connection parameters for the connectActionThread's
     * current connection operation.
     * Also acts as a lock - it is non-null only if a connect action
     * is in progress.
     */
    ConfigManagerConnectionParameters currentConnectionParameters = null;
    
    /**
     * The maximum number of susbscriptions to display in the GUI.
     */
    private static final int MAX_SUBSCRIPTIONS_TO_DISPLAY = 100;
    
    /**
     * Instantiates a new ClassTesterForConfigManagerProxy that is
     * linked to the supplied GUI
     * @param exerciser GUI object to which the tester is linked
     */
    ClassTesterForConfigManagerProxy(CMPAPIExerciser exerciser) {
        this.exerciser=exerciser;
    }

    /**
     * Enables batch mode for the current CMP handle
     * @throws ConfigManagerProxyLoggedException if a batch
     * could not be started for whatever reason.
     */
    public void testBatchStart() throws ConfigManagerProxyLoggedException {
        ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
        if (cmp != null) {
            cmp.beginUpdates();
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.BATCH_START));
        }
    }
    
    /**
     * Enables batch mode for the current CMP handle
     * @throws ConfigManagerProxyException if a batch
     * could not be sent for whatever reason.
     */
    public void testBatchSend() throws ConfigManagerProxyException {
        ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
        if (cmp != null) {
            cmp.sendUpdates();
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.BATCH_SENT));
        }
    }
    
    /**
     * Enables batch mode for the current CMP handle
     */
    public void testBatchClear() {
        ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
        if (cmp != null) {
            cmp.clearUpdates();
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.BATCH_CLEARED));
        }
    }
    
    
    
    /**
     * Gives a quick test to change the system trace settings of the
     * Configuration Manager
     * @param cmp
     */
    public void testConfigurationManagerTraceStart(ConfigManagerProxy cmp) {
        try {
            cmp.startSystemTrace();
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test to change the system trace settings of the
     * Configuration Manager
     * @param cmp
     */
    public void testConfigurationManagerDebugTraceStart(ConfigManagerProxy cmp) {
        try {
            cmp.startDebugSystemTrace();
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test to change the system trace settings of the
     * Configuration Manager
     * @param cmp
     */
    public void testConfigurationManagerTraceStop(ConfigManagerProxy cmp) {
        try {
            cmp.stopSystemTrace();
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    

    /**
     * Tests setting the variables that define how long Configuration Manager
     * Proxy methods should block before throwing a
     * ConfigManagerProxyPropertyNotInitialisedException.
     * @param maxConfigWaitTimeMs The amount of time (in milliseconds) the
     * Configuration Manager Proxy should wait for Configuration Manager
     * responses.
     * @param deployWaitTimeMs The amount of time (in milliseconds) the
     * Configuration Manager Proxy should wait for broker responses when
     * deploying.
     */
    public void testRetryCharacteristics(long maxConfigWaitTimeMs, long deployWaitTimeMs) {
        ConfigManagerProxy.setRetryCharacteristics(maxConfigWaitTimeMs);
        ResourcesHandler.setUserSetting(ResourcesHandler.CONFIG_RETRY_TIME, ""+maxConfigWaitTimeMs);
        ResourcesHandler.setUserSetting(ResourcesHandler.DEPLOY_WAIT_TIME, ""+deployWaitTimeMs);
        ResourcesHandler.saveUserSettings();
    }
    
    
    /**
     * Tests a connection to the Configuration Manager
     * @param ip Hostname or IP address (or empty String, to use Java Bindings)
     * @param port Port on which the SVRCONN channel is listening
     * @param qmgr Queue Manager on which the Configuration Manager is
     * running (or empty String to use the default Queue Manager)
     * @param secexit Optional security Exit
     * @param secexitURLString Optional URL describing where the security
     * exit can be found (for use in Eclipse environments where the security
     * exit is not necessarily available on the CLASSPATH).
     */
    public void testConnect(String ip, int port, String qmgr, String secexit, String secexitURLString) {

        try {
            ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
            if (cmp != null) {
                testDisconnect();
            }
            
            URL secexitURL = null;
			if (secexitURLString != null) {
				if (!secexitURLString.equals("")) {
					secexitURL = new URL(secexitURLString);
				}
			}
			
            ConfigManagerConnectionParameters cmcp =
            	new MQConfigManagerConnectionParameters(ip,port,qmgr,secexit,secexitURL);

            doConnectAction(cmcp);

        } catch (Exception ex) {
		    exerciser.log(ex);
		}

    }

    /**
     * Tests a connection to the Configuration Manager
     * using a *.configmgr file (as generated by the toolkit)
     * @param filename Path and name of the *.configmgr
     * file that contains the connection parameters.
     */
    public void testConnect(String filename) {
        try {
            ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
            if (cmp != null) {
                testDisconnect();
            }
            
            ConfigManagerConnectionParameters cmcp =
            	new MQPropertyFileConfigManagerConnectionParameters(filename);

			doConnectAction(cmcp);
        } catch (Exception ex) {
		    exerciser.log(ex);
		}
    }
    
    /**
     * Connects to the Configuration Manager with the
     * supplied characteristics.
     * @param cmcp
     */
    private void doConnectAction(ConfigManagerConnectionParameters cmcp) {
		
        // If we're already trying to connect, don't allow a new connect action
        // to take place.
        if (currentConnectionParameters != null) {
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.CONNECT_IN_PROGRESS));
        } else {
            
            // Initialise some parameters required by this specific application. You probably
            // won't need these if you're writing your own Configuration Manager Proxy application.
            // 'currentConnectionParameters' will stop further connection attempts until this one succeeds.
            currentConnectionParameters = cmcp;
            // 'exerciser.administeredObjects' will contain handles to all the domain objects that the exerciser knows about.
            exerciser.administeredObjects = new Hashtable();
            // Now that we're trying to connect, remove the 'connect' menus if we're running the exerciser with a GUI.
            if (!exerciser.isHeadless()) {
                exerciser.connectContextSensitiveMenuItem.setEnabled(false);
                exerciser.connectMenuItem.setEnabled(false);
            }
            
            // Now do the important stuff.
            ConfigManagerProxy cmp = null;
            try {
                
                // The next statement is THE most important one in this method. When writing a
                // Configuration Manager Proxy application, this is the one you NEED to call. It
                // starts up the connection to the Configuration Manager Proxy.
                cmp = ConfigManagerProxy.getInstance(currentConnectionParameters);
                
                // A successful (non-exception throwing) return from this method indicates that
                // the Configuration Manager Proxy was able to PUT a message to the Configuration Manager's
                // input queue.
                // Therefore we can infer that the Configuration Manager's queue manager is alive at
                // this point - so report that fact now.
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.CONNECTED_TO_QMGR));
                
                // Wait to see if the Configuration Manager is talking back to us.
                // 'configMgrIsResponding' will be true if and only if at least one
                // response is received in the configured timeout period.
                // (This step isn't necessary, although it does allow us to
                // catch and pinpoint comms failures early on, before we initialise
                // too many resources.)
                // First check if we have been restricted
                boolean bRestricted = cmp.hasBeenRestrictedByConfigManager(true);
                if (!bRestricted) {
                    
                   // either we haven't been restricted, or we haven't heard back from the CM
                   // so we need to now check if we have heard back
                   boolean configMgrIsResponding = cmp.hasBeenUpdatedByConfigManager(false);

                   if(configMgrIsResponding)
                   {                    
                      // We're connected to a running Configuration Manager.
                      exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.CONNECTED_TO_CM));
                      exerciser.setConfigManagerProxyConnectedInstance(cmp);
                      exerciser.updateStatusLine();
                    
                      // If we're running with a GUI, set up the Window to show the domain objects.
                      // If we're running without a GUI, just set up the tree that would otherwise
                      // show these domain objects.
                      if (!exerciser.isHeadless()) {
                         exerciser.initWindow();
                         exerciser.setupJTable(cmp);
                         exerciser.setupSelectedMenu(cmp);
                      } else {
                         exerciser.root = exerciser.getTree(cmp, false);
                         exerciser.tree = new JTree(exerciser.root);
                         exerciser.initialiseMappingOfIdentifyingStringsToNodes(exerciser.root);
                      }
                    
                      // The domain objects are available at this point.
                      exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.CONNECTION_COMPLETED));
                      exerciser.selectedAdministeredObject = cmp;
                    
                      // Connect to the Configuration Manager has succeeded.
                      // Display some navigation help in the log if
                      // we're running with a GUI
                      if (!exerciser.isHeadless()) {
                         exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.TREE_HELP));
                         exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.TREE_HELP_2));
                      }
                   } else {
                       // If the Configuration Manager's not responding, don't bother going any further.
                       exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.NO_RESPONSE_FROM_CM));
                       exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.DISCONNECTED));                       
                   }                    
                } else {
                    exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.NOT_AUTHORISED));
                }                 
            } catch (ConfigManagerProxyLoggedException e) {
                // thrown by getInstance() only
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.CONNECT_FAILED));
                exerciser.log(e);
            } finally {
                // Release the locks to allow future connection attempts to happen
                currentConnectionParameters = null;
                if (!exerciser.isHeadless()) {
                    exerciser.connectContextSensitiveMenuItem.setEnabled(true);
                    exerciser.connectMenuItem.setEnabled(true);
                }
            }
        }
    } 

    /**
     * Grants access to the subscriptions table for the supplied credentials
     * @param principalName User or group name
     * of the principal who is to be granted access.
     * @param principalType Type of the principal mentioned above - USER or GROUP
     * @param machineOrDomainName Name of the machine or domain on which the ID resides
     * (blank = all machines)
     * @param permission Authority to be granted - FULL, EDIT, VIEW or DEPLOY.
     */
    public void testGrantSubscriptionsAccess(String principalName, String principalType, String machineOrDomainName, String permission) {
        ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
        
        try {
            SubscriptionsProxy s = cmp.getSubscriptions("", "", "", "", null, null);
            exerciser.classTesterAdministeredObject.testGrantAccess(s, principalName, principalType, machineOrDomainName, permission);
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Removes all access to the subscriptions table for the specified user.
     * @param principalName User or group name (on the CM machine)
     * of the principal who is to have access removed.
     * @param principalType Type of the principal mentioned above - USER or GROUP
     * @param machineOrDomainName Name of the machine or domain on which the ID resides
     * (blank = all machines)
     */
    public void testRemoveSubscriptionsAccess(String principalName, String principalType, String machineOrDomainName) {
        ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
        
        try {
            SubscriptionsProxy s = cmp.getSubscriptions("", "", "", "", null, null);
            exerciser.classTesterAdministeredObject.testRemoveAccess(s, principalName, principalType, machineOrDomainName);
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Shows which users and groups have access to the the subscriptions table
     */
    public void testShowSubscriptionsAccess() {
        ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
        try {
            SubscriptionsProxy s = cmp.getSubscriptions("", "", "", "", null, null);
            exerciser.classTesterAdministeredObject.testShowAccess(s);
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test of the Configuration Manager Proxy's subscriptions
     * query facility
     * @param cmp
     * @param topic
     * @param brokers
     * @param users
     * @param subscriptionsPoints
     * @param startDateString
     * @param endDateString
     * @param deleteMatches
     */
    public void testGetSubscriptions(ConfigManagerProxy cmp,
          String topic,
          String brokers,
          String users,
          String subscriptionsPoints,
          String startDateString,
          String endDateString,
          boolean deleteMatches) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            GregorianCalendar startGC = null;
            GregorianCalendar endGC = null;
            
            try {
                Date startDate = sdf.parse(startDateString);
                startGC = new GregorianCalendar();
                startGC.setTime(startDate);
            } catch (ParseException e1) {
                 // If no value was supplied, don't specify a start date
            }
            try {
                Date endDate = sdf.parse(endDateString);
                endGC = new GregorianCalendar();
                endGC.setTime(endDate);
            } catch (ParseException e1) {
                // If no value was supplied, don't specify a end date
            }
            
            SubscriptionsProxy l = cmp.getSubscriptions(topic, brokers, users, subscriptionsPoints, startGC, endGC);
            if (!l.hasBeenRestrictedByConfigManager(true)) {
                
                Enumeration e = l.elements();
                int numberDisplayed = 0;
                
                // Go through all returned subscriptions and display them.
                // Also delete them if we were told to.
                while (e.hasMoreElements() && (numberDisplayed++ < MAX_SUBSCRIPTIONS_TO_DISPLAY)) {
                    Subscription thisEntry = (Subscription) e.nextElement();
                    exerciser.log("-----------------------------------");
                    exerciser.log("getClient() = "+thisEntry.getClient());
                    exerciser.log("getFilter() = "+thisEntry.getFilter());
                    exerciser.log("getRegistrationDate() = "+thisEntry.getRegistrationDate());
                    exerciser.log("getSubscriptionPoint() = "+thisEntry.getSubscriptionPoint());
                    exerciser.log("getTopicName() = "+thisEntry.getTopicName());
                    exerciser.log("getUser() = "+thisEntry.getUser());
                    exerciser.log("getBroker() = "+thisEntry.getBroker());
                    if (deleteMatches) {
                        exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.DELETING_SUBSCRIPTION));
                        thisEntry.delete(l); // Could use SubscriptionsProxy.delete() instead.
                    }
                    if ((!e.hasMoreElements()) || (numberDisplayed == MAX_SUBSCRIPTIONS_TO_DISPLAY)) {
                        exerciser.log("-----------------------------------");
                    }
                }
                
                // If there are susbscriptions we haven't dealt with,
                // then warn the user.
                if (e.hasMoreElements()) {
                    String totalSubs = "" + l.getSize();
                    if (deleteMatches) {
                        exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.TOO_MANY_SUBS_TO_DELETE, new String[] {totalSubs, ""+MAX_SUBSCRIPTIONS_TO_DISPLAY}));
                    } else {
                        exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.TOO_MANY_SUBS_TO_DISPLAY, new String[] {totalSubs, ""+MAX_SUBSCRIPTIONS_TO_DISPLAY}));
                    }
                }
                
            } else {
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.NOT_AUTHORISED));
            }
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test of the Configuration Manager Proxy's deployment
     * cancel functionality.
     * @param cmp
     */
    public void testCancelDeploy(ConfigManagerProxy cmp) {
        try {
            DeployResult ds = cmp.cancelDeployment(ResourcesHandler.getUserSettingInt(ResourcesHandler.DEPLOY_WAIT_TIME, 0));
            exerciser.reportDeployResult(ds);
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }
    

    /**
     * Tests a disconnection
     */
    public void testDisconnect() {
        ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
        if (cmp != null) {
            cmp.disconnect();
            exerciser.setConfigManagerProxyConnectedInstance((ConfigManagerProxy)null);
            cmp = null;
            if (!exerciser.isHeadless()) {
                exerciser.initWindow();
                exerciser.setupJTable(null);
                exerciser.selectedAdministeredObject = null;
                exerciser.administeredObjects = new Hashtable();
                exerciser.updateStatusLine();
            }
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.DISCONNECTED));
        }
    }

    /**
     * Adds to the supplied Properties table a set of key/value pairs that
     * describe some ConfigManagerProxy-specific methods that may
     * be invoked on the supplied object, and the returned value from
     * those methods.
     * @param proxy Configuration Manager Proxy object.
     * @param p A valid Properties object.
     */
    public void discoverProperties(ConfigManagerProxy cmp, Properties p) {
        p.setProperty("getConfigManagerProxyVersion()", ""+ConfigManagerProxy.getConfigManagerProxyVersion());
        p.setProperty("isIncompatible()", ""+cmp.isIncompatible());
        
        try {
            // These methods set may fail with a
            // ConfigManagerProxyPropertyNotInitialisedException, which means
            // that information on the administered object was not supplied by
            // the Configuration Manager before a timeout occurred. If this
            // happens for *one* of these methods it will happen for *all*, so it
            // is acceptable to enclose all of this section in a single
            // try/catch block.
            p.setProperty("getConfigManagerVersion()", ""+cmp.getConfigManagerVersion());
            
            if (exerciser.showAdvanced()) {
                p.setProperty("getConfigManagerFullVersion()", ""+cmp.getConfigManagerFullVersion());
                p.setProperty("getConfigManagerOSName()", ""+cmp.getConfigManagerOSName());
                p.setProperty("getConfigManagerOSArch()", ""+cmp.getConfigManagerOSArch());
                p.setProperty("getConfigManagerOSVersion()", ""+cmp.getConfigManagerOSVersion());
                p.setProperty("getTopology()", CMPAPIExerciser.formatAdminObject(cmp.getTopology()));
                p.setProperty("getLog()", CMPAPIExerciser.formatAdminObject(cmp.getLog()));
                p.setProperty("getTopicRoot()", CMPAPIExerciser.formatAdminObject(cmp.getTopicRoot()));
            }
        } catch (ConfigManagerProxyPropertyNotInitializedException e) {
            exerciser.log(e);
        }
        
        
    }
    
}
