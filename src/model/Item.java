package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Item {
	public int itemID;
	public int bounciness;
	public String category;
	public String title;
	public String description;
	public String size;
	public String subCategory;
	
	public Item(int itemID, int b, String c, String t, String d, String siz, String subc){
		this.itemID = itemID;
		this.bounciness = b;
		this.category = c ;
		this.title = t;
		this.description = d;
		this.size = siz;
		this.subCategory = subc;
	}
	
	public static Item findItem(int itemID){
		try{
		Connection con = SQLConnector.getConnection();
		Statement s = con.createStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM Item WHERE ItemID = "+ itemID);
		if(!rs.next()) return null;
		Item i = new Item(
				rs.getInt("ItemID"),
				rs.getInt("Bounciness"),
				rs.getString("Category"),
				rs.getString("Title"),
				rs.getString("Description"),
				rs.getString("Size"),
				rs.getString("SubCategory"));
		return i;
		}
		catch(NumberFormatException | IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e){
			System.out.println("Unable to Find Item");
			return null;
		}
	}
}
