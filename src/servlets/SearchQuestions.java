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
 * Servlet implementation class SearchQuestions
 */
@WebServlet(name = "SearchQuestions", urlPatterns={"/SearchQuestions"})
public class SearchQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchQuestions() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
		}
		
		PrintWriter writer  = response.getWriter();
		writer.println("<html>" +
							"<head>" +
								"<title>" +
									"Bouncehouse Emporium - Search Questions" +
								"</title>" +
							"</head>" +
							"<body>" +
								"<center>" +
								"<h1>Bouncehouse Emporium</h1>" +
								"<h3>Question Search Results</h3>" +
								"<hr>" +
								"<table border = 1 width = 75%>"+
								"<tr>"
								+		"<td><center> Topic </center></td>"
								+		"<td><center> Question </center></td>"
								+		"<td><center> Asking User </center></td>"
								+		"<td><center> Responding Rep </center></td>"
								+ 		"<td><center> View Answer </center></td></tr>"
		);	
		
		/*
		 * Boolean statements to keep track of what the user searched for. 
		 * Relevance level is just a measure of how many fields matched the user's search.
		 * In the current implementation, these are all disabled.
		 * Re-enabling them would allow to return *partially* relevant results for the search -
		 * but as it stands, the current implementation takes the least confusing route for the end
		 * user and only returns tuples matching their query exactly.
		 */
		Connection connection = null;
		Statement getQuestions = null;
		ResultSet questions = null;
		int count = 0;
		String query = null;

		
		try {
			connection = SQLConnector.getConnection();
			getQuestions = connection.createStatement();
			query = "SELECT Q.*, U.username, U1.firstname FROM question Q, user U, user U1 WHERE U.userid=Q.userid AND Q.repid=U1.userid";

			if(request.getParameter("topic") != null) {
				if(!request.getParameter("topic").equals("")) query += " AND topic = \"" + request.getParameter("topic") + "\";";
			} 
			
			questions = getQuestions.executeQuery(query);
			
			while (questions.next()) {
				count++;
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
			
			if (count == 0) {
				writer.println("<tr>"
						+ 	"<td><center>We're sorry, but there were no questions matching your search query.</center></td>"
						+	"</tr>"
				);
			}
			
			writer.println("</table>"
						+ "<br>"
						+ "<br>"
						+ "<br>");
					
			} catch (SQLException s) {
				writer.println("Search failed: " + s.getMessage() + "<br>");
			} catch (Exception e) {
					writer.println("Search failed: " + e.getMessage() + "<br>");
					//writer.println(e.getCause());
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
					writer.println("<br>"
								+	"<a href = \"GetContent\">Return to Main Page</a>"
								+	"</center>"
								+	"</body"
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
