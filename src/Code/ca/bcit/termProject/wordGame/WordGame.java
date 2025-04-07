package ca.bcit.termProject.wordGame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * A geographical trivia game that tests knowledge of countries, capitals, and facts.
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class WordGame
{
    // Constants
    private static final int INITIAL_STAT = 0;
    private static final int FIRST_TRY_PTS = 2;
    private static final int SECOND_TRY_PTS = 1;
    private static final int TOTAL_ATTEMPTS = 2;
    private static final int TOTAL_QUESTIONS = 10;
    private static final int TOTAL_QUESTION_TYPES = 3;
    private static final int TOTAL_ANSWERS = 3;
    private static final int TOTAL_FACTS = 3;
    private static final int FIRST_ANS = 0;
    private static final int SECOND_ANS = 1;
    private static final int THIRD_ANS = 2;
    private static final int FIRST_Q = 0;
    private static final int SECOND_Q = 1;
    private static final int THIRD_Q = 2;
    private static final int ANS_INDEX_SHIFT = 1;

    private static final World COUNTRIES = new World();
    private static final Scanner INPUT_SCANNER = new Scanner(System.in);

    // Game statistics
    private int gamesPlayed;
    private int firstTryAns;
    private int secondTryAns;
    private int incorrectAns;
    private int currentIndex;

    private WordGame()
    {
        resetStats();
    }

    /**
     * Resets all game statistics to initial values.
     */
    private void resetStats()
    {
        this.incorrectAns = INITIAL_STAT;
        this.secondTryAns = INITIAL_STAT;
        this.firstTryAns = INITIAL_STAT;
        this.gamesPlayed = INITIAL_STAT;
    }

    /**
     * Displays the game menu and handles game sessions.
     */
    public static void wordGameMenu()
    {
        final WordGame session;
        boolean playing;

        playing = true;
        session = new WordGame();

        displayWelcomeMessage();

        while (playing)
        {
            final String playerInput;

            playerInput = askToPlay(session.gamesPlayed == INITIAL_STAT);

            if (playerInput.equals("yes"))
            {
                session.playWordGame();
            } else

            {
                endGameSession(session);
                playing = false;
            }
        }
    }

    /**
     * Displays the welcome message with game instructions.
     */
    private static void displayWelcomeMessage()
    {
        final StringBuilder intro;

        intro = new StringBuilder();

        intro.append("Welcome to WordGame! The Geographical Trivia game!\n");
        intro.append("In this game you need to answer ").append(TOTAL_QUESTIONS).append(" different questions\n");
        intro.append("You will have TWO attempts at each question! \n");
        intro.append("First Try gets you ").append(FIRST_TRY_PTS).append(" points\n");
        intro.append("Second Try gets you ").append(SECOND_TRY_PTS).append("\n");
        intro.append("After that you get nothing!\n");

        System.out.println(intro);
    }

    /**
     * Asks the player if they want to play (again).
     *
     * @param firstTime Whether this is the first time asking
     * @return The player's response ("yes" or "no")
     */
    private static String askToPlay(final boolean firstTime)
    {
        final String prompt;
        prompt = "Would you like to play word game" + (firstTime ? "?" : " again?");
        return getValidInput(prompt, new String[]{"yes", "no"});
    }

    /**
     * Ends the game session and displays results.
     *
     * @param session The current game session
     */
    private static void endGameSession(final WordGame session)
    {
        final Score sessionResults;

        sessionResults = new Score(
                LocalDateTime.now(),
                session.gamesPlayed,
                session.firstTryAns,
                session.secondTryAns,
                session.incorrectAns
        );

        try
        {
            Score.appendScoreToFile(sessionResults, "src/res/test.txt");
        } catch (final IOException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("Thanks for playing!\n");
        System.out.println(sessionResults);
    }

    /**
     * Plays a complete word game session with all questions.
     */
    private void playWordGame()
    {
        this.gamesPlayed++;

        for (int i = 0; i < TOTAL_QUESTIONS; i++)
        {
            switch (generateRandomQuestionType())
            {
                case FIRST_Q -> identifyCountryByCapital();
                case SECOND_Q -> identifyCapitalByCountry();
                case THIRD_Q -> identifyCountryByFact();
                default -> throw new IllegalStateException("Invalid randomizer value");
            }
        }
    }

    /**
     * Generates a random question type index.
     *
     * @return A random number between 0 and TOTAL_QUESTION_TYPES-1
     */
    private int generateRandomQuestionType()
    {
        return new Random().nextInt(TOTAL_QUESTION_TYPES);
    }

    /**
     * Gets valid input from the player.
     *
     * @param prompt The prompt to display
     * @param validOptions The array of valid options
     * @return The valid input from the player
     */
    private static String getValidInput(final String prompt,
                                        final String[] validOptions)
    {
        while (true)
        {
            System.out.println(prompt);
            final String input;

            input = INPUT_SCANNER.nextLine().toLowerCase().trim();
            for (final String option : validOptions)
            {
                if (input.equals(option))
                {
                    return input;
                }
            }
            System.out.println("Invalid input! Please try again.");
        }
    }

    /**
     * Handles the player's guesses for a question.
     *
     * @param guesses The current number of guesses
     * @param playerGuess The player's guess
     * @param correctIndex The index of the correct answer
     * @param correctAns The correct answer text
     * @return The updated number of guesses
     */
    private int handleQuestionGuesses(final int guesses,
                                      final String playerGuess,
                                      final int correctIndex,
                                      final String correctAns)
    {
        if (playerGuess.equals(String.valueOf(correctIndex + ANS_INDEX_SHIFT)))
        {
            handleCorrectGuess(guesses);
            return TOTAL_ATTEMPTS; // Exit loop
        } else
        {
            return handleIncorrectGuess(guesses, correctAns);
        }
    }

    /**
     * Handles a correct guess.
     *
     * @param guesses The current number of guesses
     */
    private void handleCorrectGuess(final int guesses)
    {
        switch (guesses)
        {
            case FIRST_ANS ->
            {
                System.out.println("Correct on the first try!");
                this.firstTryAns++;
            }
            case SECOND_ANS ->
            {
                System.out.println("Correct on the second try!");
                this.secondTryAns++;
            }
            default -> throw new IllegalStateException("Invalid guesses for correct gameplay");
        }
    }

    /**
     * Handles an incorrect guess.
     *
     * @param guesses The current number of guesses
     * @param correctAns The correct answer text
     * @return The updated number of guesses
     */
    private int handleIncorrectGuess(int guesses,
                                     final String correctAns)
    {
        System.out.print("Incorrect! ");
        System.out.println((guesses == FIRST_ANS) ? "try again!\n" : "The answer was " + correctAns);

        guesses++;
        if (guesses == TOTAL_ATTEMPTS)
        {
            this.incorrectAns++;
        }
        return guesses;
    }

    /**
     * Identifies a country by its capital city.
     */
    private void identifyCountryByCapital()
    {
        final Country[] countries;
        final Country correctCountry;
        final String question;

        countries = prepareQuestionCountries();
        correctCountry = countries[currentIndex];

        question = "\nWhich country does the capital city of " +
                correctCountry.getCapitalCityName() +
                displayCountryChoices(countries);

        askQuestion(question, countries, correctCountry.getName());
    }

    /**
     * Identifies a capital city by its country.
     */
    private void identifyCapitalByCountry() {
        final Country[] countries;
        final Country correctCountry;
        final String question;

        countries = prepareQuestionCountries();
        correctCountry = countries[currentIndex];

        question = "\nWhich is the capital city of " +
                correctCountry.getName() +
                "\n1-->" + countries[FIRST_ANS].getCapitalCityName() +
                "\n2-->" + countries[SECOND_ANS].getCapitalCityName() +
                "\n3-->" + countries[THIRD_ANS].getCapitalCityName() +
                "\n\ntype in the corresponding number next to the capital!";

        askQuestion(question, countries, correctCountry.getCapitalCityName());
    }

    /**
     * Identifies a country by one of its facts.
     */
    private void identifyCountryByFact() {
        final Country[] countries;
        final Country correctCountry;
        final String question;

        countries = prepareQuestionCountries();
        correctCountry = countries[currentIndex];

        question = "\nWhich country does this fact coincide with?\n" +
                correctCountry.getFact(new Random().nextInt(TOTAL_FACTS)) +
                displayCountryChoices(countries);

        askQuestion(question, countries, correctCountry.getName());
    }

    /**
     * Prepares an array of countries for a question, with one correct answer.
     *
     * @return Array of countries with one correct answer
     */
    private Country[] prepareQuestionCountries()
    {
        final Country[] countries;
        final Country correctCountry;

        currentIndex = new Random().nextInt(TOTAL_ANSWERS);
        System.out.println(currentIndex);
        countries = new Country[TOTAL_ANSWERS];
        correctCountry = COUNTRIES.selectRandCountry();

        countries[currentIndex] = correctCountry;

        for (int i = 0; i < countries.length; i++)
        {
            if (countries[i] == null)
            {
                do
                {
                    countries[i] = COUNTRIES.selectRandCountry();
                } while (countries[i].equals(correctCountry));
            }
        }
        return countries;
    }

    /**
     * Displays the country choices for a question.
     *
     * @param countries The array of countries to display
     * @return The formatted choices string
     */
    private static String displayCountryChoices(final Country[] countries)
    {
        return "\n1-->" + countries[FIRST_ANS].getName() +
                "\n2-->" + countries[SECOND_ANS].getName() +
                "\n3-->" + countries[THIRD_ANS].getName() +
                "\n\ntype in the corresponding number next to the country!";
    }

    /**
     * Asks a question and handles the player's response.
     *
     * @param question The question to ask
     * @param countries The array of countries
     * @param correctAnswer The correct answer text
     */
    private void askQuestion(final String question,
                             final Country[] countries,
                             final String correctAnswer)
    {
        int guesses;
        final int correctIndex;

        guesses = INITIAL_STAT;

        do
        {
            final String playerInput;

            playerInput = getValidInput(question, new String[]{"1", "2", "3"});
            guesses = handleQuestionGuesses(guesses, playerInput, currentIndex, correctAnswer);
        } while (guesses < TOTAL_ATTEMPTS);
    }
}