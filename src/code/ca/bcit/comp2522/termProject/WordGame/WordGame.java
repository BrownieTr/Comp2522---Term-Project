package ca.bcit.comp2522.termProject.WordGame;

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
 * <p>
 * COMP 2522 Term Project
 * BCIT CST
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
    protected Map<String, Country> getQuestionBank()
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
    private void loadQuestions() throws IOException
    {
        try
        {
            // Iterate through each letter from 'a' to 'z' to read corresponding country files
            for(char c = 'a'; c <= 'z'; c++)
            {
                // Define the path for the current letter's file (e.g., a.txt, b.txt, etc.)
                final Path dataPath = Paths.get("src", "res", "WordGame",  c + ".txt");

                // Temporary arrays to hold the country name, capital, and facts
                final String[] countryName = {""};
                final String[] countryCapital = {""};

                // Check if the file exists
                if(Files.exists(dataPath))
                {
                    // Read all lines from the file into a list
                    List<String> countryList = Files.readAllLines(dataPath);

                    // Split the content by double newlines to separate each entry
                    countryList = Arrays.stream(String.join("\n", countryList).split("\n\n")).toList();

                    // Process each entry in the file
                    countryList.forEach(part -> {
                        // Split each entry by newlines and filter out empty lines
                        List<String> entry = Arrays.stream(part.split("\n")).filter(str -> !str.isEmpty()).toList();

                        // Extract the country name and capital from the first line of the entry
                        final String[] firstLine = entry.getFirst().split(":");
                        countryName[FIRST_INDEX] = firstLine[FIRST_INDEX];
                        countryCapital[FIRST_INDEX] = firstLine[SECOND_INDEX];

                        // Inside forEach
                        String[] facts = new String[3];
                        for (int j = FIRST_FACT_INDEX; j <= LAST_FACT_INDEX; j++) {
                            facts[j - FIRST_FACT_INDEX] = entry.get(j);
                        }

                        // Extract the three facts from the remaining lines in the entry
                        for(int j = FIRST_FACT_INDEX; j <= LAST_FACT_INDEX; j++)
                        {
                            facts[j - FIRST_FACT_INDEX] = entry.get(j);
                        }

                        // Create a Country object with the extracted information
                        Country country = new Country(countryName[FIRST_INDEX], countryCapital[FIRST_INDEX], facts);

                        // Add the country to the question bank
                        questionBank.put(countryName[FIRST_INDEX], country);
                    });
                }

            }
        } catch (IOException e) {
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
    public void run() throws IOException
    {
        // Initialize a Score object with the current date and initial values.
        Score totalScore = new Score(DATE_TIME_PLAYED, INITIAL_VALUE, INITIAL_VALUE, INITIAL_VALUE, INITIAL_VALUE);

        // Create a new WordGame instance to load and manage the questions.
        WordGame wordGame = new WordGame();
        boolean playAgain;
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
    private boolean playGame(Score totalScore, final WordGame wordGame)
    {
        final Scanner scan = new Scanner(System.in);
        final Random random = new Random();
        final Map<String, Country> questionBank = wordGame.getQuestionBank();
        final String[] countryNames = questionBank.keySet().toArray(String[]::new);
        final int incorrectTwoAttempts;

        // Game-specific counters
        int numGamesPlayed = 1;
        int firstAttemptCorrect = 0;
        int secondAttemptCorrect = 0;
        int incorrectFirstAttempts = 0;
        int incorrectSecondAttempts = 0;

        System.out.println("\nWelcome to the Word Game! Let's start.\n");

        // Loop through a fixed number of questions
        for (int i = 0; i < MAX_QUESTIONS; i++)
        {
            // Randomly select a country and a question type
            final Country country = questionBank.get(countryNames[random.nextInt(countryNames.length)]);
            final int questionType = random.nextInt(MAX_QUESTION_TYPES); // 0 = capital to country, 1 = country to capital, 2 = fact to country
            final String prompt;
            final String correctAnswer;

            // Build the question prompt and determine the correct answer
            switch (questionType)
            {
                case 0: // Capital to Country
                    prompt = "What country has the capital " + country.getCapital() + "? ";
                    correctAnswer = country.getCountry();
                    break;
                case 1: // Country to Capital
                    prompt = "What is the capital of " + country.getCountry() + "? ";
                    correctAnswer = country.getCapital();
                    break;
                default: // Fact to Country
                    String fact = country.getFacts()[random.nextInt(country.getFacts().length)];
                    prompt = "Which country is described by: \"" + fact + "\"? ";
                    correctAnswer = country.getCountry();
                    break;
            }

            // Ask the user and allow up to MAX_ATTEMPTS to answer
            boolean correct = false;
            for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++)
            {
                System.out.print(prompt);
                final String userAnswer = scan.nextLine().trim();

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
                        secondAttemptCorrect++;
                        incorrectFirstAttempts++; // they got first attempt wrong
                    }
                    correct = true;
                    break; // exit attempt loop
                }
                else if (attempt == FIRST_ATTEMPT)
                {
                    System.out.println("INCORRECT. Try again.\n");
                }
            }

            // If they never got it correct
            if (!correct)
            {
                System.out.println("INCORRECT.");
                System.out.println("The correct answer was " + correctAnswer + "\n");
                incorrectFirstAttempts++;
                incorrectSecondAttempts++;
            }
        }

        // Calculate the number of questions where both attempts were incorrect
        incorrectTwoAttempts = (incorrectFirstAttempts + incorrectSecondAttempts) / MAX_ATTEMPTS;

        // Update the total score stats
        totalScore.setNumGamesPlayed(numGamesPlayed);
        totalScore.setNumCorrectFirstAttempt(firstAttemptCorrect);
        totalScore.setNumCorrectSecondAttempt(secondAttemptCorrect);
        totalScore.setNumIncorrectTwoAttempts(incorrectTwoAttempts);

        // Show the updated score to the user
        totalScore.displayScore();

        // Ask the player if they want to play again
        while (true) {
            System.out.print("\nDo you want to play again? (Yes/No): ");
            String response = scan.nextLine().trim();
            if (response.equalsIgnoreCase("Yes")) return true;
            else if (response.equalsIgnoreCase("No")) return false;
            else System.out.println("Invalid input. Please enter Yes or No.");
        }
    }
}
