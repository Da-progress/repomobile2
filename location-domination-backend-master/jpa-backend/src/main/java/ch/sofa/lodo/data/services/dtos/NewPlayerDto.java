package ch.sofa.lodo.data.services.dtos;

/**
 * Also used as login dto
 */
public class NewPlayerDto {

	private String username;
	private String password;
	private String mobileNr;
	private String firebaseUserDeviceToken;

	public String getMobileNr() {
		return mobileNr;
	}

	public void setMobileNr(String mobileNr) {
		this.mobileNr = mobileNr;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirebaseUserDeviceToken() {
		return firebaseUserDeviceToken;
	}

	public void setFirebaseUserDeviceToken(String firebaseUserDeviceToken) {
		this.firebaseUserDeviceToken = firebaseUserDeviceToken;
	}
}
