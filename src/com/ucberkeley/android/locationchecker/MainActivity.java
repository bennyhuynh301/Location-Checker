package com.ucberkeley.android.locationchecker;

import com.ucberkeley.android.locationchecker.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
	
	}
	
	public void onStartService(View view) {
		Intent service = new Intent(this, MainService.class);
		this.startService(service); 
		Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();
	}

	public void onStopService(View view) {
		Intent service = new Intent(this, MainService.class);
		this.stopService(service);
		Toast.makeText(this, "Stop Service", Toast.LENGTH_SHORT).show();
	}
}
