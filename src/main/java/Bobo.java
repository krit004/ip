import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Bobo {
    public static String filename = "store.txt";

    public static void checkOrCreateFile(String filePath) {
        File file = new File(filePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            System.out.println("File already exists. Ready to use.");
        } else {
            try {
                // This creates the file physically on your drive
                boolean dynamicCreation = file.createNewFile();
                if (dynamicCreation) {
                    System.out.println("File did not exist. Created a new one: " + filePath);
                }
            } catch (IOException e) {
                System.err.println("Could not create file due to an error: " + e.getMessage());
            }
        }
    }

    public static List<Task> readFromTextFile(String filePath) {
        List<Task> loadedList = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return loadedList;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Split line pieces by pipe character
                String[] pieces = line.split("\\|");
                for (int i = 0; i < pieces.length; i++) {
                    pieces[i] = pieces[i].trim();
                }

                String type = pieces[0];
                boolean isDone = pieces.length > 1 && pieces[1].equals("1");

                Task task = null;
                if (type.equals("T")){
                    if (pieces.length > 2) {
                        task = new Todo(pieces[2]);
                    }
                } else if (type.equals("D")) {
                    if (pieces.length > 3) {
                        task = new Deadline(pieces[2], pieces[3]);
                    }
                } else if (type.equals("E")){
                    if (pieces.length > 4) {
                        task = new Event(pieces[2], pieces[3], pieces[4]);
                    }
                } else if (type.equals("Task")) {
                    if (pieces.length > 2) {
                        task = new Task(pieces[2]);
                    }
                }

                if (task != null) {
                    if (isDone) {
                        task.markAsDone();
                    }
                    loadedList.add(task);
                }
            }
        } catch (IOException e) {
            System.err.println("Read error: " + e.getMessage());
        }
        return loadedList;
    }

    // 3. Write array data when chatbot shuts down
    public static void writeToTextFile(String filePath, List<Task> activeArray) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (MemoryItem item : activeArray) {
                writer.write(item.toTextLine());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Write error: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        checkOrCreateFile(filename);
        ArrayList<Task> lst = new ArrayList<>(readFromTextFile(filename));
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
                
            } else if (text.startsWith("delete")) {
                int number = Integer.parseInt(text.substring(6).trim());
                if (number > 0 && number <= lst.size()) {
                    Task task = lst.get(number - 1);
                    lst.remove(number - 1);
                    System.out.println("     Noted. I've removed this task:");
                    System.out.println("       " + task);
                    System.out.println("     Now you have " + lst.size() + " tasks in the list.");
                } else {
                    System.out.println("     I'm sorry, but the task number you provided is out of bounds.");
                    text = scanner.nextLine();
                }
            } else {
                Task task = new Todo(text);
                lst.add(task);
                System.out.println("     added: " + text);
            }

        text = scanner.nextLine();
        }
        scanner.close();
        writeToTextFile(filename, lst);
        System.out.println("     Bye. Hope to see you again soon!");
    }
}

