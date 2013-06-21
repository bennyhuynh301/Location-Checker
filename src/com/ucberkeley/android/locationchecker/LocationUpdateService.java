package com.ucberkeley.android.locationchecker;

import java.io.File;
import java.io.FileWriter;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationUpdateService extends Service implements LocationListener,
											GooglePlayServicesClient.ConnectionCallbacks,
											GooglePlayServicesClient.OnConnectionFailedListener  {

	private static final String TAG = "LocationUpdateService";
	
	// A request to connect to Location Services
	private LocationRequest mLocationRequest;

	// Stores the current instantiation of the location client in this object
	private LocationClient mLocationClient;
	
	// Update time interval
	private long timeInterval = 0;
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			timeInterval = (Long) bundle.get("UPDATE_INTERVAL"); 
		}
		else {
			timeInterval = LocationUtils.DAYTIME_UPDATE_INTERVAL_IN_MILLISECONDS;
		}
	    
	    return Service.START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// Create a new global location parameters object
		mLocationRequest = LocationRequest.create();
		
		/*
		 * Set the update interval
		 */
		mLocationRequest.setInterval(timeInterval);

		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		// Set the interval ceiling to one minute
		mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		/*
		 * Create a new location client, using the enclosing class to
		 * handle callbacks.
		 */
		mLocationClient = new LocationClient(this, this, this);
		mLocationClient.connect();
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.d(TAG, "onConnectionFailed");
		mLocationClient.connect();
	}

	@Override
	public void onConnected(Bundle arg0) {
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	@Override
	public void onDisconnected() {

	}

	@Override
	public void onLocationChanged(Location location) {
		StringBuilder sb = new StringBuilder("{location:{");
		sb.append("time:").append(location.getTime()).append(",");
		sb.append("alt:").append(location.getAltitude()).append(",");
		sb.append("lat:").append(location.getLatitude()).append(",");
		sb.append("long:").append(location.getLongitude()).append(",");
		sb.append("speed:").append(location.getSpeed()).append(",");
		sb.append("error:").append(location.getAccuracy());
		sb.append("}}");
		
		String log = sb.toString();
		Log.d(TAG, "Location: " + log);
		File f = new File(Environment.getExternalStorageDirectory(),"tracker.txt");
		try{ 
			if (!f.exists()) {
				f.createNewFile();
			}
			FileWriter out = new FileWriter(f,true);
			out.write(log + "\n");
			out.close();
		}catch (Exception e) {
			return;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	
	/*
	 * Called when the Activity is no longer visible at all.
	 * Stop updates and disconnect.
	 */
	@Override
	public void onDestroy() {
		// If the client is connected
		if (mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(this);
		}

		// After disconnect() is called, the client is considered "dead".
		mLocationClient.disconnect();
		super.onDestroy();
	}
}
