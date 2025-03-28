package ca.bcit.termProject.wordGame;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class World
{
    private static final int NAME_INDEX     = 0;
    private static final int CAPITAL_INDEX  = 1;

    private static final int MAX_FACTS      = 3;

    private final HashMap<String, Country> countries;

    public World()
    {

        this.countries = new HashMap<>();

        for (char i = 'a'; i <= 'z'; i++)
        {
            if (i == 'w')
            {
                i = 'y';
            }

            String currentFile = "src/res/" + i + ".txt";

            try
            {
                File countryFile;
                countryFile = new File(currentFile);

                Scanner reader;
                reader = new Scanner(countryFile);

                while (reader.hasNextLine())
                {
                    Country newCountry;
                    String nameAndCapitalLine;
                    String name;
                    String capital;
                    String[] facts;
                    String[] nameAndCapital;

                    nameAndCapitalLine = reader.nextLine();
                    if (nameAndCapitalLine.isBlank())
                    {
                        continue;
                    }

                    nameAndCapital = nameAndCapitalLine.split(":");
                    if (nameAndCapital.length != 2)
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

                    //System.out.println(name + ": " + capital + "\n" + facts[FIRST_FACT] + "\n" + facts[SECOND_FACT] + "\n" + facts[THIRD_FACT] + "\n");

                    newCountry = new Country(name, capital, facts);
                    this.countries.put(name, newCountry);
                }
                reader.close();

            } catch (FileNotFoundException e)
            {
                System.out.println("File not found: " + currentFile);
            }
        }
    }

    public Country selectRandCountry()
    {
        final Country[] countryArray;
        final Country country;
        final Random randomizer;

        countryArray = this.countries.values().toArray(new Country[0]);

        randomizer = new Random();

        country = countryArray[randomizer.nextInt(this.countries.size())];

        return country;
    }
}
