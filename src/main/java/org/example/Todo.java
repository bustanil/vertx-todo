package org.example;

import java.time.LocalDateTime;

public class Todo {

    private String id;
    private String task;
    private boolean completed;

    private LocalDateTime createdAt;
    private Todo(){}

    public Todo(String id, String task, Boolean completed, LocalDateTime createdAt){
        this.id = id;
        this.task = task;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
