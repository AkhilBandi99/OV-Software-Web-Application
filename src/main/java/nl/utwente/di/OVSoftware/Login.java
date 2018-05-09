package nl.utwente.di.OVSoftware;

import javax.servlet.http.HttpSession;

public class Login {
	
	public static Long TIMEOUT = (long) 100000;

	public static int Security(HttpSession s) {
		Object x = s.getAttribute("Timeout");
		if (x != null && x instanceof Long) {
			if (System.currentTimeMillis() - (Long) x < TIMEOUT) {
				s.setAttribute("Timeout", System.currentTimeMillis());
				return 1;
			}
		}
		return 0;
	}
	
}
