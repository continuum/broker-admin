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
import java.util.Vector;

import cmp.common.ResourcesHandler;

import com.ibm.broker.config.proxy.AccessControlEntry;
import com.ibm.broker.config.proxy.AccessControlEntryPermission;
import com.ibm.broker.config.proxy.AccessControlEntryPrincipalType;
import com.ibm.broker.config.proxy.AdministeredObject;
import com.ibm.broker.config.proxy.AttributeConstants;
import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.CollectiveProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyLoggedException;
import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.ConfigurationObjectType;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.LogEntry;
import com.ibm.broker.config.proxy.LogProxy;
import com.ibm.broker.config.proxy.MessageFlowProxy;
import com.ibm.broker.config.proxy.SubscriptionsProxy;
import com.ibm.broker.config.proxy.TopicProxy;
import com.ibm.broker.config.proxy.TopicRootProxy;
import com.ibm.broker.config.proxy.TopologyProxy;

/*****************************************************************************
 * <p>The AdministeredObject class is the superclass of all
 * objects managed by the Configuration Manager. This includes
 * all BrokerProxy, ExecutionGroupProxy, SubscriptionsProxy,
 * MessageFlowProxy, LogProxy, ConfigManagerProxy objects.
 * <p>As such, this class tester is used to test methods
 * common to all administered object instances.
 * 
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ClassTesterForAdministeredObject</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Services to test general AdministeredObject APIs
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
 * @version Samples/ConfigManagerProxy/cmp/exerciser/ClassTesterForAdministeredObject.java, Config.Proxy, S000, S000-L50818.2 1.13
 *****************************************************************************/
public class ClassTesterForAdministeredObject {
    
    /** GUI object to which the tester is linked */
    CMPAPIExerciser exerciser;
    
    /** ConfigManagerProxy class tester to which the tester is linked */
    private ClassTesterForConfigManagerProxy classTesterCMP;
    /** CollectiveProxy class tester to which the tester is linked */
    private ClassTesterForCollectiveProxy classTesterCollective;
    /** BrokerProxy class tester to which the tester is linked */
    private ClassTesterForBrokerProxy classTesterBroker;
    /** TopologyProxy class tester to which the tester is linked */
    private ClassTesterForTopologyProxy classTesterTopology;
    /** TopicProxy class tester to which the tester is linked */
    private ClassTesterForTopicProxy classTesterTopic;
    /** LogProxy class tester to which the tester is linked */
    private ClassTesterForLogProxy classTesterLog;
    /** TopicRootProxy class tester to which the tester is linked */
    private ClassTesterForTopicRootProxy classTesterTopicRoot;
    /** ExecutionGroupProxy class tester to which the tester is linked */
    private ClassTesterForExecutionGroupProxy classTesterEG;
    /** MessageFlowProxy class tester to which the tester is linked */
    private ClassTesterForMessageFlowProxy classTesterFlow;
    
    /**
     * Instantiates a new ClassTesterForAdministeredObject that is
     * linked to the supplied GUI
     * @param exerciser GUI object to which the tester is linked
     * @param classTesterLog
     * @param classTesterTopic
     * @param classTesterTopicRoot
     * @param classTesterEG
     * @param classTesterFlow
     * @param classTesterBroker
     * @param classTesterCollective
     * @param classTesterTopology
     * @param classTesterCMP
     */
    ClassTesterForAdministeredObject(CMPAPIExerciser exerciser,
            ClassTesterForConfigManagerProxy classTesterCMP,
            ClassTesterForTopologyProxy classTesterTopology,
            ClassTesterForCollectiveProxy classTesterCollective,
            ClassTesterForBrokerProxy classTesterBroker,
            ClassTesterForExecutionGroupProxy classTesterEG,
            ClassTesterForMessageFlowProxy classTesterFlow,
            ClassTesterForTopicRootProxy classTesterTopicRoot,
            ClassTesterForTopicProxy classTesterTopic,
            ClassTesterForLogProxy classTesterLog) {
        this.exerciser=exerciser;
        this.classTesterCMP = classTesterCMP;
        this.classTesterTopology = classTesterTopology;
        this.classTesterCollective = classTesterCollective;
        this.classTesterBroker = classTesterBroker;
        this.classTesterEG = classTesterEG;
        this.classTesterFlow = classTesterFlow;
        this.classTesterTopicRoot = classTesterTopicRoot;
        this.classTesterTopic = classTesterTopic;
        this.classTesterLog = classTesterLog;
    }
    
    /**
     * Gives a quick test of the AdministeredObject refresh
     * functionality.
     * @param obj
     */
    public void testRefresh(AdministeredObject obj) {

        try {
            obj.refresh();
            exerciser.reportActionSubmitted();
        } catch (Exception e) {
            exerciser.log(e);
        }
    }
    
    /**
     * Grants access to the supplied object for the supplied credentials
     * @param obj Object whose ACL is to be modified
     * @param principalName User or group name (on the CM machine)
     * of the principal who is to be granted access.
     * @param principalType Type of the principal mentioned above - USER or GROUP
     * @param machineOrDomainName Name of the machine or domain on which the ID resides
     * (blank = all machines)
     * @param permission Authority to be granted - FULL, EDIT, VIEW or DEPLOY.
     */
    public void testGrantAccess(AdministeredObject obj, String principalName, String principalType, String machineOrDomainName, String permission) {
        AccessControlEntryPermission acePermission = AccessControlEntryPermission.getAccessControlEntryPermission(permission.trim().substring(0,1));
        AccessControlEntryPrincipalType aceType = AccessControlEntryPrincipalType.getAccessControlEntryPrincipalType(principalType);
        String aceName = null;
        boolean paramsOK = true;
        
        // Check that the values entered were valid
        if (acePermission == null) {
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.INVALID_PERMISSION));
            paramsOK = false;
        }
        
        if (aceType == null) {
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.INVALID_USER_TYPE));
            paramsOK = false;
        }
        
        if (principalName.trim().equals("")) {
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.INVALID_USER_NAME));
            paramsOK = false;
        }
        
        // If a machine or domain was specified, put it on the front of the user/group name
        if (!(machineOrDomainName.trim().equals(""))) {
            aceName = machineOrDomainName + AttributeConstants.DOMAIN_USER_DELIMITER + principalName;
        } else {
            aceName = principalName;
        }
        
        if (paramsOK) {
            // Create an AccessControlEntry that describes the intended access
            AccessControlEntry newEntry = new AccessControlEntry(
                aceName,
                aceType,
                acePermission);
            
            AccessControlEntry newACL[] = new AccessControlEntry[] { newEntry };
            
            // Add the Entry
            try {
                if(obj instanceof BrokerProxy)
                    ((BrokerProxy)obj).addAccessControlEntries(newACL);                    
                else if(obj instanceof ConfigManagerProxy)
                    ((ConfigManagerProxy)obj).addAccessControlEntries(newACL);
                else if(obj instanceof ExecutionGroupProxy)
                    ((ExecutionGroupProxy)obj).addAccessControlEntries(newACL);
                else if(obj instanceof SubscriptionsProxy)
                    ((SubscriptionsProxy)obj).addAccessControlEntries(newACL);                    
                else if(obj instanceof TopicRootProxy)
                    ((TopicRootProxy)obj).addAccessControlEntries(newACL);                    
                else if(obj instanceof TopologyProxy)
                    ((TopologyProxy)obj).addAccessControlEntries(newACL);
                else
                    exerciser.log("Access control not valid for objects of this type");
                
                exerciser.reportActionSubmitted();
            } catch (ConfigManagerProxyLoggedException ex) {
                exerciser.log(ex);
            }   
        }
    }
    
    /**
     * Removes all access to the supplied object for the specified user.
     * @param obj Object whose ACL is to be modified
     * @param principalName User or group name (on the CM machine)
     * of the principal who is to have access removed.
     * @param principalType Type of the principal mentioned above - USER or GROUP
     * @param machineOrDomainName Name of the machine or domain on which the ID resides
     * (blank = all machines)
     */
    public void testRemoveAccess(AdministeredObject obj, String principalName, String principalType, String machineOrDomainName) {
        AccessControlEntryPrincipalType targetType = AccessControlEntryPrincipalType.getAccessControlEntryPrincipalType(principalType);
        String targetName = null;
        boolean paramsOK = true;
        
        if (targetType == null) {
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.INVALID_USER_TYPE));
            paramsOK = false;
        }
        
        if (principalName.trim().equals("")) {
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.INVALID_USER_NAME));
            paramsOK = false;
        }
        
        // If a machine or domain was specified, put it on the front of the user/group name
        if (!(machineOrDomainName.trim().equals(""))) {
            targetName = machineOrDomainName + AttributeConstants.DOMAIN_USER_DELIMITER + principalName;
        } else {
            targetName = principalName;
        }
        
        if (paramsOK) {
            AccessControlEntryPrincipalType aceType = AccessControlEntryPrincipalType.getAccessControlEntryPrincipalType(principalType);

            // note that a permission is required to make a valid AccessControlEntry,
            // even though we are only actually using the ACE to delete an existing entry
            AccessControlEntry removeEntry = new AccessControlEntry(
                    targetName,
                    aceType,
                    AccessControlEntryPermission.view);
                                
            AccessControlEntry removeACL[] = new AccessControlEntry[] { removeEntry };
            
            // Remove the Entry
            try {
                if(obj instanceof BrokerProxy)
                    ((BrokerProxy)obj).removeAccessControlEntries(removeACL);                    
                else if(obj instanceof ConfigManagerProxy)
                    ((ConfigManagerProxy)obj).removeAccessControlEntries(removeACL);
                else if(obj instanceof ExecutionGroupProxy)
                    ((ExecutionGroupProxy)obj).removeAccessControlEntries(removeACL);
                else if(obj instanceof SubscriptionsProxy)
                    ((SubscriptionsProxy)obj).removeAccessControlEntries(removeACL);                    
                else if(obj instanceof TopicRootProxy)
                    ((TopicRootProxy)obj).removeAccessControlEntries(removeACL);                    
                else if(obj instanceof TopologyProxy)
                    ((TopologyProxy)obj).removeAccessControlEntries(removeACL);
                else
                    exerciser.log("Access control not valid for objects of this type");
                
                exerciser.reportActionSubmitted();
            } catch (ConfigManagerProxyLoggedException ex) {
                exerciser.log(ex);
            }   
        }
    }
    
    /**
     * Shows the access permissions for the supplied object (including
     * inherited permissions from parent objects)
     * @param obj The object whose permissions are to be displayed
     */
    public void testShowAccess(AdministeredObject obj) {
        
        AccessControlEntry acl[] = null;
        
        if(obj instanceof BrokerProxy)
            acl = ((BrokerProxy)obj).getAccessControlEntries();
        else if(obj instanceof ConfigManagerProxy)
            acl = ((ConfigManagerProxy)obj).getAccessControlEntries();
        else if(obj instanceof ExecutionGroupProxy)
            acl = ((ExecutionGroupProxy)obj).getAccessControlEntries();
        else if(obj instanceof SubscriptionsProxy)
            acl = ((SubscriptionsProxy)obj).getAccessControlEntries();
        else if(obj instanceof TopicRootProxy)
            acl = ((TopicRootProxy)obj).getAccessControlEntries();
        else if(obj instanceof TopologyProxy)
            acl = ((TopologyProxy)obj).getAccessControlEntries();

        for (int i=0; acl != null && i<acl.length; i++) {
            String name = acl[i].getName();
            AccessControlEntryPrincipalType type = acl[i].getType();
            AccessControlEntryPermission permission = acl[i].getPermission();
            
            String localizedPermission = "[???]";
            String localizedUserType = "[???]";
            if (permission == AccessControlEntryPermission.deploy)
                localizedPermission = ResourcesHandler.getNLSResource(ResourcesHandler.ACCESS_DEPLOY);
            if (permission == AccessControlEntryPermission.view)
                localizedPermission = ResourcesHandler.getNLSResource(ResourcesHandler.ACCESS_VIEW);
            if (permission == AccessControlEntryPermission.fullControl)
                localizedPermission = ResourcesHandler.getNLSResource(ResourcesHandler.ACCESS_FULL);
            if (permission == AccessControlEntryPermission.edit)
                localizedPermission = ResourcesHandler.getNLSResource(ResourcesHandler.ACCESS_EDIT);
            if (type == AccessControlEntryPrincipalType.user)
                localizedUserType = ResourcesHandler.getNLSResource(ResourcesHandler.USER);
            if (type == AccessControlEntryPrincipalType.group)
                localizedUserType = ResourcesHandler.getNLSResource(ResourcesHandler.GROUP);
            exerciser.log(ResourcesHandler.getNLSResource(
                    ResourcesHandler.SHOW_ACCESS_LINE,
                    new String[] { localizedUserType, name, localizedPermission, CMPAPIExerciser.formatAdminObject(obj)}));
        }
        
        // Recurse up the hierarchy, to find out inherited permissions
        try {
            AdministeredObject objParent = obj.getParent();
            if (objParent != null) {
                testShowAccess(objParent);
            }
        } catch (ConfigManagerProxyLoggedException e) {
            exerciser.log(e);
        }
        
    }
    
    /**
     * Displays in the log the complete property table
     * for the current object. 
     * @param obj
     */
    public void testShowRawPropertyTable(AdministeredObject obj) {

        try {
            Properties p = obj.getProperties();
            Enumeration e = p.keys();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = p.getProperty(key);
                exerciser.log(key+" = "+value);
            }
        } catch (Exception e) {
            exerciser.log(e);
        }
    }
    
    /**
     * Gives a quick test of the commands to change
     * an arbitrary administered object's properties
     * @param exerciser Handler (for logging capabilities)
     * @param object Selected AdministeredObject
     * @param newName New name
     * @param shortDesc New Short Description
     * @param longDesc New Long Description
     */
    static void testModifyStandardProperties(CMPAPIExerciser exerciser,
            								 AdministeredObject object,
                                             String newName,
                                             String shortDesc,
                                             String longDesc) {
        try {
            String oldName = "";
            String oldShortDesc = "";
            String oldLongDesc = "";

            try {
                oldName = object.getName();
                oldShortDesc = object.getShortDescription();
                oldLongDesc = object.getLongDescription();
            } catch (ConfigManagerProxyPropertyNotInitializedException ex) {
                exerciser.log(ex);
            }

            if (!oldName.equals(newName)) {
                object.setName(newName);
            }

            if (!oldShortDesc.equals(shortDesc)) {
                object.setShortDescription(shortDesc);
            }

            if (!oldLongDesc.equals(longDesc)) {
                object.setLongDescription(longDesc);
            }
        } catch (ConfigManagerProxyLoggedException ex) {
            exerciser.log(ex);
        }
    }


    /**
     * Populates a Properties object with a set of key/value pairs that
     * describe some methods that may be invoked on the supplied
     * object, and the returned value from those methods.
     * @param obj
     * @return Properties
     */
    protected Properties discoverProperties(AdministeredObject obj) {
        
        Properties retVal = new Properties();
        
        if (obj != null) {
            // Discover the properties that are found in every administered
            // object type
            
            // ---------- Misc info ----------
            retVal.setProperty("getConfigurationObjectType()", ""+obj.getConfigurationObjectType());
            retVal.setProperty("getUUID()", ""+obj.getUUID());
            if (exerciser.showAdvanced()) {
                retVal.setProperty("getTimeOfLastUpdate()", CMPAPIExerciser.formatGC(obj.getTimeOfLastUpdate()));
                retVal.setProperty("getTimeOfLastCompletionCode()", CMPAPIExerciser.formatGC(obj.getTimeOfLastCompletionCode()));
                retVal.setProperty("getLastUpdateUser()", ""+obj.getLastUpdateUser());
                retVal.setProperty("getLastCompletionCode()", ""+obj.getLastCompletionCode());
                retVal.setProperty("hasBeenRestrictedByConfigManager()", ""+obj.hasBeenRestrictedByConfigManager());
                retVal.setProperty("hasBeenUpdatedByConfigManager()", ""+obj.hasBeenUpdatedByConfigManager());
                retVal.setProperty("getConfigurationObjectTypeOfParent()", ""+obj.getConfigurationObjectTypeOfParent());
                retVal.setProperty("getType()", ""+obj.getType());
            }
            
            
            // ---------- More Misc info ----------
            // These methods set may fail with a
            // ConfigManagerProxyPropertyNotInitialisedException, which means
            // that information on the administered object was not supplied by
            // the Configuration Manager before a timeout occurred. If this
            // happens for *one* of these methods it will happen for *all*, so it
            // is acceptable to enclose all of this section in a single
            // try/catch block.
            try {
                retVal.setProperty("getLongDescription()", ""+obj.getLongDescription());
                retVal.setProperty("getShortDescription()", ""+obj.getShortDescription());
                retVal.setProperty("getName()", ""+obj.getName());
                
                if (exerciser.showAdvanced()) {
                    retVal.setProperty("getRepositoryTimestamp()", CMPAPIExerciser.formatDate(obj.getRepositoryTimestamp()));
                    retVal.setProperty("getNumberOfSubcomponents()", ""+obj.getNumberOfSubcomponents());
                    retVal.setProperty("isDeployed()", ""+obj.isDeployed());
                    retVal.setProperty("isShared()", ""+obj.isShared());
                }
            } catch (ConfigManagerProxyPropertyNotInitializedException ex) {
                exerciser.log(ex);
            }
            
            
            // ---------- Last BIP Messages ----------
            // Display one message per line
            Vector v = obj.getLastBIPMessages();
            StringBuffer key = new StringBuffer("getLastBIPMessages()");
            StringBuffer value = new StringBuffer();
            if (v == null) {
                value.append(""+v);
            } else {
                Enumeration e = v.elements();
                int count = 1;
                while (e.hasMoreElements()) {
                    LogEntry le = (LogEntry) e.nextElement();
                    key.append("\n    ["+(count++)+"]");
                    value.append("\n"+CMPAPIExerciser.getFirstLine(le.getDetail()));
                }
            }
            retVal.setProperty(""+key, ""+value);
            
            
            
            // ---------- Parent info ----------
            String parent;
            try {
                parent = CMPAPIExerciser.formatAdminObject(obj.getParent());
            } catch (ConfigManagerProxyLoggedException e1) {
                exerciser.log(e1);
                parent = "...?";
            }
            retVal.setProperty("getParent()", ""+parent);
            
            // ---------- ACLs ----------
            AccessControlEntry acls[];
            
            if(obj instanceof BrokerProxy)
                acls = ((BrokerProxy)obj).getAccessControlEntries();
            else if(obj instanceof ConfigManagerProxy)
                acls = ((ConfigManagerProxy)obj).getAccessControlEntries();
            else if(obj instanceof ExecutionGroupProxy)
                acls = ((ExecutionGroupProxy)obj).getAccessControlEntries();
            else if(obj instanceof SubscriptionsProxy)
                acls = ((SubscriptionsProxy)obj).getAccessControlEntries();
            else if(obj instanceof TopicRootProxy)
                acls = ((TopicRootProxy)obj).getAccessControlEntries();
            else if(obj instanceof TopologyProxy)
                acls = ((TopologyProxy)obj).getAccessControlEntries();
            else
                acls = new AccessControlEntry[0];

            StringBuffer aclRowsKey = new StringBuffer("getAccessControlEntries()");
            StringBuffer aclRowsValue = new StringBuffer();
            if (acls.length == 1) {
                aclRowsValue.append("("+acls[0].getType()+") "+
                                 acls[0].getName() + " " +
                                 acls[0].getPermission());
            } else {
                for (int i=0; i<acls.length; i++) {
                    if (i < acls.length) {
                        aclRowsValue.append("\n");
                    }
                    aclRowsKey.append("\n    ["+(i+1)+"]");
                    aclRowsValue.append("("+acls[i].getType()+") "+
                                 acls[i].getName() + " " +
                                 acls[i].getPermission());
                }
            }
            retVal.setProperty(aclRowsKey.toString(), aclRowsValue.toString());
            
            
            // Discover the properties that are specific to individual types.
            ConfigurationObjectType type = obj.getConfigurationObjectType();
            if (type == ConfigurationObjectType.configmanager) {
                classTesterCMP.discoverProperties((ConfigManagerProxy)obj, retVal);
            } else if (type == ConfigurationObjectType.topology) {
                classTesterTopology.discoverProperties((TopologyProxy)obj, retVal);
            } else if (type == ConfigurationObjectType.collective) {
                classTesterCollective.discoverProperties((CollectiveProxy)obj, retVal);
            } else if (type == ConfigurationObjectType.broker) {
                classTesterBroker.discoverProperties((BrokerProxy)obj, retVal);
            } else if (type == ConfigurationObjectType.executiongroup) {
                classTesterEG.discoverProperties((ExecutionGroupProxy)obj, retVal);
            } else if (type == ConfigurationObjectType.messageflow) {
                classTesterFlow.discoverProperties((MessageFlowProxy)obj, retVal);
            } else if (type == ConfigurationObjectType.topicroot) {
                classTesterTopicRoot.discoverProperties((TopicRootProxy)obj, retVal);
            } else if (type == ConfigurationObjectType.topic) {
                classTesterTopic.discoverProperties((TopicProxy)obj, retVal);
            } else if (type == ConfigurationObjectType.log) {
                classTesterLog.discoverProperties((LogProxy)obj, retVal);
            }
        }
        
        return retVal;
   }
    
}
