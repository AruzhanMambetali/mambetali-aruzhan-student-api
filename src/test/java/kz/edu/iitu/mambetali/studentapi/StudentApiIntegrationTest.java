package kz.edu.iitu.mambetali.studentapi;

import kz.edu.iitu.mambetali.studentapi.student.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentApiIntegrationTest {
    private static final String STUDENT = """
            {"fullName":"Mambetali Aruzhan Imanaliyevna",
             "email":"aruzhan@example.com",
             "program":"Software Engineering", "year":4}
            """;

    @Autowired MockMvc mvc;
    @Autowired StudentRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    private long createStudent() throws Exception {
        var response = mvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON).content(STUDENT))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Mambetali Aruzhan Imanaliyevna"))
                .andReturn();
        return ((Number) com.jayway.jsonpath.JsonPath.read(response.getResponse().getContentAsString(), "$.id")).longValue();
    }

    @Test
    void createReturns201AndStoresStudent() throws Exception {
        long id = createStudent();
        mvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("aruzhan@example.com"));
    }

    @Test
    void listReturnsStudents() throws Exception {
        createStudent();
        mvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].program").value("Software Engineering"));
    }

    @Test
    void updateChangesExistingStudent() throws Exception {
        long id = createStudent();
        var updated = """
                {"fullName":"Mambetali Aruzhan Imanaliyevna",
                 "email":"updated@example.com",
                 "program":"Backend Development", "year":4}
                """;
        mvc.perform(put("/api/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON).content(updated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.program").value("Backend Development"));
    }

    @Test
    void deleteReturns204AndFollowingGetReturns404() throws Exception {
        long id = createStudent();
        mvc.perform(delete("/api/students/{id}", id))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void missingStudentReturns404() throws Exception {
        mvc.perform(get("/api/students/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Student with id 99999 was not found"));
    }

    @Test
    void invalidEmailReturns400() throws Exception {
        var invalid = """
                {"fullName":"Aruzhan Mambetali",
                 "email":"bad-email", "program":"Software Engineering", "year":4}
                """;
        mvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON).content(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.email").exists());
    }

    @Test
    void healthEndpointReturnsUp() throws Exception {
        mvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
