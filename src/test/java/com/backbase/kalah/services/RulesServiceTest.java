package com.backbase.kalah.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.backbase.kalah.exceptions.InvalidCupException;
import com.backbase.kalah.exceptions.WrongPlayerException;
import com.backbase.kalah.model.Game;
import com.backbase.kalah.model.PlayerIdentifier;
import com.backbase.kalah.service.RulesService;
import com.backbase.kalah.service.RulesServiceImpl;

@RunWith(SpringRunner.class)
public class RulesServiceTest {

	@Autowired
	private RulesService rulesService;

	@TestConfiguration
	static class RuleServiceTestContextConfiguration {

		@Bean
		public RulesService rulesService() {
			return new RulesServiceImpl();
		}
	}

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

	@Test(expected = Test.None.class)
	public void shouldValidateThatCupIsForTopPlayer() throws WrongPlayerException, InvalidCupException {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.TOP, false);
		rulesService.checkPlayerRulesAndPosition(1, game);
	}

	@Test(expected = Test.None.class)
	public void shouldValidateThatCupIsForBottomPlayer() throws WrongPlayerException, InvalidCupException {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.BOTTOM, false);
		rulesService.checkPlayerRulesAndPosition(8, game);
	}

	@Test(expected = WrongPlayerException.class)
	public void shoulThrowExceptionIfTopPlayerSelectsWrongCup() throws WrongPlayerException, InvalidCupException {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.TOP, false);
		rulesService.checkPlayerRulesAndPosition(8, game);
	}

	@Test(expected = WrongPlayerException.class)
	public void shoulThrowExceptionIfBottomPlayerSelectsWrongCup() throws WrongPlayerException, InvalidCupException {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.BOTTOM, false);
		rulesService.checkPlayerRulesAndPosition(1, game);
	}

	@Test(expected = InvalidCupException.class)
	public void shouldThrowExceptionIfBottomPlayerSelectsInvalidCup() throws WrongPlayerException, InvalidCupException {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.BOTTOM, false);
		rulesService.checkPlayerRulesAndPosition(15, game);
	}

	@Test(expected = InvalidCupException.class)
	public void shoulThrowExceptionIfTopPlayerSelectsInvalidCup() throws WrongPlayerException, InvalidCupException {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.TOP, false);
		rulesService.checkPlayerRulesAndPosition(0, game);
	}

	@Test
	public void ifLastPositionIsSevenTopPlayerShouldGoAgain() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.TOP, false);
		assertThat(rulesService.checkLastPosition(game, 7)).isEqualTo(game);
	}

	@Test
	public void ifLastPositionIsSevenBottomPlayerShouldGoAgain() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.TOP, false);
		assertThat(rulesService.checkLastPosition(game, 14)).isEqualTo(game);
	}

	@Test
	public void ifPositionOneHasOneStoneTopPlayerShouldAddStonesInPositionThirteen() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.TOP, false);
		game.setGame(setCupStones(game.getGame(), 1, 1));
		assertThat(rulesService.checkLastPosition(game, 1).getGame().get(7)).isEqualTo(7);

	}

	@Test
	public void ifPositionThirteenHasOneStoneTopPlayerShouldAddStonesInPositionOne() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.TOP, false);
		game.setGame(setCupStones(game.getGame(), 1, 13));
		assertThat(rulesService.checkLastPosition(game, 13).getGame().get(7)).isEqualTo(7);

	}

	@Test
	public void ifPositionOneHasOneStoneBottomPlayerShouldAddStonesInPositionThirteen() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.BOTTOM, false);
		game.setGame(setCupStones(game.getGame(), 1, 1));
		assertThat(rulesService.checkLastPosition(game, 1).getGame().get(14)).isEqualTo(7);

	}

	@Test
	public void ifPositionThirteenHasOneStoneBottomPlayerShouldAddStonesInPositionOne() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.BOTTOM, false);
		game.setGame(setCupStones(game.getGame(), 1, 13));
		assertThat(rulesService.checkLastPosition(game, 13).getGame().get(14)).isEqualTo(7);

	}

	@Test
	public void ifPositionOneHasOneStoneBottomPlayerShouldBeNext() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.TOP, false);
		game.setGame(setCupStones(game.getGame(), 1, 1));
		assertThat(rulesService.checkLastPosition(game, 1).getPlayerIdentifier().getPlayer())
				.isEqualTo(PlayerIdentifier.BOTTOM.getPlayer());

	}

	@Test
	public void ifPositionThirteenHasOneBottomPlayerShouldBeNext() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.TOP, false);
		game.setGame(setCupStones(game.getGame(), 1, 13));
		assertThat(rulesService.checkLastPosition(game, 13).getPlayerIdentifier().getPlayer())
				.isEqualTo(PlayerIdentifier.BOTTOM.getPlayer());

	}

	@Test
	public void ifPositionOneHasOneStoneTopPlayerShouldBeNext() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.BOTTOM, false);
		game.setGame(setCupStones(game.getGame(), 1, 1));
		assertThat(rulesService.checkLastPosition(game, 1).getPlayerIdentifier().getPlayer())
				.isEqualTo(PlayerIdentifier.TOP.getPlayer());
	}

	@Test
	public void ifPositionThirteenHasOneStoneTopPlayerShouldBeNext() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.BOTTOM, false);
		game.setGame(setCupStones(game.getGame(), 1, 13));
		assertThat(rulesService.checkLastPosition(game, 13).getPlayerIdentifier().getPlayer())
				.isEqualTo(PlayerIdentifier.TOP.getPlayer());
	}

	@Test
	public void ifNoSpecialRuleAppliesForTopPlayerNextPlayerShouldGo() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.TOP, false);
		assertThat(rulesService.checkLastPosition(game, 1).getPlayerIdentifier().getPlayer())
				.isEqualTo(PlayerIdentifier.BOTTOM.getPlayer());
	}

	@Test
	public void ifNoSpecialRuleAppliesForBottomPlayerNextPlayerShouldGo() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.BOTTOM, false);
		assertThat(rulesService.checkLastPosition(game, 13).getPlayerIdentifier().getPlayer())
				.isEqualTo(PlayerIdentifier.TOP.getPlayer());
	}

	@Test
	public void gameShouldContinueIfNoPlayerHasAllCupsEmpty() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.BOTTOM, false);
		assertThat(rulesService.checkEndGameConditions(game)).isFalse();
	}

	@Test
	public void gameShouldNotContinueIfBottomPlayerHasAllCupsEmpty() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.BOTTOM, false);
		for (int i = 8; i < 14; i++) {
			game.getGame().compute(i, (key, val) -> val = 0);
		}
		assertThat(rulesService.checkEndGameConditions(game)).isTrue();
	}

	@Test
	public void gameShouldNotContinueIfTopPlayerHasAllCupsEmpty() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.TOP, false);
		for (int i = 1; i < 7; i++) {
			game.getGame().compute(i, (key, val) -> val = 0);
		}
		assertThat(rulesService.checkEndGameConditions(game)).isTrue();
	}

	@Test
	public void gameShouldContinueIfTopPlayerHasAllSomeCupsEmpty() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.TOP, false);
		for (int i = 4; i < 7; i++) {
			game.getGame().compute(i, (key, val) -> val = 0);
		}
		assertThat(rulesService.checkEndGameConditions(game)).isFalse();
	}

	@Test
	public void gameShouldContinueIfBottomPlayerHasAllSomeCupsEmpty() {
		Game game = new Game(Long.getLong("1"), setUpGame(), PlayerIdentifier.BOTTOM, false);
		for (int i = 9; i < 14; i++) {
			game.getGame().compute(i, (key, val) -> val = 0);
		}
		assertThat(rulesService.checkEndGameConditions(game)).isFalse();
	}

	private Map<Integer, Integer> setCupStones(Map<Integer, Integer> game, Integer stones, Integer cup) {
		game.compute(cup, (key, val) -> val = stones);
		return game;
	}
}
