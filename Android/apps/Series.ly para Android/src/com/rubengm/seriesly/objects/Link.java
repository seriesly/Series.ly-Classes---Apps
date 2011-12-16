package com.rubengm.seriesly.objects;

import com.google.gson.Gson;

public class Link {
	private String language = "";
	private String subtitles = "";
	private int hd = -1;
	private String url_cineraculo = "";
	private String url_megavideo = "";
	private String desc = null;

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

	public CharSequence getDescription() {
		if(desc != null) {
			return desc;
		}
		StringBuilder desc = new StringBuilder();
		desc.append(language);
		if(!"no".equals(subtitles)) {
			desc.append(", Subtitulado: ");
			desc.append(subtitles);
		}
		if(hd != 0) desc.append(", [HD]");
		this.desc = desc.toString();
		return desc.toString();
	}
}
