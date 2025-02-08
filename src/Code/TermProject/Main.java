package TermProject;

import java.util.Scanner;


public class Main
{
    public static void main(final String[] args)
    {
        Scanner input;
        String choice;

        input = new Scanner(System.in);

        do
        {
            System.out.println("Press W to play the Word game.");
            System.out.println("Press N to play the Number game.");
            System.out.println("Press M to play the <your game's name> game.");
            System.out.println("Press Q to quit.");

            choice = input.nextLine();

            switch (choice.toLowerCase())
            {
                case "w" ->
                {
                    System.out.println("Working");
                    World world = new World();
                }
                case "n" -> System.out.println("Working2");
                case "m" -> System.out.println("Working3");
                case "q" -> System.out.println("Exiting...");
                default -> System.out.println("Invalid input! Please try again!");
            }

        } while (!choice.equalsIgnoreCase("q"));

        input.close();
    }
}
