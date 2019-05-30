package com.backbase.kalah.service;

import org.springframework.stereotype.Service;

import com.backbase.kalah.exceptions.InvalidCupException;
import com.backbase.kalah.exceptions.WrongPlayerException;
import com.backbase.kalah.model.Game;
import com.backbase.kalah.model.PlayerIdentifier;

@Service
public class RulesServiceImpl implements RulesService{

	/**
	 *Checks if the game has ended (a player no longer has stones in the cups)
	 */
	public Boolean checkEndGameConditions(Game game) {
		if(checkTopCups(game) || checkBottomCups(game)) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * Validates the player input, if invalid
	 * throws an exception.
	 * 
	 * @param Integer cup - Cup position
	 * @param Game game - game information
	 *
	 */
	public void checkPlayerRulesAndPosition(Integer cup, Game game) throws WrongPlayerException, InvalidCupException {
		if (game.getPlayerIdentifier().getPlayer().equals(PlayerIdentifier.TOP.getPlayer())) {
			if (cup >= 7) {
				throw new WrongPlayerException("Top player should choose a cup between 1 and 6");
			} else if (cup <= 0) {
				throw new InvalidCupException("Invalid Cup, please choose again");
			}
		} else if (game.getPlayerIdentifier().getPlayer().equals(PlayerIdentifier.BOTTOM.getPlayer())) {
			if (cup <= 7) {
				throw new WrongPlayerException("Bottom player should choose a cup between 8 and 13");
			} else if (cup >= 14) {
				throw new InvalidCupException("Invalid Cup, please choose again");
			}
		}
	}
	

	/**
	 * Checks the last cup where stones were inserted. If a the last stone ended in the players kalah
	 * then that player goes again. If the last stone ended in a cup which has only one stone in total,
	 * the player adds to the kalah that stone and the ones in the opposite cup.
	 * 
	 * @param Game game - The game information
	 * @param Integer position - The last position/cup
	 * 
	 * @return Game game - the updated game info 
	 *
	 */
	public Game checkLastPosition(Game game, Integer position) {
		if (position == 7 && position == 14) {
			return game;
		}if (game.getGame().get(position) == 1 && (position != 7 && position != 14)) {
			if (position < 7) {
				Integer stones = game.getGame().get(Math.abs(position - 14));
				game.getGame().compute(position, (key, val) -> val = 0);
				game.getGame().compute(Math.abs(position - 14), (key, val) -> val = 0);
				if (game.getPlayerIdentifier().getPlayer().equals(PlayerIdentifier.TOP.getPlayer())) {
					game.getGame().compute(7, (key, val) -> val + stones + 1);
				} else {
					game.getGame().compute(14, (key, val) -> val + stones + 1);
				}
				game.setPlayerIdentifier(PlayerIdentifier.getNextPlayer(game.getPlayerIdentifier()));
			}else {
				Integer stones = game.getGame().get(Math.abs(position - 14));
				game.getGame().compute(position, (key, val) -> val = 0);
				game.getGame().compute(Math.abs(position - 14), (key, val) -> val = 0);
				if (game.getPlayerIdentifier().getPlayer().equals(PlayerIdentifier.TOP.getPlayer())) {
					game.getGame().compute(7, (key, val) -> val + stones + 1);
				} else {
					game.getGame().compute(14, (key, val) -> val + stones + 1);
				}
				game.setPlayerIdentifier(PlayerIdentifier.getNextPlayer(game.getPlayerIdentifier()));
			}
		}else if (position != 7 && position != 14) {
			game.setPlayerIdentifier(PlayerIdentifier.getNextPlayer(game.getPlayerIdentifier()));
		}
		return game;
	}
	
	private Boolean checkTopCups(Game game) {
		Boolean endGame = true;
		for (int i = 1; i < 7; i++) {
			if(game.getGame().get(i) > 0) {
				endGame = false;
			}
		}
		return endGame;
	}
	
	private Boolean checkBottomCups(Game game) {
		Boolean endGame = true;
		for (int i = 8; i < 14; i++) {
			if(game.getGame().get(i) > 0) {
				endGame = false;
			}
		}
		return endGame;
	}

}
