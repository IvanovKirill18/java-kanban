import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Integer> subtaskIds;



    public Epic(String name, String description, int id, TaskStatus status) {
        super(name, description, id, status);
        this.subtaskIds = new HashMap<>();
    }

    public HashMap<Integer, Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.put(subtaskId, subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" + "subtaskIds=" + subtaskIds + ", name='" + getName() + '\'' + ", description='" + getDescription() + '\'' + ", id=" + getId() + ", status=" + getStatus() + '}';
    }
}
