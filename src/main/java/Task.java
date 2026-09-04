import java.time.LocalDate;

public class Task extends MemoryItem {
    protected String description;
    protected boolean isDone;


    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    /**
     * Checks if this task occurs on the specified date.
     *
     * @param targetDate The date to check against.
     * @return true if the task occurs on targetDate, false otherwise.
     */
    public boolean isOnDate(LocalDate targetDate) {
        return false;
    }

    @Override
    public String toTextLine() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
