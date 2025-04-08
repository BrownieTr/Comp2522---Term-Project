package ca.bcit.comp2522.termProject.mygame;

public abstract class Animal
{
    private final String name;
    private final int rank;
    private Position position;
    private final boolean isPlayerOne;

    public Animal(final String name, final int rank, final Position position, final boolean isPlayerOne)
    {
        this.name = name;
        this.rank = rank;
        this.position = position;
        this.isPlayerOne = isPlayerOne;
    }

    protected Position getPosition()
    {
        return position;
    }

    protected void setPosition(final Position newPos)
    {
        this.position = newPos;
    }

    public boolean canCapture(final Animal target)
    {
        return target != null && target.isPlayerOne != this.isPlayerOne && this.rank >= target.rank;
    }

    public abstract boolean canMoveTo(final Position target);
}
