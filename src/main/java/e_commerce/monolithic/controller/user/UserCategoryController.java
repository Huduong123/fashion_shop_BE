package e_commerce.monolithic.controller.user;

import e_commerce.monolithic.dto.admin.category.CategoryResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
import e_commerce.monolithic.service.user.UserCategoryService;
import e_commerce.monolithic.service.user.UserProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users/categories")
public class UserCategoryController {

    private final UserProductService userProductService;
    private final UserCategoryService userCategoryService;

    public UserCategoryController(UserProductService userProductService, UserCategoryService userCategoryService) {
        this.userProductService = userProductService;
        this.userCategoryService = userCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categoryResponseDTOS = userCategoryService.findAll();
        return ResponseEntity.ok(categoryResponseDTOS);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(required = false)BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt, desc") String[] sort
            ){
        List<Sort.Order> orders = new ArrayList<>();
        if (sort.length > 0 && sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                Sort.Direction direction = _sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
                orders.add(new Sort.Order(direction, _sort[0]));
            }
        }else {
            orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        Page<ProductResponseDTO> productResponseDTOS = userProductService.findAllVisibleProducts(null, minPrice,maxPrice,categoryId,pageable);

        return ResponseEntity.ok(productResponseDTOS);

    }
}
