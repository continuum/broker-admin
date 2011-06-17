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
package cmp.doc;
import com.ibm.broker.config.proxy.*;

/*****************************************************************************
 * <P>A really basic demonstration of the Configuration Manager Proxy API.
 *
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ConfigManagerRunStateChecker</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Describes whether or not a Configuration Manager is running.
 *     </UL>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Internal Collaborators</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     None
 *   </TD>
 * </TR>
 * </TABLE>
 * <pre>
 *
 * Change Activity:
 * -------- ----------- -------------   ------------------------------------
 * Reason:  Date:       Originator:     Comments:
 * -------- ----------- -------------   ------------------------------------
 * WBIB v6  2004-02-18  HDMPL           Initial Creation
 *
 * </pre>
 *
 * @version Samples/ConfigManagerProxy/cmp/doc/ConfigManagerRunStateChecker.java, Config.Proxy, S000, S000-L50818.2 1.5
 *****************************************************************************/
public class ConfigManagerRunStateChecker {

    /**
     * Main method
     * @param args Not used
     */
    public static void main(String[] args) {
        displayConfigManagerRunState("localhost", 1414, "");
    }

    /**
     * Displays on System.out the runstate of the 
     * Configuration Manager listening on the supplied hostname,
     * port and Queue Manager.
     * @param hostname
     * @param port
     * @param qmgr
     */
    public static void displayConfigManagerRunState(String hostname,
                                                    int port,
                                                    String qmgr) {
        ConfigManagerProxy cmp = null;
        try {
            ConfigManagerConnectionParameters cmcp =
                new MQConfigManagerConnectionParameters(hostname, port, qmgr);
            cmp = ConfigManagerProxy.getInstance(cmcp);
            String configManagerName = cmp.getName();
            
            System.out.println("Configuration Manager "+configManagerName+" is available!");
            cmp.disconnect();
        } catch (ConfigManagerProxyException ex) {
            System.out.println("Configuration Manager is NOT available because "+ex);
        }
    }
}
