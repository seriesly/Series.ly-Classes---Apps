package com.rubengm.seriesly.api;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.rubengm.seriesly.objects.Serie;
import com.rubengm.seriesly.utils.DownloadHelper;
import com.rubengm.seriesly.utils.Prefs;

public class UserMisSeries {
	public static final String TAG = "UserMisSeries";
	private static final String UserMisSeriesUrl = "http://series.ly/api/userSeries.php?auth_token=[auth_token]&user_token=[user_token]&format=json";
	public static final String getUserMisSeriesUrl(Context c) {
		return UserMisSeriesUrl.replace("[auth_token]", Prefs.getString(c, "auth_token", "")).replace("[user_token]", Prefs.getString(c, "user_token", ""));
	}

	public static Serie[] getMisSeries(Context c) {
		Serie[] series = new Serie[0];
		String res = "";
		try {
			res = new DownloadHelper().sendPost(getUserMisSeriesUrl(c), new ArrayList<NameValuePair>());
			series = new Gson().fromJson(res, Serie[].class);
			if(series == null) {
				series = new Serie[0];
			}
		} catch (Exception e) {
			Log.e(TAG, "RES: " + res);
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return series;
	}
}
