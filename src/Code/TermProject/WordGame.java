package TermProject;

import java.util.Scanner;

public class WordGame
{
    private static final int INITIAL_STAT = 0;
    private static final int FIRST_TRY_PTS = 2;
    private static final int SECOND_TRY_PTS = 1;

    private int gamesPlayed;
    private int firstTryAns;
    private int secondTryAns;
    private int incorrectAns;
    private int score;

    private WordGame()
    {
        this.score = INITIAL_STAT;
        this.incorrectAns = INITIAL_STAT;
        this.secondTryAns = INITIAL_STAT;
        this.firstTryAns = INITIAL_STAT;
        this.gamesPlayed = INITIAL_STAT;
    }

    public static void wordGameMenu()
    {
        boolean playing;
        StringBuilder intro;
        WordGame session;

        playing = true;
        intro = new StringBuilder();
        session = new WordGame();


        intro.append("Welcome to WordGame! The Geographical Trivia game!\n");
        intro.append("In this game you need to answer 10 different questions\n");
        intro.append("You will have TWO attempts at each question! Where: \n");
        intro.append("First Try gets you ");
        intro.append(FIRST_TRY_PTS);
        intro.append(" points\nSecond Try gets you ");
        intro.append(SECOND_TRY_PTS);
        intro.append("\nAfter that you get nothing!\n");

        System.out.println(intro);

        do
        {
            StringBuilder playGame;
            Scanner inputScanner;
            String playerInput;

            playGame = new StringBuilder();
            inputScanner = new Scanner(System.in);

            playGame.append("Would you like to play word game");
            playGame.append((session.gamesPlayed == INITIAL_STAT) ? "?" : " again?");

            System.out.println(playGame);

            System.out.println("Type Yes or No");
            playerInput = inputScanner.nextLine().toLowerCase();

            while(!playerInput.equals("yes") && !playerInput.equals("no"))
            {
                System.out.println("Invalid Input!");
                System.out.println("Type Yes or No");
                playerInput = inputScanner.nextLine();
            }

            if (playerInput.equals("yes"))
            {
                session.playWordGame();
            } else
            {
                System.out.println("Thanks for playing!\n");
                playing = false;
            }
        } while(playing);
    }

    private void playWordGame()
    {
        this.gamesPlayed++;
    }
}
