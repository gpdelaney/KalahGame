package com.backbase.kalah.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KalahControllerTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void test1_aNewGameIsCreatedIdShouldBeOneAndStatusOk() {
		String url = "/games";
		HttpEntity<String> entity = new HttpEntity<String>("");
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("/games/1");
	}

	@Test
	public void test2_bottomPlayerMakesMoveButExceptionAsNotHisTurn() {
		String url = "/games/1/pits/8";
		HttpEntity<String> entity = new HttpEntity<String>("");
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("/games/1");
	}

	@Test
	public void test3_topPlayerMovesAndAddsStonesExceptCupOne() {
		String url = "/games/1/pits/1";
		HttpEntity<String> entity = new HttpEntity<String>("");
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains(
				"status\":{\"1\":\"0\",\"2\":\"7\",\"3\":\"7\",\"4\":\"7\",\"5\":\"7\",\"6\":\"7\",\"7\":\"1\",\"8\":\"6\",\"9\":\"6\",\"10\":\"6\",\"11\":\"6\",\"12\":\"6\",\"13\":\"6\",\"14\":\"0");
	}

	@Test
	public void test4_topPlayerMovesAgainAndMakesInvalidMoveAsOneHasNoStones() {
		String url = "/games/1/pits/1";
		HttpEntity<String> entity = new HttpEntity<String>("");
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void test5_bottomPLayerMovesAndWrongTurn() {
		String url = "/games/1/pits/1";
		HttpEntity<String> entity = new HttpEntity<String>("");
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void test6_topPlayerMovesAgainAndAddsStonesExceptCupTwo() {
		String url = "/games/1/pits/2";
		HttpEntity<String> entity = new HttpEntity<String>("");
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		System.out.println(response.getBody());
		assertThat(response.getBody()).contains(
				"status\":{\"1\":\"0\",\"2\":\"0\",\"3\":\"8\",\"4\":\"8\",\"5\":\"8\",\"6\":\"8\",\"7\":\"2\",\"8\":\"7\",\"9\":\"7\",\"10\":\"6\",\"11\":\"6\",\"12\":\"6\",\"13\":\"6\",\"14\":\"0");
	}
	
	@Test
	public void test7_topPlayerMovesAgainAndWrongTurn() {
		String url = "/games/1/pits/2";
		HttpEntity<String> entity = new HttpEntity<String>("");
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}	
	@Test
	public void test8_bottomPlayerMoveAndAddsStonesExceptCupEightAndAddsToCupFourteen() {
		String url = "/games/1/pits/8";
		HttpEntity<String> entity = new HttpEntity<String>("");
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		System.out.println(response.getBody());
		assertThat(response.getBody()).contains(
				"status\":{\"1\":\"0\",\"2\":\"0\",\"3\":\"8\",\"4\":\"8\",\"5\":\"8\",\"6\":\"8\",\"7\":\"2\",\"8\":\"0\",\"9\":\"8\",\"10\":\"7\",\"11\":\"7\",\"12\":\"7\",\"13\":\"0\",\"14\":\"9");
	}
	
}
