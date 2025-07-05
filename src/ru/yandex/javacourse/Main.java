package ru.yandex.javacourse;

import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.Task;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.TaskStatus;


public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Пополнить счёт на телефоне", "После 20:00", TaskStatus.NEW);
        Task task2 = new Task("Купить продукты", "Точно по списку", TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Переезд", "Переехать в новый дом");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Собрать вещи и вывезти вещи", "Все вещи, которые нужны для переезда", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подготовить документы", "Подписать, забрать и отправить документы", TaskStatus.IN_PROGRESS, epic1.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Ремонт", "Сделать ремонт во всей квартире");
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Выбрать подрядчика", "Познакомиться с бригадиром и описать задачи", TaskStatus.DONE, epic2.getId());
        Subtask subtask4 = new Subtask("Закупить материалы", "Всё что в смете", TaskStatus.IN_PROGRESS, epic2.getId());
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);

        System.out.println("Задачи: " + taskManager.getAllTasks());
        System.out.println("Эпики: " + taskManager.getAllEpics());
        System.out.println("Подзадачи: " + taskManager.getAllSubtasks());

        task1.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask2);

        System.out.println("Обновленные задачи: " + taskManager.getAllTasks());
        System.out.println("Обновленные эпики: " + taskManager.getAllEpics());
        System.out.println("Обновленные подзадачи: " + taskManager.getAllSubtasks());

        taskManager.deleteTask(task1.getId());
        taskManager.deleteEpic(epic2.getId());

        System.out.println("После удаления:");
        System.out.println("Список задач: " + taskManager.getAllTasks());
        System.out.println("Список эпиков: " + taskManager.getAllEpics());
        System.out.println("Список подзадач: " + taskManager.getAllSubtasks());

    }
}
