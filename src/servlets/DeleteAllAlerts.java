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

/**
 * Servlet implementation class DeleteAllAlerts
 */
@WebServlet(name = "DeleteAllAlerts", urlPatterns = {"/DeleteAllAlerts"})
public class DeleteAllAlerts extends HttpServlet {
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
									"Bouncehouse Emporium - Delete Alerts" +
								"</title>" +
							"</head>" +
							"<body>" +
								"<center>" +
									"<h1>Bouncehouse Emporium</h1>" +
									"<h3>Delete All Alerts</h3>"+
								"<hr>"
		);	
		
		Connection connection = null;
		Statement deleteAlert = null;
		Statement getAlerts = null;
		ResultSet getNumAlerts = null;
		int affectedRows = 0;
		boolean error = false;
		
		try {
			connection = SQLConnector.getConnection();
			deleteAlert = connection.createStatement();
			getAlerts = connection.createStatement();
			getNumAlerts = getAlerts.executeQuery("SELECT COUNT(*) AS Count FROM WishList WHERE UserID = " + request.getParameter("userID") + ";");
			affectedRows = deleteAlert.executeUpdate("DELETE FROM WishList WHERE UserID = " + request.getParameter("userID") + ";"); 
			
			getNumAlerts.next();
			
			if (affectedRows != getNumAlerts.getInt("Count")) {
				throw new SQLException("An error occurred while deleting the alerts, but the deletion may still have been successful.");
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
						+ 	"<br>"
						+ 	"<br>"
						+	"<a href = \"GetContent\">Home</a>"
						+	"</center>"
						+	"</body"
						+	"</html>"
				);
			} else {
				response.sendRedirect("ViewAlerts?userID=" + request.getSession().getAttribute("userID"));
				return;
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
