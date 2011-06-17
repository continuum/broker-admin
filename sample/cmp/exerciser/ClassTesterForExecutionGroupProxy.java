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
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.MessageFlowDependency;
import com.ibm.broker.config.proxy.MessageFlowProxy;

/*****************************************************************************
 * <p>Each ExecutionGroup object represents an execution
 * group for a single broker. 
 * <p>
 * <b>NOTE:</b>
 * <p>
 * Most methods in this class tester take a ExecutionGroupProxy
 * parameter. If you wish to gain a handle to such an object
 * in your own code, use something like:
 * <pre>
 * ConfigManagerConnectionParameters cmcp =
 *   MQConfigManagerConnectionParameters("localhost", 1414, "QMGR");
 * ConfigManagerProxy cmp = ConfigManagerProxy.getInstance(cmcp);
 * TopologyProxy t = cmp.getTopology();
 * BrokerProxy b = t.getBrokerByName("myBroker");
 * ExecutionGroupProxy e = b.getExecutionGroupByName("eg1");
 * </pre>
 * 
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ClassTesterForExecutionGroupProxy</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Services to test ExecutionGroupProxy APIs
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
 * @version Samples/ConfigManagerProxy/cmp/exerciser/ClassTesterForExecutionGroupProxy.java, Config.Proxy, S000, S000-L50818.2 1.15
 *****************************************************************************/
public class ClassTesterForExecutionGroupProxy {
    
    /**
     * GUI object to which the tester is linked
     */
    CMPAPIExerciser exerciser;
    
    /**
     * Instantiates a new ClassTesterForExecutionGroupProxy that is
     * linked to the supplied GUI
     * @param exerciser GUI object to which the tester is linked
     */
    ClassTesterForExecutionGroupProxy(CMPAPIExerciser exerciser) {
        this.exerciser=exerciser;
    }
    
    /**
     * Gives a quick test of the commands to change
     * eg properties
     * @param object Selected AdministeredObject
     * @param newName New name of the object
     * @param shortDesc New Short Description
     * @param longDesc New Long Description
     */
    public void testModifyEGProperties(ExecutionGroupProxy object,
                                        String newName,
                                        String shortDesc,
                                        String longDesc) {

        ClassTesterForAdministeredObject.testModifyStandardProperties(exerciser, object, newName, shortDesc, longDesc);
        exerciser.reportActionSubmitted();
    }
    
    /**
     * Gives a quick test of the Configuration Manager Proxy's deployment
     * functionality.
     * @param eg
     * @param barfile
     */
    public void testDeployBAR(ExecutionGroupProxy eg, String barfile) {
        try {
            DeployResult ds = eg.deploy(barfile,
                    ResourcesHandler.getUserSettingBoolean(ResourcesHandler.INCREMENTAL_DEPLOY, true),
                    ResourcesHandler.getUserSettingInt(ResourcesHandler.DEPLOY_WAIT_TIME, 0));
            exerciser.reportDeployResult(ds);
            exerciser.updateStatusLine();
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Gives a quick test of the Configuration Manager Proxy's deployment
     * deletion functionality.
     * @param eg
     * @param objectNamesToDelete deployed objects names to delete
     */
    public void testDeleteDeployed(ExecutionGroupProxy eg, String objectNamesToDelete) {
        try {
            String[] toDelete = objectNamesToDelete.split(";");
            DeployResult ds = eg.deleteDeployedObjectsByName(toDelete,
            		ResourcesHandler.getUserSettingInt(ResourcesHandler.DEPLOY_WAIT_TIME, 0));
            exerciser.reportDeployResult(ds);
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Adds to the supplied Properties table a set of key/value pairs that
     * describe some ExecutionGroupProxy-specific methods that may
     * be invoked on the supplied object, and the returned value from
     * those methods.
     * @param eg A valid ExecutionGroupProxy
     * @param p A valid Properties object
     */
    public void discoverProperties(ExecutionGroupProxy eg, Properties p) {
        
        // ---------- run state ----------
        try {
            p.setProperty("isRunning()", ""+eg.isRunning());
        } catch (ConfigManagerProxyPropertyNotInitializedException e) {
            exerciser.log(e);
        }
        
        StringBuffer key;
        StringBuffer value;
        Enumeration e;
        
        if (exerciser.showAdvanced()) {
            
            // ---------- Display message flows ----------
            key = new StringBuffer("getMessageFlows()");
            value = new StringBuffer();
            e = null;
            try {
                e = eg.getMessageFlows(null);
            } catch (ConfigManagerProxyPropertyNotInitializedException e1) {
                exerciser.log(e1);
            }
            
            if (e == null) {
                value.append(""+e);
            } else {
                int count = 0;
                while (e.hasMoreElements()) {
                    count++;
                    key.append("\n    ["+count+"]");
                    value.append("\n");
                    MessageFlowProxy mf = (MessageFlowProxy)e.nextElement();
                    value.append(CMPAPIExerciser.formatAdminObject(mf));
                }
            }
            p.setProperty(""+key, ""+value);
        }
        
        
        // ---------- Message Flow Dependencies ----------
        // Could also use getMessageFlows(), getMessageSets() or
        // getDeployedObjects() for similar information.
        key = new StringBuffer("getMessageFlowDependencies()");
        value = new StringBuffer();
        
        try {
            e = eg.getMessageFlowDependencies();
            
            if (e == null) {
                value.append(""+e);
            } else {
                int count = 0;
                while (e.hasMoreElements()) {
                    count++;
                    MessageFlowDependency mfd = (MessageFlowDependency)e.nextElement();
                    
                    key.append("\n    ["+count+"] getFullName()");
                    value.append("\n"+mfd.getFullName());
                    if (exerciser.showAdvanced()) {
                        key.append("\n    ["+count+"] getName()");
                        value.append("\n"+mfd.getName());
                        key.append("\n    ["+count+"] getFileExtension()");
                        value.append("\n"+mfd.getFileExtension());
                        key.append("\n    ["+count+"] getDeployTime()");
                        value.append("\n"+CMPAPIExerciser.formatDate(mfd.getDeployTime()));
                        key.append("\n    ["+count+"] getModifyTime()");
                        value.append("\n"+CMPAPIExerciser.formatDate(mfd.getModifyTime()));
                        key.append("\n    ["+count+"] getVersion()");
                        value.append("\n"+mfd.getVersion());
                        key.append("\n    ["+count+"] getBARFileName()");
                        value.append("\n"+mfd.getBARFileName());
                        
                        // ------- Keyword list -------
                        key.append("\n    ["+count+"] getKeywords()");
                        String[] keywords = mfd.getKeywords();
                        if (keywords != null) {
                            value.append("\n");
                            for (int i=0; i<keywords.length; i++) {
                                if (i!=0) {
                                    value.append(", ");
                                }
                                value.append(keywords[i]);
                            }
                        } else {
                            value.append("\n"+keywords);
                        }
                        
                        // ------- Each keyword -------
                        // Only display the first line of each value.
                        if (keywords != null) {
                            for (int i=0; i<keywords.length; i++) {
                                key.append("\n    ["+count+"] getKeywordValue(\""+keywords[i]+"\")");
                                value.append("\n"+CMPAPIExerciser.getFirstLine(mfd.getKeywordValue(keywords[i])));
                            }
                        }
                        
                        if (e.hasMoreElements()) {
                            key.append("\n    ----------");
                            value.append("\n");
                        }
                    }
                }
            }        
        } catch (ConfigManagerProxyPropertyNotInitializedException ex) {
            exerciser.log(ex);
        }
       
        p.setProperty(""+key, ""+value);
    }
    
    
}
