package com.rubengm.seriesly.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class DownloadHelper {
	private static String TAG = "DownloadHelper";
	private static final int HTTP_STATUS_OK = 200;
	private static final String sUserAgent = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; 86) AppleWebKit/533.4 (KHTML, like Gecko) Chrome/5.0.375.125 Safari/533.4";
	private byte[] sBuffer = new byte[8192];
	private int readBytes = 0;
	private ProgressDialog pd;
	private InputStream inputStream;
	private ByteArrayOutputStream content;
	private boolean failed = false;
	private boolean hasNewDialog = true;
	private boolean cancelled = false;
	private Runnable descargaNormal = new Runnable() {
		public void run() {
			try {
				while ((readBytes = inputStream.read(sBuffer)) != -1 && !cancelled) {
					content.write(sBuffer, 0, readBytes);
				}
			} catch (IOException e) {
				failed = true;
			}
		}
	};

	public ProgressDialog getNewDialog() {
		setHasNewDialog(false);
		return pd;
	}

	public void setHasNewDialog(boolean hasNewDialog) {
		this.hasNewDialog = hasNewDialog;
	}

	public boolean isHasNewDialog() {
		return hasNewDialog;
	}

	public void destroyNewDialog() {
		if(pd != null) {
			pd.dismiss();
			pd = null;
		}
		hasNewDialog = false;
	}

	public void cancel() {
		cancelled = true;
	}

	/**
	 * Ejemplo de parametros:
	 * 
	 * 		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	 * 		nameValuePairs.add(new BasicNameValuePair("id", itemId));
	 * 		nameValuePairs.add(new BasicNameValuePair("userid", nick.getText().toString()));
	 * 		nameValuePairs.add(new BasicNameValuePair("comentario", comentario.getText().toString()));
	 * 		nameValuePairs.add(new BasicNameValuePair("voto", voto.getRating() + ""));
	 * 
	 * @param url
	 * @param parametros
	 * @param _pd
	 * @param _activity
	 * @return
	 */
	public String sendPost(String url, List<NameValuePair> parametros) {
		cancelled = false;
		hasNewDialog = false;
		if (sUserAgent  == null) {
			return "";
		}

		// Create client and set our specific user-agent string
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		try {
			request.setEntity(new UrlEncodedFormEntity(parametros));
			request.setHeader("User-Agent", sUserAgent);

			client.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);

			HttpResponse response = client.execute(request);

			// Check if server response is valid
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HTTP_STATUS_OK) {
				return "" + status.getReasonPhrase();
			}

			// Pull content stream from response
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();

			content = new ByteArrayOutputStream();

			// Read response into a buffered stream
			readBytes = 0;
				descargaNormal.run();
			if(failed || cancelled) {
				return "";
			}
			return new String(content.toByteArray());
		} catch (Exception e) {
			return "";
		}
	}

	public String sp(String url, List<NameValuePair> parametros) {
		cancelled = false;
		hasNewDialog = false;
		if (sUserAgent  == null) {
			return "";
		}

		// Create client and set our specific user-agent string
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		try {
			request.setEntity(new UrlEncodedFormEntity(parametros));
			request.setHeader("User-Agent", sUserAgent);

			HttpResponse response = client.execute(request);

			// Check if server response is valid
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HTTP_STATUS_OK) {
				return "Status: " + status.getStatusCode();
			}

			// Pull content stream from response
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();

			content = new ByteArrayOutputStream();

			// Read response into a buffered stream
			readBytes = 0;
			descargaNormal.run();
			return new String(content.toByteArray());
		} catch (Exception e) {
			Log.e(TAG, "Error: " + e.getLocalizedMessage());
			return "Error: " + e.getLocalizedMessage();
		}
	}

	/**
	 * Checks if we have a valid Internet Connection on the device.
	 * @param ctx
	 * @return True if device has internet
	 *
	 * Code from: http://www.androidsnippets.org/snippets/131/
	 */
	public static boolean haveInternet(Context ctx) {

		NetworkInfo info = ((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			// here is the roaming option you can change it if you want to
			// disable internet while roaming, just return false
			// return false;
		}
		return true;
	}

	public void downloadBinarySilent(Activity activity, String uri, String destino) {
		CookieSyncManager.createInstance(activity);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		//		cookieManager.setCookie(uri, megauploadCookie);
		cancelled = false;
		int count = 0;
		try{
			BufferedInputStream in = new BufferedInputStream(new URL(uri).openStream());
			FileOutputStream fos = new FileOutputStream(destino);
			BufferedOutputStream bout = new BufferedOutputStream(fos,8192);
			readBytes = 0;
			int size = 32768;
			byte data[] = new byte[size];
			while(count >= 0 && !cancelled)
			{
				count = in.read(data,0,size);
				if(count != -1) {
					bout.write(data,0,count);
				}
			}
			bout.close();
			fos.close();
			in.close();
		}catch(Exception ex) {
			Log.e(TAG, "Error: " + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}


	public String download(String uri) {
		return download(uri, 0);
	}

	public String download(String uri, int timeout) {
		Log.i(TAG, "Descargando: " + uri);
		String resultado = "";
		cancelled = false;
		try {
			URL url = new URL(uri);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setConnectTimeout(timeout);
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream in = httpConnection.getInputStream();
				resultado = convertStreamToString(in);
			}
		}
		catch(Exception ex) {
			Log.e(TAG, "Error download: " + ex.getMessage().toString());
		}
		if(cancelled) return "";
		return resultado;
	}

	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8192);
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null && !cancelled) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
