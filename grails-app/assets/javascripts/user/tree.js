/*!
 * tree.js
 * Dynamic tree view control, with support for lazy loading of branches.
 * https://github.com/mar10/fancytree/
 *
 * Manipulation d'un tree fancytree
 *
 */


/**
 * Enregistre l'état du tree dans le composant inputId
 * @param treeId
 * @param inputId
 */
function saveTree(treeId, inputId) {
	var tree = $('#' + treeId).fancytree("getTree");
	var datas = tree.toDict(true);
	$('#' + inputId).val(JSON.stringify(datas));
}



/**
 * Renomme le node sélectionné
 * @param treeId
 */
function tree_renommer(treeId) {
	var node = $('#' + treeId).fancytree("getActiveNode");
	node.editStart();
}


/**
 * Supprime le node et ses descendants
 * @param treeId
 */
function tree_supprimer(treeId) {
	var node = $('#' + treeId).fancytree("getActiveNode");
	node.parent.folder = (node.parent.countChildren() > 1);
	node.remove();
}

/**
 * Supprime les noeuds descendants
 * @param treeId
 */
function tree_supprimerChildren(treeId) {
	var node = $('#' + treeId).fancytree("getActiveNode");
	node.removeChildren();
	node.folder = false;
}

/**
 * Supprime les noeuds descendants
 * @param treeId
 * @param mode
 */
function tree_ajouter(treeId, mode) {
	var node = $('#' + treeId).fancytree("getActiveNode");
	var tree = $('#' + treeId).fancytree("getTree");
	
	var newNode = {
		title: 'Nouveau noeud',
	};
	
	if (node) {
		newNode = node.addNode(newNode, mode);
		node.setExpanded();
	} else {
		newNode = tree.getRootNode().addNode(newNode, 'child');
	}
}


function fancyTree() {
	
	$(".fancytree").each(function() {
		var content = $(this).html().trim();
		$(this).html('');
		var activate = window[$(this).attr('activate')];
		var extensions = null;
		var ondblclick = window[$(this).attr('ondblclick')];
		
		if ($(this).attr('editable') == 'true') {
			extensions =  ["dnd", "edit", "wide"];
		} else {
			extensions =  ["wide"];
		}
		
		if (content == '') {
			content = '[{"title": "Nouveau"}]';
		}
		
		$(this).fancytree({
			
			activate: activate,
			extensions: extensions,
			dblclick: ondblclick,
			
			createNode: function(event, data) {
				var node = data.node;
				node.parent.folder = node.parent.hasChildren();
				node.parent.renderStatus();
			},
			
			edit: {
				triggerStart: ["f2", "dblclick"],
				edit: function(event, data) {
					data.input.select();
				},
			},
			
			dnd: {
				autoExpandMS: 400,
				focusOnClick: true,
				preventVoidMoves: true, // Prevent dropping nodes 'before self', etc.
				preventRecursiveMoves: true, // Prevent dropping nodes on own descendants
				
				dragStart: function(node, data) {
					/** This function MUST be defined to enable dragging for the tree.
					 *  Return false to cancel dragging of node.
					 */
					return true;
				},
				
				dragEnter: function(node, data) {
					/** data.otherNode may be null for non-fancytree droppables.
					 *  Return false to disallow dropping on node. In this case
					 *  dragOver and dragLeave are not called.
					 *  Return 'over', 'before, or 'after' to force a hitMode.
					 *  Return ['before', 'after'] to restrict available hitModes.
					 *  Any other return value will calc the hitMode from the cursor position.
					 */
					// Prevent dropping a parent below another parent (only sort
					// nodes under the same parent)
					//if(node.parent !== data.otherNode.parent){
					//	return false;
					//}
					// Don't allow dropping *over* a node (would create a child)
					//return ["before", "after"];

				   return true;
				},
				
				dragDrop: function(node, data) {
					/** This function MUST be defined to enable dropping of items on
					 *  the tree.
					 */
					data.otherNode.moveTo(node, data.hitMode);
				}
			},
			
			source: JSON.parse(content),
		});
	});
}



