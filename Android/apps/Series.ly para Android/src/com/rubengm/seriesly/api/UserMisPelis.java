package com.rubengm.seriesly.api;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.rubengm.seriesly.objects.Pelicula;
import com.rubengm.seriesly.utils.DownloadHelper;
import com.rubengm.seriesly.utils.Prefs;

public class UserMisPelis {
	public static final String TAG = "UserMisPelis";
	private static final String UserMisPelisUrl = "http://series.ly/api/userMovies.php?auth_token=[auth_token]&user_token=[user_token]&format=json";
	public static final String getUserMisPelisUrl(Context c) {
		return UserMisPelisUrl.replace("[auth_token]", Prefs.getString(c, "auth_token", "")).replace("[user_token]", Prefs.getString(c, "user_token", ""));
	}

	public static Pelicula[] getMisSeries(Context c) {
		Pelicula[] peliculas = new Pelicula[0];
		try {
			String res = new DownloadHelper().sendPost(getUserMisPelisUrl(c), new ArrayList<NameValuePair>());
			peliculas = new Gson().fromJson(res, Pelicula[].class);
			if(peliculas == null) {
				peliculas = new Pelicula[0];
			}
		} catch (Exception e) {
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return peliculas;
	}

}
