package servlets;

/*	
 * Servlet requires several exceptions to function, SQL functions,
 * and servlet/HTTP functions.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import model.SQLConnector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author thomasbrown - Team 15
 * GetContent should be the main landing page after a successful login for a standard user.
 * Users will see links to view ongoing auctions, search, ask a question, see their acount, and manage/view alerts.
 * After these links, the user will be provided with a list of items and quick info on them.
 */

/*
 * The @WebServlet annotation prevents having to manually set up servlet paths in web.xml.
 * When linking to this servlet, simply specify the name in the annotation, and the server
 * will understand to link to the URL pattern below. Java servlets need not be linked to 
 * using their filename; only the class name is necessary. 
 * e.g. <a href = "Servlet"></a> is a valid link.
 */
@WebServlet(name = "GetContent", urlPatterns = {"/GetContent"})
public class GetContent extends HttpServlet {
	//Useless code according to the internet but Eclipse complains if you remove it.
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null || request.getSession().getAttribute("username") == null) {
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
						+			"<h3>Central Hub</h3>" 
						+			"<a href = \"create_auction\">Create An Auction</a> | "
						+			"<a href = \"createAlert.jsp\">Create Alert</a> | "
						+ 			"<a href = \"search.jsp\">Search</a> | "
						+			"<a href = \"ViewQuestions\">Ask A Question/Contact Us</a> | "
						+			"<a href = \"ViewAccount?userID=" + request.getSession().getAttribute("userID") + "\">View Account</a> | "
						+			"<a href = \"ViewAlerts?userID=" + request.getSession().getAttribute("userID") + "\">Manage Alerts</a> | "
						+			"<a href = \"RecievedAlerts?userID=" + request.getSession().getAttribute("userID") + "\">View Recieved Alerts</a> | "
						+			"<a href = \"Logout\">Logout</a>"
		);
		
		if (request.getSession().getAttribute("role").toString().equals("admin")) {
			writer.println("<hr>"
					+ 		"<a href = \"adminFunctions.jsp\">Administrator Functions</a>"
			);
		} else if (request.getSession().getAttribute("role").toString().equals("rep")) {
			writer.println("<hr>"
					+		"<a href = \"repFunctions.jsp\">Customer Representative Functions</a>"
			);
		}
			writer.println("</center>" 
						+		"<hr>" 
						+		"<center>"	
						+		"<form action = \"GetContent\" method = \"post\">"
						+			"<select name = \"sortBy\" id = \"sortBy\">"
						+				"<option value = \"noSort\" selected>Do Not Sort Results</option>"
						+				"<option value = \"bouncinessASC\">Bounciness Low to High</option>"
						+				"<option value = \"bouncinessDESC\">Bounciness High to Low</option>"
						+				"<option value = \"categoryASC\">Category A to Z</option>"
						+				"<option value = \"categoryDESC\">Category Z to A</option>"
						+				"<option value = \"sizeDESC\">Size S to L</option>"
						+				"<option value = \"sizeASC\">Size L to S</option>"
						+				"<option value = \"subcategoryASC\">Subcategory A to Z</option>"
						+				"<option value = \"subcategoryDESC\">Subcategory Z to A</option>"
						+			"</select>"
						+			"<input type = \"submit\" value = \"Apply\">"
						+		"</form>"
						+		"<table border = 1 width = 100%>"
						+			"<th>Category</th>"
						+			"<th>Subcategory</th>"
						+			"<th>Description</th>"
						+			"<th>Bounciness</th>"
						+			"<th>Color</th>"
						+			"<th>Size</th>"
						+			"<th>See The Auction For This Item</th>"
		);	
		
		//Create objects for connections, statements, and resultsets.
		Connection connection = null;
		Statement getItems = null;
		Statement getAuction = null;
		ResultSet items = null;
		ResultSet auction = null;
		boolean error = false;
		
		//Open connection, create statement, and process request.
		try {
			connection = SQLConnector.getConnection();
			getItems = connection.createStatement();
			getAuction = connection.createStatement();
			String query = "SELECT * FROM Item ";
			
			//Implement sorting:
			if (request.getParameter("sortBy") == null || request.getParameter("sortBy").equals("noSort")) {
				//writer.println("no sort<br>");
				query += "ORDER BY ItemID ASC;"; //sort in terms of itemID number low to high if no sorting specified.
			} else if (request.getParameter("sortBy").equals("bouncinessASC")) { //Bounciness low to high
				//writer.println("bounciness low/high");
				query += "ORDER BY Bounciness ASC;";
			} else if (request.getParameter("sortBy").equals("bouncinessDESC")) { //Bounciness high to low
				query += "ORDER BY Bounciness DESC;";
			} else if (request.getParameter("sortBy").equals("categoryASC")) { //Category A to Z
				query += "ORDER BY Category ASC;";
			} else if (request.getParameter("sortBy").equals("categoryDESC")) { //Category Z to A
				query += "ORDER BY Category DESC;";
			} else if (request.getParameter("sortBy").equals("sizeASC")) { //Size XS to XL
				query += "ORDER BY Size ASC;";
			} else if (request.getParameter("sortBy").equals("sizeDESC")) { //Size XL to XS
				query += "ORDER BY Size DESC;";
			} else if (request.getParameter("sortBy").equals("subcategoryASC")) { //Subcategory A to Z
				query += "ORDER BY Subcategory ASC;";
			} else if (request.getParameter("sortBy").equals("subcategoryDESC")) { //Subcategory Z to A
				query += "ORDER BY Subcategory DESC;";
			}
			
			//Send query to MySQL.
			items = getItems.executeQuery(query);
			
			/*
			 * Process request and output HTML. I may update this to 
			 * be more compact and include a link to a ViewItem
			 * servlet that has more info.
			 */
			while (items.next()) {
				auction = getAuction.executeQuery("SELECT AuctionID,Completed FROM Auction WHERE ItemID = " + items.getInt("ItemID") + ";");
				
				if (auction.next()) {
					if (auction.getInt("Completed") == 0) {
						writer.println("<tr>"
									+		"<td><center>" + items.getString("Category") + "</center></td>"
									+		"<td><center>" + items.getString("Subcategory") + "</center></td>"
									+ 		"<td><center>" + items.getString("Description") + "</center></td>"
									+ 		"<td><center>" + items.getInt("Bounciness") + "</center></td>"
									+		"<td><center>" + items.getString("Size") + "</center></td>"
									+		"<td><center><a href = \"auction?" + auction.getInt("AuctionID") + "\">View Auction For This Item</a></center></td>"
									+ 	"</tr>" 
						);
					}
				}
			}
			
			writer.println("</table>"
					+ "<br>"
					+ "<br>"
					+ "<br>");
			
		} catch (SQLException s) {
			error = true;
			writer.println("Failed to get item list: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			error = true;
			writer.println("Failed to get item list: " + e.getMessage() + "<br>");
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
				if (auction != null) {
					auction.close();
				}
				if (getAuction != null) {
					getAuction.close();
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

	//congrats on making it this far into the file
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}