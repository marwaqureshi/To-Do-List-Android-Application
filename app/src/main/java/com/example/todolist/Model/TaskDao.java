package com.example.todolist.Model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 Data Access Object used to access the local database
 @author Jay Stewart
 */
@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks")
    List<Task> getAll();

    @Query("SELECT * FROM tasks WHERE isComplete = 1")
    List<Task> getComplete();

    @Query("SELECT * FROM tasks WHERE isComplete = 0")
    List<Task> getIncomplete();

    @Query("UPDATE tasks SET isComplete = 1 WHERE taskId = :taskId")
    void setComplete(int taskId);

    @Query("UPDATE tasks SET isComplete = 0 WHERE taskId = :taskId")
    void setIncomplete(int taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);

    @Delete
    void delete(Task task);
}
