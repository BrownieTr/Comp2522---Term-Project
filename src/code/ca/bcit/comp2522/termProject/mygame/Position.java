package ca.bcit.comp2522.termProject.mygame;

public class Position {
    private final int x;
    private final int y;

    public Position(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }

    protected int getX()
    {
        return x;
    }

    protected int getY()
    {
        return y;
    }

    public boolean equals(final Object obj)
    {
        if (!(obj instanceof Position other))
        {
            return false;
        }
        return this.x == other.x && this.y == other.y;
    }

    public int hashCode()
    {
        return 31 * x + y;
    }
}
