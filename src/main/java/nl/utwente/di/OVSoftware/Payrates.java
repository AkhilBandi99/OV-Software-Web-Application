package nl.utwente.di.OVSoftware;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "cost", "startDate", "endDate"})
public class Payrates {
	
	private final int id;
	private final double cost;
	private final String startDate;
	private final String endDate;
	
	
	public Payrates(int i, double c, String s, String e) {
		id = i;
		cost = c;
		startDate = s;
		endDate = e;
	}
	
	public int getId() {
		return id;
	}

	public double getCost() {
		return cost;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}
	
}
