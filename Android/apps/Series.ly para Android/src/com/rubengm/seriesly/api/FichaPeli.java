package com.rubengm.seriesly.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.rubengm.seriesly.Avanzado;
import com.rubengm.seriesly.objects.Pelicula;
import com.rubengm.seriesly.utils.DownloadHelper;
import com.rubengm.seriesly.utils.Prefs;

public class FichaPeli {
	public static final String TAG = "FichaPeli";
	public static String getFichaPeliUrl(Context c) { return Prefs.getString(c, Avanzado.FICHA_PELI_URL, Avanzado.FICHA_PELI_URL_DEFAULT); }
	private static String getFichaPeliUrl(Context c, String filmid) {
		return getFichaPeliUrl(c).replace("[token]", Prefs.getString(c, "auth_token", "")).replace("[filmid]", filmid).replace("[usertoken]", Prefs.getString(c, "user_token", ""));
	}

	public static Pelicula getPeli(Context c, String filmId) {
		Pelicula p = null;
		try {
			String res = new DownloadHelper().download(getFichaPeliUrl(c, filmId));
			p = new Gson().fromJson(res, Pelicula.class);
		} catch (Exception e) {
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return p;
	}
}