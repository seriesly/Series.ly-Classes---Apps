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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rubengm.seriesly.api.AuthToken;
import com.rubengm.seriesly.api.UserMisSeries;
import com.rubengm.seriesly.api.UserPermisos;
import com.rubengm.seriesly.objects.Serie;

public class MisSeries extends Activity {
	public static final String TAG = MisSeries.class.getName();
	private MisSeriesAdapter misSeriesAdapter;
	private boolean pideTokens = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);
		config();
	}

	private void config() {
		final ListView lista = (ListView)findViewById(R.id.lista);
		misSeriesAdapter = new MisSeriesAdapter();
		lista.setAdapter(misSeriesAdapter);
		lista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Serie s = misSeriesAdapter.getItem(arg2);
				Intent intent = new Intent(MisSeries.this, VerSerie.class);
				intent.putExtra("serie", s.toString());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

	private class MisSeriesAdapter extends BaseAdapter {
		Serie[] series = new Serie[0];

		public MisSeriesAdapter() {
			new CargaSeries().execute();
		}

		@Override
		public int getCount() {
			return series.length;
		}

		@Override
		public Serie getItem(int position) {
			return series[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			Serie s = getItem(position);
			if(v == null) {
				v = View.inflate(MisSeries.this, R.layout.row_serie, null);
			}
			TextView text = (TextView) v.findViewById(R.id.text);
			LinearLayout linearImagen = (LinearLayout) v.findViewById(R.id.linearImagen);
			text.setText(s.getTitle());
			linearImagen.removeAllViews();
			linearImagen.addView(s.getImagen(MisSeries.this));
			return v;
		}

		private class CargaSeries extends AsyncTask<Void, Void, Void> {

			@Override
			protected Void doInBackground(Void... params) {
				if(pideTokens) {
					AuthToken.getAuthToken(MisSeries.this);
					UserPermisos.getUserToken(MisSeries.this);
				}
				series = UserMisSeries.getMisSeries(MisSeries.this);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				notifyDataSetChanged();
				if(series.length == 0) {
					pideTokens = true;
					new CargaSeries().execute();
				}
				super.onPostExecute(result);
			}

			@Override
			protected void onPreExecute() {
				Toast.makeText(MisSeries.this, "Descargando...", Toast.LENGTH_SHORT).show();
				super.onPreExecute();
			}

		}
	}
}
