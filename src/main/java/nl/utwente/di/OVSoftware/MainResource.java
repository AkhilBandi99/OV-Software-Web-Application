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
import javax.ws.rs.*;
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
			return Database.getEmployees();
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

	@POST
	@Path("/editPayrates")
	@Consumes(MediaType.TEXT_PLAIN)
	public void editPayrates(String payrates){

		System.out.println(payrates);
		Scanner s = new Scanner(payrates);
		List<Payrates> prts = new ArrayList<>();
		while(s.hasNextLine()) {
			String line = s.nextLine();
			String[] elems = line.split(",");
			prts.add(new Payrates(Integer.parseInt(elems[0]), Double.parseDouble(elems[1]), elems[2], elems[3]));
		}
		s.close();
		Database.editPayrates(prts);
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
	@Produces(MediaType.TEXT_PLAIN)
	public String importcsv(@Context HttpServletRequest r, @FormDataParam("files") InputStream in) {
		String ret = null;
		if (Login.Security(r.getSession()) == 1) {
			List<Payrates> list = new ArrayList<>();
			Scanner s = new Scanner(in);
			while (!s.nextLine().equals("id,cost,startDate,endDate"));
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
			try {
				Payrates.checkIntegrity(list);
				Database.importPayrts(list);
			} catch(DateException e){
				ret = e.getMessage();
			}

			s.close();
		}
		return ret;
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
}
