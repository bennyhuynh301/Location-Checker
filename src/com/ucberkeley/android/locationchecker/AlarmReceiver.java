package com.ucberkeley.android.locationchecker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	private static final String TAG = "ALARMRECEIVER";

	private DetectionRequester mDetectionRequester;
	private LocationUpdateRequester mLocationUpdateRequester;

	@Override
	public void onReceive(Context context, Intent intent) {
		mDetectionRequester = new DetectionRequester(context);
		mLocationUpdateRequester = new LocationUpdateRequester(context);

		try {
			Bundle bundle = intent.getExtras();
			String message = bundle.getString("ALARM_TYPE");
			if (servicesConnected(context)) {
				if (message.equals("DAY_START")) {
					mDetectionRequester.setUpdateTimeInterval(ActivityUtils.DAYTIME_DETECTION_INTERVAL_MILLISECONDS);
					mDetectionRequester.requestUpdates();
					mLocationUpdateRequester.setUpdateTimeInterval(LocationUtils.DAYTIME_UPDATE_INTERVAL_IN_MILLISECONDS);
					mLocationUpdateRequester.requestUpdates();
				}
				else if (message.equals("NIGHT_START")) {
					mDetectionRequester.setUpdateTimeInterval(ActivityUtils.NIGHTTIME_DETECTION_INTERVAL_MILLISECONDS);
					mDetectionRequester.requestUpdates();
					mLocationUpdateRequester.setUpdateTimeInterval(LocationUtils.NIGHTTIME_UPDATE_INTERVAL_IN_MILLISECONDS);
					mLocationUpdateRequester.requestUpdates();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean servicesConnected(Context context) {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if (ConnectionResult.SUCCESS == resultCode) {
			Log.d(TAG, "Google Play Service is available");
			return true;
		} else {
			Log.e(TAG, "Google Play Service is not available: " + resultCode);
			return false;
		}
	}
}
