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

/**
 * Servlet implementation class RecievedAlerts
 */
@WebServlet(name = "ReceievedAlerts", urlPatterns = {"/RecievedAlerts"})
public class RecievedAlerts extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
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
						+			"Bouncehouse Emporium - Main" 
						+		"</title>" 
						+	"</head>" 
						+	"<body>" 
						+		"<center>" 
						+			"<h1>Bouncehouse Emporium</h1>" 
						+			"<h3>View Recieved Alerts</h3>"
						+			"<hr>"
						+			"<table border = 1 width = 60%>"
		);
		
		//Create objects for connections, statements, and resultsets.
		Connection connection = null;
		Statement getAlerts = null;
		Statement getAuctions = null;
		ResultSet alerts = null;
		ResultSet auctions = null;
				
		//Open connection, create statement, and process request.
		try {
			connection = SQLConnector.getConnection();
			getAlerts = connection.createStatement();
			getAuctions = connection.createStatement();
					
			alerts = getAlerts.executeQuery("SELECT AuctionID FROM Alert WHERE UserID = \"" + request.getParameter("userID") + "\" AND Completed = 0;");
					
					while (alerts.next()) {
						auctions = getAuctions.executeQuery("SELECT CloseDate FROM Auction WHERE AuctionID = " + alerts.getInt("AuctionID") + ";");
						
						while (auctions.next()) {
							writer.println("<tr>"
									+		"<td><center>" + alerts.getString("AuctionID") + "</center></td>"
									+ 		"<td><center>Closes on " + auctions.getString("CloseDate") + "</center></td>"
/*------------->*/					+		"<td><center><a href = \"?auctionID" + alerts.getInt("AuctionID") + "\">View This Auction</a></center></td>" //Add Auction Page Here - Haikinh
									+ 	"</tr>" 
							);
						}
					}
					
					writer.println("</table>"
							+ "<br>"
							+ "<br>"
							+ "<br>");
					
				} catch (SQLException s) {
					writer.println("Failed to get alert list: " + s.getMessage() + "<br>");
				} catch (Exception e) {
					writer.println("Failed to get alert list: " + e.getMessage() + "<br>");
					//writer.println(e.getCause());
				} finally {
					//Close resultset, statement, connection.
					try {
						if (auctions != null) {
							auctions.close();
						}
						if (getAuctions != null) {
							getAuctions.close();
						}
						if (alerts != null) {
							alerts.close();
						}
						if (getAlerts != null) {
							getAlerts.close();
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
					writer.println("<br>"
								+ 	"<br>"
								+	"<a href = \"GetContent\">Return To Main Page</a>"
								+	"</center>"
								+	"</body"
								+	"</html>"
					);
				}
		}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
