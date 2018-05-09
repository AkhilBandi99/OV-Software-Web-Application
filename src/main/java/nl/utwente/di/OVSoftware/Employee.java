package nl.utwente.di.OVSoftware;

import java.util.ArrayList;
import java.util.List;

public class Employee {
	private final int id;
	private final String name;
	List<Payrates> payrates;
	
	public Employee(int i, String n) {
		id = i;
		name = n;
		payrates = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void addPayrates(int c, String s, String e) {
		payrates.add(new Payrates(c, s, e));
	}
	
	public List<Payrates> getPayrates(){
		return payrates;
	}

}
