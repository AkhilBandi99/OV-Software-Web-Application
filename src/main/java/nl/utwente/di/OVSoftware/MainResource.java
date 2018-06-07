package nl.utwente.di.OVSoftware;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import nl.utwente.di.OVSoftware.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/main")
public class MainResource {
	
	DatabaseMaps tables = new DatabaseMaps();

	@GET
	@Path("/status/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Employee> status(@Context HttpServletRequest r, @PathParam("status") String s) {
		if (Login.Security(r.getSession()) == 1) {
			return Database.statusFilter(s);
		}
		return null;
	}

	@GET
	@Path("/employees")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Employee> getEmployees(@Context HttpServletRequest r) {
		if (Login.Security(r.getSession()) == 1) {
			return Database.allEmployees();
		}
		return null;
	}

	@GET
	@Path("/employees/{crdnr}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Payrates> getEmployees(@Context HttpServletRequest r, @PathParam("crdnr") int n) {
		if (Login.Security(r.getSession()) == 1) {
			return Database.getPayratesSpecificEmployee(n);
		}
		return null;
	}

	@GET
	@Path("/search/{crdnr}/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Employee> search(@Context HttpServletRequest r, @PathParam("crdnr") int n,
			@PathParam("name") String c) {
		if (Login.Security(r.getSession()) == 1) {
			return Database.searchEmployees(n, c);
		}
		return null;
	}

	@GET
	@Path("/export.csv")
	@Produces("text/csv")
	public List<Payrates> exportcsv(@Context HttpServletRequest r) {
		if (Login.Security(r.getSession()) == 1) {
			return Database.getAllPayrates();
		}
		return null;
	}

	@POST
	@Path("/import")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void importcsv(@Context HttpServletRequest r, @FormDataParam("file") InputStream in) {
		if (Login.Security(r.getSession()) == 1) {
			List<Payrates> list = new ArrayList<>();
			Scanner s = new Scanner(in);
			while (!s.nextLine().equals("id,cost,startDate,endDate"))
				;
			String line = " ";
			while (s.hasNextLine() || line.equals("")) {
				line = s.nextLine();
				String[] words = line.split(",");
				if (words.length == 4) {
					int id = Integer.parseInt(words[0]);
					double cost = Double.parseDouble(words[1]);
					list.add(new Payrates(id, cost, words[2], words[3]));
				}
			}
			// delete the table
			Database.emptyAllTables();
			System.out.println(list.size());
			// rewrite the database
			Database.addPayrts(list);
			s.close();
		}
	}

	@GET
	@Path("/databases")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getDatabases(@Context HttpServletRequest r) {
		if (Login.Security(r.getSession()) == 1) {
			return tables.getDatabases();
		}
		return null;
	}
	
	@POST
	@Path("/databases/{selection}")
	@Produces(MediaType.APPLICATION_JSON)
	public void selectDatabases(@Context HttpServletRequest r, @PathParam("selection") String n) {
		if (Login.Security(r.getSession()) == 1) {
			r.getSession().setAttribute("Database", n);
		}
	}
	
	public static void main(String[] args) {
		List<Payrates> temp = new ArrayList<Payrates>();
		temp.add(new Payrates(1, 60,"2016-02-01", "2017-02-05"));
		temp.add(new Payrates(1, 60,"2017-02-06", "2018-02-05"));
		temp.add(new Payrates(2, 60,"2016-02-03", "2017-02-05"));
		temp.add(new Payrates(1, 60,"2018-02-06", "2017-02-05"));
		System.out.println(checkDates(temp));
	}

	public static int checkDates(List<Payrates> head) {
		System.out.println(head);
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
				if (!temp.get(i2).isNextDate(temp.get(++i2).getStartDate())) {
					System.out.println(temp.get(i2 - 1).getId() + " " + temp.get(i2 - 1).getEndDate() + " " + temp.get(i2).getId() + " " +  temp.get(i2).getStartDate());
					return head.indexOf(temp.get(i2 - 1));
				}
			}
		}
		return -1;
	}
	
}
