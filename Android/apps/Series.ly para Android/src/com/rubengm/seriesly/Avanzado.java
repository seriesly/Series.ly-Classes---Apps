package com.rubengm.seriesly;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.rubengm.seriesly.utils.Prefs;

public class Avanzado extends Activity {

	public static final String BUSCAR_URL = "Buscar";
	public static final String ENLACES_EPISODIO_URL = "Enlaces episodio";
	public static final String AUTH_URL = "Auth";
	public static final String FICHA_PELI_URL = "Ficha peli";
	public static final String FICHA_SERIE_URL = "Ficha serie";
	public static final String EPISODIOS_SERIE_URL = "Episodios serie";
	public static final String USER_ESTADO_URL = "User estado";
	public static final String USER_MIS_PELIS_URL = "User mis pelis";
	public static final String USER_MIS_SERIES_URL = "User mis series";
	public static final String USER_PERMISOS_URL = "User permisos";

	public static final String BUSCAR_URL_DEFAULT = "http://series.ly/api/search.php?auth_token=[token]&search=[search]&type=[type]&format=json";
	public static final String ENLACES_EPISODIO_URL_DEFAULT = "http://series.ly/api/linksCap.php?auth_token=[token]&idCap=[filmid]&user_token=[usertoken]&format=json";
	public static final String AUTH_URL_DEFAULT = "http://series.ly/api/auth.php";
	public static final String FICHA_PELI_URL_DEFAULT = "http://series.ly/api/detailMovie.php?auth_token=[token]&idFilm=[filmid]&user_token=[usertoken]&format=json";
	public static final String FICHA_SERIE_URL_DEFAULT = "http://series.ly/api/detailoSerie.php?auth_token=[token]&idSerie=[filmid]&user_token=[usertoken]&format=json&caps=1";
	public static final String EPISODIOS_SERIE_URL_DEFAULT = "http://series.ly/api/capsSerie.php?format=json&auth_token=[token]&idSerie=[filmid]&user_token=[usertoken]";
	public static final String USER_ESTADO_URL_DEFAULT = "http://series.ly/api/isUserLogged.php";
	public static final String USER_MIS_PELIS_URL_DEFAULT = "http://series.ly/api/userMovies.php?auth_token=[auth_token]&user_token=[user_token]&format=json";
	public static final String USER_MIS_SERIES_URL_DEFAULT = "http://series.ly/api/userSeries.php?auth_token=[auth_token]&user_token=[user_token]&format=json";
	public static final String USER_PERMISOS_URL_DEFAULT = "http://series.ly/api/api_login.php?lg_login=[user]&lg_pass=[pass]&auth_token=[token]";

	private final String[] constantes = new String[] {BUSCAR_URL, ENLACES_EPISODIO_URL, AUTH_URL, FICHA_PELI_URL, FICHA_SERIE_URL, EPISODIOS_SERIE_URL, USER_ESTADO_URL, USER_MIS_PELIS_URL, USER_MIS_SERIES_URL, USER_PERMISOS_URL};
	private String[] defaultValues = new String[] {BUSCAR_URL_DEFAULT, ENLACES_EPISODIO_URL_DEFAULT, AUTH_URL_DEFAULT, FICHA_PELI_URL_DEFAULT, FICHA_SERIE_URL_DEFAULT, EPISODIOS_SERIE_URL_DEFAULT, USER_ESTADO_URL_DEFAULT, USER_MIS_PELIS_URL_DEFAULT, USER_MIS_SERIES_URL_DEFAULT, USER_PERMISOS_URL_DEFAULT};
	private String[] valores = new String[] {BUSCAR_URL_DEFAULT, ENLACES_EPISODIO_URL_DEFAULT, AUTH_URL_DEFAULT, FICHA_PELI_URL_DEFAULT, FICHA_SERIE_URL_DEFAULT, EPISODIOS_SERIE_URL_DEFAULT, USER_ESTADO_URL_DEFAULT, USER_MIS_PELIS_URL_DEFAULT, USER_MIS_SERIES_URL_DEFAULT, USER_PERMISOS_URL_DEFAULT};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avanzado);
		final ListView lista = (ListView)findViewById(R.id.lista);
		int len = constantes.length;
		for(int i = 0; i < len; i++) {
			valores[i] = Prefs.getString(this, constantes[i], valores[i]);
		}
		lista.setAdapter(new VarAdapter());
		lista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				final Dialog d = new Dialog(Avanzado.this, R.style.FondoTransparente);
				d.setContentView(R.layout.dialog_edit_avanzado);
				final EditText et = (EditText) d.findViewById(R.id.et);
				et.setText(valores[position]);
				final Button ok = (Button)d.findViewById(R.id.ok);
				final Button cancel = (Button)d.findViewById(R.id.cancel);
				final Button restore = (Button)d.findViewById(R.id.restore);
				
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						d.dismiss();
					}
				});
				
				restore.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Prefs.put(Avanzado.this, constantes[position], defaultValues[position]);
						valores[position] = defaultValues[position];
						d.dismiss();
						((VarAdapter)lista.getAdapter()).notifyDataSetChanged();
					}
				});
				
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Prefs.put(Avanzado.this, constantes[position], et.getText().toString());
						valores[position] = et.getText().toString();
						d.dismiss();
						((VarAdapter)lista.getAdapter()).notifyDataSetChanged();
					}
				});
				
				d.show();
			}
		});
	}

	private class VarAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return constantes.length;
		}

		@Override
		public String getItem(int position) {
			return constantes[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		private class ViewHolder {
			TextView var;
			TextView val;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null) {
				convertView = View.inflate(Avanzado.this, R.layout.row_avanzado, null);
				holder = new ViewHolder();
				holder.var = (TextView)convertView.findViewById(R.id.var);
				holder.val = (TextView)convertView.findViewById(R.id.val);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String item = getItem(position);

			holder.var.setText(item);
			holder.val.setText(valores[position]);

			return convertView;
		}

	}

}
