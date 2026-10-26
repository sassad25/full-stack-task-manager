package io.shehzeb.taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the task API: login issues a usable JWT, task CRUD works, unauthenticated
 * access is rejected, validation is enforced, and tasks are isolated per user.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String loginAs(String username) throws Exception {
        String body = """
                {"username":"%s","password":"pw"}
                """.formatted(username);
        String json = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(json).get("token").asText();
    }

    @Test
    void createListAndGetTask() throws Exception {
        String token = loginAs("alice");

        String create = """
                {"title":"Write tests","description":"cover the API","priority":"HIGH","dueDate":"2026-12-31"}
                """;
        mockMvc.perform(post("/api/v1/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(create))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Write tests"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        mockMvc.perform(get("/api/v1/tasks").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Write tests"));
    }

    @Test
    void tasksAreIsolatedPerUser() throws Exception {
        String aliceToken = loginAs("alice2");
        String bobToken = loginAs("bob2");

        mockMvc.perform(post("/api/v1/tasks")
                        .header("Authorization", "Bearer " + aliceToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Alice task","priority":"LOW"}
                                """))
                .andExpect(status().isCreated());

        // Bob must not see Alice's task.
        mockMvc.perform(get("/api/v1/tasks").header("Authorization", "Bearer " + bobToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void unauthenticatedAccessIsRejected() throws Exception {
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithBlankTitleIsBadRequest() throws Exception {
        String token = loginAs("carol2");
        mockMvc.perform(post("/api/v1/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"","priority":"LOW"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation error"));
    }
}
