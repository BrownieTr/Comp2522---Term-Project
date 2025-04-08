package ca.bcit.comp2522.termProject.mygame;

public class Rat extends Animal
{
    public Rat(Position position, boolean isPlayerOne)
    {
        super("Rat", 1, position, isPlayerOne);
    }

    @Override
    public boolean canMoveTo(final Position target)
    {
        int dx = Math.abs(this.getPosition().getX() - target.getX());
        int dy = Math.abs(this.getPosition().getY() - target.getY());
        return dx + dy == 1; // Only move to adjacent cell
    }
}

