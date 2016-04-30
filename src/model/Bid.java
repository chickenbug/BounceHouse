package model;

import java.sql.*;
import java.util.Calendar;

public class Bid {
	public int bidID;
	public float amount;
	public int auctionID;
	public Timestamp bidTime;
	public int userID;
	
	public Bid(int bidID, float amount, int auctionID, Timestamp bidTime, int userID){
		this.bidID = bidID;
		this.amount = amount;
		this.auctionID = auctionID;
		this.bidTime = bidTime;
		this.userID = userID;
	}
	
	public static boolean insertBid(int auctionID, int userID, float amount){
		try {
			String sql = "INSERT INTO Bid (AuctionID, UserID, Amount, BidTime) VALUES (?,?,?,?)";
			Connection c = SQLConnector.getConnection();
			PreparedStatement s = c.prepareStatement(sql);
			s.setInt(1, auctionID);
			s.setInt(2, userID);
			s.setFloat(3, amount);
			s.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			s.executeUpdate();
			return true;
		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
