package nl.utwente.di.OVSoftware;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

	private static int i = 1;
	private static List<Employee> ListOnPage = new ArrayList<>();

	private static String allPayrates = "SELECT * " + "FROM di08.employeerates";

	private static String all = "SELECT h.res_id, h.fullname, h.emp_stat " + "FROM di08.humres h "
			+ "WHERE h.\"freefield 16\" = 'N' " + "ORDER BY h.res_id";

	private static String specpr(int crdnr) {
		return "SELECT r.crdnr, r.purchaseprice, r.vandatum, r.totdatum " + "FROM di08.employeerates r, di08.humres h "
				+ "WHERE r.crdnr = h.res_id " + "AND r.crdnr = " + crdnr;
	}


	private static String dropall() {
		return "DELETE FROM di08.employeerates;";
	}

	private static int tsvector() {
		if (i == 0) {
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
				tsvector = statement.executeUpdate("ALTER TABLE di08.humres " + "ADD ts tsvector;");
				statement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			i = 1;
			return tsvector;
		} else {
			i = 1;
			return 1;
		}
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
					+ "SET ts = to_tsvector('english', coalesce(res_id, '0') ||' '|| coalesce(fullname, ''));");
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return update;
	}

	private static int index() {
		if (i == 0) {
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
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			i = 1;
			return index;
		} else {
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
				index = statement.executeUpdate("REINDEX TABLE di08.humres");
				statement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			i = 1;
			index = 1;
			return index;
		}
	}

	private static String searchWstat(int crdnr, String fullname, String status) {
		if (crdnr != -1 && !fullname.equals("-1")) {
			if(!status.equals("-1")) {
				return "SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
						+ "FROM di08.humres h, to_tsquery('" + crdnr + "|" + fullname + "') query " 
						+ "WHERE (ts @@ query "
						+ "OR (h.fullname ILIKE '" + fullname + "%' " + "AND h.res_id::varchar LIKE '" + crdnr + "%')) "
						+ "AND h.\"freefield 16\" = 'N' "
						+ "AND h.emp_stat = '" + status + "'"
						+ "ORDER BY rank DESC;";
			} else {
				return "SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
						+ "FROM di08.humres h, to_tsquery('" + crdnr + "|" + fullname + "') query " 
						+ "WHERE (ts @@ query "
						+ "OR (h.fullname ILIKE '" + fullname + "%' " + "AND h.res_id::varchar LIKE '" + crdnr + "%')) "
						+ "AND h.\"freefield 16\" = 'N' "
						+ "ORDER BY rank DESC;";
			}
		} else if (crdnr != -1 && fullname.equals("-1")) {
			if(!status.equals("-1")) {
				return "SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
						+ "FROM di08.humres h, to_tsquery('" + crdnr + "') query " 
						+ "WHERE (ts @@ query "
						+ "OR h.res_id::varchar LIKE '" + crdnr + "%') "
						+ "AND h.\"freefield 16\" = 'N' "
						+ "AND h.emp_stat = '" + status + "'"
						+ "ORDER BY rank DESC;";
			} else {
				return "SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
						+ "FROM di08.humres h, to_tsquery('" + crdnr + "') query " 
						+ "WHERE (ts @@ query "
						+ "OR h.res_id::varchar LIKE '" + crdnr + "%') "
						+ "AND h.\"freefield 16\" = 'N' "
						+ "ORDER BY rank DESC;";
			}
		} else if (crdnr == -1 && !fullname.equals("-1")) {
			if(!status.equals("-1")) {
				return "SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
						+ "FROM di08.humres h, to_tsquery('" + fullname + "') query " 
						+ "WHERE (ts @@ query "
						+ "OR h.fullname ILIKE '" + fullname + "%') "
						+ "AND h.\"freefield 16\" = 'N' "
						+ "AND h.emp_stat = '" + status + "'"
						+ "ORDER BY rank DESC;";
			} else {
				return "SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
						+ "FROM di08.humres h, to_tsquery('" + fullname + "') query " 
						+ "WHERE (ts @@ query "
						+ "OR h.fullname ILIKE '" + fullname + "%') "
						+ "AND h.\"freefield 16\" = 'N' "
						+ "ORDER BY rank DESC;";
			}
		} else {
			return "";
		}
	}
	
	private static String search(int crdnr, String fullname) {
		if (crdnr != -1 && !fullname.equals("-1")) {
			return "SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
					+ "FROM di08.humres h, to_tsquery('" + crdnr + "|" + fullname + "') query " 
					+ "WHERE (ts @@ query "
					+ "OR (h.fullname ILIKE '" + fullname + "%' " + "AND h.res_id::varchar LIKE '" + crdnr + "%')) "
					+ "AND h.\"freefield 16\" = 'N' "
					+ "ORDER BY rank DESC;";
		} else if (crdnr != -1 && fullname.equals("-1")) {
			return "SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
					+ "FROM di08.humres h, to_tsquery('" + crdnr + "') query " 
					+ "WHERE (ts @@ query "
					+ "OR h.res_id::varchar LIKE '" + crdnr + "%') "
					+ "AND h.\"freefield 16\" = 'N' "
					+ "ORDER BY rank DESC;";
		} else if (crdnr == -1 && !fullname.equals("-1")) {
			return "SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
					+ "FROM di08.humres h, to_tsquery('" + fullname + "') query " 
					+ "WHERE (ts @@ query "
					+ "OR h.fullname ILIKE '" + fullname + "%') "
					+ "AND h.\"freefield 16\" = 'N' "
					+ "ORDER BY rank DESC;";
		} else {
			return "";
		}
	}

	public static String status(String status) {
		return "SELECT h.res_id, h.fullname, h.emp_stat " + "FROM di08.humres h " 
				+ "WHERE h.emp_stat = '" + status + "' " 
				+ "AND h.\"freefield 16\" = 'N' "
				+ "ORDER BY h.res_id";
	}

	public static void importPayrts(List<Payrates> list) {
		try {
			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String url = "jdbc:postgresql://farm03.ewi.utwente.nl:7016/docker";
		try {
			Connection conn = DriverManager.getConnection(url, "docker", "YkOkimczn");
			for(Payrates p: list) {
				Statement statement = conn.createStatement();
				statement.executeUpdate("INSERT INTO di08.employeerates(crdnr, purchaseprice, vandatum, totdatum) VALUES ('"
								+ p.getId() + "', '" + p.getCost() + "', '" + p.getStartDate() + "', '" + p.getEndDate() + "');");
				statement.close();
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static void editPayrts(List<Payrates> list) {
		String ret = null;
		try {
			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String url = "jdbc:postgresql://farm03.ewi.utwente.nl:7016/docker";
		try {
			Connection conn = DriverManager.getConnection(url, "docker", "YkOkimczn");
			try {
				Payrates.checkIntegrity(list);
				
				// delete the table
				Database.emptyAllTables();
				//System.out.println(list.size());
				// rewrite the database
				Database.importPayrts(list);
			} catch(DateException e){
				ret = e.getMessage();
			}
			if(ret == null) {
				for(Payrates p: list) {
					Statement statement = conn.createStatement();
					statement.executeQuery("DELETE FROM di08.employeerates WHERE crdnr = " + p.getId());
					statement.executeUpdate("INSERT INTO di08.employeerates(crdnr, purchaseprice, vandatum, totdatum) VALUES ('"
								+ p.getId() + "', '" + p.getCost() + "', '" + p.getStartDate() + "', '" + p.getEndDate() + "');");
					statement.close();
					conn.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ret = null;
		return;
	}

	public static List<Employee> getEmployees(String query) {
		ResultSet res = getData("", query);
		List<Employee> l = new ArrayList<>();
		try {
			while(res.next()) {
				String status = "Unknown";
				switch (res.getString(3)){
					case "A":
						status = "Active";
						break;
					case "I":
						status = "Not Active";
						break;
					case "H":
						status = "Not Active Yet";
						break;
				}
				l.add(new Employee(res.getInt(1), res.getString(2), status));
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		ListOnPage.clear();
		ListOnPage.addAll(l);
		return l;
	}

	public static List<Employee> allEmployees() {
		return getEmployees(all);
	}

	public static List<Payrates> getPayratesSpecificEmployee(int crdnr){
		ResultSet res = getData("", Database.specpr(crdnr));
		List<Payrates> l = new ArrayList<>();
		try {
			while(res.next()) {
				l.add(new Payrates(res.getInt(1), res.getDouble(2), res.getString(3), res.getString(4)));
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		return l;
	}

	public static List<Payrates> getAllPayrates() {
		ResultSet res = getData("", allPayrates);
		List<Payrates> l = new ArrayList<>();
		try {
			while(res.next()) {
				l.add(new Payrates(res.getInt(1), res.getDouble(2), res.getString(3),res.getString(4)));
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		return l;
	}

	public static boolean emptyAllTables() {
		try {
			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String url = "jdbc:postgresql://farm03.ewi.utwente.nl:7016/docker";
		try {
			Connection conn = DriverManager.getConnection(url, "docker", "YkOkimczn");
			Statement statement = conn.createStatement();
			statement.executeUpdate(dropall());
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static List<Employee> searchEmployees(int crdnr, String fullname, String status) {
		if(Database.tsvector() != 0 && Database.update() != 0 && Database.index() != 0) {
			ResultSet res;
			if(!status.equals("-1")) {
				res = getData("", Database.searchWstat(crdnr, fullname, status));
			} else {
				res = getData("", Database.search(crdnr, fullname));
			}
			List<Employee> l = new ArrayList<>();
			try {
				if(res != null) {
					while(res.next()) {
						l.add(new Employee(res.getInt(1), res.getString(2),res.getString(3)));
					}
				}
			} catch (SQLException | NullPointerException e) {
				e.printStackTrace();
			}
			ListOnPage.clear();
			ResultSet result = getData("", Database.search(crdnr, fullname));
			List<Employee> f = new ArrayList<>();
			try {
				if(result != null) {
					while(result.next()) {
						f.add(new Employee(result.getInt(1), result.getString(2),result.getString(3)));
					}
				}
			} catch (SQLException | NullPointerException e) {
				e.printStackTrace();
			};
			ListOnPage.addAll(f);
			return l;
		} else {
			List<Employee> l = new ArrayList<>();
			return l;
		}
		
	}

	public static List<Employee> statusFilter(String status, int crdnr, String fullname) {
		ResultSet res = getData("", Database.status(status));
		List<Employee> l = new ArrayList<>();
		try {
			while(res.next()) {
				for(int i = 0; i < ListOnPage.size(); i++) {
					if(res.getInt(1) == ListOnPage.get(i).getId()) {
						l.add(new Employee(res.getInt(1), res.getString(2),res.getString(3)));
					}
				}
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		return l;
	}

	public static ResultSet getData(String db, String query) {
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
