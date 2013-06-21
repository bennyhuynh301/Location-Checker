package com.ucberkeley.android.locationchecker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			String message = bundle.getString("ALARM_TYPE");
			Intent mIntent = new Intent(context, LocationUpdateService.class);
			if (message.equals("DAY_START")) {
				context.stopService(mIntent);
				Intent newIntent = new Intent(context, LocationUpdateService.class);
				newIntent.putExtra("UPDATE_INTERVAL", LocationUtils.DAYTIME_UPDATE_INTERVAL_IN_MILLISECONDS);
				context.startService(newIntent);
			}
			else if (message.equals("NIGHT_START")) {
				context.stopService(mIntent);
				Intent newIntent = new Intent(context, LocationUpdateService.class);
				newIntent.putExtra("UPDATE_INTERVAL", LocationUtils.NIGHTTIME_UPDATE_INTERVAL_IN_MILLISECONDS);
				context.startService(newIntent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
