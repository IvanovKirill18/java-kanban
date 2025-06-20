public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        int taskId1 = taskManager.createTask(new Task("Пополнить счёт на телефоне","После 20:00", 0, TaskStatus.NEW));
        int taskId2 = taskManager.createTask(new Task("Купить продукты", "Точно по списку", 0, TaskStatus.NEW));

        Epic epic1 = new Epic("Переезд", "Переехать в новый дом", 0, TaskStatus.NEW);
        int epicId1 = taskManager.createEpic(epic1);
        Epic epic2 = new Epic("Ремонт", "Сделать ремонт во всей квартире", 0, TaskStatus.NEW);
        int epicId2 = taskManager.createEpic(epic2);

        int subtaskId1 = taskManager.createSubtask(new Subtask("Собрать вещи и вывезти вещи", "Все вещи, которые нужны для переезда", 0, TaskStatus.NEW, epicId1 ));
        int subtaskId2 = taskManager.createSubtask(new Subtask("Подготовить документы", "Подписать, забрать и отправить документы", 0, TaskStatus.IN_PROGRESS, epicId1));
        int subtaskId3 = taskManager.createSubtask(new Subtask("Выбрать подрядчика", "Познакомиться с бригадиром и описать задачи", 0, TaskStatus.DONE, epicId2));
        int subtaskId4 = taskManager.createSubtask(new Subtask("Закупить материалы", "Всё что в смете", 0, TaskStatus.IN_PROGRESS, epicId2));

        Task task1 = taskManager.getTaskById(taskId1);
        task1.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);

        Subtask subtask1 = taskManager.getSubtaskById((subtaskId1));
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);

        System.out.println("Список задач: " + taskManager.getAllTasks());
        System.out.println("Список эпиков: " + taskManager.getAllEpics());
        System.out.println("Список подзадач: " + taskManager.getAllSubtasks());

        taskManager.deleteTaskById(taskId1);
        taskManager.deleteEpicById(epicId2);

        System.out.println("После удаления:");
        System.out.println("Список задач: " + taskManager.getAllTasks());
        System.out.println("Список эпиков: " + taskManager.getAllEpics());
        System.out.println("Список подзадач: " + taskManager.getAllSubtasks());
    }
}
