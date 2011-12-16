package com.rubengm.seriesly.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.rubengm.seriesly.Avanzado;
import com.rubengm.seriesly.objects.Episode;
import com.rubengm.seriesly.objects.Serie;
import com.rubengm.seriesly.utils.DownloadHelper;
import com.rubengm.seriesly.utils.Prefs;

public class FichaSerie {
	public static final String TAG = "FichaSerie";
	private static String getFichaSerieUrl(Context c) { return Prefs.getString(c, Avanzado.FICHA_SERIE_URL, Avanzado.FICHA_SERIE_URL_DEFAULT); }
	private static String getEpisodiosSerieUrl(Context c) { return Prefs.getString(c, Avanzado.EPISODIOS_SERIE_URL, Avanzado.EPISODIOS_SERIE_URL_DEFAULT); }

	private static String getFichaSerieUrl(Context c, String filmid) {
		return getFichaSerieUrl(c).replace("[token]", Prefs.getString(c, "auth_token", "")).replace("[filmid]", filmid).replace("[usertoken]", Prefs.getString(c, "user_token", ""));
	}
	private static String getEpisodiosSerie(Context c, String filmid) {
		return getEpisodiosSerieUrl(c).replace("[token]", Prefs.getString(c, "auth_token", "")).replace("[filmid]", filmid).replace("[usertoken]", Prefs.getString(c, "user_token", ""));
	}

	public static Serie getSerie(Context c, String serieId) {
		Serie s = null;
		try {
			String res = new DownloadHelper().download(getFichaSerieUrl(c, serieId));
			s = new Gson().fromJson(res, Serie.class);
		} catch (Exception e) {
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return s;
	}
	
	//Unused
	@SuppressWarnings("unused")
	private static Episode[] getEpisodios(Context c, Serie serie) {
		Episode[] s = null;
		try {
			String res = new DownloadHelper().download(getEpisodiosSerie(c, serie.getIds()));
			s = new Gson().fromJson(res, Episode[].class);
		} catch (Exception e) {
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return s;
	}
}