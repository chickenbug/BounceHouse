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
	
	public static int findWinID(int auctionID){
		try{
			Connection con = SQLConnector.getConnection();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT WinnerID FROM Auction WHERE AuctionID = "+ auctionID);
			if(!rs.next()) return -1;
			return rs.getInt("WinnerID"); 
		}
		catch(IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e){
			System.out.println("Unable to find WinBid");
			return -1;
		}	
	}
	
	// returns the value of the current top bid given auction ID
	public static float findWinBid(int auctionID){
		try{
			Connection con = SQLConnector.getConnection();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT WinBid FROM Auction WHERE AuctionID = "+ auctionID);
			if(!rs.next()) return -1;
			return rs.getFloat("WinBid"); 
		}
		catch(IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e){
			System.out.println("Unable to find WinBid");
			return -1;
		}
	}
	
	public static boolean updateTop(int auctionID, float winBid, int userID){
		try{
			Connection con = SQLConnector.getConnection();
			Statement s = con.createStatement();
			s.executeUpdate("UPDATE Auction SET WinBid = " + winBid+ ", WinnerID = " + userID + " WHERE AuctionID = "+ auctionID);
			return true;
		}
		catch(IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e){
			System.out.println("Invalid Query Param (Negative or non Integer)");
			return false;
		}
		
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
