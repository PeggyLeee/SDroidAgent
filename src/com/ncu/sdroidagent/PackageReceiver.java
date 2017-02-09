package com.ncu.sdroidagent;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;
import Util.AsyncHttpTask;
import Util.LockAppPreference;
/**
 * 此部分有三項主要動作：取得package name、Look app、傳送給SDroid平台
 * */
public class PackageReceiver extends BroadcastReceiver {
	
	public final String addpk_ACTION = "android.intent.action.PACKAGE_ADDED";
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(addpk_ACTION)) {
			Log.i("IntentReceiver", "Get PACKAGE_ADDED Intent!");
			
			//取的使用者最新下載的Apk name
			Uri data = intent.getData();
			String pkname = data.getEncodedSchemeSpecificPart();
			Log.d("IntentReceiver", "pkname: "+pkname);
			
			//Lock app
			LockAppPreference pref = new LockAppPreference(context);
			pref.save(pref.putLock(pkname, true));
			
			// 將資訊傳送給SDroid平台
			AsyncHttpTask aht = new AsyncHttpTask(context);
			aht.execute(new String[] {pkname});
			

		}
	}
}
