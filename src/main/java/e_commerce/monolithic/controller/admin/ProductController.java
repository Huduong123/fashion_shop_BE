package e_commerce.monolithic.controller.admin;

import e_commerce.monolithic.dto.admin.product.ProductCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.service.admin.ProductService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProdcut(@RequestParam(required = false) String name,
                                                                  @RequestParam(required = false)BigDecimal minPrice,
                                                                  @RequestParam(required = false) BigDecimal maxPrice,
                                                                  @RequestParam(required = false) Integer minQuantity,
                                                                  @RequestParam(required = false) Integer maxQuantity,
                                                                  @RequestParam(required = false) Boolean enabled,
                                                                  @RequestParam(required = false) Long categoryId,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate createdAt,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate updatedAt) {

        List<ProductResponseDTO> products = productService.findAll(name, minPrice, maxPrice,minQuantity,maxQuantity,enabled,categoryId,createdAt,updatedAt);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable("id") long id){
        ProductResponseDTO product = productService.findById(id);

        return ResponseEntity.ok(product);
    }


    @PostMapping()
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO){
        ProductResponseDTO productResponseDTO = productService.createProduct(productCreateDTO);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long productId,
                                                            @Valid @RequestBody ProductUpdateDTO productUpdateDTO){
        ProductResponseDTO productResponseDTO = productService.updateProduct(productId, productUpdateDTO);
        return  ResponseEntity.ok(productResponseDTO);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseMessageDTO> deleteProduct(@PathVariable long productId){
        ResponseMessageDTO responseMessageDTO = productService.deleteProduct(productId);

        return ResponseEntity.ok(responseMessageDTO);
    }
}
