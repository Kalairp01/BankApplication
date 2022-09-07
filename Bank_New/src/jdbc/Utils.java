//$Id$
package jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class Utils {
	
	public static Connection init_connection() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://localhost:5432/kalai-15140";
		String username = "kalai-15140";
		String password ="1234";
		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}
	
	public static void insert_query(Map<String,String> details, String table) throws ClassNotFoundException, SQLException {
		
		Connection con = Utils.init_connection();
		int length = details.size(),value=1;
		String parameters ="";
		String columns ="";
		String query="insert into "+table+" ";
		parameters+="(";
		
		for(Map.Entry<String, String> detail : details.entrySet()) {
			if(value!=length) {
			columns+=detail.getKey()+",";
			parameters+="?,";
			}
			else {
				columns+=detail.getKey();
				parameters+="?";
			}
			value++;
		}
		
		value=1;
		parameters+=")";
		query+="(" +columns +")" + " values "+ parameters+";";
		PreparedStatement ps = con.prepareStatement(query);
		for(Map.Entry<String, String> detail : details.entrySet()) {
			if(detail.getKey().endsWith("id") || detail.getKey().endsWith("amount") || detail.getKey().endsWith("no") ||
					detail.getKey().endsWith("interest") || detail.getKey().endsWith("period") || detail.getKey().endsWith("balance"))
				ps.setInt(value, Integer.parseInt(detail.getValue()));
			else if(detail.getKey().endsWith("date")) {  
			    Date date=Date.valueOf(detail.getValue());
			    ps.setDate(value,date);
			}				
			else 
				ps.setString(value, detail.getValue());
			value++;
		}
		
		
		ps.executeUpdate();
		con.close();
		ps.close();	
		
		System.out.println("Success");
	}
	
	
	public static ResultSet get_result_set(String table, int id) {
		String query = "select * from "+table+" where id=? ;";
		
		try {
			Connection con = init_connection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			return rs;
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.out.println(table + "details not found");
		}
		
		return null;
		
	}
	
	public static Date get_date() {
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		return date;
	}
	
	public static Date get_enddate(java.sql.Date date,int period) {
		long millis = (long) (date.getTime()+31556952000L*period);
		java.sql.Date end_date = new java.sql.Date(millis) ;
		return end_date;
	}
	
	public static Date get_duedate(java.sql.Date date) {
		long millis = (long) (date.getTime()+31l*24l*60l*60l*1000l);
		java.sql.Date due_date = new java.sql.Date(millis);
		return due_date;
	}
	
	public static int total_amount(int amount,int interest) {
		int totalamount = amount + (amount*interest)/100;
		return totalamount;
	}
	
}
