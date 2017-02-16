package wsc.ecj.gp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterables;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.gp.GPTree;
import ec.simple.SimpleFitness;
import ec.util.Parameter;
import wsc.graph.ServiceGraph;

public class WSCIndividual extends GPIndividual {

	private static final long serialVersionUID = 1L;

	public WSCIndividual() {
		super();
		super.fitness = new SimpleFitness();
		super.species = new WSCSpecies();
	}

	public WSCIndividual(GPNode root) {
		super();
		super.fitness = new SimpleFitness();
		super.species = new WSCSpecies();
		super.trees = new GPTree[1];
		GPTree t = new GPTree();
		super.trees[0] = t;/** the root GPNode in the GPTree */
		t.child = root;
	}

	public WSCIndividual(GPNode root, ServiceGraph graph) {
		super();
		super.fitness = new SimpleFitness();
		super.species = new WSCSpecies();
		super.trees = new GPTree[1];
		GPTree t = new GPTree();
		super.trees[0] = t;/** the root GPNode in the GPTree */
		t.child = root;
	}

	@Override
	public Parameter defaultBase() {
		return new Parameter("wscindividual");
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof WSCIndividual) {
			return toString().equals(other.toString());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("digraph tree { ");
		builder.append(trees[0].child.toString());
		builder.append("}");
		return builder.toString();
	}

	@Override
	public WSCIndividual clone() {
		WSCIndividual wsci = new WSCIndividual((GPNode) super.trees[0].child.clone());
		wsci.fitness = (SimpleFitness) fitness.clone();
		wsci.species = species;
		return wsci;
	}

	// Get FiltedTreeNodes not including startNodes and endNodes
	public List<GPNode> getFiltedTreeNodes() {
		List<GPNode> allNodes = new ArrayList<GPNode>();
		AddFiltedChildNodes(trees[0].child, allNodes);

		List<GPNode> removedNodeList = new ArrayList<GPNode>();
		for (int i = 0; i < allNodes.size(); i++) {
			GPNode filteredChild = allNodes.get(i);
			if (filteredChild instanceof ServiceGPNode) {
				ServiceGPNode sgp = (ServiceGPNode) filteredChild;
				if (sgp.getSerName().equals("startNode")) {
					// remove startNode
					removedNodeList.add(allNodes.get(i));
				}
				if (sgp.getSerName().equals("endNode")) {
					// remove endNode
					removedNodeList.add(allNodes.get(i));
				}
			}
		}

		allNodes.removeAll(removedNodeList);

		return allNodes;
	}

	// Get FiltedTreeNodes not including startNodes and endNodes
	public List<GPNode> getAllStartNodes() {
		List<GPNode> allNodes = new ArrayList<GPNode>();
		AddStartNodes(trees[0].child, allNodes);
		return allNodes;
	}

	public List<GPNode> AddFiltedChildNodes(GPNode gpChild, List<GPNode> allNodes) {

		GPNode current = gpChild;
		allNodes.add(current);
		if (current.children != null) {
			for (GPNode child : current.children)
				AddChildNodes(child, allNodes);
		}
		return allNodes;

	}

	// Get AllTreeNodes

	public List<GPNode> getAllTreeNodes() {
		List<GPNode> allNodes = new ArrayList<GPNode>();
		AddChildNodes(trees[0].child, allNodes);

		return allNodes;
	}

	// Get All Nodes from GPNode

	public List<GPNode> getAllTreeNodes(GPNode gpNode) {
		List<GPNode> allNodes = new ArrayList<GPNode>();
		// AddChildNodes(trees[0].child, allNodes);
		AddChildNodes(gpNode, allNodes);

		return allNodes;
	}

	public List<GPNode> AddChildNodes(GPNode gpChild, List<GPNode> allNodes) {

		GPNode current = gpChild;
		allNodes.add(current);
		if (current.children != null) {
			for (GPNode child : current.children)
				AddChildNodes(child, allNodes);
		}
		return allNodes;

	}

	public List<GPNode> AddStartNodes(GPNode gpChild, List<GPNode> allNodes) {

		GPNode current = gpChild;
		if (((ServiceGPNode) current).getSerName() == "endNode") {
			allNodes.add(current);
		}
		if (current.children != null) {
			for (GPNode child : current.children)
				AddStartNodes(child, allNodes);
		}
		return allNodes;

	}

	// Replace the GPNodes and associated semantic edges
	public void replaceNode4Crossover(GPNode node, GPNode replacement) {
		// Perform replacement if neither node is not null
		if (node != null && replacement != null) {

			replacement = (GPNode) replacement.clone();

			GPNode parentNode = (GPNode) node.parent;

			replacement.parent = node.parent;
			for (int i = 0; i < parentNode.children.length; i++) {
				if (parentNode.children[i] == node) {
					parentNode.children[i] = replacement;
					// wonder whether to break while considering the
					// redundant nodes in the tree transfered from the graph
					break;
				}
			}

		}
	}

	public void replaceNode4Mutation(GPNode node, GPNode replacement) {
		// Perform replacement if neither node is not null
		if (node != null && replacement != null) {
			// clone replacement
			replacement = (GPNode) replacement.clone();

			// GPNode[] replacementList = replacement.children.clone();

			boolean isParentofStartNode = false;
			for (GPNode child : node.children) {
				if (((ServiceGPNode) child).getSerName() == "startNode") {
					isParentofStartNode = true;
					break;
				}
			}

			if (isParentofStartNode == false) {
				GPNode pNode = (GPNode) node.parent;

				// obtain the appedixNode to tailed to the deleted endNode in
				// replacement
				GPNode[] appedixNode = node.children;

				// find the endNode in replacement
				List<GPNode> endNodeList = new ArrayList<GPNode>();
				List<GPNode> allNodeofReplacement = this.getAllTreeNodes(replacement);
				for (GPNode gpn : allNodeofReplacement) {
					if (gpn instanceof ServiceGPNode) {
						if (((ServiceGPNode) gpn).getSerName().equals("startNode")) {
							endNodeList.add(gpn);
						}
					}
				}

				// replace the endNode with appedixNode
				// replaceNode(endNode, appedixNode);
				for (GPNode endNode : endNodeList) {

					GPNode parentEndNode = (GPNode) endNode.parent;

					for (GPNode appedix : appedixNode) {
						appedix.parent = endNode.parent;
					}

					for (int i = 0; i < parentEndNode.children.length; i++) {
						parentEndNode.children = appedixNode;
						break;
						// wonder whether to break while considering the
						// redundant nodes in the tree transfered from the
						// graph
					}

				}

				// replace replacement in the graph
				GPNode[] childOfReplacement = replacement.children;
				// point all children's parent to parent

				for (GPNode childofRep : childOfReplacement) {
					childofRep.parent = pNode;
				}

				// point all parent's children to children
				Iterator<GPNode> childOfNodeIter = Iterables
						.concat(Arrays.asList(pNode.children), Arrays.asList(childOfReplacement)).iterator();

				List<GPNode> copy = new ArrayList<GPNode>();

				while (childOfNodeIter.hasNext()) {
					GPNode childOfNode = childOfNodeIter.next();
					if (childOfNode != node) {
						copy.add(childOfNode);
					}
				}

				GPNode[] allChildOfNode = new GPNode[pNode.children.length + childOfReplacement.length - 1];
				for (int i = 0; i < copy.size(); i++) {
					allChildOfNode[i] = copy.get(i);
				}

				pNode.children = allChildOfNode;
			} else {
				GPNode pNode = (GPNode) node.parent;
				GPNode[] childOfReplacement = replacement.children;

				// point all children's parent to parent
				for (GPNode childofRep : childOfReplacement) {
					childofRep.parent = pNode;
				}

				// point all parent's children to children
				Iterator<GPNode> childOfNodeIter = Iterables
						.concat(Arrays.asList(pNode.children), Arrays.asList(childOfReplacement)).iterator();

				List<GPNode> copy = new ArrayList<GPNode>();

				while (childOfNodeIter.hasNext()) {
					GPNode childOfNode = childOfNodeIter.next();
					if (childOfNode != node) {
						copy.add(childOfNode);
					}
				}

				GPNode[] allChildOfNode = new GPNode[pNode.children.length + childOfReplacement.length - 1];
				for (int i = 0; i < copy.size(); i++) {
					allChildOfNode[i] = copy.get(i);
				}

				pNode.children = allChildOfNode;

			}

		}
	}
}
