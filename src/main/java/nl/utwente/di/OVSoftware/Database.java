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
			+ "FROM public.\"HumresTable\" h, public.\"EmployeeRateTable\" r "
			+ "WHERE h.res_id = r.crdnr AND h.\"freefield 16\" = 'N' "
			+ "ORDER BY h.res_id";
	
	private static String all = "SELECT h.res_id, h.fullname "
			+ "FROM public.\"HumresTable\" h "
			+ "WHERE h.\"freefield 16\" = 'N' "
			+ "ORDER BY h.res_id";

	public static List<Employee> getEmployees(String query) {
		ResultSet res = getData("", query);
		List<Employee> l = new ArrayList<>();
		try {
			while(!res.isLast()) {
				res.next();
				l.add(new Employee(res.getInt(1), res.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return l;
	}
	
	public static List<Employee> allEmployees(){
		return getEmployees(all);
	}
	
	/*
	public static List<Employee> searchEmployees() {
		return getEmployees()
	}
*/
	public static ResultSet getData(String db, String query){
		try {
			Class.forName("org.postgresql.Driver");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String url = "jdbc:postgresql://localhost:5432/ov";
		try {
			Connection conn = DriverManager.getConnection(url, "user", "dab");
			Statement statement = conn.createStatement();
			ResultSet res = statement.executeQuery(query);
			return res;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
