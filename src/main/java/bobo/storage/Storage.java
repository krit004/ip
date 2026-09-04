package bobo.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import bobo.exception.BoboException;
import bobo.task.Deadline;
import bobo.task.Event;
import bobo.task.Task;
import bobo.task.TaskList;
import bobo.task.Todo;

/**
 * Handles loading tasks from file and saving tasks into file.
 */
public class Storage {

    private final String filePath;

    /**
     * Constructs a Storage object with the specified file path.
     *
     * @param filePath Path to the storage file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Checks if storage file exists; creates file and parent directories if missing.
     */
    public void checkOrCreateFile() {
        File file = new File(filePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Could not create storage file: " + e.getMessage());
            }
        }
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return List of parsed tasks.
     * @throws BoboException If a critical read error occurs.
     */
    public List<Task> load() throws BoboException {
        List<Task> loadedList = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            checkOrCreateFile();
            return loadedList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] pieces = line.split("\\|");
                for (int i = 0; i < pieces.length; i++) {
                    pieces[i] = pieces[i].trim();
                }

                String type = pieces[0];
                boolean isDone = pieces.length > 1 && pieces[1].equals("1");

                Task task = null;
                if (type.equals("T")) {
                    if (pieces.length > 2) {
                        task = new Todo(pieces[2]);
                    }
                } else if (type.equals("D")) {
                    if (pieces.length > 3) {
                        task = new Deadline(pieces[2], pieces[3]);
                    }
                } else if (type.equals("E")) {
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
            throw new BoboException("Read error while loading file: " + e.getMessage());
        }
        return loadedList;
    }

    /**
     * Saves tasks from TaskList to the storage file.
     *
     * @param taskList TaskList containing tasks to save.
     * @throws BoboException If a write error occurs.
     */
    public void save(TaskList taskList) throws BoboException {
        checkOrCreateFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : taskList.getTasks()) {
                writer.write(task.toTextLine());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new BoboException("Write error while saving file: " + e.getMessage());
        }
    }
}
