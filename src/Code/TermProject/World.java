package TermProject;

import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

public class World
{
    private static final int MAX_FACTS = 3;
    private static final int FIRST_FACT = 0;
    private static final int SECOND_FACT = 1;
    private static final int THIRD_FACT = 2;

    private final HashMap<String, Country> countries;

    public World()
    {

        this.countries = new HashMap<String, Country>();

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
                reader.useDelimiter(":");

                while (reader.hasNext())
                {
                    Country newCountry;
                    String name;
                    String capital;
                    String[] facts = new String[MAX_FACTS];

                    name = reader.next();
                    capital = reader.next();
                    facts[FIRST_FACT] = reader.next();
                    facts[SECOND_FACT] = reader.next();
                    facts[THIRD_FACT] = reader.next();

                    System.out.println(name + "\n" + capital + "\n" + facts[FIRST_FACT] + "\n" + facts[SECOND_FACT] + "\n" + facts[THIRD_FACT]);

                    newCountry = new Country(name, capital, facts);
                    this.countries.put(name, newCountry);
                }
                reader.close();

            } catch (FileNotFoundException e)
            {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    private static HashMap<String, Country> populateWorld()
    {
        HashMap<String, Country> countries = new HashMap<String, Country>();



        return countries;
    }

    private static void ScanCountryFile()
    {

    }
}
