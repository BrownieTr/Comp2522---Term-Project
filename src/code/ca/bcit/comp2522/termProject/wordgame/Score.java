package ca.bcit.comp2522.termProject.wordgame;

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
 *
 *  @author Brownie Tran
 *  @version 1.0
 */
public class Score {

    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;
    private static final int THIRD_INDEX = 2;
    private static final int FOURTH_INDEX = 3;
    private static final int FIFTH_INDEX = 4;
    private static final int DATE_TIME_DATE_START_CHARACTER = 14;

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
                 final int numIncorrectTwoAttempts)
    {
        this.dateTimePlayed = dateTimePlayed;
        this.numGamesPlayed = numGamesPlayed;
        this.numCorrectFirstAttempt = numCorrectFirstAttempt;
        this.numCorrectSecondAttempt = numCorrectSecondAttempt;
        this.numIncorrectTwoAttempts = numIncorrectTwoAttempts;
    }

    // Setters for updating statistics after each round
    protected final void setNumGamesPlayed(final int numGamesPlayed)
    {
        this.numGamesPlayed += numGamesPlayed;
    }

    protected final void setNumCorrectFirstAttempt(final int numCorrectFirstAttempt)
    {
        this.numCorrectFirstAttempt += numCorrectFirstAttempt;
    }

    protected final void setNumCorrectSecondAttempt(final int numCorrectSecondAttempt)
    {
        this.numCorrectSecondAttempt += numCorrectSecondAttempt;
    }

    protected final void setNumIncorrectTwoAttempts(final int numIncorrectTwoAttempts)
    {
        this.numIncorrectTwoAttempts += numIncorrectTwoAttempts;
    }

    /**
     * Returns the average score per game.
     */
    protected final double getAverageScore()
    {
        return numGamesPlayed == 0 ? 0.0 : (double) getScore() / numGamesPlayed;
    }

    protected final LocalDateTime getDateTimePlayed()
    {
        return dateTimePlayed;
    }

    protected final int getNumGamesPlayed()
    {
        return numGamesPlayed;
    }

    /**
     * Calculates the total score.
     * First attempt = 2 points, second attempt = 1 point.
     */
    protected final int getScore()
    {
        return (numCorrectFirstAttempt * 2) + numCorrectSecondAttempt;
    }

    /**
     * Displays current stats in a user-friendly format.
     */
    protected final void displayScore()
    {
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
    protected final void congratulation()
    {
        // File to store score
        String fileName = "score.txt";
        try
        {
            // Variables to handle high score congratulation
            final List<Score> scores;
            final Score highestScore;

            // Read all Score entry in the file
            scores = readScoresFromFile(fileName);

            // Find the highest previous score by average points per game
            highestScore = scores.stream()
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
    public final String toString()
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
    public final static void appendScoreToFile(final Score score, final String fileName) throws IOException
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
     * @param fileName the file to read from
     * @return a list of Score objects parsed from the file
     * @throws IOException if file reading fails
     */
    public final static List<Score> readScoresFromFile(final String fileName) throws IOException
    {
        final List<Score> scores;            // List to hold parsed Score objects
        final File file;                     // File object for the given file name
        final StringBuilder content;         // Holds the entire file content as a string
        int ch;                              // Character read from the file
        final String[] entries;              // Array of score blocks split by double newlines

        String[] lines;                      // Individual lines in a score entry
        LocalDateTime dateTimePlayed;        // Parsed timestamp
        int gamesPlayed;                     // Total games played
        int correctFirst;                    // Correct answers on first attempt
        int correctSecond;                   // Correct answers on second attempt
        int incorrect;                       // Incorrect answers after two attempts

        scores = new ArrayList<>();
        file = new File(fileName);

        // If the file does not exist, return an empty score list
        if (!file.exists())
        {
            return scores;
        }

        // Read entire file content character by character into a StringBuilder
        try (FileReader fr = new FileReader(file))
        {
            content = new StringBuilder();
            while ((ch = fr.read()) != -1)
            {
                content.append((char) ch);
            }

            // Normalize newlines and split entries by double newlines (empty line between scores)
            entries = content.toString().replace("\r\n", "\n").trim().split("\n\n");

            // Loop over each entry block
            for (final String entry : entries)
            {
                lines = entry.split("\n");
                if (lines.length >= 6)  // Ensure the entry has all expected lines
                {
                    // Extract date and time from the first line after "Date and Time: "
                    dateTimePlayed = LocalDateTime.parse(
                            lines[FIRST_INDEX].substring(DATE_TIME_DATE_START_CHARACTER).trim(), FORMATTER
                    );

                    // Extract numerical values from the formatted text lines
                    gamesPlayed = Integer.parseInt(lines[SECOND_INDEX].split(": ")[SECOND_INDEX]);
                    correctFirst = Integer.parseInt(lines[THIRD_INDEX].split(": ")[SECOND_INDEX]);
                    correctSecond = Integer.parseInt(lines[FOURTH_INDEX].split(": ")[SECOND_INDEX]);
                    incorrect = Integer.parseInt(lines[FIFTH_INDEX].split(": ")[SECOND_INDEX]);

                    // Create a new Score object and add to the list
                    scores.add(new Score(dateTimePlayed, gamesPlayed, correctFirst, correctSecond, incorrect));
                }
            }
        }

        return scores;
    }
}
