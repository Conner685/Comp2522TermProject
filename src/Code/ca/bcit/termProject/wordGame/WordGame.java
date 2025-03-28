package ca.bcit.termProject.wordGame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;

public class WordGame
{
    private static final int INITIAL_STAT   = 0;

    private static final int FIRST_TRY_PTS  = 2;
    private static final int SECOND_TRY_PTS = 1;
    private static final int TOTAL_ATTEMPTS = 2;

    private static final int TOTAL_QUESTIONS        = 10;
    private static final int TOTAL_QUESTION_TYPES   = 3;
    private static final int TOTAL_ANSWERS          = 3;
    private static final int TOTAL_FACTS            = 3;
    private static final int FIRST_ANS              = 0;
    private static final int SECOND_ANS             = 1;
    private static final int THIRD_ANS              = 2;
    private static final int ANS_INDEX_SHIFT        = 1;

    private static final World COUNTRIES      = new World();
    private static final Scanner INPUTSCANNER = new Scanner(System.in);

    private int gamesPlayed;
    private int firstTryAns;
    private int secondTryAns;
    private int incorrectAns;

    private WordGame()
    {
        this.incorrectAns = INITIAL_STAT;
        this.secondTryAns = INITIAL_STAT;
        this.firstTryAns  = INITIAL_STAT;
        this.gamesPlayed  = INITIAL_STAT;
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

            String[] validInputs = {"yes", "no"};

            playerInput = getValidInput("Type Yes or No", validInputs);

            if (playerInput.equals("yes"))
            {
                session.playWordGame();
            } else
            {
                Score sessionResults;

                sessionResults = new Score(LocalDateTime.now(),
                                        session.gamesPlayed,
                                        session.firstTryAns,
                                        session.secondTryAns,
                                        session.incorrectAns);
                try
                {
                    Score.appendScoreToFile(sessionResults, "test.txt");
                } catch (IOException e)
                {
                    throw new RuntimeException(e);
                }

                System.out.println("Thanks for playing!\n");
                System.out.println(sessionResults);

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
                case 1 -> this.identifyCapitalByCountry();
                case 2 -> this.identifyCountryByFact();
                default -> throw new IllegalStateException("Invalid randomizer value");
            }
        }
    }

    private static String getValidInput(final String prompt,
                                 final String[] validOptions)
    {
        String input;
        while (true)
        {
            System.out.println(prompt);
            input = INPUTSCANNER.nextLine().toLowerCase().trim();
            for (String option : validOptions) {
                if (input.equals(option)) {
                    return input;
                }
            }
            System.out.println("Invalid input! Please try again.");
        }
    }

    private int questionGuesses(int guesses,
                                final String playerGuess,
                                final int correctIndex,
                                final String correctAns)
    {
        if (playerGuess.equals(correctIndex + ANS_INDEX_SHIFT + ""))
        {
            switch (guesses)
            {
                case FIRST_ANS ->
                {
                    System.out.println("Correct on the first try!");
                    this.firstTryAns++;
                    guesses = TOTAL_ATTEMPTS;
                }
                case SECOND_ANS ->
                {
                    System.out.println("Correct on the second try!");
                    this.secondTryAns++;
                    guesses = TOTAL_ATTEMPTS;
                }
                default -> throw new IllegalStateException("Invalid guesses for correct gameplay");
            }
        } else
        {
            System.out.print("Incorrect! ");
            System.out.println((guesses == FIRST_ANS) ? "try again!\n" : "The answer was "
                    + correctAns);
            guesses++;
            if (guesses == 2)
            {
                this.incorrectAns++;
            }
        }
        return guesses;
    }

    private void identifyCapitalByCountry()
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
                do {
                    selectedCountries[i] = COUNTRIES.selectRandCountry();
                } while (selectedCountries[i].equals(correctCountry));
            }
        }

        StringBuilder question;
        question = new StringBuilder();
        question.append("\nWhich is the capital city of ");
        question.append(correctCountry.getName());

        question.append("\n1-->");
        question.append(selectedCountries[FIRST_ANS].getCapitalCityName());

        question.append("\n2-->");
        question.append(selectedCountries[SECOND_ANS].getCapitalCityName());

        question.append("\n3-->");
        question.append(selectedCountries[THIRD_ANS].getCapitalCityName());

        question.append("\n\ntype in the corresponding number next to the capital!");

        int guesses;

        guesses = INITIAL_STAT;

        do
        {
            String[] validInputs = {"1", "2", "3"};

            playerInput = getValidInput(question.toString(),
                    validInputs);

            guesses = this.questionGuesses(guesses,
                    playerInput,
                    correctIndex,
                    selectedCountries[correctIndex].getCapitalCityName());

        } while(guesses < TOTAL_ATTEMPTS);
    }

    private static String displayCountryChoices(Country[] countries)
    {
        StringBuilder answers;

        answers = new StringBuilder();

        answers.append("\n1-->");
        answers.append(countries[FIRST_ANS].getName());

        answers.append("\n2-->");
        answers.append(countries[SECOND_ANS].getName());

        answers.append("\n3-->");
        answers.append(countries[THIRD_ANS].getName());

        answers.append("\n\ntype in the corresponding number next to the country!");

        return answers.toString();
    }

    private void identifyCountryByFact()
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
                do {
                    selectedCountries[i] = COUNTRIES.selectRandCountry();
                } while (selectedCountries[i].equals(correctCountry));
            }
        }

        StringBuilder question;
        question = new StringBuilder();
        question.append("\nWhich country does this fact coincide with?\n");
        question.append(correctCountry.getFact(RandValue(TOTAL_FACTS)));

        question.append(displayCountryChoices(selectedCountries));

        int guesses;

        guesses = INITIAL_STAT;

        do
        {
            String[] validInputs = {"1", "2", "3"};

            playerInput = getValidInput(question.toString(),
                    validInputs);

            guesses = this.questionGuesses(guesses,
                    playerInput,
                    correctIndex,
                    selectedCountries[correctIndex].getName());

        } while(guesses < TOTAL_ATTEMPTS);
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
                do
                {
                    selectedCountries[i] = COUNTRIES.selectRandCountry();
                } while (selectedCountries[i].equals(correctCountry));
            }
        }

        StringBuilder question;
        question = new StringBuilder();
        question.append("\nWhich country does the capital city of ");
        question.append(correctCountry.getCapitalCityName());

        question.append(displayCountryChoices(selectedCountries));

        int guesses;

        guesses = INITIAL_STAT;

        do
        {
            String[] validInputs = {"1", "2", "3"};

            playerInput = getValidInput(question.toString(),
                    validInputs);

            guesses = this.questionGuesses(guesses,
                    playerInput,
                    correctIndex,
                    selectedCountries[correctIndex].getName());

        } while(guesses < TOTAL_ATTEMPTS);
    }
}
