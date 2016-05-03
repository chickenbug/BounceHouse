package model;
import java.sql.*;

/**
 * Provides the Controller a connection between the program's logic and the model.
 * @author Team 17
 *
 */

public class SQLConnector {
	
	/**
	 * Connection URL.
	 */
	private static final String connection = "com.mysql.jdbc.Driver";
	/**
	 * Database admin user.
	 */
	private static final String dbUser = "root";
	/**
	 * Database amdin password.
	 */
	private static final String dbPassword = "sqlpls7";
	/**
	 * SQLConnector instance connection object.
	 */
	private Connection dbconnection;
	/**
	 * Database name on server.
	 */
	private static final String dbName = "proj2016";
	/**
	 * Keeps track of current live connections.
	 */
	private static int currentConnections = 0;
	/**
	 * Maximum number of simultaneous live connection on Database.
	 */
	private final static int maxConnections = 150;
	
	/**
	 * SQLConnector constructor.
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	private SQLConnector() throws 
	SQLException, 
	IllegalAccessException, 
	InstantiationException, 
	ClassNotFoundException {
		Class.forName(connection).newInstance();
		dbconnection = DriverManager
		          .getConnection("jdbc:mysql://localhost:3306/"+dbName,dbUser,dbPassword);
		
		System.out.println("Connection established with "+dbName);
		
	}
	
	/** Validates if a connection is possible. If so, returns a live connection to the Database. Otherwise, it throws a DB exception.
	 * 
	 * @return connection
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public static Connection getConnection() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
		if(currentConnections < maxConnections) {
			Connection con = new SQLConnector().dbconnection;
			++currentConnections;
			return con;
		} else {
			throw new SQLException("ERROR: Connections exceeded");
		}
	}
	
	public synchronized static void closeConnection(Connection con) throws SQLException {
		con.close();
		--currentConnections;
	}
}
