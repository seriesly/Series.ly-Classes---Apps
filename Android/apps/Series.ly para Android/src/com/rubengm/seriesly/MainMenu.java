package com.rubengm.seriesly;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rubengm.seriesly.api.AuthToken;
import com.rubengm.seriesly.api.UserPermisos;
import com.rubengm.seriesly.utils.Prefs;

public class MainMenu extends Activity {
	public static final String TAG = MainMenu.class.getName();
	private MainMenuAdapter mainMenuAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);
		try {
		} catch (Exception e) { }
		config();
	}
	
	private void config() {
		final ListView lista = (ListView)findViewById(R.id.lista);
		mainMenuAdapter = new MainMenuAdapter();
		lista.setAdapter(mainMenuAdapter);
		lista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent;
				switch(arg2) {
				case 0: //Mis Series
					intent = new Intent(MainMenu.this, MisSeries.class);
					startActivity(intent);
					break;
				case 1: //Mis peliculas
					break;
				case 2: //Buscar Series
					break;
				case 3: //Buscar peliculas
					break;
				case 4: //Desconectar
					Prefs.put(MainMenu.this, "logged_in", false);
					intent = new Intent(MainMenu.this, Main.class);
					startActivity(intent);
					finish();
					break;
				}
			}
		});
		new Auth().execute();
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
	
	private class MainMenuAdapter extends BaseAdapter {
		private String[] acciones = new String[]{ getString(R.string.series), getString(R.string.peliculas), getString(R.string.buscar), getString(R.string.buscar_pelis), getString(R.string.logout) };

		@Override
		public int getCount() {
			return acciones.length;
		}

		@Override
		public String getItem(int arg0) {
			return acciones[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			String accion = getItem(arg0);
			View v = arg1;
			if(v == null) {
				v = View.inflate(MainMenu.this, R.layout.mainmenu_grid_item, null);
			}
			TextView texto = (TextView)v.findViewById(R.id.text);
			texto.setText(accion);
			return v;
		}
	}
	
	private class Auth extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			AuthToken.getAuthToken(MainMenu.this);
			UserPermisos.getUserToken(MainMenu.this);
			return null;
		}
	}
}
