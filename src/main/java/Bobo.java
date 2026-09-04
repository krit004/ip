/**
 * Main class for the Bobo task chatbot application.
 * Orchestrates Ui, Storage, TaskList, and Parser components.
 */
public class Bobo {

    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

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
     * Runs the main interactive loop for Bobo.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                isExit = Parser.executeCommand(fullCommand, tasks, ui, storage);
            } catch (BoboException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Main entry point of the Bobo application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new Bobo("store.txt").run();
    }
}


