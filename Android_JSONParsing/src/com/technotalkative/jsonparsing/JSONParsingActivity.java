package com.technotalkative.jsonparsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class JSONParsingActivity extends Activity {
    /** Called when the activity is first created. */
	
//	TextView txtViewParsedValue;
	
	TextView nameTextView;
	TextView ratingTextView;
	TextView locationTexView;
	private JSONObject jsonObject;
	
	String strParsedValue = null;
	
	private String strJSONValue=null;
//	private String strJSONValue = "{\"FirstObject\":{\"attr1\":\"one value\" ,\"attr2\":\"two value\","
//			+"\"sub\": { \"sub1\":[ {\"sub1_attr\":\"sub1_attr_value\" },{\"sub1_attr\":\"sub2_attr_value\" }]}}}";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        nameTextView = (TextView) findViewById(R.id.textView1);
        ratingTextView = (TextView) findViewById(R.id.textView2);
        locationTexView=(TextView) findViewById(R.id.textView3);
        
        	
        GPSTracker gpsTracker=new GPSTracker(this);
        String latitude=null;
        String longitude=null;
        if(gpsTracker.canGetLocation())
        {
        	latitude=String.valueOf(gpsTracker.getlatitude());
        	longitude=String.valueOf(gpsTracker.getLongitude());
        	
        }
//			parseJSON();
        	JSONObject json=getJSONFromUrl("https://maps.googleapis.com/maps/api/place/search/json?location=-33.8670522,151.1957362&radius=500&types=food&name=harbour&sensor=false&key=AIzaSyB0KlOrROpVl6ssjqKMxSIaJrDGBCRxdo4");
        	try {
				JSONArray jsonData=json.getJSONArray("results");
				JSONObject firstResult=jsonData.getJSONObject(0);
				String name=firstResult.getString("name");
				String rating=firstResult.getString("rating");
				String latitude1= firstResult.getJSONObject("geometry").getJSONObject("location").getString("lat");
				String longitude1=firstResult.getJSONObject("geometry").getJSONObject("location").getString("lng");
				
				nameTextView.setText(name);
				ratingTextView.setText(rating);
				locationTexView.setText("Co-ordinates of "+name+"are "+latitude1+","+longitude1);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	
//        	txtViewParsedValue.setText("Latitude: "+latitude+"Longitude: "+longitude);
			
    }
	
	public JSONObject getJSONFromUrl(String url){
		InputStream is=null;
		String json=null;
		JSONObject jObj=null;
		try{
			DefaultHttpClient httpClient=new DefaultHttpClient();
			HttpPost httpPost=new HttpPost(url);
			HttpResponse httpResponse=httpClient.execute(httpPost);
			HttpEntity httpEntity=httpResponse.getEntity();
			is=httpEntity.getContent();
		}
		catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		catch(ClientProtocolException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		try{
			BufferedReader reader=new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
			StringBuilder sb=new StringBuilder();
			String line=null;
			while((line=reader.readLine())!=null){
				sb.append(line+"\n");
			}
			is.close();
			json=sb.toString();
		}
		catch(Exception e){
			e.printStackTrace();
		}
				
		try{
			jObj=new JSONObject(json);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		
		return jObj;
		
	}
	
	
	
	
	
	
	
/*    public void parseJSON() throws JSONException
    {
    	jsonObject = new JSONObject(strJSONValue);
    	
    	JSONObject object = jsonObject.getJSONObject("FirstObject");
    	String attr1 = object.getString("attr1");
    	String attr2 = object.getString("attr2");
    	
    	strParsedValue="Attribute 1Changed value => "+attr1;
    	strParsedValue+="\n Attribute 2 value => "+attr2;
    	
    	JSONObject subObject = object.getJSONObject("sub");
    	JSONArray subArray = subObject.getJSONArray("sub1");

    	strParsedValue+="\n Array Length => "+subArray.length();
    	
    	for(int i=0; i<subArray.length(); i++)
    	{
    		strParsedValue+="\n"+subArray.getJSONObject(i).getString("sub1_attr").toString();
    	}
    	
    	txtViewParsedValue.setText(strParsedValue);
    }*/
}


// Actual JSON Value
/*
{"FirstObject": { "attr1":"one value" ,"attr2":"two value",
	
   "sub": { "sub1":[ {"sub1_attr":"sub1_attr_value" },{"sub1_attr":"sub2_attr_value" }]}
  }
"}; */

// Same JSON value in XML
/*
<FirstObject obj1="Object 1 value" obj2="Object 2 value">
	<sub>
	    <sub1 sub1_attr="sub1_attr_value" />
	    <sub1 sub1_attr="sub2_attr_value" />
	</sub>
</FirstObject> */
