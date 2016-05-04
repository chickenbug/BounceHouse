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
					+ ") AND B.UserID = U.UserID");
			if(!rs.next()) return null;
			String maxUname= rs.getString(1);
			con.close(); 
			return maxUname;
		}
		catch(IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e){
			System.out.println("error finding username");
			return null;
		}
	}
	
	/**
	 * inserts a bid into the bidtable given auctionID, userID and amount
	 * @param auctionID
	 * @param userID
	 * @param amount
	 * @return false on failure, true on success
	 */
	public static boolean insertBid(int auctionID, int userID, float amount){
		try {
			Auction a = Auction.findAuction(auctionID);
			if(a == null || a.completed == 1){
				System.out.println("Cannot Make Bid. Auction does not exist or is closed.");
				return false;
			}
			
			String sql = "INSERT INTO Bid (AuctionID, UserID, Amount, BidTime) VALUES (?,?,?,?)";
			Connection c = SQLConnector.getConnection();
			PreparedStatement s = c.prepareStatement(sql);
			s.setInt(1, auctionID);
			s.setInt(2, userID);
			s.setFloat(3, amount);
			s.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			s.executeUpdate();
			Auction.updateTop(auctionID, amount, userID);
			c.close();
			return true;
		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
