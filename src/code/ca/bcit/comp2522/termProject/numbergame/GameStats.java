package ca.bcit.comp2522.termProject.numbergame;

public interface GameStats
{
    void recordWin();
    void recordLoss(final int successfulPlacements);
    String getStatistics();
}

