/**
 * Web Broker Administrator
 * brokeradmin.layout.js
 *
 * @author    Jorge Rodriguez Suarez
 * @copyright (c) Continuum 2008.
 * @version   $Id$
 *
 * All right reserved by Continuum
 * 
 */
 
/*global Ext, brokeradmin */
 
Ext.BLANK_IMAGE_URL = 'ext-2.2/resources/images/default/s.gif'
 
Ext.ns('BrokerAdmin');

// more timeout for ajax response
Ext.Ajax.timeout = 120000;
// other functions, like ajax global exceptions
Ext.Ajax.on('requestexception', function (conn, xhr, options) {
	Ext.Msg.show({
		title: 'Error del servidor',
		msg : Ext.util.JSON.decode(xhr.responseText).errorMsg,
		buttons: Ext.MessageBox.OK,
		icon: Ext.MessageBox.ERROR
	});
});
	
// application main entry point
Ext.onReady(function() {
 
	// NOTE: This is an example showing simple state management. During development,
	// it is generally best to disable state management as dynamically-generated ids
	// can change across page loads, leading to unpredictable results.  The developer
	// should ensure that stable state ids are set for stateful components in real apps.
	
	//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
	
	Ext.QuickTips.init();
	
	// turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';
	
	// top region
	BrokerAdmin.north = new Ext.Panel({
		region : 'north',
		title  : 'Broker and MQSeries Manager',
		height : 32,
		collapsible : true,
		collapsed : true,
		border : true
	});
	
	BrokerAdmin.southTabPanel = new Ext.TabPanel({
		border : true,
		activeTab:0,
		tabPosition:'top'
	})
	
	// south region
	BrokerAdmin.south = new Ext.Panel({
		region : 'south',
		title  : '...',
		split  : true,
		height : 100,
		minSize : 100,
		maxSize : 200,
		collapsible : true,
		margins :'0 0 0 0',
		layout : 'fit',
		autoTabs : true,
		border : false,
		items : BrokerAdmin.southTabPanel
	});
	
	// grid de propiedades
	BrokerAdmin.propertiesGrid = new Ext.grid.PropertyGrid({
		title : "Propiedades"
	});
	
	// east region
	BrokerAdmin.east = new Ext.Panel({
		region : 'east',
		collapsible: true,
		split :true,
		width : 225,
		minSize : 175,
		maxSize : 400,
		margins :'0 5 0 0',
		layout : 'fit',
		autoTabs : true,
		items : new Ext.TabPanel({
			border:false,
			activeTab:0,
			tabPosition:'bottom',
			items:[BrokerAdmin.propertiesGrid]
		})
	});
	
	
	// west region
	BrokerAdmin.west = new Ext.Panel({
		region : 'west',
		layout : 'fit',
		title  : ' ',
		split:true,
		width: 200,
		minSize : 175,
		maxSize : 400,
		collapsible : true,
		margins :'0 0 0 5'
	});
	
	// Broker Tab
	BrokerAdmin.managerTab = new Ext.TabPanel({
		border:false,
		activeTab:0,
		tabPosition:'top'
	});
	
	// add to east
	BrokerAdmin.west.add(BrokerAdmin.managerTab);
	
	// generic function to create listener for nodes in the tree
	BrokerAdmin.treeLoaderBeforeLoad = function (enviroment) {
		return function (treeLoader, node) {
			if (node.attributes.x_class) {
				treeLoader.dataUrl = "main/brokeradmin/" + node.attributes.x_mvc_method + "?enviroment=" + enviroment;
				for (var attr in node.attributes) {
					if (attr.charAt(0) === 'x') {
						this.baseParams[attr] = node.attributes[attr];
					};
				}; // end for
			} // end if
		};
	}
	
	// TODO temporal, el arbol debe cargarse cuando se baje el accordion Desarrollo, QA, Produccion
	Ext.Msg.alert('Status', 'Cargando el arbol de Configuration Managers');
	
	BrokerAdmin.msgLoading = Ext.MessageBox.show({
	    title: 'Inicializando...',
	    msg: 'Cargando los configuration managers...',
	    progressText: 'Inicializando...',
	    width:300,
	    progress:true,
	    closable:false
	});
	
	// this hideous block creates the bogus progress
	var f = function (v) {
	     return function () {
	         if (v == 30) {
	             Ext.MessageBox.hide();
	         } else {
	             var i = v/29;
	             Ext.MessageBox.updateProgress(i, Math.round(100*i)+'% completed');
	         }
	    };
	};
	
	for (var i = 1; i < 30; i++){
	    setTimeout(f(i), i*500);
	}
	
	BrokerAdmin.tree = new Ext.tree.TreePanel({
		id: 'tree-panel',
		region: 'center',
		margins: '2 2 0 2',
		autoScroll: true,
		border : false,
		rootVisible: false,
		tbar : [
			{
	            text:'Menu',
	            iconCls: 'bmenu',  // <-- icon
	            menu: BrokerAdmin.nodeMenu  // assign menu by instance
	        },
			{
				text : 'Add',
				handler : function (btn, event) {
					BrokerAdmin.createCMForm('development');
				}
			}
		],
		root: new Ext.tree.AsyncTreeNode({
			text : "Configuration managers",
			loader : new Ext.tree.TreeLoader({
				dataUrl : 'main/brokeradmin/managers?enviroment=development',
				listeners : {
					"beforeload" : BrokerAdmin.treeLoaderBeforeLoad('development'),
					"load" : function (treeLoader, node, response) {
						BrokerAdmin.msgLoading.hide();
					}
				}
			}),
		}),
		listeners: {
		    'click' : function (node, event) {
		    	// mostrar las propiedades del nodo
		    	if (node.attributes.properties) {
		    		BrokerAdmin.propertiesGrid.setSource(node.attributes.properties);
		    	}
		    	// set the selected node (for refresh from here)
		    	BrokerAdmin.tree.selectedNode = node;
		    	// refresh the context menu
		    	BrokerAdmin.nodeMenu.refreshMenu(node);
			},
			'dblclick' : function (node, event) {
				if (node.attributes.x_class === 'log') {
					// crear log
					var logTabId = 'tabLog' + node.parentNode.text;
					var logGrid = undefined;
					if (!BrokerAdmin.southTabPanel.getItem(logTabId)) {
						// create a task that periodically refresh the log
						var task = {
							run : function () {
								// agregar los items al log
								Ext.getCmp(logTabId).store.reload();
							},
							interval : 30000
						};
						// create the grid
						logGrid = new Ext.grid.GridPanel({
					        title : 'Log - ' + node.parentNode.text,
					        id  : logTabId,
					        closable : true,
					        autoScroll : true,
					        listeners : {
						        "beforedestroy" : function (panel) {
						        	// destroy the periodical task
						        	Ext.TaskMgr.stop(task);
						        }
					        },    
					        viewConfig: {
								forceFit: true
							},
					        store : new Ext.data.JsonStore({
							    url: 'main/brokeradmin/log',
							    root: 'entries',
							    baseParams : {
							    	manager : node.parentNode.attributes.x_name
							    },
							    fields: ['message', 'bip', 'isError', 'source', 'timestamp', 'detail']
							}),
							columns: [
							    {id:'message', header: "Mensaje", width: 200, sortable: true, dataIndex: 'message'},
							    {header: "BIP", width: 120, sortable: true, dataIndex: 'bip'},
							    {header: "Error", width: 50, sortable: true, dataIndex: 'isError'},
							    {header: "Fuente", width: 120, sortable: true, dataIndex: 'source'},
							    {header: "Fecha", width: 135, sortable: true, renderer: Ext.util.Format.dateRenderer('d/m/Y H:i:s'), dataIndex: 'timestamp'},
							    {header: "Detalle", width: 135, dataIndex: 'detail'}
							]
						});
						// add the panel and set active
						BrokerAdmin.southTabPanel.add(logGrid);
						BrokerAdmin.southTabPanel.setActiveTab(logGrid);
						// start the task of refresh the log panel
						Ext.TaskMgr.start(task);
					} else {
						BrokerAdmin.southTabPanel.setActiveTab(logGrid);
					}
				}
			}
		}
	});
	
	// domains accordion
	BrokerAdmin.brokerDomain = new Ext.Panel({
		title : 'Dominios Broker',
		layout:'fit',
		border : false,
		layoutConfig:{
		    animate:true
		},
		items: [BrokerAdmin.tree]
	});
	
	// add to BrokerTab
	BrokerAdmin.managerTab.add(BrokerAdmin.brokerDomain);
	
	
	// FIN DE TODO
		
	// domains accordion
	BrokerAdmin.mqSeries = new Ext.Panel({
		title : 'MQ Series',
		layout:'accordion',
		layoutConfig:{
		    animate:true
		},
		items: [{
		    title:'Desarrollo',
		    border:false,
		    iconCls:'nav'
		},{
		    title:'QA',
		    border:false,
		    iconCls:'settings'
		},{
		    title:'Produccion',
		    border:false,
		    iconCls:'settings'
		}]
	});
	
	// add to BrokerTab
	BrokerAdmin.managerTab.add(BrokerAdmin.mqSeries);
		
	// center region
	BrokerAdmin.center = new Ext.TabPanel({
	    region:'center',
	    border : false
	})
	
	// window region
	BrokerAdmin.viewPort = new Ext.Viewport({
		layout : 'border',
		border : true,
		items : [BrokerAdmin.north, BrokerAdmin.south, BrokerAdmin.east, BrokerAdmin.west, BrokerAdmin.center]
	});
		
}); // eo function onReady
 
// eof