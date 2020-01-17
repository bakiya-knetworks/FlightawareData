package tfm.MissingFlights;

public class TfmModel {
	public String _id,acid, airline, flightRef, arrArpt, depArpt,msgType,sourceTimeStamp,
	diversionIndicator,actualArrTime,actualDepTime,etaTimeInMillis,acType,etdTimeInMillis;
	public eta _eta;
	public etd _etd;
			 
}
class eta {
	public String  etaType,timeValue;
			 
}
class etd {
	public String  etaType,timeValue;
			 
}