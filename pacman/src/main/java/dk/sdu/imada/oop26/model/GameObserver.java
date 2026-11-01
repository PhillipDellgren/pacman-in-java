package dk.sdu.imada.oop26.model;


public interface GameObserver {
	void onGameStateChange();

	void update(GameModel gameModel);
}
