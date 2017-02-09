package com.ncu.sdroidagent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
/**
 * 設定多久執行背景程式一次，目前設為每秒
 * */
public class StartupReceiver extends BroadcastReceiver{
	
	final int startupID = 1111111;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
        final AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
    try{
            // Create pending intent for CheckRunningApplicationReceiver.class 
            // it will call after each 1 seconds
            Intent it = new Intent(context, CheckRunningAppReceiver.class);
            PendingIntent ServiceManagementIntent = PendingIntent.getBroadcast(context,
                    startupID, it, 0);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), 
                    1000, ServiceManagementIntent);
             
        } catch (Exception e) {
            Log.i("StartupReceiver", "Exception : "+e);
        }
	}
	


}
