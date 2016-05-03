package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ForgotUsername
 */
@WebServlet(name = "ForgotUsername", urlPatterns = {"/ForgotUsername"})
public class ForgotUsername extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer  = response.getWriter();
		
		writer.println("<html>" +
							"<head>" +
								"<title>" +
									"Bouncehouse Emporium - Username Recovery" +
								"</title>" +
							"</head>" +
							"<body>" +
								"<center>" +
									"<h1>Bouncehouse Emporium</h1>" +
									"<h3>Username Recovery </h3>"+
								"</center><br><br>"
		);	
		
		Connection connection = null;
		Statement getUsername = null;
		Statement sendEmail = null;
		ResultSet resultSet = null;
		int affectedRows = 0;
		boolean error = false;
		String username = "";
		int userID = 0;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//username and password below are placeholders - replace them 
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proj2016", "root", "pw");
			getUsername = connection.createStatement();
			
			resultSet = getUsername.executeQuery("SELECT Username, UserID FROM User WHERE Email = \"" + request.getParameter("email") + "\";");
			
			if(resultSet.next()) {
				username = resultSet.getString("Username");
				userID = resultSet.getInt("UserID");
			
			
				sendEmail = connection.createStatement();
				affectedRows = sendEmail.executeUpdate("INSERT INTO Email(Content, Recipient, RecipientID, Sender, SenderID, SendTime) VALUES("
						+ "\"Congratulations! We've found your username for you. It was " + username + ".\","
						+ "\"" + request.getParameter("email") + "\","
						+ userID + ","
						+ "\"System\","
						+ "1,"
						+ "\"" + LocalDate.now() + " " + LocalTime.now() + "\");"
				);
				
			
				if (affectedRows != 1) {
					throw new SQLException("Failed to send username recovery email to user.");
				}
			} else {
				throw new SQLException("No user was found with the specified email.");
			}
		} catch (SQLException s) {
			error = true;
			writer.println("Username retrieval failed: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			error = true;
			writer.println("Username retrieval failed: " + e.getMessage() + "<br>");
			//writer.println(e.getCause());
		}finally {
			try {
				if (getUsername != null) {
					getUsername.close();
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
				writer.println("Please click <a href = \"ForgotUsername\">here</a> to try again."
						+ 		"</body>"
						+ 	"</html>"
				);
			} else {
				response.sendRedirect("index.jsp");
				return;
			}
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
