package com.rubengm.seriesly.api;

import android.content.Context;
import android.util.Log;

import com.rubengm.seriesly.utils.DownloadHelper;
import com.rubengm.seriesly.utils.Prefs;

public class UserPermisos {

	//http://seriesly.pbworks.com/w/page/41577968/user-permisos
	/*Introducción

Mediante POST:
Este segundo método permite más flexibilidad, ya que es el desarrollador el que construye el formulario de login y envía después los datos para verificación y obtención del user_token.

Descripción:
Solicita permiso al usuario para acceder a sus pelis y series. 

Límite de llamadas:
     No hay límite de llamadas.



Mediante POST:

  Dirección: http://series.ly/scripts/login/login.php?callback_url=&auth_token=

- Parámetros que deben ir en POST:
  |- lg_login: Nick del usuario/Email.
  |- lg_pass: Contraseña del usuario.

  |- callback_url: Dirección a la que se redireccionará al usuario.
    |- no = En caso de poner no, se devolverá el user_token, sin redirección, ni nada.

  |- auth_token: Identificador de la sesión. 
  |- autologin: Define el tipo de respuesta.
     |- true = Redirecciona hacia callback_url con la variable user_token añadida.
     |- (vacío) = Devuelve un código javascript que realiza la redirección a callback_url con la variable user_token añadida. Ejemplo:
     <script language='javascript'> 
        window.opener.location.href="/http://www.ejemplo.com/mi_widget/?user_token=498rghy8374';
        window.close(); 
     </script>*/
/*
	public static final String UserPermisosUrl = "http://series.ly/scripts/login/login.php";
	public static final String TAG = "UserPermisos";
	private static class UserPermisosRequest {
		private String lg_login;
		private String lg_pass;
		private String callback_url = "no";
		private String auth_token;
		private String autologin = "1";

		public UserPermisosRequest(String token, String login, String pass) {
			auth_token = token;
			lg_login = login;
			lg_pass = pass;
		}

		public String getUserToken() {
			String userToken = null;
			try {
				DownloadHelper helper = new DownloadHelper();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("lg_login", lg_login));
				params.add(new BasicNameValuePair("lg_pass", lg_pass));
				params.add(new BasicNameValuePair("callback_url", callback_url));
				params.add(new BasicNameValuePair("auth_token", auth_token));
				params.add(new BasicNameValuePair("autologin", autologin));
				params.add(new BasicNameValuePair("Expect", "100-Continue"));
				userToken = helper.sendPost(UserPermisosUrl, params);
			} catch (Exception e) {
				Log.e(TAG, "Error: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
			return userToken;
		}
	}
	
	public static String getUserToken(Context c) {
		String login = Prefs.getString(c, "lg_login", "");
		String pass = Prefs.getString(c, "lg_pass", "");
		String token = Prefs.getString(c, "auth_token", "");
		UserPermisosRequest request = new UserPermisosRequest(token, login, pass);
		String userToken = request.getUserToken();
		Prefs.put(c, "user_token", userToken);
		return userToken;
	}
*/
	
	
	
	
	public static final String UserPermisosUrl = "http://series.ly/api/api_login.php?lg_login=[user]&lg_pass=[pass]&auth_token=[token]";
	public static final String TAG = "UserPermisos";
	private static class UserPermisosRequest {
		private String lg_login;
		private String lg_pass;
		private String auth_token;

		public UserPermisosRequest(String token, String login, String pass) {
			auth_token = token;
			lg_login = login;
			lg_pass = pass;
		}

		public String getUserToken() {
			String userToken = null;
			try {
				DownloadHelper helper = new DownloadHelper();
				String url = UserPermisosUrl.replace("[user]", lg_login).replace("[pass]", lg_pass).replace("[token]", auth_token);
				userToken = helper.download(url);
				userToken = userToken.replace("\n", "");
			} catch (Exception e) {
				Log.e(TAG, "Error: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
			return userToken;
		}
	}
	
	public static String getUserToken(Context c) {
		String login = Prefs.getString(c, "lg_login", "");
		String pass = Prefs.getString(c, "lg_pass", "");
		String token = Prefs.getString(c, "auth_token", "");
		UserPermisosRequest request = new UserPermisosRequest(token, login, pass);
		String user_token = request.getUserToken();
		Prefs.put(c, "user_token", user_token);
		return user_token;
	}
}
