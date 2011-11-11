package com.rubengm.seriesly.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.rubengm.seriesly.objects.Episode;
import com.rubengm.seriesly.objects.Serie;
import com.rubengm.seriesly.utils.DownloadHelper;
import com.rubengm.seriesly.utils.Prefs;

public class FichaSerie {
	public static final String TAG = "FichaSerie";
	private static final String FichaSerieUrl = "http://series.ly/tmp/detailSer.php?auth_token=[token]&idSerie=[filmid]&user_token=[usertoken]&format=json";
	//private static final String EpisodiosSerieUrl = 			"http://series.ly/tmp/episodelist.php?auth_token=[token]&idSerie=[filmid]&user_token=[usertoken]";
	private static final String EpisodiosSerieUrl = 	"http://series.ly/api/detailSerie.php?auth_token=[token]&idSerie=[filmid]&user_token=[usertoken]&format=json";
	//private static final String FichaSerieUrl = "http://series.ly/api/detailSerie.php?auth_token=[token]&idSerie=[filmid]&user_token=[usertoken]&format=json";
	private static String getFichaSerieUrl(Context c, String filmid) {
		return FichaSerieUrl.replace("[token]", Prefs.getString(c, "auth_token", "")).replace("[filmid]", filmid).replace("[usertoken]", Prefs.getString(c, "user_token", ""));
	}
	private static String getEpisodiosSerie(Context c, String filmid) {
		return EpisodiosSerieUrl.replace("[token]", Prefs.getString(c, "auth_token", "")).replace("[filmid]", filmid).replace("[usertoken]", Prefs.getString(c, "user_token", ""));
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
	
	public static Episode[] getEpisodios(Context c, String serieId) {
		Episode[] s = null;
		try {
			String res = new DownloadHelper().download(getEpisodiosSerie(c, serieId));
			s = new Gson().fromJson(res, Episode[].class);
		} catch (Exception e) {
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return s;
	}
}