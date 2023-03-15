package com.example.todolist;

import androidx.room.Room;

import androidx.test.core.app.ApplicationProvider;

import com.example.todolist.Model.AppDatabase;
import com.example.todolist.Model.Task;
import com.example.todolist.Model.TaskDao;

import org.junit.Test;

import java.util.Objects;

public class AppDatabaseTest {

    AppDatabase db = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
    AppDatabase.class, "tasks-db").allowMainThreadQueries().build();
    TaskDao taskDao = db.taskDao();

    @Test
    public void testInsert() {
        Task task1 = new Task
                (1, R.drawable.placeholder, true, "task1", "desc1", "1/1/1");
        taskDao.insert(task1);

        assert Objects.equals(taskDao.getAll().get(0).getTaskId(), task1.getTaskId());
        assert Objects.equals(taskDao.getAll().get(0).getTaskImage(), task1.getTaskImage());
        assert Objects.equals(taskDao.getAll().get(0).getIsComplete(), task1.getIsComplete());
        assert Objects.equals(taskDao.getAll().get(0).getTaskName(), task1.getTaskName());
        assert Objects.equals(taskDao.getAll().get(0).getTaskDescription(), task1.getTaskDescription());
        assert Objects.equals(taskDao.getAll().get(0).getTaskDate(), task1.getTaskDate());
    }

    @Test
    public void testInsertDuplicateId() {
        Task task1 = new Task
                (1, R.drawable.placeholder, true, "task1", "desc1", "1/1/1");
        Task task2 = new Task
                (1, R.drawable.placeholder, false, "task2", "desc2", "2/2/2");
        taskDao.insert(task1);
        taskDao.insert(task2);

        assert Objects.equals(taskDao.getAll().get(0).getTaskId(), task2.getTaskId());
        assert Objects.equals(taskDao.getAll().get(0).getTaskImage(), task2.getTaskImage());
        assert Objects.equals(taskDao.getAll().get(0).getIsComplete(), task2.getIsComplete());
        assert Objects.equals(taskDao.getAll().get(0).getTaskName(), task2.getTaskName());
        assert Objects.equals(taskDao.getAll().get(0).getTaskDescription(), task2.getTaskDescription());
        assert Objects.equals(taskDao.getAll().get(0).getTaskDate(), task2.getTaskDate());
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testInsertNull() {
        taskDao.insert(null);
    }

    @Test
    public void testDelete() {
        Task task1 = new Task
                (1, R.drawable.placeholder, true, "task1", "desc1", "1/1/1");
        taskDao.insert(task1);
        taskDao.delete(task1);
        assert taskDao.getAll().isEmpty();
    }

}