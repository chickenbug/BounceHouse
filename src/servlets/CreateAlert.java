package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
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
								"</center><br><br>" +
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
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proj2016", "root", "pw");
			selectItem = connection.createStatement();
			insertAlert = connection.createStatement();
			
			//Error checking code - check if an entry already exists in the wishlist table for this user and item.
			hasAlertForThisItem = selectItem.executeQuery("SELECT COUNT(*) AS Count FROM WishList WHERE UserID = "
								+	Integer.parseInt(request.getSession().getAttribute("userID").toString()) 
								+	" AND ItemID = " + request.getParameter("itemID")
								+	";"
			);
			
			while (hasAlertForThisItem.next()) {
				//Error checking code: if the user already made an alert for this item, then don't insert again.
				if (hasAlertForThisItem.getInt("Count") > 0) {
					throw new SQLException("An alert already exists for this user and item.");
				}
			}
			
			item = selectItem.executeQuery("SELECT Bounciness, Category, Color, Size, Subcategory FROM Item WHERE ItemID = " + request.getParameter("itemID") + ";");
			while (item.next()) {
				affectedRows = insertAlert.executeUpdate("INSERT INTO Wishlist(Bounciness, Category, Color, ItemID, Size, Subcategory, UserID)" 
							+   " VALUES(" 
							+	item.getInt("Bounciness") + ","
							+	"\"" +	item.getString("Category")											+ "\","
							+	"\"" +	item.getString("Color") 											+ "\","
							+	request.getParameter("itemID")												+ ","
							+	"\"" +	item.getString("Size") 												+ "\","
							+	"\"" +	item.getString("Subcategory")										+ "\","
							+ 	Integer.parseInt(request.getSession().getAttribute("userID").toString())	+ ");"
				);
				
				if (affectedRows != 1) {
					throw new SQLException("Alert was not inserted or insertion was accidentally repated.<br>");
				}
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
				writer.println("Please click <a href = \"GetContent\">here</a> to try again."
						+ 		"</body>"
						+ 	"</html>"
				);
			} else {
				writer.println("Alert creation successful.<br> You can click <a href = \"GetContent\">here</a> to go back to the main page."
						+ 		"</body>"
						+ 	"</html>"
				);
			}
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
