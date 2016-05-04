package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import model.SQLConnector;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CreateAlert", urlPatterns = {"/CreateAlert"})
public class CreateAlert extends HttpServlet {
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
									"Bouncehouse Emporium - Create Alert" +
								"</title>" +
							"</head>" +
							"<body>" +
								"<center>" +
									"<h1>Bouncehouse Emporium</h1>" +
									"<h3>Create Alert</h3>"+
								"<hr>" 
		);	
		
		Connection connection = null;
		Statement selectItem = null;
		Statement insertAlert = null;
		ResultSet item = null;
		ResultSet hasAlertForThisItem = null;
		int affectedRows = 0;
		boolean error = false;
		
		try {
			connection = SQLConnector.getConnection();
			selectItem = connection.createStatement();
			insertAlert = connection.createStatement();
			
			//Error checking code - check if an entry already exists in the wishlist table for this user and item.
			hasAlertForThisItem = selectItem.executeQuery("SELECT COUNT(*) AS Count FROM Wishlist WHERE UserID = "
								+	Integer.parseInt(request.getSession().getAttribute("userID").toString()) 
								+	" AND Bounciness = \"" + request.getParameter("bounciness") + "\""
								+	" AND Category = \"" + request.getParameter("category") + "\""
								+	" AND Size = \"" + request.getParameter("size") + "\""
								+	" AND SubCategory = \"" + request.getParameter("subcategory") + "\""
								+	";"
			);
			
			while (hasAlertForThisItem.next()) {
				//Error checking code: if the user already made an alert for this item, then don't insert again.
				if (hasAlertForThisItem.getInt("Count") > 0) {
					throw new SQLException("An alert already exists for this user and item.");
				}
			}
			
			affectedRows = insertAlert.executeUpdate("INSERT INTO Wishlist(Bounciness, Category, Size, Subcategory, UserID)" 
							+   " VALUES(" 
							+	request.getParameter("bounciness") + ","
							+	"\"" +	request.getParameter("category")									+ "\","
							+	"\"" +	request.getParameter("size") 										+ "\","
							+	"\"" +	request.getParameter("subcategory")									+ "\","
							+ 	Integer.parseInt(request.getSession().getAttribute("userID").toString())	+ ");"
			);
				
			if (affectedRows != 1) {
				throw new SQLException("Alert was not inserted or insertion was accidentally repeated.<br>");
			}
		} catch (SQLException s) {
			error = true;
			writer.println("Alert creation failed: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			error = true;
			writer.println("Alert Creation failed: " + e.getMessage() + "<br>");
		} finally {
			try {
				if (item != null) {
					item.close();
				}
				if (hasAlertForThisItem != null) {
					hasAlertForThisItem.close();
				}
				if (selectItem != null) {
					selectItem.close();
				}
				if (insertAlert != null) {
					insertAlert.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException s) {
				error = true;
				writer.println("An SQL exception occured while attempting to close SQL objects: " + s.getMessage() + "<br>");
			} catch (Exception e) {
				error = true;
				writer.println("A general exception occured while attempting to close SQL objects: " + e.getMessage() + "<br>");
			}
			
			if (error) {
				writer.println("Please click <a href = \"createAlert.jsp\">here</a> to try again."
						+ 	"<br>"
						+ 	"<br>"
						+	"<a href = \"GetContent\">Home</a>"
						+	"</center>"
						+	"</body"
						+	"</html>"
				);
			} else {
				writer.println("Alert creation successful.<br> You can click <a href = \"GetContent\">here</a> to go back to the main page."
						+ 	"<br>"
						+ 	"<br>"
						+	"<a href = \"GetContent\">Home</a>"
						+	"</center>"
						+	"</body"
						+	"</html>"
				);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
