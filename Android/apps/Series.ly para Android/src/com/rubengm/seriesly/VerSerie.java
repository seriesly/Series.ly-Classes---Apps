package com.rubengm.seriesly;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rubengm.seriesly.api.FichaSerie;
import com.rubengm.seriesly.objects.Episode;
import com.rubengm.seriesly.objects.Serie;
import com.rubengm.seriesly.objects.Temporada;

public class VerSerie extends Activity {
	public static final String TAG = VerSerie.class.getName();
	private Serie s;
	private EpisodioAdapter adapter;
	private boolean episodiosLeidos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verserie);
		config();
	}

	private void config() {
		try {
			s = new Gson().fromJson(getIntent().getExtras().getString("serie"), Serie.class);
			readSerie(s);
			((TextView) findViewById(R.id.sinopsis)).setText("Descargando...");
			final ExpandableListView lista = (ExpandableListView) findViewById(R.id.lista);
			adapter = new EpisodioAdapter();
			lista.setAdapter(adapter);
			new DatosSerie().execute();
		} catch (Exception e) {
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
			e.printStackTrace();
			finish();
		}
	}

	private void readSerie(Serie s) {
		TextView titulo = (TextView) findViewById(R.id.titulo);
		TextView puntuacion = (TextView) findViewById(R.id.puntuacion);
		TextView sinopsis = (TextView) findViewById(R.id.sinopsis);
		LinearLayout linearImagen = (LinearLayout) findViewById(R.id.linearImagen);
		try {
			titulo.setText(s.getTitle());
			puntuacion.setText(s.getSeriesly_score() + "/5");
			sinopsis.setText(s.getSynopsis());
			linearImagen.removeAllViews();
			linearImagen.addView(s.getImagenGrande(VerSerie.this));
		} catch (Exception e) {
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
		}
		if(adapter != null) adapter.reload();
		if(!episodiosLeidos) {
			new EpisodiosSerie().execute();
			episodiosLeidos = true;
		}
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

	private class EpisodioAdapter extends BaseExpandableListAdapter {
		private ArrayList<Temporada> temporadas = new ArrayList<Temporada>();

		public EpisodioAdapter() {
			reload();
		}

		public void reload() {
			temporadas = new ArrayList<Temporada>();
			try {
				for(Episode e : s.getEpisode()) {
					addEpisodio(e);
				}
			} catch (Exception e) {
				Log.e(TAG, "Error: " + e.getLocalizedMessage());
			}
			notifyDataSetChanged();
		}

		private void addEpisodio(Episode e) {
			String t = "Temporada " + e.getSeason().split("x")[0];
			int i = 0;
			boolean found = false;
			while(!found && i < temporadas.size()) {
				found = t.equals(temporadas.get(i).getTitle());
				if(found) {
					temporadas.get(i).add(e);
				}
				i++;
			}
			if(!found) {
				Temporada temp = new Temporada();
				temp.setTitle(t);
				temp.add(e);
				temporadas.add(temp);
			}
		}

		@Override
		public Episode getChild(int groupPosition, int childPosition) {
			return getGroup(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			View v = convertView;
			Episode e = getChild(groupPosition, childPosition);

			if(v == null) {
				v = View.inflate(VerSerie.this, R.layout.row_episodio, null);
			}

			((TextView)v.findViewById(R.id.textLeft)).setText(e.getSeason());
			((TextView)v.findViewById(R.id.text)).setText(e.getTitle());

			return v;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return getGroup(groupPosition).size();
		}

		@Override
		public Temporada getGroup(int groupPosition) {
			return temporadas.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return temporadas.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v == null) {
				v = View.inflate(VerSerie.this, R.layout.row_temporada, null);
			}
			Temporada t = getGroup(groupPosition);
			((TextView)v.findViewById(R.id.text)).setText(t.getTitle());
			return v;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}



	}

	private class DatosSerie extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			readSerie(s);
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			s = FichaSerie.getSerie(VerSerie.this, s.getIdSerie());
			return null;
		}

	}

	private class EpisodiosSerie extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			readSerie(s);
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			s.setEpisode(FichaSerie.getEpisodios(VerSerie.this, s.getIdSerie()));
			return null;
		}
	}
}
