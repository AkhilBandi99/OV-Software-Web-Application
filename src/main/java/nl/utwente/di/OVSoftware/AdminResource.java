package nl.utwente.di.OVSoftware;

import javax.ws.rs.Path;

import java.util.ArrayList;
import java.util.List;

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

@Path("/admin")
public class AdminResource {
	
	UserMaps users = new UserMaps();
	
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
	
	
}
