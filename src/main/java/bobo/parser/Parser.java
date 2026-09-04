package bobo.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import bobo.exception.BoboException;
import bobo.storage.Storage;
import bobo.task.Deadline;
import bobo.task.Event;
import bobo.task.Task;
import bobo.task.TaskList;
import bobo.task.Todo;
import bobo.ui.Ui;
import bobo.util.DateUtil;

/**
 * Parses user input commands and executes the corresponding actions on TaskList, Ui, and Storage.
 */
public class Parser {

    /**
     * Executes the command given in the text input.
     *
     * @param text Raw user input string.
     * @param tasks TaskList instance to manipulate.
     * @param ui Ui instance for output.
     * @param storage Storage instance for saving changes.
     * @return true if command is 'bye' (signals exit), false otherwise.
     * @throws BoboException If input command is invalid or parameters are missing.
     */
    public static boolean executeCommand(String text, TaskList tasks, Ui ui, Storage storage) throws BoboException {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        String trimmedText = text.trim();

        if (trimmedText.equals("bye")) {
            ui.showBye();
            return true;
        }

        if (trimmedText.equals("list")) {
            ui.showTaskList(tasks);
            return false;
        }

        if (trimmedText.startsWith("unmark")) {
            String arg = trimmedText.substring(6).trim();
            int number = parseTaskIndex(arg, "unmark");
            Task task = tasks.unmark(number);
            storage.save(tasks);
            ui.showTaskMarked(task, false);
            return false;
        }

        if (trimmedText.startsWith("mark")) {
            String arg = trimmedText.substring(4).trim();
            int number = parseTaskIndex(arg, "mark");
            Task task = tasks.mark(number);
            storage.save(tasks);
            ui.showTaskMarked(task, true);
            return false;
        }

        if (trimmedText.startsWith("todo")) {
            String description = trimmedText.length() > 4 ? trimmedText.substring(4).trim() : "";
            if (description.isEmpty()) {
                throw new BoboException("OOPS!!! The description of a todo cannot be empty.");
            }
            Task task = new Todo(description);
            tasks.add(task);
            storage.save(tasks);
            ui.showTaskAdded(task, tasks.size());
            return false;
        }

        if (trimmedText.startsWith("deadline")) {
            String content = trimmedText.length() > 8 ? trimmedText.substring(8).trim() : "";
            String[] parts = content.split(" /by ", 2);
            String description = parts[0].trim();
            String by = parts.length > 1 ? parts[1].trim() : "";

            if (description.isEmpty()) {
                throw new BoboException("OOPS!!! The description of a deadline cannot be empty.");
            }
            if (by.isEmpty()) {
                throw new BoboException("OOPS!!! The deadline of a deadline cannot be empty.");
            }

            Task task = new Deadline(description, by);
            tasks.add(task);
            storage.save(tasks);
            ui.showTaskAdded(task, tasks.size());
            return false;
        }

        if (trimmedText.startsWith("event")) {
            String content = trimmedText.length() > 5 ? trimmedText.substring(5).trim() : "";
            String[] parts = content.split(" /from ", 2);
            String description = parts[0].trim();
            String from = "";
            String to = "";
            if (parts.length > 1) {
                String[] timeParts = parts[1].split(" /to ", 2);
                from = timeParts[0].trim();
                to = timeParts.length > 1 ? timeParts[1].trim() : "";
            }

            if (description.isEmpty()) {
                throw new BoboException("OOPS!!! The description of an event cannot be empty.");
            }
            if (from.isEmpty()) {
                throw new BoboException("OOPS!!! The from of an event cannot be empty.");
            }
            if (to.isEmpty()) {
                throw new BoboException("OOPS!!! The to of an event cannot be empty.");
            }

            Task task = new Event(description, from, to);
            tasks.add(task);
            storage.save(tasks);
            ui.showTaskAdded(task, tasks.size());
            return false;
        }

        if (trimmedText.startsWith("delete")) {
            String arg = trimmedText.substring(6).trim();
            int number = parseTaskIndex(arg, "delete");
            Task task = tasks.delete(number);
            storage.save(tasks);
            ui.showTaskRemoved(task, tasks.size());
            return false;
        }

        if (trimmedText.startsWith("on ")) {
            String dateStr = trimmedText.substring(3).trim();
            LocalDate targetDate = DateUtil.parseDate(dateStr);
            if (targetDate == null) {
                LocalDateTime dt = DateUtil.parseDateTime(dateStr);
                if (dt != null) {
                    targetDate = dt.toLocalDate();
                }
            }
            if (targetDate == null) {
                throw new BoboException("Please specify a valid date (e.g., yyyy-MM-dd or d/M/yyyy).");
            }
            List<Task> matchingTasks = tasks.getTasksOnDate(targetDate);
            ui.showTasksOnDate(targetDate, matchingTasks);
            return false;
        }

        if (trimmedText.startsWith("find")) {
            String keyword = trimmedText.length() > 4 ? trimmedText.substring(4).trim() : "";
            if (keyword.isEmpty()) {
                throw new BoboException("OOPS!!! The search keyword for find cannot be empty.");
            }
            List<Task> matchingTasks = tasks.findTasks(keyword);
            ui.showMatchingTasks(matchingTasks);
            return false;
        }

        // Fallback for generic item addition or unknown command
        Task task = new Todo(trimmedText);
        tasks.add(task);
        storage.save(tasks);
        ui.showMessage("added: " + trimmedText);
        return false;
    }

    /**
     * Parses a string representation of a 1-based task index into an integer.
     *
     * @param input Raw index argument string.
     * @param commandName Name of the command requesting the index.
     * @return Parsed 1-based integer task index.
     * @throws BoboException If input is empty or not a valid integer.
     */
    private static int parseTaskIndex(String input, String commandName) throws BoboException {
        if (input.isEmpty()) {
            throw new BoboException("OOPS!!! Please specify a task number for " + commandName + ".");
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new BoboException("Error: Invalid task number!");
        }
    }
}
