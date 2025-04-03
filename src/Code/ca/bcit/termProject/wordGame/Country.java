package ca.bcit.termProject.wordGame;

/**
 * Represents a country with its name, capital city, and associated facts.
 * This class is immutable and validates all input parameters upon construction.
 */
public final class Country
{
    private static final int FACT_TOTAL = 3;
    private static final int MIN_FACTS = 0;

    private final String name;
    private final String capitalCityName;
    private final String[] facts;

    /**
     * Constructs a new Country with the specified name, capital city, and facts.
     *
     * @param name the name of the country (cannot be null or blank)
     * @param capitalCityName the name of the capital city (cannot be null or blank)
     * @param facts an array of exactly 3 facts about the country (cannot be null or contain null/blank elements)
     * @throws IllegalArgumentException if any parameter is invalid
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

    /**
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
     * Returns the fact at the specified index.
     *
     * @param index the index of the fact to retrieve (must be between MIN_FACTS and FACT_TOTAL)
     * @return the fact at the specified index
     * @throws IllegalArgumentException if the index is invalid
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
}