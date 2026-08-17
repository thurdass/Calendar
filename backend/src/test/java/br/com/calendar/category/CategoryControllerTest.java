package br.com.calendar.category;

import br.com.calendar.category.dto.CategoryResponseDTO;
import br.com.calendar.category.dto.CreateCategoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private static final String USER_ID = "usr_abc123";

    @Mock
    private CategoryService categoryService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CategoryController(categoryService))
                .build();
    }

    @Test
    void createsCategoryForTheAuthenticatedUser() throws Exception {
        CreateCategoryDTO request = new CreateCategoryDTO("Work", "3366FF", "briefcase");
        when(categoryService.createCategory(USER_ID, request))
                .thenReturn(new CategoryResponseDTO("cat_123", "Work", "3366FF", "briefcase"));

        mockMvc.perform(post("/categories")
                        .principal(new UsernamePasswordAuthenticationToken(USER_ID, null))
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Work",
                                  "color": "3366FF",
                                  "icon": "briefcase"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("cat_123"))
                .andExpect(jsonPath("$.title").value("Work"))
                .andExpect(jsonPath("$.color").value("3366FF"))
                .andExpect(jsonPath("$.icon").value("briefcase"));

        verify(categoryService).createCategory(USER_ID, request);
    }
}
