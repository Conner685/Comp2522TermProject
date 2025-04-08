package ca.bcit.termProject.wordGame;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.io.File;
import java.util.Random;
import java.util.Scanner;

/**
 * Data repository class that loads and manages country information from text files.
 *
 * <p>This class serves as the data layer for geographical trivia games, providing:
 * <ul>
 *   <li>File-based country data loading</li>
 *   <li>In-memory storage of country details</li>
 *   <li>Random country selection</li>
 *   <li>Data validation and error handling</li>
 * </ul>
 *
 * <p>Data File Requirements:
 * <table border="1">
 *   <tr><th>Attribute</th><th>Format</th></tr>
 *   <tr><td>File Location</td><td>src/res/[a-z].txt (excluding some letters)</td></tr>
 *   <tr><td>Country Entry</td><td>CountryName:CapitalCity</td></tr>
 *   <tr><td>Facts</td><td>3 lines following each country entry</td></tr>
 * </table>
 *
 * <p>Implementation Details:
 * <ul>
 *   <li>Uses HashMap for O(1) country name lookups</li>
 *   <li>Handles missing files gracefully</li>
 *   <li>Validates data format during loading</li>
 *   <li>Provides thread-safe read operations</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class World
{
    private static final int NAME_INDEX         = 0;
    private static final int CAPITAL_INDEX      = 1;
    private static final int FIRST_COUNTRY      = 0;
    private static final int VALID_COUNTRY_DATA = 2;
    private static final int MAX_FACTS          = 3;

    private final HashMap<String, Country> countries;

    /**
     * Constructs a new World by loading country data from files.
     *
     * <p>Loading Process:
     * <ol>
     *   <li>Scans files from a.txt to z.txt (skipping certain letters)</li>
     *   <li>Parses each line as country:capital pairs</li>
     *   <li>Reads subsequent 3 lines as country facts</li>
     *   <li>Validates format before creating Country objects</li>
     * </ol>
     */
    public World()
    {

        this.countries = new HashMap<>();

        for (char i = 'a'; i <= 'z'; i++)
        {
            if (i == 'w')
            {
                i = 'y';
            }

            final String currentFile;

            currentFile = "src/res/" + i + ".txt";

            try
            {
                final File countryFile;
                final Scanner reader;

                countryFile = new File(currentFile);
                reader = new Scanner(countryFile);

                while (reader.hasNextLine())
                {
                    final Country newCountry;
                    final String nameAndCapitalLine;
                    final String name;
                    final String capital;
                    final String[] facts;
                    final String[] nameAndCapital;

                    nameAndCapitalLine = reader.nextLine();
                    if (nameAndCapitalLine.isBlank())
                    {
                        continue;
                    }

                    nameAndCapital = nameAndCapitalLine.split(":");
                    if (nameAndCapital.length != VALID_COUNTRY_DATA)
                    {
                        System.out.println("Invalid format in file: " + currentFile);
                        continue;
                    }

                    name = nameAndCapital[NAME_INDEX];
                    capital = nameAndCapital[CAPITAL_INDEX];

                    facts = new String[MAX_FACTS];
                    for (int j = 0; j < MAX_FACTS; j++)
                    {
                        if (reader.hasNextLine())
                        {
                            facts[j] = reader.nextLine().trim();
                        } else
                        {
                            facts[j] = ""; // Handle missing facts
                        }
                    }

                    newCountry = new Country(name, capital, facts);
                    this.countries.put(name, newCountry);
                }
                reader.close();

            } catch (final FileNotFoundException e)
            {
                System.out.println("File not found: " + currentFile);
            }
        }
    }

    /**
     * Selects a random country from the loaded dataset.
     *
     * @return uniformly distributed random Country object
     */
    public Country selectRandCountry()
    {
        final Country[] countryArray;
        final Country country;
        final Random randomizer;

        countryArray = this.countries.values().toArray(new Country[FIRST_COUNTRY]);

        randomizer = new Random();

        country = countryArray[randomizer.nextInt(this.countries.size())];

        return country;
    }
}