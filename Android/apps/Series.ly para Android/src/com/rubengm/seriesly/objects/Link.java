package com.rubengm.seriesly.objects;

import com.google.gson.Gson;

public class Link {
	private String language = "";
	private String subtitles = "";
	private int hd = -1;
	private String url_cineraculo = "";
	private String url_megavideo = "";
	
	public Link() {
		super();
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSubtitles() {
		return subtitles;
	}

	public void setSubtitles(String subtitles) {
		this.subtitles = subtitles;
	}

	public int getHd() {
		return hd;
	}

	public void setHd(int hd) {
		this.hd = hd;
	}

	public String getUrl_cineraculo() {
		return url_cineraculo;
	}

	public void setUrl_cineraculo(String url_cineraculo) {
		this.url_cineraculo = url_cineraculo;
	}

	public String getUrl_megavideo() {
		return url_megavideo;
	}

	public void setUrl_megavideo(String url_megavideo) {
		this.url_megavideo = url_megavideo;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
