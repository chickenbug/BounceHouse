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
import java.time.Year;

@WebServlet(name = "Registration", urlPatterns = {"/Registration"})
public class Registration extends HttpServlet {
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
								"</center><br><br>" +
								"<hr>"
		);	
		
		Connection connection = null;
		Statement statement = null;
		ResultSet usernameExists = null;
		int affectedRows = 0;
		boolean error = false;
		
		writer.println(parseMonth(request.getParameter("monthdropdown")));
		
		/*
		 * Do all error checking with regards to input fields here. Invalid or null inputs are reasons to terminate.
		 */
		if (request.getParameter("address") == null) {
			writer.println("Account creation failed: all fields are required, but some were left blank.<br>"
					+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
					+	"</body>"
					+	"</html>"
			);
			return;	
		} else if (request.getParameter("city") == null) {
			writer.println("Account creation failed: all fields are required, but some were left blank.<br>"
					+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
					+	"</body>"
					+	"</html>"
			);
			return;	
		} else if (request.getParameter("country") == null) {
			writer.println("Account creation failed: all fields are required, but some were left blank.<br>"
					+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
					+	"</body>"
					+	"</html>"
			);
			return;	
		} else if (request.getParameter("email") == null) {
			writer.println("Account creation failed: all fields are required, but some were left blank.<br>"
					+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
					+	"</body>"
					+	"</html>"
			);
			return;	
		} else if (request.getParameter("firstname") == null) {
			writer.println("Account creation failed: all fields are required, but some were left blank.<br>"
					+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
					+	"</body>"
					+	"</html>"
			);
			return;	
		} else if (request.getParameter("lastname") == null) {
			writer.println("Account creation failed: all fields are required, but some were left blank.<br>"
					+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
					+	"</body>"
					+	"</html>"
			);
			return;	
		} else if (request.getParameter("password") == null) {
			writer.println("Account creation failed: all fields are required, but some were left blank.<br>"
					+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
					+	"</body>"
					+	"</html>"
			);
			return;	
		} else if (request.getParameter("phone") == null) {
			writer.println("Account creation failed: all fields are required, but some were left blank.<br>"
					+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
					+	"</body>"
					+	"</html>"
			);
			return;	
		} else if (request.getParameter("postcode") == null) {
			writer.println("Account creation failed: all fields are required, but some were left blank.<br>"
					+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
					+	"</body>"
					+	"</html>"
			);
			return;	
		} else if (request.getParameter("state") == null) {
			writer.println("Account creation failed: all fields are required, but some were left blank.<br>"
					+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
					+	"</body>"
					+	"</html>"
			);
			return;	
		} else if (request.getParameter("username") == null) {
			writer.println("Account creation failed: all fields are required, but some were left blank.<br>"
					+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
					+	"</body>"
					+	"</html>"
			);
			return;	
		} else if (!request.getParameter("password").equals(request.getParameter("confirmpassword"))) {
			//passwords do not match - reject
			writer.println("Account creation failed: the passwords do not match.<br>"
						+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
						+	"</body>"
						+	"</html>"
			);
			return;
		} else if ((Year.now().getValue() - Integer.parseInt(request.getParameter("yeardropdown")) < 18)) {
		//	else if ((Year.now().getValue() - request.getParameter("yeardropdown")) < 18) {
			//user is less than 18 years old - reject
			writer.println("Account creation failed: you must be at least 18 years of age to create an account.<br>"
					+	"If this was an error, please click <a href = \"createAccount.jsp\">here</a> to try again."
					+	"</body>"
					+	"</html>"
			);
			return;
		} else if (request.getParameter("monthdropdown").equals("February") && (Integer.parseInt(request.getParameter("daydropdown")) > 28)) {
			if (Year.now().isLeap() && Integer.parseInt(request.getParameter("daydropdown")) > 29) {
				//Specified 30th or 31st of February in a leap year - reject
				writer.println("Account creation failed: you have specified an invalid birthday.<br>"
						+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
						+	"</body>"
						+	"</html>"
				);
				return;
			} else if (!Year.now().isLeap()) {
				//specified 29th-31st of February in a non-leap year - reject 
				writer.println("Account creation failed: you have specified an invalid birthday.<br>"
						+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
						+	"</body>"
						+	"</html>"
				);
				return;
			}
		} else if ((request.getParameter("monthdropdown").equals("April") || request.getParameter("monthdropdown").equals("June") || request.getParameter("monthdropdown").equals("September") || request.getParameter("monthdropdown").equals("November")) && (Integer.parseInt(request.getParameter("daydropdown")) > 30)) {
			//specified April, June, September, or November 31
			writer.println("Account creation failed: you have specified an invalid birthday.<br>"
					+	"Please click <a href = \"createAccount.jsp\">here</a> to try again."
					+	"</body>"
					+	"</html>"
			);
			return;
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//username and password below are placeholders - replace them 
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proj2016", "root", "pw");
			statement = connection.createStatement();
			usernameExists = statement.executeQuery("SELECT COUNT(*) AS Count FROM User WHERE Username = \"" + request.getParameter("username") + "\";");
			
			while(usernameExists.next()) {
				if (usernameExists.getInt("Count") > 0) {
					//I know it says resource leak here - but all objects are closed within the finally clause. 'Tis all good.
					throw new SQLException("An account already exists with this username.");
				}
			}
			
			if (!error) {
				affectedRows = statement.executeUpdate("INSERT INTO User(Address, BirthDate, City, Country, Email, FirstName, LastName, Password, Phone, PostCode, Role, State, Username)" 
												+   " VALUES(" 
												+	"\"" + request.getParameter("address") + "\","
												+ 	"\"" + request.getParameter("yeardropdown") + "-" + parseMonth(request.getParameter("monthdropdown")) + "-" + request.getParameter("daydropdown") + "\","
												+	"\"" + request.getParameter("city") + "\","
												+	"\"" + request.getParameter("country") + "\","
												+ 	"\"" + request.getParameter("email") + "\","
												+	"\"" + request.getParameter("firstname") + "\","
												+ 	"\"" + request.getParameter("lastname") + "\","
												+ 	"\"" + request.getParameter("password") + "\","
												+ 	"\"" + request.getParameter("phone") + "\","
												+ 	"\"" + request.getParameter("postcode") + "\","
												+	"\"EndUser\"," 
												+ 	"\"" + request.getParameter("state") + "\","
												+ 	"\"" + request.getParameter("username") + "\");");
				if (affectedRows != 1) {
					throw new SQLException("Failed to insert new user, or insertion was accidentally repeated.");
				}
			}
		} catch (SQLException s) {
			error = true;
			writer.println("Account creation failed: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			error = true;
			writer.println("Account creation failed: " + e.getMessage() + "<br>");
		} finally {
			try {
				if (statement != null) {
					statement.close();
				} 
				if (connection != null) {
					connection.close();
				}
				if (usernameExists != null) {
					usernameExists.close();
				}
			} catch (SQLException s) {
				error = true;
				writer.println("An SQL exception occured while attempting to close SQL objects: " + s.getMessage() + "<br>");
			} catch (Exception e) {
				error = true;
				writer.println("A general exception occurred while attempting to close SQL objects: " + e.getMessage() + "<br>");
			}
			
			if (error) {
				writer.println("Please click <a href = \"index.jsp\">here</a> to try again."
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
	
	private String parseMonth(String month) throws IOException {
		
		if (month == null) {
			throw new IOException("There was an error parsing your birthdate.");
		} else if (month.equals("January")) {
			return "01";
		} else if (month.equals("February")) {
			return "02";
		} else if (month.equals("March")) {
			return	"03";
		} else if (month.equals("April")) {
			return	"04";
		} else if (month.equals("May")) {
			return "05";
		} else if (month.equals("June")) {
			return "06";
		} else if (month.equals("July")) {
			return"07";
		} else if (month.equals("August")) {
			return "08";
		} else if (month.equals("September")) {
			return "09";
		} else if (month.equals("October")) {
			return "10";
		} else if (month.equals("November")) {
			return "11";
		} else if (month.equals("December")) {
			return "12";
		}
		
		return "00";
	}
}
