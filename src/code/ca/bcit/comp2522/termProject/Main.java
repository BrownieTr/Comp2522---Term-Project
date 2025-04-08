package ca.bcit.comp2522.termProject;

//import ca.bcit.comp2522.termProject.mygame.MyGame;
import ca.bcit.comp2522.termProject.numbergame.MainMenu;
import ca.bcit.comp2522.termProject.wordgame.WordGame;

import java.io.IOException;
import java.util.Scanner;

public class Main
{
    public static void main(final String[] args) throws IOException
    {
        Main main = new Main();
        main.menu(args);
    }

    private void menu(final String[] args) throws IOException
    {
        String choice = "";
        boolean run = true;
        Scanner scan = new Scanner(System.in);
        do
        {
            System.out.println("Welcome to the game console, " +
                    "what game do you want to play today?");
            choice = scan.nextLine();
            switch (choice)
            {
                case "w":
                case "W":
                    WordGame game = new WordGame();
                    game.run();
                    break;
                case "n":
                case "N":
                    // Start the game
                    MainMenu.main(args);
                    break;
                case "m":
                case "M":
//                    MyGame game3 = new MyGame();
//                    game3.run();
                    break;
                case "q":
                case "Q":
                    run = false;
                    System.out.println("Bye!");
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
                    break;
            }
        } while (run);
    }

}
