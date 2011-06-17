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

import java.util.Properties;

import cmp.common.ResourcesHandler;

import com.ibm.broker.config.proxy.ConfigManagerProxyException;
import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.DeployResult;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.MessageFlowProxy;

/*****************************************************************************
 * <p>Each MessageFlowProxy object represents an message flow
 * as deployed in a single execution group.
 * <p>
 * <b>NOTE:</b>
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ClassTesterForMessageFlowProxy</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Services to test MessageFlowProxy APIs
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
 * 35097    2004-09-13  HDMPL           v6 Release
 *
 * </pre>
 *
 * @version Samples/ConfigManagerProxy/cmp/exerciser/ClassTesterForMessageFlowProxy.java, Config.Proxy, S000, S000-L50818.2 1.7
 *****************************************************************************/
public class ClassTesterForMessageFlowProxy {
    
    /**
     * GUI object to which the tester is linked
     */
    CMPAPIExerciser exerciser;
    
    /**
     * Instantiates a new ClassTesterForMessageFlowProxy that is
     * linked to the supplied GUI
     * @param exerciser GUI object to which the tester is linked
     */
    ClassTesterForMessageFlowProxy(CMPAPIExerciser exerciser) {
        this.exerciser=exerciser;
    }
    
    /**
     * Attempts to remove the supplied message flow.
     * This is a deploy action that requires a running broker.
     * @param object The flow to be removed.
     */
    public void testDeleteMsgFlow(MessageFlowProxy object) {

        try {
            ExecutionGroupProxy eg = (ExecutionGroupProxy)object.getParent();
            DeployResult dr = eg.deleteDeployedObjectsByName(new String[] {object.getName()},
                    ResourcesHandler.getUserSettingInt(ResourcesHandler.DEPLOY_WAIT_TIME, 0));
            exerciser.reportDeployResult(dr);
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    
    /**
     * Adds to the supplied Properties table a set of key/value pairs that
     * describe some MessageFlowProxy-specific methods that may
     * be invoked on the supplied object, and the returned value from
     * those methods.
     * @param eg A valid MessageFlowProxy
     * @param p A valid Properties object
     */
    public void discoverProperties(MessageFlowProxy mf, Properties p) {
        
        // These methods set may fail with a
        // ConfigManagerProxyPropertyNotInitialisedException, which means
        // that information on the administered object was not supplied by
        // the Configuration Manager before a timeout occurred. If this
        // happens for *one* of these methods it will happen for *all*, so it
        // is acceptable to enclose all of this section in a single
        // try/catch block.
        
        // ---------- Misc properties ----------
        try {
            p.setProperty("isRunning()", ""+mf.isRunning());
            
            if (exerciser.showAdvanced()) {
                p.setProperty("getAdditionalInstances()", ""+mf.getAdditionalInstances());
                p.setProperty("getCommitCount()", ""+mf.getCommitCount());
                p.setProperty("getCommitInterval()", ""+mf.getCommitInterval());
                p.setProperty("getCoordinatedTransaction()", ""+mf.getCoordinatedTransaction());
                p.setProperty("getUserTrace()", ""+mf.getUserTrace());
                p.setProperty("getFileExtension()", ""+mf.getFileExtension());
            }
            p.setProperty("getFullName()", ""+mf.getFullName());
            p.setProperty("getBARFileName()", ""+mf.getBARFileName());
            p.setProperty("getDeployTime()", ""+CMPAPIExerciser.formatDate(mf.getDeployTime()));
            p.setProperty("getModifyTime()", ""+CMPAPIExerciser.formatDate(mf.getModifyTime()));
            p.setProperty("getVersion()", ""+mf.getVersion());
            
            
            // ------- Keyword list -------
            StringBuffer value = new StringBuffer();
            String[] keywords = mf.getKeywords();
            if (keywords != null) {
                for (int i=0; i<keywords.length; i++) {
                    if (i!=0) {
                        value.append(", ");
                    }
                    value.append(keywords[i]);
                }
            } else {
                value.append(""+keywords);
            }
            p.setProperty("getKeywords()", value.toString());
            
            // ------- Each keyword -------
            if (keywords != null) {
                for (int i=0; i<keywords.length; i++) {
                    p.setProperty("getKeywordValue(\""+keywords[i]+"\")", mf.getKeywordValue(keywords[i]));
                }
            }
        } catch (ConfigManagerProxyPropertyNotInitializedException e) {
            exerciser.log(e);
        } 
    }
}
