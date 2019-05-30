package com.backbase.kalah.dto;

import com.backbase.kalah.model.Game;

public class NewGameDTO {
	
	private String id;
	private String uri;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String url) {
		this.uri = url;
	}
	
	public NewGameDTO build(Game game, String request) {
		this.id = game.getId().toString();
		this.uri =request.concat("/".concat(this.id));
		return this;
	}
}
