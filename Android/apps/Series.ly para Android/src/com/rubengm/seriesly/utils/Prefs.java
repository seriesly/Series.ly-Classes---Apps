package com.rubengm.seriesly.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Prefs {
	public static final String TAG = "Prefs";
	public static final String PREF_NAME = "pref_quis";

	public static SharedPreferences get(Context context) {
		try {
			return context.getSharedPreferences(PREF_NAME, 0);
		}catch(Exception ex) {
			Log.e(TAG, "Error: " + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		return null;
	}

	public static void put(Context context, String key, int value) {
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void put(Context context, String key, boolean value) {
		int intVal = 0;
		if(value) intVal = 1;
		put(context, key, intVal);
	}

	public static void put(Context context, String key, String value) {
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void put(Context context, String key, long value) {
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static String getString(Context context, String key, String defaultValue) {
		try {
			return get(context).getString(key, defaultValue);
		}catch(Exception ex) {
			return defaultValue;
		}
	}

	public static int getInt(Context context, String key, int defaultValue) {
		try {
			return get(context).getInt(key, defaultValue);
		}catch(Exception ex) {
			return defaultValue;
		}
	}

	public static long getLong(Context context, String key, long defaultValue) {
		try {
			return get(context).getLong(key, defaultValue);
		}catch (Exception e) {
			return defaultValue;
		}
	}

	public static boolean getBool(Context context, String key, boolean defaultValue) {
		try {
			int defValue = 0;
			if(defaultValue) defValue = 1;
			return getInt(context, key, defValue) == 1;
		}catch (Exception e) {
			return defaultValue;
		}
	}
}
