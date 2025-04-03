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
 * Manages saving and retrieving high scores for the game.
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class ScoreManager
{
    /**
     * Saves the provided score to the score file.
     *
     * @param score the score to save
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
     * Retrieves the highest N scores from the score file.
     *
     * @return the List of N highest scores, or 0 if no scores are found
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