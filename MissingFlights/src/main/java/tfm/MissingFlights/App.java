package tfm.MissingFlights;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection; 
import com.mongodb.client.MongoDatabase;

import tfm.MissingFlights.FlightsDAO;

import org.bson.Document;
 

import tfm.MissingFlights.Constants;
import com.mongodb.MongoClient; 
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;


/**
 * Hello world!
 *
 */
public class App 
{
	   private static final String mongoDBHost = "172.16.1.94";
	    private static final String mongoDBPort = "27017";
	    private static final String mongoDBUserName = "tfmdataserver";
	    private static final String mongoDBPassword = "9jrk4d1!";
	    private static final String mongoDBName ="tfm_data";
	    private static MongoClient mongoClient;
	    static ArrayList<FlightawareModel> flightawareFlights = new ArrayList<FlightawareModel>();
	    static ArrayList<String> tfmFlightsArr = new ArrayList<String>();
	    static FlightsDAO dataAccess = new FlightsDAO();
	    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        static Date date = new Date();  
        static String dateStr = formatter.format(date).toString();
        static String airportCode = "";
    public static void main( String[] args )
    {
    	airportCode= PropertiesReader.getProperties().getProperty("airportcode");
    	//dateStr = "2020-01-06";
    	//init();
        System.out.println( "Hello World!" );
        HtmlDecoder htmldecoder = new HtmlDecoder();
        
        //System.out.println(formatter.format(date));          
        //System.out.println( "dateStr::"+dateStr);  
        dateStr = "2020-01-10";
         flightawareFlights = htmldecoder.getallFlightawaredata(dateStr);
         for (FlightawareModel f : flightawareFlights) {
				 try {
					//  dataAccess.Insert_FlightAwareData(f,airportCode);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
         }
       //  getDataFromMongoDB(flightawareFlights);
         
		// PrintData();
		// String url="https://flightaware.com/live/airport/KPWM/arrivals?offset= 0;order=actualarrivaltime;sort=DESC";
		//  String output  = htmldecoder.getUrlContents(url);
		//  System.out.println(output);
        // CompareData();
         
    }
     
    public static void PrintData(){
		  
		try {
			for (FlightawareModel f : flightawareFlights) {
				//dataAccess.InsertData(f);
			//	public String ident, type, origin, departuretime, arrivaltime;
			//	public long departuretimemilliseconds, arrivaltimemilliseconds;
				System.out.println( "flight aware data");
				 System.out.println( f.arrivaltime);
				 System.out.println( f.type);
				 System.out.println( f.ident);
				 System.out.println( f.origin);
				 System.out.println( f.departuretime);
				 System.out.println( f.arrivaltime);
				 
				 System.out.println( f.departuretimemilliseconds);
				 System.out.println( f.arrivaltimemilliseconds);
			}
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
    public static void init(){
        List<ServerAddress> seeds = new ArrayList<>();
        seeds.add( new ServerAddress(Constants.getMongoDBHost(), Integer.valueOf(Constants.getMongoDBPort())));

        List<MongoCredential> credentials = new ArrayList<>();
        if(Constants.getMongoDBUserName() != null && !Constants.getMongoDBUserName().trim().isEmpty()){
            credentials.add(MongoCredential.createCredential(Constants.getMongoDBUserName(),Constants.getMongoDBName(),Constants.getMongoDBPassword().toCharArray()));
        }


        if(credentials.size() > 0){
              mongoClient = new MongoClient(seeds,credentials);
              System.out.println("Connection successful");
        }else{
              mongoClient = new MongoClient(seeds);
           
        }
       
    }
    public static void getDataFromMongoDB( ArrayList<FlightawareModel> faFlights){
    	  // Accessing the database 
        MongoDatabase database = mongoClient.getDatabase(mongoDBName);  
        
        // Retrieving a collection 
        MongoCollection<Document> collection = database.getCollection(dateStr);//"2019-12-23"
        System.out.println("Collection selected successfully"); 

        // Getting the iterable object 
        
        FindIterable<Document> iterDoc = collection.find(); 
        int i = 1; 

        // Getting the iterator 
        Iterator it = iterDoc.iterator(); 
      
        while (it.hasNext()) {  
        	// System.out.println(it.next());  
        	//TfmModel objTfm = new TfmModel();
        	String acid = ProcessDoc(it.next().toString()); //(TfmModel);
        	tfmFlightsArr.add(acid);
          
          
        i++; 
        }
        
    }
    public static String ProcessDoc(String doc){
    	String acid = "";
    	// Document{{_id=5e133687b0c0d183e94ec7fc, acid=JBU609, airline=JBU, flightRef=123705105, arrArpt=DSD, depArpt=JFK, msgType=FlightModify, sourceTimeStamp=2020-01-05T05:05:41Z, diversionIndicator=NO_DIVERSION, actualArrTime=2020-01-04T08:23:24Z, actualDepTime=2020-01-05T05:06:00Z, eta=Document{{etaType=SCHEDULED, timeValue=2020-01-06T08:38:20Z}}, etd=Document{{etdType=SCHEDULED, timeValue=2020-01-06T05:14:00Z}}, etaTimeInMillis=1578299900000, acType=A321, etdTimeInMillis=1578287640000}}
    	String[] strArr = doc.split(",");
    	for(int i=0; i<strArr.length ; i++){
    		if(strArr[i].split("=")[0].equals("acid")){
    			acid = strArr[i].split("=")[1];break;
    		}
    	}
    	return acid;
    }
    public static void CompareData(){
  	  int count = 0;int count1 = 0;
  		try {
  		  boolean isExists = false; 
	  		for (FlightawareModel f : flightawareFlights) {
	  			for (String str : tfmFlightsArr) {  	
	  				System.out.println("flight : " + f.ident);
	  				System.out.println("tfm  : " + str);
		  				if(str.equals(f.ident)){	
		  					isExists = true;break;
		  				}
	  			}
				if(!isExists){ // If flightaware data is missing then insert to DB.  	  				
				 	//dataAccess.InsertData(f);
					count1++;System.out.println(count1); 
	  			}else{
	  				System.out.println(f.ident); count++;
	  			}
	  		}
	  		System.out.println(count);
	  		System.out.println(count1); 
  		} catch (Exception e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
    	
    }
    
}
