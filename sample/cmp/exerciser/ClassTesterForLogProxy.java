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
import com.ibm.broker.config.proxy.LogEntry;
import com.ibm.broker.config.proxy.LogProxy;

/*****************************************************************************
 * <p>The LogProxy object represents the log of broker
 * messages relevant to the current user.
 * <p>
 * <b>NOTE:</b>
 * <p>
 * Most methods in this class tester take a LogProxy
 * parameter. If you wish to gain a handle to such an object
 * in your own code, use something like:
 * <pre>
 * ConfigManagerConnectionParameters cmcp =
 *   MQConfigManagerConnectionParameters("localhost", 1414, "QMGR");
 * ConfigManagerProxy cmp = ConfigManagerProxy.getInstance(cmcp);
 * LogProxy l = cmp.getLog();
 * </pre>
 * 
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ClassTesterForLogProxy</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Services to test LogProxy APIs
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
 * @version Samples/ConfigManagerProxy/cmp/exerciser/ClassTesterForLogProxy.java, Config.Proxy, S000, S000-L50818.2 1.6
 *****************************************************************************/
public class ClassTesterForLogProxy {
    
    /**
     * GUI object to which the tester is linked
     */
    CMPAPIExerciser exerciser;
    
    /**
     * Instantiates a new ClassTesterForLogProxy that is
     * linked to the supplied GUI
     * @param exerciser GUI object to which the tester is linked
     */
    ClassTesterForLogProxy(CMPAPIExerciser exerciser) {
        this.exerciser=exerciser;
    }
    
    /**
     * Gives a quick test of the log display
     * functionality.
     * @param l
     */
    public void testLogDisplay(LogProxy l) {

        try {
            Enumeration e = l.elements();
            if (e.hasMoreElements()) {
                while (e.hasMoreElements()) {
                    LogEntry thisEntry = (LogEntry) e.nextElement();
                    exerciser.log("-----------------------------------");
                    exerciser.log("getMessage() = "+thisEntry.getMessage());
                    exerciser.log("getDetail() = "+thisEntry.getDetail());
                    exerciser.log("getSource() = "+thisEntry.getSource());
                    exerciser.log("getTimestamp() = "+thisEntry.getTimestamp());
                    if (!e.hasMoreElements()) {
                        exerciser.log("-----------------------------------");
                    }
                }
            } else {
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.LOG_EMPTY));
            }

        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Gives a quick test of the log clear
     * functionality.
     * @param l
     */
    public void testLogClear(LogProxy l) {
        try {
            l.clear();
            exerciser.reportActionSubmitted();
        } catch (Exception ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Adds to the supplied Properties table a set of key/value pairs that
     * describe some LogProxy-specific methods that may
     * be invoked on the supplied object, and the returned value from
     * those methods.
     * @param log A valid LogProxy
     * @param p A valid Properties object
     */
    public void discoverProperties(LogProxy log, Properties p) {
        
        // ---------- Misc properties ----------
        // (only one specific to logs)
        try {
            p.setProperty("getSize()", ""+log.getSize());
        } catch (ConfigManagerProxyPropertyNotInitializedException e) {
            exerciser.log(e);
        }
        
        // ---------- Display log entries ----------
        // Could also use getLogEntry( 0 .. log.getSize() )
        Enumeration e = null;
        StringBuffer key = new StringBuffer("elements()");
        StringBuffer value = new StringBuffer();
        
        try {
            e = log.elements();
            if (e == null) {
                value.append(""+e);
            } else {
                int count = 0;
                while (e.hasMoreElements()) {
                    count++;
                    LogEntry l = (LogEntry)e.nextElement();
                    
                    if (exerciser.showAdvanced()) {
                        key.append("\n    ["+count+"] getMessage()");
                        value.append("\n"+l.getMessage());
                    }
                    
                    key.append("\n    ["+count+"] getDetail()");
                    value.append("\n"+CMPAPIExerciser.getFirstLine(l.getDetail()));
                    
                    if (exerciser.showAdvanced()) {
                        key.append("\n    ["+count+"] getMessageNumber()");
                        value.append("\n"+l.getMessageNumber());
                        key.append("\n    ["+count+"] getSource()");
                        value.append("\n"+l.getSource());
                        key.append("\n    ["+count+"] getTimestamp()");
                        value.append("\n"+CMPAPIExerciser.formatDate(l.getTimestamp()));
                        key.append("\n    ["+count+"] isErrorMessage()");
                        value.append("\n"+l.isErrorMessage());
                        key.append("\n    ["+count+"] getInsertsSize()");
                        int numberOfInserts = l.getInsertsSize();
                        value.append("\n"+numberOfInserts);
                    
                        // ------- Inserts -------
                        for (int i=0; i<numberOfInserts; i++) {
                            key.append("\n    ["+count+"] getInsert("+i+")");
                            value.append("\n"+l.getInsert(i));
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
