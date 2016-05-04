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
 * Servlet implementation class ViewAuctions
 */
@WebServlet(name = "ViewSimilarAuctions", urlPatterns = {"/ViewSimilarAuctions"})
public class ViewSimilarAuctions extends HttpServlet {
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
				+			"<h3>Viewing Similar Auctions</h3>"
				+			"<hr>"
				+			"<table border = 1 width = 100%>"
				+			"<th>Category</th>"
				+			"<th>Subcategory</th>"
				+			"<th>Description</th>"
				+			"<th>Close Date</th>"
				+			"<th>Current Max Bid</th>"
				+			"<th>View Auction For This Item</th>"
				);

		if (request.getSession().getAttribute("role").equals("rep")){
			writer.println("<th>Remove This Auction</th>");
		}

		//Create objects for connections, statements, and resultsets.
		Connection connection = null;
		Statement getAuctions = null;
		Statement getItems = null;
		Statement getBid = null;
		ResultSet auctions = null;
		ResultSet items = null;
		ResultSet bid = null;
		boolean error = false;

		//Open connection, create statement, and process request.
		try {
			connection = SQLConnector.getConnection();
			getAuctions = connection.createStatement();
			getItems = connection.createStatement();
			getBid = connection.createStatement();

			auctions = getAuctions.executeQuery("SELECT A.AuctionID,A.CloseDate,A.ItemID FROM Auction A WHERE A.Completed = 0 AND \"" + request.getParameter("category") 
					+ "\" IN (SELECT Category FROM Item WHERE ItemID = A.ItemID) AND \"" + request.getParameter("subcategory") + "\" IN (SELECT SubCategory FROM Item WHERE"
							+ " ItemID = A.ItemID) ORDER BY A.CloseDate ASC;");

			/*
			 * Process request and output HTML.
			 */
			while (auctions.next()) {
				int auctionID = auctions.getInt("AuctionID");
				items = getItems.executeQuery("SELECT Category,SubCategory,Description FROM Item WHERE ItemID =" + auctions.getString("ItemID") + ";");
				
				while (items.next()) {
					bid = getBid.executeQuery("SELECT MAX(Amount) AS MaxBid FROM Bid WHERE AuctionID = " + auctions.getInt("AuctionID") + ";");
					
					if (bid.next()) {
						writer.println("<tr>"
								+		"<td><center>" + items.getString("Category") + "</center></td>"
								+		"<td><center>" + items.getString("SubCategory") + "</center></td>"
								+ 		"<td><center>" + items.getString("Description") + "</center></td>"
								+ 		"<td><center>Closes on " + auctions.getString("CloseDate") + "</center></td>"
								+		"<td><center>" +  bid.getInt("MaxBid") + "</center></td>"
								+		"<td><center><a href = \"auction?auctionID" + auctions.getInt("AuctionID") + "\">View This Auction</a></center></td>" 
							
						);
					
						if (request.getSession().getAttribute("role").equals("rep")){
							writer.println("<td><center><form action=\"ViewAuctions\" method=\"post\" onsubmit=\"return confirm('Confirm Removal?');\">"
									+ "<INPUT TYPE=\"submit\" VALUE=\"Remove\"><input type = \"hidden\" name = \"aucID\" value = "+auctionID+"></form></center></td>");
						}
					}
				}
			}
			
			writer.println("</tr>"
					+ "</table>"
					+ "<br>"
					+ "<br>"
					+ "<br>"
			);

		} catch (SQLException s) {
			error = true;
			writer.println("Failed to get auction list: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			error = true;
			writer.println("Failed to get auction list: " + e.getMessage() + "<br>");
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
				if (bid != null) {
					bid.close();
				}
				if (getBid != null) {
					getBid.close();
				}
				if (items != null) {
					items.close();
				}
				if (getItems != null) {
					getItems.close();
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
								"<center>"
							+ 	"<br>"
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
