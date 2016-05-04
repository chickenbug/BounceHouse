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
 * Servlet implementation class ViewBidsAsRep
 */
@WebServlet(name = "ViewBidsAsRep", urlPatterns = {"/ViewBidsAsRep"})
public class ViewBidsAsRep extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewBidsAsRep() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null || !request.getSession().getAttribute("role").equals("rep")) {
			response.sendError(403, "You are not authorized to access this page.");
			return;
		}

		PrintWriter writer  = response.getWriter();

		writer.println("<html>" 
				+	"<head>" 
				+		"<title>" 
				+			"Bouncehouse Emporium - Bid List" 
				+		"</title>" 
				+	"</head>" 
				+	"<body>" 
				+		"<center>" 
				+			"<h1>Bouncehouse Emporium</h1>" 
				+			"<h3>View/Remove Bids</h3>"
				+			"<center><a href = \"GetContent\"><br><br>Home</a></center>"
				+			"<hr>"
				+			"<table border = 1 width = 100%>"
				);

		//Create objects for connections, statements, and resultsets.
		Connection connection = null;
		Statement getBids = null;
		ResultSet bids = null;

		//Open connection, create statement, and process request.
		try {
			connection = SQLConnector.getConnection();
			getBids = connection.createStatement();

			bids = getBids.executeQuery("SELECT * FROM Bid;");

			writer.println("<tr>"
					+		"<td><center> BidID </center></td>"
					+		"<td><center> AuctionID </center></td>"
					+		"<td><center> UserID </center></td>"
					+ 		"<td><center> Amount </center></td>"
					+ 		"<td><center> BidTime </center></td>"
					+		"<td><center> Options </center></td>"
					);
			while (bids.next()) {
				int bidID = Integer.parseInt(bids.getString("bidid"));
				writer.println("<tr>"
						+		"<td><center>" + bids.getString("bidid") + "</center></td>"
						+		"<td><center>" + bids.getString("auctionid") + "</center></td>"
						+		"<td><center>"+ bids.getString("userid") + "</center></td>"
						+ 		"<td><center>" + bids.getString("amount") + "</center></td>"
						+ 		"<td><center>" + bids.getString("bidtime") + "</center></td>"
						+		"<td><form action=\"ViewBidsAsRep\" method=\"post\" onsubmit=\"return confirm('Confirm Removal?');\">"
						+		"<center> <INPUT TYPE=\"submit\" VALUE=\"Remove\">"
						+		"<input type = \"hidden\" name = \"bidID\" value = "+bidID+">"
						+ 		"</center></form></td>"
						);
			}

			writer.println("</table>"
					+ "<br>"
					+ "<br>"
					+ "<br>");

		} catch (SQLException s) {
			writer.println("Failed to get bid list: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			writer.println("Failed to get bid list: " + e.getMessage() + "<br>");
			//writer.println(e.getCause());
		} finally {
			//Close resultset, statement, connection.
			try {
				if (bids != null) {
					bids.close();
				}
				if (getBids != null) {
					getBids.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sql) {
				return;
			} catch (Exception e) {
				return;
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
		int bidID = Integer.parseInt((String)request.getParameter("bidID"));
		try {
			Connection con = SQLConnector.getConnection();
			String sql = "Delete from bid where bidid = "+bidID;
			PreparedStatement s = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			s.executeUpdate();
			response.sendRedirect("ViewBidsAsRep");
			return;
		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
			response.sendRedirect("error.html");
			return;
		}
	}

}
