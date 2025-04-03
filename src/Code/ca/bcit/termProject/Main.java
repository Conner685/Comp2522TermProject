package ca.bcit.termProject;

import ca.bcit.termProject.numberGame.NumberGame;
import ca.bcit.termProject.vortexGame.VortexGameEngine;
import ca.bcit.termProject.wordGame.WordGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Scanner;

/**
 * The Main menu for the entire program, starting the java runtime and allowing the user to open games
 * @author Conner Ponton
 * @version 1.0
 */
public class Main extends Application
{
    private static final String WORD_GAME = "w";
    private static final String NUMBER_GAME = "n";
    private static final String VORTEX_GAME = "v";
    private static final String QUIT_GAME = "q";

    /**
     * Entry point and launches JavaFx
     * @param args program arguments
     */
    public static void main(final String[] args)
    {
        launch(args);
    }

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
                                new NumberGame().start(new Stage()));
                    case VORTEX_GAME ->
                            Platform.runLater(() -> new VortexGameEngine().start(new Stage()));
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
