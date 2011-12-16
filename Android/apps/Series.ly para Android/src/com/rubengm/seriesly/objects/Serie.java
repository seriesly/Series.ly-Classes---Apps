package com.rubengm.seriesly.objects;

import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.gson.Gson;

public class Serie {
	private String idSerie;
	private String ids;
	private String title;
	private int seasons;
	private int episodes;
	private String poster;
	private String thumb;
	private String small_thumb;
	private String status;
	private String synopsis;
	private int seriesly_score;
	private int participants_score;
	private Episode[] episode;
	private UrlImageView urlImageView = null;

	public Serie() {}

	public String getIdSerie() {
		return idSerie;
	}

	public void setIdSerie(String idSerie) {
		this.idSerie = idSerie;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSeasons() {
		return seasons;
	}

	public void setSeasons(int seasons) {
		this.seasons = seasons;
	}

	public int getEpisodes() {
		return episodes;
	}

	public void setEpisodes(int episodes) {
		this.episodes = episodes;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getSmall_thumb() {
		return small_thumb;
	}

	public void setSmall_thumb(String small_thumb) {
		this.small_thumb = small_thumb;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public int getSeriesly_score() {
		return seriesly_score;
	}

	public void setSeriesly_score(int seriesly_score) {
		this.seriesly_score = seriesly_score;
	}

	public int getParticipants_score() {
		return participants_score;
	}

	public void setParticipants_score(int participants_score) {
		this.participants_score = participants_score;
	}

	public Episode[] getEpisode() {
		if(episode == null) episode = new Episode[0];
		return episode;
	}

	public void setEpisode(Episode[] episode) {
		this.episode = episode;
	}

	public UrlImageView getImagen(Activity a) {
		if(urlImageView == null) {
			urlImageView = new UrlImageView(a, null);
		}
		urlImageView.setImageURL(thumb);
		try {
			LinearLayout v = (LinearLayout) urlImageView.getParent();
			if(v != null) {
				v.removeView(urlImageView);
			}
		} catch (Exception e) {
			Log.e(getClass().getName().toString(), "Error: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return urlImageView;
	}

	public UrlImageView getImagenGrande(Activity a) {
		if(urlImageView == null) {
			urlImageView = new UrlImageView(a, null);
		}
		urlImageView.setImageURL(poster);
		return urlImageView;
	}

	@Override
	public String toString() {
		urlImageView = null;
		return new Gson().toJson(this);
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getIds() {
		return ids;
	}
}