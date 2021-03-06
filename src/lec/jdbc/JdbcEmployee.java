package lec.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcEmployee {

	public static void main(String[] args) throws Exception {
		var out = System.out; 
		
		out.println( "Hello ..." );
		
		Class.forName("org.mariadb.jdbc.Driver");

		// Create a database connection by using user name and password
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/MY_SCHEMA", "MY_USER", "admin");		
		
		Statement stmt = conn.createStatement();
		
		// drop table using statement
		int upNo = stmt.executeUpdate( "DROP TABLE if exists EMPLOYEE" );
		
		if( upNo >= 1 ) {
			out.println( "Employee tabled is deleted." );
		} else {
			out.println( "Employee tabled is not deleted." );
		}
		
		// creatte table using statement
		String sql = """
					CREATE TABLE employee ( 
					  id INT PRIMARY KEY AUTO_INCREMENT ,
					  first_name VARCHAR(200) NOT NULL ,
					  last_name VARCHAR(200) ,
					  email VARCHAR(200) NOT NULL ,
					  age INT ,
					  phone_no VARCHAR(200)
					) ;				
				""";
		
		upNo = stmt.executeUpdate( sql );
		
		if( upNo >= 1 ) {
			out.println( "Employee tabled is created." );
		} else {
			out.println( "Employee tabled is not created." );
		}
		
		// insert a record using statement
		sql = "INSERT INTO employee(first_name, email, age ) VALUES( 'john', 'john@google.com', 18 )" ;
		upNo = stmt.executeUpdate( sql );
		
		if( upNo >= 1 ) {
			out.println( "Employee record is inserted." );
		} else {
			out.println( "Employee tabled is not inserted." );
		}
		
		// insert record using prepared statement		
		sql = "INSERT INTO employee(first_name, email, age) VALUES( ?, ?, ? )" ;
		
		PreparedStatement pst = conn.prepareStatement( sql );
		Object [][] records = { { "brown", "brown@gmail.com", 20 }, { "jane", "jane@gmail.com", 22 } }; 
		
		for( var record : records ) {
			var idx = 1; // Index starts from one.
			for( var c : record ) {
				if( c instanceof String ) { 
					pst.setString( idx ++, "" + c );
				} else if ( c instanceof Integer ) {
					pst.setInt(idx, (int) c);
				}
			}
			upNo = pst.executeUpdate();
			if( upNo >= 1 ) {
				out.println( "Employee record is inserted. ");
			} else {
				out.println( "Employee tabled is not inserted." );
			}
		}
		
		// query using prepared statement
		sql = "SELECT first_name, email, age FROM employee where 1 = ?";
		pst = conn.prepareStatement( sql );
		var idx = 1 ;
		pst.setInt( idx ++, 1 );
		ResultSet rs = pst.executeQuery();
		out.println( "Query Result..." );
		while( rs.next() ) {
			idx = 1;
			String firstName = rs.getString( idx ++ );
			String email = rs.getString( idx ++ );
			Integer age = rs.getInt( idx ++ );
			out.println( String.format( "first_name = %s, email = %s, age = %s", firstName, email, age) );
		}
		
		// clear resources
		rs.close();
		pst.close();
		stmt.close();		
		conn.close(); 
		
		out.println( "Good bye!" );		
	} 
}