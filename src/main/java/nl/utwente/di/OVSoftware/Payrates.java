package nl.utwente.di.OVSoftware;

public class Payrates {
	private final int cost;
	private final String startDate;
	private final String endDate;
	
	public Payrates(int c, String s, String e) {
		cost = c;
		startDate = s;
		endDate = e;
	}

	public int getCost() {
		return cost;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}
	
}
