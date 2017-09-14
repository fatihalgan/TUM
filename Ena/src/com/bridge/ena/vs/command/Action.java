package com.bridge.ena.vs.command;

public enum Action {

	Commit("commit"),
	Rollback("rollback");
	
	private final String action;
	
	private Action(String action) {
		this.action = action;
	}
	
	public String toString() {
		return action;
	}
}
