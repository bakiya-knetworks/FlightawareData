package tfm.MissingFlights;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tfm.MissingFlights.FlightawareModel;



public class HtmlDecoder {

	private String airportCode = "";
	
	public ArrayList<FlightawareModel> getallFlightawaredata(String cDate){
		System.out.println("cDate ::: "+cDate);
		ArrayList<FlightawareModel> allflightaware = new ArrayList<FlightawareModel>();
		try{
			SimpleDateFormat processingdate = new SimpleDateFormat("yyyy-MM-dd");//2019-06-16-18
			SimpleDateFormat currentdayofmonth = new SimpleDateFormat("dd-MM-yyyy ");
			SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); 
			int offset=0;
			airportCode= PropertiesReader.getProperties().getProperty("airportcode");
			Date dt = processingdate.parse(cDate);
			Calendar c = Calendar.getInstance(); 
			c.setTime(dt); 
			c.add(Calendar.DATE, -1);
			String yesterDayofWeek = simpleDateformat.format(c.getTime());
			String yesterday = currentdayofmonth.format(c.getTime());
//			dt = c.getTime();
			c.setTime(dt); 
			c.add(Calendar.DATE, 0);
			Date currentDate = c.getTime();
			String currentDayofWeek = simpleDateformat.format(currentDate);
			
			String currentday= currentdayofmonth.format(currentDate);
			
			
			
			boolean isbreakEnabled = false;
			SimpleDateFormat fltTime = new SimpleDateFormat("dd-MM-yyyy E K:mm z");//Wed 11:31AM EDT
			System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
//			Document doc = Jsoup.parse(htmlContent);//https://flightaware.com/live/airport/KSBP/arrivals?;offset=20;order=actualarrivaltime;sort=DESC
			for(;;){//https://flightaware.com/live/airport/KPWM/arrivals?;offset=20;order=actualarrivaltime;sort=DESC
			//	System.out.println("https://flightaware.com/live/airport/"+airportCode+"/arrivals?offset="+offset+";order=actualarrivaltime;sort=DESC");
			//	String htmlContent = testIt(offset,airportCode);
		    
 			Document doc = Jsoup.connect("http://flightaware.com/live/airport/"+airportCode+"/arrivals?;offset="+offset+";order=actualarrivaltime;sort=DESC").get();
				//Document doc = Jsoup.parse(htmlContent);//https://flightaware.com/live/airport/KSBP/arrivals?;offset=20;order=actualarrivaltime;sort=DESC
	         Elements tableElements = doc.select("table.prettyTable");
	         for (int i = 0; i < tableElements.size(); i++) {
//	             System.out.println(tableElements.get(i).text());
	             Elements tableRowElements = tableElements.select(":not(thead) tr");

	             for (int j = 0; j < tableRowElements.size(); j++) {
	            	 Element row = tableRowElements.get(j);
//	                 System.out.println("row");
	                 Elements rowItems = row.select("td");
	                 try{
	                	 if(rowItems.size()<=0)continue;
	                	 System.out.println("rowItems.get(4).text");
	                	 System.out.println(rowItems.get(4).text());
	                	 if(rowItems.get(4).text().toUpperCase().contains(yesterDayofWeek.toUpperCase())){
	                		 isbreakEnabled = true;
	                		 break;
	                	 }
	                	 if(!rowItems.get(4).text().toUpperCase().contains(currentDayofWeek.toUpperCase())){
	                		 
	                		 continue;
	                	 }
	                	 FlightawareModel fmodel = new FlightawareModel();
	                	 fmodel.ident = rowItems.get(0).text();
	                	
	                	 fmodel.type = rowItems.get(1).text();
	                	 System.out.println("rowItems.get(2).text");
	                	 System.out.println(rowItems.get(2).text());

	                	 if( rowItems.get(2).text().contains("(")){
	                	 fmodel.origin = rowItems.get(2).text().substring(rowItems.get(2).text().indexOf("(") + 1, rowItems.get(2).text().indexOf(")"));;//s.substring(s.indexOf("(") + 1, s.indexOf(")"));
	                	 }else{
	                		 fmodel.origin = rowItems.get(2).text();
	                	 }
	                	  
	                	 fmodel.departuretime = rowItems.get(3).text();//Wed 11:31AM EDT
	                	
	                	 if(fmodel.departuretime.contains("?")){
	                		 fmodel.departuretime.replace("(?)", "");
	                		 fmodel.departuretime=currentDayofWeek+" "+fmodel.departuretime;
	                	 }
	                	 if(fmodel.departuretime.toUpperCase().contains(yesterDayofWeek.toUpperCase())){
	                		 fmodel.departuretime= yesterday +fmodel.departuretime;
	                	 }else{
	                		 fmodel.departuretime=currentday+fmodel.departuretime;
	                	 }

	                	 System.out.println(fmodel.departuretime);
	                	 Date dateObj = fltTime.parse( fmodel.departuretime );        //String to Date
	                	 fmodel.departuretimemilliseconds=dateObj.getTime();
	                	 
	                	 fmodel.arrivaltime = rowItems.get(4).text();
	                	 if(fmodel.arrivaltime.contains("?")){
	                		 fmodel.arrivaltime.replace("(?)", "");
	                		 fmodel.arrivaltime=currentDayofWeek+" "+fmodel.arrivaltime;
	                	 }
	                	 if(fmodel.arrivaltime.toUpperCase().contains(yesterDayofWeek.toUpperCase())){
	                		 fmodel.arrivaltime= yesterday +fmodel.arrivaltime;
	                	 }else{
	                		 fmodel.arrivaltime=currentday+fmodel.arrivaltime;
	                	 }	                	
	                	 dateObj = fltTime.parse(fmodel.arrivaltime);        //String to Date
	                	 fmodel.arrivaltimemilliseconds=dateObj.getTime();
	                	
	                	 if(fmodel.arrivaltime.toUpperCase().contains(yesterDayofWeek.toUpperCase())){
	                		 isbreakEnabled = true;
	                		 break;
	                	 }	                	 
	                	 allflightaware.add(fmodel);	                	
	                 }catch(Exception e){
	                	 e.printStackTrace();
	                 }
	                 
	             }
	             if( isbreakEnabled) break;
	            
	                 System.out.println("");
	          }
	         if( isbreakEnabled) break;
	         offset = offset + 20;
			}//for ; ; 
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return allflightaware;
	}
	
	 private  String testIt(int offSet, String airport){

	      String https_url = "https://flightaware.com/live/airport/"+airport+"/arrivals?;offset="+offSet+";order=actualarrivaltime;sort=DESC";
	      URL url;
	      try {

		     url = new URL(https_url);
		     HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
				
		     //dump all the content
		     return print_content(con);
				
	      } catch (MalformedURLException e) {
		     e.printStackTrace();
	      } catch (IOException e) {
		     e.printStackTrace();
	      }
	      return null;
	   }
	 private String print_content(HttpsURLConnection con){
			if(con!=null){
				 String htmlContent = "";	
			try {
				
			   System.out.println("****** Content of the URL ********");			
			   BufferedReader br = 
				new BufferedReader(
					new InputStreamReader(con.getInputStream()));
						
			   String input;
			  
			   while ((input = br.readLine()) != null){
				   htmlContent += input;
//			      System.out.println(input);
			   }
			   br.close();
					
			   
			} catch (IOException e) {
			   e.printStackTrace();
			}finally{
				return htmlContent;
			}
					
		       }
			return null;
				
		   }
}
