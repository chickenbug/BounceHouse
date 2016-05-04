package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AutoBid {
	public int autoBidID;
	public int auctionID;
	public int userID;
	public float maxBid;	
	
	public AutoBid(int autoBidID, int auctionID, int userID, float maxBid){
		this.auctionID = autoBidID;
		this.auctionID = auctionID;
		this.userID = userID;
		this.maxBid = maxBid;
	}
	
	private static ArrayList<AutoBid> findAutoBids(int auctionID){
		try{
			Connection con = SQLConnector.getConnection();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM AutoBid WHERE AuctionID = " + auctionID);
			ArrayList<AutoBid> bidlist = new ArrayList<AutoBid>();
			while(rs.next()){
				bidlist.add(new AutoBid(
						rs.getInt("AutoBidID"),
						rs.getInt("AuctionID"),
						rs.getInt("UserID"),
						rs.getFloat("MaxBid")
						));
			}
			if(bidlist.size() == 0) return null;
			con.close();
			s.close();
			rs.close();
			return bidlist;
		}
		catch(IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e){
			System.out.println("error finding username");
			return null;
		}
	}
	
	public static boolean deleteAutobid(int auctionID, int userID){
		try {
			String sql = "DELETE FROM AutoBid WHERE AuctionID = ? AND UserID = ?";
			Connection c = SQLConnector.getConnection();
			PreparedStatement s = c.prepareStatement(sql);
			s.setInt(1, auctionID);
			s.setInt(2, userID);
			s.executeUpdate();
			c.close();
			s.close();
			return true;
		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static boolean insertAutobid(int auctionID, int userID, float maxval){
		try {
			deleteAutobid(auctionID, userID);
			String sql = "INSERT INTO AutoBid (AuctionID, UserID, MaxBid) VALUES (?,?,?)";
			Connection c = SQLConnector.getConnection();
			PreparedStatement s = c.prepareStatement(sql);
			s.setInt(1, auctionID);
			s.setInt(2, userID);
			s.setFloat(3, maxval);
			s.executeUpdate();
			c.close();
			s.close();
			return true;
		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean runAutobids(int auctionID){
			ArrayList<AutoBid> bidlist = findAutoBids(auctionID);	
			Auction a = Auction.findAuction(auctionID);
			if(a == null || bidlist == null) return false;
			float current_wbid = a.win_bid;
			int current_wID = a.winID;
			
			boolean possible_autobid = true;
			while(possible_autobid){
				possible_autobid = false;
				for(int i = 0; i< bidlist.size(); i++){
					AutoBid b = bidlist.get(i);
					current_wID = Auction.findWinID(auctionID);
					if(current_wbid + a.increment <= b.maxBid && current_wID != b.userID){
						Bid.insertBid(auctionID, b.userID , current_wbid + a.increment);
						possible_autobid = true;
						current_wbid += a.increment;
					}
				}
			}
			return true;
	}
	
	public static void sendAlerts(float amount, int auctionID, int userID) {
		Connection con = null;
		Statement S = null;
		Statement S2 = null;
		ResultSet rs = null;
		
		try {
			con = SQLConnector.getConnection();
			S = con.createStatement();
			S2 = con.createStatement();
			rs = S.executeQuery("SELECT * FROM AutoBid WHERE MaxBid < " + amount + " AND UserID <> " + userID + " AND AuctionID = " + auctionID + ";");
			
			while (rs.next()) {
				S2.executeUpdate("INSERT INTO Email(Content,Recipient,RecipientID,Sender,SenderID,SendTime) VALUES (\"Someone has made a bid higher than your autobid!\""
						+ ",(SELECT Email FROM User WHERE UserID = " + userID + ")," + userID + ",\"system@bouncehouseemporium\",1,NOW());");
			}
			
		} catch (SQLException s){
			
		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
			
		} finally {
			try {
				con.close();
				S.close();
				S2.close();
				rs.close();
			} catch (SQLException s) {
				
			}
		}
		return;
	}
}
