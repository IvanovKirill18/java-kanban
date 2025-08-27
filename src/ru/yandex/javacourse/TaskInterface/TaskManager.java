package ru.yandex.javacourse.TaskInterface;

import ru.yandex.javacourse.tasks.Task;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Epic;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    void deleteAllTasks();

    Task getTask(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void deleteTask(int id);

    List<Epic> getAllEpics();

    void deleteAllEpics();

    Epic getEpic(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpic(int id);

    List<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtask(int id);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(int id);

    List<Subtask> getEpicSubtasks(int epicId);

    List<Task> getHistory();
}
