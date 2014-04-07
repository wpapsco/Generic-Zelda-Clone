package com.papsco.GenericAStarSearch;
import java.util.ArrayList;

public abstract class Node { //your classes will extend node. you must generate child nodes and child node costs. ArrayLists of child nodes and costs MUST correspond.
	private ArrayList<Integer> childNodeCosts;
	private ArrayList<Node> childNodes;
	private float heuristic;
	public abstract void onExpand();
	/**
	 * @return if this node is the or a goal state.
	 */
	public abstract boolean isGoalNode();
	/**
	 * Note: generateChildren MUST set the childNodes, childNodeCosts, and the heuristic value
	 */
	public abstract void generateChildren();
	/**
	 * @param child a child of the node
	 * @return the cost to move to that node
	 */
	public int getCostOf(Node child) {
		return childNodeCosts.get(childNodes.indexOf(child));
	}
	public int getCost(int child) {
		return childNodeCosts.get(child);
	}
	public ArrayList<Integer> getChildNodeCosts() {
		return childNodeCosts;
	}
	public float getHeuristic() {
		if (childNodes == null) {
			generateChildren();
		}
		return heuristic;
	}
	public ArrayList<Node> getChildren() {
		if (childNodes == null) {
			generateChildren();
		}
		return childNodes;
	}
	public void setChildNodeCosts(ArrayList<Integer> costs) {
		childNodeCosts = costs;
	}
	public void setChildren(ArrayList<Node> children) {
		childNodes = children;
	}
	public void setHeuristic(float h) {
		heuristic = h;
	}
}