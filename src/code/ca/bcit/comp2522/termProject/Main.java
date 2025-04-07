package ca.bcit.comp2522.termProject;

import ca.bcit.comp2522.termProject.MyGame.MyGame;
import ca.bcit.comp2522.termProject.NumberGame.NumberGame;
import ca.bcit.comp2522.termProject.WordGame.WordGame;

import java.io.IOException;
import java.util.Scanner;

public class Main
{
    public static void main(final String[] args) throws IOException
    {
        Main main = new Main();
        main.menu();
    }

    private void menu() throws IOException
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
                    NumberGame game2 = new NumberGame();
//                    game2.run();
                    break;
                case "m":
                case "M":
                    MyGame game3 = new MyGame();
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
