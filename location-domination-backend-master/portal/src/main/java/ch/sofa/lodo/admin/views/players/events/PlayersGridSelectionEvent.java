package ch.sofa.lodo.admin.views.players.events;

import ch.sofa.lodo.data.models.User;

public class PlayersGridSelectionEvent {

	public static final String PLAYER_SELECTION_TOPIC = "playerSelectionTopic";
	private User instance;

	public PlayersGridSelectionEvent(User instance) {
		super();
		this.instance = instance;
	}

	public User getInstance() {
		return instance;
	}
}
