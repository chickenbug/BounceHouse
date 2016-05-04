package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
	
	public static String findUName(int userID){
		try{
			Connection con = SQLConnector.getConnection();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT Username FROM User WHERE UserID = "+ userID);
			if(!rs.next()) return null;
			String user = rs.getString(1);
			con.close();
			s.close();
			rs.close();
			return user;
		}
		catch(IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e){
			System.out.println("error finding username");
			return null;
		}
	}
}
