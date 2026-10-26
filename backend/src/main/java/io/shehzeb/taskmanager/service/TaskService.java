package io.shehzeb.taskmanager.service;

import io.shehzeb.taskmanager.api.TaskDtos;
import io.shehzeb.taskmanager.domain.Task;
import io.shehzeb.taskmanager.domain.TaskNotFoundException;
import io.shehzeb.taskmanager.domain.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Task use-cases, scoped to the authenticated owner. Every read and mutation is filtered by
 * {@code ownerId} so users can never see or modify another user's tasks.
 */
@Service
@Transactional
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TaskDtos.TaskResponse> list(String ownerId) {
        return repository.findByOwnerIdOrderByCreatedAtDesc(ownerId).stream()
                .map(TaskDtos.TaskResponse::from).toList();
    }

    public TaskDtos.TaskResponse create(String ownerId, TaskDtos.CreateTaskRequest req) {
        Task task = new Task(ownerId, req.title(), req.description(), req.priority(), req.dueDate());
        return TaskDtos.TaskResponse.from(repository.save(task));
    }

    @Transactional(readOnly = true)
    public TaskDtos.TaskResponse get(String ownerId, UUID id) {
        return repository.findByIdAndOwnerId(id, ownerId)
                .map(TaskDtos.TaskResponse::from)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public TaskDtos.TaskResponse update(String ownerId, UUID id, TaskDtos.UpdateTaskRequest req) {
        Task task = repository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.update(req.title(), req.description(), req.status(), req.priority(), req.dueDate());
        return TaskDtos.TaskResponse.from(repository.save(task));
    }

    public void delete(String ownerId, UUID id) {
        Task task = repository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new TaskNotFoundException(id));
        repository.delete(task);
    }
}
