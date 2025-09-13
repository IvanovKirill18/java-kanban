package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.TaskInterface.HistoryManager;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> historyMap = new HashMap<>();
    private Node head;
    private Node tail;

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }

    private Node linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        return newNode;
    }

    private List<Task> getTask() {
        List<Task> taskList = new ArrayList<>();
        Node currentNode = head;

        while (currentNode != null) {
            taskList.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return taskList;
    }

    private void removeNode(Node nodeToRemove) {
        if (nodeToRemove == null) {
            return;
        }

        final Node nextNode = nodeToRemove.next;
        final Node prevNode = nodeToRemove.prev;

        if (prevNode == null) {
            head = nextNode;
        } else {
            prevNode.next = nextNode;
            nodeToRemove.prev = null;
        }

        if (nextNode == null) {
            tail = prevNode;
        } else {
            nextNode.prev = prevNode;
            nodeToRemove.next = null;
        }
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        final int taskId = task.getId();

        if (historyMap.containsKey(taskId)) {
            removeNode(historyMap.get(taskId));
            historyMap.remove(taskId);
        }

        Node newNode = linkLast(task);
        historyMap.put(taskId, newNode);
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTask();
    }
}
