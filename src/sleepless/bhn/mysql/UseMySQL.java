/**
 * Connect.java
 * Purpose: Sample MySQL connection and interaction class using JDBC
 */
package sleepless.bhn.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Borys Niewiadomski
 * @version 0.1 05.31.2013
 * <p>
 * Constructor initializes variables.
 * Client should manage opening and closing the connection.
 * Statement opening and closing is handled by this class. Client just
 * needs to supply the SQL updates or queries. 
 * <P>
 * Supports Statement queries and updates
 * Supports PreparedStatement queries and updates
 * <p>
 * use printResult to print results from queries to console
 * use getResult to return String array from queries
 */
public class UseMySQL {
	private Connection connection;
	private String user;
	private String password;
	private String url;
	
	/**
	 * Constructor. 
	 * A check for MySQL JDBC driver happens here.
	 * 
	 * @param url String url pointing to MySQL Server
	 * @param user String containing username
	 * @param password String containing password
	 */
	public UseMySQL(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.connection = null;
		
		// load mysql driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC Driver not found...");
			e.printStackTrace();
		}
	}
	
	/**
	 * Open connection to the MySQL server.
	 * @
	 */
	public void openConnection() {
		try {
			connection = DriverManager.getConnection(this.url, this.user, this.password);
		} catch (SQLException e) {
			System.out.println("MySQL connection failed...");
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the MySQL server connection.
	 * @
	 */
	public void closeConnection() { 
		try {
			if (this.connection != null) {
				this.connection.close(); 
			}
		} catch (SQLException e) {
			System.out.println("MySQL connection failed...");
			e.printStackTrace();
		}
	}
	
	/**
	 * Run an SQL Statement Query.
	 * 
	 * @param sqlQuery String containing the SQL query to execute.
	 * @param sqlPrintArgs String array containing column names for data retrieval
	 * @return String[][] return result set data
	 */
	public String [][] runSQLStatementQuery(String sqlQuery, String [] sqlPrintArgs) {
		ResultSet resultSet = null;
		Statement statement = null; 
		String [][] results = null;
		// attempt to execute sql statement
		try {
			// execute statement
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlQuery);
			
			// save results
			results = getResults(resultSet, sqlPrintArgs);
			
			// clean up
			statement.close();
			resultSet.close();
		} catch (SQLException e) {
			System.out.println("MySQL statement query failed...");
			e.printStackTrace();
		} 
		
		return results;
	}
	
	/**
	 * Run an SQL statement update.
	 * 
	 * @param sqlUpdate String representing the SQL update to execute
	 */
	public void runSQLStatementUpdate(String sqlUpdate) {
		Statement statement = null;
		
		try {
			// execute query
			statement = connection.createStatement();
			statement.executeUpdate(sqlUpdate);
			// clean up
			statement.close();
		} catch (SQLException e) {
			System.out.println();
			e.printStackTrace();
		}
	}
	
	/**
	 * Run an SQL PreparedStatement update.
	 * 
	 * @param sqlUpdate String containing prepared SQL update to execute.
	 * @param args String array containing data for prepared SQL update.
	 */
	public void runSQLPreparedUpdate(String sqlUpdate, String [] args) {
		PreparedStatement preparedStatement = null;
		
		// attempt to execute prepared statment
		try {
			preparedStatement = connection.prepareStatement(sqlUpdate);
			// traverse args for sql input
			for (int i = 0; i < args.length; i++) {
				// PreparedStatment arguments start at 1, i+1
				preparedStatement.setString(i+1, args[i]);
			}
			// execute query
			preparedStatement.executeUpdate();
			// clean up
			preparedStatement.close();
		} catch (SQLException e) {
			System.out.println("MySQL prepared statement udpate failed...");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Run an SQL PreparedStatement query.
	 * 
	 * @param sqlQuery String containing the SQL query to execute.
	 * @param queryArgs String array containing the prepared query data .
	 * @param sqlPrintArgs String array containing column names for data retrieval.
	 * @return String[][] String multidimensional array containing result set data.
	 */
	public String [][] runSQLPreparedQuery(String sqlQuery, String [] queryArgs, String [] printArgs) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String [][] results = null;
		// attempt to execute prepared statment
		try {
			preparedStatement = connection.prepareStatement(sqlQuery);
			// traverse args for sql input
			if (queryArgs != null) {
				for (int i = 0; i < queryArgs.length; i++) {
					// PreparedStatment arguments start at 1, i+1
					preparedStatement.setString(i+1, queryArgs[i]);
				}
			}
			resultSet = preparedStatement.executeQuery();
			results = getResults(resultSet, printArgs);
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			System.out.println("MySQL prepared statement query failed...");
			e.printStackTrace();
		}
		
		return results;
	}
	
	/**
	 * Return results of SQL query in a multidimensional string array
	 * String[rows][columns] 
	 * 
	 * @param resultSet ResultSet containing results from SQL query.
	 * @param args String array containing column names for date retrieval.
	 * @return String[][] String multidimensional array containing query results.
	 */
	private String [][] getResults(ResultSet resultSet, String [] args) {
		// store results in a multi-dimensional array
		String [][] results = null;
		
		try {
			// first going to figure out how many rows we have
			// move cursor to last position
			resultSet.last();
			// setup results using [rows][cols]
			// ReseultSet rows start at one
			int rows = resultSet.getRow();
			int cols = args.length;
			results = new String[rows][cols];
			// move cursor back to before first position
			resultSet.beforeFirst();
			
			while (resultSet.next()) {
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// System.out.print(args[j] + ": " + resultSet.getString(args[j]) + ". ");
						results[i][j] = resultSet.getString(args[j]);
					}
				}
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("MySQL printResult failed...");
			e.printStackTrace();
		}
		
		return results;
	}
}
