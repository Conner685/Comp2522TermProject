package ca.bcit.termProject.wordGame;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * Immutable class representing a player's score in the word game with persistence capabilities.
 *
 * <p>This class handles:
 * <ul>
 *   <li>Score calculation and tracking</li>
 *   <li>File-based score storage and retrieval</li>
 *   <li>Detailed performance statistics</li>
 *   <li>Temporal tracking of game sessions</li>
 * </ul>
 *
 * <p>Score Calculation:
 * <table border="1">
 *   <tr><th>Attempt Type</th><th>Points</th></tr>
 *   <tr><td>First Correct</td><td>2 points</td></tr>
 *   <tr><td>Second Correct</td><td>1 point</td></tr>
 *   <tr><td>Incorrect</td><td>0 points</td></tr>
 * </table>
 *
 * <p>File Format:
 * <pre>
 * Date and Time: [timestamp]
 * Games Played: [count]
 * Correct First Attempts: [count]
 * Correct Second Attempts: [count]
 * Incorrect Attempts: [count]
 * Score: [total] points
 * </pre>
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
     * Constructs a new score record with game statistics.
     *
     * @param dateTime When the game session occurred
     * @param gamesPlayed Total games played in session
     * @param correctFirstAttempts Questions solved on first try
     * @param correctSecondAttempts Questions solved on second try
     * @param incorrectAttempts Unsolved questions
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
     * @param score The score to persist
     * @param fileName Target file path
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
     * Reads all scores from a persistence file.
     *
     * @param fileName Source file path
     * @return List of historical scores (chronological order)
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
     * Generates formatted string for file storage.
     *
     * @return Multi-line string with all score components
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

    /*
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

    /*
     * Helper method to read and parse an integer value from a line in the score file.
     *
     * @param reader The BufferedReader to read from
     * @param prefix The expected prefix of the line being read
     * @return The parsed integer value
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
}