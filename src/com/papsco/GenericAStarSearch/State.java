package com.papsco.GenericAStarSearch;
import java.util.ArrayList;

public class State {
	private ArrayList<Node> path;
	public State(ArrayList<Node> path) {
		this.path = path;
	}
	public float computeCost() { //computes g+h
		float val = 0;
		for (int i = 0; i < path.size() - 1; i++) {
			val = val + path.get(i).getCostOf(path.get(i + 1)); //g value
		}
		val = val + path.get(path.size() - 1).getHeuristic(); //h value
		return val;
	}
	public ArrayList<State> expand() {
		ArrayList<State> childPaths = new ArrayList<State>();
		for (int i = 0; i < path.get(path.size() - 1).getChildren().size(); i++) { //iterates through the children of the last node in the sequence
			ArrayList<Node> f_path = new ArrayList<Node>();
			f_path.addAll(path);
			f_path.add(path.get(path.size() - 1).getChildren().get(i));
			path.get(path.size() - 1).getChildren().get(i).onExpand();
			childPaths.add(new State(f_path));
		}
		return childPaths;
	}
	public boolean isGoalState() {
		return path.get(path.size() - 1).isGoalNode();
	}
	public ArrayList<Node> getPath() {
		return path;
	}
}
