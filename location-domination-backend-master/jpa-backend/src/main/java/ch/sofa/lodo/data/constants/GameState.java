package ch.sofa.lodo.data.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("unused")
public enum GameState {

	OPEN(0, "Open"),
	PLAYED(1, "Played"),
	RECORDED(2, "Recorded"),
	PLANNED(3, "Planned"),
	INVALID(4, "Invalid");

	private Integer orderId;
	private String name;

	GameState(Integer orderId, String name) {
		this.name = name;
		this.orderId = orderId;
	}

	public static List<GameState> getStates() {
		List<GameState> states = new ArrayList<>();
		Collections.addAll(states, values());
		states.sort(Comparator.comparing(GameState::getOrderId));
		return states;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
}
