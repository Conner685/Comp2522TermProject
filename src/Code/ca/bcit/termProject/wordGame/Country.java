package ca.bcit.termProject.wordGame;

/**
 * Immutable data class representing a country with its essential attributes.
 *
 * <p>This class encapsulates:
 * <ul>
 *   <li>Country name and capital city</li>
 *   <li>Three distinctive facts about the country</li>
 *   <li>Validation of all constructor parameters</li>
 * </ul>
 *
 * <p>Class Invariants:
 * <table border="1">
 *   <tr><th>Attribute</th><th>Constraint</th></tr>
 *   <tr><td>Name</td><td>Non-null, non-blank string</td></tr>
 *   <tr><td>Capital</td><td>Non-null, non-blank string</td></tr>
 *   <tr><td>Facts</td><td>Array of exactly 3 non-null, non-blank strings</td></tr>
 * </table>
 *
 * <p>Thread Safety:
 * <ul>
 *   <li>Immutable (all fields final)</li>
 *   <li>No modification methods</li>
 *   <li>Safe for concurrent access</li>
 * </ul>
 *
 * @author Conner Ponton
 * @version 1.0
 */
public final class Country
{
    private static final int FACT_TOTAL = 3;
    private static final int MIN_FACTS = 0;

    private final String name;
    private final String capitalCityName;
    private final String[] facts;

    /**
     * Constructs a validated Country instance.
     *
     * @param name the official country name (e.g., "Canada")
     * @param capitalCityName the capital city (e.g., "Ottawa")
     * @param facts array of exactly 3 distinctive facts
     */
    public Country(final String name,
                   final String capitalCityName,
                   final String[] facts)
    {
        validateString(name);
        validateString(capitalCityName);
        validateFacts(facts);

        this.name = name;
        this.capitalCityName = capitalCityName;
        this.facts = facts;
    }

    /**
     * Returns the name of the country.
     *
     * @return the country name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the name of the capital city.
     *
     * @return the capital city name
     */
    public String getCapitalCityName()
    {
        return capitalCityName;
    }

    /**
     * Retrieves a specific fact by its index.
     *
     * @param index the fact position (0-based)
     * @return the requested fact string
     */
    public String getFact(final int index)
    {
        if (index < MIN_FACTS ||
                index > FACT_TOTAL)
        {
            throw new IllegalArgumentException("Invalid array index");
        }
        return this.facts[index];
    }

    /*
     * Validates that a string is not null or blank.
     *
     * @param name the string to validate
     * @throws IllegalArgumentException if the string is null or blank
     */
    private static void validateString(final String name)
    {
        if (name == null ||
                name.isBlank())
        {
            throw new IllegalArgumentException("Invalid String");
        }
    }

    /*
     * Validates that the facts array is not null, has exactly FACT_TOTAL elements,
     * and that none of the elements are null or blank.
     *
     * @param facts the facts array to validate
     * @throws IllegalArgumentException if the array is invalid
     */
    private static void validateFacts(final String[] facts)
    {
        if (facts == null ||
                facts.length != FACT_TOTAL)
        {
            throw new IllegalArgumentException("Invalid Array size");
        }

        for (final String fact : facts)
        {
            validateString(fact);
        }
    }
}