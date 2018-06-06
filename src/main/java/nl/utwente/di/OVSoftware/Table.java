package nl.utwente.di.OVSoftware;

public class Table {

	private final String name;
	private final String login;
	
	public Table(String n, String l) {
		name = n;
		login = l;
	}

	public String getName() {
		return name;
	}

	public String getLogin() {
		return login;
	}
	
}
