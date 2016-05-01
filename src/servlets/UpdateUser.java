package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SQLConnector;

/**
 * Servlet implementation class UpdateUser
 */
@WebServlet(name = "UpdateUser", urlPatterns = {"/UpdateUser"})
public class UpdateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
		}
		
		PrintWriter writer  = response.getWriter();
		
		writer.println("<html>" +
							"<head>" +
								"<title>" +
									"Bouncehouse Emporium - Update User" +
								"</title>" +
							"</head>" +
							"<body>" +
								"<center>" +
									"<h1>Bouncehouse Emporium</h1>" +
									"<h3>Account Modification</h3>"+
								"</center><br><br>" +
								"<hr>"
		);	
		
		Connection connection = null;
		Statement statement = null;
		int affectedRows = 0;
		boolean error = false;
		boolean hasOtherParam = false;
		String update = "UPDATE USER ";
		
		if (request.getParameter("address") != null && !request.getParameter("address").equals("")) {
			hasOtherParam = true;
			update += ("SET Address = \"" + request.getParameter("address") + "\"");
		} 
		if (request.getParameter("city") != null && !request.getParameter("city").equals("")) {
			if (hasOtherParam) {
				update += ", City = \"" + request.getParameter("city") + "\"";
			} else {
				hasOtherParam = true;
				update += "SET City = \"" + request.getParameter("city") + "\"";
			}
		} 
		if (request.getParameter("country") != null && !request.getParameter("country").equals("")) {
			if (hasOtherParam) {
				update += ", Country = \"" + request.getParameter("country") + "\"";
			} else {
				hasOtherParam = true;
				update += "SET Country = \"" + request.getParameter("country") + "\"";
			}
		} 
		if (request.getParameter("email") != null && !request.getParameter("email").equals("")) {
			if (hasOtherParam) {
				update += ", Email = \"" + request.getParameter("email") + "\"";
			} else {
				hasOtherParam = true;
				update += "SET Email = \"" + request.getParameter("email") + "\"";
			}
		} 
		if (request.getParameter("password") != null && !request.getParameter("password").equals("")) {
			if (hasOtherParam) {
				update += ", Password = \"" + request.getParameter("password") + "\"";
			} else {
				hasOtherParam = true;
				update += "SET Password = \"" + request.getParameter("password") + "\"";
			}
		} if (request.getParameter("phone") != null && !request.getParameter("phone").equals("")) {
			if (hasOtherParam) {
				update += ", Phone = \"" + request.getParameter("phone") + "\"";
			} else {
				hasOtherParam = true;
				update += "SET Phone = \"" + request.getParameter("phone") + "\"";
			}
		} if (request.getParameter("postcode") != null && !request.getParameter("postcode").equals("")) {
			if (hasOtherParam) {
				update += ", PostCode = \"" + request.getParameter("postcode") + "\"";
			} else {
				hasOtherParam = true;
				update += "SET PostCode = \"" + request.getParameter("postcode") + "\"";
			}
		} if (request.getParameter("state") != null && !request.getParameter("state").equals("")) {
			if (hasOtherParam) {
				update += ", State = \"" + request.getParameter("state") + "\"";
			} else {
				hasOtherParam = true;
				update += "SET State = \"" + request.getParameter("state") + "\"";
			}
		}
		
		try {
			connection = SQLConnector.getConnection();
			statement = connection.createStatement();
			
			affectedRows = statement.executeUpdate(update + " WHERE UserID = " + request.getSession().getAttribute("userID") + ";");

			//writer.println(update + " WHERE UserID = " + request.getSession().getAttribute("userID") + ";");
			
			if (affectedRows != 1) {
				throw new SQLException("Failed to insert new user, or insertion was accidentally repeated.");
			}

		} catch (SQLException s) {
			error = true;
			writer.println("Account update failed: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			error = true;
			writer.println("Account update failed: " + e.getMessage() + "<br>");
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
				writer.println("A general exception occurred while attempting to close SQL objects: " + e.getMessage() + "<br>");
			}
			
			if (error) {
				writer.println("Please click <a href = \"ViewAccount?userID=" + request.getSession().getAttribute("userID") + "\">here</a> to go back to your account details try again."
						+ 		"</body>"
						+ 	"</html>"
				);
			} else {
				response.sendRedirect("ViewAccount?userID=" + request.getSession().getAttribute("userID"));
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
