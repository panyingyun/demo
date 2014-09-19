package com.demo;

import android.app.Activity;
import android.os.Bundle;

import com.demopush_k.R;
import com.kl.klservice.KL;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		KL.enableKLService(MainActivity.this, "");
	}
}
