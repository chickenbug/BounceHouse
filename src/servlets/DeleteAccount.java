package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeleteAccount
 */
@WebServlet(name = "DeleteAccount", urlPatterns = {"/DeleteAccount"})
public class DeleteAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
		}
		
		PrintWriter writer  = response.getWriter();
		
		writer.println("<html>" +
							"<head>" +
								"<title>" +
									"Bouncehouse Emporium - Delete Account" +
								"</title>" +
							"</head>" +
							"<body>" +
								"<center>" +
									"<h1>Bouncehouse Emporium</h1>" +
									"<h3>Account Deletion</h3>"+
								"</center><br><br>" +
								"<hr>"
		);	
		
		Connection connection = null;
		Statement statement = null;
		int affectedRows = 0;
		boolean error = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proj2016", "root", "pw");
			statement = connection.createStatement();
			affectedRows = statement.executeUpdate("DELETE FROM User WHERE UserID = " + Integer.parseInt(request.getSession().getAttribute("userID").toString()) + ";");
			
			if (affectedRows != 1) {
				throw new SQLException("An unexpected error occurred deleting the user account, but the account may still have been successfully deleted.");
			}
		} catch (SQLException s) {
			error = true;
			writer.println("Account deletion failed: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			error = true;
			writer.println("Account deletion failed: " + e.getMessage() + "<br>");
		} finally {
			try {
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
				writer.println("A general exception occured while attempting to close SQL objects: " + e.getMessage() + "<br>");
			}
			
			if (error) {
				writer.println("Please click <a href = \"Modify Account?userID=" + request.getSession().getAttribute("userID") + "\">here</a> to try again."
						+ 		"</body>"
						+ 	"</html>"
				);
			} else {
				response.sendRedirect("Logout");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
