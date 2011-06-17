/**
 * Web Broker Administrator
 * Layout libs
 *
 * @author    Jorge Rodriguez Suarez
 * @copyright (c) Continuum 2008.
 * @version   $Id$
 *
 * All right reserved by Continuum
 * 
 */

BrokerAdmin.createCMForm = function (enviroment) {

	if (!cmAddForm) {
	
       	var form = new Ext.form.FormPanel({
	        width : 360,
	        autoHeight : true,
	        frame : true,
	        defaultType: 'textfield',
       		items : [
	       		{
	                fieldLabel: 'Nombre (Amigable)',
	                name: 'name',
	                width:190,
	                allowBlank : false
                }, 
                {
                    fieldLabel: 'Host (o IP)',
                    name: 'host',
                    width:190,
                    allowBlank : false
                },
                {
                    fieldLabel: 'Puerto',
                    name: 'port',
                    width:190,
                    allowBlank : false
                },
                {
                    fieldLabel: 'Exit Class',
                    name: 'exitClass',
                    width:190
                },
                {
                    fieldLabel: 'Exit URL',
                    name: 'exitUrl',
                    width:190
                }
			],
			baseParams : { enviroment : enviroment },
			method : 'POST',
			timeout : 120,
			url :'main/brokeradmin/addCM',
			buttons : [
				{
					text : "Agregar",
					handler : function () {
						// si todo ok
						if (form.getForm().isValid()) {
				            form.getForm().submit({
				            	clientValidation : true,
				            	waitMsg :'Conectando al Configuration Manager...',
								success : function (form, action) {
									Ext.Msg.alert('Status', 'Configuration Manager agregado con exito!');
									// esconder el formulario
									cmAddForm.close();
									// agergar nuevo nodo al arbol
									var treeNode = new Ext.tree.AsyncTreeNode(Ext.util.JSON.decode(action.response.responseText));
									treeNode.loader = new Ext.tree.TreeLoader({
										dataUrl : 'main/brokeradmin/managers?enviroment' + enviroment,
										listeners : {
											"beforeload" : BrokerAdmin.treeLoaderBeforeLoad(enviroment)
										}
									});
									BrokerAdmin.tree.getRootNode().appendChild(treeNode);
								},
								failure : function (form, action) {
									Ext.Msg.show({
										title: 'Error de conexion',
										msg : Ext.util.JSON.decode(action.response.responseText).errorMsg,
										buttons: Ext.MessageBox.OK,
										icon: Ext.MessageBox.ERROR
									});								
								}
				            }) // end form.getForm().submit(
						}
					} // end handler
				} // end button Agregar
			] // end buttons
       	}) // end Ext.form.FormPanel
	
	    var cmAddForm = new Ext.Window({
   	        width : 360,
   	        autoHeight:true,
	        title:'Add one configuration manager',
	        layout : 'fit',
	        plain : true,
	        items: form
	    }); // end new Ext.Window
	} // end if (!cmdAddForm)

    cmAddForm.show();
};

// create a broker asociation
BrokerAdmin.asociateBrokerForm = function (enviroment, node, callback) {

	if (!asociateBrokerForm) {
	
       	var form = new Ext.form.FormPanel({
	        width : 360,
	        autoHeight : true,
	        autoWitdh : true,
	        frame : true,
	        defaultType: 'textfield',
			region : "north",
       		items : [
	       		{
	                fieldLabel: 'Broker Name',
	                name: 'name',
	                width:190,
	                allowBlank : false
                },
                {
                    fieldLabel: 'Queue Manager (Name)',
                    name: 'queueManager',
                    width:190,
	                allowBlank : false
                },
                {
                    fieldLabel: 'Execution Group Name',
                    emptyText : 'default',
                    name: 'execGroupName',
                    width:190
                }
			],
			baseParams : { enviroment : enviroment },
			method : 'POST',
			timeout : 120,
			url :'main/brokeradmin/addBrokerToTopology',
			buttons : [
				{
					text : "Add",
					handler : function () {
						// if there is not errors in the client
						if (form.getForm().isValid()) {
						
				            form.getForm().submit({
				            	clientValidation : true,
				            	waitMsg :'Creating the broker asociation...',
				            	params : {
				            		manager : node.attributes.x_name,
				            		timeout : 30000 // 30 secs
				            	},
								success : function (form, action) {
									// call callback
									if (callback) { 
										callback(action.response); 
									}
									// Ext.Msg.alert('Status', 'Broker associated succesfully !');
									// esconder el formulario
									asociateBrokerForm.close();
									// reload node
									node.reload();
								},
								failure : function (form, action) {
									Ext.Msg.show({
										title: 'Error on configuration manager...',
										msg : Ext.util.JSON.decode(action.response.responseText).errorMsg,
										buttons: Ext.MessageBox.OK,
										icon: Ext.MessageBox.ERROR
									});								
								}
				            }) // end form.getForm().submit(
						}
					} // end handler
				} // end button Agregar
			] // end buttons
       	}) // end Ext.form.FormPanel
	
	    var asociateBrokerForm = new Ext.Window({
   	        width : 360,
   	        autoHeight:true,
	        title:'Add a broker asociation',
	        layout : 'fit',
	        plain : true,
	        items: [
	        	new Ext.Panel({
			        width : 360,
			        autoHeight : true,
			        autoWitdh : true,
			        region : "south",
			        frame : true,
			        html : "Asks the Configuration Manager to add a broker with the supplied name and queue manager to the Topology workspace. It is assumed that the Broker has already been created using e.g. <b>mqsicreatebroker</b>. Note that an execution group will be created with the name you supply or 'default'"
	        	}),
	        	form
	        ]
	    }); // end new Ext.Window
	} // end if (!cmdAddForm)

    asociateBrokerForm.show();
};

// delete a broker
BrokerAdmin.deleteBrokerForm = function (enviroment, node, callback) {

	if (!removeDeletedBrokerForm) {
	
       	var form = new Ext.form.FormPanel({
	        width : 360,
	        autoHeight : true,
	        autoWitdh : true,
	        frame : true,
	        defaultType: 'textfield',
			region : "north",
       		items : [
	       		{
	                fieldLabel: 'Broker Name',
	                name: 'name',
	                width:190,
	                allowBlank : false
                }
			],
			baseParams : { enviroment : enviroment },
			method : 'POST',
			timeout : 120,
			url :'main/brokeradmin/removeDeletedBrokerFromTopology',
			buttons : [
				{
					text : "Remove",
					handler : function () {
						// if there is not errors in the client
						if (form.getForm().isValid()) {
						
				            form.getForm().submit({
				            	clientValidation : true,
				            	waitMsg :'Removing the broker asociation prematurely deleted...',
				            	params : {
				            		manager : node.attributes.x_name,
				            		timeout : 30000 // 30 secs
				            	},
								success : function (form, action) {
									// call callback
									if (callback) { 
										callback(action.response); 
									}
									Ext.Msg.alert('Status', 'Broker removed succesfully !');
									// esconder el formulario
									removeDeletedBrokerForm.close();
									// reload node
									node.reload();
								},
								failure : function (form, action) {
									Ext.Msg.show({
										title: 'Error on configuration manager...',
										msg : Ext.util.JSON.decode(action.response.responseText).errorMsg,
										buttons: Ext.MessageBox.OK,
										icon: Ext.MessageBox.ERROR
									});								
								}
				            }) // end form.getForm().submit(
						}
					} // end handler
				} // end button Agregar
			] // end buttons
       	}) // end Ext.form.FormPanel
	
	    var removeDeletedBrokerForm = new Ext.Window({
   	        width : 360,
   	        autoHeight:true,
	        title:'Delete a broker asociation',
	        layout : 'fit',
	        plain : true,
	        items: [
	        	new Ext.Panel({
			        width : 360,
			        autoHeight : true,
			        autoWitdh : true,
			        region : "south",
			        frame : true,
			        html : "The 'remove a deleted broker' option only exists to clean any references to the broker from the Configuration Manager repository if the broker component has been prematurely deleted. Invoking this method against a running broker will make it unmanageable from the Configuration Manager and all Configuration Manager Proxy applications, including the toolkit.  If you have a BrokerProxy reference to the broker, you must discard it after calling this method.\n\n" +
							"If the connected Configuration Manager is of a version earlier than v6, this method will have the same effect as calling the standard deleteBroker(). If the broker component has already been deleted on a domain controlled by one of these Configuration Managers, you must delete the broker's SYSTEM.BROKER.ADMIN.QUEUE and then redeploy the topology in order for the reference to be tidied from the repository"
	        	}),
	        	form
	        ]
	    }); // end new Ext.Window
	} // end if (!cmdAddForm)

    asociateBrokerForm.show();
}