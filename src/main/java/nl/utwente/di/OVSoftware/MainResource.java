package nl.utwente.di.OVSoftware;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	@Path("/employees")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Employee> getEmployees(){
		return Database.allEmployees();
	}
	
	@GET
	@Path("/employees/{crdnr}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Payrates> getEmployees(@PathParam("crdnr") int n){
		return Database.getPayratesSpecificEmployee(n);
	}
	
	@GET
	@Path("/search/{crdnr}/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Employee> search(@PathParam("crdnr") int n, @PathParam("name") String c){
		return Database.searchEmployees(n, c);		
	}
	
	@GET
	@Path("/export.csv")
	@Produces("text/csv")
	public List<Payrates>  exportcsv(){
		return Database.getAllPayrates();
	}
	
	
	@POST
	@Path("/import")	
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void importcsv(@FormDataParam("file") InputStream in) {
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
				list.add(new Payrates(id, cost, words[2], words[1]));
			}
		}
		System.out.println(list.size());
	}
	
	
}
