package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.AutoBid;

/**
 * Servlet implementation class MakeAutoBid
 */
@WebServlet("/MakeAutoBid")
public class MakeAutoBid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MakeAutoBid() {
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
		
		float maxBid = Float.parseFloat(request.getParameter("MaxBid"));
		int auctionID = Integer.parseInt(request.getParameter("auction"));
		int userID = (Integer)request.getSession().getAttribute("userID");
		if(!AutoBid.insertAutobid(auctionID, userID, maxBid)){
			response.sendRedirect("error.html");
			return;
		}
		
		AutoBid.runAutobids(auctionID);
		
		request.setAttribute("type", "Auto-");
		request.setAttribute("value", Float.toString(maxBid));
		request.setAttribute("auction", Integer.toString(auctionID));
		request.getRequestDispatcher("bid_success.jsp").forward(request, response);;
		
		
	}

}
