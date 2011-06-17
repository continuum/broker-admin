/**
 * Web Broker Administrator
 * menu for nodes
 *
 * @author    Jorge Rodriguez Suarez
 * @copyright (c) Continuum 2008.
 * @version   $Id$
 *
 * All right reserved by Continuum
 * 
 */
 

BrokerAdmin.nodeAjaxAction = function (method, p, callbackMsg) {
	Ext.Ajax.request({
		url : 'main/brokeradmin/' + method,
		params : p,
		success : function (response, params) {
			Ext.Msg.show({
				title: 'Information',
				msg : callbackMsg,
				buttons: Ext.MessageBox.OK,
				icon: Ext.MessageBox.INFO
			});
		}
	});
}

BrokerAdmin.showDeployResult = function (dr) {
	var log = "";
	Ext.each(dr.log, function (l) {
		log = log.concat(l.detail + "\n");
	});
	var win = new Ext.Window({
		title : dr.completionCode,
		items:[
			new Ext.Panel({
				title : 'Log detail :',
				width:400,
				html : log
			})
		],
		buttons : [
			{
				text : "OK",
				handler : function () {
					win.close();
				}
			}
		]
	});
	win.show();
}

// constant literal, map between node type and menu items 
BrokerAdmin.nodeTypeMenu = {
	configuration_manager : [
		{
			text : '"Cancel Pendings Deployments"',
			handler : function () {
				Ext.Ajax.request({
					url : 'main/brokeradmin/cancelCMDeployment',
					params : {
						x_name : BrokerAdmin.tree.selectedNode.attributes.x_name,
						timeout : 30000 // 30 secs
					},
					success : function (response, params) {
						var json = Ext.util.JSON.decode(response.responseText);
						BrokerAdmin.showDeployResult(json.deployResult);
					}
				});
			}
		},
		{
			text : '"New Broker"',
			handler : function () {
				var form = BrokerAdmin.asociateBrokerForm('development', BrokerAdmin.tree.selectedNode, function (response) {
					var json = Ext.util.JSON.decode(response.responseText);
					BrokerAdmin.showDeployResult(json.deployResult);
				});
			}
		},
		{
			text : '"Grant Access"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"List Access"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		}
	],
	topology : [
		{
			text : '"New Broker"',
			handler : function () {
				var form = BrokerAdmin.asociateBrokerForm('development', BrokerAdmin.tree.selectedNode.parentNode, function (response) {
					var json = Ext.util.JSON.decode(response.responseText);
					BrokerAdmin.showDeployResult(json.deployResult);
				});
			}
		},
		{
			text : '"Remove a broker deleted already"',
			handler : function () {
				var form = BrokerAdmin.deleteBrokerForm('development', BrokerAdmin.tree.selectedNode, function (response) {
					var json = Ext.util.JSON.decode(response.responseText);
					BrokerAdmin.showDeployResult(json.deployResult);
				});
			}
		},
		{
			text : '"Grant Access"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"List Access"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		}
	],
	subscription : [
	],
	topic : [
	],
	log : [
		{
			text : '"Clean Log"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		}
	],
	broker : [
		{
			text : '"De-Asociate Broker"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"List Access"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"Grant Accesd"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"New Execution Group"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"Start Flows"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"Stop Flows"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"Delete All Execution Flows"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"Deploy Broker Config"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"Cancel all pending deployments"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		}
	],
	executionGroup : [
		{
			text : '"Deploy BAR"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"Start Flows"',
			handler : function () {
				BrokerAdmin.nodeAjaxAction('startMsgFlows',
					{
						x_cmp : BrokerAdmin.tree.selectedNode.attributes.x_cmp,
						x_broker : BrokerAdmin.tree.selectedNode.attributes.x_broker,
						x_name : BrokerAdmin.tree.selectedNode.attributes.x_name
					}, 'Se ha enviado la peticion para ejecutar todos los flujos'
				);
			}
		},
		{
			text : '"Stop Flows"',
			handler : function () {
				BrokerAdmin.nodeAjaxAction('stopMsgFlows',
					{
						x_cmp : BrokerAdmin.tree.selectedNode.attributes.x_cmp,
						x_broker : BrokerAdmin.tree.selectedNode.attributes.x_broker,
						x_name : BrokerAdmin.tree.selectedNode.attributes.x_name
					}, 'Se ha enviado la peticion para detener todos los flujos'
				);
			}
		},
		{
			text : '"Delete Execution Flow"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"List Access"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"Grant Access"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		}
	],
	messageFlow : [
		{
			text : '"Delete Flow"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"Start Flow"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		},
		{
			text : '"Stop Flow"',
			handler : function () {
				Ext.Msg.alert('Atencion', 'Metodo no implementado');
			}
		}
	]
}

BrokerAdmin.nodeMenu = new Ext.menu.Menu({
    id: 'mainMenu',
    items: [
			{
				text : 'Refresh',
				handler : function () {
					if (BrokerAdmin.tree.selectedNode) {
						if (!BrokerAdmin.tree.selectedNode.leaf) { BrokerAdmin.tree.selectedNode.reload() };
					} else {
						Ext.Msg.alert('Atencion', 'Debe seleccionar un nodo del arbol primero.');
					}
				}
			}
    ]
});

BrokerAdmin.nodeMenu.refreshMenu = function (node) {
	// clean nodeMenu
	BrokerAdmin.nodeMenu.removeAll();
	// all have Refresh
	BrokerAdmin.nodeMenu.add(			{
		text : 'Refresh',
		handler : function () {
			if (node) {
				if (! node.leaf) { node.reload() };
			} else {
				Ext.Msg.alert('Atencion', 'Debe seleccionar un nodo del arbol primero.');
			}
		}
	});
	// add all items related to nodeType
	if (node.attributes.x_class) {
		var menuItems = BrokerAdmin.nodeTypeMenu[node.attributes.x_class] || []
		for (var i = 0; i < menuItems.length; i++) {
			BrokerAdmin.nodeMenu.add(menuItems[i]);
		}
	}
};
