package TermProject;

import java.util.Random;
import java.util.Scanner;

public class WordGame
{
    private static final int INITIAL_STAT = 0;

    private static final int INCORRECT_PTS = 0;
    private static final int FIRST_TRY_PTS = 2;
    private static final int SECOND_TRY_PTS = 1;
    private static final int TOTAL_ATTEMPTS = 2;

    private static final int TOTAL_QUESTIONS = 10;
    private static final int TOTAL_QUESTION_TYPES = 3;
    private static final int TOTAL_FACTS = 3;
    private static final int TOTAL_ANSWERS = 3;
    private static final int FIRST_ANS = 0;
    private static final int SECOND_ANS = 1;
    private static final int THIRD_ANS = 2;
    private static final int ANS_INDEX_SHIFT = 1;

    private static final World COUNTRIES = new World();
    private static final Scanner INPUTSCANNER = new Scanner(System.in);

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
        intro.append("In this game you need to answer ");
        intro.append(TOTAL_QUESTIONS);
        intro.append(" different questions\n");
        intro.append("You will have TWO attempts at each question! \n");
        intro.append("First Try gets you ");
        intro.append(FIRST_TRY_PTS);
        intro.append(" points\nSecond Try gets you ");
        intro.append(SECOND_TRY_PTS);
        intro.append("\nAfter that you get nothing!\n");

        System.out.println(intro);

        do
        {
            StringBuilder playGame;
            String playerInput;

            playGame = new StringBuilder();

            playGame.append("Would you like to play word game");
            playGame.append((session.gamesPlayed == INITIAL_STAT) ? "?" : " again?");

            System.out.println(playGame);

            System.out.println("Type Yes or No");
            playerInput = INPUTSCANNER.nextLine().toLowerCase();

            while(!playerInput.equals("yes") && !playerInput.equals("no"))
            {
                System.out.println("Invalid Input!");
                System.out.println("Type Yes or No");
                playerInput = INPUTSCANNER.nextLine();
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

    private static int RandValue(final int range)
    {
        Random randomizer;

        randomizer = new Random();

        return randomizer.nextInt(range);
    }

    private void playWordGame()
    {
        this.gamesPlayed++;

        for (int i = 0; i < TOTAL_QUESTIONS; i++)
        {
            int questionNum = RandValue(TOTAL_QUESTION_TYPES);

            switch (questionNum)
            {
                case 0 -> this.identifyCountryByCapital();
                case 1 -> System.out.println(questionNum);
                case 2 -> System.out.println(questionNum);
                default -> throw new IllegalStateException("Invalid randomizer value");
            }
        }
    }

    private void identifyCountryByCapital()
    {
        Country correctCountry;
        Country[] selectedCountries;
        String playerInput;
        int correctIndex;

        selectedCountries = new Country[TOTAL_ANSWERS];

        correctCountry = COUNTRIES.selectRandCountry();

        correctIndex = RandValue(TOTAL_ANSWERS);
        selectedCountries[correctIndex] = correctCountry;

        for (int i = 0; i < selectedCountries.length; i++)
        {
            if (selectedCountries[i] == null)
            {
                selectedCountries[i] = COUNTRIES.selectRandCountry();
            }
        }

        StringBuilder question;
        question = new StringBuilder();
        question.append("Which country does the capital city of ");
        question.append(correctCountry.getCapitalCityName());

        question.append(" belong too?\n1-->");
        question.append(selectedCountries[FIRST_ANS].getName());

        question.append("\n2-->");
        question.append(selectedCountries[SECOND_ANS].getName());

        question.append("\n3-->");
        question.append(selectedCountries[THIRD_ANS].getName());

        question.append("\n\ntype in the corresponding number next to the country!");

        int guesses;

        guesses = INITIAL_STAT;

        do
        {
            System.out.println(question);

            playerInput = INPUTSCANNER.nextLine();

            while(!playerInput.equals("1") && !playerInput.equals("2") && !playerInput.equals("3"))
            {
                System.out.println("Invalid Input!");
                System.out.println("Type the number next to the question you think is correct!");
                playerInput = INPUTSCANNER.nextLine();
            }

            if (playerInput.trim().equals(correctIndex + ANS_INDEX_SHIFT + ""))
            {
                switch (guesses)
                {
                    case FIRST_ANS ->
                    {
                        System.out.println("Correct on the first try!");
                        this.firstTryAns++;
                        this.score += FIRST_TRY_PTS;
                        guesses = TOTAL_ATTEMPTS;
                    }
                    case SECOND_ANS ->
                    {
                        System.out.println("Correct on the second try!");
                        this.secondTryAns++;
                        this.score += FIRST_TRY_PTS;
                        guesses = TOTAL_ATTEMPTS;
                    }
                    default -> throw new IllegalStateException("Invalid guesses for correct gameplay");
                }
            } else
            {
                System.out.print("Incorrect! ");
                System.out.println((guesses == FIRST_ANS) ? "try again!" : "The answer was "
                        + selectedCountries[correctIndex].getName());
                guesses++;
            }
        } while(guesses < TOTAL_ATTEMPTS);
    }
}
