package io.shehzeb.taskmanager.api;

import io.shehzeb.taskmanager.domain.Task;
import io.shehzeb.taskmanager.domain.TaskPriority;
import io.shehzeb.taskmanager.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/** Task API payloads. */
public final class TaskDtos {

    private TaskDtos() {
    }

    public record CreateTaskRequest(
            @NotBlank @Size(max = 255) String title,
            @Size(max = 2000) String description,
            @NotNull TaskPriority priority,
            LocalDate dueDate) {
    }

    public record UpdateTaskRequest(
            @NotBlank @Size(max = 255) String title,
            @Size(max = 2000) String description,
            @NotNull TaskStatus status,
            @NotNull TaskPriority priority,
            LocalDate dueDate) {
    }

    public record TaskResponse(
            UUID id,
            String title,
            String description,
            TaskStatus status,
            TaskPriority priority,
            LocalDate dueDate,
            long version,
            Instant createdAt) {

        public static TaskResponse from(Task t) {
            return new TaskResponse(t.getId(), t.getTitle(), t.getDescription(), t.getStatus(),
                    t.getPriority(), t.getDueDate(), t.getVersion(), t.getCreatedAt());
        }
    }
}
