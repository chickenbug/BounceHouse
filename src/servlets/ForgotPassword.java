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
import java.time.*;

@WebServlet(name = "ForgotPassword", urlPatterns = {"/ForgotPassword"})
public class ForgotPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer  = response.getWriter();
		
		writer.println("<html>" +
							"<head>" +
								"<title>" +
									"Bouncehouse Emporium - Password Recovery" +
								"</title>" +
							"</head>" +
							"<body>" +
								"<center>" +
									"<h1>Bouncehouse Emporium</h1>" +
									"<h3>Password Recovery </h3>"+
								"</center><br><br>"
		);	
		
		Connection connection = null;
		Statement getPW = null;
		Statement sendEmail = null;
		ResultSet resultSet = null;
		int affectedRows = 0;
		boolean error = false;
		String pw = "";
		String email = "";
		int userID = 0;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//username and password below are placeholders - replace them 
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proj2016", "root", "pw");
			getPW = connection.createStatement();
			
			resultSet = getPW.executeQuery("SELECT Email, Password, UserID FROM User WHERE Username = \"" + request.getParameter("username") + "\";");
			
			if (resultSet.next()) {
				pw = resultSet.getString("Password");
				userID = resultSet.getInt("UserID");
				email = resultSet.getString("Email");
				
				sendEmail = connection.createStatement();
				affectedRows = sendEmail.executeUpdate("INSERT INTO Email(Content, Recipient, RecipientID, Sender, SenderID, SendTime) VALUES("
						+ "\"Congratulations! We've found your password for you. It was " + pw + ". If this process was not initiated by you, please login and change your password now for security purposes.\","
						+ "\"" + email + "\","
						+ userID + ","
						+ "\"system\","
						+ "1,"
						+ "\"" + LocalDate.now() + " " + LocalTime.now() + "\");"
				);
					
			
				if (affectedRows != 1) {
					throw new SQLException("Failed to send password recovery email to user.");
				}
			} else {
				throw new SQLException("No user was found with the specified username.");
			}
		} catch (SQLException s) {
			error = true;
			writer.println("Password retrieval failed: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			error = true;
			writer.println("Password retrieval failed: " + e.getMessage() + "<br>");
			//writer.println(e.getCause());
		} finally {
			try {
				if (getPW != null) {
					getPW.close();
				}
				
				if (sendEmail != null) {
					sendEmail.close();
				}
				if (resultSet != null) {
					resultSet.close();
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
				writer.println("Please click <a href = \"ForgotPassword?username=" + request.getParameter("username") + "\">here</a> to try again."
						+ 		"</body>"
						+ 	"</html>"
				);
			} else {
				response.sendRedirect("index.jsp");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
