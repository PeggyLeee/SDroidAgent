package com.ncu.sdroidagent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
/**
 * Lock app���e��
 * */
public class LockScreenActivity extends Activity {

	private Context contex;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contex = this;
		
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("�еy��");
		alertDialog.setMessage(getIntent().getStringExtra("msg"));
		alertDialog.setButton("�T�w", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
		        Intent intent = new Intent(contex, MainActivity.class);
		        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 

		        contex.startActivity(intent);
			}
		});
		alertDialog.show();
	}

}
