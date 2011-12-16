package com.rubengm.seriesly.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.rubengm.seriesly.Avanzado;
import com.rubengm.seriesly.objects.Link;
import com.rubengm.seriesly.utils.DownloadHelper;
import com.rubengm.seriesly.utils.Prefs;

public class EnlacesEpisodio {
	public static final String TAG = "EnlacesEpisodio";
	public static String getEnlacesEpisodio(Context c) { return Prefs.getString(c, Avanzado.ENLACES_EPISODIO_URL, Avanzado.ENLACES_EPISODIO_URL_DEFAULT); }
	private static String getUrl(Context c, String filmid) {
		return getEnlacesEpisodio(c).replace("[token]", Prefs.getString(c, "auth_token", "")).replace("[filmid]", filmid).replace("[usertoken]", Prefs.getString(c, "user_token", ""));
	}

	public static Link[] getEnlaces(Context c, String episodioId) {
		Link[] p = null;
		try {
			String res = new DownloadHelper().download(getUrl(c, episodioId));
			p = new Gson().fromJson(res, Link[].class);
		} catch (Exception e) {
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return p;
	}
}