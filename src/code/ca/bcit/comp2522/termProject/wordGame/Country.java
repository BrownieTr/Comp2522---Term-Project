package ca.bcit.comp2522.termProject.wordGame;


public class Country
{
    private final String countryName;
    private final String capitalCityName;
    private final String[] facts;

    public Country(final String countryName, final String capitalCityName, final String... facts)
    {
        validateString(countryName);
        validateString(capitalCityName);
        for (String fact : facts)
        {
            validateString(fact);
        }
        this.countryName = countryName;
        this.capitalCityName = capitalCityName;
        this.facts = facts;
    }

    private void validateString(final String str)
    {
        if (str == null || str.isEmpty())
        {
            throw new IllegalArgumentException("String can't be null or empty");
        }
    }

    protected String getCountry() { return countryName; }
    protected String getCapital() { return capitalCityName; }
    protected String[] getFacts() { return facts;}
}


