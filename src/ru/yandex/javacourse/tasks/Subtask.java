package ru.yandex.javacourse.tasks;

public class Subtask extends Task {
    private  int epicId;

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public  Subtask (Subtask original) {
        super(original);
        this.epicId = original.epicId;
    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) { this.epicId = epicId; }

    @Override
    public String toString() {
        return "Subtask{" + "epicId=" + epicId + ", name='" + getName() + '\'' + ", description='" + getDescription() + '\'' + ", id=" + getId() + ", status=" + getStatus() + '}';
    }

}
