package com.rubengm.seriesly;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rubengm.seriesly.api.AuthToken;
import com.rubengm.seriesly.api.UserEstado;
import com.rubengm.seriesly.api.UserPermisos;
import com.rubengm.seriesly.utils.Prefs;

public class Main extends Activity {
	public static final String TAG = "Seriesly";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		config();
		if(Prefs.getBool(this, "logged_in", false)) {
			goToMenu();
		}
	}

	private void config() {
		final Button login = (Button)findViewById(R.id.login);
		final EditText user = (EditText)findViewById(R.id.user);
		final EditText pass = (EditText)findViewById(R.id.pass);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String userName = user.getText().toString();
				String password = pass.getText().toString();
				new TryLogin().execute(new String[] {userName,password});
			}
		});
	}
	
	private void goToMenu() {
		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

	private class TryLogin extends AsyncTask<String, Void, Void> {
		ProgressDialog pd;
		boolean ok = false;
		@Override
		protected Void doInBackground(String... params) {
			String user = params[0];
			String pass = params[1];
			Context c = Main.this;
			Prefs.put(c, "lg_login", user);
			Prefs.put(c, "lg_pass", pass);
			AuthToken.getAuthToken(c);
			UserPermisos.getUserToken(c);
			ok = UserEstado.isLoggedIn(c);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(pd != null) {
				pd.dismiss();
				pd = null;
			}
			Prefs.put(Main.this, "logged_in", ok);
			if(ok) {
				Toast.makeText(Main.this, R.string.login_ok, Toast.LENGTH_SHORT).show();
				goToMenu();
			} else {
				String error = getString(R.string.login_ko);
				error += " User Token: " + Prefs.getString(Main.this, "user_token", "");
				error += " Auth Token: " + Prefs.getString(Main.this, "auth_token", "");
				Toast.makeText(Main.this, error, Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(Main.this, R.style.FondoTransparente);
			pd.setTitle(R.string.espere);
			pd.setMessage(getString(R.string.conectando));
			pd.setCancelable(false);
			pd.show();
			super.onPreExecute();
		}

	}
}