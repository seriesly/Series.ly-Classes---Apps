package com.rubengm.seriesly.api;

import android.content.Context;
import android.util.Log;

import com.rubengm.seriesly.Avanzado;
import com.rubengm.seriesly.R;
import com.rubengm.seriesly.utils.DownloadHelper;
import com.rubengm.seriesly.utils.Prefs;

public class AuthToken {
	public static final String TAG = "AuthToken";
	public static String getAuthTokenURL(Context c) { return Prefs.getString(c, Avanzado.AUTH_URL, Avanzado.AUTH_URL_DEFAULT); }

	private static class AuthTokenRequest {
		public String api;
		public String secret;
		public AuthTokenRequest(String _api, String _secret) {
			api = _api;
			secret = _secret;
		}
		public String getUrl(Context c) {
			return getAuthTokenURL(c) + "?api=" + api + "&secret=" + secret;
		}
	}

	public static String getAuthToken(Context c) {
		try {
			AuthTokenRequest request = new AuthTokenRequest(c.getString(R.string.api), c.getString(R.string.secret));
			String res = new DownloadHelper().download(request.getUrl(c)).replace("\n", "");
			Prefs.put(c, "auth_token", res);
			return res;
		} catch (Exception e) {
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
	}
}
