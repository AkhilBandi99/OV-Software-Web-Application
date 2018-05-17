package nl.utwente.di.OVSoftware;

import java.util.ArrayList;
import java.util.List;

public class Employee {
	private final int id;
	private final String name;
	private final String status;
	List<Payrates> payrates;
	
	public Employee(int i, String name, String status) {
		id = i;
		this.name = name;
		this.status = status;
		payrates = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getStatus(){return status;}
	
	public void addPayrates(int c, String s, String e) {
		payrates.add(new Payrates(c, s, e));
	}
	
	public List<Payrates> getPayrates(){
		return payrates;
	}
	
	public String toString() {
		return getId() + " " + getName() + " " +getStatus();
	}

}
