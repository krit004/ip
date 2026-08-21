import java.util.Scanner;
import java.util.ArrayList; 

public class Bobo {
    public static void main(String[] args) {
        ArrayList<String> lst = new ArrayList<>();
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
            if (text.equals("list")) {
                for (int i = 0; i < lst.size(); i++) {
                    System.out.println(lst.get(i));
                }

            }
            else if (text.length() > 6 && (text.substring(0, 6).equals("unmark"))) {
                char lastChar = text.charAt(text.length() - 1);
                int number = Character.getNumericValue(lastChar); 
                String goo = lst.get(number - 1);
                if (goo.contains("[X]")) {
                    goo = goo.replace("[X]", "[]");
                    lst.set(number - 1, goo);
                    System.out.println("OK, I've marked this task as not done yet: " + lst.get(number - 1));
                } else {
                    System.out.println("Error: Task is already unmarked or invalid!");
                }
            }
            else if (text.length() > 4 && (text.substring(0, 4).equals("mark"))) {
                char lastChar = text.charAt(text.length() - 1);
                int number = Character.getNumericValue(lastChar); 
                String goo = lst.get(number - 1);
                if (goo.contains("[]")) {
                    goo = goo.replace("[]", "[X]");
                    lst.set(number - 1, goo);
                    System.out.println("Nice! I've marked this task as done: " + lst.get(number - 1));
                } else {
                    System.out.println("Error: Task is already marked or invalid!");
                }
            }
            else {
                lst.add((lst.size() + 1) + ". [] " + text);
                System.out.println("added: " + text);
            }
            text = scanner.nextLine(); 

        }
        
        // 4. Close the scanner to prevent memory leaks
        scanner.close();
        System.out.println("\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________");

        }
        
}
