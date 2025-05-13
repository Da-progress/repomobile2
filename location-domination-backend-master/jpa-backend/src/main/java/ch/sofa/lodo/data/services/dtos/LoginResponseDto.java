package ch.sofa.lodo.data.services.dtos;

import ch.sofa.lodo.data.models.User;

public class LoginResponseDto {

	private boolean success;
	private User player;
	private String errorMessage;
	private int countLogin;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public User getPlayer() {
		return player;
	}

	public void setPlayer(User player) {
		this.player = player;
	}

	public int getCountLogin() {
		return countLogin;
	}

	public void setCountLogin(int countLogin) {
		this.countLogin = countLogin;
	}
}
