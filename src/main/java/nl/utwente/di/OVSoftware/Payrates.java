package nl.utwente.di.OVSoftware;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "cost", "startDate", "endDate"})
public class Payrates implements Comparable<Payrates>{
	
	private final int id;
	private final double cost;
	private final Calendar startDate;
	private final Calendar endDate;
	
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
	
	public int getId() {
		return id;
	}

	public double getCost() {
		return cost;
	}
	
	public boolean isNextDate(String next) {
		Calendar temp = (Calendar) endDate.clone();
		temp.add(Calendar.DATE, 1);
		return format(temp).equals(next);
	}

	public String getStartDate() {
		return format(startDate);
	}
	
	public String format(Calendar date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date.getTime());
	}

	public String getEndDate() {
		return format(endDate);
	}

	@Override
	public int compareTo(Payrates o) {
		o.getStartDate().compareTo(this.getStartDate());
		return 0;
	}
	
	public String toString() {
		return getId() + " " + getCost() + " " + getStartDate() + " " + getEndDate();
	}
	
}
