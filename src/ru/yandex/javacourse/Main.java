package ru.yandex.javacourse;

import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.tasks.Task;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.TaskStatus;


public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task("Пополнить счёт на телефоне", "После 20:00", TaskStatus.NEW);
        Task task2 = new Task("Купить продукты", "Точно по списку", TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Переезд", "Переехать в новый дом");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Собрать вещи и вывезти вещи", "Все вещи, которые нужны для переезда", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подготовить документы", "Подписать, забрать и отправить документы", TaskStatus.IN_PROGRESS, epic1.getId());
        Subtask subtask3 = new Subtask("Найти машину для перевозки", "Договориться о времени", TaskStatus.NEW, epic1.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        Epic epic2 = new Epic("Ремонт", "Сделать ремонт во всей квартире");
        taskManager.createEpic(epic2);

        System.out.println("Состояние трекера задач: ");
        System.out.println("Задачи: " + taskManager.getAllTasks());
        System.out.println("Эпики: " + taskManager.getAllEpics());
        System.out.println("Подзадачи: " + taskManager.getAllSubtasks());
        System.out.println("История: " + taskManager.getHistory());
        System.out.println();

        taskManager.getTask(task1.getId());
        taskManager.getEpic((epic1.getId()));
        taskManager.getSubtask(subtask1.getId());
        System.out.println("История после первого блока запросов: " + taskManager.getHistory());
        System.out.println();

        taskManager.getSubtask(subtask2.getId());
        taskManager.getTask((task2.getId()));
        taskManager.getEpic(epic1.getId());
        System.out.println("История после второго блока запросов: " + taskManager.getHistory());
        System.out.println();

        taskManager.getEpic(epic2.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getTask(task1.getId());
        System.out.println("История после третьего блока запросов: " + taskManager.getHistory());
        System.out.println();

        System.out.println("Удалаем задачу с id: " + task1.getId());
        taskManager.deleteTask(task1.getId());
        System.out.println("История после удаления задачи: " + taskManager.getHistory());
        System.out.println();

        System.out.println("Удаляем эпик с 3 подзадачами, его id: " + epic1.getId());
        taskManager.deleteEpic(epic1.getId());
        System.out.println("История после удаления эпика: " + taskManager.getHistory());
        System.out.println("Подзадачи после удаления эпика: " + taskManager.getAllSubtasks());
        System.out.println();

        System.out.println("Состояние трекера задач: ");
        System.out.println("Задачи: " + taskManager.getAllTasks());
        System.out.println("Эпики: " + taskManager.getAllEpics());
        System.out.println("Подзадачи: " + taskManager.getAllSubtasks());
        System.out.println("История: " + taskManager.getHistory());
    }
}
