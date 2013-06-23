package com.ucberkeley.android.locationchecker;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public class ActivityRecognitionIntentService extends IntentService {
	
	private static final String TAG = "ActivityRecognitionIntentService";
	
	public ActivityRecognitionIntentService() {
		super("ActivityRecognitionIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (ActivityRecognitionResult.hasResult(intent)) {
			ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
			logActivityRecognitionResult(result);
		}
	}

	private void logActivityRecognitionResult(ActivityRecognitionResult result) {
		long timeStamp = (new Date()).getTime();
		StringBuilder sb = new StringBuilder("{activity:{");
		sb.append("time:").append(timeStamp).append(",");
		
		for (DetectedActivity detectedActivity : result.getProbableActivities()) {

			int activityType = detectedActivity.getType();
			int confidence = detectedActivity.getConfidence();
			String activityName = getNameFromType(activityType);

			String actProb = activityName + ":" + confidence;   
			sb.append(actProb).append(",");
		}
		sb.append("}}");
		
		String log = sb.toString();
		Log.d(TAG, "Location: " + log);
		File f = new File(Environment.getExternalStorageDirectory(),"tracker.txt");
		try{ 
			if (!f.exists()) {
				f.createNewFile();
			}
			FileWriter out = new FileWriter(f,true);
			out.write(log +"\n");
			out.close();
		} catch (Exception e) {
			return;
		}
	}
 
	private String getNameFromType(int activityType) {
		switch(activityType) {
		case DetectedActivity.IN_VEHICLE:
			return "in_vehicle";
		case DetectedActivity.ON_BICYCLE:
			return "on_bicycle";
		case DetectedActivity.ON_FOOT:
			return "on_foot";
		case DetectedActivity.STILL:
			return "still";
		case DetectedActivity.UNKNOWN:
			return "unknown";
		case DetectedActivity.TILTING:
			return "tilting";
		}
		return "unknown";
	}
}
