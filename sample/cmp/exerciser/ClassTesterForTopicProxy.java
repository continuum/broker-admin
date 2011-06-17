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

import com.ibm.broker.config.proxy.AttributeConstants;
import com.ibm.broker.config.proxy.ConfigManagerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyException;
import com.ibm.broker.config.proxy.ConfigManagerProxyLoggedException;
import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.TopicProxy;
import com.ibm.broker.config.proxy.TopicProxy.Policy;

/*****************************************************************************
 * <p>The TopicProxy object represents a topic in
 * the topic hierarchy. Note that TopicRootProxy is a
 * specialization of the TopicProxy class which
 * represents the root of the topic hierarchy.
 * <p>
 * <b>NOTE:</b>
 * <p>
 * Most methods in this class tester take a TopicProxy
 * parameter. If you wish to gain a handle to such an object
 * in your own code, use something like:
 * <pre>
 * ConfigManagerConnectionParameters cmcp =
 *   MQConfigManagerConnectionParameters("localhost", 1414, "QMGR");
 * ConfigManagerProxy cmp = ConfigManagerProxy.getInstance(cmcp);
 * TopicRootProxy t0 = cmp.getTopicRoot();
 * TopicProxy t1 = t0.getChildTopicByName("shares");
 * TopicProxy t2 = t1.getChildTopicByName("tech");
 * TopicProxy t3 = t2.getChildTopicByName("ibm");
 * </pre>
 * 
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ClassTesterForTopicProxy</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Services to test TopicProxy APIs
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
 * @version Samples/ConfigManagerProxy/cmp/exerciser/ClassTesterForTopicProxy.java, Config.Proxy, S000, S000-L50818.2 1.13
 *****************************************************************************/
public class ClassTesterForTopicProxy {
    
    /**
     * GUI object to which the tester is linked
     */
    CMPAPIExerciser exerciser;
    
    /**
     * Instantiates a new ClassTesterForTopicProxy that is
     * linked to the supplied GUI
     * @param exerciser GUI object to which the tester is linked
     */
    ClassTesterForTopicProxy(CMPAPIExerciser exerciser) {
        this.exerciser=exerciser;
    }
    
    /**
     * Gives a quick test of the commands to change topic properties
     * @param object Selected AdministeredObject
     * @param newName New name of the topic
     * @param newQoPS New QoP level to apply to the root
     * (String representation)
     * @param mcEnabled Multicast enabled parameter
     * @param mcIPv4GroupAddress Multicast IPv4 Group Address parameter
     * @param mcEncrypted True iff multicast encryption is enabled
     * @param mcQoS True iff multicast reliability is enabled  
     */
    public void testModifyTopicProperties(TopicProxy object,
                                           String newName,
                                           String newQoPS,
                                           String mcEnabled,
                                           String mcIPv4GroupAddress,
                                           boolean mcEncrypted,
                                           boolean mcQoS) {

        
        TopicProxy.QoP newQoP = TopicProxy.QoP.getQualityOfProtectionFromString(newQoPS);
        ClassTesterForAdministeredObject.testModifyStandardProperties(exerciser, object, newName, "", "");

        try {
            TopicProxy.QoP oldQoP = object.getQualityOfProtectionLevel();
            String oldMCEnabled = object.getMulticastEnabled();
            String oldMCIPv4GroupAddress = object.getMulticastIPv4GroupAddress();
            boolean oldMCEncrypted = object.getMulticastEncrypted();
            boolean oldMCQoS = object.getMulticastQualityOfService();

            if (oldQoP != newQoP) {
                object.setQualityOfProtectionLevel(newQoP);
                exerciser.reportActionSubmitted();
            }

            if (oldMCEnabled != mcEnabled) {
                object.setMulticastEnabled(mcEnabled);
                exerciser.reportActionSubmitted();
            }

            if (oldMCIPv4GroupAddress != mcIPv4GroupAddress) {
                object.setMulticastGroupAddress(mcIPv4GroupAddress, null);
                exerciser.reportActionSubmitted();
            }

            if (oldMCEncrypted != mcEncrypted) {
                object.setMulticastEncrypted(mcEncrypted);
                exerciser.reportActionSubmitted();
            }

            if (oldMCQoS != mcQoS) {
                object.setMulticastQualityOfService(mcQoS);
                exerciser.reportActionSubmitted();
            }
        } catch (ConfigManagerProxyPropertyNotInitializedException ex) {
            exerciser.log(ex);
        } catch (ConfigManagerProxyLoggedException e2) {
            exerciser.log(e2);
        }
        
        exerciser.topicDeployRequired = true;
        exerciser.updateStatusLine();
        
    }
    
    /**
     * Gives a quick test of topic creation
     * @param parent Parent topic
     * @param childName Name of new child
     */
    public void testCreateTopic(TopicProxy parent, String childName) {
        try {
            parent.createChildTopic(childName);
            exerciser.reportActionSubmitted();
            exerciser.topicDeployRequired = true;
            exerciser.updateStatusLine();
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Asks the Configuration Manager to delete the specified topic.
     * @param topic Topic to delete
     */
    public void testDeleteTopic(TopicProxy topic) {
        try {
            if (topic != null) {
                TopicProxy parent = (TopicProxy) topic.getParent();
                parent.deleteChildTopic(topic.getName());
                exerciser.reportActionSubmitted();
                exerciser.topicDeployRequired = true;
                exerciser.updateStatusLine();
            }
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Gives a quick test of the Configuration Manager Proxy's move topic
     * functionality.
     * @param oldParent Parent topic who owns the topic about to move
     * @param topicToMove Name of the topic to move (e.g. "IBM")
     * @param newParentPath String describing the path to the new
     * parent. Path should be described top down using '/' to
     * separate items in the path, without the name
     * of the current topic at the end (e.g. "Shares/Tech");
     */
    public void testMoveTopic(TopicProxy oldParent, String topicToMove, String newParentPath) {

        try {
            ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
            TopicProxy root = cmp.getTopicRoot();
            TopicProxy newParent = getTopicFromString(root, newParentPath);

            if (oldParent == null) {
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.INVALID_PATH_TO_TOPIC));
            }
            if (newParent == null) {
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.INVALID_PATH_TO_NEW_TREE));
            }

            if ((oldParent != null)&&(newParent != null)) {
                // Moving <topicToMove> from <oldParent.getName()> to <newParent.getName()>
                oldParent.moveTopic(topicToMove, newParent);
                exerciser.reportActionSubmitted();
                exerciser.topicDeployRequired = true;
                exerciser.updateStatusLine();
            }
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Gives a quick test of default policy creation
     * @param topic
     * @param name Name of the principal
     * @param type Type of the principal (user/group)
     * @param publish Whether publish is allowed
     * @param subscribe Whether subscribe is allowed
     * @param persist Whether persistent
     */
    public void testAddDefaultPolicy(TopicProxy topic, String name, String type, String publish, String subscribe, String persist) {
        try {
            topic.addDefaultPolicy(new TopicProxy.Policy (name, type, publish, subscribe, persist) );
            exerciser.reportActionSubmitted();
            exerciser.topicDeployRequired = true;
            exerciser.updateStatusLine();
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Gives a quick test of removing a default policy
     * @param topic
     */
    public void testRemoveDefaultPolicy(TopicProxy topic) {
        try {
            topic.removeDefaultPolicy();
            exerciser.reportActionSubmitted();
            exerciser.topicDeployRequired = true;
            exerciser.updateStatusLine();
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Gives a quick test of principal creation
     * @param topic
     * @param name Name of the principal
     * @param type Type of the principal (user/group)
     * @param publish Whether publish is allowed
     * @param subscribe Whether subscribe is allowed
     * @param persist Whether persistent
     */
    public void testAddPolicy(TopicProxy topic, String name, String type, String publish, String subscribe, String persist) {
        try {
            topic.addPolicies(new TopicProxy.Policy[] { new TopicProxy.Policy(name, type, publish, subscribe, persist) });
            exerciser.reportActionSubmitted();
            exerciser.topicDeployRequired = true;
            exerciser.updateStatusLine();
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Gives a quick test of policy removal
     * @param topic
     * @param name Name of the principal
     * @param type Type of the principal (user/group)
     * @param publish Whether publish is allowed
     * @param subscribe Whether subscribe is allowed
     * @param persist Whether persistent
     */
    public void testRemovePolicy(TopicProxy topic, String name, String type, String publish, String subscribe, String persist) {
        try {
            topic.removePolicies(new TopicProxy.Policy[] { new TopicProxy.Policy(name, type, publish, subscribe, persist) });
            exerciser.reportActionSubmitted();
            exerciser.topicDeployRequired = true;
            exerciser.updateStatusLine();
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Returns the topic element found at the supplied
     * position in the hierarchy.
     * @param root TopicProxy to begin the search from
     * @param topic /-separated String containing the path name to the topic
     * (e.g. Shares/Tech/IBM)
     * @return TopicProxy which represents the supplied topic, or null
     * if the topic could not be found.
     */
    private TopicProxy getTopicFromString(TopicProxy root, String topic) {
        TopicProxy retVal = null;

        int positionOfFirstSeparator = topic.indexOf("/");

        // If topic was "Shares/Tech/IBM" then
        // nextChildsName would be "Shares"
        // and remainingPathName would be "Tech/IBM"
        String nextChildsName = "";
        String remainingPathName = "";
        if (positionOfFirstSeparator>-1) {
            nextChildsName = topic.substring(0, positionOfFirstSeparator);
            remainingPathName = topic.substring(positionOfFirstSeparator+1);
        } else {
            nextChildsName = topic;
        }

        try {
            if (root != null) {
                TopicProxy parent = root.getChildTopic(TopicProxy.withName(nextChildsName));

                // If we are at the last topic in the tree (e.g. after "IBM")
                // then break out of the recursion.
                if (!remainingPathName.equals("")) {
                    retVal = getTopicFromString(parent, remainingPathName);
                } else {
                    retVal = parent;
                }
            } else {
                retVal = null;
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.NO_SUCH_TOPIC));
            }
        } catch (Exception ex) {
            exerciser.log(ex);
        }

        return retVal;
    }
    
    /**
     * Modifies the default policy
     * @param topic Topic to modify (may be the topic root)
     * @param publish Default publish behaviour
     * @param subscribe Default subscribe behaviour
     * @param persist Default persistence
     */
    public void testModifyDefaultPolicy(TopicProxy topic,
                                        String publish,
                                        String subscribe,
                                        String persist) {
        try {
            TopicProxy.Policy p = new TopicProxy.Policy(AttributeConstants.TOPIC_PRINCIPAL_PUBLICGROUP,
                    									AttributeConstants.TOPIC_PRINCIPAL_PUBLICGROUP,
                                                        publish,
                                                        subscribe,
                                                        persist);
            topic.setDefaultPolicy(p);
            exerciser.reportActionSubmitted();
            exerciser.topicDeployRequired = true;
            exerciser.updateStatusLine();
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }
    
   
    /**
     * Adds to the supplied Properties table a set of key/value pairs that
     * describe some TopicProxy-specific methods that may
     * be invoked on the supplied object, and the returned value from
     * those methods.
     * @param t A valid TopicProxy
     * @param p A valid Properties object
     */
    public void discoverProperties(TopicProxy t, Properties p) {
        
        // These methods set may fail with a
        // ConfigManagerProxyPropertyNotInitialisedException, which means
        // that information on the administered object was not supplied by
        // the Configuration Manager before a timeout occurred. If this
        // happens for *one* of these methods it will happen for *all*, so it
        // is acceptable to enclose all of this section in a single
        // try/catch block.
        try {
            StringBuffer key;
            StringBuffer value;
            Enumeration e;
            
            if (exerciser.showAdvanced()) {
                // ---------- Display child topics ----------
                key = new StringBuffer("getChildTopics()");
                value = new StringBuffer();
                e = t.getChildTopics(null);
                if (e == null) {
                    value.append(""+e);
                } else {
                    int count = 0;
                    while (e.hasMoreElements()) {
                        count++;
                        key.append("\n    ["+count+"]");
                        value.append("\n");
                        TopicProxy t1 = (TopicProxy)e.nextElement();
                        value.append(CMPAPIExerciser.formatAdminObject(t1));
                    }
                }
                p.setProperty(""+key, ""+value);
            }
            
            
            // -------- Policies ---------
            p.setProperty("getDefaultPolicy()", formatPolicy(t.getDefaultPolicy(), true));
            key = new StringBuffer("getPolicies()");
            value = new StringBuffer();
            e = t.getPolicies();
            
            if (e == null) {
                value.append(""+e);
            } else {
                int count = 0;
                while (e.hasMoreElements()) {
                    count++;
                    TopicProxy.Policy pol = (TopicProxy.Policy)e.nextElement();
                    
                    if (t.getNumberOfPolicies() > 1) {
                        // If there is only one policy defined, display it
                        // on the same line as the 'getPolicies()' label.
                        // Otherwise, span the list over multiple lines.
                        key.append("\n    ["+count+"]");
                        value.append("\n");
                    }
                    value.append(formatPolicy(pol, false));
                }
            }
            p.setProperty(""+key, ""+value);
            p.setProperty("getNumberOfPolicies()", ""+t.getNumberOfPolicies());
            
            if (exerciser.showAdvanced()) {
                // ---------- Misc info ---------
                p.setProperty("getQualityOfProtectionLevel()", ""+t.getQualityOfProtectionLevel());
                p.setProperty("getTopicName()", ""+t.getTopicName());
                
                // ---------- Multicast info ----------
                p.setProperty("getMulticastEnabled()", ""+t.getMulticastEnabled());
                p.setProperty("getMulticastEncrypted()", ""+t.getMulticastEncrypted());
                p.setProperty("getMulticastIPv4GroupAddress()", ""+t.getMulticastIPv4GroupAddress());
                p.setProperty("getMulticastQualityOfService()", ""+t.getMulticastQualityOfService());
                p.setProperty("getMulticastQualityOfService()", ""+t.getMulticastQualityOfService());
                
            }
        } catch (ConfigManagerProxyPropertyNotInitializedException ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Returns a string representation of a toplc's policy 
     * @param p Policy to format
     * @return String
     */
    private String formatPolicy(Policy p, boolean isADefaultPolicy) {
        String retVal;
        if (p == null) {
            retVal = ""+p;
        } else {
            if (isADefaultPolicy) {
                retVal = "pub="+p.getPublish() + " " +
                         "sub="+p.getSubscribe() + " " +
                         "persist="+p.getPersistence();
            } else {
                retVal = "(" + p.getPrincipalType()+") " +
                         p.getPrincipalName()+ " " +
                         "pub="+p.getPublish() + " " +
                         "sub="+p.getSubscribe() + " " +
                         "persist="+p.getPersistence();
            }
        }
        return retVal;
    }

    


}
