package ca.bcit.comp2522.termProject.wordGame;

import java.util.Scanner;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.nio.file.*;
import java.io.IOException;
import java.time.LocalDateTime;

public class WordGame
{
    private final int FIRST_INDEX = 0;
    private final int SECOND_INDEX = 1;
    private final int FIRST_FACT_INDEX = 1;
    private final int FIRST_ATTEMPT = 1;
    private final int LAST_FACT_INDEX = 3;
    private final int MAX_QUESTION_TYPES = 3;
    private final int MAX_ATTEMPTS = 2;
    private final int MAX_QUESTIONS = 10;
    private final int INITIAL_VALUE = 0;
    private final LocalDateTime DATE_TIME_PLAYED = LocalDateTime.now();


    private final Map<String, Country> questionBank;

    public WordGame()
    {
        questionBank = new HashMap<>();
    }

    protected Map<String, Country> getQuestionBank()
    {
        return questionBank;
    }


    private void loadQuestions() throws IOException
    {
        try
        {
            for(char c = 'a'; c <= 'z'; c++)
            {
                final Path dataPath = Paths.get("src", "res", c + ".txt");
                final String[] countryName = {""};
                final String[] countryCapital = {""};
                final String[] facts = {"", "", ""};

                if(Files.exists(dataPath))
                {
                    List<String> countryList = Files.readAllLines(dataPath);
                    countryList = Arrays.stream(String.join("\n", countryList).split("\n\n")).toList();

                    countryList.forEach(part -> {
                        List<String> entry = Arrays.stream(part.split("\n")).filter(str -> !str.isEmpty()).toList();

                        countryName[FIRST_INDEX] = entry.getFirst().split(":")[FIRST_INDEX];
                        countryCapital[FIRST_INDEX] = entry.getFirst().split(":")[SECOND_INDEX];
                        for(int j = FIRST_FACT_INDEX; j <= LAST_FACT_INDEX; j++)
                        {
                            facts[j - FIRST_FACT_INDEX] = entry.get(j);
                        }

                        Country country = new Country(countryName[FIRST_INDEX], countryCapital[FIRST_INDEX], facts);
                        questionBank.put(countryName[0], country);
                    });
                }
            }
        } catch (IOException e){
        System.out.println("Error loading country files: " + e.getMessage());
        }
    }

    public void run() throws IOException
    {
        Score totalScore = new Score(DATE_TIME_PLAYED, INITIAL_VALUE, INITIAL_VALUE, INITIAL_VALUE, INITIAL_VALUE);
        WordGame wordGame = new WordGame();
        boolean playAgain;

        wordGame.loadQuestions();
        do {
            playAgain = playGame(totalScore, wordGame);
        } while (playAgain);

        Score.appendScoreToFile(totalScore, "score.txt");
        totalScore.congratulation();
        System.out.println("\nThank you for playing!");
    }

    private boolean playGame(Score totalScore, final WordGame wordGame)
    {
        final Scanner scan = new Scanner(System.in);
        final Random random = new Random();
        final Map<String, Country> questionBank = wordGame.getQuestionBank();
        final String[] countryNames = questionBank.keySet().toArray(String[]::new);
        final int incorrectTwoAttempts;

        int numGamesPlayed = 1;
        int firstAttemptCorrect = 0;
        int secondAttemptCorrect = 0;
        int incorrectFirstAttempts = 0;
        int incorrectSecondAttempts = 0;

        System.out.println("\nWelcome to the Word Game! Let's start.\n");
        for (int i = 0; i < MAX_QUESTIONS; i++)
        {
            final Country country = questionBank.get(countryNames[random.nextInt(countryNames.length)]);
            int questionType = random.nextInt(MAX_QUESTION_TYPES); // 0 = capital to country, 1 = country to capital, 2 = fact to country
            final String prompt;
            final String correctAnswer;

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

            // Ask user and track response
            boolean correct = false;
            for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++)
            {
                System.out.print(prompt);
                final String userAnswer = scan.nextLine().trim();

                if (userAnswer.equalsIgnoreCase(correctAnswer))
                {
                    System.out.println("CORRECT!\n");
                    if (attempt == FIRST_ATTEMPT) firstAttemptCorrect++;
                    else
                    {
                        secondAttemptCorrect++;
                        incorrectFirstAttempts++;
                    }
                    correct = true;
                    break;
                } else if(attempt == FIRST_ATTEMPT)
                {
                    System.out.println("INCORRECT. Try again.\n");
                }
            }

            if (!correct)
            {
                System.out.println("INCORRECT.");
                System.out.println("The correct answer was " + correctAnswer + "\n");
                incorrectFirstAttempts++;
                incorrectSecondAttempts++;
            }
        }

        // Update total score
        incorrectTwoAttempts = (incorrectFirstAttempts + incorrectSecondAttempts)/MAX_ATTEMPTS;
        totalScore.setNumGamesPlayed(numGamesPlayed);
        totalScore.setNumCorrectFirstAttempt(firstAttemptCorrect);
        totalScore.setNumCorrectSecondAttempt(secondAttemptCorrect);
        totalScore.setNumIncorrectTwoAttempts(incorrectTwoAttempts);
        totalScore.displayScore();

        // Ask if they want to play again
        while (true) {
            System.out.print("\nDo you want to play again? (Yes/No): ");
            String response = scan.nextLine().trim();
            if (response.equalsIgnoreCase("Yes")) return true;
            else if (response.equalsIgnoreCase("No")) return false;
            else System.out.println("Invalid input. Please enter Yes or No.");
        }
    }
}
