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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import cmp.common.ResourcesHandler;

import com.ibm.broker.config.proxy.AdministeredObject;

/*****************************************************************************
 * <P>This class implements a number of interface that receive Swing
 * notifications.
 *
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>GUIEventListener</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Receives notifications from the Swing subsystem and
 *     deals with them accordingly.
 *     </UL>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Internal Collaborators</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *       <LI><TT>cmp.exerciser.CMPAPIExerciser</TT>
 *       <LI><TT>cmp.exerciser.CMPAPIExerciser.CommandInformation</TT>
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
 * @version Samples/ConfigManagerProxy/cmp/exerciser/GUIEventListener.java, Config.Proxy, S000, S000-L50831.3 1.7
 *****************************************************************************/
public class GUIEventListener implements TreeSelectionListener, ActionListener, WindowListener, MouseListener {
    
    
    /**
     * GUI to which this listener is linked
     */
    private CMPAPIExerciser exerciser;
    
    /**
     * Information on the currently selected command
     */
    CommandInformation activeCommandInformation = null;

    /**
     * Initialises a new GUIEventListener
     * @param exerciser
     */
    public GUIEventListener(CMPAPIExerciser exerciser) {
        this.exerciser = exerciser;
    }
    
    /**
     * Called when the mouse is pressed
     * @param e
     */
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    /**
     * Called when the mouse button is released
     * @param e
     */
    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    /**
     * Called when the mouse is clicked
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Called when the mouse enters a component
     * @param e
     */
    public void mouseEntered(MouseEvent e) {
    }
    
    /**
     * Called when the mouse leaves a component
     * @param e
     */
    public void mouseExited(MouseEvent e) {
    }
    
    /**
     * Upon receiving a MouseEvent, determines whether a popup menu
     * should be displayed, and if so, does it.
     * @param e
     */
    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            if (e.getSource() == exerciser.console) {
                JPopupMenu consoleMenu = new JPopupMenu();
                try {
                    exerciser.addMenuItem(consoleMenu, ResourcesHandler.getNLSResource(ResourcesHandler.SAVE_CONSOLE),
                            new CommandInformation(ResourcesHandler.SAVE_CONSOLE, JFileChooser.SAVE_DIALOG, "log", false, exerciser, exerciser.getClass().getMethod("saveConsole", new Class[] { String.class } ), true));
                    exerciser.addMenuItem(consoleMenu, ResourcesHandler.getNLSResource(ResourcesHandler.CLEAR_CONSOLE),
                            new CommandInformation(null, null, null, false, exerciser.classTesterMisc,exerciser.classTesterMisc.getClass().getMethod("clearConsole", null), true));
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                }
                consoleMenu.show(e.getComponent(),
                        e.getX(), e.getY());
            } else {
                if (exerciser.getConnectedConfigManagerProxyInstance() != null) {
                    exerciser.menuForSelectedObject.show(e.getComponent(),
                            e.getX(), e.getY());
                } else {
                    exerciser.connectMenu.show(e.getComponent(),
                            e.getX(), e.getY());
                }
            }

        }
    }
    
    /**
     * Called when a window is closing
     * @param e
     */
    public void windowClosing(WindowEvent e) {
        if (e.getComponent() == exerciser) {
            exerciser.quit();
        } else if (e.getComponent() == exerciser.dataEntryFrame) {
            exerciser.dataEntryFrame.setVisible(false);
        }
    }
    
    /**
     * Called when a window is opened
     * @param e
     */
    public void windowOpened(WindowEvent e) {
    }
    
    /**
     * Called when a window is closed
     * @param e
     */
    public void windowClosed(WindowEvent e) {
    }
    
    /**
     * Called when a window is iconified
     * @param e
     */
    public void windowIconified(WindowEvent e) {
    }
    
    /**
     * Called when a window is deiconified
     * @param e
     */
    public void windowDeiconified(WindowEvent e) {
    }
    
    /**
     * Called when a window is activated
     * @param e
     */
    public void windowActivated(WindowEvent e) {
    }
    
    /**
     * Called when a window is deactivated
     * @param e
     */
    public void windowDeactivated(WindowEvent e) {
    }
    
    
    /**
     * (from TreeSelectionListener)
     * Called when the user selects an item in the tree
     * @param e
     */
    public void valueChanged(TreeSelectionEvent e) {
        Object o = e.getSource();
        if (o instanceof JTree) {
            JTree newSelection = (JTree) o;
            TreePath t = newSelection.getSelectionPath();

            if (t != null) {
                if (t.getLastPathComponent() instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode selectedObject = (DefaultMutableTreeNode)
                    t.getLastPathComponent();

                    // Look up the administered object for this object
                    AdministeredObject props = (AdministeredObject)(exerciser.administeredObjects).get(selectedObject);
                    exerciser.selectAdministeredObject(props);
                }
            }
        }
    }
    
    /**
     * Called when a button is clicked
     * (from ActionListener)
     * @param e
     */
    public void actionPerformed (ActionEvent e) {
        Object source = e.getSource();

        String[] parameters = null;
        
        if (source == exerciser.cancelButton) {
            activeCommandInformation = null;
            exerciser.dataEntryFrame.setVisible(false);
            return;
        } else if (source == exerciser.submitButton) {
            parameters = new String[activeCommandInformation.parametersRequired];
            for (int i=0; i<activeCommandInformation.parametersRequired; i++) {

                if (i<exerciser.tf.length) {
                    parameters[i] = exerciser.tf[i].getText();
                    if (activeCommandInformation.userSettingsKeyNames != null) {
                        String userSettingKey = activeCommandInformation.userSettingsKeyNames[i];
                        ResourcesHandler.setUserSetting(userSettingKey, parameters[i]);
                    }
                }
            }
            
            // Automatically save the user's settings
            ResourcesHandler.saveUserSettings();
            
            exerciser.dataEntryFrame.setVisible(false);
        } else {
            // If we've just selected a menu item (and not hit the
            // submit button) then look up the command information
            // object from the JMenuItem->CommandInformation hashtable. 
            activeCommandInformation = (CommandInformation)exerciser.mappingOfMenuItemsToCommands.get(source);
        }
        
            
        // If no parameters are required then issue the command now.
        // If parameters have been entered in the dataEntryFrame dialog
        // then issue the command now.
        if (activeCommandInformation != null) {
            boolean cancelled = false;
            
            if (activeCommandInformation.showFileDialog) {
                
                JFileChooser chooser = new JFileChooser(activeCommandInformation.fileDialogTitle);
                
                chooser.addChoosableFileFilter(new FileFilter() {
                    public boolean accept(File arg0) {
                        boolean retVal = true;
                        if (activeCommandInformation.fileTypeFilter != null) {
                            retVal = ((arg0.getName().endsWith(activeCommandInformation.fileTypeFilter))
                            	|| (arg0.isDirectory()));
                        }
                        return retVal;
                    }
                    public String getDescription() {
                        if (activeCommandInformation.fileTypeFilter != null) {
                            return "*."+activeCommandInformation.fileTypeFilter;
                        } else {
                            return "*.*";
                        }
                    }});
                
                chooser.setDialogType(activeCommandInformation.fileDialogType);
                
                int result = chooser.showDialog(exerciser, null);
                if (result != JFileChooser.APPROVE_OPTION) {
                    cancelled = true;
                } else {
                    String filename = chooser.getSelectedFile().getAbsolutePath();
                    if (activeCommandInformation.fileTypeFilter != null) {
	                    int lastDot = filename.lastIndexOf(".");
	                    int lastFileSeparator = filename.lastIndexOf(File.separator);
	                    
	                    // Do we need to append the filetype filter?
	                    if ((lastDot == -1) || (lastDot<lastFileSeparator)) {
	                        filename = filename + "."+activeCommandInformation.fileTypeFilter;
	                    }
                    }
                    parameters = new String[] { filename };
                }

            }
            
            if ((parameters == null) && (activeCommandInformation.parametersRequired > 0)) {
                
                String[] defaultParameters = null;
                
                // If the user has default values saved to the settings file, then
                // substitute them in.
                if (activeCommandInformation.userSettingsKeyNames != null) {
                    defaultParameters = new String[activeCommandInformation.userSettingsKeyNames.length];
                    for (int i=0; i<activeCommandInformation.userSettingsKeyNames.length; i++) {
                        String userSavedDefaultValue = ResourcesHandler.getUserSetting(activeCommandInformation.userSettingsKeyNames[i]);
                        if (userSavedDefaultValue != null) {
                            // The user has saved a default value for this field
                            defaultParameters[i] = userSavedDefaultValue;
                        } else if (activeCommandInformation.defaults != null) {
                            // This application supplied some default defaults when
                            // setting up the command
                            defaultParameters[i] = activeCommandInformation.defaults[i];
                        } else {
                            // No default value available - use blank.
                            defaultParameters[i] = "";
                        }
                    }
                } else {
                    defaultParameters = activeCommandInformation.defaults; // may be null
                }
                
                exerciser.getParameters(
	                    e.getActionCommand(),
	                    activeCommandInformation.labels,
	                    defaultParameters,
	                    activeCommandInformation.canBeBatched);
	        } else {
	            if (!cancelled) {
	                // The parameters have been supplied
	                exerciser.issueAction(
	                        activeCommandInformation.classTesterObject,
	                        activeCommandInformation.methodToInvoke,
	                        parameters,
	                        activeCommandInformation.suppressEntryExitLogMessages);
	            }
	        }
        }
    }
}



