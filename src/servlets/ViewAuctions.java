package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
 * Servlet implementation class ViewAuctions
 */
@WebServlet(name = "ViewAuctions", urlPatterns = {"/ViewAuctions"})
public class ViewAuctions extends HttpServlet {
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
						+			"<h3>View Auctions</h3>"
						+			"<hr>"
						+			"<table border = 1 width = 100%>"
		);
		
		//Create objects for connections, statements, and resultsets.
		Connection connection = null;
		Statement getAuctions = null;
		Statement getItems = null;
		ResultSet auctions = null;
		ResultSet items = null;
				
		//Open connection, create statement, and process request.
		try {
			connection = SQLConnector.getConnection();
			getAuctions = connection.createStatement();
			getItems = connection.createStatement();
					
			auctions = getAuctions.executeQuery("SELECT AuctionID,CloseDate,ItemID FROM Auction WHERE Completed = 0 ORDER BY CloseDate ASC;");
					
					/*
					 * Process request and output HTML.
					 */
					writer.println("<tr>"
							+		"<td><center> Category </center></td>"
							+		"<td><center> Subcategory</center></td>"
							+		"<td><center> Title </center></td>"
							+ 		"<td><center> Description</center></td>"
							+ 		"<td><center>Close Date</center></td>"
							+ 		"<td><center>Link</center></td>"
					);
					while (auctions.next()) {
						int auctionID = Integer.parseInt(auctions.getString("auctionID"));
						items = getItems.executeQuery("SELECT Category,Subcategory,Description,Title FROM Item WHERE ItemID =" + auctions.getString("ItemID"));
						while (items.next()) {
							writer.println("<tr>"
									+		"<td><center>" + items.getString("Category") + "</center></td>"
									+		"<td><center>" + items.getString("Subcategory") + "</center></td>"
									+		"<td><center>"+ items.getString("Title") + "</center></td>"
									+ 		"<td><center>" + items.getString("Description") + "</center></td>"
									+ 		"<td><center>" + auctions.getString("CloseDate") + "</center></td>"
									+		"<td><center><a href = \"auction?" + auctions.getInt("AuctionID") + "\">View This Auction</a>"
							);
							if (request.getSession().getAttribute("role").equals("rep")){
								writer.println("<form action=\"ViewAuctions\" method=\"post\" onsubmit=\"return confirm('Confirm Removal?');\">"
										+ "<INPUT TYPE=\"submit\" VALUE=\"Remove\"><input type = \"hidden\" name = \"aucID\" value = "+auctionID+"></form>");
							}
							writer.println("</center></td>");
						}
					}
					
					writer.println("</table>"
							+ "<br>"
							+ "<br>"
							+ "<br>");
					
				} catch (SQLException s) {
					writer.println("Failed to get auction list: " + s.getMessage() + "<br>");
				} catch (Exception e) {
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
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
			return;
		}
		int auctionID = Integer.parseInt((String)request.getParameter("aucID"));
		try {
			Connection con = SQLConnector.getConnection();
			String sql = "Delete from auction where auctionID = "+auctionID;
			PreparedStatement s = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			s.executeUpdate();
			response.sendRedirect("ViewAuctions");
		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
}
