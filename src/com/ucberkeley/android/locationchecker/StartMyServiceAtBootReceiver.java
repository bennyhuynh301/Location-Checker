package com.ucberkeley.android.locationchecker;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			if (isNowBetweenDayTime()) {
				Intent newIntent = new Intent(context, LocationUpdateService.class);
				newIntent.putExtra("UPDATE_INTERVAL", LocationUtils.DAYTIME_UPDATE_INTERVAL_IN_MILLISECONDS);
				context.startService(newIntent);
			}
			else {
				Intent newIntent = new Intent(context, LocationUpdateService.class);
				newIntent.putExtra("UPDATE_INTERVAL", LocationUtils.NIGHTTIME_UPDATE_INTERVAL_IN_MILLISECONDS);
				context.startService(newIntent);
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
