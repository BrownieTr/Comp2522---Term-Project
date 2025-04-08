//package ca.bcit.comp2522.termProject.mygame;
//
//public class MyGame
//{
//    private final Board board;
//    private final GameView view;
//    private final Animal selected;
//    private boolean isPlayerOneTurn = true;
//
//    public GameController(Board board, GameView view) {
//        this.board = board;
//        this.view = view;
//        this.view.update(board);
//    }
//
//    public void handleClick(int row, int col) {
//        Position pos = new Position(row, col);
//        Animal clicked = board.getAnimalAt(pos);
//
//        if (selected == null) {
//            if (clicked != null && clicked.isPlayerOne == isPlayerOneTurn) {
//                selected = clicked;
//            }
//        } else {
//            if (board.moveAnimal(selected.getPosition(), pos)) {
//                isPlayerOneTurn = !isPlayerOneTurn;
//            }
//            selected = null;
//        }
//
//        view.update(board);
//    }
//}
