package nl.utwente.di.OVSoftware;

import javax.ws.rs.Path;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/admin")
public class AdminResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ovusers")
	public List<OVAccount> getAllOVUsers(@Context HttpServletRequest r) {
		List<OVAccount> ovlist = Database.getOVAccounts();
		return ovlist;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/googleusers")
	public List<GoogleAccount> getAllGoogleUsers(@Context HttpServletRequest r){
		List<GoogleAccount> googlelist = Database.getGoogleAccounts();
		return googlelist;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/ovuser")
	public void createOVUser(OVAccount ovAccount){
		Database.createOVAccount(ovAccount.getUsername(),ovAccount.getPassword());
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/googleuser")
	public void createGoogleUser(GoogleAccount googleAccount){
		//Database.createGoogleAccount(googleAccount.getEmail());
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/ovuser")
	public void deleteOVUser(OVAccount ovAccount){
		//Database.deleteOVAccount(ovAccount.getUsername(),ovAccount.getPassword());
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/googleuser")
	public void deleteGoogleUser(GoogleAccount googleAccount){
		//Database.deleteGoogleAccount(googleAccount.getEmail());
	}
	
	
}
