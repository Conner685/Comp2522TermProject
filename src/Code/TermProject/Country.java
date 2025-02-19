package TermProject;

public class Country
{

    private final String name;
    private final String capitalCityName;
    private final String[] facts;

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

    private static void validateString(final String name)
    {
        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Invalid String");
        }
    }

    private static void validateFacts(final String[] facts)
    {
        if (facts == null || facts.length != 3)
        {
            throw new IllegalArgumentException("Invalid Array size");
        }

        for (String fact : facts)
        {
            validateString(fact);
        }
    }

    public String getName()
    {
        return name;
    }

    public String getCapitalCityName()
    {
        return capitalCityName;
    }

    public String[] getFacts()
    {
        return facts;
    }

    public String getFact(final int index)
    {
        if (index < 0 || index > 3)
        {
            throw new IllegalArgumentException("Invalid array index");
        }
        return this.facts[index];
    }
}
