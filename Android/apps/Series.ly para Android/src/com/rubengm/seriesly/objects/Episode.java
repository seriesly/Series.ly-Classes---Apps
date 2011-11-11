package com.rubengm.seriesly.objects;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class Episode implements InstanceCreator<Episode> {
	private String idc = "";
	private String title = "";
	private String season = "";
	private int viewed = -1;
	private Link[] links;
	
	public Episode() {
		super();
	}

	public Episode(String idc, String title, String season, int viewed) {
		super();
		this.idc = idc;
		this.title = title;
		this.season = season;
		this.viewed = viewed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public int getViewed() {
		return viewed;
	}

	public void setViewed(int viewed) {
		this.viewed = viewed;
	}

	public Link[] getLinks() {
		return links;
	}

	public void setLinks(Link[] links) {
		this.links = links;
	}

	@Override
	public Episode createInstance(Type arg0) {
		return new Episode();
	}

	public void setIdc(String idc) {
		this.idc = idc;
	}

	public String getIdc() {
		return idc;
	}
}
