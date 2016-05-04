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
 * Servlet implementation class ViewQuestions
 */
@WebServlet("/ViewQuestions")
public class ViewQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewQuestions() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null || !request.getSession().getAttribute("role").equals("rep")) {
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
				+			"Bouncehouse Emporium - Questions" 
				+		"</title>" 
				+	"</head>" 
				+	"<body>" 
				+		"<center>" 
				+			"<h1>Bouncehouse Emporium</h1>" 
				+			"<h2> View Questions </h2>"
				);

		//Create objects for connections, statements, and resultsets.
		Connection connection = null;
		Statement getQuestions = null;
		ResultSet questions = null;

		//Open connection, create statement, and process request.
		try {
			connection = SQLConnector.getConnection();
			getQuestions = connection.createStatement();

			questions = getQuestions.executeQuery("SELECT Q.*, U.username, U1.firstname FROM question Q, user U, user U1 WHERE U.userid=Q.userid AND Q.repid=U1.userid;");

			/*
			 * Process request and output HTML.
			 */
			writer.println("<h3>" + "<a href = \"askQuestion.jsp\">Ask a Question</a>"+ "&emsp;" +"<a href = \"searchQuestions.jsp\">Search Questions</a>"+"</h3>"
					+ 		"<a href = \"GetContent\">Home</a><br>"
					+			"<hr>"
					+			"<table border = 1 width = 100%>"
					+		"<tr>"
					+		"<td><center> Topic </center></td>"
					+		"<td><center> Question </center></td>"
					+		"<td><center> Asking User </center></td>"
					+		"<td><center> Responding Rep </center></td>"
					+ 		"<td><center> View Answer </center></td>"
					);
			while (questions.next()) {
				writer.println("<tr>"
						+		"<td><center>" + questions.getString("Q.topic") + "</center></td>"
						+		"<td><center>" + questions.getString("Q.qtext") + "</center></td>"
						+		"<td><center>" + questions.getString("U.username") + "</center></td>"
						+		"<td><center>"+ questions.getString("U1.firstname") + "</center></td>"
						);
				if(request.getSession().getAttribute("role").equals("rep")){
					if(questions.getString("answer")!=null){
						writer.println("<td><center><a href = \"question?" + questions.getInt("QuestionID") + "\">Edit Answer</a></center></td>");
					}
					else
						writer.println("<td><center><a href = \"question?" + questions.getInt("QuestionID") + "\">Answer Question</a></center></td>");
				}
				else{
					if(questions.getString("answer")!=null){
						writer.println("<td><center><a href = \"question?" + questions.getInt("QuestionID") + "\">View Answer</a></center></td>");
					}
					else
						writer.println("<td><center>Question Unanswered</center></td>");
				}
			}

			writer.println("</table>"
					+ "<br>"
					+ "<br>"
					+ "<br>");

		} catch (SQLException s) {
			writer.println("Failed to get auction list: " + s.getMessage() + "<br>");
		} catch (Exception e) {
			writer.println("Failed to get auction list: " + e.getMessage() + "<br>");
		} finally {
			//Close resultset, statement, connection.
			try {
				if (questions != null) {
					questions.close();
				}
				if (getQuestions != null) {
					getQuestions.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sql) {
				/*
				 * Do nothing. The page has already loaded - no need to let the user know
				 * there was an error that doesn't affect them.
				 */
			} catch (Exception e) {
				/*
				 * Do nothing. The page has already loaded - no need to let the user know
				 * there was an error that doesn't affect them.
				 */
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
