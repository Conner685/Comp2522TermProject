package ca.bcit.termProject.vortexGame;

import ca.bcit.termProject.numberGame.NumberGame;
import ca.bcit.termProject.wordGame.WordGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Scanner;

public class Main extends Application

    //TODO MAKE DIFFERNET PACKAGES FOR EACH GAMES AND SET VISIBILITY AS NEEDED
{
    public static void main(final String[] args)
    {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage)
    {
        Platform.setImplicitExit(false);

        //TODO Possible better solution(?) (ask brady he seems to know what he is doing)
        new Thread(() ->
        {
            final Scanner input;
            input = new Scanner(System.in);
            String choice;

            do
            {
                //TODO magic strings for W N V Q
                System.out.println("Press W to play the Word game.");
                System.out.println("Press N to play the Number game.");
                System.out.println("Press V to play Vortex.");
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
                    case "v" ->
                    {
                        Platform.runLater(() -> new VortexGameEngine().start(new Stage()));
                    }

                    case "q" ->
                    {
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
