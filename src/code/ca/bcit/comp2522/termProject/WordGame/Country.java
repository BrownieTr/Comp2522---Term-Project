package ca.bcit.comp2522.termProject.WordGame;

import java.util.Arrays;

/**
 * Represents a country with its name, capital city, and trivia facts.
 * Used to generate geography-based quiz questions.
 *
 * Each Country object is immutable once created.
 *
 * Author: [Your Name]
 * Date: [Optional Date]
 */
public class Country
{
    private final String countryName;
    private final String capitalCityName;
    private final String[] facts;

    /**
     * Constructs a new Country instance.
     *
     * @param countryName     the name of the country
     * @param capitalCityName the capital city of the country
     * @param facts           an array of interesting facts about the country
     * @throws IllegalArgumentException if any input string is null or empty
     */
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

    /**
     * Validates that a string is not null or empty.
     *
     * @param str the string to validate
     * @throws IllegalArgumentException if the string is null or empty
     */
    private void validateString(final String str)
    {
        if (str == null || str.isEmpty())
        {
            throw new IllegalArgumentException("String can't be null or empty");
        }
    }

    /**
     * Gets the name of the country.
     *
     * @return the country's name
     */
    protected String getCountry()
    {
        return countryName;
    }

    /**
     * Gets the capital city of the country.
     *
     * @return the capital city's name
     */
    protected String getCapital()
    {
        return capitalCityName;
    }

    /**
     * Gets the trivia facts about the country.
     *
     * @return an array of fact strings
     */
    protected String[] getFacts()
    {
        return facts;
    }

    /**
     * Returns a string representation of the country.
     *
     * @return formatted string including country, capital, and facts
     */
    @Override
    public String toString()
    {
        String result = "";
        result += "Country: " + countryName + "\n";
        result += "Capital: " + capitalCityName + "\n";
        result += "Facts: " + Arrays.toString(facts) + "\n";
        return result;
    }
}
