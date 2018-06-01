package nl.utwente.di.OVSoftware;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.impl.CsvReader;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javassist.bytecode.analysis.Type;

@Path("/main")
public class MainResource {

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
					list.add(new Payrates(id, cost, words[2], words[1]));
				}
			}
			// delete the table
			Database.emptyAllTables();
			// rewrite the database
			for (Payrates p : list) {
				Database.addPayrate(p.getId(), (int) p.getCost(), p.getStartDate(), p.getEndDate());
			}
		}
	}

}
