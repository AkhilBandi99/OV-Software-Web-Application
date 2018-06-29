package nl.utwente.di.OVSoftware;

import org.mindrot.jbcrypt.BCrypt;

import com.gargoylesoftware.htmlunit.javascript.host.Console;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Database {

	private static int i = 1;
	public static Table mainDatabase = new Table("Amsterdam", "//farm03.ewi.utwente.nl:7016/docker", "docker", "YkOkimczn");
	
	//Creates a connection to the database
	private static Connection MakeConnection(Table database) throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		System.out.println(database);
		String url = "jdbc:postgresql:" + database.getLogin();
		Connection conn = DriverManager.getConnection(url, database.getUser(), database.getPass());
		return conn;
	}

	//Gets all payrates
	private static ResultSet allPayrates(Connection conn) throws SQLException {
		PreparedStatement p = conn.prepareStatement(
				"SELECT * " + 
				"FROM di08.employeerates");
		ResultSet res = p.executeQuery();
		return res;
	}

	//Gets all employees
	private static ResultSet allEmployees(Connection conn) throws SQLException {
		PreparedStatement p = conn.prepareStatement(
				"SELECT h.res_id, h.fullname, h.emp_stat " +
				"FROM di08.humres h " +
				"WHERE h.\"freefield 16\" = 'N' " +
				"ORDER BY h.res_id");
		ResultSet res = p.executeQuery();
		return res;
	}

	//Gets a specific employees Payrates
	private static ResultSet specpr(Connection conn, int crdnr) throws SQLException {
		PreparedStatement p = conn.prepareStatement(
				"SELECT r.crdnr, r.purchaseprice, r.vandatum, r.totdatum " + 
				"FROM di08.employeerates r, di08.humres h " +
				"WHERE r.crdnr = h.res_id " + 
				"AND r.crdnr = ?" );
		p.setInt(1, crdnr);
		ResultSet res = p.executeQuery();
		return res;
	}

	//Drops all employees in the database
	private static void dropall(Connection conn) throws SQLException {
		PreparedStatement p = conn.prepareStatement(
				"DELETE FROM di08.employeerates;");
		p.execute();
	}
	
	//Drops all Payrates for an employee
	private static void delPayrate(Connection conn, int crdnr) throws SQLException {
		PreparedStatement p = conn.prepareStatement(
		"DELETE FROM di08.employeerates WHERE crdnr = ?");
		p.setInt(1, crdnr);
		p.execute();
	}
	
	private static void addPayrates(Connection conn, List<Payrates> list) throws SQLException {
		PreparedStatement p = conn.prepareStatement(
				"INSERT INTO di08.employeerates(crdnr, purchaseprice, vandatum, totdatum) VALUES (?,?,?,?);");
		for (Payrates rate: list) {
			p.setInt(1, rate.getId());
			p.setDouble(2, rate.getCost());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar startDate = Calendar.getInstance();
			Calendar endDate = Calendar.getInstance();
			try {
				startDate.setTime(sdf.parse(rate.getStartDate()));
				endDate.setTime(sdf.parse(rate.getEndDate()));
				p.setDate(3, new java.sql.Date(startDate.getTimeInMillis()));
				p.setDate(4, new java.sql.Date(endDate.getTimeInMillis()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			p.execute();
		}
	}
	
	//Edit the payrates for one employee with deletion
	public static void editPayrates(int crdnr, List<Payrates> list, Table database) {
		try {
			Connection conn = MakeConnection(database);
			delPayrate(conn, crdnr);
			addPayrates(conn, list);
		} catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Import the payrates for all employee with deletion
	public static void importPayrts(List<Payrates> list, Table database) {
		try {
			Connection conn = MakeConnection(database);
				try {
					dropall(conn);
					addPayrates(conn, list);
				} catch(SQLException e) {
					try {
						conn.rollback();
					} catch (SQLException e1) {
						
					}
				}
			} catch(SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int tsvector() {
		if (i == 0) {
			try {
				Class.forName("org.postgresql.Driver");

			} catch (ClassNotFoundException e) {

			}
			String url = "jdbc:postgresql://farm03.ewi.utwente.nl:7016/docker";
			int tsvector = 0;
			try {
				Connection conn = DriverManager.getConnection(url, "docker", "YkOkimczn");
				PreparedStatement statement = conn.prepareStatement("ALTER TABLE di08.humres " + "ADD ts tsvector;");
				tsvector = statement.executeUpdate();
				statement.close();
				conn.close();
			} catch (SQLException e) {

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

		}
		String url = "jdbc:postgresql://farm03.ewi.utwente.nl:7016/docker";
		int update = 0;
		try {
			Connection conn = DriverManager.getConnection(url, "docker", "YkOkimczn");
			PreparedStatement statement = conn.prepareStatement("UPDATE di08.humres "
					+ "SET ts = to_tsvector('english', coalesce(res_id, '0') ||' '|| coalesce(fullname, ''));");
			update = statement.executeUpdate();
			statement.close();
			conn.close();
		} catch (SQLException e) {

		}
		return update;
	}

	private static int index() {
		if (i == 0) {
			try {
				Class.forName("org.postgresql.Driver");

			} catch (ClassNotFoundException e) {

			}
			String url = "jdbc:postgresql://farm03.ewi.utwente.nl:7016/docker";
			int index = 0;
			try {
				Connection conn = DriverManager.getConnection(url, "docker", "YkOkimczn");
				PreparedStatement statement = conn.prepareStatement("CREATE INDEX index ON di08.humres USING GIN(ts)");
				index = statement.executeUpdate();
				statement.close();
				conn.close();
			} catch (SQLException e) {

			}
			i = 1;
			return index;
		} else {
			try {
				Class.forName("org.postgresql.Driver");

			} catch (ClassNotFoundException e) {

			}
			String url = "jdbc:postgresql://farm03.ewi.utwente.nl:7016/docker";
			int index = 0;
			try {
				Connection conn = DriverManager.getConnection(url, "docker", "YkOkimczn");
				PreparedStatement statement = conn.prepareStatement("REINDEX TABLE di08.humres");
				index = statement.executeUpdate();
				statement.close();
				conn.close();
			} catch (SQLException e) {

			}
			index = 1;
			return index;
		}
	}

	public static ResultSet search(Connection conn, int crdnr, String fullname) {
		try {
			if (crdnr != -1 && !fullname.equals("-1")) {
				PreparedStatement p = conn.prepareStatement (
						"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 " + 
						"FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " + 
						"WHERE ((ts @@ query1 OR ts @@ query2) " +
						"OR (h.fullname ILIKE ? " +
						"AND h.res_id::varchar LIKE ?)) " +
						"AND h.\"freefield 16\" = 'N' " +
						"ORDER BY rank1 DESC, rank2 DESC;");
				p.setString(1, crdnr + "");
				p.setString(2, fullname);
				p.setString(3, fullname + "%");
				p.setString(4, crdnr + "%");
				ResultSet res = p.executeQuery();
				return res;
			} else if (crdnr != -1 && fullname.equals("-1")) {
				PreparedStatement p = conn.prepareStatement(
						"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank " +
						"FROM di08.humres h, to_tsquery(?) query " +
						"WHERE (ts @@ query " +
						"OR h.res_id::varchar LIKE ?) " +
						"AND h.\"freefield 16\" = 'N' " +
						"ORDER BY rank DESC;");
				p.setString(1, crdnr + "");
				p.setString(2, crdnr + "%");
				ResultSet res = p.executeQuery();
				return res;
			} else if (crdnr == -1 && !fullname.equals("-1")) {
				PreparedStatement p = conn.prepareStatement(
						"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank " +
						"FROM di08.humres h, to_tsquery(?) query " +
						"WHERE (ts @@ query " +
						"OR h.fullname ILIKE ?) " +
						"AND h.\"freefield 16\" = 'N' " +
						"ORDER BY rank DESC;");
				p.setString(1, fullname);
				p.setString(2, fullname + "%");
				ResultSet res = p.executeQuery();
				return res;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static ResultSet searchFilter(Connection conn, int crdnr, String fullname, String status) {
		try {
			if (crdnr != -1 && !fullname.equals("-1")) {
				PreparedStatement p = conn.prepareStatement (
						"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 " + 
						"FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " + 
						"WHERE ((ts @@ query1 OR ts @@ query2) " +
						"OR (h.fullname ILIKE ? " + "AND h.res_id::varchar LIKE ?)) " +
						"AND h.\"freefield 16\" = 'N' " +
						"AND h.emp_stat = ?" +
						"ORDER BY rank1 DESC, rank2 DESC;");
				p.setString(1, crdnr + "");
				p.setString(2, fullname);
				p.setString(3, fullname + "%");
				p.setString(4, crdnr + "%");
				p.setString(5, status);
				ResultSet res = p.executeQuery();
				return res;
			} else if (crdnr != -1 && fullname.equals("-1")) {
				PreparedStatement p = conn.prepareStatement(
						"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank " +
						"FROM di08.humres h, to_tsquery(?) query " +
						"WHERE (ts @@ query " +
						"OR h.res_id::varchar LIKE ?) " +
						"AND h.\"freefield 16\" = 'N' " +
						"AND h.emp_stat = ?" +
						"ORDER BY rank DESC;");
				p.setString(1, crdnr + "");
				p.setString(2, crdnr + "%");
				p.setString(3, status);
				ResultSet res = p.executeQuery();
				return res;
			} else if (crdnr == -1 && !fullname.equals("-1")) {
				PreparedStatement p = conn.prepareStatement(
						"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank " +
						"FROM di08.humres h, to_tsquery(?) query " +
						"WHERE (ts @@ query " +
						"OR h.fullname ILIKE ?) " +
						"AND h.\"freefield 16\" = 'N' " +
						"AND h.emp_stat = ?" +
						"ORDER BY rank DESC;");
				p.setString(1, fullname);
				p.setString(2, fullname + "%");
				p.setString(3, status);
				ResultSet res = p.executeQuery();
				return res;
			} else if (crdnr == -1 && fullname.equals("-1")) {
				PreparedStatement p = conn.prepareStatement(
						"SELECT h.res_id, h.fullname, h.emp_stat " +
						"FROM di08.humres h " +
						"WHERE h.emp_stat = ? " +
						"AND h.\"freefield 16\" = 'N' " +
						"ORDER BY h.res_id");
				p.setString(1, status);
				ResultSet res = p.executeQuery();
				return res;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	private static ResultSet searchSort(Connection conn, int crdnr, String fullname, int sort) throws SQLException {
		PreparedStatement p;
		try {
			if (crdnr != -1 && !fullname.equals("-1")) {
				if(sort == 00) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 "
							+ "FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " 
							+ "WHERE ((ts @@ query1 OR ts @@ query2) "
							+ "OR (h.fullname::varchar ILIKE ? AND h.res_id::varchar LIKE ?)) "
							+ "AND h.\"freefield 16\" = 'N' "
							+ "ORDER BY rank1 DESC, rank2 DESC, h.res_id;");
					p.setString(1, crdnr + "");
					p.setString(2, fullname);
					p.setString(3, fullname + "%");
					p.setString(4, crdnr + "%");
					ResultSet res = p.executeQuery();
					return res;
				} else if(sort == 01) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 "
							+ "FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " 
							+ "WHERE ((ts @@ query1 OR ts @@ query2) "
							+ "OR (h.fullname::varchar ILIKE ? AND h.res_id::varchar LIKE ?)) "
							+ "AND h.\"freefield 16\" = 'N' "
							+ "ORDER BY rank1 DESC, rank2 DESC, h.res_id DESC;");
					p.setString(1, crdnr + "");
					p.setString(2, fullname);
					p.setString(3, fullname + "%");
					p.setString(4, crdnr + "%");
					ResultSet res = p.executeQuery();
					return res;
				} else if(sort == 10) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 "
							+ "FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " 
							+ "WHERE ((ts @@ query1 OR ts @@ query2) "
							+ "OR (h.fullname::varchar ILIKE ? AND h.res_id::varchar LIKE ?)) "
							+ "AND h.\"freefield 16\" = 'N' "
							+ "ORDER BY rank1 DESC, rank2 DESC, h.fullname;");
					p.setString(1, crdnr + "");
					p.setString(2, fullname);
					p.setString(3, fullname + "%");
					p.setString(4, crdnr + "%");
					ResultSet res = p.executeQuery();
					return res;
				} else if(sort == 11) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 "
							+ "FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " 
							+ "WHERE ((ts @@ query1 OR ts @@ query2) "
							+ "OR (h.fullname::varchar ILIKE ? AND h.res_id::varchar LIKE ?)) "
							+ "AND h.\"freefield 16\" = 'N' "
							+ "ORDER BY rank1 DESC, rank2 DESC, h.fullname DESC;");
					p.setString(1, crdnr + "");
					p.setString(2, fullname);
					p.setString(3, fullname + "%");
					p.setString(4, crdnr + "%");
					ResultSet res = p.executeQuery();
					return res;
				}
			} else if (crdnr != -1 && fullname.equals("-1")) {
				if(sort == 00) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
							+ "FROM di08.humres h, to_tsquery(?) query " 
							+ "WHERE (ts @@ query "
							+ "OR h.res_id::varchar LIKE ?) "
							+ "AND h.\"freefield 16\" = 'N' "
							+ "ORDER BY rank DESC, h.res_id;");
					p.setString(1, "'" + crdnr + "'");
					p.setString(2, crdnr + "%");
					ResultSet res = p.executeQuery();
					return res;
				} else if(sort == 01) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
							+ "FROM di08.humres h, to_tsquery(?) query " 
							+ "WHERE (ts @@ query "
							+ "OR h.res_id::varchar LIKE ?) "
							+ "AND h.\"freefield 16\" = 'N' "
							+ "ORDER BY rank DESC, h.res_id DESC;");
					p.setString(1, "'" + crdnr + "'");
					p.setString(2, crdnr + "%");
					ResultSet res = p.executeQuery();
					return res;
				} else if(sort == 10) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
							+ "FROM di08.humres h, to_tsquery(?) query " 
							+ "WHERE (ts @@ query "
							+ "OR h.res_id::varchar LIKE ?) "
							+ "AND h.\"freefield 16\" = 'N' "
							+ "ORDER BY rank DESC, h.fullname;");
					p.setString(1, "'" + crdnr + "'");
					p.setString(2, crdnr + "%");
					ResultSet res = p.executeQuery();
					return res;
				} else if(sort == 11) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
							+ "FROM di08.humres h, to_tsquery(?) query " 
							+ "WHERE (ts @@ query "
							+ "OR h.res_id::varchar LIKE ?) "
							+ "AND h.\"freefield 16\" = 'N' "
							+ "ORDER BY rank DESC, h.fullname DESC;");
					p.setString(1, "'" + crdnr + "'");
					p.setString(2, crdnr + "%");
					ResultSet res = p.executeQuery();
					return res;
				}
			} else if (crdnr == -1 && !fullname.equals("-1")) {
				if(sort == 00) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
							+ "FROM di08.humres h, to_tsquery(?) query " 
							+ "WHERE (ts @@ query "
							+ "OR h.fullname ILIKE ?) "
							+ "AND h.\"freefield 16\" = 'N' "
							+ "ORDER BY rank DESC, h.res_id;");
					p.setString(1, "'" + fullname + "'");
					p.setString(2, fullname + "%");
					ResultSet res = p.executeQuery();
					return res;
				} else if(sort == 01) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
							+ "FROM di08.humres h, to_tsquery(?) query " 
							+ "WHERE (ts @@ query "
							+ "OR h.fullname ILIKE ?) "
							+ "AND h.\"freefield 16\" = 'N' "
							+ "ORDER BY rank DESC, h.res_id DESC;");
					p.setString(1, "'" + fullname + "'");
					p.setString(2, fullname + "%");
					ResultSet res = p.executeQuery();
					return res;
				} else if(sort == 10) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
							+ "FROM di08.humres h, to_tsquery(?) query " 
							+ "WHERE (ts @@ query "
							+ "OR h.fullname ILIKE ?) "
							+ "AND h.\"freefield 16\" = 'N' "
							+ "ORDER BY rank DESC, h.fullname;");
					p.setString(1, "'" + fullname + "'");
					p.setString(2, fullname + "%");
					ResultSet res = p.executeQuery();
					return res;
				} else if(sort == 11) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
							+ "FROM di08.humres h, to_tsquery(?) query " 
							+ "WHERE (ts @@ query "
							+ "OR h.fullname ILIKE ?) "
							+ "AND h.\"freefield 16\" = 'N' "
							+ "ORDER BY rank DESC, h.fullname DESC;");
					p.setString(1, "'" + fullname + "'");
					p.setString(2, fullname + "%");
					ResultSet res = p.executeQuery();
					return res;
				}
			} else if (crdnr == -1 && fullname.equals("-1")) {
				if(sort == 00) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat "
							+ "FROM di08.humres h " 
							+ "WHERE h.\"freefield 16\" = 'N' "
							+ "ORDER BY h.res_id;");
					ResultSet res = p.executeQuery();
					return res;
				} else if(sort == 01) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat "
							+ "FROM di08.humres h " 
							+ "WHERE h.\"freefield 16\" = 'N' "
							+ "ORDER BY h.res_id DESC;");
					ResultSet res = p.executeQuery();
					return res;
				} else if(sort == 10) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat "
							+ "FROM di08.humres h " 
							+ "WHERE h.\"freefield 16\" = 'N' "
							+ "ORDER BY h.fullname;");
					ResultSet res = p.executeQuery();
					return res;
				} else if(sort == 11) {
					p = conn.prepareStatement(
							"SELECT h.res_id, h.fullname, h.emp_stat "
							+ "FROM di08.humres h " 
							+ "WHERE h.\"freefield 16\" = 'N' "
							+ "ORDER BY h.fullname DESC;");
					ResultSet res = p.executeQuery();
					return res;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static ResultSet searchFilterSort(Connection conn, int crdnr, String fullname, String status, int sort) {
		PreparedStatement p;
		try {
			if (crdnr != -1 && !fullname.equals("-1")) {
				if(status.equals("-1")) {
					if(sort == 00) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 "
								+ "FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " 
								+ "WHERE ((ts @@ query1 OR ts @@ query2) "
								+ "OR (h.fullname::varchar ILIKE ? AND h.res_id::varchar LIKE ?)) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "ORDER BY rank1 DESC, rank2 DESC, h.res_id;");
						p.setString(1, crdnr + "");
						p.setString(2, fullname);
						p.setString(3, fullname + "%");
						p.setString(4, crdnr + "%");
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 01) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 "
								+ "FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " 
								+ "WHERE ((ts @@ query1 OR ts @@ query2) "
								+ "OR (h.fullname::varchar ILIKE ? AND h.res_id::varchar LIKE ?)) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "ORDER BY rank1 DESC, rank2 DESC, h.res_id DESC;");
						p.setString(1, crdnr + "");
						p.setString(2, fullname);
						p.setString(3, fullname + "%");
						p.setString(4, crdnr + "%");
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 10) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 "
								+ "FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " 
								+ "WHERE ((ts @@ query1 OR ts @@ query2) "
								+ "OR (h.fullname::varchar ILIKE ? AND h.res_id::varchar LIKE ?)) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "ORDER BY rank1 DESC, rank2 DESC, h.fullname;");
						p.setString(1, crdnr + "");
						p.setString(2, fullname);
						p.setString(3, fullname + "%");
						p.setString(4, crdnr + "%");
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 11) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 "
								+ "FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " 
								+ "WHERE ((ts @@ query1 OR ts @@ query2) "
								+ "OR (h.fullname::varchar ILIKE ? AND h.res_id::varchar LIKE ?)) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "ORDER BY rank1 DESC, rank2 DESC, h.fullname DESC;");
						p.setString(1, crdnr + "");
						p.setString(2, fullname);
						p.setString(3, fullname + "%");
						p.setString(4, crdnr + "%");
						ResultSet res = p.executeQuery();
						return res;
					}
				} else {
					if(sort == 00) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 "
								+ "FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " 
								+ "WHERE ((ts @@ query1 OR ts @@ query2) "
								+ "OR (h.fullname::varchar ILIKE ? AND h.res_id::varchar LIKE ?)) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY rank1 DESC, rank2 DESC, h.res_id;");
						p.setString(1, crdnr + "");
						p.setString(2, fullname);
						p.setString(3, fullname + "%");
						p.setString(4, crdnr + "%");
						p.setString(5, status);
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 01) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 "
								+ "FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " 
								+ "WHERE ((ts @@ query1 OR ts @@ query2) "
								+ "OR (h.fullname::varchar ILIKE ? AND h.res_id::varchar LIKE ?)) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY rank1 DESC, rank2 DESC, h.res_id DESC;");
						p.setString(1, crdnr + "");
						p.setString(2, fullname);
						p.setString(3, fullname + "%");
						p.setString(4, crdnr + "%");
						p.setString(5, status);
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 10) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 "
								+ "FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " 
								+ "WHERE ((ts @@ query1 OR ts @@ query2) "
								+ "OR (h.fullname::varchar ILIKE ? AND h.res_id::varchar LIKE ?)) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY rank1 DESC, rank2 DESC, h.fullname;");
						p.setString(1, crdnr + "");
						p.setString(2, fullname);
						p.setString(3, fullname + "%");
						p.setString(4, crdnr + "%");
						p.setString(5, status);
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 11) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query1) AS rank1, ts_rank(ts, query2) AS rank2 "
								+ "FROM di08.humres h, to_tsquery(?) query1, to_tsquery(?) query2 " 
								+ "WHERE ((ts @@ query1 OR ts @@ query2) "
								+ "OR (h.fullname::varchar ILIKE ? AND h.res_id::varchar LIKE ?)) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY rank1 DESC, rank2 DESC, h.fullname DESC;");
						p.setString(1, crdnr + "");
						p.setString(2, fullname);
						p.setString(3, fullname + "%");
						p.setString(4, crdnr + "%");
						p.setString(5, status);
						ResultSet res = p.executeQuery();
						return res;
					}
				}
			} else if (crdnr != -1 && fullname.equals("-1")) {
				if(status.equals("-1")) {
					if(sort == 00) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.res_id::varchar LIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "ORDER BY rank DESC, h.res_id;");
						p.setString(1, "'" + crdnr + "'");
						p.setString(2, crdnr + "%");
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 01) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.res_id::varchar LIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "ORDER BY rank DESC, h.res_id DESC;");
						p.setString(1, "'" + crdnr + "'");
						p.setString(2, crdnr + "%");
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 10) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.res_id::varchar LIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "ORDER BY rank DESC, h.fullname;");
						p.setString(1, "'" + crdnr + "'");
						p.setString(2, crdnr + "%");
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 11) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.res_id::varchar LIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "ORDER BY rank DESC, h.fullname DESC;");
						p.setString(1, "'" + crdnr + "'");
						p.setString(2, crdnr + "%");
						ResultSet res = p.executeQuery();
						return res;
					}
				} else {
					if(sort == 00) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.res_id::varchar LIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY rank DESC, h.res_id;");
						p.setString(1, "'" + crdnr + "'");
						p.setString(2, crdnr + "%");
						p.setString(3, status);
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 01) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.res_id::varchar LIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY rank DESC, h.res_id DESC;");
						p.setString(1, "'" + crdnr + "'");
						p.setString(2, crdnr + "%");
						p.setString(3, status);
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 10) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.res_id::varchar LIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY rank DESC, h.fullname;");
						p.setString(1, "'" + crdnr + "'");
						p.setString(2, crdnr + "%");
						p.setString(3, status);
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 11) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.res_id::varchar LIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY rank DESC, h.fullname DESC;");
						p.setString(1, "'" + crdnr + "'");
						p.setString(2, crdnr + "%");
						p.setString(3, status);
						ResultSet res = p.executeQuery();
						return res;
					}
				}
			} else if (crdnr == -1 && !fullname.equals("-1")) {
				if(status.equals("-1")) {
					if(sort == 00) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.fullname ILIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "ORDER BY rank DESC, h.res_id;");
						p.setString(1, "'" + fullname + "'");
						p.setString(2, fullname + "%");
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 01) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.fullname ILIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "ORDER BY rank DESC, h.res_id DESC;");
						p.setString(1, "'" + fullname + "'");
						p.setString(2, fullname + "%");
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 10) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.fullname ILIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "ORDER BY rank DESC, h.fullname;");
						p.setString(1, "'" + fullname + "'");
						p.setString(2, fullname + "%");
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 11) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.fullname ILIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "ORDER BY rank DESC, h.fullname DESC;");
						p.setString(1, "'" + fullname + "'");
						p.setString(2, fullname + "%");
						ResultSet res = p.executeQuery();
						return res;
					}
				} else {
					if(sort == 00) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.fullname ILIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY rank DESC, h.res_id;");
						p.setString(1, "'" + fullname + "'");
						p.setString(2, fullname + "%");
						p.setString(3, status);
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 01) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.fullname ILIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY rank DESC, h.res_id DESC;");
						p.setString(1, "'" + fullname + "'");
						p.setString(2, fullname + "%");
						p.setString(3, status);
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 10) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.fullname ILIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY rank DESC, h.fullname;");
						p.setString(1, "'" + fullname + "'");
						p.setString(2, fullname + "%");
						p.setString(3, status);
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 11) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat, ts_rank(ts, query) AS rank "
								+ "FROM di08.humres h, to_tsquery(?) query " 
								+ "WHERE (ts @@ query "
								+ "OR h.fullname ILIKE ?) "
								+ "AND h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY rank DESC, h.fullname DESC;");
						p.setString(1, "'" + fullname + "'");
						p.setString(2, fullname + "%");
						p.setString(3, status);
						ResultSet res = p.executeQuery();
						return res;
					}
				}
			} else if (crdnr == -1 && fullname.equals("-1")) {
				if(status.equals("-1")) {
					if(sort == 00) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat "
								+ "FROM di08.humres h " 
								+ "WHERE h.\"freefield 16\" = 'N' "
								+ "ORDER BY h.res_id;");
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 01) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat "
								+ "FROM di08.humres h " 
								+ "WHERE h.\"freefield 16\" = 'N' "
								+ "ORDER BY h.res_id DESC;");
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 10) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat "
								+ "FROM di08.humres h " 
								+ "WHERE h.\"freefield 16\" = 'N' "
								+ "ORDER BY h.fullname;");
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 11) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat "
								+ "FROM di08.humres h " 
								+ "WHERE h.\"freefield 16\" = 'N' "
								+ "ORDER BY h.fullname DESC;");
						ResultSet res = p.executeQuery();
						return res;
					}
				} else {
					if(sort == 00) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat "
								+ "FROM di08.humres h " 
								+ "WHERE h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY h.res_id;");
						p.setString(1, status);
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 01) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat "
								+ "FROM di08.humres h " 
								+ "WHERE h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY h.res_id DESC;");
						p.setString(1, status);
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 10) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat "
								+ "FROM di08.humres h " 
								+ "WHERE h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY h.fullname;");
						p.setString(1, status);
						ResultSet res = p.executeQuery();
						return res;
					} else if(sort == 11) {
						p = conn.prepareStatement(
								"SELECT h.res_id, h.fullname, h.emp_stat "
								+ "FROM di08.humres h " 
								+ "WHERE h.\"freefield 16\" = 'N' "
								+ "AND h.emp_stat = ?"
								+ "ORDER BY h.fullname DESC;");
						p.setString(1, status);
						ResultSet res = p.executeQuery();
						return res;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	

	public static List<Employee> getEmployees(Table database) {
		Connection conn;
		try {
			conn = MakeConnection(database);
			ResultSet res = allEmployees(conn);
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

			}
			return l;
		} catch (ClassNotFoundException | SQLException e1) {
			
		}
		return new ArrayList<Employee>();
	}

	public static List<GoogleAccount> getGoogleAccounts(){
		Connection conn;
		try {
			conn = MakeConnection(mainDatabase);
			PreparedStatement p = conn.prepareStatement("SELECT * FROM di08.googleaccounts");
			ResultSet res = p.executeQuery();
			conn.close();
			List<GoogleAccount> l = new ArrayList<>();
			try {
				while(res.next()) {
					l.add(new GoogleAccount(res.getString(1)));
				}
			} catch (SQLException | NullPointerException e) {

			}
			return l;
		} catch (ClassNotFoundException | SQLException e1) {

		}
		return null;
	}

    public static boolean googleAccountAccepted(String email){
    	try {
			Connection conn = MakeConnection(mainDatabase);
			PreparedStatement p = conn.prepareStatement("SELECT * FROM di08.googleaccounts WHERE email=?");
			p.setString(1, email);
			ResultSet res = p.executeQuery();
			conn.close();
	        try {
	            while(res.next()) {
	                return true;
	            }
	        } catch (SQLException | NullPointerException e) {

	        }
		} catch (ClassNotFoundException | SQLException e1) {
	
		}
        return false;
    }

	public static List<OVAccount> getOVAccounts(){
		try {
			Connection conn = MakeConnection(mainDatabase);
			PreparedStatement p = conn.prepareStatement("SELECT * FROM di08.localaccounts");
			ResultSet res = p.executeQuery();
			conn.close();
			List<OVAccount> l = new ArrayList<>();
			try {
				while(res.next()) {
					//don't send password hashes of all users to client for security reasons
					l.add(new OVAccount(res.getString(1),"•••••••••"));
				}
			} catch (SQLException | NullPointerException e) {

			}
			return l;
		} catch (ClassNotFoundException | SQLException e1) {
			
		}
		return null;
	}


    public static boolean OVAccountAccepted(String username, String password){
    	try {
			Connection conn = MakeConnection(mainDatabase);
			PreparedStatement p = conn.prepareStatement("SELECT password FROM di08.localaccounts WHERE username= ?");
			p.setString(1, username);
			ResultSet res = p.executeQuery();
			conn.close();
	        try {
	            while(res.next()) {
	                return BCrypt.checkpw(password,res.getString(1));
	            }
	        } catch (SQLException | NullPointerException e) {

	        }

		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
        return false;
    }

    public static void createOVAccount(String username, String password) throws SQLException, ClassNotFoundException {
			String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
			Connection conn = MakeConnection(mainDatabase);
			PreparedStatement p = conn.prepareStatement("INSERT INTO di08.localaccounts VALUES(?,?)");
			p.setString(1, username);
			p.setString(2, hashed);
			p.execute();
			conn.close();

    }
	public static void createGoogleAccount(String email) throws SQLException, ClassNotFoundException {
			Connection conn = MakeConnection(mainDatabase);
			PreparedStatement p = conn.prepareStatement("INSERT INTO di08.googleaccounts VALUES(?)");
			p.setString(1, email);
			p.execute();
			conn.close();

	}

	public static void deleteOVAccount(String username) throws SQLException, ClassNotFoundException {
			Connection conn = MakeConnection(mainDatabase);
			PreparedStatement p = conn.prepareStatement("DELETE FROM di08.localaccounts WHERE username=?");
			p.setString(1, username);
			p.execute();
			conn.close();
	}

	public static void deleteGoogleAccount(String email) throws SQLException, ClassNotFoundException {
			Connection conn = MakeConnection(mainDatabase);
			PreparedStatement p = conn.prepareStatement("DELETE FROM di08.googleaccounts WHERE email=?");
			p.setString(1, email);
			p.execute();
			conn.close();
	}

	public static void deletePayrate(String startDate, String endDate, int id, Table database){
		try {
			Connection conn = MakeConnection(database);
			PreparedStatement p = conn.prepareStatement("DELETE FROM di08.employeerates WHERE vandatum=? AND totdatum=? AND id=?");
			p.setString(1, startDate);
			p.setString(2,endDate);
			p.setInt(3,id);
			p.execute();
			conn.close();
		} catch (ClassNotFoundException | SQLException e1) {

		}
	}


	public static List<Payrates> getPayratesSpecificEmployee(int crdnr, Table database){
		try {
			Connection conn = MakeConnection(database);
			ResultSet res = specpr(conn, crdnr);
			conn.close();
			List<Payrates> l = new ArrayList<>();
			try {
				while(res.next()) {
					try {
						l.add(new Payrates(res.getInt(1), res.getDouble(2), res.getString(3), res.getString(4)));
					} catch (ParseException e) {

					}
				}
			} catch (SQLException | NullPointerException e) {

			}
			return l;
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public static List<Payrates> getAllPayrates(Table database) {
		Connection conn;
		try {
			conn = MakeConnection(database);
			ResultSet res = allPayrates(conn);
			List<Payrates> l = new ArrayList<>();
			try {
				while(res.next()) {
					try {
						l.add(new Payrates(res.getInt(1), res.getDouble(2), res.getString(3),res.getString(4)));
					} catch (ParseException e) {

					}
				}
			} catch (SQLException | NullPointerException e) {

			}
			return l;
		} catch (ClassNotFoundException | SQLException e1) {

		}
		return null;
	}

	public static List<Employee> searchEmployees(int crdnr, String fullname, String status, int sort, Table database) {
		try {
			if(Database.tsvector() != 0 && Database.update() != 0 && Database.index() != 0) {
				Connection conn = MakeConnection(database);
				ResultSet res;
				if((crdnr != -1 || !fullname.equals("-1")) && status.equals("-1") && sort == -1) {
					res = Database.search(conn, crdnr, fullname);
				} else if(!status.equals("-1") && sort == -1) {
					res = Database.searchFilter(conn, crdnr, fullname, status);
				} else if(status.equals("-1") && sort != -1) {
					res = Database.searchSort(conn, crdnr, fullname, sort);
				} else if(sort != -1) {
					res = Database.searchFilterSort(conn, crdnr, fullname, status, sort);
				} else {
					res = null;
				}
				List<Employee> l = new ArrayList<>();
				try {
					if(!res.wasNull()) {
						while(res.next()) {
							String stat = "unknown";
							switch (res.getString(3)) {
							case "A":
								stat = "Active";
								break;
							case "I":
								stat = "Not Active";
								break;
							case "H":
								stat = "Not Active Yet";
								break;
							}
							l.add(new Employee(res.getInt(1), res.getString(2),stat));
						}
					}
				} catch (SQLException | NullPointerException e) {

				}
				return l;
			} else {
				List<Employee> l = new ArrayList<>();
				return l;
			}
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}