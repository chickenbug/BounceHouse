package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SQLConnector;

/**
 * Servlet implementation class ViewBestBuyers
 */
@WebServlet(name = "ViewBestBuyers", urlPatterns ={"/ViewBestBuyers"})
public class ViewBestBuyers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null || !request.getSession().getAttribute("role").equals("admin")) {
			response.sendError(403, "You are not authorized to access this page.");
			return;
		}
		PrintWriter writer  = response.getWriter();

		writer.println("<html>" 
				+	"<head>" 
				+		"<title>" 
				+			"Bouncehouse Emporium - Best Buyers" 
				+		"</title>" 
				+	"</head>" 
				+	"<body>" 
				+		"<center>" 
				+			"<h1>Bouncehouse Emporium</h1>" 
				+			"<h3> Best Buyers </h3>"
				+	"<h4>	<a href = \"salesReports.jsp\">Return to sales reports page</a> </h4>"
				+			"<hr>"
				+			"<table border = 1 width = 100%>"
				);

		Connection connection = null;
		Statement getEarnings = null;
		ResultSet earnings = null;

		//Open connection, create statement, and process request.
		try {
			connection = SQLConnector.getConnection();
			getEarnings = connection.createStatement();

			earnings = getEarnings.executeQuery("SELECT U.username FROM User U, Auction A WHERE A.completed=1 AND U.userid=A.winnerid");
			/*
			 * Process request and output HTML.
			 */
			writer.println("<tr>"
					+		"<td><center><span style = \"font-weight:bold\"> Username </span></center></td>"
					+		"<td><center><span style = \"font-weight:bold\"> Number of Items Bought </span></center></td>");

			Map<String,Integer> count = new HashMap<String,Integer>();
			Map<String,Integer> sortedCount = new HashMap<String,Integer>();
			while(earnings.next()){
				if(!count.containsKey(earnings.getString("U.username"))){
					count.put(earnings.getString("U.username"), 1);
				}
				else{
					count.put(earnings.getString("U.username"), count.get(earnings.getString("U.username"))+1);
				}
			}
			sortedCount = sortByValue(count);
			for(Entry<String, Integer> entry: sortedCount.entrySet()){
				writer.println("<tr><td><center>" + entry.getKey() + "</center></td>"
						+		"<td><center>" + entry.getValue() + "</center></td>");
			}

			writer.println("</table>"
					+ "<br>"
					+ "<br>"
					+ "<br>");

		} catch (SQLException s) {
			writer.println("Failed to get earnings list: " + s.getMessage() + "<br>");
			return;
		} catch (Exception e) {
			writer.println("Failed to get earnings list: " + e.getMessage() + "<br>");
			return;
		} finally {
			//Close resultset, statement, connection.
			try {
				if (earnings != null) {
					earnings.close();
				}
				if (getEarnings != null) {
					getEarnings.close();
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
	public static Map<String, Integer> sortByValue( Map<String, Integer> map )
	{
		List<Map.Entry<String, Integer>> list =
				new LinkedList<Map.Entry<String, Integer>>( map.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
		{
			public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
			{
				return (o2.getValue()).compareTo( o1.getValue() );
			}
		} );

		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : list)
		{
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}
}
