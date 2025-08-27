package ru.yandex.javacourse.manager;
import ru.yandex.javacourse.TaskInterface.HistoryManager;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    private final static int MAX_HISTORY_SIZE = 10;
    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task){
        if (task == null){
            return;
        }
        Task taskCopy;
        if (task instanceof Subtask) {
            taskCopy = new Subtask((Subtask) task);
        } else if (task instanceof Epic) {
            taskCopy = new Epic((Epic) task);
        } else {
            taskCopy = new Task(task);
        }
        if (history.size() == MAX_HISTORY_SIZE){
            history.remove(0);
        }
        history.add(taskCopy);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
