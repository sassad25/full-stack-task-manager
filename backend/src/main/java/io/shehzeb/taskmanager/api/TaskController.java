package io.shehzeb.taskmanager.api;

import io.shehzeb.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Task REST API. The owner id is taken from the authenticated JWT subject, never from the client,
 * so one user cannot act on another's tasks.
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaskDtos.TaskResponse> list(@AuthenticationPrincipal Jwt jwt) {
        return service.list(jwt.getSubject());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDtos.TaskResponse> create(@AuthenticationPrincipal Jwt jwt,
                                                        @Valid @RequestBody TaskDtos.CreateTaskRequest req,
                                                        UriComponentsBuilder uri) {
        TaskDtos.TaskResponse created = service.create(jwt.getSubject(), req);
        URI location = uri.path("/api/v1/tasks/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public TaskDtos.TaskResponse get(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        return service.get(jwt.getSubject(), id);
    }

    @PutMapping("/{id}")
    public TaskDtos.TaskResponse update(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id,
                                        @Valid @RequestBody TaskDtos.UpdateTaskRequest req) {
        return service.update(jwt.getSubject(), id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        service.delete(jwt.getSubject(), id);
    }
}
