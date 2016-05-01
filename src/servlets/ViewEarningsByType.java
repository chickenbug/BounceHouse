package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SQLConnector;

/**
 * Servlet implementation class ViewEarningsByType
 */
@WebServlet("/ViewEarningsByType")
public class ViewEarningsByType extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
		}
		PrintWriter writer  = response.getWriter();
		
		writer.println("<html>" 
				+	"<head>" 
				+		"<title>" 
				+			"Bouncehouse Emporium - Total Earnings By Type" 
				+		"</title>" 
				+	"</head>" 
				+	"<body>" 
				+		"<center>" 
				+			"<h1>Bouncehouse Emporium</h1>" 
				+			"<h3>Total Earnings By Type</h3>"
				+			"<hr>"
				+			"<table border = 1 width = 100%>"
				);
		
		Connection connection = null;
		Statement getEarnings = null;
		ResultSet earnings = null;

		//Open connection, create statement, and process request.
		try {
			connection = SQLConnector.getConnection();
			getEarnings = connection.createStatement();
			String query = "";
			
			if (request.getParameter("sortBy") == null || request.getParameter("sortBy").equals("noSort")) {
				query.concat("ORDER BY ItemID ASC"); //sort in terms of itemID number low to high if no sorting specified.
			} else if (request.getParameter("sortBy").equals("bouncinessASC")) { //Bounciness low to high
				query.concat("ORDER BY Bounciness ASC;");
			}
			/*
			 * Process request and output HTML.
			 */
			writer.println("<tr>"
					+		"<td><center><span style = \"font-weight:bold\"> Username </span></center></td>"
					+		"<td><center><span style = \"font-weight:bold\"> Earnings </span></center></td>");

			writer.println("</table>"
					+ "<br>"
					+ "<br>"
					+ "<br>");

		} catch (SQLException s) {
			writer.println("Failed to get earnings list: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			writer.println("Failed to get earnings list: " + e.getMessage() + "<br>");
			//writer.println(e.getCause());
		} finally {
			//Close resultset, statement, connection.
			try {
				if (earnings != null) {
					earnings.close();
				}
				if (getEarnings != null) {
					getEarnings.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sql) {
				/*
				 * Do nothing. The page has already loaded - no need to let the user know
				 * there was an error that doesn't affect them.
				 */
			} catch (Exception e) {
				/*
				 * Do nothing. The page has already loaded - no need to let the user know
				 * there was an error that doesn't affect them.
				 */
			}

			//Write closing html for page.
			writer.println(
					"</center>"
							+		"</body"
							+	"</html>"
					);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
