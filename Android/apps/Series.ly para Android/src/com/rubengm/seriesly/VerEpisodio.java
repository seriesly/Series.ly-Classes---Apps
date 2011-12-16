package com.rubengm.seriesly;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rubengm.seriesly.api.EnlacesEpisodio;
import com.rubengm.seriesly.objects.Link;

public class VerEpisodio extends Activity {
	public static final String TAG = "VerEpisodio";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verepisodio);
		config();
	}

	private void config() {
		new CargaEnlaces().execute(getIntent().getExtras().getString("episodioId"));
	}

	private class CargaEnlaces extends AsyncTask<String, Void, Link[]> {

		@Override
		protected Link[] doInBackground(String... params) {
			return EnlacesEpisodio.getEnlaces(VerEpisodio.this, getIntent().getExtras().getString("episodioId"));
		}

		@Override
		protected void onPostExecute(Link[] result) {
			if(result == null) finish();
			else {
				final ListView lista = (ListView) findViewById(R.id.lista);
				lista.setAdapter(new EnlaceAdapter(result));
				lista.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

						EnlaceAdapter ea = (EnlaceAdapter)lista.getAdapter();
						Link l = ea.getItem(arg2);
						try {
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(l.getUrl_megavideo()));
							startActivity(intent);
						} catch (Exception e) {
							Log.e(TAG, "Error: " + e.getLocalizedMessage());
							e.printStackTrace();
							Toast.makeText(VerEpisodio.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
			super.onPostExecute(result);
		}

	}

	private class EnlaceAdapter extends BaseAdapter {
		private Link[] enlaces;

		public EnlaceAdapter(Link[] e) {
			enlaces = e;
		}

		@Override
		public int getCount() {
			return enlaces.length;
		}

		@Override
		public Link getItem(int position) {
			return enlaces[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		private class ViewHolder {
			TextView texto;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			ViewHolder holder;
			if(v == null) {
				v = View.inflate(VerEpisodio.this, R.layout.row_enlace, null);
				holder = new ViewHolder();
				holder.texto = (TextView) v.findViewById(R.id.text);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}

			holder.texto.setText(getItem(position).getDescription());

			return v;
		}

	}

}
