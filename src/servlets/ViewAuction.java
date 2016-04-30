package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.NoSuchElementException;

import model.Auction;
import model.Item;

/**
 * Servlet implementation class Auction
 */
@WebServlet("/auction")
public class ViewAuction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewAuction() {
        super();
        // TODO Auto-generated constructor stub
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			int auctionID = Integer.parseInt(request.getQueryString());
			if(auctionID < 0) throw new NumberFormatException();
			
			Auction a = Auction.findAuction(auctionID);
			if(a == null) throw new NoSuchElementException();
			
			Item i = Item.findItem(a.itemID);
			if(i == null) throw new NoSuchElementException();
			
			request.setAttribute("a", a);
			request.setAttribute("i", i);
			request.getRequestDispatcher("WEB-INF/auction.jsp?" + auctionID).forward(request, response);
		}
		catch(NumberFormatException| NoSuchElementException e ){
			System.out.println("Invalid Query Param (Negative or non Integer)");
			response.sendRedirect("no_auction.jsp?" + request.getQueryString());
		}
	}

}
