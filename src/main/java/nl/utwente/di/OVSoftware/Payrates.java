package nl.utwente.di.OVSoftware;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "cost", "startDate", "endDate"})
public class Payrates implements Comparable<Payrates>{
	
	private final int id;
	private final double cost;
	private final Calendar startDate;
	private final Calendar endDate;
	
	// constructor 
	public Payrates(int i, double c, String s, String e) {
		id = i;
		cost = c;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		startDate = Calendar.getInstance();
		endDate = Calendar.getInstance();
		try {
			startDate.setTime(sdf.parse(s));
			endDate.setTime(sdf.parse(e));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}
	
	// returns id of payrate
	public int getId() {
		return id;
	}

	// returns cost of payrate
	public double getCost() {
		return cost;
	}

	// checks if the date entered is valid or not
	public boolean isNextDate(String next) {
		Calendar temp = (Calendar) endDate.clone();
		temp.add(Calendar.DATE, 1);
		return format(temp).equals(next);
	}

	// returns start date
	public String getStartDate() {
		return format(startDate);
	}

	// formats the date into simple data
	public String format(Calendar date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date.getTime());
	}

	// returns end date
	public String getEndDate() {
		return format(endDate);
	}

	// compares the startdates of the payrates
	@Override
	public int compareTo(Payrates o) {
		return startDate.compareTo(o.startDate);
	}

	// checks if the end date is after the start date
	public boolean checkDates() {
		return startDate.before(endDate);
	}

	// converts the payrate to a string
	public String toString() {
		return getId() + " " + getCost() + " " + getStartDate() + " " + getEndDate();
	}

	// checks the integrety of the payrates
	public static void checkIntegrity(List<Payrates> mainlist) throws DateException {
		List<Payrates> head = new ArrayList<>(mainlist);
		Collections.sort(head);
		checkPayrate(mainlist);
		checkDates(head, mainlist);
	}

	// checks the date of the given payrates
	private static void checkPayrate(List<Payrates> head) throws DateException {
		int i = 0;
		while (i < head.size()) {
			if (!head.get(i).checkDates()) {
				throw new DateException (i + 1 + ": " + head.get(i).getStartDate() + " " + head.get(i).getEndDate() + " Start date after end date");
			}
			i++;
		}
	}
	
	// check the dates of the payrates
	private static void checkDates(List<Payrates> head, List<Payrates> mainlist) throws DateException {
		List<Payrates> list = new ArrayList<Payrates>(head);
		while(!list.isEmpty()) {
			int id = list.get(0).getId();
			List<Payrates> temp = new ArrayList<Payrates>();
			int i = 0;
			int i2 = 0;
			while (i < list.size()) {
				Payrates item = list.get(i);
				if (item.getId() == id) {
					temp.add(item);
					list.remove(i);
				} else {
					i++;
				}
			}
			while (i2 < temp.size() - 1) {
				if (!temp.get(i2).isNextDate(temp.get(i2 + 1).getStartDate())) {
					throw new DateException((mainlist.indexOf(temp.get(i2 + 1)) + 1) + ": " + temp.get(i2).getEndDate() + " " + temp.get(i2 + 1).getStartDate() + " Start date is not next day from previous end date");
				}
				i2++;
			}
		}
	}
	
}