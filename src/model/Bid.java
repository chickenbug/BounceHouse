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
	
	public static String maxBidUsername(int auctionID){
		try{
			Connection con = SQLConnector.getConnection();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(
					"SELECT U.Username FROM User U, Bid B WHERE B.auctionID = " + auctionID 
					+ " AND B.Amount = (SELECT MAX(AMOUNT) FROM Bid WHERE auctionID = " + auctionID 
					+ "B.UserID = U.UserID");
			if(!rs.next()) return null;
			return rs.getString(1);
		}
		catch(IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e){
			System.out.println("error finding username");
			return null;
		}
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
