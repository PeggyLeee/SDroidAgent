/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ncu.sdroidagent;

import static com.ncu.sdroidagent.CommonUtilities.SERVER_URL;
import static com.ncu.sdroidagent.CommonUtilities.TAG;

import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.ncu.sdroidagent.R;

public final class ServerUtilities {

	/**
	 * Register this account/device pair within the server.
	 *
	 */
	public static void register(final Context context, String name,
			String email, final String regId) {
		Log.i(TAG, "registering device (regId = " + regId + ")");
		
		String serverUrl = SERVER_URL + "/SDroid/RegisterServlet.do";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);
		params.put("name", name);
		params.put("email", email);

		try {
			post(serverUrl, params);
			GCMRegistrar.setRegisteredOnServer(context, true);
			String message = context.getString(R.string.server_registered);
			CommonUtilities.displayMessage(context, message);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Unregister this account/device pair within the server.
	 */
	static void unregister(final Context context, final String regId) {
		Log.i(TAG, "unregistering device (regId = " + regId + ")");
		String serverUrl = SERVER_URL + "/unregister";
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);
		try {
			post(serverUrl, params);
			GCMRegistrar.setRegisteredOnServer(context, false);
			String message = context.getString(R.string.server_unregistered);
			CommonUtilities.displayMessage(context, message);
		} catch (IOException e) {
			// At this point the device is unregistered from GCM, but still
			// registered in the server.
			// We could try to unregister again, but it is not necessary:
			// if the server tries to send a message to the device, it will get
			// a "NotRegistered" error message and should unregister the device.
			String message = context.getString(
					R.string.server_unregister_error, e.getMessage());
			CommonUtilities.displayMessage(context, message);
		}
	}

	/**
	 * Issue a POST request to the SDroid server.
	 *
	 * @param endpoint
	 *            POST address.
	 * @param params
	 *            request parameters.
	 *
	 * @throws IOException
	 *             propagated from POST.
	 */
	private static void post(String endpoint, Map<String, String> params)
			throws IOException {

		String result = null;
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(endpoint);

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			nameValuePair.add(new BasicNameValuePair(param.getKey(), param
					.getValue()));
		}

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,
					HTTP.UTF_8));
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}

		try {
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			} else {
				result = "HttpPost False!";
			}
			Log.d("ServerUtilities", "result: " + result);

		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
