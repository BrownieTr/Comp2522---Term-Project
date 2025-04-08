package ca.bcit.comp2522.termProject.wordgame;

import java.util.Scanner;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.nio.file.*;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * The WordGame class manages the structure and logic for a geography-based word game.
 * It initializes and holds the question bank, constants for game configuration,
 * and tracks when the game was played.
 * <p>
 * This game is designed to test a player's knowledge of countries through various
 * question types with a limited number of attempts and questions.
 *
 * @author Brownie Tran
 * @version 1.0
 */
public class WordGame
{
    // Constant indices used for parsing or accessing specific list elements
    private final int FIRST_INDEX = 0;
    private final int SECOND_INDEX = 1;
    private final int FIRST_FACT_INDEX = 1;
    private final int FIRST_ATTEMPT = 1;
    private final int LAST_FACT_INDEX = 3;

    // Constants for game limits
    private final int MAX_QUESTION_TYPES = 3;
    private final int MAX_ATTEMPTS = 2;
    private final int MAX_QUESTIONS = 10;

    // Initial score or attempt counter value
    private final int INITIAL_VALUE = 0;
    private final int COUNTRY_ARRAY_SIZE = 1;
    private final int FACTS_ARRAY_SIZE = 3;

    // Date and time when the game session is initiated
    private final LocalDateTime DATE_TIME_PLAYED = LocalDateTime.now();

    // Holds a collection of questions mapped to their corresponding country objects
    private final Map<String, Country> questionBank;

    /**
     * Constructs a WordGame object and initializes the question bank.
     */
    public WordGame()
    {
        questionBank = new HashMap<>();
    }

    /**
     * Returns the question bank for the game.
     *
     * @return a Map containing questions and their associated Country objects
     */
    protected final Map<String, Country> getQuestionBank()
    {
        return questionBank;
    }

    /**
     * Loads country data from text files and populates the question bank.
     * Iterates through files named from 'a.txt' to 'z.txt' in the 'src/res' directory.
     * Each file should contain entries with a country name, capital, and three trivia facts.
     * Each entry is split and added to the question bank as a Country object.
     *
     * @throws IOException if there is an error reading from the files or processing the data.
     */
    private final void loadQuestions() throws IOException
    {
        try
        {
            // Iterate through each letter from 'a' to 'z' to read corresponding country files
            for(char c = 'a'; c <= 'z'; c++)
            {
                // Declare variables for path and String arrays
                final Path dataPath;
                final String[] countryName;
                final String[] countryCapital;
                List<String> countryList;

                dataPath = Paths.get("src", "res", "WordGame",  c + ".txt");
                countryName = new String[COUNTRY_ARRAY_SIZE];
                countryCapital = new String[COUNTRY_ARRAY_SIZE];

                // Check if the file exists
                if(Files.exists(dataPath))
                {
                    // Read all lines from the file into a list
                    countryList = Files.readAllLines(dataPath);

                    // Split the content by double newlines to separate each entry
                    countryList = Arrays.stream(String.join("\n", countryList).split("\n\n")).toList();

                    // Process each entry in the file
                    countryList.forEach(part -> {

                        // Declares variables needed to handle each entry
                        final String[] facts;
                        final String[] firstLine;
                        final List<String> entry;
                        final Country country;

                        // Split each entry by newlines and filter out empty lines
                        entry = Arrays.stream(part.split("\n")).filter(str -> !str.isEmpty()).toList();
                        firstLine = entry.getFirst().split(":");
                        facts = new String[FACTS_ARRAY_SIZE];

                        // Extract the country name and capital from the first line of the entry
                        countryName[FIRST_INDEX] = firstLine[FIRST_INDEX];
                        countryCapital[FIRST_INDEX] = firstLine[SECOND_INDEX];

                        // Extract the three facts from the remaining lines in the entry
                        for(int j = FIRST_FACT_INDEX; j <= LAST_FACT_INDEX; j++)
                        {
                            facts[j - FIRST_FACT_INDEX] = entry.get(j);
                        }

                        // Create a Country object with the extracted information
                        country = new Country(countryName[FIRST_INDEX], countryCapital[FIRST_INDEX], facts);

                        // Add the country to the question bank
                        questionBank.put(countryName[FIRST_INDEX], country);
                    });
                }

            }
        } catch (final IOException e) {
            // Handle any IO exceptions and print the error message
            System.out.println("Error loading country files: " + e.getMessage());
        }
    }

    /**
     * Starts and manages the gameplay loop for the WordGame.
     * <p>
     * Initializes the score, loads questions, runs the main game loop,
     * appends the final score to a file, and displays a thank-you message.
     *
     * @throws IOException if an error occurs during reading or writing files.
     */
    public final void run() throws IOException
    {
        // Declares variables for Score and WordGame
        final Score totalScore;
        final WordGame wordGame;
        boolean playAgain;

        // Initialize a Score object with the current date and initial values.
        totalScore = new Score(DATE_TIME_PLAYED, INITIAL_VALUE, INITIAL_VALUE, INITIAL_VALUE, INITIAL_VALUE);

        // Create a new WordGame instance to load and manage the questions.
        wordGame = new WordGame();
        wordGame.loadQuestions();

        // Game loop: continue playing as long as the user chooses to.
        do {
            playAgain = playGame(totalScore, wordGame);
        } while (playAgain);

        // Display a congratulatory message based on the final score.
        totalScore.congratulation();
        System.out.println("\nThank you for playing!");
    }

    /**
     * Runs a single session of the word game and updates the total score.
     *
     * @param totalScore the Score object that tracks overall player statistics
     * @param wordGame   the WordGame instance that holds the question bank
     * @return true if the player chooses to play again, false otherwise
     */
    private final boolean playGame(final Score totalScore, final WordGame wordGame) {

        // Declares variables to operate the game
        final Scanner scan;
        final Random random;
        final Map<String, Country> questionBank;
        final String[] countryNames;
        final int incorrectTwoAttempts;

        // Initializes variables
        scan = new Scanner(System.in);
        random = new Random();
        questionBank = wordGame.getQuestionBank();
        countryNames = questionBank.keySet().toArray(String[]::new);

        // Initialize score trackers
        int numGamesPlayed = INITIAL_VALUE;
        int firstAttemptCorrect = INITIAL_VALUE;
        int secondAttemptCorrect = INITIAL_VALUE;
        int incorrectFirstAttempts = INITIAL_VALUE;
        int incorrectSecondAttempts = INITIAL_VALUE;

        // Welcome message
        System.out.println("\nWelcome to the Word Game! Let's start.\n");

        // Loop through the number of questions
        for (int i = 0; i < MAX_QUESTIONS; i++)
        {
            final Country country;
            int questionType; // Selects a random question type
            final String prompt;
            final String correctAnswer;
            String userAnswer;
            boolean correct = false;

            country = questionBank.get(countryNames[random.nextInt(countryNames.length)]);
            questionType = random.nextInt(MAX_QUESTION_TYPES);

            // Generate the question prompt and correct answer
            prompt = generateQuestionPrompt(country, questionType);
            correctAnswer = getCorrectAnswer(country, questionType);

            // Prints the question prompt and get user's answer
            System.out.print(prompt);
            userAnswer = scan.nextLine().trim();

            // Ask the user and allow up to MAX_ATTEMPTS to answer
            for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++)
            {
                // Prints the question prompt and get user's answer
                System.out.print(prompt);
                userAnswer = scan.nextLine().trim();

                // Check correct answer
                if (userAnswer.equalsIgnoreCase(correctAnswer))
                {
                    System.out.println("CORRECT!\n");
                    // Update correct answer counters
                    if (attempt == FIRST_ATTEMPT)
                    {
                        firstAttemptCorrect++;
                    }
                    else
                    {
                        // First attempt wrong
                        secondAttemptCorrect++;
                        incorrectFirstAttempts++;
                    }
                    correct = true;
                    break; // exit attempt loop
                } else if (attempt == FIRST_ATTEMPT)
                {
                    // Let user try again if it is their first attempt
                    System.out.println("INCORRECT. Try again.\n");
                }
            }

            // If second attempt wrong
            if (!correct)
            {
                System.out.println("INCORRECT.");
                System.out.println("The correct answer was " + correctAnswer + "\n");
                incorrectFirstAttempts++;
                incorrectSecondAttempts++;
            }
        }


        // Update total score
        incorrectTwoAttempts = calculateIncorrectAttempts(incorrectFirstAttempts, incorrectSecondAttempts);
        updateTotalScore(totalScore, ++numGamesPlayed, firstAttemptCorrect, secondAttemptCorrect, incorrectTwoAttempts);

        // Ask if the user wants to play again
        return askPlayAgain(scan);
    }

    /**
     * Generates the question prompt based on the question type.
     *
     * @param country the country object that contains the question data
     * @param questionType the type of question (capital-to-country, country-to-capital, or fact-to-country)
     * @return the generated question prompt
     */
    private final String generateQuestionPrompt(final Country country, int questionType) {
        switch (questionType) {
            case 0: // Capital to Country
                return "What country has the capital " + country.getCapital() + "? ";
            case 1: // Country to Capital
                return "What is the capital of " + country.getCountry() + "? ";
            default: // Fact to Country
                String fact = country.getFacts()[new Random().nextInt(country.getFacts().length)];
                return "Which country is described by: \"" + fact + "\"? ";
        }
    }

    /**
     * Returns the correct answer based on the question type.
     *
     * @param country the country object
     * @param questionType the type of question
     * @return the correct answer to the question
     */
    private final String getCorrectAnswer(final Country country, int questionType) {
        switch (questionType) {
            case 0: // Capital to Country
                return country.getCountry();
            case 1: // Country to Capital
                return country.getCapital();
            default: // Fact to Country
                return country.getCountry();
        }
    }


    /**
     * Calculates the number of incorrect attempts over two tries.
     *
     * @param incorrectFirstAttempts the number of incorrect answers on the first attempt
     * @param incorrectSecondAttempts the number of incorrect answers on the second attempt
     * @return the total number of incorrect answers over two attempts
     */
    private final int calculateIncorrectAttempts(int incorrectFirstAttempts, int incorrectSecondAttempts)
    {
        return (incorrectFirstAttempts + incorrectSecondAttempts) / MAX_ATTEMPTS;
    }

    /**
     * Updates the total score object with the current game data.
     *
     * @param totalScore the score object to update
     * @param numGamesPlayed the total number of games played
     * @param firstAttemptCorrect the number of first attempt correct answers
     * @param secondAttemptCorrect the number of second attempt correct answers
     * @param incorrectTwoAttempts the number of incorrect answers over two attempts
     */
    private final void updateTotalScore(Score totalScore, int numGamesPlayed, int firstAttemptCorrect,
                                  int secondAttemptCorrect, int incorrectTwoAttempts)
    {
        totalScore.setNumGamesPlayed(numGamesPlayed);
        totalScore.setNumCorrectFirstAttempt(firstAttemptCorrect);
        totalScore.setNumCorrectSecondAttempt(secondAttemptCorrect);
        totalScore.setNumIncorrectTwoAttempts(incorrectTwoAttempts);
        totalScore.displayScore();
    }

    /**
     * Prompts the user to decide whether to play the game again.
     * Repeats until a valid response ("Yes" or "No") is entered.
     *
     * @param scan Scanner object used to read user input
     * @return true if the user wants to play again, false if not
     */
    private final boolean askPlayAgain(Scanner scan) {
        while (true) {
            String response;

            // Prompt the user
            System.out.print("\nDo you want to play again? (Yes/No): ");
            response = scan.nextLine().trim();

            // Check user response
            if (response.equalsIgnoreCase("Yes")) return true;
            else if (response.equalsIgnoreCase("No")) return false;
            else System.out.println("Invalid input. Please enter Yes or No."); // Handle invalid input
        }
    }
}