package com.collab.version;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.collab.version.model.Version;
import com.collab.version.service.VersionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
class VersionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VersionService versionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveVersion_shouldReturnSaved() throws Exception {
        Version v = new Version();
        v.setId(1L);
        v.setDocId(10L);
        v.setEditorName("alice");
        v.setContent("Hello");
        v.setTime(LocalDateTime.now());
        when(versionService.saveVersion(any(Version.class))).thenReturn(v);
        mockMvc.perform(post("/api/versions/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(v)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.editorName").value("alice"));
    }

    @Test
    void history_shouldReturnList() throws Exception {
        Version v = new Version();
        v.setId(2L); v.setDocId(11L); v.setEditorName("bob"); v.setContent("X"); v.setTime(LocalDateTime.now());
        when(versionService.getHistory(11L)).thenReturn(List.of(v));
        mockMvc.perform(get("/api/versions/history/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].editorName").value("bob"));
    }
}
