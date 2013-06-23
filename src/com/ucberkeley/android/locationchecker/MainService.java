package com.ucberkeley.android.locationchecker;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MainService extends Service {
	private static final String TAG = "MAINSERVICE";
	
	@Override
	public int onStartCommand(Intent intent, int flag, int startId) {
		return Service.START_STICKY;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "Start main service");
		
		Calendar onDayTime = Calendar.getInstance();
		onDayTime.set(Calendar.HOUR_OF_DAY, 7);
		onDayTime.set(Calendar.MINUTE, 0);
		onDayTime.set(Calendar.SECOND, 0);
		
		Calendar onNightTime = Calendar.getInstance();
		onNightTime.set(Calendar.HOUR_OF_DAY, 23);
		onNightTime.set(Calendar.MINUTE, 59);
		onNightTime.set(Calendar.SECOND, 59);
		
		Intent dayStartIntent = new Intent(this, AlarmReceiver.class);
		dayStartIntent.putExtra("ALARM_TYPE", "DAY_START");
		PendingIntent daySender = PendingIntent.getBroadcast(this,0,dayStartIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, onDayTime.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY, daySender);

		Intent nightStartIntent = new Intent(this, AlarmReceiver.class);
		nightStartIntent.putExtra("ALARM_TYPE", "NIGHT_START");
		PendingIntent nightSender = PendingIntent.getBroadcast(this,1,nightStartIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		am.setRepeating(AlarmManager.RTC_WAKEUP, onNightTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, nightSender);
	}
	
	public void onDestroy() {
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
