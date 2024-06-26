package fr.sncf.d2d.colibri.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sncf.d2d.colibri.domain.users.Role;
import fr.sncf.d2d.colibri.test.extensions.WithMockUserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static fr.sncf.d2d.colibri.test.extensions.RequestPostProcessors.userRole;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ParcelControllerTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUserRole(Role.USER)
    void test_get_all_parcels_works() throws Exception {
        mvc.perform(get("/parcels"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUserRole(Role.USER)
    void test_get_all_parcels_not_empty() throws Exception {
        String address = "13 rue de Bonheur 99000 Ailleurs";
        String body = """
                {
                    "address": "%s",
                    "weight": %f
                }
                """.formatted(address, 3.14);
        mvc.perform(post("/parcels")
                        .with(userRole(Role.POSTMAN))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isCreated());
        mvc.perform(get("/parcels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", not(empty())));
    }

    @Test
    @WithMockUserRole(Role.USER)
    void test_get_all_parcels_pagination() throws Exception {
        String address = "13 rue de Bonheur 99000 Ailleurs";
        String body = """
                {
                    "address": "%s",
                    "weight": %f
                }
                """.formatted(address, 3.14);
        for (int i = 0; i < 7; i++) {
            mvc.perform(post("/parcels")
                            .with(userRole(Role.POSTMAN))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body))
                    .andExpect(status().isCreated());
        }
        mvc.perform(get("/parcels")
                        .param("page", "0")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", hasSize(3)))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(3)))
                .andExpect(jsonPath("$.totalPages", greaterThanOrEqualTo(3)));
        mvc.perform(get("/parcels")
                        .param("page", "1")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", hasSize(3)))
                .andExpect(jsonPath("$.page", is(1)))
                .andExpect(jsonPath("$.size", is(3)))
                .andExpect(jsonPath("$.totalPages", greaterThanOrEqualTo(3)));
        mvc.perform(get("/parcels")
                        .param("page", "2")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", not(empty())))
                .andExpect(jsonPath("$.page", is(2)))
                .andExpect(jsonPath("$.totalPages", greaterThanOrEqualTo(3)));
    }

    @Test
    @WithMockUserRole(Role.USER)
    void test_get_all_parcels_pagination_defaults() throws Exception {
        String address = "13 rue de Bonheur 99000 Ailleurs";
        String body = """
                {
                    "address": "%s",
                    "weight": %f
                }
                """.formatted(address, 3.14);
        for (int i = 0; i < 7; i++) {
            mvc.perform(post("/parcels")
                            .with(userRole(Role.POSTMAN))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body))
                    .andExpect(status().isCreated());
        }
        mvc.perform(get("/parcels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", hasSize(greaterThanOrEqualTo(7))))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", greaterThanOrEqualTo(7)));
    }

    @Test
    void test_get_all_parcels_unauthorized() throws Exception {
        mvc.perform(get("/parcels"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_create_parcel() throws Exception {
        String address = "13 rue de Bonheur 99000 Ailleurs";
        String body = """
                {
                    "address": "%s",
                    "weight": %f
                }
                """.formatted(address, 3.14);
        String responseStr = mvc.perform(post("/parcels")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        record WithId(String id) {}
        WithId withId = objectMapper.readValue(responseStr, WithId.class);
        String id = withId.id();
        mvc.perform(get("/parcels/%s".formatted(id)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_create_parcel_bad_address() throws Exception {
        String body = """
                {
                    "address": "",
                    "weight": 3.14
                }
                """;
        mvc.perform(post("/parcels")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUserRole(Role.USER)
    void test_create_parcel_forbidden() throws Exception {
        String address = "13 rue de Bonheur 99000 Ailleurs";
        String body = """
                {
                    "address": "%s",
                    "weight": %f
                }
                """.formatted(address, 3.14);
        mvc.perform(post("/parcels")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUserRole(Role.USER)
    void test_get_parcel() throws Exception {
        String address = "13 rue de Bonheur 99000 Ailleurs";
        double weight = 3.14;
        String body = """
                {
                    "address": "%s",
                    "weight": %f
                }
                """.formatted(address, weight);
        String responseStr = mvc.perform(post("/parcels")
                        .with(userRole(Role.POSTMAN))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        record WithId(String id) {}
        WithId withId = objectMapper.readValue(responseStr, WithId.class);
        String id = withId.id();
        mvc.perform(get("/parcels/%s".formatted(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.weight").value(weight));
    }

    @Test
    @WithMockUserRole(Role.USER)
    void test_get_parcel_not_found() throws Exception {
        mvc.perform(get("/parcels/not_an_id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_get_parcel_unauthorized() throws Exception {
        mvc.perform(get("/parcels/whatever"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_delete_parcel() throws Exception {
        String address = "13 rue de Bonheur 99000 Ailleurs";
        String body = """
                {
                    "address": "%s",
                    "weight": %f
                }
                """.formatted(address, 3.14);
        String responseStr = mvc.perform(post("/parcels")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        record WithId(String id) {}
        String id = objectMapper.readValue(responseStr, WithId.class).id;
        mvc.perform(delete("/parcels/%s".formatted(id)))
                .andExpect(status().isNoContent());
        mvc.perform(get("/parcels/%s".formatted(id)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_delete_parcel_not_found() throws Exception {
        mvc.perform(delete("/parcels/not_an_id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUserRole(Role.USER)
    void test_delete_parcel_forbidden() throws Exception {
        mvc.perform(delete("/parcels/whatever"))
                .andExpect(status().isForbidden());
    }

    @Test
    void test_delete_parcel_unauthorized() throws Exception {
        mvc.perform(delete("/parcels/whatever"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_update_parcel() throws Exception {
        String address = "13 rue de Bonheur 99000 Ailleurs";
        double weight = 3.14;
        String create = """
                {
                    "address": "%s",
                    "weight": %f
                }
                """.formatted(address, weight);
        String responseStr = mvc.perform(post("/parcels")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(create))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        record WithId(String id) {}
        String id = objectMapper.readValue(responseStr, WithId.class).id;
        String update = """
                {
                    "status": "TRANSIT"
                }
                """;
        mvc.perform(patch("/parcels/%s".formatted(id))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(update))
                .andExpect(status().isOk());
        mvc.perform(get("/parcels/%s".formatted(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.weight").value(weight))
                .andExpect(jsonPath("$.status").value("TRANSIT"));
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_update_parcel_modifying_postman() throws Exception {
        // Given
        String address = "13 rue de Bonheur 99000 Ailleurs";
        double weight = 3.14;
        String create = """
                {
                    "address": "%s",
                    "weight": %f
                }
                """.formatted(address, weight);
        String responseStr = mvc.perform(post("/parcels")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(create))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        record WithId(String id) {}
        String id = objectMapper.readValue(responseStr, WithId.class).id;
        String username = "user_%s".formatted(UUID.randomUUID().toString().replaceAll("-", ""));
        String user = """
                {
                    "username": "%s",
                    "password": "pa55w0rd",
                    "role": "POSTMAN"
                }
                """.formatted(username);
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(userRole(Role.ADMIN))
                        .content(user))
                .andExpect(status().isCreated());

        // When
        String update = """
                {
                    "postmanId": "%s"
                }
                """.formatted(username);
        mvc.perform(patch("/parcels/%s".formatted(id))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(update))
                .andExpect(status().isOk());
        // Then
        mvc.perform(get("/parcels/%s".formatted(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.weight").value(weight))
                .andExpect(jsonPath("$.postmanId").value(username));
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_update_parcel_modifying_postman_non_existing() throws Exception {
        // Given
        String address = "13 rue de Bonheur 99000 Ailleurs";
        double weight = 3.14;
        String create = """
                {
                    "address": "%s",
                    "weight": %f
                }
                """.formatted(address, weight);
        String responseStr = mvc.perform(post("/parcels")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(create))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        record WithId(String id) {}
        String id = objectMapper.readValue(responseStr, WithId.class).id;
        String username = UUID.randomUUID().toString().replaceAll("-", "");
        // When
        String update = """
                {
                    "postmanId": "%s"
                }
                """.formatted(username);
        mvc.perform(patch("/parcels/%s".formatted(id))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(update))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_update_parcel_bad_address() throws Exception {
        String body = """
                {
                    "address": ""
                }
                """;
        mvc.perform(patch("/parcels/an_id")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_update_parcel_bad_parcel_status() throws Exception {
        String address = "13 rue de Bonheur 99000 Ailleurs";
        double weight = 3.14;
        String create = """
                {
                    "address": "%s",
                    "weight": %f
                }
                """.formatted(address, weight);
        String responseStr = mvc.perform(post("/parcels")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(create))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        record WithId(String id) {}
        String id = objectMapper.readValue(responseStr, WithId.class).id;
        String update = """
                {
                    "status": "RETURNED"
                }
                """;
        mvc.perform(patch("/parcels/%s".formatted(id))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(update))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUserRole(Role.POSTMAN)
    void test_update_parcel_not_found() throws Exception {
        String create = """
                {
                    "address": "3 rue de Bonheur 99000 Ailleurs",
                    "weight": 3.14
                }
                """;
        mvc.perform(patch("/parcels/not_an_id")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(create))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUserRole(Role.USER)
    void test_update_parcel_forbidden() throws Exception {
        mvc.perform(patch("/parcels/whatever"))
                .andExpect(status().isForbidden());
    }

    @Test
    void test_update_parcel_unauthorized() throws Exception {
        mvc.perform(patch("/parcels/whatever"))
                .andExpect(status().isUnauthorized());
    }
}
