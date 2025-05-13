package ch.sofa.lodo.data.constants;

@SuppressWarnings("unused")
public enum UserType {

	PORTAL_USER(0),
	PLAYER(1);

	private int dbValue;

	UserType(int asInt) {
		this.dbValue = asInt;
	}

	public static UserType getByDbValue(int dbValue) {
		for (UserType userType : values())
			if (userType.getDbValue() == dbValue)
				return userType;

		return null;
	}

	public int getDbValue() {
		return dbValue;
	}

	public void setDbValue(int asInt) {
		this.dbValue = asInt;
	}
}
