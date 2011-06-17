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
package cmp;

import java.io.IOException;

import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.ConfigManagerConnectionParameters;
import com.ibm.broker.config.proxy.ConfigManagerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyException;
import com.ibm.broker.config.proxy.DeployResult;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.MQConfigManagerConnectionParameters;
import com.ibm.broker.config.proxy.TopologyProxy;

/*****************************************************************************
 * <P>A simple application to deploy a BAR file.
 *
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>DeployBAR</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Provides example code that shows how to use the
 *     Configuration Manager Proxy API to deploy a BAR file.
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
 * 25103.5  2004-04-22  HDMPL           v6 Release
 *
 * </pre>
 *
 * @version Samples/ConfigManagerProxy/cmp/DeployBAR.java, Config.Proxy, S000, S000-L50818.2 1.8
 *****************************************************************************/

public class DeployBAR {
    
    /**
     * Attempts to deploy a BAR file to
     * the resource whose name is hard-coded within the source. 
     * @param args Not used
     */
    public static void main(String[] args) {
        
        // Modify the values of these variables in order
        // to change the Configuration Manager, broker, execution
        // group and BAR file settings used by this sample.
        // -----------------------------------------------
        String configHostName     = "localhost";
        int    configPort         = 2414;
        String configQmgrName     = "WBRK6_DEFAULT_QUEUE_MANAGER";
        String brokerName         = "WBRK6_DEFAULT_BROKER";
        String executionGroupName = "default";
        String barFileName        = "mybar.bar";
        // -----------------------------------------------
        
        // Instantiate an object that describes the connection
        // characteristics to the Configuration Manager.
        ConfigManagerConnectionParameters cmcp =
            new MQConfigManagerConnectionParameters(configHostName, configPort, configQmgrName);
        ConfigManagerProxy cmp = null;
        
        try {
            
            // Start communication with the Configuration Manager
            System.out.println("Connecting to Configuration Manager running on "+configQmgrName+" at "+configHostName+":"+configPort+"...");
            cmp    = ConfigManagerProxy.getInstance(cmcp);
            
            // Has the Configuration Manager responded to the connection attempt?
            if (!cmp.hasBeenUpdatedByConfigManager(true)) {
                // The application timed out while waiting for a response from the
                // Configuration Manager. When it finally becomes available,
                // hasBeenUpdatedByConfigurationManager()
                // will return true. This application won't wait for that though-
                // it will just exit now.
                System.out.println("Configuration Manager is not responding.");
            } else {
                // Get the list of brokers defined in that CM's domain
                System.out.println("Getting domain information...");
                TopologyProxy topology    = cmp.getTopology();
                
                // Find the broker with the given name
                System.out.println("Discovering broker '"+brokerName+"'...");
                BrokerProxy broker        = topology.getBrokerByName(brokerName);
                
                // If the broker exists, find the execution group with the given name
                if (broker == null) {
                    System.out.println("Broker not found");
                } else {
                    System.out.println("Discovering execution group '"+executionGroupName+"'...");
                    ExecutionGroupProxy eg = broker.getExecutionGroupByName(executionGroupName);
                    
                    // If the execution group exists, deploy to it.
                    if (eg == null) {
                        System.out.println("Execution group not found");
                    } else {
                        
                        // Deploy the BAR file and display the result
                        System.out.println("Deploying "+barFileName+"...");
                        try {
                            DeployResult deployResult = eg.deploy(barFileName, // location of BAR
                                                                  true,        // incremental, i.e. don't empty the execution group first
                                                                  10000);      // wait 10s for broker response
                                                                  
                            System.out.println("Result = "+deployResult.getCompletionCode());

                            // You may like to improve this application by querying
                            // the deployResult for more information, particularly if
                            // deployResult.getCompletionCode() == CompletionCodeType.failure.
                            
                        } catch (IOException ioEx) {
                            // e.g. if BAR file doesn't exist
                            ioEx.printStackTrace();
                        }
                        
                    }
                }
            }
        } catch (ConfigManagerProxyException cmpEx) {
            cmpEx.printStackTrace();
        } finally {
            if (cmp != null) {
                cmp.disconnect();
            }
        }
    }      
}
