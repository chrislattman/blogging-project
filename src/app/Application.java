package app;

import java.util.Scanner;

/**
 * Description of class here.
 * 
 * @author
 *
 */
public class Application {

    /**
     * Constructor for Application class.
     */
    public Application() {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Entry point for the application. This runs until the user exits.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the blog!");
        String input;
        do {
            System.out.print("What would you like to do? Enter HELP ");
            System.out.print("for help or press enter/return to quit: ");
            input = scanner.nextLine();
            if (input.toLowerCase().strip().equals("help")) {
                helpMenu();
            }
            else if (input.length() == 0) {
                break;
            }
            else {
                invalidInput();
            }
        }
        while (input.length() > 0);
        System.out.println("Thanks for using the blog!");
        scanner.close();
    }

    /**
     * Prints out a help menu.
     */
    public void helpMenu() {
        System.out.println("HELP: prints out a help menu");
    }
    
    /**
     * Default message for invalid input.
     */
    public void invalidInput() {
        System.out.println("You entered invalid input.");
    }
}
