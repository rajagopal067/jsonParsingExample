package com.technotalkative.jsonparsing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public class GPSTracker implements LocationListener {

	private final Context context;
	
	boolean isGPSEnabled=false;
	boolean isNetworkEnabled=false;
	boolean canGetLocation=false;
	
	Location location;
	double latitude;
	double longitude;
	
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES=10;
	private static final long MIN_TIME_BW_UPDATES=1000*60*2;
	
	LocationManager locationManager;
	
	public GPSTracker(Context context){
		this.context=context;
		getLocation();
	}
	
	public Location getLocation(){
		try{
			locationManager=(LocationManager) context.getSystemService(context.LOCATION_SERVICE);
			
			isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if(!isGPSEnabled && !isNetworkEnabled){
				
			}
			else{
				this.canGetLocation=true;
				
				if(isNetworkEnabled){
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network is present");
					
					if(locationManager!=null){
						location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						
					}
					
				if(isGPSEnabled){
					if(location==null){
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						
					}
				}
					
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return location;
	}
	
	public double getlatitude()
	{
		if(location!=null){
			latitude=location.getLatitude();
		}
		return latitude;
	}
	
	public double getLongitude(){
		if(location!=null){
			longitude=location.getLongitude();
		}
		return longitude;
	}
	
	public boolean canGetLocation(){
		return this.canGetLocation;
	}
	public void showSettingsAlert(){
		
		AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
		alertDialog.setTitle("Oops:");
		alertDialog.setMessage("Something went wrong:");
		
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(intent);
			}
		});
		
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		
		alertDialog.show();
		
		
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}
