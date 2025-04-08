package ca.bcit.termProject.numberGame;

/**
 * Defines the core game lifecycle operations for number-based puzzle games.
 *
 * <p>This interface establishes the fundamental contract for:
 * <ul>
 *   <li>Game session management</li>
 *   <li>Result processing</li>
 *   <li>UI feedback mechanisms</li>
 * </ul>
 *
 * <p>Implementation Requirements:
 * <table border="1">
 *   <tr><th>Method</th><th>Responsibility</th></tr>
 *   <tr><td>startNewGame</td><td>Initialize all game state</td></tr>
 *   <tr><td>endGame</td><td>Clean up resources and determine outcome</td></tr>
 *   <tr><td>showGameResult</td><td>Display win/loss feedback</td></tr>
 * </table>
 *
 * <p>UI Constants:
 * <ul>
 *   <li>{@value #PADDING} - Standard spacing between grid elements</li>
 *   <li>{@value #ROOT_PADDING} - Main layout container padding</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public interface GeneralGameLogic
{
    /**
     * Standard spacing between grid elements (pixels).
     * Used for consistent visual layout of game components.
     */
    int PADDING      = 10;
    /**
     * Main container padding (pixels).
     * Provides breathing room around the game board.
     */
    int ROOT_PADDING = 20;

    /**
     * Initializes a new game session.
     *
     * <p>Implementations must:
     * <ul>
     *   <li>Reset all game state</li>
     *   <li>Prepare fresh gameplay elements</li>
     *   <li>Enable player interaction</li>
     * </ul>
     */
    void startNewGame();

    /**
     * Terminates the current game session.
     *
     * @param won indicates if player successfully completed the game
     *
     */
    void endGame(final boolean won);

    /**
     * Presents the game outcome to the player.
     *
     * @param won determines victory/loss messaging
     */
    void showGameResult(final boolean won);
}