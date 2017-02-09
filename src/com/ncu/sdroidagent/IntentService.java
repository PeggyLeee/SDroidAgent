package com.ncu.sdroidagent;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
/**
 * �t�d�^���s�WApp��intent
 * ���Uintent & broadcastReceiver
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
		// �L�oPACKAGE_ADDED�PPACKAGE_REMOVED�ƥ�
		mFilter = new IntentFilter(PR.addpk_ACTION);
		mFilter.addDataScheme("package");
		// ���UReceiver
		registerReceiver(PR, mFilter);
		
		super.onCreate();
	}

}




