package com.ucberkeley.android.locationchecker;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryLevelReceiver extends BroadcastReceiver {

	private DetectionRequester mDetectionRequester;
	private LocationUpdateRequester mLocationUpdateRequester;
	private DetectionRemover mDetectionRemover;
	private LocationUpdateRemover mLocationUpdateRemover;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		mDetectionRequester = new DetectionRequester(context);
		mLocationUpdateRequester = new LocationUpdateRequester(context);
		mDetectionRemover = new DetectionRemover(context);
		mLocationUpdateRemover = new LocationUpdateRemover(context);
		
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_BATTERY_LOW)) {
			mDetectionRemover.removeUpdates(mDetectionRequester.getRequestPendingIntent());
			mDetectionRequester.getRequestPendingIntent().cancel();
			mLocationUpdateRemover.removeUpdates(mLocationUpdateRequester.getRequestPendingIntent());
			mLocationUpdateRequester.getRequestPendingIntent().cancel();
		}
		else if (action.equals(Intent.ACTION_BATTERY_OKAY)) {
			if (isNowBetweenDayTime()) {
				mDetectionRequester.setUpdateTimeInterval(ActivityUtils.DAYTIME_DETECTION_INTERVAL_MILLISECONDS);
				mDetectionRequester.requestUpdates();
				mLocationUpdateRequester.setUpdateTimeInterval(LocationUtils.DAYTIME_UPDATE_INTERVAL_IN_MILLISECONDS);
				mLocationUpdateRequester.requestUpdates();
			}
			else {
				mDetectionRequester.setUpdateTimeInterval(ActivityUtils.NIGHTTIME_DETECTION_INTERVAL_MILLISECONDS);
				mDetectionRequester.requestUpdates();
				mLocationUpdateRequester.setUpdateTimeInterval(LocationUtils.NIGHTTIME_UPDATE_INTERVAL_IN_MILLISECONDS);
				mLocationUpdateRequester.requestUpdates();
			}
		}
	}
	
	// Check if the current time is in the day time
	private boolean isNowBetweenDayTime() {
		Calendar start = Calendar.getInstance(); 
		start.set(Calendar.HOUR, 7);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		
		Calendar end = Calendar.getInstance(); 
		end.set(Calendar.HOUR, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		
		Calendar curr = Calendar.getInstance();
		return curr.after(start) && curr.before(end);
	}
}
