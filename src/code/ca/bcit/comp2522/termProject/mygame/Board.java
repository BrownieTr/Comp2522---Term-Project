package ca.bcit.comp2522.termProject.mygame;

public class Board {
    private final Animal[][] grid;

    public Board()
    {
        grid = new Animal[7][9];
    }

    public Animal getAnimalAt(final Position pos)
    {
        return grid[pos.getX()][pos.getY()];
    }

    public boolean moveAnimal(final Position from, final Position to)
    {
        Animal mover = getAnimalAt(from);
        if (mover != null && mover.canMoveTo(to))
        {
            Animal target = getAnimalAt(to);
            if (target == null || mover.canCapture(target))
            {
                grid[to.getX()][to.getY()] = mover;
                grid[from.getX()][from.getY()] = null;
                mover.setPosition(to);
                return true;
            }
        }
        return false;
    }

    public void placeAnimal(final Animal animal)
    {
        Position pos = animal.getPosition();
        grid[pos.getX()][pos.getY()] = animal;
    }
}
