package ca.bcit.comp2522.termProject.mygame;

public class GameView
{
    private final Tile.TileType[][] terrain = new Tile.TileType[7][9];

    public Tile.TileType getTileType(final Position pos) {
        return terrain[pos.getX()][pos.getY()];
    }
}
