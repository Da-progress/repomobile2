package ch.sofa.lodo.admin.views.games.events;

import ch.sofa.lodo.data.models.Game;

public class GameGridOnSelectedEvent {

	private Game game;

	public GameGridOnSelectedEvent(Game game) {
		super();
		this.game = game;
	}

	public Game getGame() {
		return game;
	}
}
