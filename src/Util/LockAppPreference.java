package Util;

import java.util.HashSet;
import java.util.Set;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
/**
 * 使用Android內建的儲存機制 SharedPreferences
 * */
public class LockAppPreference {

	public static final String PREF_FILE_DEFAULT = "lockerPrefs";
	private static final String PREF_FILE_APPS = "lockerPrefsApps";
	private final Context mContext;
	private final SharedPreferences mPrefs;
	private Editor mEditor;
	
	public LockAppPreference(Context c){
		mContext = c;
		mPrefs = mContext.getSharedPreferences(PREF_FILE_DEFAULT,
				Context.MODE_PRIVATE);
		if (mEditor == null) {
			mEditor = mPrefs.edit();
		}
	}
	
	public Editor editor() {
		if (mEditor == null) {
			mEditor = mPrefs.edit();
		}
		return mEditor;
	}
	
	public SharedPreferences prefs() {
		return mPrefs;
	}
	
	public Editor putLock(String key, Object value) {
		if (key == null) {
			throw new IllegalArgumentException(
					"No resource matched key");
		}
		final Editor editor = editor();
		if (value instanceof String)
			editor.putString(key, (String) value);
		else if (value instanceof Integer)
			editor.putInt(key, (Integer) value);
		else if (value instanceof Boolean)
			editor.putBoolean(key, (Boolean) value);
		else if (value instanceof Float)
			editor.putFloat(key, (Float) value);
		else if (value instanceof Long)
			editor.putLong(key, (Long) value);
		else
			throw new IllegalArgumentException("Unknown data type");
		
		save(editor);
		
		Log.i("LockAppPreference", "putLock("+key+","+value+")");
		return editor;
	}
	
	public Boolean getLock(String key) {
		return mPrefs.getBoolean(key, false);
	}
	
	public void removeLock(String key) {
		save(mEditor.remove(key));
	}
	
	public Set<String> getLockedApps() {
		return new HashSet<String>(mPrefs.getAll().keySet());
	}
	
	public void clear(){
		save(mEditor.clear());
	}
	
	@SuppressLint("NewApi")
	public void save(SharedPreferences.Editor editor) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}
}
