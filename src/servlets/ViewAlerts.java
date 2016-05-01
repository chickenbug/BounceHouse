package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SQLConnector;

@WebServlet(name = "ViewAlerts", urlPatterns = {"/ViewAlerts"})
public class ViewAlerts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
		}
		
		PrintWriter writer  = response.getWriter();
		
		writer.println("<html>" +
							"<head>" +
								"<title>" +
									"Bouncehouse Emporium - Manage Alerts" +
								"</title>" +
							"</head>" +
							"<body>" +
								"<center>" +
									"<h1>Bouncehouse Emporium</h1>" +
									"<h3>Manage Alerts</h3>"+
								"</center><br><br>" +
								"<hr>"
		);	
		
		Connection connection = null;
		Statement statement = null;
		Statement statement2 = null;
		ResultSet wishlist = null; 
		ResultSet count = null;
		boolean error = false;	
		
		if (request.getParameter("userID") == null) {
			writer.println("Error: cannot load alert list without a user ID specified!<br>"
					+ 	"Please click <a href = \"GetContent?\">here</a> to return to the main page and try again."
					+ 	"</body>"
					+ 	"</html>"
			);
			return;
		} else {
			writer.println("<center>"
					+ "<table border = 1 width = 75%>"
			);
		}
		
		
		try {
			connection = SQLConnector.getConnection();
			statement = connection.createStatement();
			statement2 = connection.createStatement();

			count = statement2.executeQuery("SELECT COUNT(*) AS Count FROM Wishlist WHERE UserID = " + request.getParameter("userID") + ";");
			
			if (count.next()) {
				if (count.getInt("Count") == 0) {
					throw new SQLException("There are no alerts associated with this userID.");
				}
			} else {
				throw new SQLException("An error occurred while trying to get the number of alerts associated with this account.");
			}
			
			//If username and password are properly set (that is, not null) go ahead and query the DB.
			wishlist = statement.executeQuery("SELECT ListID,ItemID,Category,Subcategory FROM Wishlist WHERE UserID = " + request.getParameter("userID") + ";");
			
			//writer.println("Connection created. Statement created. Query Executed.<br>");
			
			//Assuming no screwups, there should be one row with field Count == 1. 
			while (wishlist.next()) {
				writer.println("<tr>"
							+		"<td><center>Item #" + wishlist.getString("ItemID") + "</center></td>"
							+		"<td><center>" + wishlist.getString("Category") + "</center></td>"
							+ 		"<td><center>" + wishlist.getString("Subcategory") + "</center></td>"
							+		"<td><center><a href = \"DeleteAlert?listID=" + wishlist.getInt("ListID") + "\">Delete Alert For This Item</a></center></td>"
							+ 	"</tr>" 
				);
			}
			
			writer.println("</table>"
					+ "</center>"
					+ "<center>"
					+ "<br>"
					+	"<a href = \"DeleteAllAlerts?userID=" + request.getParameter("userID") +"\">Delete All Alerts Associated With This Account</a>"
					+ "<br>"
					+ "<br>"
					+ "<br>"
			);
			
		} catch (SQLException s) {
			error = true;
			writer.println("Failed to get alert list: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			error = true;
			writer.println("Failed to get alert list: " + e.getMessage() + "<br>");
			//writer.println(e.getCause());
		} finally {
			try {
				if (wishlist != null) {
					wishlist.close();
				}
				
				if (count != null) {
					count.close();
				}
				
				if (statement != null) {
					statement.close();
				}
				
				if (statement2 != null) {
					statement2.close();
				}
				
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException s) {
				error = true;
				writer.println("An SQL exception occured while attempting to close SQL objects: " + s.getMessage() + "<br>");
			} catch (Exception e) {
				error = true;
				writer.println("A general exception occurred while attempting to close SQL objects: " + e.getMessage() + "<br>");
			}
			
			if (error) {
				writer.println("</table>"
						+ "</center>"
						+ "<center>"
						+ "Please click <a href = \"ViewAlerts?userID=" + request.getParameter("userID") + "\">here</a> to try again."
						+ "<br>"
						+ "<br>"
						+ "<a href = \"GetContent\">Return To Main Page</a>"
						+ "</center>"
						+ "</body>"
						+ "</html>"
				);
			} else {
				writer.println("<body>" 
							+  "<html>"
				);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
