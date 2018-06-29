package nl.utwente.di.OVSoftware;

import javax.ws.rs.Path;

import java.sql.SQLException;
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
import javax.ws.rs.core.Response;

import com.sun.glass.ui.delegate.MenuItemDelegate;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/admin")
public class AdminResource {
	
	//Returns a list off all OV accounts.
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ovusers")
	public List<OVAccount> getAllOVUsers(@Context HttpServletRequest r) {
		if(Login.Security(r.getSession())==1) {
			List<OVAccount> ovlist = Database.getOVAccounts();
			return ovlist;
		}
		return null;
	}
	
	//Returns a list of all Google accounts.
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/googleusers")
	public List<GoogleAccount> getAllGoogleUsers(@Context HttpServletRequest r){
		if(Login.Security(r.getSession())==1) {
			List<GoogleAccount> googlelist = Database.getGoogleAccounts();
			return googlelist;
		}
		return null;
	}

	//Creates a new OV account.
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/ovuser")
	public void createOVUser(@Context HttpServletRequest r, OVAccount ovAccount) throws SQLException, ClassNotFoundException {
		if(Login.Security(r.getSession())==1) {
			Database.createOVAccount(ovAccount.getUsername(), ovAccount.getPassword());
		}
	}

	//Creates a new Google account.
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/googleuser")
	public void createGoogleUser(@Context HttpServletRequest r, GoogleAccount googleAccount) throws SQLException, ClassNotFoundException {
		if(Login.Security(r.getSession())==1) {
			Database.createGoogleAccount(googleAccount.getEmail());
		}
	}

	//Deletes an OV accounts.
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/ovuser")
	public void deleteOVUser(@Context HttpServletRequest r,OVAccount ovAccount) throws SQLException, ClassNotFoundException {
		if(Login.Security(r.getSession())==1) {
			Database.deleteOVAccount(ovAccount.getUsername());
		}
	}

	//Deletes a Google account.
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/googleuser")
	public void deleteGoogleUser(@Context HttpServletRequest r, GoogleAccount googleAccount) throws SQLException, ClassNotFoundException {
		if(Login.Security(r.getSession())==1) {
			Database.deleteGoogleAccount(googleAccount.getEmail());
		}
	}
	
	
}
