package hacksc20;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logoutServlet")
public class logoutServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
	
	protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			session.setAttribute("user", null);
			
			/*****************************/
			
				// User found, redirect to homepage
				// set session to expire in 30 minutes
			
			
			//request.setAttribute("data", json);
			//session.setAttribute("data", json);
			/*
			RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/HomePage.jsp");
	        dispatch.forward(request, response);
	        */
			String redirect = 
					response.encodeRedirectURL(request.getContextPath() + "/HomePage.jsp");
			response.sendRedirect(redirect);
	        System.out.println("logging off");
	        System.out.println(session.getAttribute("user"));
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
}
