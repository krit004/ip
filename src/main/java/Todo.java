public class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    @Override
    public String toTextLine() {
        return "T | " + super.toTextLine();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
