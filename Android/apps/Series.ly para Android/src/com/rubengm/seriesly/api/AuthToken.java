package com.rubengm.seriesly.api;

import android.content.Context;
import android.util.Log;

import com.rubengm.seriesly.R;
import com.rubengm.seriesly.utils.DownloadHelper;
import com.rubengm.seriesly.utils.Prefs;

public class AuthToken {
	public static final String TAG = "AuthToken";
	public static final String AuthTokenURL = "http://series.ly/api/auth.php";

	private static class AuthTokenRequest {
		public String api;
		public String secret;
		public AuthTokenRequest(String _api, String _secret) {
			api = _api;
			secret = _secret;
		}
		public String getUrl() {
			return AuthTokenURL + "?api=" + api + "&secret=" + secret;
		}
	}

	public static String getAuthToken(Context c) {
		try {
			AuthTokenRequest request = new AuthTokenRequest(c.getString(R.string.api), c.getString(R.string.secret));
			String res = new DownloadHelper().download(request.getUrl()).replace("\n", "");
			Prefs.put(c, "auth_token", res);
			return res;
		} catch (Exception e) {
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
	}
}
