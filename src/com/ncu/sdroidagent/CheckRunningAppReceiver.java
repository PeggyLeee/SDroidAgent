package com.ncu.sdroidagent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Util.LockAppPreference;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
/**
 * 進行判斷是否lock or unlock app
 * */
public class CheckRunningAppReceiver extends BroadcastReceiver{

	private ActivityManager am;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("CheckRunningAppReceiver", "Receive Info");
		
		am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getTopPackageName();
		Log.d("CheckRunningAppReceiver", "packageName:"+packageName);
		
		LockAppPreference pref = new LockAppPreference(context);
		Boolean lock = pref.getLock(packageName);
		Log.d("CheckRunningAppReceiver", "lock:"+lock);
		if(lock){
			blockApp(context);
		}
//		if(packageName.equals("com.ncu.sdroidagent")){
//			stopActivity(context);
//		}
		
	}
	
    private String getTopPackageName() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return am.getRunningTasks(1).get(0).topActivity.getPackageName();
        } else {
            final List<ActivityManager.RunningAppProcessInfo> pis = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo pi : pis) {
                if (pi.pkgList.length == 1) return pi.pkgList[0];
            }
        }
        return "";
    }

    
    private void blockApp(Context context) {
		Log.i("CheckRunningAppReceiver", "Block specify package.");

		Intent intent = new Intent(context, LockScreenActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("msg", "此應用程式未通過SDroid系統分析");
		
		context.startActivity(intent);
		
	}
    
}
