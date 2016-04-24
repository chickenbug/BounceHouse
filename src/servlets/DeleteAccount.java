package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;

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
		PrintWriter writer  = response.getWriter();
		
		writer.println("<html>" +
							"<head>" +
								"<title>" +
									"Bouncehouse Emporium - Registration" +
								"</title>" +
							"</head>" +
							"<body>" +
								"<center>" +
									"<h1>Bouncehouse Emporium</h1>" +
									"<h3>Registration</h3>"+
								"</center><br><br>"
		);	
		
		Connection connection = null;
		Statement statement = null;
		int affectedRows = 0;
		boolean error = false;
		
		if (!request.getParameter("password").equals(request.getParameter("confirmpassword"))) {
			//passwords do not match - reject
			response.sendRedirect("error.jsp");
		} else if ((Year.now().getValue() - Integer.parseInt(request.getParameter("yeardropdown")) < 18)) {
			//user is less than 18 years old - reject
			response.sendRedirect("error.jsp"); //consider making this a different script.
		} else if ((Integer.parseInt(request.getParameter("monthdropdown")) == 2) && (Integer.parseInt(request.getParameter("daydropdown")) > 28)) {
			if (Year.now().isLeap() && Integer.parseInt(request.getParameter("daydropdown")) > 29) {
				//Specified 30th or 31st of February in a leap year - reject
				response.sendRedirect("error.jsp"); //same 
			} else if (!Year.now().isLeap()) {
				//specified 29th-31st of February in a non-leap year - reject 
				response.sendRedirect("error.jsp"); //same
			}
		} else if (((Integer.parseInt(request.getParameter("monthdropdown")) == 4) || (Integer.parseInt(request.getParameter("monthdropdown")) == 6) || (Integer.parseInt(request.getParameter("monthdropdown")) == 9) || (Integer.parseInt(request.getParameter("monthdropdown")) == 11)) && (Integer.parseInt(request.getParameter("daydropdown")) > 30)) {
			//specified April, June, September, or November 31
			response.sendRedirect("error.jsp");
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//username and password below are placeholders - replace them 
			connection = DriverManager.getConnection("jdbc:mysql://localhost/proj_2016", "root", "pw");
			statement = connection.createStatement();
			affectedRows = statement.executeUpdate("INSERT INTO User(Address, BirthDate, City, Country, Email, FirstName, LastName, Password, Phone, PostCode, State, Username)" 
											+   " VALUES(" 
											+	"\"" + request.getParameter("address") + "\"\","
											+ 	"\"" + request.getParameter("yeardropdown") + "-" + request.getParameter("monthdropdown") + "-" + request.getParameter("daydropdown") + "\","
											+	"\"" + request.getParameter("city") + "\","
											+	"\"" + request.getParameter("country") + "\","
											+ 	"\"" + request.getParameter("email") + "\","
											+	"\"" + request.getParameter("firstname") + "\","
											+ 	"\"" + request.getParameter("lastname") + "\","
											+ 	"\"" + request.getParameter("password") + "\","
											+ 	"\"" + request.getParameter("phone") + "\","
											+ 	"\"" + request.getParameter("postcode") + "\","
											+ 	"\"" + request.getParameter("state") + "\","
											+ 	"\"" + request.getParameter("username") + "\");");
			if (affectedRows != 1) {
				writer.println("Registration failed due to an unspecified error.<br>"
						+ 		"Please click <a href = \"createAccount.jsp\">here</a> to try again if the page does not automatically redirect you after 10 seconds."
				+		"</body>"
			+		"</html>");
				Thread.sleep(10000);
			}
		} catch (SQLException s) {
			response.sendRedirect("error.jsp");
		} catch (Exception e) {
			response.sendRedirect("error.jsp");
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException s) {
				response.sendRedirect("error.jsp");
			} catch (Exception e) {
				response.sendRedirect("error.jsp");
			}
			
			if (error) {
				response.sendRedirect("createAccount.jsp");
			} else {
				response.sendRedirect("index.jsp");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
