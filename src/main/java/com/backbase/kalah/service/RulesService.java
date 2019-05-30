package com.backbase.kalah.service;

import com.backbase.kalah.exceptions.InvalidCupException;
import com.backbase.kalah.exceptions.WrongPlayerException;
import com.backbase.kalah.model.Game;

public interface RulesService {
	
	public Boolean checkEndGameConditions(Game game);
	
	public void checkPlayerRulesAndPosition(Integer cup, Game game) throws WrongPlayerException, InvalidCupException;

	public Game checkLastPosition(Game game, Integer position);	
}
