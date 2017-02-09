package com.ncu.sdroidagent;

import static com.ncu.sdroidagent.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.ncu.sdroidagent.CommonUtilities.EXTRA_MESSAGE;
import static com.ncu.sdroidagent.CommonUtilities.SENDER_ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.android.gcm.GCMRegistrar;
import com.ncu.sdroidagent.R;
import com.ncu.sdroidagent.ServerUtilities;

import Model.AppInfo;
import Util.LockAppPreference;
import Util.UserEmailFetcher;
import android.support.v7.app.ActionBarActivity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 主畫面
 * */
public class MainActivity extends ActionBarActivity {

	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	public static String name;
	public static String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//註冊Google Cloud Message(GCM)
		deviceGCMRegister();

		Intent sIntent = new Intent(this, IntentService.class);
		startService(sIntent);

		// 發送Broadcast來進行Lock App
		getBaseContext().getApplicationContext().sendBroadcast(
				new Intent("StartupReceiver_Manual_Start"));

		// 取的目前 Lock& UnLock App清單
		getAndShowAppList();

		showToast("start SDroidAgent!");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

		// 取的目前 Lock& UnLock App清單
		getAndShowAppList();
	}

	public void getAndShowAppList() {
		Log.i("MainActivity", "Get Lock& Unlock App List");
		LockAppPreference pref = new LockAppPreference(this);
		// pref.clear();
		Set<String> lockSet = pref.getLockedApps();

		List<AppInfo> appList = new ArrayList<AppInfo>();

		PackageManager pm = getPackageManager();
		List<PackageInfo> pklist = pm.getInstalledPackages(0);

		for (PackageInfo pkInfo : pklist) {
			String pkName = pkInfo.packageName;
			if (lockSet.contains(pkName)) {
				AppInfo app = new AppInfo();
				app.setAppInfo(pkInfo.applicationInfo);
				app.setPkgName(pkName);
				if (pref.getLock(pkName)) {
					app.setResultIcon(R.drawable.lock);
				} else {
					app.setResultIcon(R.drawable.unlock);
				}

				appList.add(app);
			}
		}
		Log.i("MainActivity", "Show App List");
		if (appList.size() > 0) {
			Log.d("MainActivity", "appList.size: " + appList.size());
			ListView apkList = (ListView) findViewById(R.id.listView1);
			apkList.setAdapter(new ApkAdapter(this, appList, pm));
		}
	}

	public void showToast(String msg) {
		// 顯示訊息
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		toast.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Google Cloud Messaging function
	private void deviceGCMRegister() {
		// Getting name, email from intent
		UserEmailFetcher uef = new UserEmailFetcher();
		
		email = uef.getEmail(getApplicationContext());
		String[] parts = email.split("@");
		name = parts[0];
		
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, SENDER_ID);
		} 
		else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				Toast.makeText(getApplicationContext(),
						"Already registered with GCM", Toast.LENGTH_LONG)
						.show();
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context, name, email, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
//			 // Waking up mobile if it is sleeping
//			 WakeLocker.acquire(getApplicationContext());

			if(newMessage != null){
				Toast.makeText(getApplicationContext(),
						"GCM Message: " + newMessage, Toast.LENGTH_LONG).show();
			}
			// // Releasing wake lock
			// WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
	
	
}
