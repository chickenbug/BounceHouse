package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimerTask;

public class CloseAuction extends TimerTask{
	private int auctionID;
	
	
	public CloseAuction(int auctionID) {
		this.auctionID = auctionID;
	}

	public void run() {
		try{
			Connection con = SQLConnector.getConnection();
			Statement s = con.createStatement();
			s.executeUpdate("UPDATE Auction SET Completed = 1 WHERE AuctionID = "+ auctionID);
			con.close();
			return;
		}
		catch(IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e){
			System.out.println("Unable to close auction");
			return;
		}
	}
	
	
}
