package ca.bcit.termProject.wordGame;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.io.File;
import java.util.Random;
import java.util.Scanner;

/**
 * Represents a world containing countries loaded from data files.
 * This class loads country data from text files and provides access to random countries.
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class World
{
    private static final int NAME_INDEX     = 0;
    private static final int CAPITAL_INDEX  = 1;
    private static final int FIRST_COUNTRY  = 0;
    private static final int VALID_COUNTRY_DATA = 2;
    private static final int MAX_FACTS      = 3;

    private final HashMap<String, Country> countries;

    /**
     * Constructs a new World by loading country data from files.
     * Files are expected to be in src/res/ directory named a.txt through z.txt,
     * excluding some letters. Each file contains country data in specific format.
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
     * Selects and returns a random country from the world.
     *
     * @return a randomly selected Country object
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