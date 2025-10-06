package ru.yandex.javacourse;


import ru.yandex.javacourse.TaskInterface.TaskManager;
import ru.yandex.javacourse.manager.InMemoryHistoryManager;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.TaskInterface.HistoryManager;
import ru.yandex.javacourse.manager.FileBackedTaskManager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager(File file) {
        return new FileBackedTaskManager(file);
    }
}
