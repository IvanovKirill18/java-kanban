package ru.yandex.javacourse.TaskInterface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.Managers;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Task;
import ru.yandex.javacourse.tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private static final String TASK_NAME_1 = "Задача 1";
    private static final String TASK_NAME_2 = "Задача 2";
    private static final String TASK_NAME_3 = "Задача 3";
    private static final String TASK_DESCRIPTOIN_1 = "Описание задачи 1";
    private static final String TASK_DESCRIPTOIN_2 = "Описание задачи 2";
    private static final String TASK_DESCRIPTOIN_3 = "Описание задачи 3";
    private static final String EPIC_NAME_1 = "Эпик 1";
    private static final String EPIC_NAME_2 = "Эпик 2";
    private static final String EPIC_DESCRIPTOIN_1 = "Описание эпика 1";
    private static final String EPIC_DESCRIPTOIN_2 = "Описание эпика 2";
    private static final String SUBTASK_NAME_1 = "Подзадача 1";
    private static final String SUBTASK_NAME_2 = "Подзадача 2";
    private static final String SUBTASK_DESCRIPTOIN_1 = "Описание подзадачи 1";
    private static final String SUBTASK_DESCRIPTOIN_2 = "Описание подзадачи 2";
    private static final int CONSTANT_ID = 333;

    private TaskManager taskManager;


    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }


    @Test
    @DisplayName("Проверяем, что экземпляры класса Task равны друг другу, если равен их id")
    void taskEqualsByIdTest() {
        //Given
        Task task1 = new Task(TASK_NAME_1, TASK_DESCRIPTOIN_1, TaskStatus.NEW);
        Task task2 = new Task(TASK_NAME_2, TASK_DESCRIPTOIN_2, TaskStatus.NEW);

        //When
        task1.setId(CONSTANT_ID);
        task2.setId(CONSTANT_ID);

        //Then
        assertEquals(task1, task2, "Задачи равны друг другу, если равен их id");
    }

    @Test
    @DisplayName("Проверяем, что наследники класса Task - Subtask, равны друг другу, если равен их id")
    void subtaskEqualsByIdTest() {
        //Given
        Epic epic = new Epic(EPIC_NAME_1, EPIC_DESCRIPTOIN_1);
        Subtask subtask1 = new Subtask(SUBTASK_NAME_1, SUBTASK_DESCRIPTOIN_1, TaskStatus.NEW, epic.getId());
        Subtask subtask2 = new Subtask(SUBTASK_NAME_2, SUBTASK_DESCRIPTOIN_2, TaskStatus.IN_PROGRESS, epic.getId());

        //When
        epic.setId(1);
        subtask1.setId(CONSTANT_ID);
        subtask2.setId((CONSTANT_ID));

        //Then
        assertEquals(subtask1, subtask2, "Подзадачи равны друг другу, если равен их id");
    }

    @Test
    @DisplayName("Проверяем, что наследники класса Task - Epic, равны друг другу, если равен их id")
    void epicEqualsByIdTest() {
        //Given
        Epic epic1 = new Epic(EPIC_NAME_1, EPIC_DESCRIPTOIN_1);
        Epic epic2 = new Epic(EPIC_NAME_2, EPIC_DESCRIPTOIN_2);

        //When
        epic1.setId(CONSTANT_ID);
        epic2.setId(CONSTANT_ID);

        //Then
        assertEquals(epic1, epic2, "Эпики равны друг другу, если равен их id");
    }

    @Test
    @DisplayName("Проверяем, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров")
    void managerReturnInitializedTest() {
        assertNotNull(Managers.getDefault(), "Менеджер задач должен быть проинициализирован");
        assertNotNull(Managers.getDefaultHistory(), "Менеджер истории должен быть проинициализирован");
    }

    @Test
    @DisplayName("Проверяем, что объект Epic нельзя добавить в самого себя в виде подзадачи")
    void epicCannotBeItsOwnSubtaskTest() {
        //Given
        Epic epic = new Epic(EPIC_NAME_1, EPIC_DESCRIPTOIN_1);
        taskManager.createTask(epic);
        Subtask subtask = new Subtask(SUBTASK_NAME_1, SUBTASK_DESCRIPTOIN_2, TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask);

        //When
        subtask.setId(epic.getId());

        //Then
        assertNull(taskManager.getSubtask(epic.getId()), "Подзадача с id эпика не должна быть добавлена");
    }


    @Test
    @DisplayName("Проверяем, что объект Subtask нельзя сделать своим же эпиком")
    void subtaskCannotBeItsOwnEpicTest() {
        //Given
        Epic epic = new Epic(EPIC_NAME_1, EPIC_DESCRIPTOIN_1);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask(SUBTASK_NAME_1, SUBTASK_DESCRIPTOIN_1, TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask);
        Subtask savedSubtask = taskManager.getSubtask(subtask.getId());
        int originalEpicId = savedSubtask.getEpicId();

        //When
        savedSubtask.setEpicId(savedSubtask.getId());
        taskManager.updateSubtask(savedSubtask);
        Subtask updatedSubtask = taskManager.getSubtask(subtask.getId());

        //Then
        assertNotEquals(originalEpicId, updatedSubtask.getEpicId(), "EpicId подзадачи должен оставаться равным изначальному, а не быть равен её собственному id");
    }

    @Test
    @DisplayName("Проверяем, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id")
    void taskManagerAddAnyTaskTypesFindIdTest() {
        //Given
        Task task = new Task(TASK_NAME_1, TASK_DESCRIPTOIN_1, TaskStatus.NEW);
        taskManager.createTask(task);
        Epic epic = new Epic(EPIC_NAME_1, EPIC_DESCRIPTOIN_1);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask(SUBTASK_NAME_1, SUBTASK_DESCRIPTOIN_2, TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask);

        //Then
        assertNotNull(taskManager.getTask(task.getId()));
        assertNotNull(taskManager.getEpic(epic.getId()));
        assertNotNull(taskManager.getSubtask(subtask.getId()));
    }

    @Test
    @DisplayName("Проверяем, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера")
    void setIdAndAutoGenerateId_DoNotConflict_Test() {
        //Given
        Task task1 = new Task(TASK_NAME_1, TASK_DESCRIPTOIN_1, TaskStatus.NEW);
        task1.setId(CONSTANT_ID);
        Task task2 = new Task(TASK_NAME_2, TASK_DESCRIPTOIN_2, TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        //When


        //Then
        assertNotNull(taskManager.getTask(task1.getId()), "Задача с заданным id должна существовать");
        assertNotNull(taskManager.getTask(task2.getId()), "Задача с сгенерированным id должна существовать");
        assertNotEquals(task1, task2, "id задач не должны совпадать");
    }

    @Test
    @DisplayName("Проверяем неизменность задачи (по всем полям) при добавлении задачи в менеджер")
    void saveFields_WhenAddInTaskManager_Test() {
        //Given
        Task task = new Task(TASK_NAME_1, TASK_DESCRIPTOIN_1, TaskStatus.NEW);
        taskManager.createTask(task);

        //When
        Task sameTask = taskManager.getTask(task.getId());

        //Then
        assertEquals(sameTask.getName(), task.getName(), "Название не должно измениться");
        assertEquals(sameTask.getDescription(), task.getDescription(), "Описание не должно измениться");
        assertEquals(sameTask.getStatus(), task.getStatus(), "Статус не должен измениться");
    }

    @Test
    @DisplayName("Проверяем, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных")
    void historyManager_SavedPreviousTask_Test() {
        //Given
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = new InMemoryTaskManager();

        Task task = new Task(TASK_NAME_1, TASK_DESCRIPTOIN_1, TaskStatus.NEW);
        taskManager.createTask(task);
        historyManager.add(task);

        //When
        task.setName("Изменённое название");
        task.setDescription("Изменённое описание");
        task.setStatus(TaskStatus.IN_PROGRESS);
        Task previousTask = historyManager.getHistory().get(0);

        //Then
        assertEquals(TASK_NAME_1, previousTask.getName(), "История должна сохранить изначальное название задачи - Задача 1");
        assertEquals(TASK_DESCRIPTOIN_1, previousTask.getDescription(), "История должна сохранить изначальное описанние - Описание 1");
        assertEquals(TaskStatus.NEW, previousTask.getStatus(), "История должна сохранить изначальный статус - TaskStatus.NEW");
    }

    @Test
    @DisplayName("Проверяем, что история не содержит дубликатов при повторном просмотре задачи")
    void historyManager_NuDuplicates_OnDoubleAdd_Test() {
        //Given
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task(TASK_NAME_1, TASK_DESCRIPTOIN_1, TaskStatus.NEW);
        task.setId(CONSTANT_ID);

        //When
        historyManager.add(task);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();

        //Then
        assertEquals(1, history.size(), "История не должна содержать дубликатов");
        assertEquals(task, history.get(0), "В истории должна остататься последня добавленная версия");
    }

    @Test
    @DisplayName("Проверяем, что порядок задач в истоии соответствует порядку добавления (последний добавленный - последний в списке")
    void historyManager_KeepOrder_Test() {
        //Given
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task(TASK_NAME_1, TASK_DESCRIPTOIN_1, TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task(TASK_NAME_2, TASK_DESCRIPTOIN_2, TaskStatus.NEW);
        task2.setId(2);

        //When
        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> history = historyManager.getHistory();

        //Then
        assertEquals(task1, history.get(0), "Первой в истории должна быть первая добавленная задача");
        assertEquals(task2, history.get(1), "Второй в истории должна быть вторая дабавленная задача");
    }

    @Test
    @DisplayName("Проверяем удаление задачи из середины истории")
    void historyManager_RemoveFromMidle_Test() {
        //Given
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task(TASK_NAME_1, TASK_DESCRIPTOIN_1, TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task(TASK_NAME_2, TASK_DESCRIPTOIN_2, TaskStatus.NEW);
        task2.setId(2);
        Task task3 = new Task(TASK_NAME_3, TASK_DESCRIPTOIN_3, TaskStatus.NEW);
        task3.setId(3);

        //When
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(2);
        List<Task> history = historyManager.getHistory();

        //Then
        assertEquals(2, history.size(), "После удаления одной задачи в истории должно остаться 2 элемента");
        assertEquals(task1, history.get(0), "Первым элементом должен остаться task1");
        assertEquals(task3, history.get(1), "Вторым элементом должен стать task3");
    }
}