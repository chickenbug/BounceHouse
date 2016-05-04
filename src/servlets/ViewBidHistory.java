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
 * Servlet implementation class ViewBidHistory
 */
@WebServlet("/ViewBidHistory")
public class ViewBidHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewBidHistory() {
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

		PrintWriter writer  = response.getWriter();

		writer.println("<html>" 
				+	"<head>" 
				+		"<title>" 
				+			"Bouncehouse Emporium - Bid History" 
				+		"</title>" 
				+	"</head>" 
				+	"<body>" 
				+		"<center>" 
				+			"<h1>Bouncehouse Emporium</h1>" 
				+			"<h3> Bidding History </h3>"
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
			int auctionID = Integer.parseInt(request.getParameter("auctionID"));
			bids = getBids.executeQuery("SELECT * FROM Bid WHERE AuctionID = "+auctionID+" ORDER BY BidTime DESC;");

			writer.println("<tr>"
					+		"<td><center> BidID </center></td>"
					+ 		"<td><center> Amount </center></td>"
					+ 		"<td><center> BidTime </center></td>"
					);
			while (bids.next()) {
				writer.println("<tr>"
						+		"<td><center>" + bids.getString("BidID") + "</center></td>"
						+ 		"<td><center>" + bids.getString("Amount") + "</center></td>"
						+ 		"<td><center>" + bids.getString("BidTime") + "</center></td>"
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
			} catch (Exception e) {
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
		
	}

}
