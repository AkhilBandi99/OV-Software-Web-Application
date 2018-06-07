package nl.utwente.di.OVSoftware;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/")
public class LoginResource {
	
	UserMaps validUser = new UserMaps();

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/login/{user}/{pass}")
	public int getClichedMessage(@PathParam("user") String user, @PathParam("pass") String pass, @Context HttpServletRequest r) {
		if(validUser.findUser(user, pass)) {
			r.getSession().setAttribute("Timeout", System.currentTimeMillis());
			return 1;
		} else {
			return 0;

		}

	}

	@GET
	@Path("/logout")
	public void logOut(@Context HttpServletRequest r, @Context HttpServletResponse resp){
		r.getSession().invalidate();

	}
}
