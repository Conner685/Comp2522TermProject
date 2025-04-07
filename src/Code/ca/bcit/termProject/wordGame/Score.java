package ca.bcit.termProject.wordGame;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * The Score class represents a player's score in the word game.
 * It tracks game statistics, calculates the total score, and provides methods
 * for reading and writing scores to files.
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class Score
{
    // Constants for score calculation
    private static final int POINTS_FOR_FIRST_ATTEMPT       = 2;
    private static final int POINTS_FOR_SECOND_ATTEMPT      = 1;

    private static final String DATE_TIME_PREFIX            = "Date and Time: ";
    private static final String GAMES_PLAYED_PREFIX         = "Games Played: ";
    private static final String FIRST_ATTEMPTS_PREFIX       = "Correct First Attempts: ";
    private static final String SECOND_ATTEMPTS_PREFIX      = "Correct Second Attempts: ";
    private static final String INCORRECT_ATTEMPTS_PREFIX   = "Incorrect Attempts: ";
    private static final String SCORE_PREFIX                = "Score: ";
    private static final String SCORE_SUFFIX                = " points";
    private static final DateTimeFormatter FORMATTER        = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime dateTime;
    private final int gamesPlayed;
    private final int correctFirstAttempts;
    private final int correctSecondAttempts;
    private final int incorrectAttempts;
    private final int score;

    /**
     * Constructs a new Score object with the specified parameters.
     *
     * @param dateTime The date and time when the score was recorded
     * @param gamesPlayed The total number of games played
     * @param correctFirstAttempts The number of correct first attempts
     * @param correctSecondAttempts The number of correct second attempts
     * @param incorrectAttempts The number of incorrect attempts
     */
    public Score(final LocalDateTime dateTime,
                 final int gamesPlayed,
                 final int correctFirstAttempts,
                 final int correctSecondAttempts,
                 final int incorrectAttempts)
    {
        this.dateTime = dateTime;
        this.gamesPlayed = gamesPlayed;
        this.correctFirstAttempts = correctFirstAttempts;
        this.correctSecondAttempts = correctSecondAttempts;
        this.incorrectAttempts = incorrectAttempts;
        this.score = calculateScore();
    }

    /**
     * Calculates the total score based on correct attempts.
     * First attempts are worth more points than second attempts.
     *
     * @return The calculated total score
     */
    private int calculateScore()
    {
        return (correctFirstAttempts * POINTS_FOR_FIRST_ATTEMPT)
                + (correctSecondAttempts * POINTS_FOR_SECOND_ATTEMPT);
    }

    /**
     * Gets the total calculated score.
     *
     * @return The total score
     */
    public int getScore()
    {
        return score;
    }

    /**
     * Appends a score record to the specified file.
     *
     * @param score The Score object to write to the file
     * @param fileName The name of the file to append to
     * @throws IOException If an I/O error occurs while writing to the file
     */
    public static void appendScoreToFile(final Score score,
                                         final String fileName) throws IOException
    {
        try (FileWriter fileWriter = new FileWriter(fileName, true);

             final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

             final PrintWriter out = new PrintWriter(bufferedWriter))
        {
            out.println(score.toString());
        }
    }

    /**
     * Helper method to read and parse an integer value from a line in the score file.
     *
     * @param reader The BufferedReader to read from
     * @param prefix The expected prefix of the line being read
     * @return The parsed integer value
     * @throws IOException If an I/O error occurs while reading
     * @throws NumberFormatException If the value after the prefix is not a valid integer
     */
    private static int readAndParseInt(final BufferedReader reader,
                                       final String prefix) throws IOException
    {
        final String currLine;
        final int parsedInt;
        final String parsedString;

        currLine = reader.readLine();
        parsedString = currLine.substring(prefix.length());
        parsedInt = Integer.parseInt(parsedString);
        return parsedInt;
    }

    /**
     * Reads all scores from the specified file and returns them as a list.
     *
     * @param fileName The name of the file to read from
     * @return A list of Score objects read from the file
     * @throws IOException If an I/O error occurs while reading the file
     */
    public static List<Score> readScoresFromFile(final String fileName)
                                                throws IOException
    {
        final List<Score> scores;

        scores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (line.startsWith(DATE_TIME_PREFIX))
                {
                    final LocalDateTime dateTime;
                    final int gamesPlayed;
                    final int correctFirstAttempts;
                    final int correctSecondAttempts;
                    final int incorrectAttempts;


                    dateTime = LocalDateTime.parse(
                                            line.substring(DATE_TIME_PREFIX.length()),
                                            FORMATTER
                    );

                    gamesPlayed = readAndParseInt(reader, GAMES_PLAYED_PREFIX);

                    correctFirstAttempts = readAndParseInt(reader, FIRST_ATTEMPTS_PREFIX);

                    correctSecondAttempts = readAndParseInt(reader, SECOND_ATTEMPTS_PREFIX);

                    incorrectAttempts = readAndParseInt(reader, INCORRECT_ATTEMPTS_PREFIX);

                    scores.add(new Score(dateTime,
                            gamesPlayed,
                            correctFirstAttempts,
                            correctSecondAttempts,
                            incorrectAttempts));
                }
            }
        }
        return scores;
    }

    /**
     * Formats the score as a string for file storage.
     * The format includes all score components with appropriate prefixes.
     *
     * @return A string representation of the score
     */
    @Override
    public String toString()
    {
        final StringBuilder result;

        result = new StringBuilder();

        result.append(DATE_TIME_PREFIX);
        result.append(dateTime.format(FORMATTER));
        result.append("\n");

        result.append(GAMES_PLAYED_PREFIX);
        result.append(gamesPlayed);
        result.append("\n");

        result.append(FIRST_ATTEMPTS_PREFIX);
        result.append(correctFirstAttempts);
        result.append("\n");

        result.append(SECOND_ATTEMPTS_PREFIX);
        result.append(correctSecondAttempts);
        result.append("\n");

        result.append(INCORRECT_ATTEMPTS_PREFIX);
        result.append(incorrectAttempts);
        result.append("\n");

        result.append(SCORE_PREFIX);
        result.append(score);
        result.append(SCORE_SUFFIX);
        result.append("\n");

        return result.toString();
    }
}