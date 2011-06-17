/**
 * Web Broker Administrator
 *
 * xml Tree
 *
 * @author    Jorge Rodriguez Suarez
 * @copyright (c) Continuum 2008.
 * @version   $Id$
 *
 * All right reserved by Continuum
 * 
 */
 
 /**
 * @class Ext.ux.XmlTreeLoader
 * @extends Ext.tree.TreeLoader
 * <p>A TreeLoader that can convert an XML document into a hierarchy of {@link Ext.tree.TreeNode}s.
 * Any text value included as a text node in the XML will be added to the parent node as an attribute
 * called <tt>innerText</tt>.  Also, the tag name of each XML node will be added to the tree node as
 * an attribute called <tt>tagName</tt>.</p>
 * <p>By default, this class expects that your source XML will provide the necessary attributes on each 
 * node as expected by the {@link Ext.tree.TreePanel} to display and load properly.  However, you can
 * provide your own custom processing of node attributes by overriding the {@link #processNode} method
 * and modifying the attributes as needed before they are used to create the associated TreeNode.</p>
 * @constructor
 * Creates a new XmlTreeloader.
 * @param {Object} config A config object containing config properties.
 */
Ext.ux.XmlTreeLoader = Ext.extend(Ext.tree.TreeLoader, {
    /**
     * @property  XML_NODE_ELEMENT
     * XML element node (value 1, read-only)
     * @type Number
     */
    XML_NODE_ELEMENT : 1,
    /**
     * @property  XML_NODE_TEXT
     * XML text node (value 3, read-only)
     * @type Number
     */
    XML_NODE_TEXT : 3,
    
    // private override
    processResponse : function(response, node, callback){
        var json = response.responseText;
        var root = json.root || json;
        
        try {
            node.beginUpdate();
            node.appendChild(this.parseJson(root));
            node.endUpdate();
            
            if (typeof callback == "function") {
                callback(this, node);
            }
        } catch(e) {
            this.handleFailure(response);
        }
    },
    
    // private
    parseJson : function(node) {
    	var nodes = [];
    	Ext.each(node.childs, function (ch) {
    		if (ch.leaf) {
    			return ch;
    		} else if (ch.branch) {
    			var treeNode = this.createNode(ch);
    			if (ch.childs && ch.childs.length) {
    				var child = this.parseJson(ch);
    				treeNode.appendChild(child);
    			}
    		}
    	});
    },
    
    // private
    /*
    parseXml : function(node) {
        var nodes = [];
        Ext.each(node.childNodes, function(n){
            if(n.nodeType == this.XML_NODE_ELEMENT){
                var treeNode = this.createNode(n);
                if(n.childNodes.length > 0){
                    var child = this.parseXml(n);
                    if (typeof child == 'string') {
                        treeNode.attributes.innerText = child;
                    } else {
                        treeNode.appendChild(child);
                    }
                }
                nodes.push(treeNode);
            }
            else if(n.nodeType == this.XML_NODE_TEXT) {
                var text = n.nodeValue.trim();
                if(text.length > 0){
                    return nodes = text;
                }
            }
        }, this);
        
        return nodes;
    },
    */
    
    // private override
    createNode : function (node) {
    	this.processAttributes(node);
    	return new Ext.tree.AsyncTreeNode(node)
    },
    
    // private override
    /*
    createNode : function(node) {
    
    	var attr = {
            tagName: node.tagName,
            expandable : true,
			loader : new BrokerAdmin.DomainTreeLoader({
				url :'main/brokeradmin/node'
			})
    	}
        
        Ext.each(node.attributes, function(a){
            attr[a.nodeName] = a.nodeValue;
        });
    
        this.processAttributes(attr);
        
        return new Ext.tree.AsyncTreeNode(attr); //Ext.ux.XmlTreeLoader.superclass.createNode.call(this, attr);
    },
    */
    
    /*
     * Template method intended to be overridden by subclasses that need to provide
     * custom attribute processing prior to the creation of each TreeNode.  This method
     * will be passed a config object containing existing TreeNode attribute name/value
     * pairs which can be modified as needed directly (no need to return the object).
     */
    processAttributes: Ext.emptyFn
});

/*
 * Broker Domain Tree
 */
BrokerAdmin.DomainTreeLoader = Ext.extend(Ext.ux.XmlTreeLoader, {
    processAttributes : function(attr){
    
        if (attr.name){ // is it an domain node?
            
            // Set the node text that will show in the tree since our raw data does not include a text attribute:
            attr.text = attr.name + ' [ ' + attr.ip + ' ] ';
            
            // Author icon, using the gender flag to choose a specific icon:
            attr.iconCls = attr.class + '-icon';
            
            // Override these values for our folder nodes because we are loading all data at once. If we were
            // loading each node asynchronously (the default) we would not want to do this:
            attr.loaded = true;
            //attr.expanded = true;
        } else if (attr.title) { // is it a broker node?
            
            // Set the node text that will show in the tree since our raw data does not include a text attribute:
            attr.text = attr.title + ' (' + attr.published + ')';
            
            // Book icon:
            attr.iconCls = 'book';
            
            // Tell the tree this is a leaf node.  This could also be passed as an attribute in the original XML,
            // but this example demonstrates that you can control this even when you cannot dictate the format of 
            // the incoming source XML:
            attr.leaf = true;
        }
    }
});


 