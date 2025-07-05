package e_commerce.monolithic.controller.user;

import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
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
@RequestMapping("/api/users/products")
public class UserProductController {
    private final UserProductService userProductService;

    public UserProductController(UserProductService userProductService) {
        this.userProductService = userProductService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllVisibleProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,

            // Tham số phân trang
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,

            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                Sort.Direction direction = _sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
                orders.add(new Sort.Order(direction, _sort[0]));
            }
        } else {
            Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            orders.add(new Sort.Order(direction, sort[0]));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        Page<ProductResponseDTO> productResponseDTOS = userProductService.findAllVisibleProducts(name, minPrice,
                maxPrice, categoryId, pageable);

        return ResponseEntity.ok(productResponseDTOS);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable("id") long id) {
        ProductResponseDTO productResponseDTO = userProductService.findVisibleProductById(id);
        return ResponseEntity.ok(productResponseDTO);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponseDTO>> getVisibleProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                Sort.Direction direction = _sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
                orders.add(new Sort.Order(direction, _sort[0]));
            }
        } else {
            Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            orders.add(new Sort.Order(direction, sort[0]));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<ProductResponseDTO> products = userProductService.findVisibleProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }
}
