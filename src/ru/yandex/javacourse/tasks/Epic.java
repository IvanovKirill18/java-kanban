package ru.yandex.javacourse.tasks;


import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description){
        super(name, description, TaskStatus.NEW);
    }

    public Epic (Epic original) {
        super(original);
        this.subtaskIds = new ArrayList<>(original.subtaskIds);
    }
    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId (int subtaksId) {
        if (!subtaskIds.contains(subtaksId)) {
            this.subtaskIds.add(subtaksId);
        }
    }

    public void removeSubtaskId (int subtaskId) {
        this.subtaskIds.remove(Integer.valueOf(subtaskId));
    }

    public void clearSubtaskIds() {
        subtaskIds.clear();
    }

    @Override
    public String toString() {
    return "Epic{" + "name'" + getName() + '\'' + ", description='" + getDescription() + '\'' + ", id =" + getId() + ", status=" + getStatus() + ", subtaskIds=" + subtaskIds + '}';}
}
