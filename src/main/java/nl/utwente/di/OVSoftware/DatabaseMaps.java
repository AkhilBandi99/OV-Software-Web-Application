package nl.utwente.di.OVSoftware;

import java.util.ArrayList;

import java.util.List;

public class DatabaseMaps {

	private List<Table> users = new ArrayList<>();
	
	public DatabaseMaps() {
		this.newDatabase("Amsterdam", "//farm03.ewi.utwente.nl:7016/docker");
		this.newDatabase("Belgie", "This will crash it");
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
	
	public String getFirst() {
		return users.get(1).getLogin();
	}
	
	public String nametologin(String n) {
		for (Table d: users) {
			if (d.getName().equals(n)) {
				return d.getLogin();
			}
		}
		return null;
	}
	
}
