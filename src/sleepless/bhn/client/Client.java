/**
 * Client.java
 * Purpose: use sleepless.bhn.mysql.ConnectMyQL
 */
package sleepless.bhn.client;

import sleepless.bhn.mysql.UseMySQL;

/**
 * @author Borys Niewiadomski
 * @version 0.1
 */
public class Client {	
	/**
	 * Main applications
	 * 
	 * @param args
	 */
	public static void main(String [] args) {
		String url = ""; // i.e. jdbc:mysql://localhost:3306
		String user = "";
		String password = "";
		
		// init mysql connection
		UseMySQL mysql = new UseMySQL(url, user, password);
		// open mysql connection
		mysql.openConnection();
		
		// init some test variables
		String [] resultPrintArgs = {"name", "date", "location", "comment"};
		String [][] results = null; 
		
		// test runSQLStatementUpdate(...)
		String statementUpdateSQL = "insert into summer_plans.plans values (default, 'Do This', '2013-06-04', 'This place', 'do this at this place')";
		mysql.runSQLStatementUpdate(statementUpdateSQL);
		
		// test runSQLStaetmentQuery(...) and printResult(...);
		String satementQuerySQL = "select * from summer_plans.plans where name='Do This'";
		results = mysql.runSQLStatementQuery(satementQuerySQL, resultPrintArgs);
		printResults(results, resultPrintArgs);
		
		// test runSQLPreparedUpdate(...)
		String preparedUpdateSQL = "insert into summer_plans.plans values (default, ?, ?, ?, ?)";
		String [] preparedUpdateArgs = {"Doing Something", "2013-06-21", "Somewhere", "do something somewhere"};
		mysql.runSQLPreparedUpdate(preparedUpdateSQL, preparedUpdateArgs);
		
		// test runSQLPreparedQuery(...) and printResult(...)
		String preparedQuerySQL = "select * from summer_plans.plans where name=? AND location=?";
		String [] preparedQueryArgs = {"Doing Something", "Somewhere"};
		results = null; 
		results = mysql.runSQLPreparedQuery(preparedQuerySQL, preparedQueryArgs, resultPrintArgs); 
		printResults(results, resultPrintArgs);
		
		// close mysql conneciton
		mysql.closeConnection();
	}
	
	public static void printResults(String [][] results, String [] args) {
		if (results != null) {
			for (int i = 0; i < results.length; i++) {
				for (int j = 0; j < args.length; j++) {
					System.out.print(args[j] + ": " + results[i][j] + ", ");
				}
				System.out.println();
			}
		} else {
			System.out.println("Result is empty...");
		}
	}
}
