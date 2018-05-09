package nl.utwente.di.OVSoftware;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/main")
public class MainResource {
	
	public List<Employee> getEmployees() {
		return null;
	}
	
	@GET
	@Path("/search/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Employee> search(@PathParam("name") String n){
		return null;
	}
	
}
