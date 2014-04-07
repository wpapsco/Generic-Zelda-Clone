package com.papsco.GenericAStarSearch;
import java.util.ArrayList;

public class StateController {
	public static State search(Node start) {
		ArrayList<State> fringe = new ArrayList<State>();
		boolean goalExpanded = false;
		ArrayList<Node> a = new ArrayList<Node>();
		a.add(start);
		fringe.add(new State(a));
		State lowestState = fringe.get(0);
		while(!goalExpanded) {
			for (int i = 0; i < fringe.size(); i++) {
				if (fringe.get(i).computeCost() < lowestState.computeCost()) {
					lowestState = fringe.get(i);
				}
			}
//			if (lowestState.getPath().contains(goalNode)) {
//				goalExpanded = true;
//				break;
//			}
			if (lowestState.isGoalState()) {
				goalExpanded = true;
				break;
			}
			fringe.addAll(lowestState.expand());
			fringe.remove(lowestState);
			lowestState = fringe.get(0);
		}
		return lowestState;
	}
}
