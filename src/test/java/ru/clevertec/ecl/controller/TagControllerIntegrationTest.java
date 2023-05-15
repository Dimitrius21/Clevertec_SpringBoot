package ru.clevertec.ecl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMapAdapter;
import org.hamcrest.core.IsNot;
import ru.clevertec.ecl.entity.Tag;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TagControllerIntegrationTest extends AbstractContainerBaseTest{

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllTest() throws Exception {
        MultiValueMapAdapter params = new MultiValueMapAdapter<>(new HashMap<>());
        params.setAll(Map.of("page", "0", "size", "3", "sort", "name-asc"));

        mockMvc.perform(get("/tag")
                        .queryParams(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("[0].name").value("motor"))
                .andExpect(jsonPath("[1].name").value("quiz"))
                .andExpect(jsonPath("[2].name").value("relax"));
    }

    @Test
    public void findTagByIdTest() throws Exception {
        Tag tag = new Tag(4, "motor");
        long id = tag.getId();

        mockMvc.perform(
                        get("/tag/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tag)));
    }

    @Test
    public void findTagByIdNotFoundTest() throws Exception {
        long id = 1000;

        mockMvc.perform(
                        get("/tag/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(40401))
                .andExpect(jsonPath("$.errorMessage").value(StringEndsWith.endsWith(String.valueOf(id))));
    }

    @Test
    public void createTagTest() throws Exception {
        Tag tag = new Tag(0, "water");
        Matcher<Long> matcher = IsNot.not(0L);

        mockMvc.perform(
                        post("/tag")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(tag)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(matcher))
                .andExpect(jsonPath("$.name").value("water"));
    }

    @Test
    public void updateTagTest() throws Exception {
        Tag tag = new Tag(2, "sport!!!");
        long id = tag.getId();

        mockMvc.perform(
                        put("/tag")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(tag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tag.getId()))
                .andExpect(jsonPath("$.name").value(tag.getName()));
    }

    @Test
    public void deleteTag() throws Exception {
        long id = 5;
        mockMvc.perform(
                        delete("/tag/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(id)));
        mockMvc.perform(
                        get("/tag/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getMostUsedTagInOrderTest() throws Exception {
        long id = 2;
        mockMvc.perform(
                        get("/tag/popular/user/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("relax"));
    }

}
