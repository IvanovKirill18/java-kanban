package ru.yandex.javacourse.tasks;

public class Epic extends Task {
    public Epic(String name, String description){
        super(name, description, TaskStatus.NEW);
    }

    @Override
    public String toString() {
    return "Epic{" + "name'" + getName() + '\'' + ", description='" + getDescription() + '\'' + ", id =" + getId() + ", status=" + getStatus() + '}';}
}
