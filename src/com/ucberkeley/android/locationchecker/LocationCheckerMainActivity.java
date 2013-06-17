package com.ucberkeley.android.locationchecker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ucberkeley.android.locationchecker.R;
import com.ucberkeley.android.locationchecker.ActivityUtils.REQUEST_TYPE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LocationCheckerMainActivity extends Activity {
	
	// Store the current request type (ADD or REMOVE)
	private REQUEST_TYPE mRequestType;

	// The activity recognition update request object
	private DetectionRequester mDetectionRequester;

	// The activity recognition update removal object
	private DetectionRemover mDetectionRemover;

	/*
	 * Set main UI layout and create the broadcast
	 * receiver.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the main layout
		setContentView(R.layout.home_layout);

		// Get detection requester and remover objects
		mDetectionRequester = new DetectionRequester(this);
		mDetectionRemover = new DetectionRemover(this);
	}

	/*
	 * Handle results returned to this Activity by other Activities started with
	 * startActivityForResult(). In particular, the method onConnectionFailed() in
	 * DetectionRemover and DetectionRequester may call startResolutionForResult() to
	 * start an Activity that handles Google Play services problems. The result of this
	 * call returns here, to onActivityResult.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		// Choose what to do based on the request code
		switch (requestCode) {

		// If the request code matches the code sent in onConnectionFailed
		case ActivityUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

			switch (resultCode) {
			// If Google Play services resolved the problem
			case Activity.RESULT_OK:

				// If the request was to start activity recognition updates
				if (ActivityUtils.REQUEST_TYPE.ADD == mRequestType) {

					// Restart the process of requesting activity recognition updates
					mDetectionRequester.requestUpdates();

					// If the request was to remove activity recognition updates
				} else if (ActivityUtils.REQUEST_TYPE.REMOVE == mRequestType ){

					/*
					 * Restart the removal of all activity recognition updates for the 
					 * PendingIntent.
					 */
					mDetectionRemover.removeUpdates(
							mDetectionRequester.getRequestPendingIntent());

				}
				break;

				// If any other result was returned by Google Play services
			default:

				// Report that Google Play services was unable to resolve the problem.
				Log.d(ActivityUtils.APPTAG, getString(R.string.no_resolution));
			}

			// If any other request code was received
		default:
			// Report that this Activity received an unknown requestCode
			Log.d(ActivityUtils.APPTAG,
					getString(R.string.unknown_activity_request_code, requestCode));

			break;
		}
	}
	/**
	 * Verify that Google Play services is available before making a request.
	 *
	 * @return true if Google Play services is available, otherwise false
	 */
	private boolean servicesConnected() {

		// Check that Google Play services is available
		int resultCode =
				GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {

			// In debug mode, log the status
			Log.d(ActivityUtils.APPTAG, getString(R.string.play_services_available));

			// Continue
			return true;

			// Google Play services was not available for some reason
		} else {

			// Display an error dialog
			GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0).show();
			return false;
		}
	}

	/**
	 * Respond to "Start" button by requesting activity recognition
	 * updates.
	 * @param view The view that triggered this method.
	 */
	public void onStartActivityUpdates(View view) {

		// Check for Google Play services
		if (!servicesConnected()) {

			return;
		}

		/*
		 * Set the request type. If a connection error occurs, and Google Play services can
		 * handle it, then onActivityResult will use the request type to retry the request
		 */
		mRequestType = ActivityUtils.REQUEST_TYPE.ADD;

		// Pass the update request to the requester object
		mDetectionRequester.requestUpdates();
	}

	/**
	 * Respond to "Stop" button by canceling updates.
	 * @param view The view that triggered this method.
	 */
	public void onStopActivityUpdates(View view) {

		// Check for Google Play services
		if (!servicesConnected()) {

			return;
		}

		/*
		 * Set the request type. If a connection error occurs, and Google Play services can
		 * handle it, then onActivityResult will use the request type to retry the request
		 */
		mRequestType = ActivityUtils.REQUEST_TYPE.REMOVE;

		// Pass the remove request to the remover object
		mDetectionRemover.removeUpdates(mDetectionRequester.getRequestPendingIntent());

		/*
		 * Cancel the PendingIntent. Even if the removal request fails, canceling the PendingIntent
		 * will stop the updates.
		 */
		mDetectionRequester.getRequestPendingIntent().cancel();
	}
	
	public void onStartLocationUpdates(View view) {
		Intent service = new Intent(this, LocationUpdateService.class);
		this.startService(service); 
	}
	
	public void onStopLocationUpdates(View view) {
		Intent service = new Intent(this, LocationUpdateService.class);
		this.stopService(service);
	}

}
