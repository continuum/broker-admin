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

import java.util.Enumeration;
import java.util.Properties;

import cmp.common.ResourcesHandler;

import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyException;
import com.ibm.broker.config.proxy.ConfigManagerProxyLoggedException;
import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.DeployResult;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.TopicProxy;
import com.ibm.broker.config.proxy.TopologyProxy;
 
/*****************************************************************************
 * <p>The BrokerProxy object represents a single broker
 * record as stored in the Configuration Manager.
 * <p>
 * <b>NOTE:</b>
 * <p>
 * Most methods in this class tester take a BrokerProxy
 * parameter. If you wish to gain a handle to such an object
 * in your own code, use something like:
 * <pre>
 * ConfigManagerConnectionParameters cmcp =
 *   MQConfigManagerConnectionParameters("localhost", 1414, "QMGR");
 * ConfigManagerProxy cmp = ConfigManagerProxy.getInstance(cmcp);
 * TopologyProxy t = cmp.getTopology();
 * BrokerProxy b = t.getBrokerByName("myBroker");
 * </pre>
 * 
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ClassTesterForBrokerProxy</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Services to test BrokerProxy APIs
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
 * @version Samples/ConfigManagerProxy/cmp/exerciser/ClassTesterForBrokerProxy.java, Config.Proxy, S000, S000-L50831.3 1.22
 *****************************************************************************/
public class ClassTesterForBrokerProxy {
    
    /**
     * GUI object to which the tester is linked
     */
    CMPAPIExerciser exerciser;
    
    /**
     * Instantiates a new ClassTesterForBrokerProxy that is
     * linked to the supplied GUI
     * @param exerciser GUI object to which the tester is linked
     */
    ClassTesterForBrokerProxy(CMPAPIExerciser exerciser) {
        this.exerciser=exerciser;
    }
    
    /**
     * Modifies the UUID of the broker.
     * <P>
     * WARNING: This method may corrupt the domain; use with care.
     * See the Javadoc for BrokerProxy.setUUID() for more information. 
     * @param b Non-null broker object
     * @param newUUID
     */
    public void testSetUUID(BrokerProxy b, String newUUID) {
        try {
            b.setUUID(newUUID);
            exerciser.reportActionSubmitted();
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Asks the Configuration Manager to resubscribe
     * to the broker's status topics.
     * @param b Non-null broker object
     */
    public void testForceSubscribe(BrokerProxy b) {
        try {
            b.forceSubscribe();
            exerciser.reportActionSubmitted();
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test of the commands to change
     * broker properties
     * @param object Selected AdministeredObject
     * @param newName
     * @param shortDesc New Short Description
     * @param longDesc New Long Description
     * @param queueManagerName New QMgr name
     * @param sslKeyRingFile New SSL Key Ring File name
     * @param sslPasswordFile New SSL Password File name
     * @param sslConnectorEnabled New SSL Connector enabled value
     * @param tempTopicQoPS QoP level of the broker's
     * temporary topic (String representation)
     * @param sysQoPS 'Sys' QoP level of the broker
     * (String representation)
     * @param isysQoPS 'ISys' QoP level of the broker
     * (String representation)
     * @param ibHost Interbroker host name of the broker
     * @param ibPort Interbroker port name of the broker
     * @param authProtocols The Authentication Protocols String
     */
    public void testModifyBrokerProperties(BrokerProxy object,
                                            String newName,
                                            String shortDesc,
                                            String longDesc,
                                            String queueManagerName,
                                            String sslKeyRingFile,
                                            String sslPasswordFile,
                                            boolean sslConnectorEnabled,
                                            String tempTopicQoPS,
                                            String sysQoPS,
                                            String isysQoPS,
                                            String ibHost,
                                            int ibPort,
                                            String authProtocols) {

        try {
            boolean deployRequired = false;
            TopicProxy.QoP tempTopicQoP = TopicProxy.QoP.getQualityOfProtectionFromString(tempTopicQoPS);
            TopicProxy.QoP sysQoP = TopicProxy.QoP.getQualityOfProtectionFromString(sysQoPS);
            TopicProxy.QoP isysQoP = TopicProxy.QoP.getQualityOfProtectionFromString(isysQoPS);
            
            ClassTesterForAdministeredObject.testModifyStandardProperties(exerciser, object, newName, shortDesc, longDesc);

            String oldQueueManagerName = "";
            String oldSSLKeyRingFile = "";
            String oldSSLPasswordFile = "";
            boolean oldSSLConnectorEnabled = false;
            TopicProxy.QoP oldTTQoP = TopicProxy.QoP.unknown;
            TopicProxy.QoP oldSysQoP = TopicProxy.QoP.unknown;
            TopicProxy.QoP oldISysQoP = TopicProxy.QoP.unknown;
            String oldIBHost = "";
            int oldIBPort = -1;
            String oldAuthProtocols = "";

            try {
                oldQueueManagerName = (object.getQueueManagerName() != null) ? object.getQueueManagerName() : "";
                oldSSLKeyRingFile = (object.getSSLKeyRingFileName() != null) ? object.getSSLKeyRingFileName() : "";
                oldSSLPasswordFile = (object.getSSLPasswordFileName() != null) ? object.getSSLPasswordFileName() : "";
                oldSSLConnectorEnabled = object.getSSLConnectorEnabled();
                oldTTQoP = object.getTemporaryTopicQualityOfProtectionLevel();
                oldSysQoP = object.getSysQualityOfProtectionLevel();
                oldISysQoP = object.getISysQualityOfProtectionLevel();
                oldIBHost = object.getInterbrokerHost();
                oldIBPort = object.getInterbrokerPort();
                oldAuthProtocols = object.getAuthenticationProtocols();
            } catch (ConfigManagerProxyPropertyNotInitializedException ex) {
                exerciser.log(ex);
            }

            if (!queueManagerName.equals(oldQueueManagerName)) {
                object.setQueueManagerName(queueManagerName);
                exerciser.reportActionSubmitted();
            }

            if (!sslKeyRingFile.equals(oldSSLKeyRingFile)) {
                object.setSSLKeyRingFileName(sslKeyRingFile);
                exerciser.reportActionSubmitted();
                deployRequired = true;
            }

            if (!sslPasswordFile.equals(oldSSLPasswordFile)) {
                object.setSSLPasswordFileName(sslPasswordFile);
                exerciser.reportActionSubmitted();
                deployRequired = true;
            }
            
            if (sslConnectorEnabled != oldSSLConnectorEnabled) {
                object.setSSLConnectorEnabled(sslConnectorEnabled);
                exerciser.reportActionSubmitted();
                deployRequired = true;
            }

            if (oldTTQoP != tempTopicQoP) {
                object.setTemporaryTopicQualityOfProtectionLevel(tempTopicQoP);
                exerciser.reportActionSubmitted();
                deployRequired = true;
            }

            if (oldSysQoP != sysQoP) {
                object.setSysQualityOfProtectionLevel(sysQoP);
                exerciser.reportActionSubmitted();
                deployRequired = true;
            }

            if (oldISysQoP != isysQoP) {
                object.setISysQualityOfProtectionLevel(isysQoP);
                exerciser.reportActionSubmitted();
                deployRequired = true;
            }

            if (oldIBHost != ibHost) {
                object.setInterbrokerHost(ibHost);
                exerciser.reportActionSubmitted();
                deployRequired = true;
            }

            if (oldIBPort != ibPort) {
                object.setInterbrokerPort(ibPort);
                exerciser.reportActionSubmitted();
                deployRequired = true;
            }

            if (oldAuthProtocols != authProtocols) {
                object.setAuthenticationProtocols(authProtocols);
                exerciser.reportActionSubmitted();
                deployRequired = true;
            }
            
            if (deployRequired) {
                exerciser.brokerDeployRequired = true;
                exerciser.updateStatusLine();
            }
        } catch (ConfigManagerProxyLoggedException ex) {
            exerciser.log(ex);
        }
    }


    /**
     * Gives a quick test of the commands to change
     * broker's multicast properties
     * @param object Selected AdministeredObject
     * @param enabled
     * @param minIPv4Address
     * @param maxIPv4Address
     * @param dataPort
     * @param packetSize
     * @param hbTimeout
     * @param ttl
     * @param ipv4NetworkInterface
     * @param isReliable
     * @param isSecure
     * @param overlapTopics
     * @param trlEnabled
     * @param trlValue
     * @param nackBackOffTime
     * @param nackCheckPeriod
     * @param packetBuffers
     * @param socketBufferSize
     * @param historyCleaningTime
     * @param minimumHistorySize
     * @param nackAccumulationTime
     * @param maxKeyAge
     */
    public void testModifyBrokerMulticastProperties(BrokerProxy object,
                                                     boolean enabled,
                                                     String minIPv4Address,
                                                     String maxIPv4Address,
                                                     int dataPort,
                                                     int packetSize,
                                                     int hbTimeout,
                                                     int ttl,
                                                     String ipv4NetworkInterface,
                                                     boolean isReliable,
                                                     boolean isSecure,
                                                     String protocolType,
                                                     int maxMemoryAllowed,
                                                     int overlapTopics,
                                                     String trlEnabled,
                                                     int trlValue,
                                                     int nackBackOffTime,
                                                     int nackCheckPeriod,
                                                     int packetBuffers,
                                                     int socketBufferSize,
                                                     int historyCleaningTime,
                                                     int minimumHistorySize,
                                                     int nackAccumulationTime,
                                                     int maxKeyAge) {

        try {
            try {
                BrokerProxy.MulticastParameterSet params = object.getMulticastParameters();
                if (params == null) {
                    params = new BrokerProxy.MulticastParameterSet();
                }
                params.multicastEnabled = enabled;
                params.multicastMinimumIPv4Address = minIPv4Address;
                params.multicastMaximumIPv4Address = maxIPv4Address;
                params.multicastDataPort = dataPort;
                params.multicastPacketSizeBytes = packetSize;
                params.multicastHeartbeatTimeoutSec = hbTimeout;
                params.multicastMcastSocketTTL = ttl;
                params.multicastIPv4Interface = ipv4NetworkInterface;
                params.multicastDefaultQOS = isReliable;
                params.multicastDefaultSecurity = isSecure;
                params.multicastProtocolType = protocolType;
                params.multicastMaxMemoryAllowedKBytes = maxMemoryAllowed;
                params.multicastOverlappingTopicBehaviour = overlapTopics;
                params.multicastLimitTransRate = trlEnabled;
                params.multicastTransRateLimitKbps = trlValue;
                params.multicastBackoffTimeMillis = nackBackOffTime;
                params.multicastNACKCheckPeriodMillis = nackCheckPeriod;
                params.multicastPacketBuffers = packetBuffers;
                params.multicastSocketBufferSizeKBytes = socketBufferSize;
                params.multicastHistoryCleaningTimeSec = historyCleaningTime;
                params.multicastMinimalHistoryKBytes = minimumHistorySize;
                params.multicastNACKAccumulationTimeMillis = nackAccumulationTime;
                params.multicastMaxKeyAge = maxKeyAge;
                object.setMulticastParameters(params);
                exerciser.reportActionSubmitted();
                exerciser.brokerDeployRequired = true;
                exerciser.updateStatusLine();
            } catch (ConfigManagerProxyPropertyNotInitializedException ex) {
                exerciser.log(ex);
            }

        } catch (ConfigManagerProxyLoggedException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test of the Configuration Manager Proxy's deployment
     * functionality.
     * @param b
     */
    public void testDeployBrokerConfiguration(BrokerProxy b) {
        try {
            DeployResult ds = b.deploy(ResourcesHandler.getUserSettingInt(ResourcesHandler.DEPLOY_WAIT_TIME, 0));
            exerciser.reportDeployResult(ds);
            exerciser.brokerDeployRequired = false;
            exerciser.updateStatusLine();
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Gives a quick test of the Configuration Manager Proxy's deployment
     * functionality.
     * @param b
     */
    public void testDeleteAllEGsBrokerConfiguration(BrokerProxy b) {
        try {
            DeployResult ds = b.deleteAllExecutionGroupsAndDeploy(ResourcesHandler.getUserSettingInt(ResourcesHandler.DEPLOY_WAIT_TIME, 0));
            exerciser.reportDeployResult(ds);
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test of the broker's deployment
     * cancel functionality.
     * @param cmp
     */
    public void testCancelDeploy(BrokerProxy b) {
        try {
            DeployResult ds = b.cancelDeployment(ResourcesHandler.getUserSettingInt(ResourcesHandler.DEPLOY_WAIT_TIME, 0));
            exerciser.reportDeployResult(ds);
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Displays the connections belonging to the supplied broker
     * @param b - Brokers whose connections will be listed
     */
    public void testListConnections(BrokerProxy b) {
        try {
            String brokerUUID = b.getUUID();
            
            // List connections is an action on the topology
            // which can take a broker UUID parameter, which
            // is used to filter the connections.
            ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
            TopologyProxy t = cmp.getTopology();
            Enumeration e = t.getConnections(brokerUUID);

            if (e.hasMoreElements()) {
                while (e.hasMoreElements()) {
                    TopologyProxy.Connection c = (TopologyProxy.Connection) e.nextElement();
                    exerciser.log(
                        t.getBroker(BrokerProxy.withUUID(c.source)).getName()+
                        " ---> "+
                        t.getBroker(BrokerProxy.withUUID(c.target)).getName());
                }
            } else {
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.NO_CONNECTIONS));
            }
        } catch (ConfigManagerProxyPropertyNotInitializedException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Tests the Configuration Manager Proxy's eg creation.
     * @param broker Broker parent
     * @param egName Name of the eg to create
     */
    public void testCreateEG(BrokerProxy broker, String egName) {
        try {
            broker.createExecutionGroup(egName);
            	// createExecutionGroup returns an object of type
            	// ExecutionGroupProxy, but this method doesn't use it.
            exerciser.reportActionSubmitted();
        } catch (ConfigManagerProxyLoggedException ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Gives a quick test of the Configuration Manager Proxy's eg
     * deletion.
     * @param eg Execution group to delete
     */
    public void testDeleteEG(ExecutionGroupProxy eg) {
        try {
            String egName = eg.getName();
            BrokerProxy b = (BrokerProxy) eg.getParent();
            DeployResult ds = b.deleteExecutionGroup(egName, ResourcesHandler.getUserSettingInt(ResourcesHandler.DEPLOY_WAIT_TIME, 0));
            exerciser.reportDeployResult(ds);
        } catch (ConfigManagerProxyLoggedException ex) {
            exerciser.log(ex);
        } catch (ConfigManagerProxyPropertyNotInitializedException ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Adds to the supplied Properties table a set of key/value pairs that
     * describe some BrokerProxy-specific methods that may
     * be invoked on the supplied object, and the returned value from
     * those methods.
     * @param b A valid BrokerProxy
     * @param p A valid Properties object
     */
    public void discoverProperties(BrokerProxy b, Properties p) {
        
        // These methods set may fail with a
        // ConfigManagerProxyPropertyNotInitialisedException, which means
        // that information on the administered object was not supplied by
        // the Configuration Manager before a timeout occurred. If this
        // happens for *one* of these methods it will happen for *all*, so it
        // is acceptable to enclose all of this section in a single
        // try/catch block.
        try {
            p.setProperty("isRunning()", ""+b.isRunning());
            p.setProperty("getQueueManagerName()", ""+b.getQueueManagerName());
            
            if (exerciser.showAdvanced()) {
                
                // ---------- Display execution groups ----------
                StringBuffer key = new StringBuffer("getExecutionGroups()");
                StringBuffer value = new StringBuffer();
                Enumeration e = b.getExecutionGroups(null);
                if (e == null) {
                    value.append(""+e);
                } else {
                    int count = 0;
                    while (e.hasMoreElements()) {
                        count++;
                        key.append("\n    ["+count+"]");
                        value.append("\n");
                        ExecutionGroupProxy c = (ExecutionGroupProxy)e.nextElement();
                        value.append(CMPAPIExerciser.formatAdminObject(c));
                    }
                }
                p.setProperty(""+key, ""+value);
            
                // ----------- Other properties -----------
                p.setProperty("getAuthenticationProtocols()", ""+b.getAuthenticationProtocols());
                p.setProperty("getInterbrokerHost()", ""+b.getInterbrokerHost());
                p.setProperty("getInterbrokerPort()", ""+b.getInterbrokerPort());
                p.setProperty("getISysQualityOfProtectionLevel()", ""+b.getISysQualityOfProtectionLevel());
                p.setProperty("getSysQualityOfProtectionLevel()", ""+b.getSysQualityOfProtectionLevel());
                p.setProperty("getSSLKeyRingFileName()", ""+b.getSSLKeyRingFileName());
                p.setProperty("getSSLPasswordFileName()", ""+b.getSSLPasswordFileName());
                p.setProperty("getSysQualityOfProtectionLevel()", ""+b.getSysQualityOfProtectionLevel());
                p.setProperty("getTemporaryTopicQualityOfProtectionLevel()", ""+b.getTemporaryTopicQualityOfProtectionLevel());
                
                // -------- Multicast parameters --------
                key = new StringBuffer("getMulticastParameters()\n");
                value = new StringBuffer();
                BrokerProxy.MulticastParameterSet m = b.getMulticastParameters();
                if (m == null) {
                    value.append(""+m);
                } else {
                    value.append("\n");
                    key.append("  multicastBackoffTimeMillis\n");
                    value.append(""+m.multicastBackoffTimeMillis+"\n");
                    key.append("  multicastDataPort\n");
                    value.append(""+m.multicastDataPort+"\n");
                    key.append("  multicastDefaultQOS\n");
                    value.append(""+m.multicastDefaultQOS+"\n");
                    key.append("  multicastDefaultSecurity\n");
                    value.append(""+m.multicastDefaultSecurity+"\n");
                    key.append("  multicastEnabled\n");
                    value.append(""+m.multicastEnabled+"\n");
                    key.append("  multicastHeartbeatTimeoutSec\n");
                    value.append(""+m.multicastHeartbeatTimeoutSec+"\n");
                    key.append("  multicastHistoryCleaningTimeSec\n");
                    value.append(""+m.multicastHistoryCleaningTimeSec+"\n");
                    key.append("  multicastIPv4Interface\n");
                    value.append(""+m.multicastIPv4Interface+"\n");
                    key.append("  multicastLimitTransRate\n");
                    value.append(""+m.multicastLimitTransRate+"\n");
                    key.append("  multicastMaxKeyAge\n");
                    value.append(""+m.multicastMaxKeyAge+"\n");
                    key.append("  multicastMaximumIPv4Address\n");
                    value.append(""+m.multicastMaximumIPv4Address+"\n");
                    key.append("  multicastMaxMemoryAllowedKBytes\n");
                    value.append(""+m.multicastMaxMemoryAllowedKBytes+"\n");
                    key.append("  multicastMcastSocketTTL\n");
                    value.append(""+m.multicastMcastSocketTTL+"\n");
                    key.append("  multicastMinimalHistorySizeKBytes\n");
                    value.append(""+m.multicastMinimalHistoryKBytes+"\n");
                    key.append("  multicastMinimumIPv4Address\n");
                    value.append(""+m.multicastMinimumIPv4Address+"\n");
                    key.append("  multicastNACKAccumulationTimeMillis\n");
                    value.append(""+m.multicastNACKAccumulationTimeMillis+"\n");
                    key.append("  multicastNACKCheckPeriodMillis\n");
                    value.append(""+m.multicastNACKCheckPeriodMillis+"\n");
                    key.append("  multicastOverlappingTopicBehaviour\n");
                    value.append(""+m.multicastOverlappingTopicBehaviour+"\n");
                    key.append("  multicastPacketBuffers\n");
                    value.append(""+m.multicastPacketBuffers+"\n");
                    key.append("  multicastPacketSizeBytes\n");
                    value.append(""+m.multicastPacketSizeBytes+"\n");
                    key.append("  multicastProtocolType\n");
                    value.append(""+m.multicastProtocolType+"\n");
                    key.append("  multicastSocketBufferSizeKBytes\n");
                    value.append(""+m.multicastSocketBufferSizeKBytes+"\n");
                    key.append("  multicastTransRateLimitKbps");
                    value.append(""+m.multicastTransRateLimitKbps);
                }
                p.setProperty(key.toString(), value.toString());
            }
        } catch (ConfigManagerProxyPropertyNotInitializedException e) {
            exerciser.log(e);
        }
    }

    
    
    
}
