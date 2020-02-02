package hacksc20;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.*;

import java.util.List;
import java.util.ArrayList;

@WebServlet("/mapServlet")
public class mapServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
	
	protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			PrintWriter out = response.getWriter();
			
			/*****************************/
			UserDB userdb = new UserDB();
			List<ArrayList<Object>> results = userdb.getStats();
			Gson gson = new Gson();
			String json = gson.toJson(results);
			System.out.println(json);
			System.out.println("Hi");
			out.print(json);
			out.flush();
			return;
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
}