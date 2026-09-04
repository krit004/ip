package bobo.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import bobo.task.Task;
import bobo.task.TaskList;
import bobo.util.DateUtil;

/**
 * Handles all user interactions, input reading, and console outputs for Bobo.
 */
public class Ui {

    private final Scanner scanner;

    /**
     * Constructs a Ui object initializing standard input scanner.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a command line from the user.
     *
     * @return Raw command string input.
     */
    public String readCommand() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return "bye";
    }

    /**
     * Prints the welcome banner and initial greetings.
     */
    public void showWelcome() {
        String banner = " ____        _                \n"
                      + "| __ )  ___ | |__   ___   \n"
                      + "|  _ \\ / _ \\| '_ \\ / _ \\  \n"
                      + "| |_) | (_) | |_) | (_) | \n"
                      + "|____/ \\___/|_.__/ \\___/  \n";
        System.out.println(banner);
        System.out.println("     Hello! I'm Bobo.");
        System.out.println("     What can I do for you?");
    }

    /**
     * Prints a decorative divider line.
     */
    public void showLine() {
        System.out.println("    ____________________________________________________________");
    }

    /**
     * Displays a loading error message when file loading fails.
     */
    public void showLoadingError() {
        System.out.println("     Error loading task data file. Starting with an empty task list.");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message content.
     */
    public void showError(String message) {
        System.out.println("     " + message);
    }

    /**
     * Displays a generic message to the user.
     *
     * @param message Message to display.
     */
    public void showMessage(String message) {
        System.out.println("     " + message);
    }

    /**
     * Displays confirmation after adding a task.
     *
     * @param task The added task.
     * @param totalTasks Current total number of tasks.
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Displays confirmation after deleting a task.
     *
     * @param task The removed task.
     * @param totalTasks Remaining total number of tasks.
     */
    public void showTaskRemoved(Task task, int totalTasks) {
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Displays confirmation after marking or unmarking a task.
     *
     * @param task The modified task.
     * @param isDone Status whether marked done or not done.
     */
    public void showTaskMarked(Task task, boolean isDone) {
        if (isDone) {
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        System.out.println("       " + task);
    }

    /**
     * Displays all tasks in the given task list.
     *
     * @param taskList The TaskList instance to display.
     */
    public void showTaskList(TaskList taskList) {
        List<Task> tasks = taskList.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("     Your task list is currently empty.");
            return;
        }
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays tasks occurring on a specified date.
     *
     * @param targetDate The date queried.
     * @param matchingTasks List of matching tasks.
     */
    public void showTasksOnDate(LocalDate targetDate, List<Task> matchingTasks) {
        String formattedDate = DateUtil.formatForDisplay(targetDate);
        if (matchingTasks.isEmpty()) {
            System.out.println("     No tasks found on " + formattedDate + ".");
            return;
        }
        System.out.println("     Here are the tasks occurring on " + formattedDate + ":");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + matchingTasks.get(i));
        }
    }

    /**
     * Displays farewell message when exiting Bobo.
     */
    public void showBye() {
        System.out.println("     Bye. Hope to see you again soon!");
        scanner.close();
    }
}
