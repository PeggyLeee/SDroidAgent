package com.ncu.sdroidagent;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
/**
 * 負責擷取新增App的intent
 * 註冊intent & broadcastReceiver
 * */
public class IntentService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		PackageReceiver PR = new PackageReceiver();
		IntentFilter mFilter;
		// 過濾PACKAGE_ADDED與PACKAGE_REMOVED事件
		mFilter = new IntentFilter(PR.addpk_ACTION);
		mFilter.addDataScheme("package");
		// 註冊Receiver
		registerReceiver(PR, mFilter);
		
		super.onCreate();
	}

}




