package ca.bcit.termProject.numberGame;

/**
 * The GameLogic interface defines the core game logic methods.
 * @author Conner Ponton
 * @version 1.0
 */
public interface GeneralGameLogic
{
    static final int PADDING = 10;
    static final int ROOT_PADDING = 20;

    void startNewGame();
    void endGame(final boolean won);
    void showGameResult(final boolean won);
}