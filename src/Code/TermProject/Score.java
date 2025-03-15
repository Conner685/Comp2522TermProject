package TermProject;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Score
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

    public Score(LocalDateTime dateTime,
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

    private int calculateScore()
    {
        return (correctFirstAttempts * POINTS_FOR_FIRST_ATTEMPT)
                + (correctSecondAttempts * POINTS_FOR_SECOND_ATTEMPT);
    }

    public int getScore()
    {
        return score;
    }

    // Format the score as a string
    @Override
    public String toString()
    {
        StringBuilder result;

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

    // Append a score to the file
    public static void appendScoreToFile(Score score,
                                         String fileName) throws IOException
    {
        try (FileWriter fileWriter = new FileWriter(fileName, true);

             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

             PrintWriter out = new PrintWriter(bufferedWriter))
        {
            out.println(score.toString());
        }
    }

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

    public static List<Score> readScoresFromFile(String fileName) throws IOException {
        List<Score> scores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (line.startsWith(DATE_TIME_PREFIX))
                {
                    // Parse the date and time
                    LocalDateTime dateTime = LocalDateTime.parse(
                                            line.substring(DATE_TIME_PREFIX.length()),
                                            FORMATTER
                    );

                    // Parse the remaining fields
                    final int gamesPlayed;
                    final int correctFirstAttempts;
                    final int correctSecondAttempts;
                    final int incorrectAttempts;

                    gamesPlayed = readAndParseInt(reader, GAMES_PLAYED_PREFIX);


                    correctFirstAttempts = readAndParseInt(reader, FIRST_ATTEMPTS_PREFIX);

                    correctSecondAttempts = readAndParseInt(reader, SECOND_ATTEMPTS_PREFIX);

                    incorrectAttempts = readAndParseInt(reader, INCORRECT_ATTEMPTS_PREFIX);

                    // Create a new Score object and add it to the list
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
}