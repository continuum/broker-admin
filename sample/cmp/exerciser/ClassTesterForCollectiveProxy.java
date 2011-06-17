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
import com.ibm.broker.config.proxy.TopologyProxy;
 
/*****************************************************************************
 * <p>The CollectiveProxy object represents a totally connected
 * set of brokers (i.e. that can all share pub/sub information).
 * <p>
 * <b>NOTE:</b>
 * <p>
 * Most methods in this class tester take a CollectiveProxy
 * parameter. If you wish to gain a handle to such an object
 * in your own code, use something like:
 * <pre>
 * ConfigManagerConnectionParameters cmcp =
 *   MQConfigManagerConnectionParameters("localhost", 1414, "QMGR");
 * ConfigManagerProxy cmp = ConfigManagerProxy.getInstance(cmcp);
 * TopologyProxy t = cmp.getTopology();
 * CollectiveProxy c = t.getCollectiveByName("myCollective");
 * </pre>
 * 
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ClassTesterForCollectiveProxy</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Services to test CollectiveProxy APIs
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
 * @version Samples/ConfigManagerProxy/cmp/exerciser/ClassTesterForCollectiveProxy.java, Config.Proxy, S000, S000-L50818.2 1.9
 *****************************************************************************/
public class ClassTesterForCollectiveProxy {
    
    /**
     * GUI object to which the tester is linked
     */
    CMPAPIExerciser exerciser;
    
    /**
     * Instantiates a new ClassTesterForCollectiveProxy that is
     * linked to the supplied GUI
     * @param exerciser GUI object to which the tester is linked
     */
    ClassTesterForCollectiveProxy(CMPAPIExerciser exerciser) {
        this.exerciser=exerciser;
    }
    
    /**
     * Gives a quick test of the commands to change
     * collective properties
     * @param object Selected collective
     * @param newName New name of the collective
     * @param shortDesc New Short Description
     * @param longDesc New Long Description
     */
    public void testModifyCollectiveProperties(CollectiveProxy object,
                                                String newName,
                                                String shortDesc,
                                                String longDesc) {

        ClassTesterForAdministeredObject.testModifyStandardProperties(exerciser, object, newName, shortDesc,longDesc);
        exerciser.reportActionSubmitted();
    }
    
    /**
     * Gives a quick test of the Configuration Manager Proxy's collective
     * addition of brokers function.
     * @param c - Collective to which brokers will be added
     * @param brokerList - semi-colon separated list of broker uuids to add
     */
    public void testAddToCollective(CollectiveProxy c, String brokerList) {
        try {
            
            // Convert b1name;b2name;b3name to b1uuid;b2uuid;b3uuid.
            boolean okay = true;
            ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
            TopologyProxy t = cmp.getTopology();
            String[] brokers = brokerList.split(";");
            for (int i=0; i<brokers.length; i++) {
                String brokerName = brokers[i];
                BrokerProxy b = t.getBrokerByName(brokerName);
                if (b != null) {
                    brokers[i] = b.getUUID();
                } else {
                    exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_DOES_NOT_EXIST, new String[] {brokerName}));
                    okay = false;
                }
            }
            
            if (okay) {
                c.addBrokers(brokers);
                exerciser.reportActionSubmitted();
                exerciser.topologyDeployRequired = true;
                exerciser.updateStatusLine();
            }
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test of the Configuration Manager Proxy's collective
     * removal of brokers function.
     * @param c Collective from which brokers will be removed
     * @param brokerList - semi-colon separated list of broker uuids to remove
     */
    public void testRemoveFromCollective(CollectiveProxy c, String brokerList) {
        try {
            // Convert b1name;b2name;b3name to b1uuid;b2uuid;b3uuid.
            boolean okay = true;
            ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
            TopologyProxy t = cmp.getTopology();
            String[] brokers = brokerList.split(";");
            for (int i=0; i<brokers.length; i++) {
                String brokerName = brokers[i];
                BrokerProxy b = t.getBrokerByName(brokerName);
                if (b != null) {
                    brokers[i] = b.getUUID();
                } else {
                    exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_DOES_NOT_EXIST, new String[] {brokerName}));
                    okay = false;
                }
            }
            
            if (okay) {
                c.removeBrokers(brokers);
                exerciser.reportActionSubmitted();
                exerciser.topologyDeployRequired = true;
                exerciser.updateStatusLine();
            }

        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Adds to the supplied Properties table a set of key/value pairs that
     * describe some CollectiveProxy-specific methods that may
     * be invoked on the supplied object, and the returned value from
     * those methods.
     * @param c A valid CollectiveProxy
     * @param p A valid Properties object
     */
    public void discoverProperties(CollectiveProxy c, Properties p) {
        
        // The only property unique to the collective is the
        // list of broker UUIDs.
        //
        // Use:
        //     BrokerProxy b = (BrokerProxy) topologyProxy.getBroker(BrokerProxy.withUUID(uuid));
        // to convert from broker UUIDs to BrokerProxy instances.
        
        Enumeration e = null;
        try {
            e = c.getBrokerUUIDs();
        } catch (ConfigManagerProxyPropertyNotInitializedException ex) {
            exerciser.log(ex);
        }
        
        // Display one broker per line
        StringBuffer key = new StringBuffer("getBrokerUUIDs()");
        StringBuffer value = new StringBuffer();
        if (e == null) {
            value.append(""+e);
        } else {    
            int count = 1;
            while (e.hasMoreElements()) {
                key.append("\n  ["+(count++)+"]");
                value.append("\n"+(String) e.nextElement());
            }
        }
        p.setProperty(""+key, ""+value); 
    }
    
    
}
