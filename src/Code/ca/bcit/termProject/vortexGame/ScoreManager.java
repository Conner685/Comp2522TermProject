package ca.bcit.termProject.vortexGame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Manages persistent storage and retrieval of player high scores.
 *
 * <p>This utility class handles all score-related file operations including:
 * <ul>
 *   <li>Appending new scores to the score file</li>
 *   <li>Reading and sorting existing scores</li>
 *   <li>Providing top N scores for display</li>
 *   <li>Handling file system exceptions gracefully</li>
 * </ul>
 *
 * <p>File System Characteristics:
 * <table border="1">
 *   <tr><th>Attribute</th><th>Value</th></tr>
 *   <tr><td>File Location</td><td>src/res/VortexScore.txt</td></tr>
 *   <tr><td>Format</td><td>One score per line (plain text)</td></tr>
 *   <tr><td>Encoding</td><td>System default charset</td></tr>
 *   <tr><td>Concurrency</td><td>Not thread-safe</td></tr>
 * </table>
 *
 * <p>Error Handling:
 * <ul>
 *   <li>Prints stack traces to stderr on failure</li>
 *   <li>Returns default values when scores unavailable</li>
 *   <li>Creates score file if non-existent</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class ScoreManager
{
    /**
     * Persists a player's score to the score file.
     *
     * <p>Operation Details:
     * <ul>
     *   <li>Appends score as new line in UTF-8 format</li>
     *   <li>Creates score file if it doesn't exist</li>
     *   <li>Handles IO exceptions with error logging</li>
     * </ul>
     *
     * @param score The survival time in seconds to record
     */
    public static void saveScore(final long score)
    {
        try
        {
            final Path path;
            path = Paths.get("src","res", "VortexScore.txt");
            if (Files.notExists(path))
            {
                Files.createFile(path);
            }
            Files.write(path, (score + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch (final IOException e)
        {
            System.err.println("Failed to save");
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the highest N scores from persistent storage.
     *
     * <p>Processing Pipeline:
     * <ol>
     *   <li>Reads all lines from score file</li>
     *   <li>Filters empty/null entries</li>
     *   <li>Parses to integers</li>
     *   <li>Sorts in descending order</li>
     *   <li>Limits to top N results</li>
     *   <li>Converts back to strings</li>
     * </ol>
     *
     * @param n The maximum number of scores to return
     * @return Unmodifiable list of score strings, or singleton "No Scores!" list
     */
    public static List<String> getHighestNScores(final int n)
    {
        try
        {
            final Path path;
            path = Paths.get("src", "res", "VortexScore.txt");

            if (Files.exists(path))
            {
                final List<String> scores;
                scores = Files.readAllLines(path);
                return scores.stream()
                        .filter(Objects::nonNull)
                        .filter(s -> !s.trim().isEmpty())
                        .map(Integer::parseInt)
                        .sorted(Comparator.reverseOrder())
                        .limit(n)
                        .map(Object::toString)
                        .collect(Collectors.toList());
            }
        } catch (final IOException e)
        {
            System.err.println("Failed score file read");
            e.printStackTrace();
        }

        final List<String> noScores;

        noScores = new ArrayList<>();
        noScores.add("No Scores!");
        return noScores;
    }
}