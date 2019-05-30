package com.backbase.kalah.service;

import com.backbase.kalah.dto.GameDTO;
import com.backbase.kalah.dto.NewGameDTO;
import com.backbase.kalah.exceptions.InvalidCupException;
import com.backbase.kalah.exceptions.InvalidGameException;
import com.backbase.kalah.exceptions.WrongPlayerException;

public interface KalahService {

	public NewGameDTO createGame(String requestUrl);

	public GameDTO move(Long id, Integer cup, String request) throws WrongPlayerException, InvalidCupException, InvalidGameException;

}
