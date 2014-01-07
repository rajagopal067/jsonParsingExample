package com.technotalkative.jsonparsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class JSONParsingActivity extends Activity {
    /** Called when the activity is first created. */
	
//	TextView txtViewParsedValue;
	ListView list;
	TextView nameTextView;
	TextView ratingTextView;
	TextView locationTexView;
	Button btnGetData;
	JSONArray android=null;
	
	private JSONObject json;
	
	ArrayList<HashMap<String,String>> resultList=new ArrayList<HashMap<String,String>>();
	
	String strParsedValue = null;
	
	private String strJSONValue=null;
//	private String strJSONValue = "{\"FirstObject\":{\"attr1\":\"one value\" ,\"attr2\":\"two value\","
//			+"\"sub\": { \"sub1\":[ {\"sub1_attr\":\"sub1_attr_value\" },{\"sub1_attr\":\"sub2_attr_value\" }]}}}";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        list=(ListView) findViewById(R.id.list);
        nameTextView = (TextView) findViewById(R.id.textView1);
        ratingTextView = (TextView) findViewById(R.id.textView2);
        locationTexView=(TextView) findViewById(R.id.textView3);
        btnGetData=(Button) findViewById(R.id.getdata);
        
        btnGetData.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new JSONParse().execute();
			}
		});
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
//				String name=firstResult.getString("name");
//				String rating=firstResult.getString("rating");
//				String latitude1= firstResult.getJSONObject("geometry").getJSONObject("location").getString("lat");
//				String longitude1=firstResult.getJSONObject("geometry").getJSONObject("location").getString("lng");
				
//				nameTextView.setText(name);
//				ratingTextView.setText(rating);
//				locationTexView.setText("Co-ordinates of "+name+"are "+latitude1+","+longitude1);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	
//        	txtViewParsedValue.setText("Latitude: "+latitude+"Longitude: "+longitude);
			
    }
	
	private class JSONParse extends AsyncTask<String, String, JSONObject>{
		
		private ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog=new ProgressDialog(JSONParsingActivity.this);
			pDialog.setMessage("Getting Data");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			JSONParser jParser=new JSONParser();
			JSONObject json=jParser.getJSONFromUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=17.482982,78.367431&radius=5000&types=food&sensor=false&key=AIzaSyB0KlOrROpVl6ssjqKMxSIaJrDGBCRxdo4");
//			Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_SHORT).show();
			return json;
			
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			pDialog.dismiss();
			// TODO Auto-generated method stub
			try{
				android=json.getJSONArray("results");
				for(int i=0;i<android.length();i++){
					JSONObject c=android.getJSONObject(i);
					String name=c.getString("name");
					String rating=c.getString("rating");
					String latitude1= c.getJSONObject("geometry").getJSONObject("location").getString("lat");
					String longitude1=c.getJSONObject("geometry").getJSONObject("location").getString("lng");
					
					HashMap<String, String> dataList=new HashMap<String, String>();
					dataList.put("name", name);
					dataList.put("rating", rating);
					dataList.put("location", latitude1+","+longitude1);
					resultList.add(dataList);
					
					list=(ListView) findViewById(R.id.list);
					ListAdapter adapter=new SimpleAdapter(JSONParsingActivity.this, resultList, R.layout.list_v, 
							new String[]{"name","rating","location"}, new int[]{R.id.textView1,R.id.textView2,R.id.textView3});
					
					list.setAdapter(adapter);
				}
			}
				catch(JSONException e){
					e.printStackTrace();
				}
			}
			
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
