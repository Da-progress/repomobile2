package ch.sofa.lodo.data.services.dtos;

import ch.sofa.lodo.data.models.User;

public class PlayerRegistrationDto {

	private String message;
	private boolean duplicate;
	private User registeredPlayer;

	public User getRegisteredPlayer() {
		return registeredPlayer;
	}

	public void setRegisteredPlayer(User registredPlayer) {
		this.registeredPlayer = registredPlayer;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isDuplicate() {
		return duplicate;
	}

	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}
}
