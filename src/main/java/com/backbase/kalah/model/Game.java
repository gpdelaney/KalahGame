package com.backbase.kalah.model;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Game {

	public Game(Long id, Map<Integer, Integer> game, PlayerIdentifier playerIdentifier, Boolean finishedGame) {
		super();
		this.id = id;
		this.game = game;
		this.playerIdentifier = playerIdentifier;
		this.finishedGame =  finishedGame;
	}

	public Game(Long id) {
		super();
		this.id = id;
	}

	public Game() {
	}

	private Long id;

	private Map<Integer, Integer> game;

	private PlayerIdentifier playerIdentifier;
	
	private Boolean finishedGame;

	public Boolean getFinishedGame() {
		return finishedGame;
	}

	public void setFinishedGame(Boolean finishedGame) {
		this.finishedGame = finishedGame;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<Integer, Integer> getGame() {
		return game;
	}

	public void setGame(Map<Integer, Integer> game) {
		this.game = game;
	}

	public PlayerIdentifier getPlayerIdentifier() {
		return playerIdentifier;
	}

	public void setPlayerIdentifier(PlayerIdentifier playerIdentifier) {
		this.playerIdentifier = playerIdentifier;
	}
}
