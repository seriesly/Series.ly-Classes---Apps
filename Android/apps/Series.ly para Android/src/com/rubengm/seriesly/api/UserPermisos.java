package com.rubengm.seriesly.api;

import android.content.Context;
import android.util.Log;

import com.rubengm.seriesly.Avanzado;
import com.rubengm.seriesly.utils.DownloadHelper;
import com.rubengm.seriesly.utils.Prefs;

public class UserPermisos {
	public static String getUserPermisosUrl(Context c) { return Prefs.getString(c, Avanzado.USER_PERMISOS_URL, Avanzado.USER_PERMISOS_URL_DEFAULT); }
	public static final String TAG = "UserPermisos";
	private static class UserPermisosRequest {
		private String lg_login;
		private String lg_pass;
		private String auth_token;

		public UserPermisosRequest(String token, String login, String pass) {
			auth_token = token;
			lg_login = login;
			lg_pass = pass;
		}

		public String getUserToken(Context c) {
			String userToken = null;
			try {
				DownloadHelper helper = new DownloadHelper();
				String url = getUserPermisosUrl(c).replace("[user]", lg_login).replace("[pass]", lg_pass).replace("[token]", auth_token);
				userToken = helper.download(url);
				userToken = userToken.replace("\n", "");
			} catch (Exception e) {
				Log.e(TAG, "Error: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
			return userToken;
		}
	}
	
	public static String getUserToken(Context c) {
		String login = Prefs.getString(c, "lg_login", "");
		String pass = Prefs.getString(c, "lg_pass", "");
		String token = Prefs.getString(c, "auth_token", "");
		UserPermisosRequest request = new UserPermisosRequest(token, login, pass);
		String user_token = request.getUserToken(c);
		Prefs.put(c, "user_token", user_token);
		return user_token;
	}
}
