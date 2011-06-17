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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cmp.common.ResourcesHandler;

import com.ibm.broker.config.proxy.AdministeredObject;
import com.ibm.broker.config.proxy.AdministeredObjectListener;
import com.ibm.broker.config.proxy.AttributeConstants;
import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.CollectiveProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxy;
import com.ibm.broker.config.proxy.ConfigManagerProxyPropertyNotInitializedException;
import com.ibm.broker.config.proxy.ConfigurationObjectType;
import com.ibm.broker.config.proxy.DeployResult;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.LogEntry;
import com.ibm.broker.config.proxy.LogProxy;
import com.ibm.broker.config.proxy.MessageFlowProxy;
import com.ibm.broker.config.proxy.TopicProxy;
import com.ibm.broker.config.proxy.TopicRootProxy;
import com.ibm.broker.config.proxy.TopologyProxy;

/*****************************************************************************
 * <P>An application to demonstrate various features of the
 * Configuration Manager Proxy API.
 *
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ConfigManagerProxyExerciser</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Provides example code that shows how to use the majority
 *     of the APIs exposed by the Configuration Manager Proxy.
 *     <LI>Provides a small portable GUI for controlling a domain
 *     of brokers.
 *     </UL>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Internal Collaborators</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *       <LI><TT>cmp.common.ResourcesHandler</TT>
 *       <LI><TT>cmp.exerciser.*</TT>
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
 * @version Samples/ConfigManagerProxy/cmp/exerciser/CMPAPIExerciser.java, Config.Proxy, S000, S000-L50831.3 1.51
 *****************************************************************************/
public class CMPAPIExerciser extends JFrame {
    
    /**
     * The exerciser can only be connected to a single
     * Configuration Manager at any one time (this is a restriction
     * of this exerciser sample and not the Configuration Manager Proxy).
     * The connected instance is represented by this member variable. 
     */
    ConfigManagerProxy cmp;

    /**
     * The object responsible for performing actions on
     * ConfigManagerProxy instances.
     */
    public ClassTesterForConfigManagerProxy classTesterCMP = null;

    /**
     * The object responsible for performing actions
     * general to all AdministeredObject types.
     */
    public ClassTesterForAdministeredObject classTesterAdministeredObject = null;
    
    /**
     * The object responsible for performing actions on
     * TopologyProxy instances.
     */
    public ClassTesterForTopologyProxy classTesterTopology = null;
    
    /**
     * The object responsible for performing actions on
     * CollectiveProxy instances.
     */
    public ClassTesterForCollectiveProxy classTesterCollective = null;
    
    /**
     * The object responsible for performing actions on
     * BrokerProxy instances.
     */
    public ClassTesterForBrokerProxy classTesterBroker = null;
    
    /**
     * The object responsible for performing actions on
     * ExecutionGroupProxy instances.
     */
    public ClassTesterForExecutionGroupProxy classTesterEG = null;
    
    /**
     * The object responsible for performing actions on
     * MessageFlowProxy instances.
     */
    public ClassTesterForMessageFlowProxy classTesterFlow = null;
    
    /**
     * The object responsible for performing actions on
     * TopicRootProxy instances.
     */
    public ClassTesterForTopicRootProxy classTesterTopicRoot = null;

	/**
     * The object responsible for performing actions on
     * TopicProxy instances.
     */
    public ClassTesterForTopicProxy classTesterTopic = null;
    
    /**
     * The object responsible for performing actions on
     * LogProxy instances.
     */
    public ClassTesterForLogProxy classTesterLog = null;
    
    /**
     * The object responsible for performing miscellaneous
     * other actions on the Configuration Manager Proxy
     */
    public ClassTesterForMiscellaneousActions classTesterMisc = null;
    
    
    /**
     * Contains mappings between AdministeredObject and Nodes in the tree,
     * such that there is a set of Configuration Manager Proxy AdministeredObject
     * keys which map to nodes in the JTree, and vice versa.  
     */
    Hashtable administeredObjects;
    
    /**
     * The currently selected object
     */
    AdministeredObject selectedAdministeredObject = null;
    
    /**
     * Contains mappings of JMenuItem objects to CommandInformation
     * object. This means that when a user clicks on a menu item,
     * the correct command can be invoked.
     */
    Hashtable mappingOfMenuItemsToCommands;
    
    /**
     * The tree
     */
    JTree tree;

    /**
     * Root node of the tree
     */
    DefaultMutableTreeNode root;

    /**
     * The JTable on the right of the frame
     */
    JTable table;
    
    /**
     * Handler to the JTable's model
     */
    DefaultTableModel model;
    
    /**
     * The tree pane's right-click menu for the currently selected object
     */
    JPopupMenu menuForSelectedObject = null;

    /**
     * The tree pane's popup menu if the application is not
     * connected to a Configuration Manager
     */
    JPopupMenu connectMenu = null;

    /**
     * The automation menu
     */
    JMenu automation = null;

    /**
     * The frame in which parameters are entered
     */
    JDialog dataEntryFrame;

    /**
     * The editable text fields in the data entry frame
     */
    JTextField[] tf = null;
    
    /**
     * The console at the bottom of the main window
     */
    JTextArea console = null;
    
    /**
     * The status line at the bottom of the main window
     */
    JLabel statusLine = null;
    
    /**
     * XML document representing the automation script
     * that is currently being written. Will be null
     * if and only if no script is currently being written.
     */
    Document scriptOutputDoc = null;
    
    /**
     * The file name of the script that is currently
     * being written. Will be null if and only if
     * no script is currently being written.
     */
    String scriptFileName = null;
    
    /**
     * True if and only if there is no GUI present
     */
    private boolean headlessMode = true;
    
    /**
     * True if and only if the "property name" column is currently shown.
     */
    boolean propertyNameColumnShown = false;
    
    /**
     * True if and only if the "property value" column is currently shown.
     */
    boolean propertyValueColumnShown = true;
    
    /**
     * True if and only if the "property access method" column is currently shown.
     */
    boolean propertyAccessMethodColumnShown = true;
    
    /**
     * True if and only if a previously submitted action requires a topology
     * deploy in order for the changes to take effect. 
     */
    boolean topologyDeployRequired = false;
    
    /**
     * True if and only if a previously submitted action requires a broker configuration
     * deploy in order for the changes to take effect. 
     */
    boolean brokerDeployRequired = false;
    
    /**
     * True if and only if a previously submitted action requires a topic
     * deploy in order for the changes to take effect. 
     */
    boolean topicDeployRequired = false;
    
    /**
     * The table column containing property names
     */
    TableColumn propertyNameColumn;
    
    /**
     * The table column containing property values
     */
    TableColumn propertyValueColumn;
    
    /**
     * Number of rows to display in the table
     */
    private final static int DEFAULT_NUMBER_OF_ROWS = 40;

    /**
     * Describes the character that separates levels of
     * the object hierarchy in the automation script file format.
     */
    private final static String AUTOMATIONFORMAT_OBJECTPATHSEPARATOR = "/";
    
    /**
     * The object that will be use to receive GUI event-type
     * notifications (mouse clicks, tree changes etc.)
     * AdministeredObjectListener notifications are handled
     * by the ExerciserAdministeredObjectListener.
     */
    private GUIEventListener guiEventListener;

    /**
     * The listener that will receive notifications of
     * administered object changes and deletions, and of
     * completed actions.
     */
    private AdministeredObjectListener exerciserAdministeredObjectListener;

    /**
     * Handle to the submit (or "add to batch") button used in the dialog
     * that asks the user for method parameters.
     */
    JButton submitButton;

    /**
     * Handler to the cancel button in the dialog that asks the
     * user for method parameters.
     */
    JButton cancelButton;

    /**
     * Contains mapping from Strings identifying nodes to nodes.
     * These strings are used in the scripting language for
     * working out what the current selection is
     * e.g. "ConfigManager/PubSubTopology/qm1" == the node
     * representing the broker qm1.
     */
    private Hashtable identifyingStringToNodes;
    
    /**
     * The menu item representing the Connect action.
     */
    JMenuItem connectMenuItem = null;
    
    /**
     * The context sensitive version of the menu item
     * representing the Connect action.
     */
    JMenuItem connectContextSensitiveMenuItem = null;
    
    /**
     * The menu item that toggles MQ Java Client trace
     */
    JCheckBoxMenuItem mqTraceEnabledMenuItem = null;
    
    /**
     * The menu item that toggles CMP system trace
     */
    JCheckBoxMenuItem cmpTraceEnabledMenuItem = null;
    
    /**
     * The thread that is used to handle each API test
     */
    CommandThread commandThread = null;

    /**
     * If and only if the value is true, all properties will
     * be shown in the properties table for each Administered
     * Object.
     */
    private boolean showAdvancedProperties = true;
    
    /**
     * As this is a JFrame, this is a serializable class;
     * therefore, define a default serialVersionUID field.
     */
    private static final long serialVersionUID = 1L;

    // Constants used in the XML script format
    
    /** Sample information - identifier */
    private final static String SCRIPTXML_PROGID_VALUE = "Samples/ConfigManagerProxy/cmp/exerciser/CMPAPIExerciser.java, Config.Proxy, S000, S000-L50831.3 1.51";    
    /** Sample information - version */
    private final static String SCRIPTXML_PROGVER_VALUE = "1.0";
    /** Name of the method to be invoked */
    private static final String SCRIPTXML_NAME = "name";
    /** Owner of the method to be invoked */
    private static final String SCRIPTXML_OWNER = "owner";
    /** Method element */
    private static final String SCRIPTXML_METHOD = "method";
    /** Parameter element */
    private static final String SCRIPTXML_PARAMETER = "parameter";
    /** Type attribute of the parameter */
    private static final String SCRIPTXML_TYPE = "type";
    /** Value attribute of the parameter */
    private static final String SCRIPTXML_VALUE = "value";
    /** Root of the XML document */
    private static final String SCRIPTXML_ROOT = "script";
    /** Attribute describing the name of the program */
    private static final String SCRIPTXML_PROGNAME = "progname";
    /** Attribute describing the internal program information */
    private static final String SCRIPTXML_PROGID = "progid";
    /** Attribute describing the script version of the program */
    private static final String SCRIPTXML_PROGVER = "progver";
    /** The name of the program */
    private static final String SCRIPTXML_PROGNAME_VALUE = "Configuration Manager Proxy API Exerciser";
    
    /**
     * The entry point into the application.
     * @param args[0] optional parameter that describes the filename
     * that contains any saved application settings 
     */
    public static void main(String[] args) {        
        CMPAPIExerciser cmpex = new CMPAPIExerciser();       
        if (args.length > 0) {
            cmpex.startPlayback(args[0]);
            cmpex.quit();
        } else {
            cmpex.go();
        }        
    }
    
    
    /**
     * Initialises the current exerciser instance.
     */
    private void go() {
        // Set up retry characteristics
        classTesterCMP.testRetryCharacteristics(
                ResourcesHandler.getUserSettingLong(ResourcesHandler.CONFIG_RETRY_TIME, 10000),
                ResourcesHandler.getUserSettingLong(ResourcesHandler.DEPLOY_WAIT_TIME, 60000));
        
        // Set the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignore
        }
        
        guiEventListener = new GUIEventListener(this);
        addWindowListener(guiEventListener);
        
        initWindow();
        setSize(800, 600);
        
        Dimension screenSize = getToolkit().getScreenSize();
        Dimension guiSize = getSize();
        if(!((guiSize.width > screenSize.width) || (guiSize.height > screenSize.height))) {
            setLocation(new Point(
                    (screenSize.width - guiSize.width) / 2,
                    (screenSize.height - guiSize.height) / 2));
        }
        setVisible(true);
        headlessMode = false;
        
    }

    /**
     * Returns true if and only if the user has opted to show
     * advanced properties
     * @return boolean The value of the showAdvancedProperties
     */
    protected boolean showAdvanced() {
        return showAdvancedProperties;
    }
    
    /**
     * Controls whether advanced properties should be shown
     * @param showAdvancedProperties True to show advanced properties
     * in addition to the basic set.
     */
    protected void setShowAdvancedProperties(boolean showAdvancedProperties) {
        this.showAdvancedProperties = showAdvancedProperties;
    }
    
    /**
     * Contructs a new frame for the Exerciser application.
     */
    public CMPAPIExerciser() {
        super(ResourcesHandler.getNLSResource(ResourcesHandler.WINDOW_TITLE));        
        
        administeredObjects = new Hashtable();
        mappingOfMenuItemsToCommands = new Hashtable();
        identifyingStringToNodes = new Hashtable();
        
        commandThread = new CommandThread(this);
        exerciserAdministeredObjectListener = new ExerciserAdministeredObjectListener(this);
        classTesterCMP = new ClassTesterForConfigManagerProxy(this);
        classTesterCollective = new ClassTesterForCollectiveProxy(this);
        classTesterTopology = new ClassTesterForTopologyProxy(this, classTesterCollective);
        classTesterBroker = new ClassTesterForBrokerProxy(this);
        classTesterEG = new ClassTesterForExecutionGroupProxy(this);
        classTesterFlow = new ClassTesterForMessageFlowProxy(this);
        classTesterTopic = new ClassTesterForTopicProxy(this);
        classTesterTopicRoot = new ClassTesterForTopicRootProxy(this, classTesterTopic);
        classTesterLog = new ClassTesterForLogProxy(this);
        classTesterMisc = new ClassTesterForMiscellaneousActions(this);
        classTesterAdministeredObject = new ClassTesterForAdministeredObject(this, classTesterCMP,
                classTesterTopology, classTesterCollective, classTesterBroker, classTesterEG,
                classTesterFlow, classTesterTopicRoot, classTesterTopic, classTesterLog);
        
        
        root = getTree(cmp, false);
        tree = new JTree(root);
        initialiseMappingOfIdentifyingStringsToNodes(root);
    }
    
    /**
     * Creates and initialises the member hashtable that contains mappings from
     * object names (e.g. "PubSubTopology") to its clickable node in the GUI tree. 
     * @param root Top level of the GUI tree.
     */
    void initialiseMappingOfIdentifyingStringsToNodes(DefaultMutableTreeNode root) {
        identifyingStringToNodes = new Hashtable();
        if (root != null) {
	        Enumeration e = root.postorderEnumeration();
	        while (e.hasMoreElements()) {
	            DefaultMutableTreeNode n = (DefaultMutableTreeNode) e.nextElement();
	            identifyingStringToNodes.put(getIdentifyingStringFromTreeNode(n), n);
	        }
        }
    }
    
    /**
     * Adds the text string as a new item in the supplied
     * menu, and initialises an action listener for it.
     * @param m Menu to which the item is to be added
     * @param text Text label for the menu item
     * @param ci Object describing the command to invoke
     * @return JMenuItem the Menu item just created
     */
    JMenuItem addMenuItem(JPopupMenu m, String text, CommandInformation ci) {
    	// Add "..." to the end of the text if parameters are required
        // or if a file chooser is to be displayed, 
        // AND the dots aren't already in the resource bundle
        if (text.indexOf("...") == -1) {
	        if (ci != null) {
	            if (ci.labels != null) {
	                if (ci.labels.length>0) {
	                    text = text+"...";
	                }
	            } else if (ci.showFileDialog == true) {
	                text = text+"...";
	            }
	        }
    	}
        JMenuItem mi = new JMenuItem(text);
        mi.addActionListener(guiEventListener);
        m.add(mi);
        if (ci != null) {
            mappingOfMenuItemsToCommands.put(mi, ci);
        }
        return mi;
    }

    /**
     * Adds the text string as a new item in the supplied
     * menu, and initialises an action listener for it.
     * @param m Menu to which the item is to be added
     * @param text Text label for the menu item
     * @param mnemonic Keyboard Shortcut for the command
     * @param ci Object describing the command to invoke
     * @return JMenuItem The created menu item
     */
    private JMenuItem addMenuItem(JMenu m, String text, int mnemonic, CommandInformation ci) {
        return addMenuItem(m, text, mnemonic, -1, -1, ci);
    }

    /**
     * Adds a checkbox as a new item in the supplied
     * menu, and initialises an action listener for it.
     * @param m Menu to which the item is to be added
     * @param text Text label for the menu item
     * @param mnemonic Keyboard Shortcut for the command
     * @param defaultValue True iff the checkbox is to
     * be enabled by default
     * @param ci Object describing the command to invoke
     * */
    private JCheckBoxMenuItem addCheckBoxMenuItem(JMenu m, String text, int mnemonic, boolean defaultValue, CommandInformation ci) {
        JCheckBoxMenuItem cb = new JCheckBoxMenuItem(text, defaultValue);
        cb.setMnemonic(mnemonic);
        cb.addActionListener(guiEventListener);
        m.add(cb);
        if (ci != null) {
            mappingOfMenuItemsToCommands.put(cb, ci);
        }
        return cb;
    }

    /**
     * Adds the text string as a new item in the supplied
     * menu, and initialises an action listener for it.
     * @param m Menu to which the item is to be added
     * @param text Text label for the menu item
     * @param mnemonic Keyboard Shortcut for the command
     * @param keystroke Keyboard shortcut for the command
     * @param keystrokeMask Keyboard shortcut modifier for the command
     * be enabled by default
     * @param ci Object describing the command to invoke
     * @return JMenuItem The created menu item
     */
    private JMenuItem addMenuItem(JMenu m, String text, int mnemonic, int keystroke, int keystrokeMask, CommandInformation ci) {

        JMenuItem mi;

        // Add "..." to the end of the text if parameters are required
        // or if a file chooser is to be displayed, 
        // AND the dots aren't already in the resource bundle
        if (text.indexOf("...") == -1) {
	        if (ci != null) {
	            if (ci.labels != null) {
	                if (ci.labels.length>0) {
	                    text = text+"...";
	                }
	            } else if (ci.showFileDialog == true) {
	                text = text+"...";
	            }
	        }
    	}
        if (mnemonic != -1) {
            mi = new JMenuItem(text, mnemonic);
        } else {
            mi = new JMenuItem(text);
        }

        if (keystroke != -1) {
            mi.setAccelerator(KeyStroke.getKeyStroke(keystroke,keystrokeMask));
        }
        mi.addActionListener(guiEventListener);
        m.add(mi);
        
        if (ci != null) {
            mappingOfMenuItemsToCommands.put(mi, ci);
        }
        return mi;
    }
    
    /**
     * Works out the object hierarchy in terms of proxy objects
     * @param o AdministeredObject to get the tree for.
     * @param getImmediateChildrenOnly If true, only the administered object's
     * immediate children will be got. If false, either none or all descendants
     * will be got, depending on the value of autoGetChildren.
     * @return DefaultMutableTreeNode node describing the tree
     */
    DefaultMutableTreeNode getTree(AdministeredObject o, boolean getImmediateChildrenOnly) {

        DefaultMutableTreeNode parent;
        if (o != null) {

            boolean needToRegisterListener = false;
            parent = (DefaultMutableTreeNode) administeredObjects.get(o);
            String name="?";

            // Is it worthwhile getting the information?
            if ((!o.hasBeenUpdatedByConfigManager(true))
                && (o.hasBeenRestrictedByConfigManager())) {
                
                // If the object has been restricted (i.e. the current
                // user does not have at least 'view' authority), then
                // don't display it as a node in the tree.
                parent = null;
            } else {

                name = o.toString();

                if (parent == null) {
                    // We'd never found out about this object before.
                    parent = new DefaultMutableTreeNode(name);
                    administeredObjects.put(o,parent);  // for when the action() method is called
                    administeredObjects.put(parent,o);  // for when the selection changes
                    //o.registerListener(this, immediateNotification);
                    needToRegisterListener = true;
                } else {
                    // Modify the existing object
                    parent.setUserObject(name);
                }

                // If we automatically get all children, then recurse always.
                // If we're getting immediate children only, then recurse this time.
                boolean needToRecurse = true;
                boolean autoGetChildren = ResourcesHandler.getUserSettingBoolean(ResourcesHandler.AUTO_GET_CHILDREN, true);
                if ((!autoGetChildren) && (!getImmediateChildrenOnly)) {
                    needToRecurse = false;
                }

                if (needToRecurse) {
                    // The subcomponent may have children, so recurse
                    // into getTree() to find them all.
                    try {
                        parent.removeAllChildren();
                        Enumeration e = o.getManagedSubcomponents(null);

                        while (e.hasMoreElements()) {
                            AdministeredObject child = (AdministeredObject) e.nextElement();
                            DefaultMutableTreeNode childNode = null;

                            // However: We don't gather collectives' children
                            // because collectives have broker children which are
                            // also children of the topology... And because each
                            // AdministeredObject can only appear once in the tree
                            // hierarchy (because of the administeredObjects hashtable
                            // which is modified a few lines above).
                            if (o instanceof CollectiveProxy) {
                                childNode = new DefaultMutableTreeNode("("+child.getName()+")");
                            } else {
                                childNode = getTree(child, false);
                            }
                            if (childNode != null) {
                                parent.add(childNode);
                            }


                        }
                    } catch (ConfigManagerProxyPropertyNotInitializedException ex2) {
                        // Comms problems with the Configuration Manager, so Indicate that
                        // the object's information is not available.
                        log(ex2);
                        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode("...?");
                        parent.add(childNode);
                        
                    }
                } else {
                    // Do nothing
                }

                if (needToRegisterListener) {
                    registerListener(o);
                }
            }
	    } else {
            // Administered object is null (we probably aren't connected
            // to a Configuration Manager)
            parent = new DefaultMutableTreeNode(ResourcesHandler.getNLSResource(ResourcesHandler.NOT_CONNECTED));
        }
        return parent;
    }

    /**
     * Returns a handle to the exerciser's ConfigManagerProxy instance
     * @return ConfigManagerProxy
     */
	public ConfigManagerProxy getConnectedConfigManagerProxyInstance() {
	    return cmp;
	}

    /**
     * If expand is true, expands all nodes in the tree.
     * Otherwise, collapses all nodes in the tree.
     * @param tree
     * @param expand true to expand, false to collapse
     */ 
    private void expandAll(JTree tree, boolean expand) {
        // Traverse tree from root
        if (root != null) {
            expandAll(tree, new TreePath(root), expand);
        }
    }
    
    /**
     * Expands or collapses the supplied tree, starting from the
     * supplied parent
     * @param tree
     * @param parent
     * @param expand true to expand, false to collapse
     */
    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
            System.out.println("c "+parent);
        }
    }

    /**
     * Displays the complete set of properties for the supplied object in the GUI's table 
     * @param selected The object whose details are to
     * be displayed.
     */
    public void setupJTable(AdministeredObject selected) {

        // Remove the old entries in the table
        int rows = model.getRowCount();
        for (int i=0; i<rows; i++) {
            model.removeRow(0);
        }
        
        // Set up the 'method name' column heading
        TableColumnModel tcm = table.getColumnModel();
        TableColumn tc = tcm.getColumn(0);
        String objectClassName = "";
        if (selected != null) {
            objectClassName = selected.getClass().getName();
            int lastDot = objectClassName.lastIndexOf(".");
            if (lastDot > -1) {
                objectClassName = objectClassName.substring(lastDot+1);
            }
        }
        tc.setHeaderValue(ResourcesHandler.getNLSResource(ResourcesHandler.PROPERTY_NAME, new String[] {objectClassName}));
        table.getTableHeader().resizeAndRepaint();
        
        // Discover the new set of properties from the object
        Properties p = classTesterAdministeredObject.discoverProperties(selected);
        
        // Sort the returned list (by key name)
        int size = p.size();
        String[] listOfKeys = new String[size];
        Enumeration e = p.keys();
        int i=0;
        while (e.hasMoreElements()) {
            listOfKeys[i++] = (String) e.nextElement();
        }
        java.util.Arrays.sort(listOfKeys);
        int keyBeingAdded = 0;
        
        while (keyBeingAdded < size) {
            String key = listOfKeys[keyBeingAdded];
            String value = p.getProperty(key);
            
            // Split each key and value so that the newline
            // character '\n' causes the next row to be used.
            String[] multiLineKey = key.split("\n");
            String[] multiLineValue = value.split("\n");
            
            // Add the rows for this key/value pair to the table
            for (i=0; i<Math.max(multiLineKey.length, multiLineValue.length); i++) {
                
                String thisRowsKey = "";
                String thisRowsValue = "";
                
                if (multiLineKey.length > i) {
                    thisRowsKey = multiLineKey[i];
                }
                if (multiLineValue.length > i) {
                    thisRowsValue = multiLineValue[i];
                }
                
                model.addRow( new String[] { thisRowsKey, thisRowsValue });
            }
            keyBeingAdded++;
        }

        while (model.getRowCount() < DEFAULT_NUMBER_OF_ROWS) {
            model.addRow( new String[] { "", "" } );
        }
    }

    

    /**
     * Causes the supplied AdministeredObject to be selected
     * in the GUI.
     * @param selectedObject 
     */
    void selectAdministeredObject(AdministeredObject selectedObject) {
        if (!headlessMode) {
	        setupJTable(selectedObject);
	        setupSelectedMenu(selectedObject);
        }
        selectedAdministeredObject = selectedObject;
    }

    

    /**
     * Sets up the "Selected" menu context for the supplied object
     * @param o
     */
    void setupSelectedMenu(AdministeredObject o) {

        try {       
	        menuForSelectedObject.removeAll();
	        if (o != null) {
	            ConfigurationObjectType type =
	                ConfigurationObjectType.getConfigurationObjectType(o.getType());
	
	            addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.ADMINISTEREDOBJECT_REFRESH),
	                    new CommandInformation(
	                            null, null, null, false,
	                            classTesterAdministeredObject,
	                            this.classTesterAdministeredObject.getClass().getMethod("testRefresh", new Class[] {AdministeredObject.class})));
	            
	            addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.ADMINISTEREDOBJECT_GETCHILDREN),
	                    new CommandInformation(
	    	                    null, null, null, false,
	    	                    this, this.getClass().getMethod("discoverImmediateChildren", new Class[] {AdministeredObject.class})));
                
                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.ADMINISTEREDOBJECT_RAWPROPERTIES),
                        new CommandInformation(
                                null, null, null, false,
                                classTesterAdministeredObject,
                                this.classTesterAdministeredObject.getClass().getMethod("testShowRawPropertyTable", new Class[] {AdministeredObject.class})));
                
	            menuForSelectedObject.addSeparator();
	
	            // The addMenuItem() method may look rather complicated...
	            // However, by doing this we can configure *all in one place*
	            // all information associated with the action; this includes the menu name,
	            // any required parameters, default values AND the name of the test
	            // method to invoke.
	            //
	            // The format used by most options is:
	            // addMenuItem( <Menu to which the item should be added>,
	            //              <String containing the menu item text>,
	            //              new CommandInformation(
	            //                  <string array, each element containing a label for a required parameter>,
	            //                  <string array, each element containing the key name to use if the user entered value is to you want to store the user entered value in the settings file>
	            //                  <string array, each element containing the default value for a required parameter>,
	            //                  <boolean describing whether the command can be added to a batch (cmp.beginUpdates())>,
	            //                  <the object whose method will be invoked>,
	            //                  <the Method to be invoked>));
	            //
	            // The first parameter to "the method to be invoked" may be an AdministeredObject,
	            // in which case the currently selected object (i.e. in the tree) will be passed at invocation time.
	            // All other parameters to the method are taken from the user input fields
	            // and may be Strings, ints or booleans only.
	            //  
	            // The CommandInformation constructor is slightly different for methods that use dialog boxes to input parameters 
	            if (type == ConfigurationObjectType.configmanager) {
	            	// First, set up the items for the Configuration Manager context sensitive menu
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.CM_GETSUBSCRIPTIONS),
	                        new CommandInformation(
	                                new String[] {
	                                        ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_SPEC),
	                                        ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_SPEC),
	                                        ResourcesHandler.getNLSResource(ResourcesHandler.USER_SPEC),
	                                        ResourcesHandler.getNLSResource(ResourcesHandler.SUBS_POINTS),
	                                        ResourcesHandler.getNLSResource(ResourcesHandler.START_DATE),
	                                        ResourcesHandler.getNLSResource(ResourcesHandler.END_DATE),
	                                        ResourcesHandler.getNLSResource(ResourcesHandler.DELETE_MATCHING_SUBS)},
                                    new String[] {
                                            ResourcesHandler.TOPIC_SPEC,
                                            ResourcesHandler.BROKER_SPEC,
                                            ResourcesHandler.USER_SPEC,
                                            ResourcesHandler.SUBS_POINTS,
                                            ResourcesHandler.START_DATE,
                                            ResourcesHandler.END_DATE,
                                            ResourcesHandler.DELETE_MATCHING_SUBS},
                                    new String[] { "", "", "", "", "", "", "n" },
		    	                    true,
		    	                    classTesterCMP,
		    	                    this.classTesterCMP.getClass().getMethod("testGetSubscriptions", new Class[] {ConfigManagerProxy.class, String.class, String.class, String.class, String.class, String.class, String.class, Boolean.TYPE})));
                    menuForSelectedObject.addSeparator();
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.GRANT_ACCESS_SUBS),
                            new CommandInformation(
                                    new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.ACL_NAME),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_TYPE),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_DOMAIN_GRANT),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_PERMISSION)},
                                    new String[] { ResourcesHandler.ACL_NAME,
                                                   ResourcesHandler.ACL_TYPE,
                                                   ResourcesHandler.ACL_DOMAIN_GRANT,
                                                   ResourcesHandler.ACL_PERMISSION},
                                    new String[] { "", "USER", "", "FULL" },
                                    true,
                                    classTesterCMP,
                                    this.classTesterCMP.getClass().getMethod("testGrantSubscriptionsAccess", new Class[] {String.class, String.class, String.class, String.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.REMOVE_ACCESS_SUBS),
                            new CommandInformation(
                                    new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.ACL_NAME),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_TYPE),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_DOMAIN_REMOVE)},
                                    new String[] { ResourcesHandler.ACL_NAME,
                                                   ResourcesHandler.ACL_TYPE,
                                                   ResourcesHandler.ACL_DOMAIN_REMOVE},
                                    new String[] { "", "USER", "" },
                                    true,
                                    classTesterCMP,
                                    this.classTesterCMP.getClass().getMethod("testRemoveSubscriptionsAccess", new Class[] {String.class, String.class, String.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.SHOW_SUBS_ACCESS),
                            new CommandInformation(
                                    null,
                                    null,
                                    null,
                                    true,
                                    classTesterCMP,
                                    this.classTesterCMP.getClass().getMethod("testShowSubscriptionsAccess", new Class[] {} )));
                    menuForSelectedObject.addSeparator();
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.GRANT_ACCESS_CM),
                            new CommandInformation(
                                    new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.ACL_NAME),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_TYPE),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_DOMAIN_GRANT),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_PERMISSION)},
                                    new String[] { ResourcesHandler.ACL_NAME,
                                                    ResourcesHandler.ACL_TYPE,
                                                    ResourcesHandler.ACL_DOMAIN_GRANT,
                                                    ResourcesHandler.ACL_PERMISSION},
                                    new String[] { "", "USER", "", "FULL" },
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testGrantAccess", new Class[] {AdministeredObject.class, String.class, String.class, String.class, String.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.REMOVE_ACCESS_CM),
                            new CommandInformation(
                                    new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.ACL_NAME),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_TYPE),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_DOMAIN_REMOVE)},
                                    new String[] { ResourcesHandler.ACL_NAME,
                                                   ResourcesHandler.ACL_TYPE,
                                                   ResourcesHandler.ACL_DOMAIN_REMOVE},
                                    new String[] { "", "USER", "" },
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testRemoveAccess", new Class[] {AdministeredObject.class, String.class, String.class, String.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.SHOW_ACCESS),
                            new CommandInformation(
                                    null,
                                    null,
                                    null,
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testShowAccess", new Class[] {AdministeredObject.class} )));
                    menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.CM_CANCEL_DEPLOY),
	                        new CommandInformation(
	                                null,
                                    null,
                                    null,
		    	                    true,
		    	                    classTesterCMP,
		    	                    this.classTesterCMP.getClass().getMethod("testCancelDeploy", new Class[] {ConfigManagerProxy.class})));
	                menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.CM_STARTSYSTRACE),
	                        new CommandInformation(
                            null,
                            null,
                            null,
    	                    true,
    	                    classTesterCMP,
    	                    this.classTesterCMP.getClass().getMethod("testConfigurationManagerTraceStart", new Class[] {ConfigManagerProxy.class})));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.CM_DEBUGSYSTRACE),
	                        new CommandInformation(
                            null,
                            null,
                            null,
    	                    true,
    	                    classTesterCMP,
    	                    this.classTesterCMP.getClass().getMethod("testConfigurationManagerDebugTraceStart", new Class[] {ConfigManagerProxy.class})));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.CM_STOPSYSTRACE),
	                        new CommandInformation(
                            null,
                            null,
                            null,
    	                    true,
    	                    classTesterCMP,
    	                    this.classTesterCMP.getClass().getMethod("testConfigurationManagerTraceStop", new Class[] {ConfigManagerProxy.class})));
	                menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.CM_CUSTOM),
	                        new CommandInformation(
	        	                    new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.PARAMETER_1),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.PARAMETER_2)},
	        	                    new String[] {
	        	                    ResourcesHandler.PARAMETER_1,
	        	                    ResourcesHandler.PARAMETER_2},
	        	                    null,
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testCustom", new Class[] {String.class, String.class})));
	                menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.FILE_DISCONNECT),
	                        new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterCMP,
	        	                    this.classTesterCMP.getClass().getMethod("testDisconnect", null)));
	                
	            } else if (type == ConfigurationObjectType.topology) {
	            
	            	// Set up the Pub/Sub Topology Context Sensitive menu. 
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPOLOGY_DEPLOY),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterTopology,
	        	                    classTesterTopology.getClass().getMethod("testDeployTopologyConfiguration", new Class[] {TopologyProxy.class} )));
	                menuForSelectedObject.addSeparator();
                    
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPOLOGY_CREATEBROKER),
	                		new CommandInformation(
	        	                    new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.NEW_BROKER_NAME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.NEW_BROKER_QMGR),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.CREATE_DEFAULT_EG)},
	        	                    new String[] {
	        	                    ResourcesHandler.NEW_BROKER_NAME,
	        	                    ResourcesHandler.NEW_BROKER_QMGR,
	        	                    ResourcesHandler.CREATE_DEFAULT_EG},
	        	                    new String[] { "BROKER", "", "y" },
	        	                    true,
	        	                    classTesterTopology,
	        	                    classTesterTopology.getClass().getMethod("testCreateBroker", new Class[] {TopologyProxy.class, String.class, String.class, Boolean.TYPE})));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPOLOGY_CREATECOLLECTIVE),
	                		new CommandInformation(
	        	                    new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.NEW_COLLECTIVE_NAME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_LIST_ADD)},
	        	                    new String[] {
	        	                    ResourcesHandler.NEW_COLLECTIVE_NAME,
	        	                    ResourcesHandler.BROKER_LIST_ADD},
	        	                    null,
	        	                    true,
	        	                    classTesterTopology,
	        	                    classTesterTopology.getClass().getMethod("testCreateCollective", new Class[] {TopologyProxy.class, String.class, String.class})));
                    menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPOLOGY_CREATECONNECTION),
	                		new CommandInformation(
	        	                    new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.SOURCE_BROKER_NAME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.TARGET_BROKER_NAME)},
	        	                    new String[] {
	        	                    ResourcesHandler.getUserSetting(ResourcesHandler.SOURCE_BROKER_NAME, ""),
	        	                    ResourcesHandler.getUserSetting(ResourcesHandler.TARGET_BROKER_NAME, "")},
	        	                    null,
	        	                    true,
	        	                    classTesterTopology,
	        	                    classTesterTopology.getClass().getMethod("testCreateConnection", new Class[] {TopologyProxy.class, String.class, String.class})));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPOLOGY_DELETECONNECTION),
	                		new CommandInformation(
	        	                    new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.SOURCE_BROKER_NAME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.TARGET_BROKER_NAME)},
	        	                    new String[] {
	        	                    ResourcesHandler.SOURCE_BROKER_NAME,
	        	                    ResourcesHandler.TARGET_BROKER_NAME},
	        	                    null,
	        	                    true,
	        	                    classTesterTopology,
	        	                    classTesterTopology.getClass().getMethod("testDeleteConnection", new Class[] {TopologyProxy.class, String.class, String.class})));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPOLOGY_LISTCONNECTIONS),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterTopology,
	        	                    classTesterTopology.getClass().getMethod("testListConnections", null )));
                    menuForSelectedObject.addSeparator();
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.GRANT_ACCESS),
                            new CommandInformation(
                                    new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.ACL_NAME),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_TYPE),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_DOMAIN_GRANT),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_PERMISSION)},
                                    new String[] { ResourcesHandler.ACL_NAME,
                                                    ResourcesHandler.ACL_TYPE,
                                                    ResourcesHandler.ACL_DOMAIN_GRANT,
                                                    ResourcesHandler.ACL_PERMISSION},
                                    new String[] { "", "USER", "", "FULL" },
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testGrantAccess", new Class[] {AdministeredObject.class, String.class, String.class, String.class, String.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.REMOVE_ACCESS),
                            new CommandInformation(
                                    new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.ACL_NAME),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_TYPE),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_DOMAIN_REMOVE)},
                                    new String[] { ResourcesHandler.ACL_NAME,
                                                   ResourcesHandler.ACL_TYPE,
                                                   ResourcesHandler.ACL_DOMAIN_REMOVE},
                                    new String[] { "", "USER", "" },
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testRemoveAccess", new Class[] {AdministeredObject.class, String.class, String.class, String.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.SHOW_ACCESS),
                            new CommandInformation(
                                    null,
                                    null,
                                    null,
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testShowAccess", new Class[] {AdministeredObject.class} )));
                    menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.REMOVE_DELETED_BROKER),
	                		new CommandInformation(
	        	                    new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.DELETED_BROKER_TO_REMOVE_NAME_OR_UUID) },
	        	                    new String[] { "" },
	        	                    null,
	        	                    true,
	        	                    classTesterTopology,
	        	                    classTesterTopology.getClass().getMethod("testRemoveDeletedBroker", new Class[] {TopologyProxy.class, String.class} )));
                    menuForSelectedObject.addSeparator();
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPOLOGY_PROPERTIES),
                            new CommandInformation(
                                    new String[] {
                                    ResourcesHandler.getNLSResource(ResourcesHandler.TOPOLOGY_NAME),
                                    ResourcesHandler.getNLSResource(ResourcesHandler.TOPOLOGY_SHORT_DESC),
                                    ResourcesHandler.getNLSResource(ResourcesHandler.TOPOLOGY_LONG_DESC)},
                                    new String[] {
                                    ResourcesHandler.TOPOLOGY_NAME,
                                    ResourcesHandler.TOPOLOGY_SHORT_DESC,
                                    ResourcesHandler.TOPOLOGY_LONG_DESC},
                                    null,
                                    true,
                                    classTesterTopology,
                                    classTesterTopology.getClass().getMethod("testModifyTopologyProperties", new Class[] {TopologyProxy.class, String.class, String.class, String.class})));
	            } else if (type == ConfigurationObjectType.collective) {
	            
	            	// Context-sensitive items for collectives
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.COLLECTIVE_ADDBROKERS),
	                		new CommandInformation(
	        	                    new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_LIST_ADD)},
	        	                    new String[] {
	        	                    ResourcesHandler.BROKER_LIST_ADD},
	        	                    new String[] { "B1;B2" },
	        	                    true,
	        	                    classTesterCollective,
	        	                    classTesterCollective.getClass().getMethod("testAddToCollective", new Class[] {CollectiveProxy.class, String.class})));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.COLLECTIVE_REMOVEBROKERS),
	                		new CommandInformation(
	        	                    new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_LIST_REMOVE)},
	        	                    new String[] {
	        	                    ResourcesHandler.BROKER_LIST_REMOVE},
	        	                    null,
	        	                    true,
	        	                    classTesterCollective,
	        	                    classTesterCollective.getClass().getMethod("testRemoveFromCollective", new Class[] {CollectiveProxy.class, String.class})));
	                menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.COLLECTIVE_DELETE),
	                		new CommandInformation(
	        	                    null,
	        	                    null,
	        	                    null,
	        	                    true,
	        	                    classTesterTopology,
	        	                    classTesterTopology.getClass().getMethod("testDeleteCollective", new Class[] {CollectiveProxy.class})));
	                menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.COLLECTIVE_PROPERTIES),
	                		new CommandInformation(
	        	                    new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.COLLECTIVE_NAME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.COLLECTIVE_SHORT_DESC),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.COLLECTIVE_LONG_DESC)},
	        	                    new String[] {
	        	                    ResourcesHandler.COLLECTIVE_NAME,
	        	                    ResourcesHandler.COLLECTIVE_SHORT_DESC,
	        	                    ResourcesHandler.COLLECTIVE_LONG_DESC},
	        	                    null,
	        	                    true,
	        	                    classTesterCollective,
	        	                    classTesterCollective.getClass().getMethod("testModifyCollectiveProperties", new Class[] {CollectiveProxy.class, String.class, String.class, String.class})));
	            } else if (type == ConfigurationObjectType.broker) {
	            
	            	// Context sensitive properties for the broker
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_CREATEEG),
	            			new CommandInformation(
	        	                    new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.NEW_EG_NAME)},
	        	                    new String[] {
	        	                    ResourcesHandler.NEW_EG_NAME },
	        	                    new String[] {
                                    ResourcesHandler.getUserSetting(ResourcesHandler.NEW_EG_NAME, "eg1")},
                                    true,
	        	                    classTesterBroker,
	        	                    classTesterBroker.getClass().getMethod("testCreateEG", new Class[] {BrokerProxy.class, String.class})));
	            	menuForSelectedObject.addSeparator();
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_STARTFLOWS),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStartMsgFlows", new Class[] {AdministeredObject.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_STOPFLOWS),
	                		new CommandInformation(
	                                new String[] {ResourcesHandler.getNLSResource(ResourcesHandler.IMMEDIATE_STOP)},
	                                new String[] {ResourcesHandler.IMMEDIATE_STOP},
	        	                    new String[] {"n"},
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStopMsgFlows", new Class[] {AdministeredObject.class, Boolean.TYPE} )));
	                menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_STARTUSERTRACE),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStartUserTrace", new Class[] {AdministeredObject.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_DEBUGUSERTRACE),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStartDebugUserTrace", new Class[] {AdministeredObject.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_STOPUSERTRACE),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStopUserTrace", new Class[] {AdministeredObject.class} )));
	                menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_LISTCONNECTIONS),
	                		new CommandInformation(
	                                null, 
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterBroker,
	        	                    classTesterBroker.getClass().getMethod("testListConnections", new Class[] {BrokerProxy.class} )));
	                menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_DELETEALLEGSANDDEPLOY),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterBroker,
	        	                    classTesterBroker.getClass().getMethod("testDeleteAllEGsBrokerConfiguration", new Class[] {BrokerProxy.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_DELETE),
	                		new CommandInformation(
	        	                    null,
	        	                    null,
	        	                    null,
	        	                    true,
	        	                    classTesterTopology,
	        	                    classTesterTopology.getClass().getMethod("testDeleteBroker", new Class[] {BrokerProxy.class})));
	                menuForSelectedObject.addSeparator();
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_DEPLOY),
                            new CommandInformation(
                                    null,
                                    null,
                                    null,
                                    true,
                                    classTesterBroker,
                                    classTesterBroker.getClass().getMethod("testDeployBrokerConfiguration", new Class[] {BrokerProxy.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_CANCEL_DEPLOY),
                            new CommandInformation(
                                    null,
                                    null,
                                    null,
                                    true,
                                    classTesterBroker,
                                    this.classTesterBroker.getClass().getMethod("testCancelDeploy", new Class[] {BrokerProxy.class})));
                    menuForSelectedObject.addSeparator();
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.GRANT_ACCESS),
                            new CommandInformation(
                                    new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.ACL_NAME),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_TYPE),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_DOMAIN_GRANT),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_PERMISSION)},
                                    new String[] { ResourcesHandler.ACL_NAME,
                                                    ResourcesHandler.ACL_TYPE,
                                                    ResourcesHandler.ACL_DOMAIN_GRANT,
                                                    ResourcesHandler.ACL_PERMISSION},
                                    new String[] { "", "USER", "", "FULL" },
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testGrantAccess", new Class[] {AdministeredObject.class, String.class, String.class, String.class, String.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.REMOVE_ACCESS),
                            new CommandInformation(
                                    new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.ACL_NAME),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_TYPE),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_DOMAIN_REMOVE)},
                                    new String[] { ResourcesHandler.ACL_NAME,
                                                   ResourcesHandler.ACL_TYPE,
                                                   ResourcesHandler.ACL_DOMAIN_REMOVE},
                                    new String[] { "", "USER", "" },
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testRemoveAccess", new Class[] {AdministeredObject.class, String.class, String.class, String.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.SHOW_ACCESS),
                            new CommandInformation(
                                    null,
                                    null,
                                    null,
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testShowAccess", new Class[] {AdministeredObject.class} )));
                    menuForSelectedObject.addSeparator();
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_SET_UUID),
                            new CommandInformation(
                                    new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_NEW_UUID)},
                                    new String[] { ResourcesHandler.BROKER_NEW_UUID},
                                    new String[] { "" },
                                    true,
                                    classTesterBroker,
                                    this.classTesterBroker.getClass().getMethod("testSetUUID", new Class[] {BrokerProxy.class, String.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_FORCE_SUBSCRIBE),
                            new CommandInformation(
                                    null,
                                    null,
                                    null,
                                    true,
                                    classTesterBroker,
                                    this.classTesterBroker.getClass().getMethod("testForceSubscribe", new Class[] {BrokerProxy.class} )));
                    menuForSelectedObject.addSeparator();
	                BrokerProxy.MulticastParameterSet params = null;
                    try {
                        params = ((BrokerProxy)o).getMulticastParameters();
                    } catch (ConfigManagerProxyPropertyNotInitializedException ex) {
                        // ignore - use the BrokerProxy.MulticastParameterSet created below.
                    }
                    if (params == null) {
                        params = new BrokerProxy.MulticastParameterSet();
                    }
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_MULTICASTPROPERTIES),
	                		new CommandInformation(
	        	                    new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_ENABLE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_IPV4_MINIMUM_ADDRESS),
                                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_IPV4_MAXIMUM_ADDRESS),
                                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_DATA_PORT),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_PACKET_SIZE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_HB_TIMEOUT),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_TTL),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_IPV4_NETWORK_INTERFACE),
                                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_ISRELIABLE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_ISSECURE),
                                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_PROTOCOL_TYPE),
                                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_MAX_MEMORY_ALLOWED),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_OVERLAPPING_TOPIC),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_LIMIT_TRANS),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_TRL),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_BACKOFF_TIME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_NACK_CHECK),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_PACKET_BUFFERS),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_SOCKET_BUFFER),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_HISTORY_CLEANING_TIME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_MINIMAL_HISTORY_SIZE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_NACK_ACC_TIME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MULTICAST_MAX_KEY_AGE)},
	        	                    null,
	        	                    new String[] { ""+params.multicastEnabled,
		                            ""+params.multicastMinimumIPv4Address,
                                    ""+params.multicastMaximumIPv4Address,
                                    ""+params.multicastDataPort,
		                            ""+params.multicastPacketSizeBytes,
		                            ""+params.multicastHeartbeatTimeoutSec,
		                            ""+params.multicastMcastSocketTTL,
		                            ""+params.multicastIPv4Interface,
                                    ""+params.multicastDefaultQOS,
		                            ""+params.multicastDefaultSecurity,
                                    ""+params.multicastProtocolType,
                                    ""+params.multicastMaxMemoryAllowedKBytes,
		                            ""+params.multicastOverlappingTopicBehaviour,
		                            ""+params.multicastLimitTransRate,
		                            ""+params.multicastTransRateLimitKbps,
		                            ""+params.multicastBackoffTimeMillis,
		                            ""+params.multicastNACKCheckPeriodMillis,
		                            ""+params.multicastPacketBuffers,
		                            ""+params.multicastSocketBufferSizeKBytes,
		                            ""+params.multicastHistoryCleaningTimeSec,
		                            ""+params.multicastMinimalHistoryKBytes,
		                            ""+params.multicastNACKAccumulationTimeMillis,
		                            ""+params.multicastMaxKeyAge },
	        	                    true,
	        	                    classTesterBroker,
	        	                    classTesterBroker.getClass().getMethod("testModifyBrokerMulticastProperties", new Class[] {BrokerProxy.class, Boolean.TYPE, String.class,
                                                                                                                            String.class, Integer.TYPE,
	        	                    																						Integer.TYPE, Integer.TYPE, Integer.TYPE, String.class,
	        	                    																						Boolean.TYPE, Boolean.TYPE, String.class,
                                                                                                                            Integer.TYPE, Integer.TYPE,	String.class, Integer.TYPE,
                                                                                                                            Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE,
                                                                                                                            Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE})));
	        	    String[] brokerPropertyDefaults = null;
	                
                    try {
                        int defaultIBPort = ((BrokerProxy)o).getInterbrokerPort();
                        if (defaultIBPort == -1) {
                            defaultIBPort = 1507;
                        }
                        brokerPropertyDefaults = new String[] {
                        		o.getName(),
                                o.getShortDescription(),
                                o.getLongDescription(),
                                ((BrokerProxy)o).getQueueManagerName(),
                                ((BrokerProxy)o).getSSLKeyRingFileName(),
                                ((BrokerProxy)o).getSSLPasswordFileName(),
                                ""+((BrokerProxy)o).getSSLConnectorEnabled(),
                                (((BrokerProxy)o).getTemporaryTopicQualityOfProtectionLevel()).toString(),
                                (((BrokerProxy)o).getSysQualityOfProtectionLevel()).toString(),
                                (((BrokerProxy)o).getISysQualityOfProtectionLevel()).toString(),
                                ((BrokerProxy)o).getInterbrokerHost(),
                                ""+defaultIBPort,
                                ((BrokerProxy)o).getAuthenticationProtocols()};
                    } catch (ConfigManagerProxyPropertyNotInitializedException ex ) {
                        brokerPropertyDefaults = new String[] { "", "", "", "", "", "", AttributeConstants.FALSE, "", "", "", "1507", "" };
                    }
                    
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_PROPERTIES),
	                		new CommandInformation(
	        	                    new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_NAME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_SHORT_DESC),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_LONG_DESC),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.QMGR),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.SSL_KEYRING),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.SSL_PASSWORD),
                                    ResourcesHandler.getNLSResource(ResourcesHandler.SSL_CONNECTOR_ENABLED),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.TEMP_TOPIC_QOP),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.SYS_QOP),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.ISYS_QOP),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.IB_HOST),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.IB_PORT),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.AUTH_PROTOCOLS)},
	        	                    null,
	        	                    brokerPropertyDefaults,
	        	                    true,
	        	                    classTesterBroker,
	        	                    classTesterBroker.getClass().getMethod("testModifyBrokerProperties", new Class[] {BrokerProxy.class, String.class, String.class,
	        	                            String.class, String.class, String.class, String.class,
                                            Boolean.TYPE, String.class, String.class, String.class,
                                            String.class, Integer.TYPE, String.class})));
	            } else if (type == ConfigurationObjectType.executiongroup) {
	            
	            	// Context sensitive properties for the execution group
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_DEPLOY),
	                		new CommandInformation(
		                            ResourcesHandler.getNLSResource(ResourcesHandler.SELECT_BAR),
		                            JFileChooser.OPEN_DIALOG, "bar", false, classTesterEG,
		                            this.classTesterEG.getClass().getMethod("testDeployBAR", new Class[] {ExecutionGroupProxy.class, String.class}), false));
	                menuForSelectedObject.addSeparator();
	            	addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_STARTFLOWS),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStartMsgFlows", new Class[] {AdministeredObject.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_STOPFLOWS),
	                		new CommandInformation(
	                                new String[] {ResourcesHandler.getNLSResource(ResourcesHandler.IMMEDIATE_STOP)},
	                                new String[] {ResourcesHandler.IMMEDIATE_STOP},
	        	                    new String[] { "n" },
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStopMsgFlows", new Class[] {AdministeredObject.class, Boolean.TYPE} )));
	                menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_STARTUSERTRACE),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStartUserTrace", new Class[] {AdministeredObject.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_DEBUGUSERTRACE),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStartDebugUserTrace", new Class[] {AdministeredObject.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_STOPUSERTRACE),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStopUserTrace", new Class[] {AdministeredObject.class} )));
	                menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_DELETEDEPLOYED),
	                		new CommandInformation(
	                                new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.DEPLOYED_OBJECTS_TO_REMOVE)},
	                                new String[] {
	        	                    ResourcesHandler.DEPLOYED_OBJECTS_TO_REMOVE},
	        	                    null,
	        	                    true,
	        	                    classTesterEG,
	        	                    classTesterEG.getClass().getMethod("testDeleteDeployed", new Class[] {ExecutionGroupProxy.class, String.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_DELETE),
	                		new CommandInformation(
	        	                    null,
	        	                    null,
	        	                    null,
	        	                    true,
	        	                    classTesterBroker,
	        	                    classTesterBroker.getClass().getMethod("testDeleteEG", new Class[] {ExecutionGroupProxy.class})));
	                menuForSelectedObject.addSeparator();
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.GRANT_ACCESS),
                            new CommandInformation(
                                    new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.ACL_NAME),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_TYPE),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_DOMAIN_GRANT),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_PERMISSION)},
                                    new String[] { ResourcesHandler.ACL_NAME,
                                                    ResourcesHandler.ACL_TYPE,
                                                    ResourcesHandler.ACL_DOMAIN_GRANT,
                                                    ResourcesHandler.ACL_PERMISSION},
                                    new String[] { "", "USER", "", "FULL" },
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testGrantAccess", new Class[] {AdministeredObject.class, String.class, String.class, String.class, String.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.REMOVE_ACCESS),
                            new CommandInformation(
                                    new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.ACL_NAME),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_TYPE),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_DOMAIN_REMOVE)},
                                    new String[] { ResourcesHandler.ACL_NAME,
                                                   ResourcesHandler.ACL_TYPE,
                                                   ResourcesHandler.ACL_DOMAIN_REMOVE},
                                    new String[] { "", "USER", "" },
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testRemoveAccess", new Class[] {AdministeredObject.class, String.class, String.class, String.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.SHOW_ACCESS),
                            new CommandInformation(
                                    null,
                                    null,
                                    null,
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testShowAccess", new Class[] {AdministeredObject.class} )));
                    menuForSelectedObject.addSeparator();
                    String defaultName = "";
                    String defaultShortDesc = "";
                    String defaultLongDesc = "";
                    try {
                        defaultName = o.getName();
                        defaultShortDesc = o.getShortDescription();
                        defaultLongDesc = o.getLongDescription();
                    } catch (ConfigManagerProxyPropertyNotInitializedException e) {
                        // ignore (use empty strings)
                    }
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.EG_PROPERTIES),
                    		new CommandInformation(
                                    new String[] {
                                            ResourcesHandler.getNLSResource(ResourcesHandler.EG_NAME),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.EG_SHORT_DESC),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.EG_LONG_DESC)},
                                    null,
                                    new String[] {
                                            defaultName,
                                            defaultShortDesc,
                                            defaultLongDesc },
                                    true,
                                    classTesterEG,
                                    classTesterEG.getClass().getMethod("testModifyEGProperties", new Class[] {ExecutionGroupProxy.class, String.class, String.class, String.class})));  
	            } else if (type == ConfigurationObjectType.messageflow) {
	            
	            	// Context sensitive properties for message flows
	            	addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.MF_START),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStartMsgFlows", new Class[] {AdministeredObject.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.MF_STOP),
	                		new CommandInformation(
	                                new String[] {ResourcesHandler.getNLSResource(ResourcesHandler.IMMEDIATE_STOP)},
	                                new String[] {ResourcesHandler.IMMEDIATE_STOP},
	        	                    new String[] { "n" },
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStopMsgFlows", new Class[] {AdministeredObject.class, Boolean.TYPE} )));

                    menuForSelectedObject.addSeparator();
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.MF_DELETE),
                            new CommandInformation(
                                    null,
                                    null,
                                    null,
                                    true,
                                    classTesterFlow,
                                    classTesterFlow.getClass().getMethod("testDeleteMsgFlow", new Class[] {MessageFlowProxy.class } )));

                    menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.MF_STARTUSERTRACE),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStartUserTrace", new Class[] {AdministeredObject.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.MF_DEBUGUSERTRACE),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStartDebugUserTrace", new Class[] {AdministeredObject.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.MF_STOPUSERTRACE),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterMisc,
	        	                    classTesterMisc.getClass().getMethod("testStopUserTrace", new Class[] {AdministeredObject.class} )));
	            } else if (type == ConfigurationObjectType.log) {
	            
	            	// Context sensitive properties for the broker log
	            	addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.LOG_DISPLAY),
	            			new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterLog,
	        	                    classTesterLog.getClass().getMethod("testLogDisplay", new Class[] {LogProxy.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.LOG_CLEAR),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterLog,
	        	                    classTesterLog.getClass().getMethod("testLogClear", new Class[] {LogProxy.class} )));
	            } else if (type == ConfigurationObjectType.topicroot) {
	            
	            	// Context sensitive properties for the topic root
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPICROOT_DEPLOY),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterTopicRoot,
	        	                    classTesterTopicRoot.getClass().getMethod("testDeployTopicConfiguration", new Class[] {TopicRootProxy.class} )));
	                menuForSelectedObject.addSeparator();
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_CREATE),
	                		new CommandInformation(
	                                new String[] {ResourcesHandler.getNLSResource(ResourcesHandler.NEW_TOPIC_NAME)},
	                                new String[] {ResourcesHandler.NEW_TOPIC_NAME},
	        	                    null,
	        	                    true,
	        	                    classTesterTopic,
	        	                    classTesterTopic.getClass().getMethod("testCreateTopic", new Class[] {TopicProxy.class, String.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_MOVE),
	                		new CommandInformation(
	                                new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MOVE_TOPIC_NAME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MOVE_TOPIC_PARENT)},
	                                new String[] {
	        	                    ResourcesHandler.MOVE_TOPIC_NAME,
	        	                    ResourcesHandler.MOVE_TOPIC_PARENT},
	        	                    new String[] { "IBM", "Shares/Tech" },
	        	                    true,
	        	                    classTesterTopic,
	        	                    classTesterTopic.getClass().getMethod("testMoveTopic", new Class[] {TopicProxy.class, String.class, String.class})));
	                menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPICROOT_DISPLAYUSERS),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterTopicRoot,
	        	                    classTesterTopicRoot.getClass().getMethod("testDisplayUsers", new Class[] {TopicRootProxy.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPICROOT_DISPLAYGROUPS),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterTopicRoot,
	        	                    classTesterTopicRoot.getClass().getMethod("testDisplayGroups", new Class[] {TopicRootProxy.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPICROOT_DISPLAYPUBLICGROUPS),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterTopicRoot,
	        	                    classTesterTopicRoot.getClass().getMethod("testDisplayPublicGroups", new Class[] {TopicRootProxy.class} )));
	                menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_ADDPOLICY),
	                		new CommandInformation(
	                                new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PRINCIPAL_NAME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_TYPE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PUBLISH),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_SUBSCRIBE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PERSIST)},
	                                new String[] {
	        	                    ResourcesHandler.POLICY_PRINCIPAL_NAME,
	        	                    ResourcesHandler.POLICY_TYPE,
	        	                    ResourcesHandler.POLICY_PUBLISH,
	        	                    ResourcesHandler.POLICY_SUBSCRIBE,
	        	                    ResourcesHandler.POLICY_PERSIST},
	        	                    new String[] {
									"",
									AttributeConstants.TOPIC_PRINCIPAL_GROUP,
									AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
									AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
									AttributeConstants.TOPIC_PRINCIPAL_PERSISTENT },
	        	                    true,
	        	                    classTesterTopic,
	        	                    classTesterTopic.getClass().getMethod("testAddPolicy", new Class[] {TopicProxy.class, String.class, String.class,
	        	                    																	String.class, String.class, String.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_REMOVEPOLICY),
	                		new CommandInformation(
	                                new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PRINCIPAL_NAME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_TYPE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PUBLISH),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_SUBSCRIBE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PERSIST)},
	                                new String[] {
	        	                    ResourcesHandler.getUserSetting(ResourcesHandler.POLICY_PRINCIPAL_NAME, ""),
	        	                    ResourcesHandler.getUserSetting(ResourcesHandler.POLICY_TYPE, AttributeConstants.TOPIC_PRINCIPAL_GROUP),
	        	                    ResourcesHandler.getUserSetting(ResourcesHandler.POLICY_PUBLISH, AttributeConstants.TOPIC_PRINCIPAL_ALLOW),
	        	                    ResourcesHandler.getUserSetting(ResourcesHandler.POLICY_SUBSCRIBE, AttributeConstants.TOPIC_PRINCIPAL_ALLOW),
	        	                    ResourcesHandler.getUserSetting(ResourcesHandler.POLICY_PERSIST, AttributeConstants.TOPIC_PRINCIPAL_PERSISTENT)},
	        	                    new String[] {
									"",
	        	                    AttributeConstants.TOPIC_PRINCIPAL_GROUP,
	        	                    AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
	        	                    AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
	        	                    AttributeConstants.TOPIC_PRINCIPAL_PERSISTENT },
	        	                    true,
	        	                    classTesterTopic,
	        	                    classTesterTopic.getClass().getMethod("testRemovePolicy", new Class[] {TopicProxy.class, String.class, String.class,
	        	                    																	String.class, String.class, String.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPICROOT_MODIFYDEFAULTPOLICY),
	                		new CommandInformation(
	                                new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PUBLISH),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_SUBSCRIBE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PUBLISH)},
	                                new String[] {
	        	                    ResourcesHandler.POLICY_PUBLISH,
	        	                    ResourcesHandler.POLICY_SUBSCRIBE,
	        	                    ResourcesHandler.POLICY_PERSIST},
	        	                    new String[] {
	                                        AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
	                                        AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
	                                        AttributeConstants.TOPIC_PRINCIPAL_PERSISTENT},
	        	                    true,
	        	                    classTesterTopic,
	        	                    classTesterTopic.getClass().getMethod("testModifyDefaultPolicy", new Class[] {TopicProxy.class, String.class, String.class, String.class} )));
                    menuForSelectedObject.addSeparator();
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.GRANT_ACCESS),
                            new CommandInformation(
                                    new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.ACL_NAME),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_TYPE),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_DOMAIN_GRANT),
                                            ResourcesHandler.getNLSResource(ResourcesHandler.ACL_PERMISSION)},
                                    new String[] { ResourcesHandler.ACL_NAME,
                                                    ResourcesHandler.ACL_TYPE,
                                                    ResourcesHandler.ACL_DOMAIN_GRANT,
                                                    ResourcesHandler.ACL_PERMISSION},
                                    new String[] { "", "USER", "", "FULL" },
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testGrantAccess", new Class[] {AdministeredObject.class, String.class, String.class, String.class, String.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.REMOVE_ACCESS),
                            new CommandInformation(
                                    new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.ACL_NAME),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_TYPE),
                                                   ResourcesHandler.getNLSResource(ResourcesHandler.ACL_DOMAIN_REMOVE)},
                                    new String[] { ResourcesHandler.ACL_NAME,
                                                   ResourcesHandler.ACL_TYPE,
                                                   ResourcesHandler.ACL_DOMAIN_REMOVE},
                                    new String[] { "", "USER", "" },
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testRemoveAccess", new Class[] {AdministeredObject.class, String.class, String.class, String.class} )));
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.SHOW_ACCESS),
                            new CommandInformation(
                                    null,
                                    null,
                                    null,
                                    true,
                                    classTesterAdministeredObject,
                                    this.classTesterAdministeredObject.getClass().getMethod("testShowAccess", new Class[] {AdministeredObject.class} )));
                    
	            } else if (type == ConfigurationObjectType.topic) {
	            
	            	// Context sensitive properties for topics
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_CREATE),
	                		new CommandInformation(
	                                new String[] {ResourcesHandler.getNLSResource(ResourcesHandler.NEW_TOPIC_NAME)},
	                                new String[] {ResourcesHandler.NEW_TOPIC_NAME},
	        	                    null,
	        	                    true,
	        	                    classTesterTopic,
	        	                    classTesterTopic.getClass().getMethod("testCreateTopic", new Class[] {TopicProxy.class, String.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_MOVE),
	                		new CommandInformation(
	                                new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MOVE_TOPIC_NAME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.MOVE_TOPIC_PARENT)},
	                                new String[] {
	        	                    ResourcesHandler.MOVE_TOPIC_NAME,
	        	                    ResourcesHandler.MOVE_TOPIC_PARENT},
	        	                    new String[] { "IBM", "Shares/Tech" },
	        	                    true,
	        	                    classTesterTopic,
	        	                    classTesterTopic.getClass().getMethod("testMoveTopic", new Class[] {TopicProxy.class, String.class, String.class})));
	                menuForSelectedObject.addSeparator();
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_DELETE),
                            new CommandInformation(
                                    null,
                                    null,
                                    null,
                                    true,
                                    classTesterTopic,
                                    classTesterTopic.getClass().getMethod("testDeleteTopic", new Class[] {TopicProxy.class} )));
                    menuForSelectedObject.addSeparator();
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_ADDPOLICY),
	                		new CommandInformation(
	                                new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PRINCIPAL_NAME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_TYPE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PUBLISH),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_SUBSCRIBE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PERSIST)},
	                                new String[] {
	        	                    ResourcesHandler.POLICY_PRINCIPAL_NAME,
	        	                    ResourcesHandler.POLICY_TYPE,
	        	                    ResourcesHandler.POLICY_PUBLISH,
	        	                    ResourcesHandler.POLICY_SUBSCRIBE,
	        	                    ResourcesHandler.POLICY_PERSIST},
	        	                    new String[] {
									"",
									AttributeConstants.TOPIC_PRINCIPAL_GROUP,
									AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
									AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
									AttributeConstants.TOPIC_PRINCIPAL_PERSISTENT },
	        	                    true,
	        	                    classTesterTopic,
	        	                    classTesterTopic.getClass().getMethod("testAddPolicy", new Class[] {TopicProxy.class, String.class, String.class,
	        	                    																	String.class, String.class, String.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_REMOVEPOLICY),
	                		new CommandInformation(
	                                new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PRINCIPAL_NAME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_TYPE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PUBLISH),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_SUBSCRIBE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PERSIST)},
	                                new String[] {
	        	                    ResourcesHandler.POLICY_PRINCIPAL_NAME,
	        	                    ResourcesHandler.POLICY_TYPE,
	        	                    ResourcesHandler.POLICY_PUBLISH,
	        	                    ResourcesHandler.POLICY_SUBSCRIBE,
	        	                    ResourcesHandler.POLICY_PERSIST},
	        	                    new String[] {
        									"",
        									AttributeConstants.TOPIC_PRINCIPAL_GROUP,
        									AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
        									AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
        									AttributeConstants.TOPIC_PRINCIPAL_PERSISTENT },
	        	                    true,
	        	                    classTesterTopic,
	        	                    classTesterTopic.getClass().getMethod("testRemovePolicy", new Class[] {TopicProxy.class, String.class, String.class,
	        	                    																	String.class, String.class, String.class} )));
	                menuForSelectedObject.addSeparator();
	                
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_ADDDEFAULTPOLICY),
	                		new CommandInformation(
	                                new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PRINCIPAL_NAME),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_TYPE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PUBLISH),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_SUBSCRIBE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PERSIST)},
	        	                    null,
                                    new String[] {
                                            "",
                                            AttributeConstants.TOPIC_PRINCIPAL_GROUP,
                                            AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
                                            AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
                                            AttributeConstants.TOPIC_PRINCIPAL_PERSISTENT },
	        	                    true,
	        	                    classTesterTopic,
	        	                    classTesterTopic.getClass().getMethod("testAddDefaultPolicy", new Class[] {TopicProxy.class, String.class, String.class,
	        	                    																	String.class, String.class, String.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_REMOVEDEFAULTPOLICY),
	                		new CommandInformation(
	                                null,
	                                null,
	                                null,
	        	                    true,
	        	                    classTesterTopic,
	        	                    classTesterTopic.getClass().getMethod("testRemoveDefaultPolicy", new Class[] {TopicProxy.class} )));
	                addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_MODIFYDEFAULTPOLICY),
	                		new CommandInformation(
	                                new String[] {
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PUBLISH),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_SUBSCRIBE),
	        	                    ResourcesHandler.getNLSResource(ResourcesHandler.POLICY_PUBLISH)},
	                                new String[] {
	        	                    ResourcesHandler.POLICY_PUBLISH,
	        	                    ResourcesHandler.POLICY_SUBSCRIBE,
	        	                    ResourcesHandler.POLICY_PERSIST},
	        	                    new String[] {
	                                        AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
	                                        AttributeConstants.TOPIC_PRINCIPAL_ALLOW,
	                                        AttributeConstants.TOPIC_PRINCIPAL_PERSISTENT},
	        	                    true,
	        	                    classTesterTopic,
	        	                    classTesterTopic.getClass().getMethod("testModifyDefaultPolicy", new Class[] {TopicProxy.class, String.class, String.class, String.class} )));
                    menuForSelectedObject.addSeparator();
                    String[] topicPropertyDefaults = new String[] { "", TopicProxy.QoP.none.toString(), "false", "", "", "false", "false" };
                    try {
                        topicPropertyDefaults = new String[] {
                                o.getName(),
                                (((TopicProxy) o).getQualityOfProtectionLevel()).toString(),
                                ""+((TopicProxy) o).getMulticastEnabled(),
                                ((TopicProxy) o).getMulticastIPv4GroupAddress(),
                                ""+((TopicProxy) o).getMulticastEncrypted(),
                                ""+((TopicProxy) o).getMulticastQualityOfService() };
                    } catch (ConfigManagerProxyPropertyNotInitializedException ex ) {
                        // ignore - use values above
                    }
                    addMenuItem(menuForSelectedObject, ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_PROPERTIES),
                            new CommandInformation(
                                    new String[] {
                                    ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_NAME),
                                    ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_QOP),
                                    ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_MULTICAST_ENABLED),
                                    ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_MULTICAST_IPV4_GROUP_ADDRESS),
                                    ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_MULTICAST_ENCRYPTED),
                                    ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_MULTICAST_RELIABLE)},
                                    null,
                                    topicPropertyDefaults,
                                    true,
                                    classTesterTopic,
                                    classTesterTopic.getClass().getMethod("testModifyTopicProperties", new Class[] {TopicProxy.class, String.class, String.class,
                                                                                                        String.class, String.class, Boolean.TYPE, Boolean.TYPE} )));
	            }
	        } else {
	            JMenuItem mi = new JMenuItem("["+ResourcesHandler.getNLSResource(ResourcesHandler.NO_OPTIONS_AVAILABLE)+"]");
	            mi.setEnabled(false);
	            menuForSelectedObject.add(mi);
	        }
        } catch (NoSuchMethodException ex) {
            log(ex);
        }

    }
    
    /**
     * Gets parameters from the user for a particular command
     * @param title Text to display in the title bar of the request window
     * @param labels describes the parameter prompts
     * @param defaults Default values for the text fields
     * @param canBeBatched True iff the command will be added to a
     * batch if the user submits the action. This determines the
     * text used for the OK button.
     */
    void getParameters(String title, String[] labels, String[] defaults, boolean canBeBatched) {

        if (labels != null) {
            Container dataEntryContent = dataEntryFrame.getContentPane();
            
            dataEntryContent.removeAll();
            dataEntryFrame.setTitle(title);
            SpringLayout layout = (SpringLayout) dataEntryContent.getLayout();
            
            JLabel[] lab = new JLabel[labels.length];
            tf = new JTextField[labels.length];

            for (int i=0; i<labels.length; i++) {
                lab[i] = new JLabel(labels[i], JLabel.RIGHT);
                tf[i] = new JTextField(defaults[i]);
                lab[i].setLabelFor(tf[i]);
                dataEntryContent.add(lab[i]);
                dataEntryContent.add(tf[i]);
            }
            
            // Work out the correct size and position for each component in the frame
            // First, work out the max width for the first column (labels)
            
            Spring maxWidth = Spring.constant(0);
            for (int i=0; i<labels.length; i++) {
                SpringLayout.Constraints c = layout.getConstraints(dataEntryContent.getComponent(i*2));
                maxWidth = Spring.max(maxWidth, c.getWidth());
            }
            
            if ((canBeBatched) && (cmp.isBatching())) {
                submitButton = new JButton(ResourcesHandler.getNLSResource(ResourcesHandler.ADDTOBATCH));
            } else {
                submitButton = new JButton(ResourcesHandler.getNLSResource(ResourcesHandler.SUBMIT));
            }
            
            submitButton.addActionListener(guiEventListener);
            JRootPane rootPane = dataEntryFrame.getRootPane();
            rootPane.setDefaultButton(submitButton);
            dataEntryContent.add(submitButton);

            cancelButton = new JButton(ResourcesHandler.getNLSResource(ResourcesHandler.CANCEL));
            cancelButton.addActionListener(guiEventListener);
            dataEntryContent.add(cancelButton);
            
            // Now position all the components correctly
            // Start with the text fields and labels
            int xPadding = 6;
            int yPadding = 6;
            int betweenColumnPadding = 12;
            int tfHeight = 20;
            int tfWidth = 140;
            int buttonHeight = 26;
            for (int i=0; i<labels.length; i++) {
                SpringLayout.Constraints c = layout.getConstraints(dataEntryContent.getComponent(i*2));
                c.setX(Spring.constant(xPadding));
                c.setWidth(maxWidth);
                c.setY(Spring.constant(yPadding+(i*(tfHeight+yPadding))));
                c.setHeight(Spring.constant(tfHeight));
                
                c = layout.getConstraints(dataEntryContent.getComponent((i*2)+1));
                c.setX(Spring.sum(maxWidth, Spring.constant(betweenColumnPadding)));
                c.setY(Spring.constant(yPadding+(i*(tfHeight+yPadding))));
                c.setHeight(Spring.constant(tfHeight));
                c.setWidth(Spring.constant(tfWidth));
            }
            
            // Now the buttons - submit/add to batch
            SpringLayout.Constraints c = layout.getConstraints(dataEntryContent.getComponent(labels.length*2));
            c.setX(Spring.sum(Spring.sum(maxWidth, Spring.minus(c.getWidth())), Spring.constant(xPadding)));
            c.setY(Spring.constant(yPadding+(labels.length*(tfHeight+yPadding))));
            c.setHeight(Spring.constant(buttonHeight));
            
            // Cancel button
            c = layout.getConstraints(dataEntryContent.getComponent((labels.length*2)+1));
            c.setX(Spring.sum(maxWidth, Spring.constant(betweenColumnPadding)));
            c.setY(Spring.constant(yPadding+(labels.length*(tfHeight+yPadding))));
            c.setHeight(Spring.constant(buttonHeight));
            
            // Now set the overall size of the dialog
            c = layout.getConstraints(dataEntryContent);
            c.setConstraint(SpringLayout.SOUTH, Spring.constant((labels.length * (tfHeight + yPadding)) + (buttonHeight + (yPadding*2))));
            c.setConstraint(SpringLayout.EAST, Spring.sum(maxWidth, Spring.constant(betweenColumnPadding + tfWidth + xPadding)));
            
            // (End of positioning code)
            if (!isHeadless()) {
                dataEntryFrame.pack();
                dataEntryFrame.addWindowListener(guiEventListener);
                dataEntryFrame.setResizable(false);
                centerComponentInExerciser(dataEntryFrame);
                dataEntryFrame.setVisible(true);
            }
        }
    }

    
    /**
     * Returns true if and only if the exerciser is
     * running in 'headless' mode (i.e. without a GUI)
     * @return boolean
     */
    public boolean isHeadless() {
        return headlessMode;
    }


    /**
     * Enables or disables various items in the automation menu
     * based on whether a script is currently being recorded
     */
    private void setAutomationMenuEnablement() {

        Component record = automation.getMenuComponent(0);
        Component wait = automation.getMenuComponent(1);
        Component play = automation.getMenuComponent(2);
        Component stop = automation.getMenuComponent(3);

        if (scriptFileName == null) {
            record.setEnabled (true);
            wait.setEnabled (false);
            play.setEnabled (true);
            stop.setEnabled (false);
        } else {
            record.setEnabled (false);
            wait.setEnabled (true);
            play.setEnabled (true);
            stop.setEnabled (true);
        }
    }

    /**
     * Sets up the tree for the supplied object but doesn't
     * recurse by initialising the tree for all of the
     * object's descendents.
     * @param affectedObject Object for which the tree should be initialised
     */
    public void discoverImmediateChildren(AdministeredObject affectedObject) {
        initialiseTreeForAdministeredObject(affectedObject, true);
    }
    
    /**
     * Sets up the tree for the supplied object
     * @param affectedObject Object for which the tree should be initialised
     * @param getImmediateChildrenOnly - If true, the objects
     * immediate children should also be initialised. If false,
     * either all children will be initialised, or none of them,
     * depending on the value of autoGetChildren.
     */
    void initialiseTreeForAdministeredObject(AdministeredObject affectedObject,
                                                     boolean getImmediateChildrenOnly) {
        
        DefaultMutableTreeNode treeNodeOfParent = getTree(affectedObject, getImmediateChildrenOnly);
        administeredObjects.put(affectedObject, treeNodeOfParent);
        administeredObjects.put(treeNodeOfParent, affectedObject);
        expandAll(tree, true);

	    ((DefaultTreeModel )tree.getModel()).nodeStructureChanged(treeNodeOfParent);
    }

    
    
    /**
     * Adds the supplied text to the log panel of the GUI. 
     * @param s
     */
    public void log(String s) {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.getDefault());
        String formattedDate = df.format(new Date());
        s = "("+formattedDate+") "+s;
        
        if (console != null) {
            synchronized (console) { 
                SwingUtilities.invokeLater(new LogThread(s));
            }
        } else {
            System.out.println(s);
        }
    }
    
    /**
     * Used by the log() method.
     * Appends to the log at the bottom of the screen in
     * a separate thread, because append() and setCaretPosition()
     * are not thread-safe.
     */
    class LogThread implements Runnable {
        String s;
        public LogThread(String s) {
            this.s = s;
        }
        public void run() {
            console.append(s+"\n");
            console.setCaretPosition(console.getText().length());
        }
    }
    
    /**
     * Displays information on the supplied throwable to the console.
     * @param ex
     */

    public void log(Throwable ex) {
        // Dump the stack trace to a printwriter
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        ex.printStackTrace(pw);
        pw.flush();
        
        // Convert the printwriter into a string, converting
        // newlines to spaces.
        String stackTrace = baos.toString();
        stackTrace = stackTrace.replace('\n', ' ');
        log(stackTrace);
    }
    
    /**
     * Saves the console text to the specified file
     * @param fileName Name of the file to which the
     * console output should be saved
     */
    public void saveConsole(String fileName) {

        try {
            File f = new File(fileName);
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(console.getText().getBytes("UTF8"));
            fos.close();
            log(ResourcesHandler.getNLSResource(ResourcesHandler.CONSOLE_SAVED));
        } catch (IOException ex) {
            log(ex);
        }
    }
    
    /**
     * Returns a string describing the supplied object.
     * Is used for identifying the selected object in a script. Cannot use
     * the UUID, because if the object is being created as part of
     * the script the UUID is different each time!
     */
    private String getIdentifyingStringFromTreeNode(DefaultMutableTreeNode object) {
        StringBuffer retVal = new StringBuffer();
        TreeNode[] o = object.getPath();
        for (int i=0; i<o.length; i++) {
            String str = o[i].toString();
            retVal.append(str);
            if ((i+1) < o.length) {
                retVal.append(AUTOMATIONFORMAT_OBJECTPATHSEPARATOR);
            }
        }
        return retVal.toString();
    }

    
    /**
     * Issues the supplied action with the supplied parameters
     * @param testOwner Class tester responsible for invoking the
     * supplied method
     * @param method Method to invoke
     * @param textFieldInput any parameters to the method.
     * @param suppressEntryExitLogMessage True if and only if the
     * entry and exit log messages should not be displayed
     */
    void issueAction(Object testOwner, Method method, String[] textFieldInput, boolean suppressEntryExitLogMessage) {

        // Add the action to the output script; if no such script is being
        // written, addActionToOutputScript will have no effect.
        addActionToOutputScript(testOwner, method, textFieldInput);
        
        // Invoke the method
        if (method != null) {
            Class[] parameterTypes = method.getParameterTypes();
            Object[] parameters = null;
            boolean error = false;
            
            // Fit the supplied textFieldInput strings into the parameters array
            if (parameterTypes != null) {
                int startParameter = 0;
                
                if (parameterTypes.length > 0) {
                    parameters = new Object[parameterTypes.length];
                    
                    // The first parameter may be the selected administered object
                    if (AdministeredObject.class.isAssignableFrom(parameterTypes[0])) {
                        parameters[0] = selectedAdministeredObject;
                        startParameter++;
                    }
                    
                    // Now go through the other arguments and assign the values from
                    // the user input text fields to the object array that will be
                    // passed to the invoke method.
                    int positionOfInputParametersPointer = 0;
                    for (;startParameter<parameterTypes.length;startParameter++) {
                        
                        if (textFieldInput!=null) {
                            if (positionOfInputParametersPointer>=textFieldInput.length) {
                            	// There are more method parameters required than
                            	// there are user text fields. Fill in the remaining
                            	// parameters with null.
                                parameters[startParameter] = null;
                            } else {
                            	// Look at the supplied text field parameter and try
                            	// and squeeze it into the type required by the
                            	// target method.
	                            String thisInputParameter = textFieldInput[positionOfInputParametersPointer++];
	                            try {
                                    parameters[startParameter] = getObjectFromStringRepresentation(thisInputParameter, parameterTypes[startParameter]);
                                    if (parameters[startParameter] == null) {
                                        log("Error: Unknown parameter in "+method.getName()+": "+parameterTypes[startParameter].getName());
                                        error = true;
                                    }
	    	                        
	                            } catch (Exception ex) {
	                                log("Error: Invalid "+parameterTypes[startParameter].getName()+" parameter (#"+startParameter+") to "+method.getName());
	                                error = true;
	                            }
	                        }
                        } else {
                            // No parameters were supplied.
                            // Fill in all parameters with null.
                            parameters[startParameter] = null;
                        } 
                    }
                            
                }
            }
            
            if (!error) {
                // Tell the worker thread to invoke the requested action.
                commandThread.enqueueCommand(method, testOwner, parameters, suppressEntryExitLogMessage, false);
            } else {
                log(ResourcesHandler.getNLSResource(ResourcesHandler.INVALID_ARGUMENTS));
            }
        }
    }
    
    /**
     * Returns an object of the type described by the Class argument that
     * has a value described by the input parameter.
     * @param thisInputParameter Value of the returned object
     * @param class1 AdministeredObject, String or class representing one of the primitive types int, long or boolean.
     * @return Object of the required type and value, or null if and only
     * if the object could not be transformed.
     * @throws IllegalArgumentException if the type was an AdministeredObject
     * yet the object to which the string representation refers was not
     * available in the Configuration Manager's hierarchy.  
     */
    private Object getObjectFromStringRepresentation(String thisInputParameter, Class class1)
    throws IllegalArgumentException {
        Object retVal = null;
        if (class1.equals(String.class)) {
            retVal = thisInputParameter;
        } else if (class1.equals(Integer.TYPE)) {
            retVal = new Integer(Integer.parseInt(thisInputParameter)); 
        } else if (class1.equals(Long.TYPE)) {
            retVal = new Long(Long.parseLong(thisInputParameter)); 
        } else if (class1.equals(Boolean.TYPE)) {
            String yesIdentifier = ResourcesHandler.getNLSResource(ResourcesHandler.YES_INPUT_STRING_IDENTIFIER);
            if ((thisInputParameter.indexOf(yesIdentifier)>-1)||(thisInputParameter.indexOf(""+true)>-1)) {
                retVal = Boolean.TRUE;
            } else {
                retVal = Boolean.FALSE;
            }
        } else if (AdministeredObject.class.isAssignableFrom(class1)) {
            // throws IllegalArgumentException
            retVal = getAdministeredObjectFromString(thisInputParameter);
        }
        return retVal;
    }


    /**
     * If the user has requested that his/her actions are recorded into a script AND the described 
     * method represents a recordable action, this method adds the method and parameters described
     * by the input parameters to the currently active script. Otherwise, this method does nothing.
     * @param testOwner Member variable of the CMPAPIExerciser instance that will be invoked
     * @param method Method object that is to be invoked
     * @param textFieldInput Text-based input parameters to the method
     */
    private void addActionToOutputScript(Object testOwner, Method method, String[] textFieldInput) {
        if ((scriptFileName != null) && (!cannotBeAddedToScript(method))) {

            // Work out the test owner (i.e. member variable from this class
            // that is responsible for issuing the action).
            String testOwnerS = getMemberVariableNameFromInstance(testOwner);
            
            // If there's no test owner, then don't bother recording the action
            // because there's nothing to invoke.
            if (testOwnerS == null) {
                log(ResourcesHandler.getNLSResource(ResourcesHandler.ACTION_IGNORED));
            } else {
                try {
                    // If this is the first action, the XML document representing
                    // the script will not have been initialised, so do that here.
                    if (scriptOutputDoc == null) {
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        scriptOutputDoc = db.newDocument();
                        Element root = scriptOutputDoc.createElement(SCRIPTXML_ROOT);
                        root.setAttribute(SCRIPTXML_PROGNAME, SCRIPTXML_PROGNAME_VALUE);
                        root.setAttribute(SCRIPTXML_PROGID, SCRIPTXML_PROGID_VALUE);
                        root.setAttribute(SCRIPTXML_PROGVER, SCRIPTXML_PROGVER_VALUE);
                        scriptOutputDoc.appendChild(root);
                    }
                    
                    // Write the method information...
                    Element root = scriptOutputDoc.getDocumentElement();
                    Element methodInfo = scriptOutputDoc.createElement(SCRIPTXML_METHOD);
                    root.appendChild(methodInfo);
                    
                    // Set the name of method and the owning member variable
                    methodInfo.setAttribute(SCRIPTXML_NAME, method.getName());
                    methodInfo.setAttribute(SCRIPTXML_OWNER, testOwnerS);
                    
                    // Set the parameters
                    Class[] parameterTypes = method.getParameterTypes();
                    int numberOfParameterTypes = parameterTypes.length;
                    int textFieldCounter = 0;
                    for (int paramTypeCounter=0; paramTypeCounter< numberOfParameterTypes; paramTypeCounter++) {
                        Element parameter = scriptOutputDoc.createElement(SCRIPTXML_PARAMETER);
                        methodInfo.appendChild(parameter);
                        parameter.setAttribute(SCRIPTXML_TYPE, parameterTypes[paramTypeCounter].getName());
                        
                        // If the type of the current parameter is a descendant of
                        // 'AdministeredObject', use the selected object's
                        // node information and not the text field value that the user
                        // entered. This is to record
                        // the fact that the user selected an object in the tree before
                        // typing in the text fields.
                        if (AdministeredObject.class.isAssignableFrom(parameterTypes[paramTypeCounter])) {
                            if (selectedAdministeredObject != null) {
                                parameter.setAttribute(SCRIPTXML_VALUE, getIdentifyingStringFromTreeNode((DefaultMutableTreeNode)(administeredObjects.get(selectedAdministeredObject))));
                            }
                        } else {
                            parameter.setAttribute(SCRIPTXML_VALUE, textFieldInput[textFieldCounter++]);
                        }
                        
                        
                    }
                    log(ResourcesHandler.getNLSResource(ResourcesHandler.ADDED_AUTOMATION_ACTION));
                    
                } catch (ParserConfigurationException e) {
                    log(e);
                }
            }
        }
        
    }


    /**
     * Returns true if and only if the method described by the
     * supplied parameters should not be written to a script file
     * @param method
     * @return boolean true iff the method is not scriptable
     */
    private boolean cannotBeAddedToScript(Method method) {
        boolean retVal = false;
        if (method != null) {
            retVal = "stopRecording".equals(method.getName());
        }
        return retVal;
    }


    /**
	 * Returns the name of this object's member variable that equals
     * the supplied argument.
     * @param testOwner
     * @return String CMPAPIExerciser member variable
     */
    private String getMemberVariableNameFromInstance(Object testOwner) {
        
        String retVal = null;
        
        if (testOwner == this) {
        	retVal = "this";
        } else {
	        Field[] allFields = this.getClass().getFields();
	        
	        for (int i=0; i<allFields.length; i++) {
	        	Object o = null;
	            try {
	                o = allFields[i].get(this);
	            } catch (Exception e) {
	                // Ignore - return null
	            }
	            if (o == testOwner) {
	        		retVal = allFields[i].getName();
	        	}
	       	}
	    }
       	return retVal; 
    }


    /**
     * Initialises the GUI services
     */
    public void initWindow() {

        try {
            // Potentially long operation... display the hourglass
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);
            
            model = new DefaultTableModel() {
                private static final long serialVersionUID = 1L;
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            table = new JTable(model);
            table.setModel(model);
            table.setRowSelectionAllowed(false);
            model.addColumn(ResourcesHandler.getNLSResource(ResourcesHandler.PROPERTY_NAME));
            model.addColumn(ResourcesHandler.getNLSResource(ResourcesHandler.PROPERTY_VALUE));
            
            propertyNameColumn = table.getColumn(ResourcesHandler.getNLSResource(ResourcesHandler.PROPERTY_NAME));
            propertyValueColumn = table.getColumn(ResourcesHandler.getNLSResource(ResourcesHandler.PROPERTY_VALUE));
            
            root = getTree(cmp, false);
            tree = new JTree(root);
            initialiseMappingOfIdentifyingStringsToNodes(root);
            
            // Set up the blue console area
            if (console == null) {
                console = new JTextArea(ResourcesHandler.getNLSResource(ResourcesHandler.WELCOME), 12, 50);
                log(ResourcesHandler.getNLSResource(ResourcesHandler.PROGRAM_STARTED));
            }
            console.setBackground(new Color(212,232,255));
            console.addMouseListener(guiEventListener);

            // Set up the status line
            if (statusLine == null) {
                statusLine = new JLabel("");
                updateStatusLine();
            }
            
            // Set up the panel into which parameters are eneter
            dataEntryFrame = new JDialog(this);
            dataEntryFrame.setModal(true);
            JPanel panel = new JPanel();
            panel.setLayout(new SpringLayout());
            dataEntryFrame.setContentPane(panel);
            
            menuForSelectedObject = new JPopupMenu(ResourcesHandler.getNLSResource(ResourcesHandler.SELECTED));

            Container content = getContentPane();

            tree.addMouseListener(guiEventListener);
            content.removeAll();

            JScrollPane treeScroller = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            JScrollPane tableScroller = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            JScrollPane consoleScroller = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            JPanel consoleAndStatusLine = new JPanel();
            consoleAndStatusLine.setLayout(new BorderLayout());
            consoleAndStatusLine.add(consoleScroller, BorderLayout.CENTER);
            consoleAndStatusLine.add(statusLine, BorderLayout.SOUTH);

            JSplitPane treeAndTable = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, treeScroller, tableScroller);
            treeAndTable.setResizeWeight(0.4);
            treeAndTable.setDividerLocation(0.4);
            JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, treeAndTable, consoleAndStatusLine);
            jsp.setResizeWeight(0.6);
            jsp.setDividerLocation(0.6);
            content.add(jsp);


            // Generate the menu bar
            JMenuBar m = new JMenuBar();
            setJMenuBar(m);


            // File menu ->
            JMenu fileMenuRoot = new JMenu(ResourcesHandler.getNLSResource(ResourcesHandler.FILE));
            fileMenuRoot.setMnemonic(KeyEvent.VK_F);

            connectMenuItem = addMenuItem(fileMenuRoot,
                    ResourcesHandler.getNLSResource(ResourcesHandler.FILE_CONNECT),
                    KeyEvent.VK_C, KeyEvent.VK_C, ActionEvent.ALT_MASK,
                    null);
            
            configureConnectAction(ResourcesHandler.getUserSettingBoolean(ResourcesHandler.FILE_CONNECTUSINGPROPERTIESFILE, false));
            
            addMenuItem(fileMenuRoot,
                    ResourcesHandler.getNLSResource(ResourcesHandler.FILE_DISCONNECT),
                    KeyEvent.VK_D, KeyEvent.VK_D, ActionEvent.ALT_MASK,
                    new CommandInformation(null, null, null, false, classTesterCMP,
                    this.classTesterCMP.getClass().getMethod("testDisconnect", null)));
            
            fileMenuRoot.addSeparator();
            
            addMenuItem(fileMenuRoot, ResourcesHandler.getNLSResource(ResourcesHandler.FILE_BATCHSTART), KeyEvent.VK_B, KeyEvent.VK_F3, 0,
                    new CommandInformation(
                            null, null, null, false, classTesterCMP,
                            this.classTesterCMP.getClass().getMethod("testBatchStart", null)));
            
            addMenuItem(fileMenuRoot, ResourcesHandler.getNLSResource(ResourcesHandler.FILE_BATCHSEND), KeyEvent.VK_N, KeyEvent.VK_F3, ActionEvent.CTRL_MASK,
                    new CommandInformation(
		                    null, null, null, false, classTesterCMP,
		                    this.classTesterCMP.getClass().getMethod("testBatchSend", null)));
            
            addMenuItem(fileMenuRoot, ResourcesHandler.getNLSResource(ResourcesHandler.FILE_BATCHCLEAR), KeyEvent.VK_A, KeyEvent.VK_F3, ActionEvent.SHIFT_MASK,
                    new CommandInformation(
		                    null, null, null, false, classTesterCMP,
		                    this.classTesterCMP.getClass().getMethod("testBatchClear", null)));
            fileMenuRoot.addSeparator();
            
            boolean autoGetChildren = ResourcesHandler.getUserSettingBoolean(ResourcesHandler.AUTO_GET_CHILDREN, true);
            addCheckBoxMenuItem(fileMenuRoot, ResourcesHandler.getNLSResource(ResourcesHandler.FILE_AUTOGETCHILDREN), KeyEvent.VK_G, autoGetChildren,
                    new CommandInformation(null, null, null, false, classTesterMisc, classTesterMisc.getClass().getMethod("toggleAutoGetChildren", null), true));
            boolean incrementalDeploy = ResourcesHandler.getUserSettingBoolean(ResourcesHandler.INCREMENTAL_DEPLOY, true);
            addCheckBoxMenuItem(fileMenuRoot, ResourcesHandler.getNLSResource(ResourcesHandler.FILE_ISDELTA), KeyEvent.VK_I, incrementalDeploy,
                    new CommandInformation(null, null, null, false, classTesterMisc, classTesterMisc.getClass().getMethod("toggleIsIncremental", null), true));
            showAdvancedProperties = ResourcesHandler.getUserSettingBoolean(ResourcesHandler.SHOW_ADVANCED_PROPERTIES, true);
            addCheckBoxMenuItem(fileMenuRoot, ResourcesHandler.getNLSResource(ResourcesHandler.FILE_SHOWADVANCED), KeyEvent.VK_A, showAdvancedProperties,
                    new CommandInformation(null, null, null, false, classTesterMisc, classTesterMisc.getClass().getMethod("toggleShowAdvancedProperties", null), true));
            boolean connectUsingPropertiesFile = ResourcesHandler.getUserSettingBoolean(ResourcesHandler.FILE_CONNECTUSINGPROPERTIESFILE, false);
            addCheckBoxMenuItem(fileMenuRoot, ResourcesHandler.getNLSResource(ResourcesHandler.FILE_CONNECTUSINGPROPERTIESFILE), KeyEvent.VK_P, connectUsingPropertiesFile,
                    new CommandInformation(null, null, null, false, classTesterMisc, classTesterMisc.getClass().getMethod("toggleConnectUsingPropertiesFile", null), true));
            mqTraceEnabledMenuItem = addCheckBoxMenuItem(fileMenuRoot, ResourcesHandler.getNLSResource(ResourcesHandler.FILE_MQJAVATRACE), KeyEvent.VK_J, classTesterMisc.mqTraceEnabled,
                    new CommandInformation(null, null, null, false, classTesterMisc, classTesterMisc.getClass().getMethod("toggleMQJavaTrace", null)));
            cmpTraceEnabledMenuItem = addCheckBoxMenuItem(fileMenuRoot, ResourcesHandler.getNLSResource(ResourcesHandler.FILE_CMP_TRACE), KeyEvent.VK_T, classTesterMisc.cmpTraceEnabled,
                    new CommandInformation(null, null, null, false, classTesterMisc, classTesterMisc.getClass().getMethod("toggleCMPTrace", null)));
            
            
            addMenuItem(fileMenuRoot, ResourcesHandler.getNLSResource(ResourcesHandler.FILE_RETRYCHARS), KeyEvent.VK_R,
                    new CommandInformation(
                            new String[] {
		                    ResourcesHandler.getNLSResource(ResourcesHandler.CONFIG_RETRY_TIME),
                            ResourcesHandler.getNLSResource(ResourcesHandler.DEPLOY_WAIT_TIME)} ,
		                    new String[] {
		                    ResourcesHandler.CONFIG_RETRY_TIME,
                            ResourcesHandler.DEPLOY_WAIT_TIME},
		                    new String[] { "10000", "10000" },
		                    false, classTesterCMP,
		                    this.classTesterCMP.getClass().getMethod("testRetryCharacteristics", new Class[] {Long.TYPE, Long.TYPE})));
            
            fileMenuRoot.addSeparator();
            
            addMenuItem(fileMenuRoot, ResourcesHandler.getNLSResource(ResourcesHandler.FILE_QUIT), KeyEvent.VK_X, KeyEvent.VK_F4, ActionEvent.ALT_MASK, new CommandInformation(null, null, null, false, this, this.getClass().getMethod("quit", null)));
            m.add(fileMenuRoot);

            // Automation ->
            automation = new JMenu(ResourcesHandler.getNLSResource(ResourcesHandler.AUTOMATION));
            automation.setMnemonic(KeyEvent.VK_A);
            addMenuItem(automation, ResourcesHandler.getNLSResource(ResourcesHandler.AUTOMATION_RECORD), KeyEvent.VK_R, KeyEvent.VK_R, ActionEvent.CTRL_MASK,
                    new CommandInformation(
                            ResourcesHandler.getNLSResource(ResourcesHandler.SELECT_SCRIPT_OUTPUT),
                            JFileChooser.SAVE_DIALOG, "xml", false, this, this.getClass().getMethod("startRecording", new Class[] {String.class}), false));
            
            addMenuItem(automation, ResourcesHandler.getNLSResource(ResourcesHandler.AUTOMATION_PAUSE), KeyEvent.VK_W,
                    new CommandInformation(
                            new String[] { ResourcesHandler.getNLSResource(ResourcesHandler.SCRIPT_WAIT_TIME)} ,
                            new String[] { ResourcesHandler.SCRIPT_WAIT_TIME } ,
                            new String[] { "5" },
		                    false, this, this.getClass().getMethod("pauseRecording", new Class[] {Integer.TYPE})));
            
            addMenuItem(automation, ResourcesHandler.getNLSResource(ResourcesHandler.AUTOMATION_PLAY), KeyEvent.VK_P, KeyEvent.VK_P, ActionEvent.CTRL_MASK,
                    new CommandInformation(
                            ResourcesHandler.getNLSResource(ResourcesHandler.SELECT_SCRIPT_INPUT),
                            JFileChooser.OPEN_DIALOG, "xml", false, this, this.getClass().getMethod("startPlaybackOnNewThread", new Class[] {String.class}), false));
            
            addMenuItem(automation, ResourcesHandler.getNLSResource(ResourcesHandler.AUTOMATION_STOP), KeyEvent.VK_S, KeyEvent.VK_S, ActionEvent.CTRL_MASK,
                    new CommandInformation(
		                    null, null, null, false, this, this.getClass().getMethod("stopRecording", null)));
            
            // Do not alter the format of the automation menu without also altering setAutomationMenuEnablement()!
            m.add(automation);
            setAutomationMenuEnablement();
            
            // Connect menu
            connectMenu = new JPopupMenu();
            connectContextSensitiveMenuItem = addMenuItem(connectMenu,
								                    ResourcesHandler.getNLSResource(ResourcesHandler.FILE_CONNECT),
								                    null);
            configureConnectAction(ResourcesHandler.getUserSettingBoolean(ResourcesHandler.FILE_CONNECTUSINGPROPERTIESFILE, false));

            
            // Uncomment the following line to prevent the tree from being expanded automatically
            expandAll(tree,true);
            
            tree.addTreeSelectionListener(guiEventListener);
            
            setupJTable(cmp);
            setAutomationMenuEnablement();
            
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                      validate();
                }
              });
            
        } catch (NoSuchMethodException e) {
            log(e);
        } finally {
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
        }
    }
    

    /**
     * Updates the status line to reflect the state of
     * the connection to the Configuration Manager, and
     * whether any outstanding deploys are required.
     */
    public void updateStatusLine() {    
        String text = "";
        if (cmp == null) {
            text = ResourcesHandler.getNLSResource(ResourcesHandler.DISCONNECTED);
        } else {
            
            if (topologyDeployRequired) {
                text = ResourcesHandler.getNLSResource(ResourcesHandler.TOPOLOGY_DEPLOY_REQUIRED);
            } else if (topicDeployRequired) {
                text = ResourcesHandler.getNLSResource(ResourcesHandler.TOPIC_DEPLOY_REQUIRED);
            } else if (brokerDeployRequired) {
                text = ResourcesHandler.getNLSResource(ResourcesHandler.BROKER_DEPLOY_REQUIRED);
            } else {
                String configManagerName = "";
                try {
                    configManagerName = cmp.getName();
                } catch (ConfigManagerProxyPropertyNotInitializedException e1) {
                    // Ignore this non-critical error.
                }
                text = ResourcesHandler.getNLSResource(ResourcesHandler.CONNECTED_TO_CM_ON, new String[] { configManagerName } );
            }
        }
        updateStatusLine(text);
    }
    
    /**
     * Updates the status line with the supplied text
     * @param newText Text to display
     */
    public void updateStatusLine(String newText) {
        if (statusLine != null) {
            statusLine.setText("  "+newText);
        }
    }


    /**
     * Tests listener registration. This method is called
     * when the Administered Object tree is being discovered.
     * AdministeredObject.registerListener() asks the Config
     * Manager Proxy to keep the CMP API Exerciser informed of
     * any changes to the object's state.
     * @param object AdministeredObject to register
     */
    private void registerListener(AdministeredObject object) {
        object.registerListener(exerciserAdministeredObjectListener); 
        log(ResourcesHandler.getNLSResource(ResourcesHandler.REGISTERED_LISTENER, new String[] { formatAdminObject(object)}));
    }


    /**
     * Starts recording to the specified file
     * @param fileName Name of the file to start recording to
     */
    public void startRecording(String fileName) {
        try {
            // Test a write the the file before any actions are written
            // (we want to fail *now* if the file is inaccessible).
            FileOutputStream recordFile = new FileOutputStream(new File(fileName));
            recordFile.close();
            
            // Everything appears to be OK- by setting the member variable 'scriptFileName',
            // subsequent actions will be written to the file when they are issued.
            log(ResourcesHandler.getNLSResource(ResourcesHandler.AUTOMATION_WARNING));
            log(ResourcesHandler.getNLSResource(ResourcesHandler.RECORDING_STARTED));
            scriptFileName = fileName;
            setAutomationMenuEnablement();
        } catch (Exception ex) {
            log(ex);
        }
    }

    /**
     * Stops recording
     */
    public void stopRecording() {
        try {
            if (scriptFileName != null) {
                
                // Write the currently stored actions to the file
                if (scriptOutputDoc != null) {
                    FileOutputStream recordFile = new FileOutputStream(new File(scriptFileName));

                    // Serialize the XML document representing our script and
                    // write it to the previously given file name.
                    OutputFormat format = new OutputFormat(scriptOutputDoc);
                    format.setIndenting(true);
                    StringWriter wrtr = new StringWriter();
                    XMLSerializer serial = new XMLSerializer(wrtr,format);
                    serial.asDOMSerializer();
                    serial.serialize(scriptOutputDoc.getDocumentElement());
                    recordFile.write(wrtr.toString().getBytes("UTF8"));
                    recordFile.close();
                } else {
                    // This is fine; no actions have been issued since
                    // starting the recording.
                }

                scriptFileName = null;
                scriptOutputDoc = null;
                
                setAutomationMenuEnablement();
                log(ResourcesHandler.getNLSResource(ResourcesHandler.RECORDING_STOPPED));
            }
        } catch (Exception ex) {
            log(ex);
        }
    }
    
    /**
     * Moves the inner component so that it appears in the center of the exerciser main window.
     * @param outer
     */
    public void centerComponentInExerciser(Component inner) {
        Point point = this.getLocationOnScreen();
        Dimension outerD = this.getSize();
        Dimension innerD = inner.getSize();
        Dimension screenD = inner.getToolkit().getScreenSize();
        if(!((point.x + outerD.width / 2) - innerD.width / 2 < 0 ||
             (point.y + outerD.height / 2) - innerD.height / 2 < 0 ||
             (point.x + outerD.width / 2) - innerD.width / 2 >= screenD.width - 10 ||
             (point.y + outerD.height / 2) - innerD.height / 2 >= screenD.height - 10)) {
            inner.setLocation(new Point((point.x + outerD.width / 2) - innerD.width / 2,
                                        (point.y + outerD.height / 2) - innerD.height / 2));
        }
    }
    
    /**
     * If a script is being played back, this method
     * pauses for the supplied number of seconds.
     * @param secsToPause Number of seconds pause to
     * insert into the script, or the number of
     * seconds to pause for. 
     */
    public void pauseRecording(int secsToPause) {   
        // If scriptFileName != null, then insert a pause into the script
        // (this will have been done already as part of the action to
        // invoke this method (issueAction()).
        // If scriptFileName == null, then we are playing back a script
        // and so we should pause now.
        if (scriptFileName == null) {
            try {
                Thread.sleep(secsToPause*1000);
            } catch (InterruptedException ex) {
                // Ignore
            }
        }
    }
    

    /**
     * Calls the startPlayback method on a new thread.
     * This method is called instead of 'startPlayback' when a script
     * is played back from the GUI menu. This is because commands
     * are invoked on the command thread, but the playback mechanism
     * requires sole use of the command thread in order to function;
     * not using another thread would lead to deadlock waiting for
     * the command thread to become free.
     * @param fileName
     */
    public void startPlaybackOnNewThread(final String fileName) {
        log(ResourcesHandler.getNLSResource(ResourcesHandler.PLAYBACK_INFO));
        new Thread(
            new Runnable() {
                public void run() {
                    startPlayback(fileName);
                }
            }
        ).start();
    }
    
    
    /**
     * Plays back a previously recorded file recording
     * @param fileName Name of the file to play
     */
    public void startPlayback(String fileName) {
        try {
            
            // Check that the file exists and contains text
            File f = new File(fileName);
            if (!f.exists()) {
                log(ResourcesHandler.getNLSResource(ResourcesHandler.PLAYBACK_FILE_NOT_FOUND, new String[] {fileName} ));
            } else if (!f.canRead()) {
                log(ResourcesHandler.getNLSResource(ResourcesHandler.PLAYBACK_FILE_NOT_READABLE, new String[] {fileName} ));
            } else if (f.length() == 0) {
                log(ResourcesHandler.getNLSResource(ResourcesHandler.PLAYBACK_FILE_EMPTY));
            } else {
                log(ResourcesHandler.getNLSResource(ResourcesHandler.PLAYBACK_STARTED));
                
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse (f);
                
                Element root = doc.getDocumentElement();
                String version = root.getAttribute(SCRIPTXML_PROGVER);
                if (!(SCRIPTXML_PROGVER_VALUE.equals(version))) {
                    log(ResourcesHandler.getNLSResource(ResourcesHandler.INCOMPATIBLE_SCRIPT_VERSION,
                            new String[] {fileName, version, SCRIPTXML_PROGVER_VALUE}));
                } else {
                    
                    // Go through each method in the script
                    NodeList methods = root.getElementsByTagName(SCRIPTXML_METHOD);
                    int numberOfMethods = methods.getLength();
                    for (int i=0; i<numberOfMethods; i++) {
                        
                        Element thisMethodNode = (Element) methods.item(i);
                        
                        // Get the method name from the XML
                        String methodName = thisMethodNode.getAttribute(SCRIPTXML_NAME);
                        
                        // Get the test owner
                        Object testOwner;
                        String testOwnerString = thisMethodNode.getAttribute(SCRIPTXML_OWNER);
                        if ("this".equals(testOwnerString)) {
                            testOwner = this;
                        } else {
                            Field testOwnerF = this.getClass().getField(testOwnerString);
                            testOwner = testOwnerF.get(this);
                        }
                        
                        // Enumerate through the supplied parameters
                        NodeList parameters = thisMethodNode.getElementsByTagName(SCRIPTXML_PARAMETER);
                        int numberOfParameters = parameters.getLength();
                        Class[] methodParameterTypes = new Class[numberOfParameters];
                        Object[] methodParameters = new Object[numberOfParameters];
                        
                        try {
                            for (int j=0; j<numberOfParameters; j++) {
                                Element thisParameter = (Element) parameters.item(j);
                                String type = thisParameter.getAttribute(SCRIPTXML_TYPE);
                                String value = thisParameter.getAttribute(SCRIPTXML_VALUE);
                                
                                // Derive the Class instance from the string describing it
                                methodParameterTypes[j] = getClassInstanceFromString(type);
                                
                                // Derive the parameter for the object based on its type
                                methodParameters[j] = getObjectFromStringRepresentation(value, methodParameterTypes[j]);
                            }
                                    
                            // At this point in the code, the following variables
                            // exactly describe the action to be invoked:
                            // methodName, testOwner, methodArgTypes, methodArgs
                            
                            // Work out the method from these input parameters
                            Method method = testOwner.getClass().getMethod(methodName, methodParameterTypes);
                            
                            // Add the method to the command queue (cause it to be invoked)
                            commandThread.enqueueCommand(method, testOwner, methodParameters, false, true);
                        } catch (IllegalArgumentException ex) {
                            // Thrown by getObjectFromStringRepresentation
                            log(ResourcesHandler.getNLSResource(ResourcesHandler.COMMAND_IGNORED, new String[] {methodName}));
                        }
                        
                        // Wait for the command to finish. We need to do this, in case the next
                        // command we want to invoke requires an AdministeredObject parameter
                        // which is yet to be created by this current command.
                        while (commandThread.isBusy()) {
                            Thread.sleep(200);
                        }
                        
                    }
                    log(ResourcesHandler.getNLSResource(ResourcesHandler.PLAYBACK_FINISHED));
                    
                }
            }
        } catch (Exception ex) {
            log(ex);
        }
    }
    
    /**
     * Returns an instance of the Class object that is described by the
     * supplied string.
     * @param type A class name, primitive type name or the literal
     * String "this".
     * @return Class object, or null if and only if the type could
     * not be determined.
     */
    private Class getClassInstanceFromString(String type) {
        Class retVal = null;
        
        if ("int".equals(type)) {
            retVal = Integer.TYPE;
        } else if ("boolean".equals(type)) {
            retVal = Boolean.TYPE;
        } else if ("long".equals(type)) {
            retVal = Long.TYPE;
        } else if ("char".equals(type)) {
            retVal = Boolean.TYPE;
        } else if ("short".equals(type)) {
            retVal = Short.TYPE;
        } else if ("float".equals(type)) {
            retVal = Float.TYPE;
        } else if ("double".equals(type)) {
            retVal = Double.TYPE;
        } else if ("byte".equals(type)) {
            retVal = Byte.TYPE;
        } else if ("this".equals(type)) {
            retVal = this.getClass();
        } else {
            try {
                retVal = Class.forName(type);
            } catch (ClassNotFoundException ex) {
                log(ex);
            }
        }
        return retVal;
    }


    /**
     * Convenience method to display "action submitted" message
     * when a test method completes successfully.
     */
    protected void reportActionSubmitted() {
        if (cmp != null) {
            if ((cmp.isBatching()) && (guiEventListener.activeCommandInformation.canBeBatched)) {
                log(ResourcesHandler.getNLSResource(ResourcesHandler.ACTION_ADDED_TO_BATCH));
            } else {
                log(ResourcesHandler.getNLSResource(ResourcesHandler.ACTION_SENT_TO_CM));
            }
        }
    }

    /**
     * Returns an AdministeredObject handle given a string of the form
     * "ConfigManager/PubSubTopology/broker1"
     * @param identifyingString
     * @return AdministeredObject or null if and only if the object could
     * still not be located after a ten second wait.
     * @throws IllegalArgumentException if the object to which the
     * identifying string refers could not be found (or was not
     * accessible) in the hierarchy. 
     */
    private AdministeredObject getAdministeredObjectFromString(String identifyingString)
    throws IllegalArgumentException {
        AdministeredObject retVal = null;
        boolean finished = false;
        int retries = 0;
        
        if (!identifyingString.equals("null")) {
	        while (!finished) {
	            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) identifyingStringToNodes.get(identifyingString);
	            if (dmtn != null) {
	                retVal = (AdministeredObject) administeredObjects.get(dmtn);
	                selectAdministeredObject(retVal);
	                finished = true;
	            } else {
	                // If the node is not available, it could be
	                // that a previous action in the script has
	                // not returned from the Configuration Manager yet.
	                // Wait a while for the reply to come back.
	                retries++;
	                try {
	                    Thread.sleep(1000);
	                } catch (InterruptedException e) {
	                    // ignore
	                }
	                if (retries > 9) {
	                    finished = true;
	                    log(ResourcesHandler.getNLSResource(ResourcesHandler.OBJECT_UNAVAILABLE, new String[] {identifyingString}));
                        throw new IllegalArgumentException(identifyingString+" not available");
	                }
	            }
	        }
        }
        return retVal;
    }


    /**
     * Sets the value of the connected ConfigManagerProxy instance.
     * Used if the application wishes to connect to a different
     * Configuration Manager
     * @param cmp ConfigManagerProxy value
     */
    public void setConfigManagerProxyConnectedInstance(ConfigManagerProxy cmp) {
        this.cmp = cmp;
    }
    
    /**
     * Exits the application.
     */
    public void quit() {
        stopRecording();
        classTesterCMP.testDisconnect();
        dispose();
        System.exit(0);
    }


    /**
     * Sets up the "Connect" action to correctly
     * reflect the value of the "connect using properties file"
     * parameter.
     * @param newValue true if and only if the user wishes
     * to connect using *.configmgr properties files.
     */
    public void configureConnectAction(boolean newValue) {
        CommandInformation ci = null;
        try {
	        if (newValue) {
	            ci = new CommandInformation(
	                    ResourcesHandler.getNLSResource(ResourcesHandler.SELECT_CONFIGMGR_FILE),
	                            JFileChooser.OPEN_DIALOG, "configmgr", false, classTesterCMP, classTesterCMP.getClass().getMethod("testConnect", new Class[] {String.class}), false);
	        } else {
	            ci = new CommandInformation(
	                new String[] { 
	                ResourcesHandler.getNLSResource(ResourcesHandler.HOSTNAME),
	                ResourcesHandler.getNLSResource(ResourcesHandler.PORT),
	                ResourcesHandler.getNLSResource(ResourcesHandler.QMGR),
	                ResourcesHandler.getNLSResource(ResourcesHandler.SECEXIT_CLASS),
	                ResourcesHandler.getNLSResource(ResourcesHandler.SECEXIT_URL)},
	                new String[] { 
	                ResourcesHandler.HOSTNAME,
	                ResourcesHandler.PORT,
	                ResourcesHandler.QMGR,
	                ResourcesHandler.SECEXIT_CLASS,
	                ResourcesHandler.SECEXIT_URL},
	                new String[] { "localhost", "1414", "", "", "" },
	                false,
	                classTesterCMP,
	                this.classTesterCMP.getClass().getMethod("testConnect", new Class[] {String.class, Integer.TYPE, String.class, String.class, String.class})
	                );
	        }
        } catch (NoSuchMethodException ex) {
            log(ex);
        }
        
        // Change the connect menu items to point to the new command
        if (connectMenuItem != null) {
            mappingOfMenuItemsToCommands.put(connectMenuItem, ci);
        }
        if (connectContextSensitiveMenuItem != null) {
            mappingOfMenuItemsToCommands.put(connectContextSensitiveMenuItem, ci);
        }
        
    }


    /**
     * Sends to the log information on a deployment.
     * @param ds DeployResult object that contains the results
     * of a previously submitted deployment
     */
    public synchronized void reportDeployResult(DeployResult ds) {
        
        log("");
        if (ds == null) {
            log("DeployResult == null");
        } else {
            // Display overall completion code
            log("DeployResult.getCompletionCode() = " + ds.getCompletionCode());
            
            // Display overall log messages
            Enumeration logEntries = ds.getLogEntries();
            while (logEntries.hasMoreElements()) {
                LogEntry le = (LogEntry)logEntries.nextElement();
                log("DeployResult.getLogEntries() : " + getFirstLine(le.getDetail()));
            }
            
            // Display broker specific information
            Enumeration e = ds.getDeployedBrokers();
            while (e.hasMoreElements()) {
                
                // Completion code
                BrokerProxy b = (BrokerProxy)e.nextElement();
                log("DeployResult.getCompletionCodeForBroker(<"+b+">) = "+ds.getCompletionCodeForBroker(b));
                
                // Log entries
                Enumeration e2 = ds.getLogEntriesForBroker(b);
                while (e2.hasMoreElements()) {
                    LogEntry le = (LogEntry)e2.nextElement();
                    log("DeployResult.getLogEntriesForBroker(<"+b+">) : "+getFirstLine(le.getDetail()));
                }
            }
        }
        log("");
        
    }


    /**
     * Returns the supplied string, up to (and excluding) the first newline character.
     * If the supplied string is null, the output string is also null.
     * @param string String to parse.
     * @return String the first line of the input string.
     */
    protected static String getFirstLine(String string) {
        String retVal = null;
        if (string != null) {
            int firstNewline = string.indexOf('\n');
            if (firstNewline != -1) {
                retVal = string.substring(0, firstNewline);
            } else {
                retVal = string;
            }
        }
        return retVal;
    }
    
    /**
     * Returns a locale-specific string that represents the
     * supplied GregorianCalendar
     * @param gc
     * @return String
     */
    protected static String formatGC(GregorianCalendar gc) {
        String retVal;
        if (gc != null) {
            retVal = formatDate(gc.getTime());
        } else {
            retVal = "null";
        }
        return retVal;
    }
    
    /**
     * Returns a locale-specific string that represents the
     * supplied GregorianCalendar
     * @param gc
     * @return String
     */
    protected static String formatDate(Date date) {
        
        String retVal;
        DateFormat df = DateFormat.getDateTimeInstance();
        if (date != null) {
            retVal = df.format(date);
        } else {
            retVal = "null";
        }
        return retVal;
    }
    
    /**
     * Returns a string that represents the
     * supplied AdministeredObject
     * @param obj
     * @return String
     */
    protected static String formatAdminObject(AdministeredObject obj) {
        String retVal;
        if (obj != null) {
            retVal = "<"+obj+">";
        } else {
            retVal = "null";
        }
        return retVal;
    }
    
}


/**
 * Small data structure that describes the action to
 * take when a menu item is selected.
 */
class CommandInformation {
    
    String[] labels = null;
    String[] userSettingsKeyNames = null;
    String[] defaults = null;
    int parametersRequired = 0;
    boolean canBeBatched = false;
    boolean showFileDialog = false;
    boolean suppressEntryExitLogMessages = false;
    String fileDialogTitle = null;
    int fileDialogType = 0;
    String fileTypeFilter = null;
    Method methodToInvoke = null;
    Object classTesterObject = null;
    
    /**
     * Describes a set of text field parameters and a method to invoke.
     * Assumes that log entry and exit messages will be displayed when
     * the command is invoked.
     * @param labels string array, each element containing a label for a required parameter
     * @param userSettingsKeyNames string array, each element containing the key name to use if the user entered value is to you want to store the user entered value in the settings file
     * @param defaults string array, each element containing the default value for a required parameter
     * @param canBeBatched boolean describing whether the command can be added to a batch (cmp.beginUpdates())
     * @param classTesterObject the object whose method will be invoked
     * @param methodToInvoke the Method to be invoked when the command is submitted
     */
    public CommandInformation(String[] labels, String[] userSettingsKeyNames, String[] defaults, boolean canBeBatched,
            Object classTesterObject, Method methodToInvoke) {
        this.labels = labels;
        this.userSettingsKeyNames = userSettingsKeyNames;
        this.defaults = defaults;
        this.canBeBatched = canBeBatched;
        this.showFileDialog = false;
        this.methodToInvoke = methodToInvoke;
        this.classTesterObject = classTesterObject;
        if (labels != null) {
            parametersRequired = labels.length;
        }
    }
    
    /**
     * Describes a set of text field parameters and a method to invoke
     * @param labels string array, each element containing a label for a required parameter
     * @param userSettingsKeyNames string array, each element containing the key name to use if the user entered value is to you want to store the user entered value in the settings file
     * @param defaults string array, each element containing the default value for a required parameter
     * @param canBeBatched boolean describing whether the command can be added to a batch (cmp.beginUpdates())
     * @param classTesterObject the object whose method will be invoked
     * @param methodToInvoke the Method to be invoked when the command is submitted
     * @param suppressEntryExitLogMessages True if and only if the log messages displayed
     * when the action is invoked should be hidden.
     */
    public CommandInformation(String[] labels, String[] userSettingsKeyNames, String[] defaults, boolean canBeBatched,
            Object classTesterObject, Method methodToInvoke, boolean suppressEntryExitLogMessages) {
        this.labels = labels;
        this.userSettingsKeyNames = userSettingsKeyNames;
        this.defaults = defaults;
        this.canBeBatched = canBeBatched;
        this.showFileDialog = false;
        this.methodToInvoke = methodToInvoke;
        this.classTesterObject = classTesterObject;
        if (labels != null) {
            parametersRequired = labels.length;
        }
        this.suppressEntryExitLogMessages = suppressEntryExitLogMessages;
    }
    
    /**
     * Describes a file chooser and a method to invoke
     * @param fileDialogTitle Title of the file choose
     * @param fileDialogType Type of the chooser -
     * JFileChooser.OPEN_DIALOG or JFileChooser.SAVE_DIALOG
     * @param fileTypeFilter The file filter to apply (e.g. "jpg")
     * @param canBeBatched True iff the command can be added to
     * a batch (cmp.beginUpdates() etc.)
     * @param classTesterObject the object whose method will be invoked
     * @param methodToInvoke The method to invoke when the command is submitted
     * @param suppressEntryExitLogMessages True if and only if the log messages displayed
     * when the action is invoked should be hidden.
     */
    public CommandInformation(String fileDialogTitle, int fileDialogType, String fileTypeFilter, boolean canBeBatched,
            Object classTesterObject, Method methodToInvoke, boolean suppressEntryExitLogMessages) {
        this.fileDialogTitle = fileDialogTitle;
        this.fileDialogType = fileDialogType;
        this.fileTypeFilter = fileTypeFilter;
        this.canBeBatched = canBeBatched;
        this.showFileDialog = true;
        this.classTesterObject = classTesterObject;
        this.methodToInvoke = methodToInvoke;
        this.suppressEntryExitLogMessages = suppressEntryExitLogMessages;
        parametersRequired = 0;
    }
    
}
