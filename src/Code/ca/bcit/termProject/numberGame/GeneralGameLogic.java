package ca.bcit.termProject.numberGame;

/**
 * The GameLogic interface defines the core game logic methods.
 */
public interface GeneralGameLogic
{
    void startNewGame();
    void endGame(boolean won);
    void showGameResult(boolean won);
}