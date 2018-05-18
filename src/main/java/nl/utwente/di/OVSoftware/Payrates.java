package nl.utwente.di.OVSoftware;

public class Payrates {
	private final double cost;
	private final String startDate;
	private final String endDate;
	
	public Payrates(double c, String s, String e) {
		cost = c;
		startDate = s;
		endDate = e;
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
