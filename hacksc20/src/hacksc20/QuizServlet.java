package hacksc20;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/quizServlet")
public class QuizServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
	
	protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			boolean loggedIn = true;
			String zipStr;
			int zip = 0;
			String userName = "";
			if(session.getAttribute("user") == null) {
				System.out.println("User not logged in!");
				loggedIn = false;
				zipStr = request.getParameter("zip-input");
				zip = Integer.parseInt(zipStr);
				//response.setStatus(400);
				//out.println("User not logged in");
				//return;
			} else {
				userName = session.getAttribute("user").toString();
			}
			
			String scoreStr = request.getParameter("result");
			int score = Integer.parseInt(scoreStr);
			UserDB udb = new UserDB();
			
			if(loggedIn) {
				udb.log(userName, score);
			} else {
				udb.logGuest(score, zip);
			}
			
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/map.html");
        dispatch.forward(request, response);
	}
	
}
