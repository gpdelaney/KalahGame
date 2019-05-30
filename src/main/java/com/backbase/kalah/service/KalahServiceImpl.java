package com.backbase.kalah.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backbase.kalah.dto.GameDTO;
import com.backbase.kalah.dto.NewGameDTO;
import com.backbase.kalah.exceptions.InvalidCupException;
import com.backbase.kalah.exceptions.InvalidGameException;
import com.backbase.kalah.exceptions.WrongPlayerException;
import com.backbase.kalah.model.Game;
import com.backbase.kalah.model.PlayerIdentifier;
import com.backbase.kalah.repository.GameRepository;

@Service
public class KalahServiceImpl implements KalahService {

	@Autowired
	private RulesService rulesService;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private SequenceService sequenceService;

	/**
	 * Creates game with id and all cups with 6 stones, by default the top player
	 * starts.
	 */
	public NewGameDTO createGame(String request) {
		long gameId = sequenceService.generateId("game");
		Game game = new Game(gameId, setUpGame(), PlayerIdentifier.TOP, false);
		gameRepository.insert(game);
		return new NewGameDTO().build(game,request);
	}

	/**
	 * Moves the stones inside the cups and saves the game after each movement, and
	 * checks if the game over conditions are met
	 * 
	 * @param {@link Long}    id - Id of the game
	 * @param {@link Integer} cup - Number of the cup to move the stones from
	 * @return {@link Game} - Game to continue
	 */
	public GameDTO move(Long id, Integer cup, String request) throws WrongPlayerException, InvalidCupException, InvalidGameException {
		Game game = gameRepository.findById(id).orElse(null);
		rulesService.checkPlayerRulesAndPosition(cup, game);
		if (game != null && !game.getFinishedGame()) {
			if(game.getGame().get(cup) == 0) {
				throw new InvalidCupException("That cup has no stones, select another one");
			}
			Integer lastPosition = moveStones(cup, game);
			rulesService.checkLastPosition(game, lastPosition);
			if (rulesService.checkEndGameConditions(game)) {
				game = endGame(game);
				game.setFinishedGame(true);
			}
			return new GameDTO().build(gameRepository.save(game), request);
		} else {
			throw new InvalidGameException("Game not found, please check the game id or start a new one");
		}
	}

	/**
	 * @param {@link Integer} cup - Selected cup to move the stones from
	 * @param {@link Game} game - Current Game played
	 * @return {@link Integer} - Cup where the last stone was deposited
	 */
	private Integer moveStones(Integer cup, Game game) {
		Integer lastPosition = 0;
		Integer stones = game.getGame().get(cup);
		// Removes all the stones
		game.getGame().put(cup, 0);
		for (int i = 1; i <= stones; i++) {
			int position = 0;
			if (cup + 1 >= 15) {
				position = lastPosition + 1;
			} else {
				position = cup + i;
			}
			// Checks that the position is less than the 14 total cups
			if (position > 14) {
				position = position - 14;
			}
			if (position != 7 && position != 14) {
				game.getGame().compute((position), (key, val) -> val + 1);
			} else {
				position = determineIfMoveSkipsOrNot(game, position);
			}
			lastPosition = position;
		}
		return lastPosition;
	}

	/**
	 * Determines if the cup should be skipped and corrects the position if thats
	 * the case
	 * 
	 * @param {@link Game} game - Current game
	 * @param int    position - current position/cup
	 * @return int - last position
	 */
	private int determineIfMoveSkipsOrNot(Game game, int position) {
		if (position == 7 && game.getPlayerIdentifier().getPlayer().equals(PlayerIdentifier.TOP.getPlayer())) {
			game.getGame().compute((position), (key, val) -> val + 1);
		} else if (position == 14
				&& game.getPlayerIdentifier().getPlayer().equals(PlayerIdentifier.BOTTOM.getPlayer())) {
			game.getGame().compute((position), (key, val) -> val + 1);
		} else {
			if (position == 14) {
				position = 1;
			} else {
				position = position + 1;
			}
			game.getGame().compute((position), (key, val) -> val + 1);
		}
		return position;
	}

	/**
	 * Collects all the stones and deposits in the correspoing kalah for each player
	 * 
	 * @param {@link Game} game - Current game
	 * @return {@link Game} game - with the final results
	 */
	private Game endGame(Game game) {
		game = addAllTopStones(game);
		game = addAllBottomStones(game);
		return game;
	}

	private Game addAllBottomStones(Game game) {
		Integer stones = 0;
		for (int i = 1; i < 7; i++) {
			stones += game.getGame().get(i);
			game.getGame().compute(i, (key, val) -> val = 0);
		}
		Integer sumStones = stones;
		game.getGame().compute(7, (key, val) -> val + sumStones);
		return game;
	}

	private Game addAllTopStones(Game game) {
		Integer stones = 0;
		for (int i = 8; i < 14; i++) {
			stones += game.getGame().get(i);
			game.getGame().compute(i, (key, val) -> val = 0);
		}
		Integer sumStones = stones;
		game.getGame().compute(14, (key, val) -> val + sumStones);
		return game;
	}

	/**
	 * Initializes de Map with all the stones in each cup
	 * 
	 * @return {@link Map} - contains a fresh game
	 */
	private Map<Integer, Integer> setUpGame() {
		Map<Integer, Integer> gameInit = new HashMap<Integer, Integer>();
		for (int i = 0; i < 14; i++) {
			if (i != 6 && i != 13) {
				gameInit.put(i + 1, 6);
			} else {
				gameInit.put(i + 1, 0);
			}
		}
		return gameInit;
	}

}
