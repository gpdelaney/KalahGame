package com.backbase.kalah.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.backbase.kalah.dto.GameDTO;
import com.backbase.kalah.dto.NewGameDTO;
import com.backbase.kalah.exceptions.InvalidCupException;
import com.backbase.kalah.exceptions.InvalidGameException;
import com.backbase.kalah.exceptions.WrongPlayerException;
import com.backbase.kalah.model.Game;
import com.backbase.kalah.model.PlayerIdentifier;
import com.backbase.kalah.repository.GameRepository;
import com.backbase.kalah.service.KalahService;
import com.backbase.kalah.service.KalahServiceImpl;
import com.backbase.kalah.service.RulesService;
import com.backbase.kalah.service.RulesServiceImpl;
import com.backbase.kalah.service.SequenceService;
import com.backbase.kalah.service.SequenceServiceImpl;

@RunWith(SpringRunner.class)
@DataMongoTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KalahServiceTest {

	private static final String URI = "http://localhost:8080/games/1/pits/1";

	@Autowired
	private KalahServiceImpl kalahService;

	@Autowired
	private GameRepository gameRepository;

	@TestConfiguration
	static class DifferServiceImplTestContextConfiguration {

		@Bean
		public KalahService kalahService() {
			return new KalahServiceImpl();
		}

		@Bean
		public RulesService rulesService() {
			return new RulesServiceImpl();
		}

		@Bean
		public SequenceService sequenceService() {
			return new SequenceServiceImpl();
		}

	}

	@Test
	public void test1_gameGetsCreatedForTheFirstTimeSoIdShouldBeOne() {
		NewGameDTO dto = kalahService.createGame("");
		assertThat(dto.getId()).isEqualTo("1");
	}

	@Test
	public void test2_gameGetsCreatedForTheSecondTimeSoIdShouldBeTwo() {
		NewGameDTO dto = kalahService.createGame("");
		assertThat(dto.getId()).isEqualTo("2");
	}

	@Test
	public void test3_playerMovesCupOneAndSevenAddsOne()
			throws WrongPlayerException, InvalidCupException, InvalidGameException {
		GameDTO gameDTO = kalahService.move(Long.valueOf(1), 1, URI);
		assertThat(gameDTO.getStatus().get("7")).isEqualTo("1");
		assertThat(gameDTO.getStatus().get("1")).isEqualTo("0");
		assertThat(gameDTO.getStatus().get("8")).isEqualTo("6");
	}

	@Test(expected = WrongPlayerException.class)
	public void test4_bottomPlayerMovesCupWhenItsNotCorrect()
			throws WrongPlayerException, InvalidCupException, InvalidGameException {
		kalahService.move(Long.valueOf(1), 8, URI);
	}

	@Test
	public void test5_topPlayerMovesCupTwoAsIsTurnAgainAndSevenAddsOne()
			throws WrongPlayerException, InvalidCupException, InvalidGameException {
		GameDTO gameDTO = kalahService.move(Long.valueOf(1), 2, URI);
		assertThat(gameDTO.getStatus().get("7")).isEqualTo("2");
		assertThat(gameDTO.getStatus().get("2")).isEqualTo("0");
		assertThat(gameDTO.getStatus().get("8")).isEqualTo("7");
	}

	@Test(expected = WrongPlayerException.class)
	public void test5_topPlayerMovesCupWhenItsNotCorrect()
			throws WrongPlayerException, InvalidCupException, InvalidGameException {
		kalahService.move(Long.valueOf(1), 4, URI);
	}

	@Test
	public void test6_bottomPlayerMovesEightAndStealsAndAddsNineToFourteen()
			throws WrongPlayerException, InvalidCupException, InvalidGameException {
		GameDTO gameDTO = kalahService.move(Long.valueOf(1), 8, URI);
		assertThat(gameDTO.getStatus().get("14")).isEqualTo("9");
		assertThat(gameDTO.getStatus().get("8")).isEqualTo("0");
		assertThat(gameDTO.getStatus().get("1")).isEqualTo("0");
		assertThat(gameDTO.getStatus().get("2")).isEqualTo("0");
	}

	@Test
	public void test7_gameEndsAndBottomPlayerWins()
			throws WrongPlayerException, InvalidCupException, InvalidGameException {
		gameRepository.save(new Game(Long.valueOf(4), setUpGameToWin(), PlayerIdentifier.BOTTOM, false));
		GameDTO gameDTO = kalahService.move(Long.valueOf(4), 8, URI);
		assertThat(gameDTO.getStatus().get("14")).isEqualTo("42");
		assertThat(gameDTO.getStatus().get("7")).isEqualTo("30");
		gameRepository.deleteById(Long.valueOf(4));
	}

	private Map<Integer, Integer> setUpGameToWin() {
		Map<Integer, Integer> gameInit = new HashMap<Integer, Integer>();
		for (int i = 0; i < 7; i++) {
			if (i != 6) {
				gameInit.put(i + 1, 6);
			} else {
				gameInit.put(i + 1, 0);
			}
		}
		for (int i = 7; i < 14; i++) {
			if (i != 13 && i < 8) {
				gameInit.put(i + 1, 1);
			} else if (i == 13) {
				gameInit.put(i + 1, 35);
			} else {
				gameInit.put(i + 1, 0);
			}
		}
		return gameInit;
	}

}
