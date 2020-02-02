package hacksc20;

import java.io.IOException;
import java.io.PrintWriter;

//import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/registerServlet")
public class registerServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
	
	protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			PrintWriter out = response.getWriter();
			
			/*****************************/
			String userName = request.getParameter("userName");
			System.out.println(userName);
			
			String userPass = request.getParameter("userPass");
			String passConf = request.getParameter("passConf");
			String zipString = request.getParameter("zip");
			int zip = Integer.parseInt(zipString);
			if( !(userPass.equals(passConf)) ) {
				// passwords not equal
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.print("Passwords do not match<br>");
			}
			//System.out.println(searchedRadio+"here");
			UserDB userdb = new UserDB();
			int logincode = userdb.register(userName, userPass, zip);
			if(logincode == 0) {
				// User found, redirect to homepage
				session.setAttribute("user", userName);
				// set session to expire in 30 minutes
				session.setMaxInactiveInterval(30*60);
				response.setStatus(HttpServletResponse.SC_OK);
			} else if(logincode == -1) {
				// User already exists
				response.setStatus(400);
				out.print("Username not available");
			} else if(logincode == 1) {
				// SQL exception
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				out.print("Internal Server Error");
			}
			
			
			//request.setAttribute("data", json);
			//session.setAttribute("data", json);
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
}
