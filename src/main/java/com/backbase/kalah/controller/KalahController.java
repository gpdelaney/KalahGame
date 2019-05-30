package com.backbase.kalah.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.backbase.kalah.dto.GameDTO;
import com.backbase.kalah.dto.NewGameDTO;
import com.backbase.kalah.exceptions.InvalidCupException;
import com.backbase.kalah.exceptions.InvalidGameException;
import com.backbase.kalah.exceptions.WrongPlayerException;
import com.backbase.kalah.service.KalahService;

@RestController
public class KalahController {
	
	@Autowired
	private KalahService kalahService;

	@RequestMapping(value = "/games", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public NewGameDTO createGame(HttpServletRequest request) {
		return kalahService.createGame(request.getRequestURL().toString());
	}

	@RequestMapping(value = "/games/{gameId}/pits/{pitId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public GameDTO moveStone(@PathVariable(value = "gameId") Long gameId, @PathVariable(value = "pitId") Integer pitId, HttpServletRequest request) {
		try {
			return kalahService.move(gameId, pitId, request.getRequestURL().toString());
		} catch (WrongPlayerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		} catch (InvalidCupException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		} catch (InvalidGameException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
		}
	}

}
