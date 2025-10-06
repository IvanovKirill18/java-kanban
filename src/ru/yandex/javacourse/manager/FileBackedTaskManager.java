package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.tasks.Task;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.TaskStatus;

import java.io.*;
import java.nio.file.Files;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.load();
        return manager;
    }

    private void load() {
        try {
            String content = Files.readString(file.toPath());
            String[] lines = content.split(System.lineSeparator());

            if (lines.length <= 1) {
                return;
            }

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (!line.isEmpty()) {
                    Task task = fromString(line);
                    if (task != null) {
                        addTaskToManager(task);
                    }
                }
            }

            for (Epic epic : epics.values()) {
                updateEpicStatus(epic);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки из файла", e);
        }
    }

    private void addTaskToManager(Task task) {
        if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
            if (task.getId() >= idCounter) {
                idCounter = task.getId() + 1;
            }
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.addSubtaskId(subtask.getId());
            }
            if (task.getId() >= idCounter) {
                idCounter = task.getId() + 1;
            }
        } else {
            tasks.put(task.getId(), task);
            if (task.getId() >= idCounter) {
                idCounter = task.getId() + 1;
            }
        }
    }

    protected void save() {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(("id,type,name,status,description,epic"));

            for (Task task : getAllTasks()) {
                writer.println(toString(task));
            }
            for (Epic epic : getAllEpics()) {
                writer.println(toString(epic));
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.println(toString(subtask));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл", e);
        }
    }

    private String toString(Task task) {
        if (task instanceof Epic) {
            return String.format("%d,EPIC,%s,%s,%s,", task.getId(), task.getName(), task.getStatus(), task.getDescription());
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return String.format("%d,SUBTASK,%s,%s,%s,%d", subtask.getId(), subtask.getName(), subtask.getStatus(), subtask.getDescription(), subtask.getEpicId());
        } else {
            return String.format("%d,TASK,%s,%s,%s,", task.getId(), task.getName(), task.getStatus(), task.getDescription());
        }
    }

    private Task fromString(String value) {
        String[] fields = value.split(",");
        if (fields.length < 5) return null;
        try {
            int id = Integer.parseInt(fields[0].trim());
            String type = fields[1].trim();
            String name = fields[2].trim();
            TaskStatus status = TaskStatus.valueOf(fields[3].trim());
            String description = fields[4].trim();

            switch (type) {
                case "TASK":
                    Task task = new Task(name, description, status);
                    task.setId(id);
                    return task;
                case "EPIC":
                    Epic epic = new Epic(name, description);
                    epic.setId(id);
                    epic.setStatus(status);
                    return epic;
                case "SUBTASK":
                    if (fields.length >= 6) {
                        int epicId = Integer.parseInt(fields[5].trim());
                        Subtask subtask = new Subtask(name, description, status, epicId);
                        subtask.setId(id);
                        return subtask;
                    }
                    break;

            }
        } catch (Exception e) {
            System.out.println("Ошибка парсинга строки: " + value);
        }
        return null;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }


    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    public static void main(String[] args) {
        try {
            File tempFile = File.createTempFile("tasks", ".csv");
            tempFile.deleteOnExit();

            FileBackedTaskManager manager1 = new FileBackedTaskManager(tempFile);

            Task task1 = new Task("Пополнить счёт на телефоне", "После 20:00", TaskStatus.NEW);
            Task task2 = new Task("Купить продукты", "Точно по списку", TaskStatus.NEW);

            manager1.createTask(task1);
            manager1.createTask(task2);

            Epic epic1 = new Epic("Переезд", "Переехать в новый дом");
            manager1.createEpic(epic1);
            Subtask subtask1 = new Subtask("Собрать вещи", "Все вещи для переезда", TaskStatus.NEW, epic1.getId());
            Subtask subtask2 = new Subtask("Подготовить документы", "Подписать документы", TaskStatus.IN_PROGRESS, epic1.getId());
            manager1.createSubtask(subtask1);
            manager1.createSubtask(subtask2);

            FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(tempFile);

            System.out.println("Задачи после восстановления: ");
            System.out.println("Tasks: " + manager2.getAllTasks());
            System.out.println("Epics: " + manager2.getAllEpics());
            System.out.println("Subtasks: " + manager2.getAllSubtasks());

        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом: " + e.getMessage());
        }
    }
}

