package ru.yandex.javacourse.TaskInterface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.Managers;
import ru.yandex.javacourse.manager.InMemoryHistoryManager;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Task;
import ru.yandex.javacourse.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    /* Проверяем, что экземпляры класса Task равны друг другу, если равен их id*/
    @Test
    void taskEqualsById() {
        Task task1 = new Task("Пополнить счёт на телефоне", "После 20:00", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("Купить продукты", "Точно по списку", TaskStatus.NEW);
        task2.setId(1);
        assertEquals(task1, task2, "Задачи равны друг другу, если равен их id");
    }

    /* Проверяем, что наследники класса Task равны друг другу, если равен их id" */
    @Test
    void subtaskEqualsById() {
        Epic epic = new Epic("Переезд", "Переехать в новый дом");
        epic.setId(1);
        Subtask subtask1 = new Subtask("Собрать вещи и вывезти вещи", "Все вещи, которые нужны для переезда", TaskStatus.NEW, epic.getId());
        subtask1.setId(2);
        Subtask subtask2 = new Subtask("Подготовить документы", "Подписать, забрать и отправить документы", TaskStatus.IN_PROGRESS, epic.getId());
        subtask2.setId((2));
        assertEquals(subtask1, subtask2, "Подзадачи равны друг другу, если равен их id");
    }

    @Test
    void epicEqualsById() {
        Epic epic1 = new Epic("Переезд", "Переехать в новый дом");
        epic1.setId(1);
        Epic epic2 = new Epic("Ремонт", "Сделать ремонт во всей квартире");
        epic2.setId(1);
        assertEquals(epic1, epic2, "Эпики равны друг другу, если равен их id");
    }

    /* проверяем, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров */
    @Test
    void managerReturnInitialized() {
        assertNotNull(Managers.getDefault(), "Менеджер задач должен быть проинициализирован");
        assertNotNull(Managers.getDefaultHistory(), "Менеджер истории должен быть проинициализирован");
    }

    /* проверяем, что объект Epic нельзя добавить в самого себя в виде подзадачи */
    @Test
    void epicCannotBeItsOwnSubtask() {
        Epic epic = new Epic("Переезд", "Переехать в новый дом");
        taskManager.createTask(epic);
        Subtask subtask = new Subtask("Переезд", "Переехать в новый дом", TaskStatus.NEW, epic.getId());
        subtask.setId(epic.getId());
        taskManager.createSubtask(subtask);

        assertNull(taskManager.getSubtask(epic.getId()), "Подзадача с id эпика не должна быть добавлена");
    }

    /* проверяем, что объект Subtask нельзя сделать своим же эпиком */
    @Test
    void subtaskCannotBeItsOwnEpic() {
        Epic epic = new Epic("Переезд", "Переехать в новый дом");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Подзадача", "Описание", TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask);
        Subtask savedSubtask = taskManager.getSubtask(subtask.getId());
        int originalEpicId = savedSubtask.getEpicId();
        savedSubtask.setEpicId(savedSubtask.getId());
        taskManager.updateSubtask(savedSubtask);

        Subtask updatedSubtask = taskManager.getSubtask(subtask.getId());
        assertNotEquals(originalEpicId, updatedSubtask.getEpicId(), "EpicId подзадачи должен оставаться равным изначальному, а не быть равен её собственному id");

    }



    /* проверяем, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id */
    @Test
    void taskManagerAddAnyTaskTypesFindId() {
        Task task = new Task("Задача", "Описание", TaskStatus.NEW);
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Подздача", "Описание", TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask);

        assertNotNull(taskManager.getTask(task.getId()));
        assertNotNull(taskManager.getEpic(epic.getId()));
        assertNotNull(taskManager.getSubtask(subtask.getId()));
    }


    /* проверяем, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера */
    @Test
    void setIdAndAutoGenerateIdDoNotConflict() {
        Task task1 = new Task("Задача 1", "Описание", TaskStatus.NEW);
        task1.setId(100);
        Task task2 = new Task("Задача 2", "Описание", TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertNotNull(taskManager.getTask(task1.getId()), "Задача с заданным id должна существовать");
        assertNotNull(taskManager.getTask(task2.getId()), "Задача с сгенерированным id должна существовать");
        assertNotEquals(task1, task2, "id задач не должны совпадать");
    }


    /* тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер */
    @Test
    void saveFieldsWhenAddInTaskManager() {
        Task task = new Task("Задача 1", "Описание", TaskStatus.NEW);
        taskManager.createTask(task);
        Task sameTask = taskManager.getTask(task.getId());

        assertEquals(sameTask.getName(), task.getName(), "Название не должно измениться");
        assertEquals(sameTask.getDescription(), task.getDescription(), "Описание не должно измениться");
        assertEquals(sameTask.getStatus(), task.getStatus(), "Статус не должен измениться");
    }


    /* проверяем, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных */
    @Test
    void historyManagerSavedPreviousTask() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        taskManager.createTask(task);
        historyManager.add(task);
        task.setName("Изменённое название");
        task.setDescription("Изменённое описание");
        task.setStatus(TaskStatus.IN_PROGRESS);
        Task previousTask = historyManager.getHistory().get(0);
        assertEquals("Задача 1", previousTask.getName(), "История должна сохранить изначальное название задачи - Задача 1");
        assertEquals("Описание 1", previousTask.getDescription(), "История должна сохранить изначальное описанние - Описание 1");
        assertEquals(TaskStatus.NEW, previousTask.getStatus(), "История должна сохранить изначальный статус - TaskStatus.NEW");
    }

}