package br.com.calendar.category;

import br.com.calendar.category.dto.CategoryResponseDTO;
import br.com.calendar.category.dto.CreateCategoryDTO;
import br.com.calendar.user.User;
import br.com.calendar.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper,
                           UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public CategoryResponseDTO createCategory(String userId, CreateCategoryDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with ID: " + userId));

        Category category = new Category();
        category.setUser(user);
        category.setTitle(dto.title());
        category.setColor(dto.color());
        category.setIcon(dto.icon());

        return categoryMapper.toResponse(categoryRepository.save(category));
    }
}
