import java.util.ArrayList;
import java.util.Scanner;

public class Bobo {

    public static void main(String[] args) {
        ArrayList<Task> lst = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String banner = " ____        _                \n" +
                        "| __ )  ___ | |__   ___   \n" +
                        "|  _ \\ / _ \\| '_ \\ / _ \\  \n" +
                        "| |_) | (_) | |_) | (_) | \n" +
                        "|____/ \\___/|_.__/ \\___/  \n";
        System.out.println(banner);
        System.out.println("     Hello! I'm Bobo.");
        System.out.println("     What can I do for you?");

        String text = scanner.nextLine();

        while (!text.equals("bye")) {
            if (text.equals("list")) {
                
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < lst.size(); i++) {
                    System.out.println("     " + (i + 1) + "." + lst.get(i));
                }
                
            } else if (text.startsWith("unmark")) {
                try {
                    int number = Integer.parseInt(text.substring(6).trim());
                    if (number > 0 && number <= lst.size()) {
                        Task task = lst.get(number - 1);
                        task.markAsNotDone();
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       " + task);
                    } else {
                        System.out.println("Error: Task number out of bounds!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid task number!");
                }
            } else if (text.startsWith("mark")) {
                try {
                    int number = Integer.parseInt(text.substring(4).trim());
                    if (number > 0 && number <= lst.size()) {
                        Task task = lst.get(number - 1);
                        task.markAsDone();
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       " + task);
                    } else {
                        System.out.println("Error: Task number out of bounds!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid task number!");
                }
            } else if (text.startsWith("todo")) {
                String description = text.length() > 4 ? text.substring(4).trim() : "";
                if (description.isEmpty()) {
                    System.out.println("OOPS!!! The description of a todo cannot be empty.");
                    text = scanner.nextLine();
                    continue;
                }
                else {
                    Task task = new Todo(description);
                    lst.add(task);
                    System.out.println("     Got it. I've added this task:");
                    System.out.println("       " + task);
                    System.out.println("     Now you have " + lst.size() + " tasks in the list.");
                }
            } else if (text.startsWith("deadline")) {
                String content = text.length() > 8 ? text.substring(8).trim() : "";
                String[] parts = content.split(" /by ", 2);
                String description = parts[0];
                String by = parts.length > 1 ? parts[1] : "";
                if (description.isEmpty()) {
                    System.out.println("OOPS!!! The description of a deadline cannot be empty.");
                    text = scanner.nextLine();
                    continue;
                }
                if (by.isEmpty()) {
                    System.out.println("OOPS!!! The deadline of a deadline cannot be empty.");
                    text = scanner.nextLine();
                    continue;
                }
                Task task = new Deadline(description, by);
                lst.add(task);
                System.out.println("     Got it. I've added this task:");
                System.out.println("       " + task);
                System.out.println("     Now you have " + lst.size() + " tasks in the list.");
                
            } else if (text.startsWith("event")) {
                String content = text.length() > 5 ? text.substring(5).trim() : "";
                String[] parts = content.split(" /from ", 2);
                String description = parts[0];
                String from = "";
                String to = "";
                if (parts.length > 1) {
                    String[] timeParts = parts[1].split(" /to ", 2);
                    from = timeParts[0];
                    to = timeParts.length > 1 ? timeParts[1] : "";
                }
                if (description.isEmpty()) {
                    System.out.println("OOPS!!! The description of an event cannot be empty.");
                    text = scanner.nextLine();
                    continue;
                }
                if (from.isEmpty()) {
                    System.out.println("OOPS!!! The from of an event cannot be empty.");
                    text = scanner.nextLine();
                    continue;
                }
                if (to.isEmpty()) {
                    System.out.println("OOPS!!! The to of an event cannot be empty.");
                    text = scanner.nextLine();
                    continue;
                }
                Task task = new Event(description, from, to);
                lst.add(task);
                System.out.println("     Got it. I've added this task:");
                System.out.println("       " + task);
                System.out.println("     Now you have " + lst.size() + " tasks in the list.");
                
            } else {
                Task task = new Task(text);
                lst.add(task);
                System.out.println("     added: " + text);
            }

            text = scanner.nextLine();
        }

        scanner.close();
        System.out.println("     Bye. Hope to see you again soon!");
        
    }
}
