package nl.utwente.di.OVSoftware;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
	@Path("/status/{status}/search/{crdnr}/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Employee> status(@Context HttpServletRequest r, @PathParam("status") String s, @PathParam("crdnr") int i, @PathParam("name") String f) {
		if (Login.Security(r.getSession()) == 1) {
			return Database.statusFilter(s, i, f);
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
	@Path("/search/{crdnr}/{name}/status/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Employee> search(@Context HttpServletRequest r, @PathParam("crdnr") int n, @PathParam("name") String c, @PathParam("status") String s) {
		if (Login.Security(r.getSession()) == 1) {
			return Database.searchEmployees(n, c, s);
		}
		return null;
	}
	/*
	@POST
	@Path("/editPayrate/{payrateList}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void newPayrate(@Context HttpServletRequest r, @PathParam("payrateList") List<Payrates> l) {
		if (Login.Security(r.getSession()) == 1) {
			Database.addPayrts(l);
		}
	}*/

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
		temp.add(new Payrates(1, 60,"2019-02-06", "2019-02-07"));
		temp.add(new Payrates(1, 60,"2017-02-06", "2018-02-05"));
		temp.add(new Payrates(2, 60,"2016-02-03", "2017-02-05"));
		temp.add(new Payrates(1, 60,"2018-02-06", "2019-02-05"));
		System.out.println(Payrates.checkIntegrity(temp));
	}
	
}
