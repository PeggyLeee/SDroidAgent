package Util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
/**
 * AndroidずAsyncTaskよ猭肚癳http戈斗ㄏノ
 * */
public class AsyncHttpTask extends AsyncTask<String, Integer, String>{

    // String 碞琌 Params 把计摸
    // Integer 碞琌 Progress 把计摸
    // String 碞琌 Result 把计摸

	private LockAppPreference pref;
	private Context aContext;
	private String pkName;
	
	public AsyncHttpTask(Context context){
		this.aContext = context;
		pref = new LockAppPreference(aContext);
	}
	
	@Override
	protected String doInBackground(String... params) {
		Log.i("AsyncHttpTask", "Start to Send Info.");
		ConfigFile config = new ConfigFile();
		String result = null;
		String ServerURL = "http://"+config.getPropValue("ServerHost")+":"
				+config.getPropValue("ServerPort")+"/"+config.getPropValue("ServerContext");
		pkName = params[0];
		
		// Creating HTTP client
		HttpClient httpClient = new DefaultHttpClient();
		// Creating HTTP Post
		HttpPost httpPost = new HttpPost(ServerURL);
	    
		// Building post parameters
        // key and value pair
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("pkName", pkName));
		
		// Url Encoding the POST parameters
		try{
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
		}catch(UnsupportedEncodingException ex){
			ex.printStackTrace();
		}
		
		// Making HTTP Request
		try{
			HttpResponse response = httpClient.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == 200){
				result = EntityUtils.toString(response.getEntity());
				
			}else{
				result = "HttpPost False!";
			}
			Log.d("AsyncHttpTask", "result: "+result);
			
		}catch(ClientProtocolException cpe){
			cpe.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}

		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		Log.i("AsyncHttpTask", "UnLock APP: "+result);
		
		switch(result){
		case "success":
			pref.putLock(pkName, false);
			break;
		case "failure":
			pref.putLock(pkName, true);
			break;
		case "no policies":
			pref.removeLock(pkName);
			break;
		}
		
	}	
	
	
	
}
