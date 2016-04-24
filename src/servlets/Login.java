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
								"</center><br><br>"
		);	
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultset = null; 
		boolean error = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//username and password below are placeholders - replace them 
			connection = DriverManager.getConnection("jdbc:mysql://localhost/proj_2016", "root", "pw");
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT COUNT(*) AS \"Count\" FROM User WHERE (Username = \"" + request.getParameter("username") + "\" AND Password = \"" + request.getParameter("password") + "\");");
			
			while (resultset.next()) {
				if (resultset.getInt("Count") != 1) {
					error = true;
					writer.println("Username and password did not match the provided credentials.<br>"
							+ 		"Please click <a href = \"index.jsp\">here</a> to try again if the page does not automatically redirect you after 10 seconds."
					+		"</body>"
				+		"</html>");
					Thread.sleep(10000);
				}
			}
		} catch (SQLException s) {
			response.sendRedirect("error.jsp");
		} catch (Exception e) {
			response.sendRedirect("error.jsp");
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
				response.sendRedirect("error.jsp");
			} catch (Exception e) {
				response.sendRedirect("error.jsp");
			}
			
			if (error) {
				response.sendRedirect("index.jsp");
			} else {
				response.sendRedirect("GetContent");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
