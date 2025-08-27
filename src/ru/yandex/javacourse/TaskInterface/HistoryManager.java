package ru.yandex.javacourse.TaskInterface;

import ru.yandex.javacourse.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
}
