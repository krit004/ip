package bobo.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import bobo.task.Task;
import bobo.task.TaskList;
import bobo.util.DateUtil;

/**
 * Handles all user interactions, input reading, and console/GUI outputs for Bobo.
 */
public class Ui {

    private static final String DIVIDER_LINE = "    ____________________________________________________________";

    private final Scanner scanner;
    private final StringBuilder responseBuffer = new StringBuilder();

    /**
     * Constructs a Ui object initializing standard input scanner.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    private void output(String text) {
        responseBuffer.append(text).append("\n");
        System.out.println("     " + text);
    }

    /**
     * Clears the current response buffer.
     */
    public void clearResponseBuffer() {
        responseBuffer.setLength(0);
    }

    /**
     * Returns the collected response buffer string and clears it.
     *
     * @return The formatted response string.
     */
    public String getResponseBuffer() {
        String result = responseBuffer.toString().trim();
        clearResponseBuffer();
        return result;
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
        output("Hello! I'm Bobo.");
        output("What can I do for you?");
    }

    /**
     * Prints a decorative divider line.
     */
    public void showLine() {
        System.out.println(DIVIDER_LINE);
    }

    /**
     * Displays a loading error message when file loading fails.
     */
    public void showLoadingError() {
        output("Error loading task data file. Starting with an empty task list.");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message content.
     */
    public void showError(String message) {
        output(message);
    }

    /**
     * Displays a generic message to the user.
     *
     * @param message Message to display.
     */
    public void showMessage(String message) {
        output(message);
    }

    /**
     * Displays confirmation after adding a task.
     *
     * @param task       The added task.
     * @param totalTasks Current total number of tasks.
     */
    public void showTaskAdded(Task task, int totalTasks) {
        output("Got it. I've added this task:");
        output("  " + task);
        output("Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Displays confirmation after deleting a task.
     *
     * @param task       The removed task.
     * @param totalTasks Remaining total number of tasks.
     */
    public void showTaskRemoved(Task task, int totalTasks) {
        output("Noted. I've removed this task:");
        output("  " + task);
        output("Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Displays confirmation after marking or unmarking a task.
     *
     * @param task   The modified task.
     * @param isDone Status whether marked done or not done.
     */
    public void showTaskMarked(Task task, boolean isDone) {
        if (isDone) {
            output("Nice! I've marked this task as done:");
        } else {
            output("OK, I've marked this task as not done yet:");
        }
        output("  " + task);
    }

    /**
     * Displays all tasks in the given task list.
     *
     * @param taskList The TaskList instance to display.
     */
    public void showTaskList(TaskList taskList) {
        printTaskList(taskList.getTasks(), "Here are the tasks in your list:", "Your task list is currently empty.");
    }

    /**
     * Displays tasks occurring on a specified date.
     *
     * @param targetDate    The date queried.
     * @param matchingTasks List of matching tasks.
     */
    public void showTasksOnDate(LocalDate targetDate, List<Task> matchingTasks) {
        String formattedDate = DateUtil.formatForDisplay(targetDate);
        printTaskList(matchingTasks, "Here are the tasks occurring on " + formattedDate + ":",
                "No tasks found on " + formattedDate + ".");
    }

    /**
     * Displays tasks matching a search keyword.
     *
     * @param matchingTasks List of matching tasks to display.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        printTaskList(matchingTasks, "Here are the matching tasks in your list:",
                "No matching tasks found in your list.");
    }

    private void printTaskList(List<Task> tasks, String headerMessage, String emptyMessage) {
        if (tasks.isEmpty()) {
            output(emptyMessage);
            return;
        }
        output(headerMessage);
        for (int i = 0; i < tasks.size(); i++) {
            output((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays farewell message when exiting Bobo.
     */
    public void showBye() {
        output("Bye. Hope to see you again soon!");
    }
}
