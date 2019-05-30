package com.backbase.kalah.dto;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import com.backbase.kalah.model.Game;
import com.backbase.kalah.utils.UriUtils;

public class GameDTO extends NewGameDTO{

	private Map<String, String> status;
	

	public Map<String, String> getStatus() {
		return status;
	}

	public void setStatus(Map<String, String> status) {
		this.status = status;
	}


	public GameDTO build(Game game, String request) {
		this.setId(String.valueOf(game.getId()));
		this.setUri(UriUtils.trimUrl(request));
		this.status = new TreeMap<String, String>(Comparator.comparingInt(Integer::parseInt));
		for (int i = 0; i < 14; i++) {
			this.status.put(String.valueOf(i+1), String.valueOf(game.getGame().get(i+1)));
		}
		return this;
	}
	
}
