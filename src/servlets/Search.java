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

@WebServlet(name = "Search", urlPatterns = {"/Search"})
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
			return;
		}
		
		PrintWriter writer  = response.getWriter();
		writer.println("<html>" +
							"<head>" +
								"<title>" +
									"Bouncehouse Emporium - Search" +
								"</title>" +
							"</head>" +
							"<body>" +
								"<center>" +
								"<h1>Bouncehouse Emporium</h1>" +
								"<h3>Search Results</h3>" +
								"<hr>"
		);	
		
		/*
		 * Boolean statements to keep track of what the user searched for. 
		 * Relevance level is just a measure of how many fields matched the user's search.
		 * In the current implementation, these are all disabled.
		 * Re-enabling them would allow to return *partially* relevant results for the search -
		 * but as it stands, the current implementation takes the least confusing route for the end
		 * user and only returns tuples matching their query exactly.
		 */
		Connection connection = null;
		Statement getAuction = null;
		Statement getItems = null;
		ResultSet auction = null;
		ResultSet items = null;
		boolean error = false;
		boolean hasOtherParam = false;
		int count = 0;
		//boolean hasParamBounciness = false;
		//boolean hasParamCategory = false;
		//boolean hasParamColor = false;
		//boolean hasParamSize = false;
		//boolean hasParamSubcategory = false;
		String query = null;
		//int relevanceLevel = 0;
		//Node R5 = null;
		//Node R4 = null;
		//Node R3 = null;
		//Node R2 = null;
		//Node R1 = null;
		
		try {
			connection = SQLConnector.getConnection();
			getAuction = connection.createStatement();
			getItems = connection.createStatement();
			query = "SELECT * FROM Item";

			if(request.getParameter("bounciness") != null || !request.getParameter("bounciness").equals("")) {
				hasOtherParam = true;
				//hasParamBounciness = true;
				query += " WHERE Bounciness = \"" + request.getParameter("bounciness") + "\"";
			} 
			if (request.getParameter("category") != null || !request.getParameter("category").equals("")) {
				if (hasOtherParam) {
					query += " AND ";
				} else {
					query += " WHERE ";
				}
				
				hasOtherParam = true;
				//hasParamCategory = true;
				query += "Category = \"" + request.getParameter("category") + "\"";
			}
			if (request.getParameter("color") != null || !request.getParameter("color").equals("")) {
				if (hasOtherParam) {
					query += " AND ";
				} else {
					query += " WHERE ";
				}
				
				hasOtherParam = true;
				//hasParamColor = true;
				query += "Color = \"" + request.getParameter("color") + "\"";
			}
			if (request.getParameter("size") != null || !request.getParameter("size").equals("")) {
				if (hasOtherParam) {
					query += " AND ";
				} else {
					query += " WHERE ";
				}
				
				hasOtherParam = true;
				//hasParamSize = true;
				query += "Size = \"" + request.getParameter("size") + "\"";
			}
			if (request.getParameter("subcategory") != null || !request.getParameter("subcategory").equals("")) {
				if (hasOtherParam) {
					query += " AND ";
				} else {
					query += " WHERE ";
				}
				
				hasOtherParam = true;
				//hasParamSubcategory = true;
				query += "Subcategory = \"" + request.getParameter("subcategory") + "\"";
			}
			
			query += ";";
			
			items = getItems.executeQuery(query);
			
			while (items.next()) {
				
				if (count == 0) {
					writer.println(
								"<table border = 1 width = 75%>"
							+		"<th>Category</th>"
							+		"<th>Subcategory</th>"
							+		"<th>Description</th>"
							+		"<th>Bounciness</th>"
							+		"<th>Color</th>"
							+		"<th>Size</th>"
					);
				}
				
				auction = getItems.executeQuery("SELECT AuctionID,Completed FROM Auction WHERE ItemID = " + items.getInt("ItemID") + ";");
				
				if (auction.next()) {
					if (auction.getInt("Completed") == 0) {
						writer.println("<tr>"
								+		"<td><center>" + items.getString("Category") + "</center></td>"
								+		"<td><center>" + items.getString("Subcategory") + "</center></td>"
								+ 		"<td><center>" + items.getString("Description") + "</center></td>"
								+ 		"<td><center>" + items.getInt("Bounciness") + "</center></td>"
								+		"<td><center>" + items.getString("Color") + "</center></td>"
								+		"<td><center>" + items.getString("Size") + "</center></td>"
								+		"<td><center><a href = \"auction?auctionID=" + auction.getInt("AuctionID") + "\">View Auction</a></center></td>"
								+ 	"</tr>" 
						);
					}
				}
				
				count++;
			}
			
			if (count == 0) {
				writer.println("We're sorry, but there were no items matching your search query.<br>");
			}
			
			writer.println("</table>"
						+ "<br>"
						+ "<br>"
						+ "<br>");
					
			} catch (SQLException s) {
				error = true;
				writer.println("Search failed: " + s.getMessage() + "<br>");
			} catch (Exception e) {
					error = true;
					writer.println("Search failed: " + e.getMessage() + "<br>");
					//writer.println(e.getCause());
				} finally {
					//Close resultset, statement, connection.
					try {
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
					writer.println("<br>"
								+	"<a href = \"GetContent\">Return to Main Page</a>"
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
