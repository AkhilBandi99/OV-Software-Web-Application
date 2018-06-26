package nl.utwente.di.OVSoftware;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OVAccount {
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@JsonCreator
	public OVAccount(@JsonProperty("username") String username, @JsonProperty("password") String password) {
		this.username = username;
		this.password = password;
	}
}
