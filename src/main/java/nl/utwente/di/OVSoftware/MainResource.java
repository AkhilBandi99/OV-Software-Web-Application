package nl.utwente.di.OVSoftware;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
	
}
