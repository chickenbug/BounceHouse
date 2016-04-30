package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		float amount = Float.parseFloat(request.getParameter("bid"));
		int auctionID = Integer.parseInt(request.getParameter("auction"));
		Bid.insertBid(auctionID, 1, amount); //TODO swap this with real UserID
		request.setAttribute("type", "");
		request.setAttribute("value", Float.toString(amount));
		request.setAttribute("auction", Integer.toString(auctionID));
		//Run Autobids
		request.getRequestDispatcher("bid_success.jsp").forward(request, response);;
	}

}
