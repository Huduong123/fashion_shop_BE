package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.product.ProductCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.Product;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.ProductMapper;
import e_commerce.monolithic.repository.CategoryRepository;
import e_commerce.monolithic.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements  ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    public ProductServiceImp(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO findById(Long productId) {
        Product product = findProductById(productId);
        return productMapper.convertToDTO(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO) {

        //gọi hàm check name
        checkProductNameExists(productCreateDTO.getName());

        Category category = findCategoryById(productCreateDTO.getCategoryId());

        Product newProduct = productMapper.convertCreateDtoToEntity(productCreateDTO, category);

        Product savedProduct = productRepository.save(newProduct);

        return productMapper.convertToDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long productId, ProductUpdateDTO productUpdateDTO) {

        Product existingProduct = findProductById(productId);

        checkProductNameExistsForUpdate(productUpdateDTO.getName(), productId);

        // tìm danh mục theo categoryId
        Category category = findCategoryById(productUpdateDTO.getCategoryId());

        // chuyển DTO sang entiy
        productMapper.convertUpdateDtoToEntity(productUpdateDTO, existingProduct, category);

        //lưu
        Product updatedProduct = productRepository.save(existingProduct);

        return productMapper.convertToDTO(updatedProduct);
    }

    @Override
    @Transactional
    public ResponseMessageDTO deleteProduct(Long productId) {
        checkProductExists(productId);

        productRepository.deleteById(productId);

        return  new ResponseMessageDTO(HttpStatus.OK, "Xóa sản phẩm với ID: " + productId + " thành công");

    }

    // hàm
    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + productId));
    }

    private Category findCategoryById(Long categoryId) {
        return  categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + categoryId));
    }
    private void checkProductNameExists(String productName) {
        if (productRepository.existsByName(productName)) {
            throw new IllegalArgumentException("Tên sản phẩm đã tồn tại: " + productName );
        }
    }

    private void checkProductNameExistsForUpdate(String productName, Long productId) {
        if(productRepository.existsByNameAndIdNot(productName, productId)) {
            throw new IllegalArgumentException("Tên sản phẩm '" + productName + "' đã tồn tại cho một sản phẩm khác");
        }
    }

    private void checkProductExists(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw  new NotFoundException(" Không tìm thấy sản phẩm với ID: " + productId);
        }
    }
}
