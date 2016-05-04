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
 * Servlet implementation class ViewAuctionsBySeller
 */
@WebServlet(name = "ViewAuctionsBySeller", urlPatterns = {"/ViewAuctionsBySeller"})
public class ViewAuctionsBySeller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewAuctionsBySeller() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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
				+			"Bouncehouse Emporium - Auctions" 
				+		"</title>" 
				+	"</head>" 
				+	"<body>" 
				+		"<center>" 
				+			"<h1>Bouncehouse Emporium</h1>" 
				+			"<h3>View Auctions By Seller</h3>"
				+			"<hr>"
				+			"<table border = 1 width = 100%>"
				);

		//Create objects for connections, statements, and resultsets.
		Connection connection = null;
		Statement getAuctions = null;
		Statement getItems = null;
		ResultSet auctions = null;
		ResultSet items = null;
		boolean error = false;
		//Open connection, create statement, and process request.
		try {
			connection = SQLConnector.getConnection();
			getAuctions = connection.createStatement();
			getItems = connection.createStatement();

			auctions = getAuctions.executeQuery("SELECT U.Username,A.AuctionID,A.CloseDate,A.ItemID,A.Completed FROM Auction A, User U WHERE A.UserID=U.UserID ORDER BY U.Username;");

			writer.println("<tr>"
			+		"<td><center> Seller Name </center></td>"
			+		"<td><center> Category </center></td>"
			+		"<td><center> Subcategory </center></td>"
			+ 		"<td><center> Description </center></td>"
			+ 		"<td><center> CloseDate </center></td>"
			+ 		"<td><center> Options </center></td></tr>"
			);
			/*
			 * Process request and output HTML.
			 */
			while (auctions.next()) {
				items = getItems.executeQuery("SELECT Category,SubCategory,Description FROM Item WHERE ItemID =" + auctions.getString("ItemID"));

				while (items.next()) {
					writer.println("<tr>"
							+		"<td><center>" + auctions.getString("U.Username") + "</center></td>"
							+		"<td><center>" + items.getString("Category") + "</center></td>"
							+		"<td><center>" + items.getString("SubCategory") + "</center></td>"
							+ 		"<td><center>" + items.getString("Description") + "</center></td>"
							+ 		"<td><center>Closes on " + auctions.getString("A.CloseDate") + "</center></td>");
					if(auctions.getInt("A.Completed")==0)
						writer.println("<td><center><a href = \"auction?" + auctions.getInt("A.AuctionID") + "\">View This Auction</a></center></td>");
				writer.println("</tr>");
				}
			}

			writer.println("</table>"
					+ "<br>"
					+ "<br>"
					+ "<br>");

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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
