package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Auction;
import model.AutoBid;
import model.Bid;

/**
 * Servlet implementation class Bid
 */
@WebServlet("/bid")
public class MakeBid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MakeBid() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
			return;
		}
		float amount = Float.parseFloat(request.getParameter("bid"));
		int auctionID = Integer.parseInt(request.getParameter("auction"));
		
		Auction a = Auction.findAuction(auctionID);
		if( a == null || a.completed == 1 || a.win_bid + 1 > amount){
			Bid.insertBid(auctionID, (Integer)request.getSession().getAttribute("userID"), amount);
			request.setAttribute("type", "");
			request.setAttribute("value", Float.toString(amount));
			request.setAttribute("auction", Integer.toString(auctionID));
			AutoBid.runAutobids(auctionID);
			request.getRequestDispatcher("bid_success.jsp").forward(request, response);
		}
		else{
			response.sendError(400, "Error Making Bid. Auction does not exist, Auction is closed, or bid amount too low");
		}
		return;
	}

}
