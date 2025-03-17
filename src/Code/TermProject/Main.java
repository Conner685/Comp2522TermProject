package TermProject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Scanner;

public class Main extends Application
{
    public static void main(final String[] args)
    {
        // Launch the JavaFX application to initialize the runtime
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        Platform.setImplicitExit(false);

        new Thread(() -> {
            final Scanner input;
            input = new Scanner(System.in);
            String choice;

            do
            {
                System.out.println("Press W to play the Word game.");
                System.out.println("Press N to play the Number game.");
                System.out.println("Press M to play the <your game's name> game.");
                System.out.println("Press Q to quit.");

                choice = input.nextLine();

                switch (choice.toLowerCase())
                {
                    case "w" ->
                    {
                        WordGame.wordGameMenu();
                    }
                    case "n" ->
                    {
                        Platform.runLater(() -> new NumberGame().start(new Stage()));
                    }
                    case "m" -> System.out.println("Working3");
                    case "q" -> {
                        System.out.println("Exiting...");
                        Platform.exit();
                    }
                    default -> System.out.println("Invalid input! Please try again!");
                }

            } while (!choice.equalsIgnoreCase("q"));

            input.close();
        }).start();
    }
}