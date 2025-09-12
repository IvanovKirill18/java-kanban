package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.Managers;
import ru.yandex.javacourse.TaskInterface.HistoryManager;
import ru.yandex.javacourse.TaskInterface.TaskManager;
import ru.yandex.javacourse.tasks.Task;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.TaskStatus;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {  tasks.clear(); }

    @Override
    public Task getTask(int id) {
        if(tasks.get(id) != null){
            historyManager.add(tasks.get(id));
        }
        return tasks.get(id); }

    @Override
    public void createTask(Task task) {
        task.setId(idCounter++);
        tasks.put(task.getId(), task);
        }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public Epic getEpic (int id) {
        if (epics.get(id) != null){
           historyManager.add(epics.get(id));
        }
        return epics.get(id);
    }

    @Override
    public void createEpic(Epic epic){
        epic.setId(idCounter++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic (Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic saved = epics.get(epic.getId());
            saved.setName(epic.getName());
            saved.setDescription(epic.getDescription());
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            List<Integer> toRemove = new ArrayList<>(epic.getSubtaskIds());
            for (Integer subtaskId : toRemove) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public Subtask getSubtask(int id) {
        if (subtasks.get(id) != null) {
            historyManager.add(subtasks.get(id));
        }
        return subtasks.get(id);
    }

    public void createSubtask (Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subtask.setId(idCounter++);
            subtasks.put(subtask.getId(), subtask);
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Subtask oldSubtask = subtasks.get(subtask.getId());
            int oldEpicId = oldSubtask.getEpicId();

            if (subtask.getEpicId() == subtask.getId()) {
                subtask.setEpicId(oldEpicId);
            }
            if (!epics.containsKey(subtask.getEpicId())) {
                subtask.setEpicId(oldEpicId);
            }

            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                updateEpicStatus(epic);
            }
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if(subtask != null) {
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic);
            }
            subtasks.remove(id);
            historyManager.remove(id);

        }
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        List<Subtask> result = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                result.add(subtask);
            }
        }
        return result;
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

protected void updateEpicStatus(Epic epic) {
        if(epic == null){
            return;
        }
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


