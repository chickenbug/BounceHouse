package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.*;

import model.SQLConnector;

/**
 * Servlet implementation class create_auction
 */
@WebServlet("/create_auction")
public class CreateAuction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateAuction() {
        super();
    }
    
    // inserts item into db returns zero if things go south
    private int insert_item(int bounce, String category, String size, String subcategory,
    		String title, String description, HttpServletResponse response) throws IOException{
			try {
				Connection con = SQLConnector.getConnection();
				String sql = "INSERT INTO Item (Bounciness, Category, Size, Subcategory, Description, Title) VALUES "
						+ "(?,?,?,?,?,?)";
				PreparedStatement s = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				s.setInt(1, bounce);
				s.setString(2, category);
				s.setString(3, size);
				s.setString(4, subcategory);
				s.setString(5, description);
				s.setString(6, title);
				s.executeUpdate();
				ResultSet r = s.getGeneratedKeys();
				
				r.next();
				return r.getInt(1);
			} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				response.sendRedirect("error.html");
			}
    	return 0;
    }
    
    // Inserts item into auction table returns zeor in case things go pear shaped
    private int insert_auction(int ItemID, float minbid, Timestamp t, int UserID, HttpServletResponse response) throws IOException{
			try {
				Connection con = SQLConnector.getConnection();
				String sql = "INSERT INTO Auction (CloseDate, ItemID, MinBid, UserID) VALUES (?,?,?,?)";
				PreparedStatement s = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				s.setTimestamp(1, t);
				s.setInt(2, ItemID);
				s.setFloat(3, minbid);
				s.setInt(4, UserID);
				s.executeUpdate();
				
				ResultSet r = s.getGeneratedKeys();
				r.next();
				return r.getInt(1);
				
				
			} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				response.sendRedirect("error.html");
			}
			return 0;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
			return;
		}
			request.getRequestDispatcher(("WEB-INF/create_auction.html")).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
			return;
		}
		try {
			int bounce = Integer.parseInt(request.getParameter("bounce"));
			String category = request.getParameter("category");
			String size = request.getParameter("size");
			String subcategory = request.getParameter("subcategory");
			String title = request.getParameter("title");
			String description = request.getParameter("description");		
			float minbid = Float.parseFloat(request.getParameter("minbid"));
			int userID = (Integer)request.getSession().getAttribute("userID");
			
			SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			String date_string = request.getParameter("datetime");
			Timestamp t = new Timestamp(sd.parse(date_string).getTime());
			if(t.before(new Date(Calendar.getInstance().getTimeInMillis()))){
				System.out.println("Date in the past");
				response.sendRedirect("create_auction.html?past-error");
			}
			else{
				int ItemID = insert_item(bounce,category,size,subcategory,title,description, response);
				int AuctionID = insert_auction(ItemID, minbid, t, userID, response);
				response.sendRedirect("auction?" + AuctionID);
			}
		} catch (ParseException e1) {
			System.out.println("Whoa! unparsable date?");
			response.sendRedirect("create_auction.html?dt-error");
		}
	}

}
