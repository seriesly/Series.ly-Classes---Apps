package com.rubengm.seriesly.api;

import android.content.Context;
import android.util.Log;

import com.rubengm.seriesly.utils.DownloadHelper;
import com.rubengm.seriesly.utils.Prefs;

public class UserEstado {
	public static final String UserEstadoUrl = "http://series.ly/api/isUserLogged.php";
	public static final String TAG = "UserEstado";

	private static class UserEstadoRequest {
		private String auth_token;
		private String user_token;

		public UserEstadoRequest(Context c) {
			auth_token = Prefs.getString(c, "auth_token", "");
			user_token = Prefs.getString(c, "user_token", "");
		}

		public String getUrl() {
			return UserEstadoUrl + "?auth_token=" + auth_token + "&user_token=" + user_token;
		}
	}

	public static boolean isLoggedIn(Context c) {
		boolean loggedin = false;
		try {
			UserEstadoRequest request = new UserEstadoRequest(c);
			String url = request.getUrl();
			String res = new DownloadHelper().download(url);
			loggedin = res.replace("\n", "").equals("1");
		}catch (Exception e) {
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return loggedin;
	}
}
