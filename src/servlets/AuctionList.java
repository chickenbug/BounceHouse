package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.*;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class AuctionList  extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{		
		PrintWriter writer  = response.getWriter();
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultset = null; 
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//username and password below are placeholders - replace them 
			connection = DriverManager.getConnection("jdbc:mysql://localhost/proj_2016", "root", "sqlpls7");
			statement = connection.createStatement();
			resultset = statement.executeQuery("SELECT * FROM Auction;");
			while (resultset.next()) {
				writer.println(resultset.getString("AuctionID"));
			}
		} catch (SQLException s) {
			response.sendRedirect("error.jsp");
		} catch (Exception e) {
			response.sendRedirect("error.jsp");
		}
		request.getRequestDispatcher("auctionPage.jsp");
		
	}

}
