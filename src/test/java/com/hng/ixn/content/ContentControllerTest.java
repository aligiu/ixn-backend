package com.hng.ixn.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ContentControllerTest {

    @Mock
    private ContentService contentService;

    @InjectMocks
    private ContentController contentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contentController).build();
    }

    @Test
    void getAllContent() throws Exception {
        // Create a mock Content object with a set ID
        Content content = new Content();
        content.setId(1); // Ensure ID is set
        List<Content> contents = new ArrayList<>();
        contents.add(content);

        when(contentService.getAllContent()).thenReturn(contents);

        mockMvc.perform(get("/api/content")).andExpect(status().isOk()).andExpect(
                jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(1))); // Check ID field
    }

    @Test
    void getLatestContent() throws Exception {
        // Create a mock Content object with a set ID
        Content content = new Content();
        content.setId(1); // Ensure ID is set
        List<Content> latestContents = new ArrayList<>();
        latestContents.add(content);

        when(contentService.getLatestContent()).thenReturn(latestContents);

        mockMvc.perform(get("/api/content/latest")).andExpect(status().isOk()).andExpect(
                jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(1))); // Check ID field
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createContent() throws Exception {
        // Create and setup mock ContentDTO and Content objects
        ContentDTO contentDTO = new ContentDTO();
        // Setup fields in ContentDTO if needed

        Content content = new Content();
        content.setId(1); // Ensure ID is set
        List<ContentDTO> contentDTOs = new ArrayList<>();
        contentDTOs.add(contentDTO);

        List<Content> createdContents = new ArrayList<>();
        createdContents.add(content);

        when(contentService.saveMultipleContent(anyList())).thenReturn(createdContents);

        mockMvc.perform(post("/api/content").contentType("application/json")
                        .content("[{\"id\":1}]")) // Ensure this JSON structure matches ContentDTO
                .andExpect(status().isCreated()).andExpect(jsonPath("$", hasSize(1))).andExpect(
                        jsonPath("$[0].id", is(1))); // Check ID field
    }

}
