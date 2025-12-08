package client.utility;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;

public class MyPlayerStateTest {

	List<PlayerState> playerStates = new ArrayList<>();
	UniquePlayerIdentifier playerIdOne;
	UniquePlayerIdentifier playerIdTwo;
	GameState dummyGameState;
	PlayerState playerStateOne;
	PlayerState playerStateTwo;
	MyPlayerState myPlayerState;

	@BeforeEach
	private void setUp() {
		playerIdOne = UniquePlayerIdentifier.random();
		playerIdTwo = UniquePlayerIdentifier.random();
		playerStateOne = new PlayerState("Benedikt", "Adler", "adlerb88", EPlayerGameState.MustWait, playerIdOne,
				false);
		playerStateTwo = new PlayerState("Max", "Mustermann", "maxi", EPlayerGameState.MustAct, playerIdTwo, false);
		playerStates.add(playerStateOne);
		playerStates.add(playerStateTwo);
		dummyGameState = new GameState(playerStates, "id");
		myPlayerState = new MyPlayerState(dummyGameState, playerIdOne.getUniquePlayerID());
	}

	@Test
	void givenGameStateAndPlayerId_searchForMyPlayer_ReturnsCorrectPlayerState() throws Exception {

		Assertions.assertEquals(playerIdOne.getUniquePlayerID(), myPlayerState.getPlayerId());
		Assertions.assertEquals(EPlayerGameState.MustWait, myPlayerState.getPlayerState());
		Assertions.assertFalse(myPlayerState.hasTreasure());

	}

	@Test
	void MyPlayerStateConstructor_throwsException_whenParameterNull() {

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new MyPlayerState(null, null);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new MyPlayerState(dummyGameState, null);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new MyPlayerState(null, "adlerb88");
		});
	}

	@Test
	void myPlayerFindsTreasure_updatesMyPlayerState_differentFromBefore() {

		playerStateOne = new PlayerState("Benedikt", "Adler", "adlerb88", EPlayerGameState.MustAct, playerIdOne, true);

		List<PlayerState> newPlayerStates = new ArrayList<>();

		newPlayerStates.add(playerStateOne);
		newPlayerStates.add(playerStateTwo);

		dummyGameState = new GameState(newPlayerStates, "id");
		MyPlayerState updatedMyPlayerState = myPlayerState.updateGameState(dummyGameState,
				playerIdOne.getUniquePlayerID());

		Assertions.assertNotSame(myPlayerState, updatedMyPlayerState);
	}

	@Test
	void myPlayerFindsTreasure_updatesMyPlayerState_hasTreasureIsTrue() {

		playerStateOne = new PlayerState("Benedikt", "Adler", "adlerb88", EPlayerGameState.MustAct, playerIdOne, true);

		List<PlayerState> newPlayerStates = new ArrayList<>();

		newPlayerStates.add(playerStateOne);
		newPlayerStates.add(playerStateTwo);

		dummyGameState = new GameState(newPlayerStates, "id");
		MyPlayerState updatedMyPlayerState = myPlayerState.updateGameState(dummyGameState,
				playerIdOne.getUniquePlayerID());

		Assertions.assertTrue(updatedMyPlayerState.hasTreasure());
	}

}
