package com.example.todolist.Model;

public class Task {
    private int taskId;
    private boolean isComplete;
    private String taskName, taskDescription, taskDate;

    public Task(int taskId, boolean isComplete, String taskName, String taskDescription, String taskDate) {
        this.taskId = taskId;
        this.isComplete = isComplete;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskDate = taskDate;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }
}
