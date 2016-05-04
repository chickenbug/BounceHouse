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
 * Servlet implementation class ViewAuctions
 */
@WebServlet(name = "ViewTotalEarnings", urlPatterns = {"/ViewTotalEarnings"})
public class ViewTotalEarnings extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null||!request.getSession().getAttribute("role").equals("admin")) {
			response.sendError(403, "You are not authorized to access this page.");
			return;
		}

		//PrintWriter to write to HTML.
		PrintWriter writer  = response.getWriter();

		/*
		 * Set up page title and links. The structure of the println statement is only to mimic 
		 * the structure of a formatted HTML document. This structure is only maintained below 
		 * when it is conducive to readability; when this structure inhibits code readability,
		 * I do not use it.
		 */
		writer.println("<html>" 
				+	"<head>" 
				+		"<title>" 
				+			"Bouncehouse Emporium - Total Earnings" 
				+		"</title>" 
				+	"</head>" 
				+	"<body>" 
				+		"<center>" 
				+			"<h1>Bouncehouse Emporium</h1>" 
				+			"<h3>Total Earnings</h3>"
				);

		//Create objects for connections, statements, and resultsets.
		Connection connection = null;
		Statement getAuctions = null;
		ResultSet auctions = null;

		//Open connection, create statement, and process request.
		try {
			connection = SQLConnector.getConnection();
			getAuctions = connection.createStatement();

			auctions = getAuctions.executeQuery("SELECT A.WinBid,U.Username FROM Auction A,User U WHERE Completed = 1 AND A.UserID=U.UserID;");

			Map<String,Integer> users = new HashMap<String,Integer>();
			while(auctions.next()){
				if(users.containsKey(auctions.getString("U.username"))){
					users.put(auctions.getString("U.username"),users.get(auctions.getString("U.username"))+auctions.getInt("A.WinBid"));
				}
				else users.put(auctions.getString("U.username"), auctions.getInt("A.WinBid"));
			}
			int total = 0;
			for(Entry<String, Integer> entry: users.entrySet()){
				total+=entry.getValue();
			}
			writer.println(	"<h4> $"+total+"</h4>"
					+	"<h4>	<a href = \"salesReports.jsp\">Return to sales reports page</a> </h4>"
					+			"<hr>"
					+			"<table border = 1 width = 100%>" 
					+	"<tr>"
					+		"<td><center><span style = \"font-weight:bold\"> Username </span></center></td>"
					+		"<td><center><span style = \"font-weight:bold\"> Earnings </span></center></td>");
			for(Entry<String, Integer> entry: users.entrySet()){
				writer.println("<tr><td><center>" + entry.getKey() + "</center></td>"
						+		"<td><center>" + entry.getValue() + "</center></td>");
			}

			writer.println("</table>"
					+ "<br>"
					+ "<br>"
					+ "<br>");

		} catch (SQLException s) {
			writer.println("Failed to get earnings list: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			writer.println("Failed to get earnings list: " + e.getMessage() + "<br>");
		} finally {
			//Close resultset, statement, connection.
			try {
				if (auctions != null) {
					auctions.close();
				}
				if (getAuctions != null) {
					getAuctions.close();
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
