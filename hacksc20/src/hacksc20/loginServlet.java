package hacksc20;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
	
	protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			
			/*****************************/
			String userName = request.getParameter("userName");
			System.out.println(userName);
			
			String userPass = request.getParameter("userPass");
			//System.out.println(searchedRadio+"here");
			UserDB userdb = new UserDB();
			int logincode = userdb.login(userName, userPass);
			if(logincode == 0) {
				// User found, redirect to homepage
				session.setAttribute("user", userName);
				// set session to expire in 30 minutes
				session.setMaxInactiveInterval(30*60);
				response.setStatus(HttpServletResponse.SC_OK);
			} else if(logincode == -1) {
				// User not found
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else if(logincode == -2) {
				// Bad password
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			} else if(logincode == 1) {
				// SQL exception
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			
			
			//request.setAttribute("data", json);
			//session.setAttribute("data", json);
			RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/home.jsp");
	        dispatch.forward(request, response);
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
}
