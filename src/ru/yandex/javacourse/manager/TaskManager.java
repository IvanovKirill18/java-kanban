package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.tasks.Task;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.TaskStatus;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private int idCounter = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTasks() {  tasks.clear(); }

    public Task getTask(int id) {  return tasks.get(id); }

    public void createTask(Task task) {
        task.setId(idCounter++);
        tasks.put(task.getId(), task);
        }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public Epic getEpic (int id) {
        return epics.get(id);
    }

    public void createEpic(Epic epic){
        epic.setId(idCounter++);
        epics.put(epic.getId(), epic);
    }

    public void updateEpic (Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic saved = epics.get(epic.getId());
            saved.setName(epic.getName());
            saved.setDescription(epic.getDescription());
        }
    }

    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            List<Integer> toRemove = new ArrayList<>();
            for (Subtask subtask : subtasks.values()){
                if (subtask.getEpicId() == id) {
                    toRemove.add(subtask.getId());
                }
            }
            for (Integer subtaskId : toRemove) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public Subtask getSubtask(int id) { return subtasks.get(id); }

    public void createSubtask (Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subtask.setId(idCounter++);
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            for (Epic epic : epics.values()) {
                updateEpicStatus(epic);
            }
        }
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtasks != null) {
            for (Epic epic : epics.values()) {
                updateEpicStatus(epic);
            }
        }
    }

public List<Subtask> getEpicSubtasks(int epicId) {
    List<Subtask> result = new ArrayList<>();
    for (Subtask subtask : subtasks.values()) {
        if (subtask.getEpicId() == epicId) {
            result.add(subtask);
        }
    }
    return result;
}

private void updateEpicStatus(Epic epic) {
    List<Subtask> subtaskList = getEpicSubtasks(epic.getId());

    if (subtaskList.isEmpty()) {
        epic.setStatus(TaskStatus.NEW);
        return;
    }

    boolean allNew = true;
    boolean allDone = true;

    for (Subtask subtask : subtaskList) {
        if(subtask.getStatus() != TaskStatus.NEW) {
            allNew = false;
        }
        if (subtask.getStatus() != TaskStatus.DONE) {
            allDone = false;
        }
    }

    if(allDone) {
        epic.setStatus(TaskStatus.DONE);
    } else if (allNew) {
        epic.setStatus(TaskStatus.NEW);
    } else {
        epic.setStatus(TaskStatus.IN_PROGRESS);
    }
}
}


