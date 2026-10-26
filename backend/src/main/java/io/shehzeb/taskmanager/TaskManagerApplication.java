package io.shehzeb.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Task Manager API.
 *
 * <p>Backend for the full-stack task manager: a JWT-secured REST API over a task domain, backed by
 * JPA. Each user sees only their own tasks (ownership enforced in the service layer).</p>
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class TaskManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args);
    }
}
