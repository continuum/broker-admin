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

import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.DeployResult;
import com.ibm.broker.config.proxy.TopicRootProxy;

/*****************************************************************************
 * <p>The TopicRootProxy object represents the root of
 * the topic hierarchy.
 * <p>
 * <b>NOTE:</b>
 * <p>
 * Most methods in this class tester take a TopicRootProxy
 * parameter. If you wish to gain a handle to such an object
 * in your own code, use something like:
 * <pre>
 * ConfigManagerConnectionParameters cmcp =
 *   MQConfigManagerConnectionParameters("localhost", 1414, "QMGR");
 * ConfigManagerProxy cmp = ConfigManagerProxy.getInstance(cmcp);
 * TopicRootProxy t0 = cmp.getTopicRoot();
 * </pre>
 * 
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ClassTesterForTopicRootProxy</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Services to test TopicRootProxy APIs
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
 * @version Samples/ConfigManagerProxy/cmp/exerciser/ClassTesterForTopicRootProxy.java, Config.Proxy, S000, S000-L50818.2 1.8
 *****************************************************************************/
public class ClassTesterForTopicRootProxy {
    
    /**
     * GUI object to which the tester is linked
     */
    CMPAPIExerciser exerciser;
    
    /**
     * The object responsible for performing actions on
     * TopicProxy instances.
     */
    public ClassTesterForTopicProxy classTesterTopic = null;
    
    /**
     * Instantiates a new ClassTesterForTopicRootProxy that is
     * linked to the supplied GUI
     * @param exerciser GUI object to which the tester is linked
     */
    ClassTesterForTopicRootProxy(CMPAPIExerciser exerciser, ClassTesterForTopicProxy classTesterTopic) {
        this.exerciser=exerciser;
        this.classTesterTopic = classTesterTopic;
    }
    
    /**
     * Displays all users defined on the user name server
     * @param topicRoot Handle to the TopicRootProxy object
     */
    public void testDisplayUsers(TopicRootProxy topicRoot) {
        try {
            Enumeration e = topicRoot.getUsers();
            if (e.hasMoreElements()) {
                while (e.hasMoreElements()) {
                    exerciser.log("(User) "+e.nextElement());
                }
            } else {
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.NO_USERS_DEFINED));
            }
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Displays all groups defined on the user name server
     * @param topicRoot Handle to the TopicRootProxy object
     */
    public void testDisplayGroups(TopicRootProxy topicRoot) {
        try {
            Enumeration e = topicRoot.getGroups();
            if (e.hasMoreElements()) {
                while (e.hasMoreElements()) {
                    exerciser.log("(Group) "+e.nextElement());
                }
            } else {
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.NO_GROUPS_DEFINED));
            }
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Displays all users defined on the user name server
     * @param topicRoot Handle to the TopicRootProxy object
     */
    public void testDisplayPublicGroups(TopicRootProxy topicRoot) {
        try {
            Enumeration e = topicRoot.getPublicGroups();
            if (e.hasMoreElements()) {
                while (e.hasMoreElements()) {
                    exerciser.log("(PublicGroup) "+e.nextElement());
                }
            } else {
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.NO_PUBLIC_GROUPS_DEFINED));
            }
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test of the Configuration Manager Proxy's deployment
     * functionality.
     * @param topicRoot Handle to the TopicRootProxy object
     */
    public void testDeployTopicConfiguration(TopicRootProxy topicRoot) {

        try {
            DeployResult ds = topicRoot.deploy(
                    ResourcesHandler.getUserSettingBoolean(ResourcesHandler.INCREMENTAL_DEPLOY, true),
                    ResourcesHandler.getUserSettingInt(ResourcesHandler.DEPLOY_WAIT_TIME, 0));
            exerciser.reportDeployResult(ds);
            exerciser.topicDeployRequired = false;
            exerciser.updateStatusLine();
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Adds to the supplied Properties table a set of key/value pairs that
     * describe some TopicRootProxy-specific methods that may
     * be invoked on the supplied object, and the returned value from
     * those methods.
     * @param tr A valid TopicRootProxy
     * @param p A valid Properties object
     */
    public void discoverProperties(TopicRootProxy tr, Properties p) {
        
        // As TopicRootProxy is-a TopicProxy, discover the properties
        // from the TopicProxy tester.
        classTesterTopic.discoverProperties(tr, p);
        
        // Now add the properties specific to a TopicRootProxy.
        
        // These methods set may fail with a
        // ConfigManagerProxyPropertyNotInitialisedException, which means
        // that information on the administered object was not supplied by
        // the Configuration Manager before a timeout occurred. If this
        // happens for *one* of these methods it will happen for *all*, so it
        // is acceptable to enclose all of this section in a single
        // try/catch block.
        try {
            
            // -------- User list ---------
            StringBuffer sb = new StringBuffer();
            Enumeration e = tr.getUsers();
            while (e.hasMoreElements()) {
                sb.append(e.nextElement());
                if (e.hasMoreElements()) {
                    sb.append(", ");
                }
            }
            p.setProperty("getUsers()", sb.toString());
            
            // -------- Group list ---------
            sb = new StringBuffer();
            e = tr.getGroups();
            while (e.hasMoreElements()) {
                sb.append(e.nextElement());
                if (e.hasMoreElements()) {
                    sb.append(", ");
                }
            }
            p.setProperty("getGroups()", sb.toString());
            
            // -------- Public group list ---------
            sb = new StringBuffer();
            e = tr.getPublicGroups();
            while (e.hasMoreElements()) {
                sb.append(e.nextElement());
                if (e.hasMoreElements()) {
                    sb.append(", ");
                }
            }
            p.setProperty("getPublicGroups()", sb.toString());
            
        } catch (ConfigManagerProxyPropertyNotInitializedException ex) {
            exerciser.log(ex);
        }
    }


}
