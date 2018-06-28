package nl.utwente.di.OVSoftware;

import java.util.ArrayList;

import java.util.List;

public class DatabaseMaps {

	private List<Table> users = new ArrayList<>();
	
	public DatabaseMaps() {
		this.newDatabase("Amsterdam", "//farm03.ewi.utwente.nl:7016/docker", "docker", "YkOkimczn");
		this.newDatabase("Belgie", "di049@castle.ewi.utwente.nl", "di049", "Hzixmlr+");
	}
	
	public void newDatabase(String name, String login, String user, String pass) {
		users.add(new Table(name, login, user, pass));
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
	
	public Table nametotable(String n) {
		for (Table d: users) {
			if (d.getName().equals(n)) {
				return d;
			}
		}
		return null;
	}
	
}
