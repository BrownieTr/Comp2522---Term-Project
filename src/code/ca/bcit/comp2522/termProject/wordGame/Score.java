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

public class Score {
    private final LocalDateTime dateTimePlayed;
    private int numGamesPlayed;
    private int numCorrectFirstAttempt;
    private int numCorrectSecondAttempt;
    private int numIncorrectTwoAttempts;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Score(final LocalDateTime dateTimePlayed,
                 final int numGamesPlayed, final int numCorrectFirstAttempt,
                 final int numCorrectSecondAttempt, final int numIncorrectTwoAttempts) {
        this.dateTimePlayed = dateTimePlayed;
        this.numGamesPlayed = numGamesPlayed;
        this.numCorrectFirstAttempt = numCorrectFirstAttempt;
        this.numCorrectSecondAttempt = numCorrectSecondAttempt;
        this.numIncorrectTwoAttempts = numIncorrectTwoAttempts;
    }

    protected void setNumGamesPlayed(final int numGamesPlayed)
    {
        this.numGamesPlayed +=  numGamesPlayed;
    }

    protected void setNumCorrectFirstAttempt(final int numCorrectFirstAttempt)
    {
        this.numCorrectFirstAttempt += numCorrectFirstAttempt;
    }

    protected void setNumCorrectSecondAttempt(final int numCorrectSecondAttempt)
    {
        this.numCorrectSecondAttempt += numCorrectSecondAttempt;
    }

    protected void setNumIncorrectTwoAttempts(final int numIncorrectTwoAttempts)
    {
        this.numIncorrectTwoAttempts += numIncorrectTwoAttempts;
    }

    protected double getAverageScore()
    {
        return numGamesPlayed == 0 ? 0.0 : (double) getScore() / numGamesPlayed;
    }

    protected LocalDateTime getDateTimePlayed()
    {
        return dateTimePlayed;
    }

    protected int getNumGamesPlayed() {
        return numGamesPlayed;
    }

    protected int getScore() {
        return (numCorrectFirstAttempt * 2) + numCorrectSecondAttempt;
    }

    protected void displayScore()
    {
        System.out.printf("""
                          - %d word games played
                          - %d correct answer on the first attempt
                          - %d correct answer on the second attempt
                          - %d incorrect answer on two attempts each
                          """,
                numGamesPlayed, numCorrectFirstAttempt, numCorrectSecondAttempt, numIncorrectTwoAttempts);
    }

    protected void congratulation() {
        String fileName = "score.txt";
        try {
            appendScoreToFile(this, fileName);
            List<Score> scores = readScoresFromFile(fileName);

            Score highestScore = scores.stream()
                    .filter(score -> score.getNumGamesPlayed() > 0)
                    .max(Comparator.comparingDouble(Score::getAverageScore))
                    .orElse(null);

            if (highestScore != null && this != highestScore && this.getAverageScore() > highestScore.getAverageScore()) {
                System.out.printf("%nCONGRATULATIONS! You are the new high score with an average of %.2f points per game; the previous record was %.2f points per game on %s%n",
                        this.getAverageScore(), highestScore.getAverageScore(), highestScore.getDateTimePlayed().format(FORMATTER));
            } else if (highestScore != null && this != highestScore) {
                System.out.printf("%nYou did not beat the high score of %.2f points per game from %s%n",
                        highestScore.getAverageScore(), highestScore.getDateTimePlayed().format(FORMATTER));
            }
        } catch (IOException e) {
            System.out.println("Failed to save score: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
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

    public static void appendScoreToFile(Score score, String fileName) throws IOException {
        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.write(score.toString());
            fw.write(System.lineSeparator());
        }
    }

    public static List<Score> readScoresFromFile(String fileName) throws IOException {
        List<Score> scores = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return scores;

        try (FileReader fr = new FileReader(file)) {
            StringBuilder content = new StringBuilder();
            int ch;
            while ((ch = fr.read()) != -1) {
                content.append((char) ch);
            }

            String[] entries = content.toString().replace("\r\n", "\n").trim().split("\n\n");

            for (String entry : entries) {
                String[] lines = entry.split("\n");
                if (lines.length >= 6) {
                    LocalDateTime dateTime = LocalDateTime.parse(lines[0].substring(14).trim(), FORMATTER);
                    int gamesPlayed = Integer.parseInt(lines[1].split(": ")[1]);
                    int correctFirst = Integer.parseInt(lines[2].split(": ")[1]);
                    int correctSecond = Integer.parseInt(lines[3].split(": ")[1]);
                    int incorrect = Integer.parseInt(lines[4].split(": ")[1]);
                    scores.add(new Score(dateTime, gamesPlayed, correctFirst, correctSecond, incorrect));
                }
            }
        }
        return scores;
    }
}
