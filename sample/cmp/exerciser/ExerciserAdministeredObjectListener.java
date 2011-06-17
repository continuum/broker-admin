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
import java.util.ListIterator;
import java.util.Properties;

import cmp.common.ResourcesHandler;

import com.ibm.broker.config.proxy.CompletionCodeType;
import com.ibm.broker.config.proxy.AdministeredObject;
import com.ibm.broker.config.proxy.AdministeredObjectListener;
import com.ibm.broker.config.proxy.LogEntry;

/*****************************************************************************
 * <P>The Exerciser application registers an instance of this class
 * with the Configuration Manager Proxy for every AdministeredObject it learns about.
 *
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>ExerciserAdministeredObjectListener</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
 *     <LI>Receives notifications from the Configuration Manager Proxy when
 *     AdministeredObjects are modified or deleted, or when an action
 *     involving them is completed.
 *     </UL>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Internal Collaborators</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     <UL>
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
 * @version Samples/ConfigManagerProxy/cmp/exerciser/ExerciserAdministeredObjectListener.java, Config.Proxy, S000, S000-L50818.2 1.7
 *****************************************************************************/
public class ExerciserAdministeredObjectListener implements AdministeredObjectListener {
    
    /**
     * Pointer to the exerciser object
     * that provides the tracing services
     */
    private CMPAPIExerciser exerciser;

    /**
     * @param exerciser
     */
    public ExerciserAdministeredObjectListener(CMPAPIExerciser exerciser) {
        this.exerciser = exerciser;
    }

    /**
     * States that the Configuration Manager has processed a request
     * that previously originated from the current connection
     * to the Configuration Manager. The parameters of this method
     * call indicate the result of the command that was sent,
     * and the original command for reference.
     * @param affectedObject The object on which a command was
     * attempted.
     * @param ccType The overall completion code of the action
     * @param bipMessages an unmodifiable list of
     * com.ibm.broker.config.proxy.LogEntry classes that contains
     * any localized BIP Messages associated with the action.
     * @param referenceProperties Properties of the Request that
     * caused this Action Response.
     */
    public void processActionResponse( AdministeredObject affectedObject,
            CompletionCodeType ccType,
            java.util.List bipMessages,
            Properties referenceProperties) {

        exerciser.log("");
        exerciser.log("----> "+getClass().getName()+".processActionResponse(...)");
        exerciser.log("affectedObject = "+affectedObject);
        exerciser.log("completionCode = "+ccType);

        // Display any BIPs
        ListIterator msgs = bipMessages.listIterator();
        while (msgs.hasNext()) {
            LogEntry log = (LogEntry) msgs.next();
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.LOG_ENTRY)+" "+log);
        }

        // Display the reference properties
        Enumeration e = referenceProperties.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = referenceProperties.getProperty(key);
            exerciser.log (ResourcesHandler.getNLSResource(ResourcesHandler.REFERENCE_PROPERTY)+" "+key+"="+value);
        }
        
        exerciser.log("<---- "+getClass().getName()+".processActionResponse()");
    }
    
    /**
     * States the the supplied Configuration Manager Administered Object
     * has been deleted on the server.
     * @param deletedObject AdministeredObject which has been
     * deleted.
     */
    public void processDelete( AdministeredObject deletedObject ) {

        exerciser.log("");
        exerciser.log("----> "+getClass().getName()+".processDelete(...)");
        exerciser.log("deletedObject="+deletedObject);
        
        // If the current object is the selected one then update the properties
        if (deletedObject == exerciser.selectedAdministeredObject) {
            exerciser.selectedAdministeredObject = exerciser.getConnectedConfigManagerProxyInstance();
            exerciser.tree.clearSelection();
            exerciser.setupJTable(exerciser.selectedAdministeredObject);
        }

        exerciser.log("<---- "+getClass().getName()+".processDelete()");
    }
    
    
    /**
     * States that the supplied Configuration Manager Administered Object
     * has been modified by the current or another application.
     * @param affectedObject The object which has changed. The
     * attributes of the object will already have been updated
     * to contain the new information.
     * @param changedAttributes list containing the attribute
     * key names that have changed.
     * @param newSubcomponents list containing the object's
     * subcomponents which were added by the latest change.
     * Each entry is of the form "componenttype+UUID"
     * such as "Broker+123-123-123". A list of valid component
     * types can be found in the typedef-enumeration
     * com.ibm.broker.config.common.ConfigurationObjectType.
     * @param removedSubcomponents List containing the object's
     * subcomponents which were removed by the latest change.
     * Same format as for newChildren.
     */
    public void processModify( AdministeredObject affectedObject,
            java.util.List changedAttributes,
            java.util.List newSubcomponents,
            java.util.List removedSubcomponents ) {

        exerciser.log("");
        exerciser.log("----> "+getClass().getName()+".processModify(...)");
        exerciser.log("affectedObject = "+affectedObject);

        // Display changed attributes
        ListIterator e1 = changedAttributes.listIterator();
        while (e1.hasNext()) {
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.CHANGED_ATTRIBUTE)+" "+e1.next());
        }
    
        // Display new subcomponents
        ListIterator e2 = newSubcomponents.listIterator();
        while (e2.hasNext()) {
            String representation = (String) e2.next();
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.NEW_SUBCOMPONENT)+" "+representation);
            
            // NOTE: If you want to get a handle to the AdministeredObject
            // just added (that is, the one described by 'representation', use:
            //     AdministeredObject obj = affectedObject.getManagedSubcomponentFromStringRepresentation(representation);
            // You can cast 'obj' to the expected subclass.
            
        }
    
        // Display removed subcomponents
        ListIterator e3 = removedSubcomponents.listIterator();
        while (e3.hasNext()) {
            String representation = (String) e3.next();
            exerciser.log(ResourcesHandler.getNLSResource(ResourcesHandler.REMOVED_SUBCOMPONENT)+" "+representation);  
        }
        
        exerciser.initialiseTreeForAdministeredObject(affectedObject, false);

        // If the current object is the selected one then update the properties
        if (affectedObject == exerciser.selectedAdministeredObject) {
            exerciser.setupJTable(exerciser.selectedAdministeredObject);
        }

        exerciser.log("<---- "+getClass().getName()+".processModify()");
    }
    
}
