package fr.sncf.d2d.colibri.controllers;

import fr.sncf.d2d.colibri.domain.users.Role;
import fr.sncf.d2d.colibri.domain.users.AppUser;
import fr.sncf.d2d.colibri.test.extensions.WithMockUserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AppUserControllerTests {

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUserRole(Role.ADMIN)
    void test_get_all_users_works() throws Exception {
        mvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUserRole(Role.ADMIN)
    void test_get_all_users_not_empty() throws Exception {
        AppUser user = randomUser();
        String body = """
                {
                    "username": "%s",
                    "password": "%s",
                    "role": "USER"
                }
                """.formatted(user.getUsername(), user.getPassword());
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isCreated());
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_get_all_users_forbidden() throws Exception {
        mvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void test_get_all_users_unauthorized() throws Exception {
        mvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUserRole(Role.ADMIN)
    void test_create_user() throws Exception {
        AppUser user = randomUser();
        String body = """
                {
                    "username": "%s",
                    "password": "%s",
                    "role": "USER"
                }
                """.formatted(user.getUsername(), user.getPassword());
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isCreated());
        mvc.perform(get("/users/%s".formatted(user.getUsername())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUserRole(Role.ADMIN)
    void test_create_user_conflict() throws Exception {
        AppUser user = randomUser();
        String body = """
                {
                    "username": "%s",
                    "password": "%s",
                    "role": "USER"
                }
                """.formatted(user.getUsername(), user.getPassword());
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isCreated());
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_create_user_forbidden() throws Exception {
        AppUser user = randomUser();
        String body = """
                {
                    "username": "%s",
                    "password": "%s",
                    "role": "USER"
                }
                """.formatted(user.getUsername(), user.getPassword());
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUserRole(Role.ADMIN)
    void test_get_user() throws Exception {
        AppUser user = randomUser();
        String body = """
                {
                    "username": "%s",
                    "password": "%s",
                    "role": "USER"
                }
                """.formatted(user.getUsername(), user.getPassword());
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isCreated());
        mvc.perform(get("/users/%s".formatted(user.getUsername())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.role", is("USER")))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUserRole(Role.ADMIN)
    void test_get_user_not_found() throws Exception {
        AppUser user = randomUser();
        mvc.perform(get("/users/%s".formatted(user.getUsername())))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_get_user_unauthorized() throws Exception {
        AppUser user = randomUser();
        mvc.perform(get("/users/%s".formatted(user.getUsername())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_get_user_forbidden() throws Exception {
        AppUser user = randomUser();
        mvc.perform(get("/users/%s".formatted(user.getUsername())))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUserRole(Role.ADMIN)
    void test_update_user() throws Exception {
        AppUser user = randomUser();
        String body = """
                {
                    "username": "%s",
                    "password": "%s",
                    "role": "USER"
                }
                """.formatted(user.getUsername(), user.getPassword());
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isCreated());
        body = """
                {
                    "role": "POSTMAN"
                }
                """;
        mvc.perform(patch("/users/%s".formatted(user.getUsername()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.role", is("POSTMAN")))
                .andExpect(jsonPath("$.password").doesNotExist());
        mvc.perform(get("/users/%s".formatted(user.getUsername())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.role", is("POSTMAN")))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUserRole(Role.ADMIN)
    void test_update_user_not_found() throws Exception {
        String body = """
                {
                    "role": "POSTMAN"
                }
                """;
        mvc.perform(patch("/users/not_an_id")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_update_user_unauthorized() throws Exception {
        mvc.perform(patch("/users/whatever"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_update_user_forbidden() throws Exception {
        mvc.perform(patch("/users/whatever")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    private AppUser randomUser() {
        return new AppUser(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                Role.USER
        );
    }
}
