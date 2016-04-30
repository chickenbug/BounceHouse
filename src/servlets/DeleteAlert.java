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

@WebServlet(name = "DeleteAlert", urlPatterns = {"/DeleteAlert"})
public class DeleteAlert extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
		}
		
		PrintWriter writer  = response.getWriter();
		
		writer.println("<html>" +
							"<head>" +
								"<title>" +
									"Bouncehouse Emporium - Delete Alert" +
								"</title>" +
							"</head>" +
							"<body>" +
								"<center>" +
									"<h1>Bouncehouse Emporium</h1>" +
									"<h3>Delete Alert</h3>"+
								"</center><br><br>" +
								"<hr>"
		);	
		
		Connection connection = null;
		Statement deleteAlert = null;
		Statement getAlerts = null;
		ResultSet getNumAlerts = null;
		int affectedRows = 0;
		boolean error = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proj2016", "root", "pw");
			deleteAlert = connection.createStatement();
			getAlerts = connection.createStatement();
			
			getNumAlerts = getAlerts.executeQuery("SELECT COUNT(*) AS Count FROM WishList WHERE ListID = " + request.getParameter("listID") + ";");
			
			getNumAlerts.next();
			
			affectedRows = deleteAlert.executeUpdate("DELETE FROM WishList WHERE ListID = " + request.getParameter("listID") + ";"); 
					
			if (affectedRows != getNumAlerts.getInt("Count")) {
				throw new SQLException("An error occurred while deleting the alert, but the deletion may still have been successful.");
			}
			
		} catch (SQLException s) {
			error = true;
			writer.println("Alert deletion failed: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			error = true;
			writer.println("Alert deletion failed: " + e.getMessage() + "<br>");
		} finally {
			try {
				if (getAlerts != null) {
					getAlerts.close();
				}
				if (getNumAlerts != null) {
					getNumAlerts.close();
				}
				if (deleteAlert != null) {
					deleteAlert.close();
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
				writer.println("Please click <a href = \"ViewAlerts?userID=" + request.getSession().getAttribute("userID") + "\">here</a> to try again."
						+ 		"</body>"
						+ 	"</html>"
				);
			} else {
				response.sendRedirect("ViewAlerts?userID=" + request.getSession().getAttribute("userID"));
			}
	
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
