package io.shehzeb.taskmanager.domain;

import java.util.UUID;

/** Thrown when a task does not exist or is not owned by the caller. Mapped to HTTP 404. */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(UUID id) {
        super("Task not found: " + id);
    }
}
