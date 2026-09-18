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
 * Parses user input commands and executes the corresponding actions on
 * TaskList, Ui, and Storage.
 */
public class Parser {

    /**
     * Executes the command given in the text input.
     *
     * @param text    Raw user input string.
     * @param tasks   TaskList instance to manipulate.
     * @param ui      Ui instance for output.
     * @param storage Storage instance for saving changes.
     * @return true if command is 'bye' (signals exit), false otherwise.
     * @throws BoboException If input command is invalid or parameters are missing.
     */
    public static boolean executeCommand(String text, TaskList tasks, Ui ui, Storage storage) throws BoboException {
        assert tasks != null : "TaskList instance passed to Parser cannot be null";
        assert ui != null : "Ui instance passed to Parser cannot be null";
        assert storage != null : "Storage instance passed to Parser cannot be null";

        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        String trimmedText = text.trim();

        if (trimmedText.equals("bye")) {
            ui.showBye();
            return true;
        } else if (trimmedText.equals("help")) {
            ui.showHelp();
        } else if (trimmedText.equals("list")) {
            ui.showTaskList(tasks);
        } else if (trimmedText.startsWith("unmark")) {
            executeToggleMark(trimmedText.substring(6).trim(), "unmark", false, tasks, ui, storage);
        } else if (trimmedText.startsWith("mark")) {
            executeToggleMark(trimmedText.substring(4).trim(), "mark", true, tasks, ui, storage);
        } else if (trimmedText.startsWith("todo")) {
            executeTodo(trimmedText, tasks, ui, storage);
        } else if (trimmedText.startsWith("deadline")) {
            executeDeadline(trimmedText, tasks, ui, storage);
        } else if (trimmedText.startsWith("event")) {
            executeEvent(trimmedText, tasks, ui, storage);
        } else if (trimmedText.startsWith("delete")) {
            executeDelete(trimmedText, tasks, ui, storage);
        } else if (trimmedText.startsWith("on ")) {
            executeOnDate(trimmedText, tasks, ui);
        } else if (trimmedText.startsWith("find")) {
            executeFind(trimmedText, tasks, ui);
        } else {
            executeAddFallback(trimmedText, tasks, ui, storage);
        }

        return false;
    }

    private static void executeToggleMark(String arg, String commandName, boolean isDone,
            TaskList tasks, Ui ui, Storage storage) throws BoboException {
        int number = parseTaskIndex(arg, commandName);
        Task task = isDone ? tasks.mark(number) : tasks.unmark(number);
        assert task != null : "Toggled task must not be null";
        storage.save(tasks);
        ui.showTaskMarked(task, isDone);
    }

    private static void executeTodo(String trimmedText, TaskList tasks, Ui ui, Storage storage)
            throws BoboException {
        String description = trimmedText.length() > 4 ? trimmedText.substring(4).trim() : "";
        if (description.isEmpty()) {
            throw new BoboException("OOPS!!! The description of a todo cannot be empty.");
        }
        Task task = new Todo(description);
        addAndSaveTask(task, tasks, ui, storage);
    }

    private static void executeDeadline(String trimmedText, TaskList tasks, Ui ui, Storage storage)
            throws BoboException {
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
        addAndSaveTask(task, tasks, ui, storage);
    }

    private static void executeEvent(String trimmedText, TaskList tasks, Ui ui, Storage storage)
            throws BoboException {
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
        addAndSaveTask(task, tasks, ui, storage);
    }

    private static void executeDelete(String trimmedText, TaskList tasks, Ui ui, Storage storage)
            throws BoboException {
        String arg = trimmedText.substring(6).trim();
        int number = parseTaskIndex(arg, "delete");
        Task task = tasks.delete(number);
        assert task != null : "Deleted task must not be null";
        storage.save(tasks);
        ui.showTaskRemoved(task, tasks.size());
    }

    private static void executeOnDate(String trimmedText, TaskList tasks, Ui ui) throws BoboException {
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
        assert matchingTasks != null : "Matching tasks list must not be null";
        ui.showTasksOnDate(targetDate, matchingTasks);
    }

    private static void executeFind(String trimmedText, TaskList tasks, Ui ui) throws BoboException {
        String keyword = trimmedText.length() > 4 ? trimmedText.substring(4).trim() : "";
        if (keyword.isEmpty()) {
            throw new BoboException("OOPS!!! The search keyword for find cannot be empty.");
        }
        List<Task> matchingTasks = tasks.findTasks(keyword);
        assert matchingTasks != null : "Matching tasks list must not be null";
        ui.showMatchingTasks(matchingTasks);
    }

    private static void executeAddFallback(String trimmedText, TaskList tasks, Ui ui, Storage storage)
            throws BoboException {
        Task task = new Todo(trimmedText);
        assert task != null : "Created Todo task must not be null";
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    private static void addAndSaveTask(Task task, TaskList tasks, Ui ui, Storage storage) throws BoboException {
        assert task != null : "Created task object must not be null";
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Parses a string representation of a 1-based task index into an integer.
     *
     * @param input       Raw index argument string.
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
