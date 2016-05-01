package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Auction {
	public int AuctionID;
	public Timestamp close_date;
	public int completed;
	public int itemID;
	public float min_bid;
	public int user_id;
	public float win_bid;
	
	public Auction(int AuctionID, Timestamp close_date, int completed, int itemID, float min_bid, int user_id, float win_bid){
		this.AuctionID = AuctionID;
		this.close_date = close_date;
		this.completed = completed;
		this.itemID =itemID;
		this.min_bid = min_bid;
		this.user_id = user_id;
		this.win_bid = win_bid;
	}
	
	public static Auction findAuction(int auctionID){
		try{
			Connection con = SQLConnector.getConnection();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM Auction WHERE AuctionID = "+ auctionID);
			if(!rs.next()) return null;
			Auction a = new Auction(
					rs.getInt("AuctionID"),
					rs.getTimestamp("CloseDate"),
					rs.getInt("Completed"),
					rs.getInt("ItemID"),
					rs.getFloat("MinBid"),
					rs.getInt("UserID"),
					rs.getFloat("WinBid"));
			return a;
		}
		catch(IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e){
			System.out.println("Invalid Query Param (Negative or non Integer)");
			return null;
		}
	}
}
