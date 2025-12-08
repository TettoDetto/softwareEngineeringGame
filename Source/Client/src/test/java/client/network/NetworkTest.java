package client.network;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import client.map.MapHalfGenerator;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.GameState;

public class NetworkTest {

	private INetwork dummyNetwork;
	private GameServer server;

	@BeforeEach
	private void setUp() {
		dummyNetwork = Mockito.mock(INetwork.class);
		server = new GameServer(dummyNetwork);
	}

	@Test
	public void givenName_registerPlayer_shouldReturnPlayerIDWhenSuccessfull() {

		UniquePlayerIdentifier dummyId = UniquePlayerIdentifier.random();

		Mockito.when(dummyNetwork.registerPlayer("Benedikt", "Adler", "adlerb88")).thenReturn(dummyId);

		UniquePlayerIdentifier playerReg = server.registerPlayer("Benedikt", "Adler", "adlerb88");

		assertThat(dummyId, is(equalTo(playerReg)));
		Mockito.verify(dummyNetwork).registerPlayer("Benedikt", "Adler", "adlerb88");
	}

	@Test
	public void givenNoName_registerPlayer_shouldReturnFalse() {

		Mockito.when(dummyNetwork.registerPlayer(null, null, null)).thenReturn(null);

		UniquePlayerIdentifier playerReg = server.registerPlayer(null, null, null);

		assertThat(playerReg, is(equalTo(null)));
		Mockito.verify(dummyNetwork).registerPlayer(null, null, null);
	}

	@Test
	public void noGameState_getGameState_shouldReturnValidGameState() throws Exception {

		GameState dummyGameState = new GameState();

		Mockito.when(dummyNetwork.getGameState()).thenReturn(dummyGameState);

		GameState gameState = server.getGameState();

		Assertions.assertEquals(dummyGameState, gameState,
				"The dummy game state is different from the actual game state");
		Mockito.verify(dummyNetwork).getGameState();

	}

	@ParameterizedTest
	@MethodSource("movementDirections")
	public void currentPosition_sendPlayerMove_returnTrueIfValidMove(boolean answer, String direction) {

		when(dummyNetwork.sendPlayerMove(any())).thenReturn(answer);

		UniquePlayerIdentifier dummyId = UniquePlayerIdentifier.random();
		boolean successfulMove = server.sendPlayerMove(dummyId.getUniquePlayerID(), direction);

		assertThat(successfulMove, is(equalTo(answer)));
		Mockito.verify(dummyNetwork).sendPlayerMove(any());

	}

	@Test
	public void afterMapCreation_sendHalfMap_returnAnswerFromServer() {
		MapHalfGenerator mapHalf = new MapHalfGenerator();
		Mockito.when(dummyNetwork.sendMapHalf(mapHalf)).thenReturn(true);

		assertThat(server.sendMapHalf(mapHalf), is(true));

		Mockito.verify(dummyNetwork).sendMapHalf(mapHalf);

	}

	private static Stream<Arguments> movementDirections() {
		return Stream.of(Arguments.of(true, "Up"), Arguments.of(true, "Down"), Arguments.of(true, "Left"),
				Arguments.of(true, "Right"));
	}

}
