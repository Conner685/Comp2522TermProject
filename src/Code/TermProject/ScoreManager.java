package TermProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Manages saving and retrieving high scores for the game.
 */
public class ScoreManager
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
        catch (IOException e)
        {
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
            path = Paths.get("res", "VortexScore.txt");

            if (Files.exists(path))
            {
                final List<String> scores;
                scores = Files.readAllLines(path);
                return scores.stream().sorted(Comparator.reverseOrder()).limit(n).toList();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        List<String> noScores;
        noScores = new ArrayList<>();
        noScores.add("No Scores!");
        return noScores;
    }
}