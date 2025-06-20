import java.util.HashMap;

public class TaskManager {
    private int idCounter = 1;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    private int generateId() {
        return idCounter++;
    }

    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public int createTask(Task task) {
        int id = generateId();
        task = new Task(task.getName(), task.getDescription(), id, task.getStatus());
        tasks.put(id, task);
        return id;
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public HashMap<Integer, Subtask> getAllSubtasks() {
        return subtasks;
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public int createSubtask(Subtask subtask) {
        int id = generateId();
        subtask = new Subtask(subtask.getName(), subtask.getDescription(), id, subtask.getStatus(), subtask.getEpicId());
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if(epic != null) {
            epic.addSubtaskId(id);
            updateEpicStatus(epic);
        }
        return id;
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if(epic != null) {
                updateEpicStatus(epic);
            }
        }
    }

    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtasks != null) {
            Epic epic =epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtaskIds().remove(id);
                updateEpicStatus(epic);
            }
            subtasks.remove(id);
        }
    }

    public HashMap<Integer, Epic> getAllEpics() {
        return epics;
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public int createEpic(Epic epic) {
        int id = generateId();
        epic = new Epic(epic.getName(), epic.getDescription(), id, epic.getStatus());
        epics.put(id, epic);
        return id;
    }

    public void updateEpic(Epic epic){
        if(epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
        }
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            HashMap<Integer, Integer> subtaskIds = epic.getSubtaskIds();
            for (Integer subtaskId : subtaskIds.keySet()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    public HashMap<Integer, Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        HashMap<Integer, Subtask> epicSubtasks = new HashMap<>();
        if (epic != null) {
            HashMap<Integer, Integer> subtaskIds = epic.getSubtaskIds();
            for (Integer subtaskId : subtaskIds.values()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    epicSubtasks.put(subtaskId, subtask);
                }
            }
        }
        return epicSubtasks;
    }

    private void updateEpicStatus(Epic epic) {
        HashMap<Integer, Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Integer subtaskId : subtaskIds.values()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask == null) {
                continue;
            }
            if (subtask.getStatus() != TaskStatus.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != TaskStatus.DONE) {
                allDone = false;
            }
        }

        if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
