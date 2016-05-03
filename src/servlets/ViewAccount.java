package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ViewAccount", urlPatterns = {"/ViewAccount"})
public class ViewAccount extends HttpServlet {
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
									"Bouncehouse Emporium - View Account" +
								"</title>" +
							"</head>" +
							"<body>" +
								"<center>" +
									"<h1>Bouncehouse Emporium</h1>" +
									"<h3>View Account</h3>"+
								"</center><br><br>" +
								"<hr>" +
								"<form action = \"UpdateUser\" method = \"post\">"
		);	
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultset = null; 
		boolean error = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proj2016", "root", "pw");
			statement = connection.createStatement();

			//If username and password are properly set (that is, not null) go ahead and query the DB.
			resultset = statement.executeQuery("SELECT * FROM User WHERE UserID = \"" + request.getParameter("userID") + "\";");
			
			//writer.println("Connection created. Statement created. Query Executed.<br>");
			
			//Assuming no screwups, there should be one row with field Count == 1. 
			if (resultset.next()) {
				writer.println("Address: " + checkNullFields(resultset.getString("Address")) + "<br>"
							+	"Updated address: <input type = \"text\" name = \"address\"><br><br>"
				);
				writer.println("Birthday: " + checkNullFields(resultset.getString("BirthDate")) + "<br>"
						+	"If you need to change this field, please contact support.<br><br>"
				);
				writer.println("City: " + checkNullFields(resultset.getString("City")) + "<br>"
						+	"Updated city: <input type = \"text\" name = \"city\"><br><br>"
				);
				writer.println("Country: " + checkNullFields(resultset.getString("Country")) + "<br>"
						+	"Updated country: <input type = text name = \"country\"><br><br>"
				);
				writer.println("Email: " + checkNullFields(resultset.getString("Email")) + "<br>"
						+	"Updated email: <input type = text name = \"email\"><br><br>"
				);
				writer.println("First Name: " + checkNullFields(resultset.getString("FirstName")) + "<br>"
						+	"If you need to change this field, please contact support.<br><br>"
				);
				writer.println("Last Name: " + checkNullFields(resultset.getString("LastName")) + "<br>"
						+	"If you need to change this field, please contact support.<br><br>"
				);
				writer.println("Password: " + checkNullFields(resultset.getString("Password")) + "<br>"
						+	"Updated password: <input type = \"text\" name = \"password\"><br>"
						+ 	"Note that password changes will not take effect until your next login.<br><br>"
				);
				writer.println("Phone: " + checkNullFields(resultset.getString("Phone")) + "<br>"
						+	"Updated phone number: <input type = \"text\" name = \"phone\"><br><br>"
				);
				writer.println("Postal code: " + checkNullFields(resultset.getString("PostCode")) + "<br>"
						+	"Updated postal code: <input type = \"text\" name = \"postcode\"><br><br>"
				);
				writer.println("State/Province: " + checkNullFields(resultset.getString("State")) + "<br>"
						+	"Updated state/province: <input type = \"text\" name = \"state\"><br><br>"
				);
				writer.println("Username: " + checkNullFields(resultset.getString("Username")) + "<br>"
						+	"This field cannot be changed.<br><br>"
				);
				writer.println("<input type = \"submit\" value = \"Update Fields\">"
							+ "</form>"
				);
			} else {
				throw new SQLException("No user data found for user #" + request.getParameter("userID") + "!");
			}
		} catch (SQLException s) {
			error = true;
			writer.println("Failed to get account details: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			error = true;
			writer.println("Failed to get account details: " + e.getMessage() + "<br>");
			//writer.println(e.getCause());
		} finally {
			try {
				if (resultset != null) {
					resultset.close();
				}
				
				if (statement != null) {
					statement.close();
				}
				
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException s) {
				error = true;
				writer.println("An SQL exception occured while attempting to close SQL objects: " + s.getMessage() + "<br>");
			} catch (Exception e) {
				error = true;
				writer.println("A general exception occurred while attempting to close SQL objects: " + e.getMessage() + "<br>");
			}
			
			if (error) {
				writer.println("Please click <a href = \"GetContent.jsp\">here</a> to return to the main page and try again."
						+ 		"</body>"
						+ 	"</html>"
				);
			} else {
				writer.println("<br>"
						+ "<br>"
						+ "<center>"
						+ "<a href = \"GetContent\">Return To Main Page</a>"
						+ "</center"
						+ "</body>"
						+ "</html>"
				);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private String checkNullFields(String field) {
		if (field == null) {
			return "There is no data for this field.";
		}
		
		return field;
	}
}
