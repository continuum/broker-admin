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
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import cmp.common.ResourcesHandler;

import com.ibm.broker.config.proxy.AdministeredObject;
import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyException;
import com.ibm.broker.config.proxy.ConfigManagerProxyLoggedException;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.MQConfigManagerConnectionParameters;
import com.ibm.broker.config.proxy.MessageFlowProxy;

/*****************************************************************************
 * <p>This class contains tests for the Configuration Manager Proxy
 * that didn't really fit into any of the other tester classes.
 * Either the tests are not associated with a particular
 * AdministeredObject type, or they affect multiple types. 
 * 
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ClassTesterForMiscellaneousActions</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Services to test miscellaneous APIs
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
 * @version Samples/ConfigManagerProxy/cmp/exerciser/ClassTesterForMiscellaneousActions.java, Config.Proxy, S000, S000-L50818.2 1.9
 *****************************************************************************/
public class ClassTesterForMiscellaneousActions {
    
    /**
     * GUI object to which the tester is linked
     */
    CMPAPIExerciser exerciser;
    
    /**
     * True if and only if MQ Java client tracing is enabled
     * This value is not persistent.
     */
    boolean mqTraceEnabled = false;
    
    /**
     * True if and only if CMP system tracing is enabled
     * This value is not persistent.
     */
    boolean cmpTraceEnabled = false;

    /**
     * Instantiates a new ClassTesterForMiscellaneousActions that is
     * linked to the supplied GUI
     * @param exerciser GUI object to which the tester is linked
     */
    ClassTesterForMiscellaneousActions(CMPAPIExerciser exerciser) {
        this.exerciser=exerciser;
    }
    
    
    /**
     * Gives a quick test of the Configuration Manager Proxy's start
     * message flows command.
     * @param object The Broker, Execution Group or flow whose flow(s)
     * are to be started.
     */
    public void testStartMsgFlows(AdministeredObject object) {

        try {
            if (object instanceof BrokerProxy) {
                ((BrokerProxy) object).startMessageFlows();
                exerciser.reportActionSubmitted();
            } else if (object instanceof ExecutionGroupProxy) {
                ((ExecutionGroupProxy) object).startMessageFlows();
                exerciser.reportActionSubmitted();
            } else if (object instanceof MessageFlowProxy) {
                ((MessageFlowProxy) object).start();
                exerciser.reportActionSubmitted();
            } else {
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.INVALID_TYPE));
            }
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }

    /**
     * Gives a quick test of the Configuration Manager Proxy's stop
     * message flows command.
     * @param object The Broker or Execution Group whose flows
     * are to be stopped.
     * @param immediate - True if and only if the immediate flag
     * should be used.
     */
    public void testStopMsgFlows(AdministeredObject object, boolean immediate) {

        try {
            if (object instanceof BrokerProxy) {
                ((BrokerProxy) object).stopMessageFlows(immediate);
                exerciser.reportActionSubmitted();
            } else if (object instanceof ExecutionGroupProxy) {
                ((ExecutionGroupProxy) object).stopMessageFlows(immediate);
                exerciser.reportActionSubmitted();
            } else if (object instanceof MessageFlowProxy) {
                ((MessageFlowProxy) object).stop(immediate);
                exerciser.reportActionSubmitted();
            } else {
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.INVALID_TYPE));
            }
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }


    
    /**
     * Gives a quick test to start user trace.
     * @param selectedObject broker, execution group or message flow
     * whose user trace settings are to change.
     */
    public void testStartUserTrace(AdministeredObject selectedObject) {

        try {
            if (selectedObject instanceof BrokerProxy) {
                ((BrokerProxy) selectedObject).setUserTrace(MessageFlowProxy.UserTrace.normal);
                exerciser.reportActionSubmitted();
            } else if (selectedObject instanceof ExecutionGroupProxy) {
                ((ExecutionGroupProxy) selectedObject).setUserTrace(MessageFlowProxy.UserTrace.normal);
                exerciser.reportActionSubmitted();
            } else if (selectedObject instanceof MessageFlowProxy) {
                ((MessageFlowProxy) selectedObject).setUserTrace(MessageFlowProxy.UserTrace.normal);
                exerciser.reportActionSubmitted();
            } else {
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.INVALID_TYPE));
            }
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test to stop user trace.
     * @param selectedObject broker, execution group or message flow
     * whose user trace settings are to change.
     */
    public void testStopUserTrace(AdministeredObject selectedObject) {

        try {
            if (selectedObject instanceof BrokerProxy) {
                ((BrokerProxy) selectedObject).setUserTrace(MessageFlowProxy.UserTrace.none);
                exerciser.reportActionSubmitted();
            } else if (selectedObject instanceof ExecutionGroupProxy) {
                ((ExecutionGroupProxy) selectedObject).setUserTrace(MessageFlowProxy.UserTrace.none);
                exerciser.reportActionSubmitted();
            } else if (selectedObject instanceof MessageFlowProxy) {
                ((MessageFlowProxy) selectedObject).setUserTrace(MessageFlowProxy.UserTrace.none);
                exerciser.reportActionSubmitted();
            } else {
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.INVALID_TYPE));
            }
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Gives a quick test to start debug user trace.
     * @param selectedObject broker, execution group or message flow
     * whose user trace settings are to change.
     */
    public void testStartDebugUserTrace(AdministeredObject selectedObject) {

        try {
            if (selectedObject instanceof BrokerProxy) {
                ((BrokerProxy) selectedObject).setUserTrace(MessageFlowProxy.UserTrace.debug);
                exerciser.reportActionSubmitted();
            } else if (selectedObject instanceof ExecutionGroupProxy) {
                ((ExecutionGroupProxy) selectedObject).setUserTrace(MessageFlowProxy.UserTrace.debug);
                exerciser.reportActionSubmitted();
            } else if (selectedObject instanceof MessageFlowProxy) {
                ((MessageFlowProxy) selectedObject).setUserTrace(MessageFlowProxy.UserTrace.debug);
                exerciser.reportActionSubmitted();
            } else {
                exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.INVALID_TYPE));
            }
        } catch (ConfigManagerProxyException ex) {
            exerciser.log(ex);
        }
    }
    
    /**
     * Starts or stops service tracing of the MQ Classes for Java.
     * If trace was previously enabled, invoking this method stops it.
     * If trace was previosly disabled, invoking this method starts sending
     * level 5 trace to the file specified by the user.
     */
    public void toggleMQJavaTrace() {
        String filename = null;
        mqTraceEnabled = !mqTraceEnabled;
        
        if (mqTraceEnabled) {
            filename = getTraceFilename();
            if (filename == null) {
                mqTraceEnabled = false;
            }
        }
        
        if (mqTraceEnabled) {
            MQConfigManagerConnectionParameters.enableMQJavaClientTracing(filename);
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.MQ_TRACE_STARTED)+" "+filename);
        } else {
            MQConfigManagerConnectionParameters.disableMQJavaClientTracing();
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.MQ_TRACE_STOPPED));
        }
        
        // Make the checkbox reflect the actual value of trace
        exerciser.mqTraceEnabledMenuItem.setState(mqTraceEnabled);
    }
    
    /**
     * Toggles whether the CMP system tracing is enabled
     * This setting is non persistent.
     */
    public void toggleCMPTrace() {
        String filename = null;
        
        cmpTraceEnabled = !cmpTraceEnabled;
        if (cmpTraceEnabled) {
            filename = getTraceFilename();
            if (filename == null) {
                cmpTraceEnabled = false;
            }
        }
            
        if (cmpTraceEnabled) {
            try {
                ConfigManagerProxy.enableConfigManagerProxyTracing(filename);
            } catch (ConfigManagerProxyLoggedException e) {
                cmpTraceEnabled = false;
                exerciser.log(e);
            }
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.CMP_TRACE_STARTED)+" "+filename);
        }
        
        if (!cmpTraceEnabled) {
            ConfigManagerProxy.disableConfigManagerProxyTracing();
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.CMP_TRACE_STOPPED));
        }
        
        // Make the checkbox reflect the actual value of trace
        exerciser.cmpTraceEnabledMenuItem.setState(cmpTraceEnabled);
    }
    
    /**
     * Prompts the user with a dialog box that
     * asks the user for a trace output file
     * @return String Filename of the requested file.
     * If null, the action was cancelled.  
     */
    private String getTraceFilename() {
        String filename = null;
        JFileChooser chooser = new JFileChooser(ResourcesHandler.getNLSResource(ResourcesHandler.SELECT_TRACE_OUTPUT));
        chooser.addChoosableFileFilter(new FileFilter() {
            public boolean accept(File arg0) {
                return ((arg0.getName().endsWith("trc")) || (arg0.isDirectory()));
            }
            public String getDescription() {
                return "*.trc";
            }
        });
        
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        int result = chooser.showDialog(exerciser, null);
        if (result == JFileChooser.APPROVE_OPTION) {
            filename = chooser.getSelectedFile().getAbsolutePath();    
            int lastDot = filename.lastIndexOf(".");
            int lastFileSeparator = filename.lastIndexOf(File.separator);
            
            // Do we need to append the filetype filter?
            if ((lastDot == -1) || (lastDot<lastFileSeparator)) {
                filename = filename + ".trc";
            }
        }
        return filename;
    }
    
    /**
     * Toggles the option to discover all subcomponents of the
     * Administered Object hierarchy. If enabled, the entire tree
     * will be discovered at connection time. If disabled, only
     * immediate descendants will be discovered, which means the
     * user has to select the "Discover Children" option in order
     * to traverse down the tree.
     */
    public void toggleAutoGetChildren() {
        ResourcesHandler.toggleUserSettingBoolean(ResourcesHandler.AUTO_GET_CHILDREN, false);
        ResourcesHandler.saveUserSettings();
    }
    
    /**
     * Clears the console text
     */
    public void clearConsole() {
        exerciser.console.setText(null);
    }
    
    
    /**
     * Toggles whether deployment is incremental or not.
     * <P>
     * An incremental BAR file deploy means that existing
     * message flows and sets will continue to run in the
     * Execution Group after deployment.
     * <P>
     * A non-incremental BAR file deploy means that the
     * execution group will be cleared of all message flows
     * and sets before deployment occurs.
     * <P>
     * An incremental topology or topic tree deploy means
     * that only changes to the topology or topic tree
     * are communicated to brokers.
     * <P>
     * A non-incremental topology or topic tree deploy means
     * that the entire topology or topic tree is sent.
     */
    public void toggleIsIncremental() {
        ResourcesHandler.toggleUserSettingBoolean(ResourcesHandler.INCREMENTAL_DEPLOY, false);
        ResourcesHandler.saveUserSettings();
    }
    
    /**
     * Toggles whether all properties are shown
     * in the properties table.
     * If the setting is disabled, only a subset of
     * available properties are shown.
     */
    public void toggleShowAdvancedProperties() {
        exerciser.setShowAdvancedProperties(!(exerciser.showAdvanced()));
        ResourcesHandler.setUserSetting(ResourcesHandler.SHOW_ADVANCED_PROPERTIES, ""+exerciser.showAdvanced());
        ResourcesHandler.saveUserSettings();
        
        // Refresh the table
        exerciser.setupJTable(exerciser.selectedAdministeredObject);
    }
    
    /**
     * Toggles whether the "connect" action prompts the
     * user for hostname, port, qmgr parameters, or it
     * prompts the user for a *.configmgr file name
     */
    public void toggleConnectUsingPropertiesFile() {
        boolean newValue = ResourcesHandler.toggleUserSettingBoolean(ResourcesHandler.FILE_CONNECTUSINGPROPERTIESFILE, true);
        ResourcesHandler.saveUserSettings();
        exerciser.configureConnectAction(newValue);
    }
    
    
    /**
     * Modify this method to run a test not covered
     * elsewhere in the Exerciser.
     * The user can invoke this test by highlighting the
     * Configuration Manager object and selecting the Customisable
     * Test option.
     * @param param1 First parameter to the method.
     * @param param2 Second parameter to the method.
     */
    public void testCustom(String param1, String param2) {
        // DEFAULT (no test case defined)
        exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.NO_CUSTOM_TEST_DEFINED));
        
        
		// Example test case
        /*
        exerciser.log("This test will get the topology, start a batch, "+
            "create the broker '"+param1+"' with QM '"+param2+
            "', change the long and short descriptions and "+
            "finally submit the changes.");
        try {
			ConfigManagerProxy cmp = exerciser.getConnectedConfigManagerProxyInstance();
	        TopologyProxy t = cmp.getTopology();
	        cmp.beginUpdates();
	        BrokerProxy broker=t.createBroker(param1, param2);
	        broker.setLongDescription("MY LONG DESC");
	        broker.setShortDescription("MY SHORT DESC");
	        cmp.sendUpdates();
        } catch (ConfigManagerProxyException ex) {
            exerciser.log("Exception: "+ex);
        }
        */
        
    }    
}
