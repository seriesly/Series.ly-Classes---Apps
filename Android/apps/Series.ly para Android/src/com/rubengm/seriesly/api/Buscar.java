package com.rubengm.seriesly.api;

import java.net.URLEncoder;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.rubengm.seriesly.objects.Pelicula;
import com.rubengm.seriesly.objects.Serie;
import com.rubengm.seriesly.utils.DownloadHelper;
import com.rubengm.seriesly.utils.Prefs;

public class Buscar {
	private static final String TAG = "Buscar";
	private static final String BuscarURL = "http://series.ly/api/search.php?auth_token=[token]&search=[search]&type=[type]&format=json";
	private static final String TYPE_MOVIE = "film";
	private static final String TYPE_SERIE = "serie";
	private static String getBuscarUrl(Context c, String search, String type) {
		return BuscarURL.replace("[token]", Prefs.getString(c, "auth_token", "")).replace("[search]", URLEncoder.encode(search)).replace("[type]", type);
	}

	public static Serie[] buscaSeries(Context c, String query) {
		Serie[] series = new Serie[0];
		try {
			String res = new DownloadHelper().download(getBuscarUrl(c, query, TYPE_SERIE));
			series = new Gson().fromJson(res, Serie[].class);
			if(series == null) {
				series = new Serie[0];
			}
		} catch (Exception e) {
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return series;
	}

	public static Pelicula[] buscaPeliculas(Context c, String query) {
		Pelicula[] peliculas = new Pelicula[0];
		try {
			String res = new DownloadHelper().download(getBuscarUrl(c, query, TYPE_MOVIE));
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
