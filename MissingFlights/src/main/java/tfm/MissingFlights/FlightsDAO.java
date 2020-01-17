package tfm.MissingFlights;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import tfm.MissingFlights.DaoConnectionFactory;
import tfm.MissingFlights.FlightawareModel;

public class FlightsDAO {
	
	 
	public String InsertData(FlightawareModel data) throws Exception {		 
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;		 
		String result = "failure";
		try {
			c = DaoConnectionFactory.INSTANCE.getConnection();
			st = c.createStatement();
		  
			String query = "{CALL InsertFlights(?,?,?,?,?,?,?,?,? )}";
			  
			java.sql.CallableStatement stmt = c.prepareCall(query);
			
			 stmt.setString(1, data.ident);
			stmt.setString(2, data.origin);
			stmt.setString(3, data.dest);
			stmt.setString(4, "NO_DIVERSION");			 
			stmt.setString(5,  data.arrivaltime);
			stmt.setString(6, data.departuretime);
			stmt.setDouble(7, data.arrivaltimemilliseconds) ;
			stmt.setDouble(8,  data.departuretimemilliseconds) ;			
			stmt.setString(9, data.type);
			 
			rs = stmt.executeQuery();
			 
			result = "success";
			return result;
		} catch (Exception e) {
			System.out.println("Exception in dao - InsertNoise()" + e.getMessage());
			throw e;			
		} finally {		
			closeConnections(rs,st);
		}
	}
	public String Insert_FlightAwareData(FlightawareModel data, String airportCode) throws Exception {		 
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;		 
		String result = "failure";
		try {
			c = DaoConnectionFactory.INSTANCE.getConnection();
			st = c.createStatement();
		  
			String query = "{CALL Insert_FlightAwareData(?,?,?,?,?,?,?,?)}";
			  
			java.sql.CallableStatement stmt = c.prepareCall(query);
			
			stmt.setString(1, data.ident);
			stmt.setString(2, data.origin);
			stmt.setString(3, airportCode);
			stmt.setString(4, data.arrivaltime);			 
			stmt.setString(5,  data.departuretime);
			stmt.setDouble(6, data.arrivaltimemilliseconds);
			stmt.setDouble(7, data.departuretimemilliseconds) ;
			stmt.setString(8,  data.type) ;			
			 			 
			rs = stmt.executeQuery();
			 
			result = "success";
			return result;
		} catch (Exception e) {
			System.out.println("Exception in dao - InsertNoise()" + e.getMessage());
			throw e;			
		} finally {		
			closeConnections(rs,st);
		}
	}
	private void closeConnections(ResultSet rs, Statement st) {
		try{
		if(rs!=null)
			rs.close();
		if(st!=null)
			st.close();
		}catch(Exception e){
			
		}
	}
	 
}
