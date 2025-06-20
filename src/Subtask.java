public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int id, TaskStatus status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }



    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" + "epicId=" + epicId + ", name='" + getName() + '\'' + ", description='" + getDescription() + '\'' + ", id=" + getId() + ", status=" + getStatus() + '}';
    }

}
