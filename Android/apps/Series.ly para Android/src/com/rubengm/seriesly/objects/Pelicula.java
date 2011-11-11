package com.rubengm.seriesly.objects;

public class Pelicula {
	private String idFilm;
	private String title;
	private int year;
	private String genre;
	private String postr;
	private String thumb;
	private String small_thumb;
	private String status;
	private String synopsis;
	private int seriesly_score;
	private int participants_score;

	public Pelicula() {}

	public String getIdFilm() {
		return idFilm;
	}

	public void setIdFilm(String idFilm) {
		this.idFilm = idFilm;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getPostr() {
		return postr;
	}

	public void setPostr(String postr) {
		this.postr = postr;
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
}
