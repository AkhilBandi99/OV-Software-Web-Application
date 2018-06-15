package nl.utwente.di.OVSoftware;

import java.util.ArrayList;

import java.util.List;

public class DatabaseMaps {

	private List<Table> users = new ArrayList<>();
	
	public DatabaseMaps() {
		this.newDatabase("Amsterdam", "Amsterdam");
		this.newDatabase("Belgie", "Belgie");
	}
	
	public void newDatabase(String name, String login) {
		users.add(new Table(name, login));
	}

	public List<String> getDatabases() {
		List<String> list = new ArrayList<String>();
		for (Table t: users) {
			list.add(t.getName());
		}
		return list;
	}
	
}
