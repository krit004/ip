import java.util.Scanner;

public class Bobo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); 

        String banner = " ____        _                \n" +
        "| __ )  ___ | |__   ___   \n" +
        "|  _ \\ / _ \\| '_ \\ / _ \\  \n" +
        "| |_) | (_) | |_) | (_) | \n" +
        "|____/ \\___/|_.__/ \\___/  \n";
        System.out.println(banner);
        System.out.println("Hello! I'm Bobo.\nWhat can I do for you?");
        String text = scanner.nextLine(); 
        
        while (!text.equals("bye")) {   
            System.out.println(text);
            text = scanner.nextLine(); 
        }
        
        // 4. Close the scanner to prevent memory leaks
        scanner.close();
        System.out.println("\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________");

        }
        
}
