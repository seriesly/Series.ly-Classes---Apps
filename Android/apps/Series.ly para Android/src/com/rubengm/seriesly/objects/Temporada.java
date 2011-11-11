package com.rubengm.seriesly.objects;

import java.util.ArrayList;

public class Temporada {
	private String title;
	private ArrayList<Episode> episodios;
	
	public Temporada() {
		title = "";
		episodios = new ArrayList<Episode>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<Episode> getEpisodios() {
		return episodios;
	}

	public void setEpisodios(ArrayList<Episode> episodios) {
		this.episodios = episodios;
	}
	
	public int size() {
		return episodios.size();
	}
	
	public Episode get(int position) {
		return episodios.get(position);
	}
	
	public void add(Episode e) {
		episodios.add(e);
	}
}
