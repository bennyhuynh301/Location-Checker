package com.ucberkeley.android.locationchecker;

/**
 * Defines app-wide constants and utilities
 */
public final class LocationUtils {

    // Debugging tag for the application
    public static final String APPTAG = "LocationSample";

    // Name of shared preferences repository that stores persistent state
    public static final String SHARED_PREFERENCES =
            "com.example.android.location.SHARED_PREFERENCES";

    // Key for storing the "updates requested" flag in shared preferences
    public static final String KEY_UPDATES_REQUESTED =
            "com.example.android.location.KEY_UPDATES_REQUESTED";

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /*
     * Constants for location update parameters
     */
    // Milliseconds per second
    public static final int MILLISECONDS_PER_SECOND = 1000;

    // The update interval
    public static final int DAYTIME_UPDATE_INTERVAL_IN_SECONDS = 30;
    public static final int NIGHTTIME_UPDATE_INTERVAL_IN_SECONDS = 1200;

    // A fast interval ceiling
    public static final int FAST_CEILING_IN_SECONDS = 1;

    // Update interval in milliseconds
    public static final long DAYTIME_UPDATE_INTERVAL_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * DAYTIME_UPDATE_INTERVAL_IN_SECONDS;
    public static final long NIGHTTIME_UPDATE_INTERVAL_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * NIGHTTIME_UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;

}