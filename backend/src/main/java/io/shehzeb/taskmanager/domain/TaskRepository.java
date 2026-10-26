package io.shehzeb.taskmanager.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Tasks are always queried within the owner's scope to enforce per-user isolation. */
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByOwnerIdOrderByCreatedAtDesc(String ownerId);

    Optional<Task> findByIdAndOwnerId(UUID id, String ownerId);
}
