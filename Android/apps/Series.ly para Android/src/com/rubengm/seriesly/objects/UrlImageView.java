package com.rubengm.seriesly.objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rubengm.seriesly.R;

public class UrlImageView extends ImageView {
	public static final String TAG = "UrlImageView";
	protected String url;
	protected Downloader downloader;
	protected Bitmap bm;
	protected Activity activity;
	protected String path;
	protected String hash;
	protected String fullpath;
	protected boolean isCached = false;
	protected int defaultIcon;
	protected boolean hasDefaultIcon = false;
	protected boolean isDone = false;
	protected int parentId;
	protected int height = -1;
	protected int width = -1;
	protected boolean isScaled = false;
	protected Runnable pinta = new Runnable() {

		public void run() {
			if(!isScaled) setImageBitmap(bm);
			else setImageBitmap(Bitmap.createScaledBitmap(bm, width, height, true));
			isDone = true;
		}
	};

	public UrlImageView(Activity context, AttributeSet attrs) {
		super(context, attrs);
		activity = context;
		path = getPath(context);
		File f = new File(path);
		if(!f.mkdirs()) {
			Log.e(TAG, "Directory not created");
		}
	}
	
	public static String getPath(Context context) {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/" + context.getPackageName() + "/cache/";
	}

	public void setPId(int _id) {
		parentId = _id;
	}

	public int getPId() {
		return parentId;
	}

	public boolean hasUrl() {
		return !url.equals("");
	}

	public UrlImageView setImageURL(String uri) {
		url = uri;
		if(uri == null) {
			setImageResource(defaultIcon);
			return this;
		}
		else if(uri.equals("") && hasDefaultIcon) {
			setImageResource(defaultIcon);
			return this;
		}
		try {
			hash = getHash(uri);
			fullpath = path + hash;
		} catch (NoSuchAlgorithmException e) {
			bm = getError();
			isDone = true;
			setImageBitmap(getError());
			return this;
		}
		isCached = isCached();
		if(isCached) {
			drawFromCache();
			return this;
		}
		setImageBitmap(getDownloading());
		downloader = new Downloader();
		try{
			downloader.execute();
		}
		catch(Exception ex) {
			bm = getError();
			isDone = true;
			setImageBitmap(getError());
			return this;
		}
		return this;
	}

	public void stopDownloader() {
		if(downloader != null) {
			downloader.cancel(true);
			downloader = null;
		}
	}

	public void setMaxSize(int width, int height) {
		setMaxWidth(width);
		setMaxHeight(height);
	}

	private void Download(String uri) throws MalformedURLException {
		URL url = new URL(uri);
		try{
			bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		}catch(IOException ex) {
			bm = getError();
		}
	}

	private Bitmap getError() {
		if(hasDefaultIcon) return BitmapFactory.decodeResource(getResources(), defaultIcon);
		else return BitmapFactory.decodeResource(getResources(), R.drawable.icon);
	}

	private Bitmap getDownloading() {
		if(hasDefaultIcon) return BitmapFactory.decodeResource(getResources(), defaultIcon);
		else return BitmapFactory.decodeResource(getResources(), R.drawable.icon);
	}

	private class Downloader extends AsyncTask<Void, Void, Void> {
		private boolean isCancelled = false;
		@Override
		protected void onCancelled() {
			isCancelled = true;
			super.onCancelled();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Download(url);
				if(!isCached && !isCancelled) {
					saveToCache();
				}
			} catch (MalformedURLException e) {
				bm = getError();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(!isCancelled)
				activity.runOnUiThread(pinta);
		}

	}

	private boolean isCached() {
		if(fullpath == null) return false;
		File cache = new File(fullpath);
		return cache.length() > 0;
	}

	private void drawFromCache() {
		try{	
			bm = BitmapFactory.decodeFile(fullpath);
		}catch(OutOfMemoryError ex) {
			setBm(getError());
		}
		activity.runOnUiThread(pinta);
	}

	protected void saveToCache() throws FileNotFoundException {
		try{
			FileOutputStream out = new FileOutputStream(new File(fullpath));
			bm.compress(Bitmap.CompressFormat.PNG, 70, out);
		}catch(NullPointerException ex) {
			//No hagamos nada :/
			setBm(getError());
			activity.runOnUiThread(pinta);
		}
	}

	public static String getHash(String uri) throws NoSuchAlgorithmException {
		MessageDigest mDigest=MessageDigest.getInstance("MD5");

		mDigest.update(uri.getBytes());

		byte d[]=mDigest.digest();
		StringBuffer hash=new StringBuffer();

		for (int i=0; i<d.length; i++) {
			hash.append(Integer.toHexString(0xFF & d[i]));
		}
		return hash.toString();
	}

	public void setDefaultIcon(int defaultIcon) {
		this.defaultIcon = defaultIcon;
		hasDefaultIcon = true;
	}

	public boolean isDone() {
		return isDone;
	}

	public void repinta() {
		setImageBitmap(bm);
	}

	public void setBm(Bitmap b) {
		bm = b;
	}

	public Bitmap getBm() {
		return bm;
	}

	public void copy(UrlImageView imageView) {
		stopDownloader();
		downloader = imageView.downloader;
		stopDownloader();
		bm = imageView.bm;
		activity = imageView.activity;
		path = imageView.path;
		hash = imageView.hash;
		fullpath = imageView.fullpath;
		isCached = imageView.isCached;
		defaultIcon = imageView.defaultIcon;
		hasDefaultIcon = imageView.hasDefaultIcon;
		isDone = imageView.isDone;
		parentId = imageView.parentId;
		setImageURL(imageView.url);
	}

	public void reload() {
		File f = new File(fullpath);
		if(f.exists()) { 
			if(!f.delete()) {
				Log.e(TAG, "Could not delete the file");
			}
		}
		setImageURL(url);
	}

	public String getUrl() {
		return url;
	}

	public UrlImageView fill_parent() {
		setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		return this;
	}

}