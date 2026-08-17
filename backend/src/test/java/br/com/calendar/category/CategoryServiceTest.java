package br.com.calendar.category;

import br.com.calendar.category.dto.CategoryResponseDTO;
import br.com.calendar.category.dto.CreateCategoryDTO;
import br.com.calendar.user.User;
import br.com.calendar.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    private static final String USER_ID = "usr_abc123";

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private UserRepository userRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository, categoryMapper, userRepository);
    }

    @Test
    void createsCategoryAssociatedWithTheAuthenticatedUser() {
        User user = new User();
        user.setId(USER_ID);
        CreateCategoryDTO request = new CreateCategoryDTO("Work", "3366FF", "briefcase");
        Category savedCategory = new Category();
        savedCategory.setId("cat_123");
        CategoryResponseDTO expected = new CategoryResponseDTO(
                "cat_123", "Work", "3366FF", "briefcase");

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        when(categoryMapper.toResponse(savedCategory)).thenReturn(expected);

        CategoryResponseDTO response = categoryService.createCategory(USER_ID, request);

        assertEquals(expected, response);
        verify(categoryRepository).save(argThat(category ->
                category.getUser() == user
                        && "Work".equals(category.getTitle())
                        && "3366FF".equals(category.getColor())
                        && "briefcase".equals(category.getIcon())));
    }
}
