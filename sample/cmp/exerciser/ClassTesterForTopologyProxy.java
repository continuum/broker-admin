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
import com.ibm.broker.config.proxy.CollectiveProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyException;
import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.DeployResult;
import com.ibm.broker.config.proxy.TopologyProxy;

/*****************************************************************************
 * <p>The TopologyProxy object represents the network of neighboring
 * brokers that can share publish subscribe information. It is
 * also used as the container of all brokers. Exactly one topology
 * exists in each Configuration Manager.
 * <p>
 * <b>NOTE:</b>
 * <p>
 * Most methods in this class tester take a TopologyProxy
 * parameter. If you wish to gain a handle to such an object
 * in your own code, use something like:
 * <pre>
 * ConfigManagerConnectionParameters cmcp =
 *   MQConfigManagerConnectionParameters("localhost", 1414, "QMGR");
 * ConfigManagerProxy cmp = ConfigManagerProxy.getInstance(cmcp);
 * TopologyProxy t = cmp.getTopology();
 * </pre>
 * 
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ClassTesterForTopologyProxy</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Services to test TopologyProxy APIs
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
 * @version Samples/ConfigManagerProxy/cmp/exerciser/ClassTesterForTopologyProxy.java, Config.Proxy, S000, S000-L50818.2 1.13
 *****************************************************************************/
public class ClassTesterForTopologyProxy {
    
    /**
     * GUI object to which the tester is linked
     */
    CMPAPIExerciser exerciser;
    
    /**
     * Collective tester object to which the tester is linked
     */
    ClassTesterForCollectiveProxy classTesterCollective;
    
    /**
     * Instantiates a new ClassTesterForTopologyProxy that is
     * linked to the supplied GUI
     * @param exerciser GUI object to which the tester is linked
     */
    ClassTesterForTopologyProxy(CMPAPIExerciser exerciser, ClassTesterForCollectiveProxy classTesterCollective) {
        this.exerciser=exerciser;
        this.classTesterCollective = classTesterCollective;
    }
    
    /**
     * Gives a quick test of the Configuration Manager Proxy's broker
     * creation.
     * @param t TopologyProxy object in which to create a broker
     * @param brokerName Name of the broker to create
     * @param brokerqm Name of the broker's queue manager
     * @param createDefaultEG True to create a default execution
     * group, false otherwise.
     */
    public void testCreateBroker(TopologyProxy t, String brokerName, String brokerqm, boolean createDefaultEG) {

        try {
            BrokerProxy b = t.createBroker(brokerName, brokerqm);
            if (createDefaultEG) {
                b.createExecutionGroup("default");
            }
            exerciser.reportActionSubmitted();
            exerciser.updateStatusLine(ResourcesHandler.getNLSResource(ResourcesHandler.NEW_BROKER_ANY_DEPLOY_REQUIRED));
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test of the Configuration Manager Proxy's broker
     * deletion. Note that this method has the effect of removing
     * the broker from the topology, but not deleting the physical
     * component.
     * @param b Broker to delete
     */
    public void testDeleteBroker(BrokerProxy b) {

        try {
            String brokerName = b.getName();
            TopologyProxy t = (TopologyProxy) b.getParent();
            DeployResult dr = t.deleteBroker(brokerName, ResourcesHandler.getUserSettingLong(ResourcesHandler.DEPLOY_WAIT_TIME, 0));
            exerciser.reportDeployResult(dr);
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Destroys all references to the broker with the supplied name or UUID
     * from the Configuration Manager's repository.
     * This method should not normally be used; its purpose is to allow
     * broker references to be tidied from the Configuration Manager if
     * a broker component is prematurely deleted.
     * If invoked against a running broker, it renders the broker
     * unmanageable. Use with caution!  
     * @param t TopologyProxy object
     * @param brokerNameOrUUID Name or unique identifier of the broker
     * to delete.
     */
    public void testRemoveDeletedBroker(TopologyProxy t, String brokerNameOrUUID) {
        try {
            t.removeDeletedBroker(brokerNameOrUUID);
            exerciser.reportActionSubmitted();
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    
    /**
     * Gives a quick test of the Configuration Manager Proxy's collective
     * creation.
     * @param t TopologyProxy object in which a collective is to be created
     * @param collectiveName - Name of the new collective
     * @param brokerList - semi-colon separated list of broker uuids.
     */
    public void testCreateCollective(TopologyProxy t, String collectiveName, String brokerList) {

        try {
            CollectiveProxy c = t.createCollective(collectiveName);
            classTesterCollective.testAddToCollective(c, brokerList);
            exerciser.reportActionSubmitted();
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test of the Configuration Manager Proxy's connection
     * creation.
     * @param t TopologyProxy object
     * @param sourceBroker - Name of the source broker
     * @param targetBroker - Name of the target broker
     */
    public void testCreateConnection(TopologyProxy t, String sourceBroker, String targetBroker) {

        try {
            t.createConnectionByName(sourceBroker, targetBroker);
            exerciser.reportActionSubmitted();
            exerciser.topologyDeployRequired = true;
            exerciser.updateStatusLine();
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Gives a quick test of the Configuration Manager Proxy's connection
     * deletion.
     * @param t TopologyProxy object
     * @param sourceBroker - Name of the source broker
     * @param targetBroker - Name of the target broker
     */
    public void testDeleteConnection(TopologyProxy t, String sourceBroker, String targetBroker) {

        try {
            t.deleteConnectionByName(sourceBroker, targetBroker);
            exerciser.reportActionSubmitted();
            exerciser.topologyDeployRequired = true;
            exerciser.updateStatusLine();
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Gives a quick test of the Configuration Manager Proxy's collective
     * deletion.
     * @param c CollectiveProxy object to delete
     */
    public void testDeleteCollective(CollectiveProxy c) {

        try {
            String colName = c.getName();
            TopologyProxy t = (TopologyProxy)c.getParent();
            t.deleteCollective(colName);
            exerciser.reportActionSubmitted();
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    
    /**
     * Displays the connections in the pub/sub topology.
     */
    public void testListConnections() {

        try {
            ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
            TopologyProxy t = cmp.getTopology();
            Enumeration e = t.getConnections();

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
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test of the commands to change
     * topology properties
     * @param t Selected TopologyProxy
     * @param newName New name of the object
     * @param shortDesc New Short Description
     * @param longDesc New Long Description
     */
    public void testModifyTopologyProperties(TopologyProxy t,
                                              String newName,
                                              String shortDesc,
                                              String longDesc) {
        ClassTesterForAdministeredObject.testModifyStandardProperties(exerciser, t, newName, shortDesc, longDesc);
        exerciser.reportActionSubmitted();
    }
    
    /**
     * Gives a quick test of the Configuration Manager Proxy's deployment
     * functionality.
     * @param t Topology to deploy
     */
    public void testDeployTopologyConfiguration(TopologyProxy t) {

        try {
            exerciser.topologyDeployRequired = false;
            exerciser.updateStatusLine();
            DeployResult ds = t.deploy(
                    ResourcesHandler.getUserSettingBoolean(ResourcesHandler.INCREMENTAL_DEPLOY, true),
                    ResourcesHandler.getUserSettingLong(ResourcesHandler.DEPLOY_WAIT_TIME, 0));
            exerciser.reportDeployResult(ds);
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Adds to the supplied Properties table a set of key/value pairs that
     * describe some TopologyProxy-specific methods that may
     * be invoked on the supplied object, and the returned value from
     * those methods.
     * @param proxy A valid TopologyProxy
     * @param p A valid Properties object
     */
    public void discoverProperties(TopologyProxy t, Properties p) {
        
        // These methods set may fail with a
        // ConfigManagerProxyPropertyNotInitialisedException, which means
        // that information on the administered object was not supplied by
        // the Configuration Manager before a timeout occurred. If this
        // happens for *one* of these methods it will happen for *all*, so it
        // is acceptable to enclose all of this section in a single
        // try/catch block.
        try {
            Enumeration e = null;
            StringBuffer key;
            StringBuffer value;
            if (exerciser.showAdvanced()) {
                // ---------- Display collectives ----------
                key = new StringBuffer("getCollectives()");
                value = new StringBuffer();
                e = t.getCollectives(null);
                if (e == null) {
                    value.append(""+e);
                } else {
                    int count = 0;
                    while (e.hasMoreElements()) {
                        count++;
                        key.append("\n    ["+count+"]");
                        CollectiveProxy c = (CollectiveProxy)e.nextElement();
                        value.append("\n"+CMPAPIExerciser.formatAdminObject(c));
                    }
                }
                p.setProperty(""+key, ""+value);
                
                // ---------- Display brokers ----------
                key = new StringBuffer("getBrokers()");
                value = new StringBuffer();
                e = t.getBrokers(null);
                if (e == null) {
                    value.append(""+e);
                } else {
                    int count = 0;
                    while (e.hasMoreElements()) {
                        count++;
                        key.append("\n    ["+count+"]");
                        BrokerProxy b = (BrokerProxy)e.nextElement();
                        value.append("\n"+CMPAPIExerciser.formatAdminObject(b));
                    }
                }
                p.setProperty(""+key, ""+value);
            }
            
            
            // ---------- Display connections ----------
            key = new StringBuffer("getConnections()");
            value = new StringBuffer();
            e = t.getConnections();
            
            if (e == null) {
                value.append(""+e);
            } else {
                int count = 0;
                while (e.hasMoreElements()) {
                    count++;
                    key.append("\n    ["+count+"]");
                    
                    TopologyProxy.Connection c = (TopologyProxy.Connection)e.nextElement();
                    String sourceUUID = c.source;
                    String targetUUID = c.target;
                    
                    // Map the UUIDs to the relevant BrokerProxy objects
                    BrokerProxy source = t.getBroker(BrokerProxy.withUUID(sourceUUID));
                    BrokerProxy target = t.getBroker(BrokerProxy.withUUID(targetUUID));
                    
                    value.append("\n"+CMPAPIExerciser.formatAdminObject(source) +
                                 " ==> " + CMPAPIExerciser.formatAdminObject(target));
                }
            }
            p.setProperty(""+key, ""+value);
            p.setProperty("getNumberOfConnections()", ""+t.getNumberOfConnections());
            
        } catch (ConfigManagerProxyPropertyNotInitializedException ex) {
            exerciser.log(ex);
        }
       
        
    }
    
    
}
