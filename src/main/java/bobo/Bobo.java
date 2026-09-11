package bobo;

import bobo.exception.BoboException;
import bobo.parser.Parser;
import bobo.storage.Storage;
import bobo.task.TaskList;
import bobo.ui.Ui;

/**
 * Main class for the Bobo task chatbot application.
 * Orchestrates Ui, Storage, TaskList, and Parser components.
 */
public class Bobo {

    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;
    private boolean isExit = false;

    /**
     * Constructs a Bobo chatbot instance with default storage location.
     */
    public Bobo() {
        this("store.txt");
    }

    /**
     * Constructs a Bobo chatbot instance with storage at the given file path.
     *
     * @param filePath Path to the storage text file.
     */
    public Bobo(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (BoboException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Generates a response string for the user input.
     *
     * @param input Raw user input command.
     * @return Response message from Bobo.
     */
    public String getResponse(String input) {
        ui.clearResponseBuffer();
        try {
            isExit = Parser.executeCommand(input, tasks, ui, storage);
        } catch (BoboException e) {
            ui.showError(e.getMessage());
        }
        return ui.getResponseBuffer();
    }

    /**
     * Returns whether the last processed command was an exit command.
     *
     * @return True if command was 'bye', false otherwise.
     */
    public boolean isExit() {
        return isExit;
    }

    /**
     * Returns the welcome message for Bobo.
     *
     * @return Welcome greeting string.
     */
    public String getWelcomeMessage() {
        ui.clearResponseBuffer();
        ui.showWelcome();
        return ui.getResponseBuffer();
    }

    /**
     * Runs the main interactive CLI loop for Bobo.
     */
    public void run() {
        ui.showWelcome();
        boolean isExitCli = false;
        while (!isExitCli) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                isExitCli = Parser.executeCommand(fullCommand, tasks, ui, storage);
            } catch (BoboException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Main entry point of the Bobo CLI application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new Bobo("store.txt").run();
    }
}
