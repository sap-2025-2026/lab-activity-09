package ttt_game_service.infrastructure.tests;

import ttt_game_service.application.GameAlreadyPresentException;
import ttt_game_service.application.GameNotFoundException;
import ttt_game_service.application.GameService;
import ttt_game_service.application.PlayerSession;
import ttt_game_service.application.PlayerSessionEventObserver;
import ttt_game_service.domain.Game;
import ttt_game_service.domain.InvalidJoinException;
import ttt_game_service.domain.TTTSymbol;
import ttt_game_service.domain.UserId;

public class GameServiceMock implements GameService {

	private Game game;
	
	public GameServiceMock() {
	}
	
	@Override
	public Game getGameInfo(String gameId) throws GameNotFoundException {
		return new Game(gameId);
	}

	@Override
	public PlayerSession getPlayerSession(String sessionId) {
		return new PlayerSession(sessionId, new UserId("user-01"), new Game("game-01"), TTTSymbol.X);
	}

	@Override
	public void createNewGame(String gameId) throws GameAlreadyPresentException {
	}

	@Override
	public PlayerSession joinGame(UserId userId, String gameId, TTTSymbol symbol, PlayerSessionEventObserver observer)
			throws InvalidJoinException {
		var g =  new Game(gameId);
		g.joinGame(userId, symbol);
		return new PlayerSession("player-session-01", userId, g, symbol);
	}

}
