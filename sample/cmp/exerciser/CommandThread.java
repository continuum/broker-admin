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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import cmp.common.ResourcesHandler;

import com.ibm.broker.config.proxy.AdministeredObject;

/*****************************************************************************
 * <p>This is a helper class that is used by the CMPAPIExerciser.
 * <P>The CommandThread uses its own thread to invoke the various actions
 * possible from the CMP API Exerciser. It does this because some methods
 * may take some time to complete, and so by using a separate thread interface
 * actions (such as screen updates) can still occur.
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>CommandThread</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Accepting requests to invoking methods in the CMP API Exerciser
 * application that test the Configuration Manager Proxy.
 *     </UL>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Internal Collaborators</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *       <LI><TT>cmp.common.ResourcesHandler</TT>
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
 * 25103.7  2004-08-03  HDMPL           v6 Release
 *
 * </pre>
 *
 * @version Samples/ConfigManagerProxy/cmp/exerciser/CommandThread.java, Config.Proxy, S000, S000-L50818.2 1.9
 *****************************************************************************/
class CommandThread implements Runnable {

    Thread commandThread = null;
    
    /**
     * Each element describes a command that the user has requested.
     * Commands are pulled from the command queue and invoked by the
     * command thread.
     */
    Vector commandQueue;
    
    /**
     * True if and only if the command thread is currently
     * busy processing a command.
     */
    boolean busy;
    
    /**
     * GUI object to which the tester is linked
     */
    CMPAPIExerciser exerciser;

    /**
     * The progress bar dialog - shown whenever a long running
     * operation is being performed.
     */
    private JDialog progressBarDialog;
    
    
    /**
     * Instantiates a new CommandThread.
     * @param exerciser handler to the provider of log functions.
     */
    protected CommandThread(CMPAPIExerciser exerciser) {
        commandQueue = new Vector();
        this.exerciser = exerciser;
        busy = false;
        
        initialiseProgressBarDialog();
    }  

    /**
     * Initialises the progress bar dialog, but does not display it.
     */
    private void initialiseProgressBarDialog() {
        
        int xPadding = 12;
        int yPadding = 12;
        int heightOfComponents = 24;
        int barWidth = 376;
        
        progressBarDialog = new JDialog();
        progressBarDialog.setTitle(ResourcesHandler.getNLSResource(ResourcesHandler.PLEASE_WAIT));
        progressBarDialog.setResizable(false);
        JPanel panel = new JPanel();
        panel.setLayout(new SpringLayout());
        progressBarDialog.setContentPane(panel);
        SpringLayout layout = (SpringLayout) panel.getLayout();
        
        // Position the label
        JLabel lab = new JLabel(ResourcesHandler.getNLSResource(ResourcesHandler.PLEASE_WAIT_VERBOSE));
        panel.add(lab);
        SpringLayout.Constraints c = layout.getConstraints(lab);
        c.setX(Spring.constant(xPadding));
        c.setY(Spring.constant(yPadding));
        
        // Position the progress bar
        JProgressBar jpb = new JProgressBar();
        jpb.setIndeterminate(true);
        panel.add(jpb);
        c = layout.getConstraints(jpb);
        c.setX(Spring.constant(xPadding));
        c.setWidth(Spring.constant(barWidth));
        c.setY(Spring.constant(heightOfComponents + yPadding));
        
        // Size the dialog
        c = layout.getConstraints(panel);
        c.setConstraint(SpringLayout.SOUTH, Spring.constant(heightOfComponents + (yPadding*3)));
        c.setConstraint(SpringLayout.EAST, Spring.constant((xPadding*2) + barWidth));
        progressBarDialog.pack();
        
        
    }

    /**
     * Adds the command with the supplied characteristics to the command queue for processing.
     * @param method Method to be invoked
     * @param testOwner The CMPAPIExerciser member variable that owns the method 
     * @param parameters The parameters to the method
     * @param suppressEntryExitLogMessage If and only if the value is true, the "--->" and "<---"
     * lines, which describe the method being invoked, will not be shown 
     * @param suppressBusyWarning If and only if the value is true, the warning message,
     * usually displayed if the queue is already dealing with an action, will not be shown.  
     */
    protected void enqueueCommand(Method method, Object testOwner, Object[] parameters, boolean suppressEntryExitLogMessage, boolean suppressBusyWarning) {
        
        synchronized (commandQueue) {
            commandQueue.add(new CMPAPIExerciserCommand(method, testOwner, parameters, suppressEntryExitLogMessage));
        }
        
        if ((busy) && (!suppressBusyWarning)) {
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.COMMAND_THREAD_BUSY));
        }
        ensureRunningCommandThread();
    }
    
    
    /**
     * Returns the actual thread associated with this CommandThread,
     * having first created a new one if necessary. 
     */
    private void ensureRunningCommandThread() {
        if (commandThread == null) {
            commandThread = new Thread(this);
            commandThread.setDaemon(true);
            commandThread.setName("CMPExerciserCmd");
        }  
        if (!commandThread.isAlive()) {
            commandThread.start();
        }
    }

    /**
     * Waits for commands to arrive on the command queue and invokes them
     * @see java.lang.Runnable#run()
     */
    public void run() {
        
        // As a daemon thread, this thread will finish
        // when the main (non-daemon) thread does.
        while (true) {
                    
            // If the application isn't finishing but there are
            // no commands to process, sleep for one second.
            if (commandQueue.size() == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
            
            // If there are commands to process, do the next one
            // on the queue.
            if (commandQueue.size() > 0) {
                CMPAPIExerciserCommand c;
                synchronized (commandQueue) {
                    c = (CMPAPIExerciserCommand)commandQueue.remove(0);
                }
                busy = true;
                if (!exerciser.isHeadless()) {
                    exerciser.centerComponentInExerciser(progressBarDialog);
                    progressBarDialog.setVisible(true);
                }
                invokeCommand(c);
                
                if (commandQueue.size() == 0) {
                    busy = false;
                    progressBarDialog.setVisible(false);
                }
            }
         }
    }
    
    /**
     * Issues the command represented by the parameter.
     * @param c
     */
    private void invokeCommand(CMPAPIExerciserCommand c) {
        String command = c.testOwner.getClass().getName()+"."+c.method.getName();
        String inputInfo = command + "(";
        String outputInfo = command;
        
        // Log the input parameters
        if (c.parameters != null) {
            for (int i=0; i<c.parameters.length; i++) {
                if (c.parameters[i] != null) {
                    if (c.parameters[i] instanceof String) {
                        inputInfo = inputInfo.concat("\"" + c.parameters[i] + "\"");
                    } else if (c.parameters[i] instanceof AdministeredObject) {
                        inputInfo = inputInfo.concat(CMPAPIExerciser.formatAdminObject((AdministeredObject)c.parameters[i]));
                    } else {
                        inputInfo = inputInfo.concat(""+c.parameters[i]);
                    }
                } else {
                    inputInfo = inputInfo.concat("null");
                }
                
                if ((i+1) != c.parameters.length) {
                    inputInfo  = inputInfo.concat(", ");
                }
            }
        } 
        inputInfo = inputInfo.concat(")");
        if (!c.suppressEntryExitLogMessage) {
            exerciser.log("");
            exerciser.log("----> "+inputInfo);
        }
        
        
        try {
            // Invoke the test method!
            Object rc = c.method.invoke(c.testOwner, c.parameters);
            if (rc != null) {
                outputInfo = outputInfo.concat(" returned "+rc);
            }
        } catch (InvocationTargetException e1) {
            Throwable e2 = e1.getTargetException();
            outputInfo = outputInfo.concat(" threw "+e2);
            exerciser.log(e2);
        } catch (Throwable ex) {
            exerciser.log("c.method = "+c.method);
            exerciser.log("c.testOwner = "+c.testOwner);
            for (int i=0; i< c.parameters.length; i++)
                exerciser.log("c.parameters["+i+"]= "+c.parameters[i]);
            exerciser.log(ex);
        }
        
        // Log the output
        if (!c.suppressEntryExitLogMessage) {
            exerciser.log("<---- "+outputInfo);
        }
        
    }

    /**
     * Returns true if and only if the command thread is currently processing commands
     * @return
     */
    public boolean isBusy() {
        return busy;
    }
}

/**
 * Small data structure to represent a CMP Exerciser command
 */
class CMPAPIExerciserCommand {
    Method method;
    Object testOwner;
    Object[] parameters;
    boolean suppressEntryExitLogMessage;
    public CMPAPIExerciserCommand(Method method, Object testOwner, Object[] parameters, boolean suppressEntryExitLogMessage) {
        this.method = method;
        this.testOwner = testOwner;
        this.parameters = parameters;
        this.suppressEntryExitLogMessage = suppressEntryExitLogMessage;
    }
}
