package ca.bcit.termProject;

import ca.bcit.termProject.numberGame.NumberGame;
import ca.bcit.termProject.vortexGame.VortexGameEngine;
import ca.bcit.termProject.wordGame.WordGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Scanner;

/**
 * Central hub for launching different game applications within the program.
 *
 * <p>This class serves as:
 * <ul>
 *   <li>The JavaFX application entry point</li>
 *   <li>Console-based game selection menu</li>
 *   <li>Game session lifecycle manager</li>
 * </ul>
 *
 * <p>Available Games:
 * <table border="1">
 *   <tr><th>Command</th><th>Game</th></tr>
 *   <tr><td>w</td><td>Word Game (geographical trivia)</td></tr>
 *   <tr><td>n</td><td>Number Game (placement puzzle)</td></tr>
 *   <tr><td>v</td><td>Vortex (bullet hell arcade)</td></tr>
 *   <tr><td>q</td><td>Quit program</td></tr>
 * </table>
 *
 * <p>Technical Implementation:
 * <ul>
 *   <li>Uses JavaFX application framework</li>
 *   <li>Runs console menu in separate thread</li>
 *   <li>Manages cross-thread JavaFX launches</li>
 *   <li>Handles graceful shutdown</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public class Main extends Application
{
    private static final String WORD_GAME   = "w";
    private static final String NUMBER_GAME = "n";
    private static final String VORTEX_GAME = "v";
    private static final String QUIT_GAME   = "q";

    /**
     * Entry point and launches JavaFx
     *
     * @param args command-line arguments
     */
    public static void main(final String[] args)
    {
        launch(args);
    }

    /**
     * Configures and displays the game selection interface.
     *
     * <p>Initialization includes:
     * <ul>
     *   <li>Setting up JavaFX implicit exit behavior</li>
     *   <li>Launching console interface thread</li>
     *   <li>Preparing game staging environment</li>
     * </ul>
     *
     * @param primaryStage The root JavaFX container
     */
    @Override
    public void start(final Stage primaryStage)
    {
        Platform.setImplicitExit(false);

        new Thread(() ->
        {
            final Scanner input;
            input = new Scanner(System.in);
            String choice;

            do
            {
                System.out.println("Press " + WORD_GAME + " to play the Word game.");
                System.out.println("Press " + NUMBER_GAME + " to play the Number game.");
                System.out.println("Press " + VORTEX_GAME + " to play Vortex.");
                System.out.println("Press " + QUIT_GAME + " to quit.");

                choice = input.nextLine();

                switch (choice.toLowerCase())
                {
                    case WORD_GAME ->
                            WordGame.wordGameMenu();
                    case NUMBER_GAME ->
                            Platform.runLater(() ->
                                new NumberGame().start(primaryStage));
                    case VORTEX_GAME ->
                            Platform.runLater(() ->
                                new VortexGameEngine().start(primaryStage));
                    case QUIT_GAME ->
                        {
                            System.out.println("Exiting...");
                            Platform.exit();
                        }
                    default -> System.out.println("Invalid input! Please try again!");
                }

            } while (!choice.equalsIgnoreCase(QUIT_GAME));

            input.close();
        }).start();
    }
}
