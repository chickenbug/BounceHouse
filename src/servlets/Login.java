package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		PrintWriter writer  = response.getWriter();
		
		writer.println("<html>" +
							"<head>" +
								"<title>" +
									"Bouncehouse Emporium - Login" +
								"</title>" +
							"</head>" +
							"<body>" +
								"<center>" +
									"<h1>Bouncehouse Emporium</h1>" +
									"<h3>Login</h3>"+
								"</center><br><br>" +
								"<hr>"
		);	
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultset = null; 
		boolean error = false;

		//If for whatever reason the username or password field was not filled, print out an error and quit the servlet.
		if(request.getParameter("username") == null) {
			writer.println("Login failed: no username provided.<br>"
					+	"Please click <a href = \"index.jsp\">here</a> to try again."
					+	"</body>"
					+ 	"</html>");
			return;
		} else if (request.getParameter("password") == null) {
			writer.println("Login failed: no password provided.<br>"
					+	"Please click <a href = \"index.jsp\">here</a> to try again."
					+	"</body>"
					+ 	"</html>");
			return;
		}
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proj2016", "root", "pw");
			statement = connection.createStatement();

			//If username and password are properly set (that is, not null) go ahead and query the DB.
			resultset = statement.executeQuery("SELECT COUNT(*) AS Count, UserID, Username, Role FROM User WHERE Username = \"" + request.getParameter("username") + "\" AND Password = \"" + request.getParameter("password") + "\" GROUP BY"
					+ " UserID, Username, Role;");
			
			//writer.println("Connection created. Statement created. Query Executed.<br>");
			
			//Assuming no screwups, there should be one row with field Count == 1. 
			if (resultset.next()) {
				
				if (resultset.getInt("Count") != 1) {
					throw new SQLException("Username and password did not match the provided credentials.");
				}
				
				//Bind UserID, username, and role to the session.
				request.getSession().setAttribute("userID", resultset.getInt("UserID"));
				request.getSession().setAttribute("username", resultset.getString("username"));
				request.getSession().setAttribute("role", resultset.getString("role"));
				
				//DEBUG	CODE
				//writer.println("UserID, Username, and Role bound to user session.");
			} else {
				throw new SQLException("Username and password do not match, or no user exists with specified username and password.");
			}
		} catch (SQLException s) {
			error = true;
			writer.println("Login failed: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			error = true;
			writer.println("Login failed: " + e.getMessage() + "<br>");
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
				writer.println(e.getCause());
			}
			
			if (error) {
				writer.println("Please click <a href = \"index.jsp\">here</a> to try again."
						+ 		"</body>"
						+ 	"</html>"
				);
			} else {
				response.sendRedirect("GetContent");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
