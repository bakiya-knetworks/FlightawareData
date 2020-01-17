package tfm.MissingFlights;
 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Constants {
    public static final String AIRPORT_TABLE_NAME = "flights";
    public static long getTimeInMillis(String dateTime, String pattern){
        try {//yyyy-MM-dd'T'HH:mm:ss'Z'
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat.parse(dateTime).getTime();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTimeInReadbleFormat(long dateTime, String pattern, String timeZone){
        try {//yyyy-MM-dd'T'HH:mm:ss'Z'
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
            return simpleDateFormat.format(new Date(dateTime));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private static final String mongoDBHost = "172.16.1.94";
    private static final String mongoDBPort = "27017";
    private static final String mongoDBUserName = "tfmdataserver";
    private static final String mongoDBPassword = "9jrk4d1!";
    private static final String mongoDBName ="tfm_data";

    public static String getMongoDBHost() {
        return mongoDBHost;
    }

    public static String getMongoDBUserName() {
        return mongoDBUserName;
    }

    public static String getMongoDBPassword() {
        return mongoDBPassword;
    }

    public static String getMongoDBName() {
        return mongoDBName;
    }

    public static String getMongoDBPort() {
        return mongoDBPort;
    }
}
