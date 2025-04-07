package ca.bcit.comp2522.termProject.wordGame;

import java.io.File;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a player's score for the Word Game.
 * Tracks game stats, evaluates performance, stores/retrieves high scores.
 */
public class Score {
    private final LocalDateTime dateTimePlayed;
    private int numGamesPlayed;
    private int numCorrectFirstAttempt;
    private int numCorrectSecondAttempt;
    private int numIncorrectTwoAttempts;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructs a Score object with gameplay stats.
     */
    public Score(final LocalDateTime dateTimePlayed,
                 final int numGamesPlayed,
                 final int numCorrectFirstAttempt,
                 final int numCorrectSecondAttempt,
                 final int numIncorrectTwoAttempts) {
        this.dateTimePlayed = dateTimePlayed;
        this.numGamesPlayed = numGamesPlayed;
        this.numCorrectFirstAttempt = numCorrectFirstAttempt;
        this.numCorrectSecondAttempt = numCorrectSecondAttempt;
        this.numIncorrectTwoAttempts = numIncorrectTwoAttempts;
    }

    // Setters for updating statistics after each round
    protected void setNumGamesPlayed(final int numGamesPlayed) {
        this.numGamesPlayed += numGamesPlayed;
    }

    protected void setNumCorrectFirstAttempt(final int numCorrectFirstAttempt) {
        this.numCorrectFirstAttempt += numCorrectFirstAttempt;
    }

    protected void setNumCorrectSecondAttempt(final int numCorrectSecondAttempt) {
        this.numCorrectSecondAttempt += numCorrectSecondAttempt;
    }

    protected void setNumIncorrectTwoAttempts(final int numIncorrectTwoAttempts) {
        this.numIncorrectTwoAttempts += numIncorrectTwoAttempts;
    }

    /**
     * Returns the average score per game.
     */
    protected double getAverageScore() {
        return numGamesPlayed == 0 ? 0.0 : (double) getScore() / numGamesPlayed;
    }

    protected LocalDateTime getDateTimePlayed() {
        return dateTimePlayed;
    }

    protected int getNumGamesPlayed() {
        return numGamesPlayed;
    }

    /**
     * Calculates the total score.
     * First attempt = 2 points, second attempt = 1 point.
     */
    protected int getScore() {
        return (numCorrectFirstAttempt * 2) + numCorrectSecondAttempt;
    }

    /**
     * Displays current stats in a user-friendly format.
     */
    protected void displayScore() {
        System.out.printf("""
                          - %d word games played
                          - %d correct answer on the first attempt
                          - %d correct answer on the second attempt
                          - %d incorrect answer on two attempts each
                          """,
                numGamesPlayed, numCorrectFirstAttempt, numCorrectSecondAttempt, numIncorrectTwoAttempts);
    }

    /**
     * Compares current score with previous high scores and displays a congratulatory message
     * if the new score is higher.
     */
    protected void congratulation() {
        String fileName = "score.txt";

        try
        {
            List<Score> scores = readScoresFromFile(fileName);

            // Find the highest previous score by average points per game
            Score highestScore = scores.stream()
                    .filter(score -> score.getNumGamesPlayed() > 0)
                    .max(Comparator.comparingDouble(Score::getAverageScore))
                    .orElse(null);

            // Compare and print messages accordingly
            if (highestScore != null && this != highestScore &&
                    this.getAverageScore() > highestScore.getAverageScore())
            {
                System.out.printf("%nCONGRATULATIONS! You are the new high score with an average of %.2f points per game; " +
                                "the previous record was %.2f points per game on %s%n",
                        this.getAverageScore(), highestScore.getAverageScore(), highestScore.getDateTimePlayed().format(FORMATTER));
            } else if (highestScore != null && this != highestScore)
            {
                System.out.printf("%nYou did not beat the high score of %.2f points per game from %s%n",
                        highestScore.getAverageScore(), highestScore.getDateTimePlayed().format(FORMATTER));
            }

            // Save current score to file
            appendScoreToFile(this, fileName);

        } catch (IOException e)
        {
            System.out.println("Failed to save score: " + e.getMessage());
        }
    }

    /**
     * Converts Score object to a formatted string for file saving and display.
     */
    @Override
    public String toString()
    {
        return String.format("""
                             Date and Time: %s
                             Games Played: %d
                             Correct First Attempts: %d
                             Correct Second Attempts: %d
                             Incorrect Attempts: %d
                             Score: %d points
                             """,
                dateTimePlayed.format(FORMATTER),
                numGamesPlayed,
                numCorrectFirstAttempt,
                numCorrectSecondAttempt,
                numIncorrectTwoAttempts,
                getScore()
        );
    }

    /**
     * Appends a score to the text file.
     *
     * @param score    the Score object to save
     * @param fileName file to write to
     * @throws IOException if writing fails
     */
    public static void appendScoreToFile(Score score, String fileName) throws IOException
    {
        try (FileWriter fw = new FileWriter(fileName, true))
        {
            fw.write(score.toString());
            fw.write(System.lineSeparator());
        }
    }

    /**
     * Reads all previous scores from the score file and parses them into Score objects.
     *
     * @param fileName file to read
     * @return a list of Score objects
     * @throws IOException if reading fails
     */
    public static List<Score> readScoresFromFile(String fileName) throws IOException
    {
        List<Score> scores = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists())
        {
            return scores;
        }

        try (FileReader fr = new FileReader(file))
        {
            StringBuilder content = new StringBuilder();
            int ch;
            while ((ch = fr.read()) != -1)
            {
                content.append((char) ch);
            }

            // Split by double newlines to separate entries
            String[] entries = content.toString().replace("\r\n", "\n").trim().split("\n\n");

            for (String entry : entries)
            {
                String[] lines = entry.split("\n");
                if (lines.length >= 6)
                {
                    // Parse values from each line using known format
                    LocalDateTime dateTime = LocalDateTime.parse(lines[0].substring(14).trim(), FORMATTER);
                    int gamesPlayed = Integer.parseInt(lines[1].split(": ")[1]);
                    int correctFirst = Integer.parseInt(lines[2].split(": ")[1]);
                    int correctSecond = Integer.parseInt(lines[3].split(": ")[1]);
                    int incorrect = Integer.parseInt(lines[4].split(": ")[1]);

                    // Reconstruct the Score object
                    scores.add(new Score(dateTime, gamesPlayed, correctFirst, correctSecond, incorrect));
                }
            }
        }

        return scores;
    }
}
