package ru.yandex.javacourse;


import ru.yandex.javacourse.TaskInterface.TaskManager;
import ru.yandex.javacourse.manager.InMemoryHistoryManager;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.TaskInterface.HistoryManager;

public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
