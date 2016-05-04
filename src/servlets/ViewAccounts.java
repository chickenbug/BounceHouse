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
 * Servlet implementation class ViewAccounts
 */
@WebServlet("/ViewAccounts")
public class ViewAccounts extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewAccounts() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null||!request.getSession().getAttribute("role").equals("rep")) {
			response.sendError(403, "You are not authorized to access this page.");
			return;
		}

		//PrintWriter to write to HTML.
		PrintWriter writer  = response.getWriter();

		/*
		 * Set up page title and links. The structure of the println statement is only to mimic 
		 * the structure of a formatted HTML document. This structure is only maintained below 
		 * when it is conducive to readability; when this structure inhibits code readability,
		 * I do not use it.
		 */
		writer.println("<html>" 
				+	"<head>" 
				+		"<title>" 
				+			"Bouncehouse Emporium - Accounts" 
				+		"</title>" 
				+	"</head>" 
				+	"<body>" 
				+		"<center>" 
				+			"<h1>Bouncehouse Emporium</h1>" 
				+			"<h3>View/Modify Accounts</h3>"
				+			"<center><a href = \"GetContent\"><br><br>Home</a></center>"
				+			"<hr>"
				+			"<table border = 1 width = 100%>"
				);

		//Create objects for connections, statements, and resultsets.
		Connection connection = null;
		Statement getUsers = null;
		ResultSet users = null;

		//Open connection, create statement, and process request.
		try {
			connection = SQLConnector.getConnection();
			getUsers = connection.createStatement();

			users = getUsers.executeQuery("SELECT * FROM User WHERE role = \"EndUser\";");

			/*
			 * Process request and output HTML.
			 */
			writer.println("<tr>"
					+		"<td><center> UserID </center></td>"
					+		"<td><center> Username </center></td>"
					+		"<td><center> Firstname </center></td>"
					+ 		"<td><center> Lastname </center></td>"
					+ 		"<td><center> Options </center></td>"
					);
			while (users.next()) {
				int userID = Integer.parseInt(users.getString("userID"));
				writer.println("<tr>"
						+		"<td><center>" + users.getString("UserID") + "</center></td>"
						+		"<td><center>" + users.getString("Username") + "</center></td>"
						+		"<td><center>"+ users.getString("Firstname") + "</center></td>"
						+ 		"<td><center>" + users.getString("Lastname") + "</center></td>"
						+		"<td><center><form action = \"ViewAccount\" method = \"post\">"
						+ 		"<input type = \"submit\" value = \"Edit Account\">"
						+ 		"<input type = \"hidden\" name = \"userID\" value = "+userID+"></form>"
						);
				writer.println("</center></td>");
			}

			writer.println("</table>"
					+ "<br>"
					+ "<br>"
					+ "<br>");

		} catch (SQLException s) {
			writer.println("Failed to get account list: " + s.getMessage() + "<br>");
			return;
		} catch (Exception e) {
			writer.println("Failed to get account list: " + e.getMessage() + "<br>");
			return;
		} finally {
			//Close resultset, statement, connection.
			try {
				if (users != null) {
					users.close();
				}
				if (getUsers != null) {
					getUsers.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sql) {
				/*
				 * Do nothing. The page has already loaded - no need to let the user know
				 * there was an error that doesn't affect them.
				 */
				return;
			} catch (Exception e) {
				/*
				 * Do nothing. The page has already loaded - no need to let the user know
				 * there was an error that doesn't affect them.
				 */
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
