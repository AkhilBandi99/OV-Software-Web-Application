package nl.utwente.di.OVSoftware;

import java.util.HashMap;
import java.util.Set;

public class userMaps {
	private HashMap<String, String> users = new HashMap<>();
	
	public userMaps() {
		this.newUser("Akhil", "Bandi");
	}
	
	public void newUser(String username, String password) {
		users.put(username, password);
	}

	public boolean findUser(String username, String password) {
		Set<String> usernames = users.keySet();
		for(String userName: usernames) {
			if(username.equals(userName)) {
				if(users.get(username).equals(password)) {
					return true;
				} else {
					break;
				}
			} else {
				break;
			}
		}
		return false;
	}
}
