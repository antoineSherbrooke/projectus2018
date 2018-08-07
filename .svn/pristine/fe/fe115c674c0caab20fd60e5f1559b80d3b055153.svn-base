package models;

public enum ConnectResult {
	OK (""),
	FIRST_LOGIN ("The user has no password yet"),
    E_PASSWORD ("Invalid password"),
    E_INACTIVE ("The user is not active, please contact the administrator"),
    E_CIP ("Invalid cip"),
	E_HASH ("Hash corrupted or impossible to compute");

	private final String message;

	ConnectResult(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
