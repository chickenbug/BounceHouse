package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import model.SQLConnector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RecievedAlerts
 */
@WebServlet(name = "ReceievedAlerts", urlPatterns = {"/RecievedAlerts"})
public class RecievedAlerts extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
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
						+			"Bouncehouse Emporium - Main" 
						+		"</title>" 
						+	"</head>" 
						+	"<body>" 
						+		"<center>" 
						+			"<h1>Bouncehouse Emporium</h1>" 
						+			"<h3>View Recieved Alerts</h3>"
						+			"<hr>"

		);
		
		//Create objects for connections, statements, and resultsets.
		Connection connection = null;
		Statement getAlerts = null;
		Statement getItem = null;
		Statement getAuction = null;
		ResultSet alerts = null;
		ResultSet item = null;
		ResultSet auction = null;
		boolean error = false;
		int count = 0;
				
		//Open connection, create statement, and process request.
		try {
			connection = SQLConnector.getConnection();
			getAlerts = connection.createStatement();
			getItem = connection.createStatement();
			getAuction = connection.createStatement();
					
			alerts = getAlerts.executeQuery("SELECT ItemID FROM Alert WHERE UserID = \"" + request.getParameter("userID") + "\";");
					
					while (alerts.next()) {
						
						if (count == 0) {
							writer.println(
										"<table border = 1 width = 60%>"
									+		"<th>Auction #</th>"
									+		"<th>Category</th>"
									+		"<th>Subcategory</th>"
									+		"<th>Bounciness</th>"
									+		"<th>Size</th>"
									+		"<th>Color</th>"
									+		"<th>View Auction For This Item</th>");
						}
						
						item = getItem.executeQuery("SELECT Bounciness,Category,Color,Size,Subcategory FROM Item WHERE ItemID = " + alerts.getInt("ItemID") + ";");
						
						if (item.next()) {
							auction = getAuction.executeQuery("SELECT AuctionID FROM Auction WHERE ItemID = " + item.getInt("itemID") + ";");
							
							if (auction.next()) {
								writer.println("<tr>"
										+		"<td><center>" + auction.getInt("AuctionID") + "</center></td>"
										+ 		"<td><center>" + item.getString("Category") + "</center></td>"
										+ 		"<td><center>" + item.getString("Subcategory") + "</center></td>"
										+ 		"<td><center>" + item.getInt("Bounciness") + "</center></td>"
										+ 		"<td><center>" + item.getString("Size") + "</center></td>"
										+ 		"<td><center>" + item.getString("Color") + "</center></td>"
										+		"<td><center><a href = \"auction?auctionID=" + alerts.getInt("AuctionID") + "\">View This Auction</a></center></td>"
										+ 	"</tr>" 
								);
							}
						}
						
						count++;
					}
					
					writer.println("</table>"
							+ "<br>"
							+ "<br>"
							+ "<br>");
					
				} catch (SQLException s) {
					error = true;
					writer.println("Failed to get alert list: " + s.getMessage() + "<br>");
				} catch (Exception e) {
					error = true;
					writer.println("Failed to get alert list: " + e.getMessage() + "<br>");
					//writer.println(e.getCause());
				} finally {
					//Close resultset, statement, connection.
					try {
						if (auction != null) {
							auction.close();
						}
						if (getAuction != null) {
							getAuction.close();
						}
						if (item != null) {
							item.close();
						}
						if (getItem != null) {
							getItem.close();
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
								+	"<a href = \"GetContent\">Home</a>"
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
