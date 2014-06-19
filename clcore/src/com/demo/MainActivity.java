package com.demo;

import android.app.Activity;
import android.os.Bundle;

import com.cl.clservice.CL;
import com.demopush_pri.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CL.enableCLService(MainActivity.this, "");
	}
}
