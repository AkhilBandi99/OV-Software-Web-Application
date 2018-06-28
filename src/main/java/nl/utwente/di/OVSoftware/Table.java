package nl.utwente.di.OVSoftware;

public class Table {

	private final String name;
	private final String login;
	private final String user;
	private final String pass;
	
	public Table(String n, String l, String u, String p) {
		name = n;
		login = l;
		user = u;
		pass = p;
	}

	public String getName() {
		return name;
	}
	
	public String getUser() {
		return user;
	}

	public String getLogin() {
		return login;
	}
	
	public String getPass() {
		return pass;
	}
	
	@Override
	public String toString() {
		return getName() + " " + getUser() + " " + getLogin() + " " + getPass();
	}
	
}
