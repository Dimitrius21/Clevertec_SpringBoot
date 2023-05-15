package ru.clevertec.ecl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMapAdapter;
import org.hamcrest.core.IsNot;
import ru.clevertec.ecl.entity.CertificateTestBuilder;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CertificateControllerIntegrationTest extends AbstractContainerBaseTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void findAllCertificateTest() throws Exception {
        MultiValueMapAdapter<String, String> params = new MultiValueMapAdapter<>(new HashMap<>());
        params.setAll(Map.of("page", "1", "size", "3", "sort", "name-asc"));

        mockMvc.perform(get("/cert")
                        .queryParams(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("[0].name").value("Quest Zone2"))
                .andExpect(jsonPath("[1].name").value("Rest1"))
                .andExpect(jsonPath("[2].name").value("Rest2"));
    }

    @Test
    public void findCertificateTest() throws Exception {
        long id = 2;
        mockMvc.perform(
                        get("/cert/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rest2"));
    }

    @Test
    public void findCertificateNotFoundTest() throws Exception {
        long id = 10000;
        mockMvc.perform(
                        get("/cert/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(40402))
                .andExpect(jsonPath("$.errorMessage").value(StringEndsWith.endsWith(String.valueOf(id))));
    }

    @Test
    public void saveCertificateTest() throws Exception {
        Tag tag1 = new Tag(0, "testTag");
        Tag tag2 = new Tag(1, "relax");
        CertificateTestBuilder certBuilder = CertificateTestBuilder.aCertificate();
        GiftCertificate cert = certBuilder
                .name("TestCertificate")
                .description("Test")
                .duration(10)
                .price(100)
                .addTag(tag1)
                .addTag(tag2)
                .build();

        Matcher<Long> matcherNotZero = IsNot.not(0L);
        Matcher matcherNotNull = IsNull.notNullValue();

        mockMvc.perform(
                        post("/cert")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(cert)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(matcherNotZero))
                .andExpect(jsonPath("$.name").value(cert.getName()))
                .andExpect(jsonPath("$.description").value(cert.getDescription()))
                .andExpect(jsonPath("$.duration").value(cert.getDuration()))
                .andExpect(jsonPath("$.price").value(cert.getPrice()))
                .andExpect(jsonPath("$.createDate").value(matcherNotNull))
                .andExpect(jsonPath("$.tags.length()").value(2));
    }

    @Test
    public void saveCertificateWithoutBodyTest() throws Exception {
        mockMvc.perform(
                post("/cert")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Data for save is absent"))
                .andExpect(jsonPath("$.errorCode").value(40002));
    }

    @Test
    public void updateCertFieldsShortTest() throws Exception {
        CertificateTestBuilder certBuilder = CertificateTestBuilder.aCertificate();
        GiftCertificate cert = certBuilder
                .id(1)
                .description("sauna+gym")
                .price(2000)
                .build();
        Matcher<String> matcherContainDate = StringContains.containsString(LocalDate.now().toString());

        mockMvc.perform(
                        patch("/cert")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(cert)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(cert.getDescription()))
                .andExpect(jsonPath("$.price").value(cert.getPrice()))
                .andExpect(jsonPath("$.name").value("Rest1"))
                .andExpect(jsonPath("$.duration").value(10))
                .andExpect(jsonPath("$.lastUpdateDate").value(matcherContainDate))
                .andExpect(jsonPath("$.tags.length()").value(1));
    }

    @Test
    public void updateCertFieldsFullTest() throws Exception {
        Tag tag1 = new Tag(0, "testTagUpdate");
        Tag tag2 = new Tag(4, "motor");
        CertificateTestBuilder certBuilder = CertificateTestBuilder.aCertificate();
        GiftCertificate cert = certBuilder
                .id(7)
                .name("Quad bike test")
                .description("Road 10km, 1 person test")
                .price(7100)
                .duration(35)
                .addTag(tag1)
                .addTag(tag2)
                .build();
        Matcher<String> matcherContainDate = StringContains.containsString(LocalDate.now().toString());

        mockMvc.perform(
                        patch("/cert")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(cert)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cert.getId()))
                .andExpect(jsonPath("$.id").value(cert.getId()))
                .andExpect(jsonPath("$.name").value(cert.getName()))
                .andExpect(jsonPath("$.description").value(cert.getDescription()))
                .andExpect(jsonPath("$.duration").value(cert.getDuration()))
                .andExpect(jsonPath("$.price").value(cert.getPrice()))
                .andExpect(jsonPath("$.lastUpdateDate").value(matcherContainDate))
                .andExpect(jsonPath("$.tags.length()").value(2));
    }
    @Test
    public void deleteTag() throws Exception {
        long id = 8;
        mockMvc.perform(
                        delete("/cert/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(id)));
        mockMvc.perform(
                        get("/cert/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAllCertificateWithSearchTest() throws Exception {
        MultiValueMapAdapter<String, String> params = new MultiValueMapAdapter<>(new HashMap<>());
        params.setAll(Map.of("field", "description", "text", "spa", "sort", "name-desc"));

        mockMvc.perform(
                        get("/cert/has")
                                .queryParams(params))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("[0].name").value("Rest3"))
                .andExpect(jsonPath("[1].name").value("Rest2"));
    }

    @Test
    public void findCertificateWithTagTest() throws Exception {
        MultiValueMapAdapter<String, String> params = new MultiValueMapAdapter<>(new HashMap<>());
        params.setAll(Map.of("sort", "name-desc"));
        String name = "quiz";

        mockMvc.perform(
                        get("/cert/tag/{name}", name)
                                .queryParams(params))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("[0].name").value("Quest Zone2"))
                .andExpect(jsonPath("[1].name").value("Quest Zone1"));
    }

}
