package nl.utwente.di.OVSoftware;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

	private static String retr = "SELECT h.res_id, h.fullname, r.purchaseprice, r.vandatum, r.totdatum "
			+ "FROM di08.humres h, di08.employeerates r "
			+ "WHERE h.res_id = r.crdnr AND h.\"freefield 16\" = 'N' "
			+ "ORDER BY h.res_id";
	
	private static String all = "SELECT h.res_id, h.fullname, h.emp_stat "
			+ "FROM di08.humres h "
			+ "WHERE h.\"freefield 16\" = 'N' "
			+ "ORDER BY h.res_id";
	
	private static String specpr(int crdnr) { 
		return "SELECT r.purchaseprice, r.vandatum, r.totdatum "
		+ "FROM di08.employeerates r, di08.humres h "
		+ "WHERE r.crdnr = h.res_id "
		+ "AND r.crdnr = " + crdnr;
	}
	
	private static int tsvector() {
		try {
			Class.forName("org.postgresql.Driver");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String url = "jdbc:postgresql://farm03.ewi.utwente.nl:7016/docker";
		int tsvector = 0;
		try {
			Connection conn = DriverManager.getConnection(url, "docker", "YkOkimczn");
			Statement statement = conn.createStatement();
			tsvector = statement.executeUpdate("ALTER TABLE di08.humres " 
												+ "ADD ts tsvector;");
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tsvector;
	}
	
	private static int update() {
		try {
			Class.forName("org.postgresql.Driver");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String url = "jdbc:postgresql://farm03.ewi.utwente.nl:7016/docker";
		int update = 0;
		try {
			Connection conn = DriverManager.getConnection(url, "docker", "YkOkimczn");
			Statement statement = conn.createStatement();
			update = statement.executeUpdate("UPDATE di08.humres "
												+ "SET ts = to_tsvector('english', coalesce(res_id, '0') ||' '|| coalesce(fullname, 'NULL'));");
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return update;
	}
	
	private static int index() {
		try {
			Class.forName("org.postgresql.Driver");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String url = "jdbc:postgresql://farm03.ewi.utwente.nl:7016/docker";
		int index = 0;
		try {
			Connection conn = DriverManager.getConnection(url, "docker", "YkOkimczn");
			Statement statement = conn.createStatement();
			index = statement.executeUpdate("CREATE INDEX index ON di08.humres USING GIN(ts)");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return index;
	}
	
	private static String search(int crdnr, String fullname) {
		if(crdnr != -1 && fullname != "NULL") {
			return "SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
			+ "FROM di08.humres h, to_tsquery('" + crdnr + "|" + fullname + "') query "
			+ "WHERE ts @@ query "
			//+ "OR h.fullname ILIKE '%"+ fullname +"%' "
			//+ "OR h.res_id LIKE '%" + crdnr +"%' "
			+ "ORDER BY rank DESC;";
		} else if(crdnr == -1 && !fullname.equals("-1")) {
			return "SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
			+ "FROM di08.humres h, to_tsquery('" + fullname + "') query "
			+ "WHERE ts @@ query "
			+ "OR h.fullname ILIKE '%"+ fullname +"%' "
			+ "ORDER BY rank DESC;";
		} else if(crdnr != -1 && fullname.equals("-1")) {
			return "SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
			+ "FROM di08.humres h, to_tsquery('" + crdnr + "') query "
			+ "WHERE ts @@ query "
			//+ "AND h.res_id LIKE '%"+ crdnr +"%' "
			+ "ORDER BY rank DESC;";
		} else {
			return null;
		}
	}
	
	public static List<Employee> getEmployees(String query) {
		ResultSet res = getData("", query);
		List<Employee> l = new ArrayList<>();
		try {
			while(!res.isLast()) {
				res.next();
				l.add(new Employee(res.getInt(1), res.getString(2),res.getString(3)));
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		return l;
	}
	
	public static List<Employee> allEmployees(){
		return getEmployees(all);
	}
	
	public static List<Payrates> getPayratesSpecificEmployee(int crdnr){
		ResultSet res = getData("", Database.specpr(crdnr));
		List<Payrates> l = new ArrayList<>();
		try {
			while(!res.isLast()) {
				res.next();
				l.add(new Payrates(res.getDouble(1), res.getString(2), res.getString(3)));
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		return l;
	}
	
	
	public static List<Employee> searchEmployees(int crdnr, String fullname) {
		if(Database.update() != 0) {
			ResultSet res = getData("", Database.search(crdnr, fullname));
			List<Employee> l = new ArrayList<>();
			try {
				if(res != null) {
					while(!res.isLast()) {
						res.next();
						l.add(new Employee(res.getInt(1), res.getString(2),res.getString(3)));
					}
				}
			} catch (SQLException | NullPointerException e) {
				e.printStackTrace();
			}
			return l;
		} else {
			List<Employee> l = new ArrayList<>();
			return l;
		}
		
	}

	public static ResultSet getData(String db, String query){
		try {
			Class.forName("org.postgresql.Driver");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String url = "jdbc:postgresql://farm03.ewi.utwente.nl:7016/docker";
		try {
			Connection conn = DriverManager.getConnection(url, "docker", "YkOkimczn");
			Statement statement = conn.createStatement();
			ResultSet res = statement.executeQuery(query);
			return res;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
