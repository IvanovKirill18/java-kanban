package ru.yandex.javacourse.tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description){
        super(name, description, TaskStatus.NEW);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId (int subtaksId) {
        this.subtaskIds.add(subtaksId);
    }

    public void removeSubtaskId (int subtaskId) {
        this.subtaskIds.remove(Integer.valueOf(subtaskId));
    }

    @Override
    public String toString() {
    return "Epic{" + "name'" + getName() + '\'' + ", description='" + getDescription() + '\'' + ", id =" + getId() + ", status=" + getStatus() + ", subtaskIds=" + subtaskIds + '}';}
}
